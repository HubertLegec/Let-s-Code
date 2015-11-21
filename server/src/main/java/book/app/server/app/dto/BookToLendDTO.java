package book.app.server.app.dto;

public class BookToLendDTO {

    private Long bookId;
    private String title;
    private String author;
    private String nick;
    private String city;
    private String street;

    public BookToLendDTO() {
    }

    public BookToLendDTO(Long bookId, String title, String author, String nick, String city, String street) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.nick = nick;
        this.city = city;
        this.street = street;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
