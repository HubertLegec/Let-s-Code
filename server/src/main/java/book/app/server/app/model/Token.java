package book.app.server.app.model;

import javax.persistence.*;

/**
 * Created by krzysiek on 20.11.15.
 */

@Entity
public class Token {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
