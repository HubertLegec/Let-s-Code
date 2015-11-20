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

    private String token;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
