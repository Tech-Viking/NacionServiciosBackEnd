package sube.interviews.mareoenvios.service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.model.Shipment;
import sube.interviews.mareoenvios.model.TaskRequest;
import sube.interviews.mareoenvios.model.TaskStatusResponse;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import sube.interviews.mareoenvios.repository.TaskRepository;

@Service
public class TaskService {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final TaskRepository taskRepository;
	private final ShippingRepository shippingRepository;
	private final ThreadPoolTaskScheduler taskScheduler;
	private final Map<Long, ReentrantLock> shippingLocks = new ConcurrentHashMap<>();
	private volatile boolean databaseInitialized = false;

	private EntityManager entityManager;
	
	 @Autowired
	 private Environment environment;

	@Autowired
	public TaskService(ShippingClient shippingClient, TaskRepository taskRepository,
			ShippingRepository shippingRepository) {
		this.taskRepository = taskRepository;
		this.shippingRepository = shippingRepository;
		this.taskScheduler = new ThreadPoolTaskScheduler();
		this.taskScheduler.initialize();
	}

	@PostConstruct
	public void init() {
		if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("local")) {

		} else {
			waitForDatabaseInitialization();
		}

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
			waitForDatabaseInitialization();
			logger.info("Received task request with {} shipments.", request.getShipments().size());

			request.getShipments().forEach(shipment -> {
				Runnable task = () -> processShipment(shipment);

				Date startTime = new Date(System.currentTimeMillis() + shipment.getStartTimeInSeconds() * 1000);
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
		logger.info("Processing shipment: {}", shipment.getShippingId());
		ReentrantLock lock = shippingLocks.computeIfAbsent(shipment.getShippingId(), k -> new ReentrantLock());

		if (lock.tryLock()) {
			try {
				Shipping shipping = shippingRepository.findById(shipment.getShippingId().intValue()).orElse(null);

				if (shipping != null) {
					processShipping(shipping);
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

	private void waitForDatabaseInitialization()  {
	     while(!databaseInitialized){
	         try{
	              logger.info("Waiting for database to be initialized");
	            entityManager.createNativeQuery("SELECT 1 FROM shipping").getSingleResult();
	             databaseInitialized = true;
	          } catch (Exception e){
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
		String currentState = shipping.getState();
		try {
			switch (currentState) {
			case "Inicial":
				shipping.setState("Entregado al correo");
				shippingRepository.save(shipping);
				taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "Entregado al correo");
				break;
			case "Entregado al correo":
				shipping.setState("En camino");
				shippingRepository.save(shipping);
				taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "En camino");
				break;
			case "En camino":
				shipping.setState("Entregado");
				shippingRepository.save(shipping);
				taskRepository.recordSuccessfulTask(shipping.getId().longValue(), "Entregado");
				break;
			default:
				logger.warn("Invalid shipping state: {}. Nothing to do.", currentState);
			}
		} catch (Exception e) {
			logger.error("Error processing transition for shipping: " + shipping.getId(), e);
			taskRepository.recordTaskError(shipping.getId().longValue(),
					"Error processing transition: " + e.getMessage());
		}
	}

	public TaskStatusResponse getStatus() {
		return taskRepository.getTaskStatus();
	}

}