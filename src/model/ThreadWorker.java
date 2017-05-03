package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The ThreadWorker class keeps track of a FixedThreadPool and is responsible for thread management while
 * GameOfLife is running concurrently. Is mainly responsible for running tasks and shutting down the FixedThreadPool
 * properly upon termination. The class is implemented using a Singleton design pattern to ensure that unnecessary
 * Thread Pools are avoided, and making it possible to have a controlled shutdown of the ExecutorService when the
 * application shuts down.
 * @author Oscar Vladau-Husevold
 * @version 1.0
 */
public class ThreadWorker {
    //The number of threads to be used. Equal to the system's available processors times two, because we assume the CPU
    //supports hyper-threading
    private final int numWorkers = Runtime.getRuntime().availableProcessors() * 2;

    private int threadIndex = 0;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(numWorkers);

    //The only time a threadWorker is initialized.
    private static final ThreadWorker threadWorker = new ThreadWorker();

    /**
     * Sole constructor, set private to prevent other class from initializing a ThreadWorker.
     */
    private ThreadWorker() {}

    /**
     * Method that returns the ThreadWorker, allowing other classes to take use of the class.
     * @return threadWorker - The threadWorker object that has been instanced.
     */
    public static ThreadWorker getInstance() {
        return threadWorker;
    }

    /**
     * Method that takes a Callable task and is responsible for having all workers execute that task. Calls on
     * createCallableList() to create a list the size of the current threadPool containing that task, and calls
     * runCallableList to execute all tasks in the list.
     * @param task The Callable task to be executed by all threads
     */
    public void runWorkers(Callable task) {
        List<Callable<Void>> callableList = createCallableList(task);
        runCallableList(callableList);
    }

    /**
     * Method that takes a Callable task and creates a list the size of the current threadPool with each entry being
     * the parameter task.
     * @param task The Callable task to be added into the list
     */
    private List<Callable<Void>> createCallableList(Callable task) {
        List<Callable<Void>> newList = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            newList.add(task);
        }
        return newList;
    }

    /**
     * Method that takes a list of Callable objects and calls ExecutorService's invokeAll() which will execute all
     * Callable objects in the list and wait until they are all done before continuing.
     * @param callableList The list of Callable objects to be executed.
     */
    private void runCallableList(List<Callable<Void>> callableList) {
        try {
            threadPool.invokeAll(callableList);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("Error waiting for thread");
        }
    }

    /**
     * Method that returns the current threadIndex and adds 1 to it. If it is greater or equal to the number
     * of active threads, it resets to 0. The method is synchronized to avoid a race condition.
     * @return threadIndex - The index of the current thread relative to the rest.
     * @see #threadIndex
     * @see #numWorkers
     */
    public synchronized int getThreadIndex() {
        if (threadIndex >= numWorkers) threadIndex = 0;
        return threadIndex++;
    }

    /**
     * Method that ensures that the ExecutorService is shut down properly, so that JVM can close. Calls shutdown
     * to allow all threads to finish their tasks, and waits for them to finish. If they do not finish within 2
     * seconds, it will call shutdownNow() which will terminate the executorService even if all threads are not
     * done.
     * @see #threadPool
     */
    public void shutDownExecutor() {
        if (threadPool.isShutdown()) {
            return;
        }

        //Tries shutting down the ExecutorService normally, waiting 3 seconds for it to finish
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        //After waiting, it will do a hard shut down in case some threads are still not finished.
        } finally {
            System.out.println("Shutting down...");
            threadPool.shutdownNow();
        }
    }

    /**
     * Method that returns the current number of threads in the threadPool.
     * @return numWorkers - The current number of threads in the threadPool.
     * @see #numWorkers
     */
    public int getNumWorkers() {
        return numWorkers;
    }

    /**
     * Method to check whether or not the ExecutorService has been shut down. Returns true if it has, or false
     * if it is still operational.
     * @return threadPool.isShutdown() - The boolean value representing whether or not the ExecutorService has
     * been shut down
     * @see #threadPool
     */
    public boolean getShutDownStatus() {
        return threadPool.isShutdown();
    }
}