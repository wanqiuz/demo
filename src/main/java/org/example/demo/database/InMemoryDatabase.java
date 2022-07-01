package org.example.demo.database;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.example.demo.configuration.Configuration;
import org.example.demo.exception.UserAlreadyExistException;
import org.example.demo.exception.internal.EntityAlreadyExistException;
import org.example.demo.exception.internal.EntityNotExistException;
import org.example.demo.modal.entity.Role;
import org.example.demo.modal.entity.Token;
import org.example.demo.modal.entity.User;

/**
 * In memory database that store all data
 * Only business-irrelevant primitive exceptions are thrown
 * TODO: split to smaller classes
 */
public class InMemoryDatabase {
    private final Map<String, User> users = new HashMap<>();

    private final Map<String, Role> roles = new HashMap<>();

    private final Map<String, Token> tokens = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private InMemoryDatabase() {
        Configuration configuration = Configuration.getInstance();
        Timer timer = new Timer("expire-tokens");
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        lock.writeLock().lock();
                        try {
                            Long current = System.currentTimeMillis();
                            tokens.entrySet()
                                    .removeIf(entry -> entry.getValue().getExpiredAt() < current);
                        } finally {
                            lock.writeLock().unlock();
                        }
                    }
                },
                Long.parseLong((String) configuration.get("token.clean.delay")),
                Long.parseLong((String) configuration.get("token.clean.period")));
    }

    public static InMemoryDatabase getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final InMemoryDatabase instance = new InMemoryDatabase();
    }

    public ReadWriteLock getLock() {
        return lock;
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public void create(User user) throws EntityAlreadyExistException {
        if (users.containsKey(user.getUsername())) {
            throw new EntityAlreadyExistException();
        }
        users.put(user.getUsername(), user);
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public void deleteByUsername(String username) throws EntityNotExistException {
        if (!users.containsKey(username)) {
            throw new EntityNotExistException();
        }
        users.remove(username);
    }

    public void associateRole(String username, String roleName) {
        User user = users.get(username);
        Set<String> userRoles = user.getRoles();
        userRoles.add(roleName);
    }

    public Iterable<User> findAll() {
        return users.values();
    }

    public boolean existsByToken(String token) {
        return tokens.containsKey(token)
                && tokens.get(token).getExpiredAt() > System.currentTimeMillis();
    }

    public void deleteByToken(String token) {
        tokens.remove(token);
    }

    public Token findByTokenName(String tokenName) {
        return tokens.get(tokenName);
    }

    public void create(Token token) throws EntityAlreadyExistException {
        if (tokens.containsKey(token.getName())) {
            throw new UserAlreadyExistException();
        }
        tokens.put(token.getName(), token);
    }

    public void create(Role role) throws EntityAlreadyExistException {
        if (roles.containsKey(role.getRoleName())) {
            throw new EntityAlreadyExistException();
        }
        roles.put(role.getRoleName(), role);
    }

    public void deleteByRoleName(String roleName) {
        roles.remove(roleName);
    }

    public boolean existsByRoleName(String roleName) {
        return roles.containsKey(roleName);
    }

    public Role findByRoleName(String roleName) {
        return roles.get(roleName);
    }

    /**
     * truncate database for test purpose
     */
    public void truncate() {
        lock.writeLock().lock();
        try {
            users.clear();
            tokens.clear();
            roles.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
