package org.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperHolder {

    public static ObjectMapper getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ObjectMapper instance = new ObjectMapper();
    }
}
