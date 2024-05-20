import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {
    private Queue<T> queue;
    private int capacity;

    public BlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    // Метод для добавления элемента в очередь
    public synchronized void enqueue(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    // Метод для извлечения элемента из очереди
    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.remove();
        notifyAll();
        return item;
    }

    // Метод для получения текущего размера очереди
    public synchronized int size() {
        return queue.size();
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(5);

        // Пример производителя
        Runnable producer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    blockingQueue.enqueue(i);
                    System.out.println("Производитель добавил: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Пример потребителя
        Runnable consumer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = blockingQueue.dequeue();
                    System.out.println("Потребитель извлек: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // Запуск потоков производителей и потребителей
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();
    }
}
