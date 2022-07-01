package org.example.demo.service;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Collectors;
import org.example.demo.configuration.Configuration;
import org.example.demo.database.InMemoryDatabase;
import org.example.demo.exception.AuthenticationFailedException;
import org.example.demo.exception.RoleAlreadyExistException;
import org.example.demo.exception.TokenInvalidOrExpiredException;
import org.example.demo.modal.entity.Role;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class ScenarioTest {

    private final UserService userService = UserService.getInstance();

    private final RoleService roleService = RoleService.getInstance();

    private final InMemoryDatabase inMemoryDatabase = InMemoryDatabase.getInstance();

    private final Configuration configuration = Configuration.getInstance();

    @After
    public void tearUp() throws Exception {
        inMemoryDatabase.truncate();
    }

    /** test scenario: create user -> authenticate */
    @Test
    public void createUserAndAuthenticate() {
        String username = "jasper";
        String password = "92875292923";
        try {
            userService.createUser(username, password);
            String token = userService.authenticate(username, password);
            Assert.assertTrue(token != null && token.length() > 0);
        } catch (Exception e) {
            fail();
        }
    }

    /** test scenario: create user -> delete user -> authenticate */
    @Test
    public void createAndDeleteUserThenLogin() {
        String username = "jasper_zhao";
        String password = "3213213";
        try {
            userService.createUser(username, password);
            userService.deleteUser(username);
        } catch (Exception e) {
            fail();
        }
        try {
            userService.authenticate(username, password);
            fail();
        } catch (AuthenticationFailedException e) {
            Assert.assertTrue(true);
        }
    }

    /** test scenario: create user -> authenticate -> create role -> associate role -> check role */
    @Test
    public void associateRole() {
        String username = "jasper_zhao";
        String password = "3213213";
        String roleName = "admin";
        try {
            userService.createUser(username, password);
            String token = userService.authenticate(username, password);
            roleService.createRole(roleName);
            userService.associateRole(username, roleName);
            boolean hasRole = userService.checkRole(token, roleName);
            Assert.assertTrue(hasRole);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * test scenario: create user -> authenticate -> create role -> associate role -> check role ->
     * invalidate token -> authenticate
     */
    @Test
    public void invalidateToken() {
        String username = "jasper_zhao";
        String password = "3213213";
        String roleName = "admin";
        String token = null;
        try {
            userService.createUser(username, password);
            token = userService.authenticate(username, password);
            roleService.createRole(roleName);
            userService.associateRole(username, roleName);
            userService.checkRole(token, roleName);
            userService.invalidateToken(token);
            userService.authenticate(username, password);
        } catch (Exception e) {
            fail();
        }

        try {
            userService.checkRole(token, roleName);
            fail();
        } catch (TokenInvalidOrExpiredException e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * test scenario: create user -> authenticate -> create role -> associate role -> create role ->
     * associate role -> list role
     */
    @Test
    public void listRoles() {
        String username = "jasper_zhao";
        String password = "3213213";
        String roleName = "admin";
        String roleName2 = "analyst";
        try {
            userService.createUser(username, password);
            String token = userService.authenticate(username, password);
            roleService.createRole(roleName);
            userService.associateRole(username, roleName);
            roleService.createRole(roleName2);
            userService.associateRole(username, roleName2);
            Set<String> roles =
                    roleService.allRoles(token).stream()
                            .map(Role::getRoleName)
                            .collect(Collectors.toSet());
            assertEquals(2, roles.size());
            Assert.assertTrue(roles.contains(roleName) && roles.contains(roleName2));
        } catch (Exception e) {
            fail();
        }
    }

    /** test scenario: create role -> create same role */
    @Test
    public void createSameRole() {
        String roleName = "admin";
        try {
            roleService.createRole(roleName);
            roleService.createRole(roleName);
            fail();
        } catch (RoleAlreadyExistException e) {
            Assert.assertTrue(true);
        }
    }

    /** test scenario: create role -> create role 2 */
    @Test
    public void createDifferentRole() {
        String roleName = "admin";
        String roleName2 = "analyst";
        try {
            roleService.createRole(roleName);
            roleService.createRole(roleName2);
            Assert.assertTrue(true);
        } catch (RoleAlreadyExistException e) {
            fail();
        }
    }

    /**
     * test scenario: change token duration(shorter than 1s) -> create user -> authenticate -> sleep
     * -> logout
     */
    @Test
    public void expireToken() {
        String username = "jasper_zhao";
        String password = "3213213";
        try {
            configuration.set("token.duration", "500");
            userService.createUser(username, password);
            String token = userService.authenticate(username, password);
            Thread.sleep(1000);
            userService.invalidateToken(token);
            fail();
        } catch (TokenInvalidOrExpiredException e) {
            Assert.assertTrue(true);
        } catch (InterruptedException e) {
            fail();
        }
    }

    /**
     * test scenario: change token duration(longer than 1s) -> create user -> authenticate -> logout
     */
    @Test
    public void tokenNotExpire() {
        String username = "jasper_zhao";
        String password = "3213213";
        try {
            configuration.set("token.duration", "500000");
            userService.createUser(username, password);
            String token = userService.authenticate(username, password);
            userService.invalidateToken(token);
        } catch (TokenInvalidOrExpiredException e) {
            fail();
        }
    }
}
