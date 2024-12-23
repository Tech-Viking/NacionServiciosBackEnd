package sube.interviews.mareoenvios.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskProcessorConnector {
	
	@Value("${app.task-processor.url}")
    private String taskServiceUrl;

    private final RestTemplate restTemplate;

    public TaskProcessorConnector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getTask(String taskId) {
        String url = taskServiceUrl + "/status" + taskId;
        return restTemplate.getForObject(url, String.class);
    }

}
