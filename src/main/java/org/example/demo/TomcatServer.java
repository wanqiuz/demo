package org.example.demo;

import java.util.Objects;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.example.demo.configuration.Configuration;

public class TomcatServer {

    private static final String CONTEXT_PATH = "/";

    public static void start() throws LifecycleException, ServletException {
        Tomcat tomcat = new Tomcat();
        Configuration configuration = Configuration.getInstance();
        String baseDir =
                Objects.requireNonNull(
                                Thread.currentThread().getContextClassLoader().getResource(""))
                        .getPath();
        tomcat.setBaseDir(baseDir);
        if (configuration.get("server.port") == null) {
            tomcat.setPort(8080);
        } else {
            tomcat.setPort(Integer.parseInt((String) configuration.get("server.port")));
        }

        tomcat.addWebapp(CONTEXT_PATH, baseDir);
        tomcat.enableNaming();
        tomcat.start();
        tomcat.getServer().await();
    }

    public static void main(String[] args) throws LifecycleException, ServletException {
        start();
    }
}
