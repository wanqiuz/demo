package org.example.demo.configuration;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigurationTest {

    private final Configuration configuration = Configuration.getInstance();

    /** check if Configuration read right value from properties */
    @Test
    public void get() {
        assertEquals("7200001", configuration.get("token.duration"));
        assertEquals("50000", configuration.get("token.clean.delay"));
        assertEquals("20000", configuration.get("token.clean.period"));
        assertEquals("8082", configuration.get("server.port"));
    }

    /** check if Configuration read right value from properties */
    @Test
    public void getOrDefault() {
        assertEquals("7200001", configuration.getOrDefault("token.duration", "7200002"));
        assertEquals("7200001", configuration.getOrDefault("token.duration2", "7200001"));
    }
}
