package server.thread_pool;

import java.util.logging.Logger;

public class ServerThread extends Thread {
    private static int currentMax = 0;

    public static void setCurrentMax(int max) {
        currentMax = max;
    }

    private final Logger logger;
    private int id;

    public ServerThread(int id, Logger logger) {
        this.id = id;
        this.logger = logger;
    }

    @Override
    public void run() {
        synchronized (logger) {
            while (id > currentMax) {
                try {
                    logger.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentMax++;
            logger.info("done well... sleep tight - thread: " + id);
            logger.notifyAll(); // все, кто синхронизируются по этому монитору, будут предупреждены
        }
    }
}