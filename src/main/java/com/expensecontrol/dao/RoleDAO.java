package com.expensecontrol.dao;

import com.expensecontrol.config.DatabaseConfig;
import com.expensecontrol.model.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RoleDAO {

    private EntityManager getEntityManager() {
        return DatabaseConfig.getEntityManagerFactory().createEntityManager();
    }

    public Role save(Role role) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (role.getId() == null) {
                em.persist(role);
            } else {
                role = em.merge(role);
            }
            em.getTransaction().commit();
            return role;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Role> findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return Optional.ofNullable(em.find(Role.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Role> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Role> q = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
            q.setParameter("name", name);
            return Optional.of(q.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    public List<Role> findAll() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Role> q = em.createQuery("SELECT r FROM Role r ORDER BY r.name", Role.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Role managed = em.find(Role.class, id);
            if (managed != null) {
                em.remove(managed);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery("SELECT COUNT(r) FROM Role r", Long.class);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery("SELECT COUNT(r) FROM Role r WHERE r.name = :name", Long.class);
            q.setParameter("name", name);
            return q.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}
