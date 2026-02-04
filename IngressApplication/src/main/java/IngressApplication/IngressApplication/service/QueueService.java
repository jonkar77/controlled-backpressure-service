package IngressApplication.IngressApplication.service;

import IngressApplication.IngressApplication.model.Task;
import org.springframework.stereotype.Service;


public interface QueueService {


    public Task dequeTask();


    public boolean tryEnqueue(Task task);

    public Task poll();

    public long getAcceptedCount();

    public long getRejectedCount();

    public long queueSize();


}
