/**
 * Created by tedwei on 2/15/17.
 */
public class EventData {

    private String userName;
    private String message;

    public EventData() {
    }

    public EventData(String userName, String message) {
        super();
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
