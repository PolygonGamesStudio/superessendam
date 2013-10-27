package server.thread_pool;

public class TPool {
    private final int nThreads;
    private final ServerThread[] threads;

    public TPool(int nThreads) {
        this.nThreads = nThreads;
        threads = new ServerThread[nThreads];

        for (int i = 0; i < nThreads; i++) {
            threads[i] = new ServerThread();
            threads[i].start();
        }
    }

}