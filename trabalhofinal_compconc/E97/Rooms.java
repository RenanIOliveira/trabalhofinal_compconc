import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Rooms implements Lock{

  int [] rooms;
  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();

  Handler [] handler;
  int current_room;

  public interface Handler {
    void onEmpty();
  }

  void onEmpty(){
    current_room = -1;
    notifyAll();
  }

  public Rooms(int m) {
    rooms = new int[m];
    current_room = -1;
  }

  public synchronized void enter(int i)  {
    while (current_room != -1 && current_room != i) {
      wait();
    }
    current_room = i;
    rooms[i]++;
  }

  public synchronized boolean exit() {
    if (rooms[current_room] > 0) {
      rooms[current_room]--;
      if (rooms[current_room] == 0) {
        handler[current_room].onEmpty();
      }
      return true;
    }
    return false;
  }

  public synchronized void setExitHandler(int i, Rooms.Handler h) {
    handler[i] = h;
  }
}
