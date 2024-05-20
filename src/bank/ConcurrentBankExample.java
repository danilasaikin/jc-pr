package bank;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private int balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(int amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public Lock getLock() {
        return lock;
    }
}

class ConcurrentBank {
    private final Map<Integer, BankAccount> accounts = new HashMap<>();
    private final Lock bankLock = new ReentrantLock();
    private int nextAccountId = 1;

    public BankAccount createAccount(int initialBalance) {
        bankLock.lock();
        try {
            int accountId = nextAccountId++;
            BankAccount account = new BankAccount(initialBalance);
            accounts.put(accountId, account);
            return account;
        } finally {
            bankLock.unlock();
        }
    }

    public void transfer(BankAccount from, BankAccount to, int amount) {
        BankAccount firstLock = from.hashCode() < to.hashCode() ? from : to;
        BankAccount secondLock = from.hashCode() < to.hashCode() ? to : from;

        while (true) {
            boolean firstLocked = firstLock.getLock().tryLock();
            boolean secondLocked = secondLock.getLock().tryLock();

            if (firstLocked && secondLocked) {
                try {
                    if (from.withdraw(amount)) {
                        to.deposit(amount);
                    }
                } finally {
                    secondLock.getLock().unlock();
                    firstLock.getLock().unlock();
                }
                break;
            }

            if (firstLocked) {
                firstLock.getLock().unlock();
            }

            if (secondLocked) {
                secondLock.getLock().unlock();
            }

            // Избежать активного ожидания с небольшим сном
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getTotalBalance() {
        bankLock.lock();
        try {
            return accounts.values().stream().mapToInt(BankAccount::getBalance).sum();
        } finally {
            bankLock.unlock();
        }
    }
}

public class ConcurrentBankExample {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        // Создание счетов
        BankAccount account1 = bank.createAccount(1000);
        BankAccount account2 = bank.createAccount(500);

        // Перевод между счетами
        Thread transferThread1 = new Thread(() -> bank.transfer(account1, account2, 200));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2, account1, 100));

        transferThread1.start();
        transferThread2.start();

        try {
            transferThread1.join();
            transferThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вывод общего баланса
        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}

