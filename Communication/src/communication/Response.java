package communication;

import java.io.Serializable;

public class Response<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private String errorMessage;
    public static final String SERVER_CONNECTION_ERROR = "-404";
    public static final String GENERAL_ERROR = "-999";
    public static final String ALREADY_LOGGED_IN = "-1000";
    public static final String NO_ERROR = "";
    public static final String USER_OFFLINE = "-989";

    public Response() {
        this( null);
    }

    public Response(T data) {
        this(data, (String)null);
    }

    public Response(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return "Responset [data=" + this.data + "]";
    }
}