package book.app.server.app.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by krzysiek on 20.11.15.
 */

@Entity
@Table(name = "AUTHORS")
public class Author {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(cascade= CascadeType.ALL)
    @JoinTable(name="AUTHOR_BOOK", joinColumns=@JoinColumn(name="AUTHOR_ID"),
            inverseJoinColumns=@JoinColumn(name="BOOK_ID"))
    private Set<Book> books = new HashSet<>();

    private String Name;

    private String Surname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
