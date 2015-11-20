package book.app.server.app.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by krzysiek on 20.11.15.
 */

@Entity
@Table(name = "BOOKS")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @OneToOne
    private User owner;

    private Long year;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "books")
    private Set<Author> authors = new HashSet<Author>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

}