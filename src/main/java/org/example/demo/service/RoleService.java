package org.example.demo.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.example.demo.database.InMemoryDatabase;
import org.example.demo.exception.RoleAlreadyExistException;
import org.example.demo.exception.RoleNotExistException;
import org.example.demo.exception.TokenInvalidOrExpiredException;
import org.example.demo.exception.internal.EntityAlreadyExistException;
import org.example.demo.modal.entity.Role;
import org.example.demo.modal.entity.Token;
import org.example.demo.modal.entity.User;

public class RoleService {
    private final InMemoryDatabase inMemoryDatabase;

    private RoleService() {
        this.inMemoryDatabase = InMemoryDatabase.getInstance();
    }

    public static RoleService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final RoleService instance = new RoleService();
    }

    /**
     * Create role
     *
     * @param name role name
     * @throws RoleAlreadyExistException fi the role already exists
     */
    public void createRole(String name) throws RoleAlreadyExistException {
        Role newRole = new Role(name);

        inMemoryDatabase.getLock().writeLock().lock();
        try {
            inMemoryDatabase.create(newRole);
        } catch (EntityAlreadyExistException e) {
            throw new RoleAlreadyExistException(e);
        } finally {
            inMemoryDatabase.getLock().writeLock().unlock();
        }
    }

    /**
     * Delete role
     *
     * @param roleName role name
     * @throws RoleNotExistException if the role doesn't exist
     */
    public void deleteRole(String roleName) throws RoleNotExistException {
        inMemoryDatabase.getLock().writeLock().lock();
        try {
            if (!inMemoryDatabase.existsByRoleName(roleName)) {
                throw new RoleNotExistException("Role not exist");
            }
            inMemoryDatabase.deleteByRoleName(roleName);
            inMemoryDatabase
                    .findAll()
                    .forEach(
                            user -> {
                                user.getRoles().remove(roleName);
                            });
        } finally {
            inMemoryDatabase.getLock().writeLock().unlock();
        }
    }

    /**
     * List aLl roles
     *
     * @param token authentication token
     * @return all roles for the user
     * @throws TokenInvalidOrExpiredException if token is invalid or expired
     */
    public List<Role> allRoles(String token) throws TokenInvalidOrExpiredException {
        inMemoryDatabase.getLock().readLock().lock();
        try {
            token = new String(Base64.getDecoder().decode(token.getBytes()));
            Token currentToken = inMemoryDatabase.findByTokenName(token);
            if (currentToken == null) {
                throw new TokenInvalidOrExpiredException();
            }
            User user = inMemoryDatabase.findByUsername(currentToken.getUsername());
            if (user == null) {
                throw new TokenInvalidOrExpiredException("Token is invalid or expired");
            }
            List<String> roles = new ArrayList<>(user.getRoles());
            return roles.stream()
                    .map(inMemoryDatabase::findByRoleName)
                    .map(Role::new)
                    .collect(Collectors.toUnmodifiableList());
        } finally {
            inMemoryDatabase.getLock().readLock().unlock();
        }
    }
}
