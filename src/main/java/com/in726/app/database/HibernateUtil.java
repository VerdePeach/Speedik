package com.in726.app.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Configures access and interacts with database.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    /**
     * Creates session in database
     *
     * @return session
     */
    public static Session startSession() {
        return sessionFactory.openSession();
    }

    /**
     * Initializes the database and connection with it.
     */
    public static void init() {
        // configures settings from hibernate.cfg.xml
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    /**
     * Returns current session.
     *
     * @return session.
     */
//    public static Session getCurrentSession() {
//        return sessionFactory.getCurrentSession();
//    }
}
