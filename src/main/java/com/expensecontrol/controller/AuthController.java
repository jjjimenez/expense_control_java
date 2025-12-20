package com.expensecontrol.controller;

import com.expensecontrol.model.User;
import com.expensecontrol.service.UserService;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Optional;

@Named
@SessionScoped
public class AuthController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserService userService;
    
    private String username;
    private String password;
    private User currentUser;
    
    public AuthController() {
        // Constructor vacío para CDI
    }
    
    public String checkIfLoggedIn() {
        if (isLoggedIn()) {
            return "dashboard?faces-redirect=true";
        }
        return null;
    }
    
    public String login() {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            addErrorMessage("Por favor ingrese usuario y contraseña");
            return null;
        }
        
        if (userService.authenticateUser(username.trim(), password)) {
            Optional<User> userOpt = userService.findByUsername(username.trim());
            if (userOpt.isPresent()) {
                currentUser = userOpt.get();
                addInfoMessage("Bienvenido, " + currentUser.getFullName());
                
                // Limpiar campos de login
                username = null;
                password = null;
                
                return "dashboard?faces-redirect=true";
            }
        }
        
        addErrorMessage("Usuario o contraseña incorrectos");
        password = null; // Limpiar contraseña por seguridad
        return null;
    }
    
    public String logout() {
        currentUser = null;
        username = null;
        password = null;
        
        // Invalidar sesión
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        addInfoMessage("Sesión cerrada correctamente");
        return "login?faces-redirect=true";
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
    
    public String requireLogin() {
        if (!isLoggedIn()) {
            addErrorMessage("Debe iniciar sesión para acceder a esta página");
            return "login?faces-redirect=true";
        }
        return null;
    }
    
    public String requireAdmin() {
        String loginCheck = requireLogin();
        if (loginCheck != null) {
            return loginCheck;
        }
        
        if (!isAdmin()) {
            addErrorMessage("No tiene permisos para acceder a esta página");
            return "dashboard?faces-redirect=true";
        }
        return null;
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    private void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", message));
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}