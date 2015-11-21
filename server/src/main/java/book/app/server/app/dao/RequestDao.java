package book.app.server.app.dao;

import book.app.server.app.model.Request;
import book.app.server.app.model.User;

import java.util.List;

/**
 * Created by krzysiek on 21.11.15.
 */
public interface RequestDao {
    public List<Request> findBySender(User sender);
    public List<Request> findByReceiver(User receiver);
}
