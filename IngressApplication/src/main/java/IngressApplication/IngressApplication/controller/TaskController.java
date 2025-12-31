package IngressApplication.IngressApplication.controller;

import IngressApplication.IngressApplication.model.Task;
import IngressApplication.IngressApplication.service.QueueService;
import IngressApplication.IngressApplication.utils.Exceptions.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ingress")
public class TaskController {
    private final QueueService queueService;

    public TaskController(QueueService queueService){
        this.queueService = queueService;
    }

    @RequestMapping("/request")
    @PostMapping
    public ResponseEntity<?> enqueueTask(@Validated @RequestBody Task task){

        queueService.enqueueTask(task);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new CustomResponse("Task enqueued successfully",
                        HttpStatus.ACCEPTED.value(), new Object[1]));
    }

    @RequestMapping("/request/claim")
    @GetMapping
    public ResponseEntity<?> claimTask(){
        Task task = queueService.dequeTask();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomResponse("Task dequeued successfully",
                        HttpStatus.OK.value(), new Object[]{task}));
    }

    @RequestMapping("/echo")
    @GetMapping
    public ResponseEntity<?> echo(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new CustomResponse("Echoed successfully",
                                HttpStatus.OK.value(), new Object[0])
                );
    }
}
