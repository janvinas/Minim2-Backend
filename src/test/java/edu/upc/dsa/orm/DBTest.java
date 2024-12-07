package edu.upc.dsa.orm;

import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBTest {
    final static Logger logger = Logger.getLogger(DBTest.class);
    Session session;

    @Before
    public void setUp() throws Exception {
        session = FactorySession.openSession();
    }

    @Test
    public void queryTest() throws Exception {
        User user = new User("a","a","a@a.com");
        session.save(user);
        logger.debug(session.findAll(User.class, Map.of("username", "a")));
        Assert.assertEquals("a@a.com", session.findAll(User.class, Map.of("username", "a")).get(0).getMail());

        session.update(User.class, Map.of("mail", "a@b.com"), Map.of("username", "a"));
        logger.debug(session.findAll(User.class, Map.of("username", "a")));
        Assert.assertEquals("a@b.com", session.findAll(User.class, Map.of("username", "a")).get(0).getMail());

        session.delete(User.class, Map.of("username", "a"));
        Assert.assertEquals(0, session.findAll(User.class, Map.of("username", "a")).size());

    }

    @After
    public void after() {
        session.close();
    }
}
