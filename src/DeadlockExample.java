import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*
Код подвержен риску возникновения взаимной блокировки (deadlock),
 потому что два потока блокируют ресурсы в разном порядке.

Для исправления можно использовать метод tryLock() с попытками захвата блокировок
в определенном порядке и обработкой случаев,
 когда блокировка не может быть захвачена сразу
 */

public class DeadlockExample {



    private static class Resource {
        // Ресурсы
    }

    private final Resource resourceA = new Resource();
    private final Resource resourceB = new Resource();
    private final Lock lockA = new ReentrantLock();
    private final Lock lockB = new ReentrantLock();

    public void execute() {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                acquireResourcesAndWork(lockA, lockB, resourceA, resourceB, "Thread-1");
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                acquireResourcesAndWork(lockB, lockA, resourceB, resourceA, "Thread-2");
            }
        });

        thread1.start();
        thread2.start();
    }

    private void acquireResourcesAndWork(Lock firstLock, Lock secondLock, Resource firstResource, Resource secondResource, String threadName) {
        boolean acquiredFirstLock = false;
        boolean acquiredSecondLock = false;

        try {
            while (!acquiredFirstLock || !acquiredSecondLock) {
                acquiredFirstLock = firstLock.tryLock();
                acquiredSecondLock = secondLock.tryLock();

                if (acquiredFirstLock && acquiredSecondLock) {
                    System.out.println(threadName + " locked " + firstResource + " and " + secondResource);

                    // Имитация работы с ресурсом
                    Thread.sleep(100);
                } else {
                    if (acquiredFirstLock) {
                        firstLock.unlock();
                        acquiredFirstLock = false;
                    }
                    if (acquiredSecondLock) {
                        secondLock.unlock();
                        acquiredSecondLock = false;
                    }

                    // Избежать активного ожидания с небольшим сном
                    Thread.sleep(1);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquiredFirstLock) {
                firstLock.unlock();
                System.out.println(threadName + " unlocked " + firstResource);
            }
            if (acquiredSecondLock) {
                secondLock.unlock();
                System.out.println(threadName + " unlocked " + secondResource);
            }
        }
    }

    public static void main(String[] args) {
        DeadlockExample example = new DeadlockExample();
        example.execute();
    }
}
