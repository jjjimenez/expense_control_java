package com.expensecontrol.controller;

import com.expensecontrol.model.Role;
import com.expensecontrol.service.RoleService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class RoleController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RoleService roleService;

    private List<Role> roles;
    private Role selectedRole;
    private Role editingRole;
    private boolean editMode;

    @PostConstruct
    public void init() {
        try {
            loadRoles();
            prepareNewRole();
        } catch (Exception e) {
            roles = new ArrayList<>();
            prepareNewRole();
            addErrorMessage("Error initializing roles: " + e.getMessage());
        }
    }

    public void loadRoles() {
        try {
            roles = roleService.findAll();
        } catch (Exception e) {
            roles = new ArrayList<>();
            addErrorMessage("Error loading roles: " + e.getMessage());
        }
    }

    public void prepareNewRole() {
        editingRole = new Role();
        editMode = false;
    }

    public void prepareEditRole() {
        if (selectedRole != null) {
            editingRole = new Role();
            editingRole.setId(selectedRole.getId());
            editingRole.setName(selectedRole.getName());
            editingRole.setDescription(selectedRole.getDescription());
            editMode = true;
        }
    }

    public void saveRole() {
        try {
            if (!validateRole()) {
                return;
            }
            if (editMode) {
                roleService.updateRole(editingRole);
                addSuccessMessage("Role updated successfully");
            } else {
                roleService.createRole(editingRole);
                addSuccessMessage("Role created successfully");
            }
            loadRoles();
            prepareNewRole();
        } catch (Exception e) {
            addErrorMessage("Error saving role: " + e.getMessage());
        }
    }

    public void deleteRole() {
        if (selectedRole == null) return;
        try {
            roleService.deleteRole(selectedRole.getId());
            addSuccessMessage("Role deleted successfully");
            loadRoles();
            selectedRole = null;
        } catch (Exception e) {
            addErrorMessage("Error deleting role: " + e.getMessage());
        }
    }

    private boolean validateRole() {
        if (editingRole.getName() == null || editingRole.getName().trim().isEmpty()) {
            addErrorMessage("Name is required");
            return false;
        }
        String nameUpper = editingRole.getName().trim().toUpperCase();
        if (nameUpper.length() > 20) {
            addErrorMessage("Name length must be at most 20 characters");
            return false;
        }
        // uniqueness
        Optional<Role> existing = roleService.findByName(nameUpper);
        if (!editMode && existing.isPresent()) {
            addErrorMessage("A role with this name already exists");
            return false;
        }
        if (editMode && existing.isPresent() && !existing.get().getId().equals(editingRole.getId())) {
            addErrorMessage("A role with this name already exists");
            return false;
        }
        if (editingRole.getDescription() != null && editingRole.getDescription().length() > 80) {
            addErrorMessage("Description length must be at most 80 characters");
            return false;
        }
        return true;
    }

    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }

    // Getters and setters
    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }

    public Role getSelectedRole() { return selectedRole; }
    public void setSelectedRole(Role selectedRole) { this.selectedRole = selectedRole; }

    public Role getEditingRole() { return editingRole; }
    public void setEditingRole(Role editingRole) { this.editingRole = editingRole; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
}
