package processor.processor.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import processor.processor.config.ProcessorConfig;
import processor.processor.controller.IngressClient;
import processor.processor.model.Task;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class WorkerPool {
    private static final Logger log = LoggerFactory.getLogger(WorkerPool.class);
    private static final long POLL_SLEEP_MS = 100;
    private static final long SHUTDOWN_TIMEOUT_SECONDS = 30;

    private final ProcessorConfig config;
    private final IngressClient ingressClient;
    private final TaskProcessor taskProcessor;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ExecutorService executor;

    public WorkerPool(ProcessorConfig config, IngressClient ingressClient, TaskProcessor taskProcessor) {
        this.config = config;
        this.ingressClient = ingressClient;
        this.taskProcessor = taskProcessor;
    }

    @PostConstruct
    public void start() {
        int workerCount = config.getWorkerCount();
        log.info("Starting worker pool with {} workers", workerCount);

        executor = Executors.newFixedThreadPool(workerCount);
        running.set(true);

        for (int i = 0; i < workerCount; i++) {
            int workerId = i;
            executor.submit(() -> workerLoop(workerId));
        }

        log.info("Worker pool started successfully");
    }

    private void workerLoop(int workerId) {
        log.info("Worker {} started", workerId);

        while (running.get()) {
            try {
                Optional<Task> taskOpt = ingressClient.claimTask();

                if (taskOpt.isEmpty()) {
                    // No tasks available, short sleep
                    Thread.sleep(POLL_SLEEP_MS);
                    continue;
                }

                Task task = taskOpt.get();
                log.debug("Worker {} claimed task {}", workerId, task.getId());

                try {
                    taskProcessor.process(task);
                } catch (Exception e) {
                    // Local failure handling - log only, don't crash worker
                    log.error("Worker {} failed to process task {}", workerId, task.getId(), e);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Worker {} interrupted", workerId);
                break;

            } catch (Exception e) {
                // Catch-all to prevent worker death
                log.error("Unexpected error in worker {}", workerId, e);
                try {
                    Thread.sleep(POLL_SLEEP_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        log.info("Worker {} stopped", workerId);
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down worker pool...");

        // Stop polling new tasks
        running.set(false);

        // Shutdown executor (no new tasks accepted)
        executor.shutdown();

        try {
            // Wait for in-flight tasks to complete
            log.info("Waiting for in-flight tasks to complete (timeout: {}s)", SHUTDOWN_TIMEOUT_SECONDS);
            boolean terminated = executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (!terminated) {
                log.warn("Some tasks did not complete within timeout, forcing shutdown");
                executor.shutdownNow();

                // Give a bit more time for force shutdown
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } else {
                log.info("All workers terminated cleanly");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Shutdown interrupted, forcing shutdown", e);
            executor.shutdownNow();
        }

        log.info("Worker pool shutdown complete");
    }
}