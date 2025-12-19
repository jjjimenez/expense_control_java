package com.expensecontrol.service;

import com.expensecontrol.dao.UserDAO;
import com.expensecontrol.model.User;
import com.expensecontrol.util.PasswordUtil;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    
    @Inject
    private UserDAO userDAO;
    
    public UserService() {
        // Constructor vacío para CDI
    }
    
    public User createUser(User user) {
        // Encriptar la contraseña antes de guardar
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        return userDAO.save(user);
    }
    
    public User updateUser(User user) {
        return userDAO.save(user);
    }
    
    public User updateUserPassword(User user, String newPassword) {
        user.setPassword(PasswordUtil.hashPassword(newPassword));
        return userDAO.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
    
    public List<User> findAllUsers() {
        return userDAO.findAll();
    }
    
    public List<User> findActiveUsers() {
        return userDAO.findActiveUsers();
    }
    
    public List<User> findUsersByRole(User.UserRole role) {
        return userDAO.findByRole(role);
    }
    
    public void deleteUser(Long id) {
        userDAO.deleteById(id);
    }
    
    public void deactivateUser(Long id) {
        Optional<User> userOpt = userDAO.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            userDAO.save(user);
        }
    }
    
    public void activateUser(Long id) {
        Optional<User> userOpt = userDAO.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(true);
            userDAO.save(user);
        }
    }
    
    public boolean authenticateUser(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getActive() && PasswordUtil.checkPassword(password, user.getPassword());
        }
        return false;
    }
    
    public boolean isUsernameAvailable(String username) {
        return !userDAO.existsByUsername(username);
    }
    
    public boolean isEmailAvailable(String email) {
        return !userDAO.existsByEmail(email);
    }
    
    public boolean isUsernameAvailable(String username, Long excludeUserId) {
        Optional<User> existingUser = userDAO.findByUsername(username);
        return !existingUser.isPresent() || existingUser.get().getId().equals(excludeUserId);
    }
    
    public boolean isEmailAvailable(String email, Long excludeUserId) {
        Optional<User> existingUser = userDAO.findByEmail(email);
        return !existingUser.isPresent() || existingUser.get().getId().equals(excludeUserId);
    }
    
    public long getTotalUserCount() {
        return userDAO.count();
    }
    
    public long getActiveUserCount() {
        return userDAO.countActiveUsers();
    }
    
    /**
     * Crea un usuario administrador por defecto si no existe ninguno
     */
    public void createDefaultAdminIfNotExists() {
        List<User> admins = userDAO.findByRole(User.UserRole.ADMIN);
        if (admins.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@expensecontrol.com");
            admin.setFirstName("Administrador");
            admin.setLastName("Sistema");
            admin.setRole(User.UserRole.ADMIN);
            createUser(admin);
        }
    }
}