package communication;

import java.io.Serializable;

public class Request<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String action;
    private T data;

    public Request() {
        this("");
    }

    public Request(String action) {
        this(action, null);
    }

    public Request(String action, T data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "Request [action=" + this.action + ", data=" + this.data + "]";
    }
}
