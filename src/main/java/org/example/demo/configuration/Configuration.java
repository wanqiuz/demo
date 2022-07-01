package org.example.demo.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static final String DEFAULT_PROPERTIES = "application.properties";

    private final Properties properties;

    private Configuration() {
        this.properties = this.getBeanScanPath(DEFAULT_PROPERTIES);
    }

    public static Configuration getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final Configuration instance = new Configuration();
    }

    private Properties getBeanScanPath(String propertiesPath) {
        if (propertiesPath == null || propertiesPath.length() == 0) {
            propertiesPath = DEFAULT_PROPERTIES;
        }
        Properties props = new Properties();
        try (InputStream input =
                Configuration.class.getClassLoader().getResourceAsStream(propertiesPath)) {
            if (input == null) {
                return new Properties();
            }
            props.load(input);
            return props;
        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    String.format("Unable to read config file %s: %s", e.getMessage()));
        }
    }

    public Object get(String key) {
        if (properties.size() > 0) {
            return properties.get(key);
        }
        return null;
    }

    public void set(String key, Object value) {
        properties.put(key, value);
    }

    public Object getOrDefault(String key, Object defaultValue) {
        if (properties.containsKey(key)) {
            return properties.get(key);
        }
        return defaultValue;
    }
}
