package model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TextMessage implements Serializable {
    private String sender = "";
    private String recipient = "";
    private String message = "";
    private Calendar timeSent;

    public TextMessage(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        timeSent = Calendar.getInstance();
    }

    public TextMessage(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        timeSent = Calendar.getInstance();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Calendar timeSent) {
        this.timeSent = timeSent;
    }

    public String getFormattedMessage(boolean sender) {
        //return String.format("%10s | %10s : %s", timeSent, recipient, message);
        return sender? timeSent.get(Calendar.HOUR_OF_DAY)+":"+timeSent.get(Calendar.MINUTE)+":"+timeSent.get(Calendar.SECOND)+" | Me:"+message : timeSent.get(Calendar.HOUR_OF_DAY)+":"+timeSent.get(Calendar.MINUTE)+":"+timeSent.get(Calendar.SECOND)+" | "+this.sender+":"+message;
    }
}
