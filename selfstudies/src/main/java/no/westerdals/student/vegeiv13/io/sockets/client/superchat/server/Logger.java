package no.westerdals.student.vegeiv13.io.sockets.client.superchat.server;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Logger {

    private Logger() {}

    private static Logger instance;

    public static Logger getInstance() {
        if(instance == null) {
            synchronized (Logger.class) {
                if(instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public synchronized void write(String message) throws IOException {
        FileUtils.write(new File("generated/server.log"), message + "\n", true);
    }
}
