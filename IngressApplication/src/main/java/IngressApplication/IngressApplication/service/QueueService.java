package IngressApplication.IngressApplication.service;

import IngressApplication.IngressApplication.model.Task;
import org.springframework.stereotype.Service;


public interface QueueService {

    public void enqueueTask(Task task);

    public Task dequeTask();

}
