package IngressApplication.IngressApplication.controller;

import IngressApplication.IngressApplication.model.Task;
import IngressApplication.IngressApplication.service.QueueService;
import IngressApplication.IngressApplication.service.QueueServiceImpl;
import IngressApplication.IngressApplication.utils.Exceptions.CustomResponse;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/ingress")
public class TaskController {
    private final QueueService queueService;
    private final MeterRegistry meterRegistry;
    private final Timer timer;


    public TaskController(QueueService queueService, MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.timer = meterRegistry.timer("ingress.request.latency");
        this.queueService = queueService;
    }


    @RequestMapping("/request")
    @PostMapping
    public ResponseEntity<Void> enqueue(@RequestBody Task task) {
        return queueService.tryEnqueue(task)
                ? ResponseEntity.accepted().build()
                : ResponseEntity.status(429).build();
    }


    @RequestMapping("/request/claim")
    @GetMapping
    public ResponseEntity<?> claimTask() {
        Task task = queueService.dequeTask();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CustomResponse("Task dequeued successfully",
                        HttpStatus.OK.value(), new Object[]{task}));
    }

    @RequestMapping("/echo")
    @GetMapping
    public ResponseEntity<?> echo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new CustomResponse("Echoed successfully",
                                HttpStatus.OK.value(), new Object[0])
                );
    }

    @RequestMapping("/metrics")
    @GetMapping
    public Map<String, Long> getMetrics() {
        return Map.of(
                "acceptedCount", queueService.getAcceptedCount(),
                "rejectedCount", queueService.getRejectedCount(),
                "currentQueueSize", queueService.queueSize()
        );
    }

}
