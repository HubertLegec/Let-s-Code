package book.app.server.app.model;

import javax.persistence.*;

/**
 * Created by krzysiek on 20.11.15.
 */

@Entity
@Table(name = "RATINGS")
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rated_by_id")
    private User rated_by;

    private String description;

    private Long rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getRated_by() {
        return rated_by;
    }

    public void setRated_by(User rated_by) {
        this.rated_by = rated_by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
