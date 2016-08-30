package com.epam.dog;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static Session session;

    private static Session buildSession()
    {
        try
        {
            if (session == null)
            {
                Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/hibernate.config.xml"));
                StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
                serviceRegistryBuilder.applySettings(configuration.getProperties());
                ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
                SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                session = sessionFactory.openSession();
            }
            return session;
        } catch (Throwable ex)
        {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession()
    {
        if (session != null) {
            return session;
        } else {
            return buildSession();
        }
    }

    public static void shutdown()
    {
        session.close();
    }

}
