package sii.letscode.model;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by dominik on 21.11.15.
 */
public class BookListViewModel {
    public String title;
    private String author;
    private String nick;
    private String street;
    private int id;

    public BookListViewModel() {

    }

    public BookListViewModel(String title, String author, String nick, String street, int id) {
        this.title = title;
        this.author = author;
        this.nick = nick;
        this.street = street;
        this.id = id;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
