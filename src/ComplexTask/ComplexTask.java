package ComplexTask;

public class ComplexTask {
    private final int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public void execute() {
        // Имитация выполнения сложной задачи
        System.out.println("Выполнение задачи " + taskId + " потоком " + Thread.currentThread().getName());
        try {
            Thread.sleep((int) (Math.random() * 1000)); // Симуляция времени выполнения задачи
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Завершение задачи " + taskId + " потоком " + Thread.currentThread().getName());
    }
}



