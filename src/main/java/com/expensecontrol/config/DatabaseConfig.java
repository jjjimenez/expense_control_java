package com.expensecontrol.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseConfig {
    
    private static EntityManagerFactory emf;
    
    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory("expenseControlPU");
        }
        return emf;
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}