package IngressApplication.IngressApplication.model;

import org.jspecify.annotations.NonNull;

public class Task {

   @NonNull
   private String id;
    @NonNull
   private String type;
    @NonNull
   private String payload;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Task(String id, String type, String payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
