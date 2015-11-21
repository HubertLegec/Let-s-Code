package book.app.server.app.dto;

/**
 * Created by krzysiek on 21.11.15.
 */

public class UpdateRequestStatusDTO {

    private String action;
    private String token;
    private String requestId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
