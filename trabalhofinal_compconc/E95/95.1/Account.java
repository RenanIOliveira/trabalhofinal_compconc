import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
  private double balance;
  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  public Account(double initialBalance) {
    setBalance(initialBalance);
  }

  double getBalance() {
    return balance;
  }

  private void setBalance(double balance) {
    this.balance = balance;
  }

  void deposit(double amount) {
    lock.lock();
    try {
      balance += amount;
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }

  void withdraw(double amount) throws InterruptedException {
    lock.lock();
    try {
      while (balance < amount) {
        condition.await();
      }
      balance -= amount;
      condition.signalAll();
    } finally {
      lock.unlock();
    }
  }
}
