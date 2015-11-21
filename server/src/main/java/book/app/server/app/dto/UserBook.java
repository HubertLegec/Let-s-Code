package book.app.server.app.dto;

public class UserBook {

    private String bookId;
    private String title;
    private String author;
    private String year;

    public UserBook() {
    }

    public UserBook(String bookId, String title, String author, String year) {
this.bookId=bookId;
this.title=title;
this.author=author;
this.year=year;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
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
