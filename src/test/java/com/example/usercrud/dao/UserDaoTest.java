package com.example.usercrud.dao;

import com.example.usercrud.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    static SessionFactory sessionFactory;
    static UserDao dao;

    @BeforeAll
    static void beforeAll(){
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        cfg.setProperty("hibernate.connection.url",      postgres.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", postgres.getUsername());
        cfg.setProperty("hibernate.connection.password", postgres.getPassword());
        sessionFactory = cfg.buildSessionFactory();
        dao = new UserDao(sessionFactory);
    }

    @AfterAll
    static void afterAll(){
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void beforeEach() {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void saveAndFind() {
        User user = new User("test", "test@mail.com", 20);
        dao.save(user);

        User found = dao.findById(user.getId()).orElseThrow();

        assertEquals("test", found.getName());
        assertEquals("test@mail.com", found.getEmail());
        assertEquals(20, found.getAge());
        assertNotNull(found.getCreatedAt());
        assertNotNull(found.getId());
    }

    @Test
    void findNonexistent() {
        assertTrue(dao.findById(1L).isEmpty());
    }

    @Test
    void saveAndFindAll() {
        User john = new User("john", "john@mail.com", 23);
        User emma = new User("emma", "emma@mail.com", 22);
        dao.save(john);
        dao.save(emma);

        List<User> users = dao.findAll();

        assertEquals(2, users.size());
        assertEquals(
                Set.of("john", "emma"),
                users.stream().map(User::getName).collect(Collectors.toSet())
        );
    }

    @Test
    void updateAndFind() {
        User user = new User("test", "test@mail.com", 20);
        dao.save(user);

        User loaded = dao.findById(user.getId()).orElseThrow();
        loaded.setName("updated");
        loaded.setEmail("updated@mail.com");
        User updatedUser = dao.update(loaded);

        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(1, dao.findAll().size());

        User reloaded = dao.findById(user.getId()).orElseThrow();
        assertEquals("updated", reloaded.getName());
        assertEquals("updated@mail.com", reloaded.getEmail());
    }

    @Test
    void deleteAndFind() {
        User user = new User("test", "test@mail.com", 20);
        dao.save(user);
        dao.delete(user);

        Optional<User> found = dao.findById(user.getId());
        List<User> users = dao.findAll();

        assertTrue(found.isEmpty());
        assertTrue(users.isEmpty());
    }
}