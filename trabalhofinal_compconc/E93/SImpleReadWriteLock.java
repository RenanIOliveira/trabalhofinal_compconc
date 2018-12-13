import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class SimpleReadWriteLock implements ReadWriteLock {
  int readers;
  boolean writer;
  Lock readLock, writeLock;

  public SimpleReadWriteLock(){
    writer = false;
    readers = 0;
    readLock = new ReadLock();
    writeLock = new WriteLock();
  }

  public Lock readLock() {
    return readLock;
  }

  public Lock writeLock() {
    return writeLock;
  }

  class ReadLock implements Lock {
    public synchronized void lock() {
      while (writer) {
        try {
			    wait();
		    } catch (InterruptedException e) {
			    e.printStackTrace();
		    }
      }
      readers++;
    }

    public synchronized void unlock() {
      readers--;
      if (readers == 0)
        notifyAll();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock() {
      return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
      return false;
    }

    @Override
    public Condition newCondition() {
      return null;
	  }
  }

  protected class WriteLock implements Lock {
    public synchronized void lock() {
      while (readers > 0) {
        try {
			    wait();
		    } catch (InterruptedException e) {
			    e.printStackTrace();
		    }
      }
      writer = true;
    }

    public synchronized void unlock() {
      writer = false;
      notifyAll();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {	
    }

    @Override
    public boolean tryLock() {
      return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
      return false;
    }

    @Override
    public Condition newCondition() {
      return null;
	  }
  }
}
