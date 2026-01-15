package com.expensecontrol.service;

import com.expensecontrol.dao.RoleDAO;
import com.expensecontrol.model.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RoleService {

    @Inject
    private RoleDAO roleDAO;

    public Role createRole(Role role) {
        // normalize name to uppercase to enforce uniqueness semantics
        role.setName(role.getName() != null ? role.getName().trim().toUpperCase() : null);
        return roleDAO.save(role);
    }

    public Role updateRole(Role role) {
        role.setName(role.getName() != null ? role.getName().trim().toUpperCase() : null);
        return roleDAO.save(role);
    }

    public void deleteRole(Long id) {
        roleDAO.deleteById(id);
    }

    public Optional<Role> findById(Long id) {
        return roleDAO.findById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleDAO.findByName(name != null ? name.trim().toUpperCase() : null);
    }

    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    public long count() {
        return roleDAO.count();
    }

    public boolean existsByName(String name) {
        return roleDAO.existsByName(name != null ? name.trim().toUpperCase() : null);
    }

    public void createDefaultRolesIfNotExist() {
        if (!existsByName("ADMIN")) {
            createRole(new Role("ADMIN", "System Administrator"));
        }
        if (!existsByName("USER")) {
            createRole(new Role("USER", "Basic System User"));
        }
    }
}
