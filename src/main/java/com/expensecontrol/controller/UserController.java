package com.expensecontrol.controller;

import com.expensecontrol.model.User;
import com.expensecontrol.service.UserService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@ManagedBean
@ViewScoped
public class UserController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private UserService userService;
    
    @Inject
    private AuthController authController;
    
    private List<User> users;
    private User selectedUser;
    private User newUser;
    private boolean editMode;
    private String confirmPassword;
    
    @PostConstruct
    public void init() {
        // Verificar permisos de administrador
        String authCheck = authController.requireAdmin();
        if (authCheck != null) {
            return;
        }
        
        loadUsers();
        newUser = new User();
        editMode = false;
    }
    
    public void loadUsers() {
        users = userService.findAllUsers();
    }
    
    public void prepareNewUser() {
        newUser = new User();
        confirmPassword = null;
        editMode = false;
    }
    
    public void prepareEditUser() {
        if (selectedUser != null) {
            newUser = new User();
            newUser.setId(selectedUser.getId());
            newUser.setUsername(selectedUser.getUsername());
            newUser.setEmail(selectedUser.getEmail());
            newUser.setFirstName(selectedUser.getFirstName());
            newUser.setLastName(selectedUser.getLastName());
            newUser.setRole(selectedUser.getRole());
            newUser.setActive(selectedUser.getActive());
            confirmPassword = null;
            editMode = true;
        }
    }
    
    public void saveUser() {
        try {
            if (!editMode) {
                // Crear nuevo usuario
                if (!validateNewUser()) {
                    return;
                }
                
                userService.createUser(newUser);
                addSuccessMessage("Usuario creado exitosamente");
            } else {
                // Actualizar usuario existente
                if (!validateEditUser()) {
                    return;
                }
                
                Optional<User> existingUserOpt = userService.findById(newUser.getId());
                if (existingUserOpt.isPresent()) {
                    User existingUser = existingUserOpt.get();
                    existingUser.setUsername(newUser.getUsername());
                    existingUser.setEmail(newUser.getEmail());
                    existingUser.setFirstName(newUser.getFirstName());
                    existingUser.setLastName(newUser.getLastName());
                    existingUser.setRole(newUser.getRole());
                    existingUser.setActive(newUser.getActive());
                    
                    // Actualizar contraseña solo si se proporcionó una nueva
                    if (newUser.getPassword() != null && !newUser.getPassword().trim().isEmpty()) {
                        userService.updateUserPassword(existingUser, newUser.getPassword());
                    } else {
                        userService.updateUser(existingUser);
                    }
                    
                    addSuccessMessage("Usuario actualizado exitosamente");
                }
            }
            
            loadUsers();
            prepareNewUser();
            
        } catch (Exception e) {
            addErrorMessage("Error al guardar el usuario: " + e.getMessage());
        }
    }
    
    public void deleteUser() {
        if (selectedUser != null) {
            try {
                // No permitir eliminar el usuario actual
                if (selectedUser.getId().equals(authController.getCurrentUser().getId())) {
                    addErrorMessage("No puede eliminar su propio usuario");
                    return;
                }
                
                userService.deleteUser(selectedUser.getId());
                addSuccessMessage("Usuario eliminado exitosamente");
                loadUsers();
                selectedUser = null;
                
            } catch (Exception e) {
                addErrorMessage("Error al eliminar el usuario: " + e.getMessage());
            }
        }
    }
    
    public void toggleUserStatus() {
        if (selectedUser != null) {
            try {
                // No permitir desactivar el usuario actual
                if (selectedUser.getId().equals(authController.getCurrentUser().getId())) {
                    addErrorMessage("No puede desactivar su propio usuario");
                    return;
                }
                
                if (selectedUser.getActive()) {
                    userService.deactivateUser(selectedUser.getId());
                    addSuccessMessage("Usuario desactivado exitosamente");
                } else {
                    userService.activateUser(selectedUser.getId());
                    addSuccessMessage("Usuario activado exitosamente");
                }
                
                loadUsers();
                
            } catch (Exception e) {
                addErrorMessage("Error al cambiar el estado del usuario: " + e.getMessage());
            }
        }
    }
    
    private boolean validateNewUser() {
        if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty()) {
            addErrorMessage("La contraseña es requerida");
            return false;
        }
        
        if (confirmPassword == null || !newUser.getPassword().equals(confirmPassword)) {
            addErrorMessage("Las contraseñas no coinciden");
            return false;
        }
        
        if (!userService.isUsernameAvailable(newUser.getUsername())) {
            addErrorMessage("El nombre de usuario ya está en uso");
            return false;
        }
        
        if (!userService.isEmailAvailable(newUser.getEmail())) {
            addErrorMessage("El email ya está en uso");
            return false;
        }
        
        return true;
    }
    
    private boolean validateEditUser() {
        if (!userService.isUsernameAvailable(newUser.getUsername(), newUser.getId())) {
            addErrorMessage("El nombre de usuario ya está en uso");
            return false;
        }
        
        if (!userService.isEmailAvailable(newUser.getEmail(), newUser.getId())) {
            addErrorMessage("El email ya está en uso");
            return false;
        }
        
        // Validar contraseña solo si se proporcionó una nueva
        if (newUser.getPassword() != null && !newUser.getPassword().trim().isEmpty()) {
            if (confirmPassword == null || !newUser.getPassword().equals(confirmPassword)) {
                addErrorMessage("Las contraseñas no coinciden");
                return false;
            }
        }
        
        return true;
    }
    
    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", message));
    }
    
    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }
    
    // Getters and Setters
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    public User getSelectedUser() {
        return selectedUser;
    }
    
    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public User getNewUser() {
        return newUser;
    }
    
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
    
    public boolean isEditMode() {
        return editMode;
    }
    
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public User.UserRole[] getUserRoles() {
        return User.UserRole.values();
    }
}