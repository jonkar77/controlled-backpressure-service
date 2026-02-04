package IngressApplication.IngressApplication.service;

import IngressApplication.IngressApplication.model.Task;
import IngressApplication.IngressApplication.utils.Exceptions.EmptyQueueException;
import IngressApplication.IngressApplication.utils.Exceptions.QueueFullException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

@Service
public class QueueServiceImpl implements QueueService {

    private final ArrayBlockingQueue<Task> queue =
            new ArrayBlockingQueue<>(100000);
    private final LongAdder acceptedCount = new LongAdder();
    private final LongAdder rejectedCount = new LongAdder();


    @Override
    public Task dequeTask() {
        if (queue.isEmpty()) {
            throw new EmptyQueueException("No tasks in the queue to dequeue.");
        }
        Task task = (Task) queue.poll();
        return task;
    }

    @Override
    public boolean tryEnqueue(Task task) {
        if (queue.offer(task)) {
            acceptedCount.increment();
            return true;
        } else {
            rejectedCount.increment();
            return false;
        }
    }

    public long getAcceptedCount() {
        return acceptedCount.sum();
    }

    public long getRejectedCount() {
        return rejectedCount.sum();
    }

    public long queueSize() {
        return queue.size();
    }

    public Task poll() {
        return queue.poll();
    }
}

