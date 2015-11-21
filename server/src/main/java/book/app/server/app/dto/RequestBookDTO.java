package book.app.server.app.dto;

/**
 * Created by krzysiek on 21.11.15.
 */
public class RequestBookDTO {

    private Long bookId;

    private String token;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
