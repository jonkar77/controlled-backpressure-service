package processor.processor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import processor.processor.model.Task;

import java.util.Random;

    @Service
    public class TaskProcessor {
        private static final Logger log = LoggerFactory.getLogger(TaskProcessor.class);
        private final Random random = new Random();

        public void process(Task task) {
            log.info("Processing task: id={}, payload={}", task.getId(), task.getPayload());

            try {
                // Simulate variable work duration (100-300ms)
                int sleepTime = 100 + random.nextInt(201);
                Thread.sleep(sleepTime);

                // Simulate random failures (10-20% failure rate)
                if (random.nextInt(100) < 15) {
                    throw new RuntimeException("Simulated processing failure");
                }

                log.info("Successfully processed task: id={}", task.getId());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Task processing interrupted: id={}", task.getId(), e);
                throw new RuntimeException("Task interrupted", e);

            } catch (Exception e) {
                log.error("Failed to process task: id={}", task.getId(), e);
                throw e;
            }
        }
    }
