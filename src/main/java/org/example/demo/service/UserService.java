package org.example.demo.service;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import org.example.demo.configuration.Configuration;
import org.example.demo.database.InMemoryDatabase;
import org.example.demo.exception.*;
import org.example.demo.exception.internal.EntityAlreadyExistException;
import org.example.demo.exception.internal.EntityNotExistException;
import org.example.demo.modal.entity.Token;
import org.example.demo.modal.entity.User;
import org.example.demo.util.PasswordHelper;

public class UserService {
    private final InMemoryDatabase inMemoryDatabase;

    private final Configuration configuration;

    private UserService() {
        this.inMemoryDatabase = InMemoryDatabase.getInstance();
        this.configuration = Configuration.getInstance();
    }

    public static UserService getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final UserService instance = new UserService();
    }

    /**
     * Create user
     *
     * @param username username
     * @param password plaintext password
     * @throws UserAlreadyExistException if the user already exists
     */
    public void createUser(String username, String password) throws UserAlreadyExistException {
        byte[] passwordSalt = PasswordHelper.randomSalt();
        byte[] passwordHash = PasswordHelper.calculateHash(password, passwordSalt);
        Set<String> roles = new HashSet<>();
        User newUser = new User(username, passwordSalt, passwordHash, roles);

        inMemoryDatabase.getLock().writeLock().lock();
        try {
            inMemoryDatabase.create(newUser);
        } catch (EntityAlreadyExistException e) {
            throw new UserAlreadyExistException(e);
        } finally {
            inMemoryDatabase.getLock().writeLock().unlock();
        }
    }

    /**
     * Delete user
     *
     * @param username username
     * @throws UserNotExistException if the user doesn't exit
     */
    public void deleteUser(String username) throws UserNotExistException {
        inMemoryDatabase.getLock().writeLock().lock();
        try {
            inMemoryDatabase.deleteByUsername(username);
        } catch (EntityNotExistException e) {
            throw new UserNotExistException("User not exist");
        } finally {
            inMemoryDatabase.getLock().writeLock().unlock();
        }
    }

    /**
     * Add role to user (if the role is already associated with the user, nothing should happen)
     *
     * @param username username
     * @param roleName role name
     * @throws UserNotExistException if user not exist
     * @throws RoleNotExistException if role not exist
     */
    public void associateRole(String username, String roleName)
            throws UserNotExistException, RoleNotExistException {
        inMemoryDatabase.getLock().writeLock().lock();
        try {
            if (!inMemoryDatabase.existsByUsername(username)) {
                throw new UserNotExistException("User not exist");
            }
            if (!inMemoryDatabase.existsByRoleName(roleName)) {
                throw new RoleNotExistException("Role not exist");
            }
            inMemoryDatabase.associateRole(username, roleName);
        } finally {
            inMemoryDatabase.getLock().writeLock().unlock();
        }
    }

    /**
     * Check role
     *
     * @param token authentication token
     * @param roleName role name
     * @return returns true if the user, identified by the token, belongs to the role, false
     *     otherwise
     * @throws TokenInvalidOrExpiredException if token is invalid or expired
     */
    public boolean checkRole(String token, String roleName) throws TokenInvalidOrExpiredException {
        inMemoryDatabase.getLock().readLock().lock();
        try {
            token = new String(Base64.getDecoder().decode(token.getBytes()));
            Token currentToken = inMemoryDatabase.findByTokenName(token);
            if (currentToken == null) {
                throw new TokenInvalidOrExpiredException("Token is invalid or expired");
            }
            User user = inMemoryDatabase.findByUsername(currentToken.getUsername());
            return user.getRoles().contains(roleName);
        } finally {
            inMemoryDatabase.getLock().readLock().unlock();
        }
    }

    /**
     * Authenticate
     *
     * @param username username
     * @param password plaintext password
     * @return authentication token (valid for pre-configured time)
     * @throws AuthenticationFailedException if authentication failed
     */
    public String authenticate(String username, String password)
            throws AuthenticationFailedException {
        inMemoryDatabase.getLock().readLock().lock();
        try {
            User user = inMemoryDatabase.findByUsername(username);
            if (user == null) {
                throw new AuthenticationFailedException("Authentication failed");
            }
            boolean authenticated =
                    PasswordHelper.checkPassword(
                            password, user.getPasswordSalt(), user.getPasswordHash());
            if (!authenticated) {
                throw new AuthenticationFailedException();
            }

            String tokenName = PasswordHelper.randomToken();
            Long duration = Long.parseLong((String) configuration.get("token.duration"));
            Long expiredAt = System.currentTimeMillis() + duration;
            Token newToken = new Token(tokenName, username, expiredAt);
            inMemoryDatabase.create(newToken);
            String rawTokenName = newToken.getName();
            return new String(Base64.getEncoder().encode(rawTokenName.getBytes()));
        } finally {
            inMemoryDatabase.getLock().readLock().unlock();
        }
    }

    /**
     * Invalidate authentication token
     *
     * @param token authentication token
     * @throws TokenInvalidOrExpiredException TokenInvalidOrExpiredException if token is invalid or
     *     expired
     */
    public void invalidateToken(String token) throws TokenInvalidOrExpiredException {
        inMemoryDatabase.getLock().writeLock().lock();
        try {
            token = new String(Base64.getDecoder().decode(token.getBytes()));
            boolean exist = inMemoryDatabase.existsByToken(token);
            if (!exist) {
                throw new TokenInvalidOrExpiredException("Token is invalid or expired");
            }
            inMemoryDatabase.deleteByToken(token);
        } finally {
            inMemoryDatabase.getLock().writeLock().unlock();
        }
    }
}
