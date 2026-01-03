package IngressApplication.IngressApplication.service;

import IngressApplication.IngressApplication.model.Task;
import IngressApplication.IngressApplication.utils.Exceptions.EmptyQueueException;
import IngressApplication.IngressApplication.utils.Exceptions.QueueFullException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

@Service
public class QueueServiceImpl implements QueueService{
    LongAdder enqueuedTaskCount = new LongAdder();
    LongAdder rejectedTaskCount = new LongAdder();
    ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);

    @Override
    public void enqueueTask(Task task){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean result = arrayBlockingQueue.offer(task);
        if(!result){
            rejectedTaskCount.add(1);
            throw new QueueFullException("Server is busy. Please try again later.");
        }
        enqueuedTaskCount.add(1);
    };

    @Override
    public Task dequeTask(){
        if(arrayBlockingQueue.isEmpty()){
            throw new EmptyQueueException("No tasks in the queue to dequeue.");
        }
        Task task = (Task) arrayBlockingQueue.poll();
        return task;
    };

    @Override
    public int getRejectedTasksCount(){
        System.out.println("Enqueued Requests: " + enqueuedTaskCount.intValue());
        return rejectedTaskCount.intValue();
    }
}
