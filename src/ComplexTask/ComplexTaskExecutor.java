package ComplexTask;

import java.util.concurrent.*;

public class ComplexTaskExecutor {
    private final int numberOfTasks;
    private final CyclicBarrier barrier;
    private final ExecutorService executor;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
        this.barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("Все задачи завершены. Объединение результатов.");
            // Логика объединения результатов выполнения всех задач
        });
        this.executor = Executors.newFixedThreadPool(numberOfTasks);
    }

    public void executeTasks() {
        for (int i = 0; i < numberOfTasks; i++) {
            int taskId = i;
            executor.submit(() -> {
                try {
                    ComplexTask task = new ComplexTask(taskId);
                    task.execute();
                    barrier.await(); // Ожидание завершения всех задач
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Пул потоков не завершил выполнение задач.");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
