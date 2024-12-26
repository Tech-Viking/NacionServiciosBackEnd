package sube.interviews.mareoenvios.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.repository.ShippingRepository;


@Service
public class ShippingService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingService.class);
    private final ShippingRepository shippingRepository;
   

    public ShippingService(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
   
    }
    


    public Optional<Shipping> getShippingById(Integer shippingId) {
        try {
            Optional<Shipping> shipping = shippingRepository.findById(shippingId);
            if (shipping.isPresent()) {
                return shipping;
            } else {
                logger.error("Envío no encontrado con el id " + shippingId);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error al intentar obtener el envío con el id " + shippingId, e);
            return Optional.empty();
        }
    }



    public void updateShipping(Integer shippingId, String newState) {
        try {
            Shipping shipping = shippingRepository.findById(shippingId).orElseThrow(() -> {
                logger.error("Shipping not found with id: " + shippingId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipping not found with id: " + shippingId);
            });
            if (isValidTransition(shipping.getState(), newState)) {
               
                applyDelay(newState);
                shipping.setState(newState);
                shippingRepository.save(shipping);


            } else {
                logger.error("Transición de estado invalida de " + shipping.getState() + " a " + newState);
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Transición de estado inválida de " + shipping.getState() + " a " + newState);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
		logger.error("Error al actualizar el estado del envío", e);
		
		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
				"Error al actualizar el estado del envío: " + e.getMessage());
        }
    }


    private void applyDelay(String transition) {
       try {
            switch (transition) {
             case "sendToMail":
                  logger.info("Applying delay for sendToMail transition (1 second).");
                 Thread.sleep(1000);
                 break;
             case "inTravel":
                  logger.info("Applying delay for inTravel transition (3 seconds).");
                 Thread.sleep(3000);
                 break;
             case "delivered":
                  logger.info("Applying delay for delivered transition (5 seconds).");
                 Thread.sleep(5000);
                  break;
             case "cancelled":
                  logger.info("Applying delay for cancelled transition (3 seconds).");
                 Thread.sleep(3000);
                 break;
               default:
                    logger.info("No delay applied for transition: " + transition);
                 break;
             }
       } catch (InterruptedException e) {
            logger.error("Delay interrupted for transition: " + transition, e);
             Thread.currentThread().interrupt();
          }

    }

 private boolean isValidTransition(String currentState, String newState) {
    if (currentState.equals(newState)) {
        return false;
    }
    switch (currentState) {
        case "Inicial":
            return newState.equals("Entregado al correo") || newState.equals("Cancelado");
        case "Entregado al correo":
            return newState.equals("En camino") || newState.equals("Cancelado");
        case "En camino":
            return newState.equals("Entregado") || newState.equals("Cancelado");
        case "Entregado":
        case "Cancelado":
            return false;
        default:
            return false;
    }
}
}