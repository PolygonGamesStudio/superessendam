package server.thread_pool;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ThreadPool {
    private final int nThreads;
    private final ServerThread[] threads;
    private Logger logger;

    public ThreadPool(int nThreads, String loggerPath) {
        this.nThreads = nThreads;
        threads = new ServerThread[nThreads];
        logger = Logger.getLogger(ThreadPool.class.getName());
        logger.setLevel(Level.ALL);

        try {
            FileHandler fileHandler = new FileHandler(loggerPath);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
            // тут логирование в консоль
        }

        int threadId = 0;
        ServerThread.setCurrentMax(threadId);
        for (int i = threadId; i < nThreads; i++) {
            threads[i] = new ServerThread(i, logger);
            threads[i].start();
        }
    }

}