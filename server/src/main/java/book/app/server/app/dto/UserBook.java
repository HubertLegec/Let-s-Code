package book.app.server.app.dto;

public class UserBook {

    private Long bookId;
    private String title;
    private String author;
    private String year;

    public UserBook() {
    }

    public UserBook(Long bookId, String title, String author, String year) {

    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
