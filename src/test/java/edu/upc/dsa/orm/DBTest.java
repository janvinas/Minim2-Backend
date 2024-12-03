package edu.upc.dsa.orm;

import edu.upc.dsa.models.User;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {

    Session session = FactorySession.openSession();

    @Test
    public void queryTest() throws SQLException {
        User user = new User("jan","jan","jan@gmail.com");
        session.save(user);
        session.findAll(User.class).forEach(System.out::println);

    }

    @After
    public void after() {
        session.close();
    }
}
