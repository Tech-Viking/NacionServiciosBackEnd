package sube.interviews.taskprocessor.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import sube.interviews.taskprocessor.model.Shipment;
import sube.interviews.taskprocessor.model.TaskRequest;
import sube.interviews.taskprocessor.model.TaskStatusResponse;
import sube.interviews.taskprocessor.repository.TaskRepository;
import sube.interviews.taskprocessor.entity.Shipping;


@Service
public class TaskService {

	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final TaskRepository taskRepository;
	private final ThreadPoolTaskScheduler taskScheduler;
    private final ShippingClient shippingClient; // Nueva dependencia
	private final Map<Long, ReentrantLock> shippingLocks = new ConcurrentHashMap<>();
	private volatile boolean databaseInitialized = false;

    @Value("${app.task.timeout-seconds}")
    private int taskTimeoutSeconds;


	@Autowired
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	@Autowired
	private Environment environment;

	@Autowired
	public TaskService(ShippingClient shippingClient, TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
        this.shippingClient = shippingClient; //Asignamos la dependencia
		this.taskScheduler = new ThreadPoolTaskScheduler();
		this.taskScheduler.initialize();
	}

    @PostConstruct
    public void initialize(){
      setEntityManager();
        if (environment.getActiveProfiles().length > 0 && !environment.getActiveProfiles()[0].equals("local")) {
                waitForDatabaseInitialization();
            }
        
    }
    
	@PostConstruct
    public void setEntityManager(){
      this.entityManager = entityManagerFactory.createEntityManager();
    }

	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		logger.info("ContextRefreshedEvent received");
		if (environment.getActiveProfiles().length > 0 && !environment.getActiveProfiles()[0].equals("local")) {
			if (!databaseInitialized) {
				waitForDatabaseInitialization();
				logger.info("Database is initialized");
			}
		}
	}

	public void processTasks(TaskRequest request) {
		try {
			logger.info("Received task request with {} shipments.", request.getShipments().size());

			request.getShipments().forEach(shipment -> {
				Runnable task = () -> processShipment(shipment);

				Instant startTime = Instant.now().plus(shipment.getStartTimeInSeconds(), ChronoUnit.SECONDS);
				taskScheduler.schedule(task, startTime);
				logger.info("Scheduled shipment: {} to start in {} seconds.", shipment.getShippingId(),
						shipment.getStartTimeInSeconds());
			});

			logger.info("Finished scheduling all tasks.");
		} catch (Exception e) {
			logger.error("Error scheduling tasks: {}", e.getMessage(), e);
			Thread.currentThread().interrupt();
		}
	}

	private void processShipment(Shipment shipment) {
        Instant startTime = Instant.now();
		logger.info("Processing shipment: {}", shipment.getShippingId());
		ReentrantLock lock = shippingLocks.computeIfAbsent(shipment.getShippingId(), k -> new ReentrantLock());

		if (lock.tryLock()) {
			try {
                long elapsedTime = ChronoUnit.SECONDS.between(startTime, Instant.now());
                if (elapsedTime > taskTimeoutSeconds) {
                   logger.error("Task timed out for shipment: {}, elapsedTime: {} seconds", shipment.getShippingId(), elapsedTime);
                   taskRepository.recordTaskError(shipment.getShippingId(), "Task timed out after " + elapsedTime + " seconds");
                   return;
                }

				Shipping shipping = new Shipping();
                shipping.setId(shipment.getShippingId().intValue());


				if (shipping != null) {
                    if (shipment.isNextState()) {
                       processShipping(shipping);
                    } else {
                       cancelShipping(shipping);
                    }

				} else {
					logger.error("Shipping not found with id: {}", shipment.getShippingId());
					taskRepository.recordTaskError(shipment.getShippingId(), "Shipping not found");
				}
			} catch (Exception e) {
				logger.error("Error processing shipment: {}, error: {}", shipment.getShippingId(), e.getMessage(), e);
				taskRepository.recordTaskError(shipment.getShippingId(),
						"Error processing transition: " + e.getMessage());
			} finally {
				lock.unlock();
				logger.info("Finished processing shipment: {}", shipment.getShippingId());
			}
		} else {
			logger.warn("Concurrency conflict for shipment: {}", shipment.getShippingId());
			taskRepository.recordConcurrencyConflict(shipment.getShippingId());
		}
	}

	private void waitForDatabaseInitialization() {
		while (!databaseInitialized) {
			try {
				logger.info("Waiting for database to be initialized");
				entityManager.createNativeQuery("SELECT 1 FROM task_shipping").getSingleResult();
				databaseInitialized = true;
			} catch (Exception e) {
				logger.error("Error while waiting for database to initialize: " + e.getMessage(), e);
				taskRepository.recordTaskError(0L, "Error al inicializar la base de datos: " + e.getMessage());
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private void processShipping(Shipping shipping) {
		try {
			    shippingClient.sendCommand(shipping, "sendToMail");
                taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "Entregado al correo");
				
                shippingClient.sendCommand(shipping, "inTravel");
                 taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "En camino");
				
                shippingClient.sendCommand(shipping, "delivered");
                 taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "Entregado");
				
		} catch (Exception e) {
			logger.error("Error processing transition for shipping: " + shipping.getId(), e);
			taskRepository.recordTaskError(shipping.getId().longValue(),
					"Error processing transition: " + e.getMessage());
		}
	}


     private void cancelShipping(Shipping shipping) {
		try {
             shippingClient.sendCommand(shipping, "cancelled");
             taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "Cancelado");
		} catch (Exception e) {
			logger.error("Error processing cancelation for shipping: " + shipping.getId(), e);
			taskRepository.recordTaskError(shipping.getId().longValue(),
					"Error processing cancelation: " + e.getMessage());
		}
	}

	public TaskStatusResponse getStatus() {
		return taskRepository.getTaskStatus();
	}
}