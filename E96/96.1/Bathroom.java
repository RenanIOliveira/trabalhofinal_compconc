import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bathroom {

  private int maleCount;
  private int femaleCount;
  Lock lock = new ReentrantLock();
  Condition noMales = lock.newCondition();
  Condition noFemales = lock.newCondition();

  public Bathroom() {
    maleCount = 0;
    femaleCount = 0;
  }

  public void enterMale() throws InterruptedException {
    lock.lock();
    try {
      while (femaleCount > 0) {
        noFemales.await();
      }
      maleCount++;
    } finally {
      lock.unlock();
    }
  }

  public void leaveMale() {
    lock.lock();
    try {
      maleCount--;
      if (maleCount == 0) {
        noMales.signalAll();
      }
    } finally {
      lock.unlock();
    }
  }

  public void enterFemale() throws InterruptedException {
    lock.lock();
    try {
      while (maleCount > 0) {
        noMales.await();
      }
      femaleCount++;
    } finally {
      lock.unlock();
    }
  }

  public void leaveFemale() {
    lock.lock();
    try {
      femaleCount--;
      if (femaleCount == 0) {
        noFemales.signalAll();
      }
    } finally {
      lock.unlock();
    }
  }
}
