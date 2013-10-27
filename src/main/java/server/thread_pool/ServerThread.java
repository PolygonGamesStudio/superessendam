package server.thread_pool;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerThread extends Thread {
    private static Logger logger;

    static {
        logger = Logger.getLogger("ServerLog");
        FileHandler fileHandler;
        try {
            String workingDir = System.getProperty("user.dir");
            fileHandler = new FileHandler(workingDir + "/static/log/server.log");
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int counter = 0;
    private int id;

    public ServerThread() {
        id = ++ServerThread.counter;
    }

    @Override
    public void run() {
        synchronized (logger) {
            logger.info("thread " + id + ": done well... sleep tight");
        }
    }
}