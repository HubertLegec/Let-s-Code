package book.app.server.app.dto;

import book.app.server.app.model.RequestStatus;

public class UpdateRequestDTO {
    private String requestId;
    private RequestStatus status;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

}
