package sube.interviews.mareoenvios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sube.interviews.mareoenvios.model.TaskRequest;
import sube.interviews.mareoenvios.model.TaskStatusResponse;
import sube.interviews.mareoenvios.model.TopProductResponse;
import sube.interviews.mareoenvios.service.ReportService;
import sube.interviews.mareoenvios.service.TaskService;

import java.util.List;


@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
     private final ReportService reportService;


    public TaskController(TaskService taskService, ReportService reportService) {
        this.taskService = taskService;
        this.reportService = reportService;
    }

    @PostMapping("/tarea-concurrente")
    public ResponseEntity<String> processTasks(@RequestBody TaskRequest request) {
        taskService.processTasks(request);
        return ResponseEntity.ok("Tasks are being processed");
    }


    @GetMapping("/status")
    public ResponseEntity<TaskStatusResponse> getStatus() {
        return ResponseEntity.ok(taskService.getStatus());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

   @GetMapping("/reports/top-sent")
   public ResponseEntity<List<TopProductResponse>> getTopSentProducts() {
       return ResponseEntity.ok(reportService.getTopSentProducts());
   }

}