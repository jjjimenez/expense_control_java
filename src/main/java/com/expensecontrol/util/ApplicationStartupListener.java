package com.expensecontrol.util;

import com.expensecontrol.service.RoleService;
import com.expensecontrol.service.UserService;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationStartupListener implements ServletContextListener {
    
    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Crear roles por defecto si no existen
            roleService.createDefaultRolesIfNotExist();
            // Crear usuario administrador por defecto si no existe
            userService.createDefaultAdminIfNotExists();
            System.out.println("Aplicación iniciada correctamente");
        } catch (Exception e) {
            System.err.println("Error al inicializar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Aplicación detenida");
    }
}