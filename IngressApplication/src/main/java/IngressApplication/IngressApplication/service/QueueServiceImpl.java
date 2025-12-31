package IngressApplication.IngressApplication.service;

import IngressApplication.IngressApplication.model.Task;
import IngressApplication.IngressApplication.utils.Exceptions.EmptyQueueException;
import IngressApplication.IngressApplication.utils.Exceptions.QueueFullException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;

@Service
public class QueueServiceImpl implements QueueService{
    ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(100);

    @Override
    public void enqueueTask(Task task){
        boolean result = arrayBlockingQueue.offer(task);
        if(!result){
            throw new QueueFullException("Server is busy. Please try again later.");
        }
    };

    @Override
    public Task dequeTask(){
        if(arrayBlockingQueue.isEmpty()){
            throw new EmptyQueueException("No tasks in the queue to dequeue.");
        }
        Task task = (Task) arrayBlockingQueue.poll();
        return task;
    };
}
