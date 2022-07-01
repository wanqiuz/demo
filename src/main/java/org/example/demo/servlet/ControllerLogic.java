package org.example.demo.servlet;

import java.io.IOException;

/**
 * This is where we do input parsing and validation
 */
public interface ControllerLogic {
    Object run() throws IOException;
}
