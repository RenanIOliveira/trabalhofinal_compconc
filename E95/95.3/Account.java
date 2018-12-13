import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
  private double balance;
  private int preferredRequests;
  private final Lock lock = new ReentrantLock();
  private final Lock transferLock = new ReentrantLock();
  private final Condition ordinaryCondition = lock.newCondition();
  private final Condition preferredCondition = lock.newCondition();

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
      notifyThreads();
    } finally {
      lock.unlock();
    }
  }

  void withdraw(double amount, boolean preferred) throws InterruptedException {
    lock.lock();
    try {
      if (preferred) {
        preferredRequests++;
        while (balance < amount) {
          preferredCondition.await();
        }
        preferredRequests--;
        balance -= amount;
        notifyThreads();
      }
      else {
        while (balance < amount) {
          ordinaryCondition.await();
        }
        balance -= amount;
        notifyThreads();
      }
    } finally {
      lock.unlock();
    }
  }

  void transfer(double amount, Account reserve, boolean preferred) throws InterruptedException {
    transferLock.lock();
    try {
      reserve.withdraw(amount, preferred);
      deposit(amount);
    } finally {
      transferLock.unlock();
    }
  }

  private void notifyThreads() {
    if (preferredRequests > 0) {
      preferredCondition.signalAll();
    }
    else {
      ordinaryCondition.signalAll();
    }
  }
}
