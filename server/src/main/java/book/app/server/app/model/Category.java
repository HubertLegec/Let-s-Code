package book.app.server.app.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by krzysiek on 20.11.15.
 */

@Entity
@Table(name="CATEGORIES")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="CATEGORY_BOOK", joinColumns=@JoinColumn(name="CATEGORY_ID"),
            inverseJoinColumns=@JoinColumn(name="BOOK_ID"))
    Set<Book> books = new HashSet<>();




}
