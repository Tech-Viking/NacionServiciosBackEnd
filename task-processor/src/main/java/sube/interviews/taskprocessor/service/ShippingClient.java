package sube.interviews.taskprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import sube.interviews.taskprocessor.entity.Shipping;
import sube.interviews.taskprocessor.model.TransitionRequest;
import static sube.interviews.taskprocessor.util.TaskConstants.*;


@Component
public class ShippingClient {

   private static final Logger logger = LoggerFactory.getLogger(ShippingClient.class);
   private final RestTemplate restTemplate;
   
   @Value("${app.mareoenvios.url}")
   private String mareoEnviosUrl;


    public ShippingClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendCommand(Shipping shipping, String transition) {
    	String url = mareoEnviosUrl + "/shippings/" + shipping.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

       TransitionRequest requestBody = new TransitionRequest();
       requestBody.setTransition(transition); 
        HttpEntity<TransitionRequest> entity = new HttpEntity<>(requestBody, headers);
         
        try {
             logger.info("Sending PATCH request to {}, payload: {}", url, requestBody);
            restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
        } catch (Exception e) {
             logger.error("Error sending PATCH request to {}: {}", url, e.getMessage(), e);
            throw e;
        }
    }


     private String getTransitionPayload(String transition) {
            return String.format("{ \"transition\": \"%s\" }", transition);
        }


   private void applyDelay(String transition) {
      try {
           switch (transition) {
            case TRANSITION_SEND_TO_MAIL:
                 logger.info("Applying delay for sendToMail transition (1 second).");
                Thread.sleep(1000);
                break;
            case TRANSITION_IN_TRAVEL:
                 logger.info("Applying delay for inTravel transition (3 seconds).");
                Thread.sleep(3000);
                break;
            case TRANSITION_DELIVERED:
                 logger.info("Applying delay for delivered transition (5 seconds).");
                Thread.sleep(5000);
                 break;
            case TRANSITION_CANCELLED:
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

}