public class Bathroom {

  private int maleCount;
  private int femaleCount;

  public Bathroom() {
    maleCount = 0;
    femaleCount = 0;
  }

  public synchronized void enterMale() throws InterruptedException {
    while (femaleCount > 0) {
      wait();
    }
    maleCount++;
  }

  public synchronized void leaveMale() {
    maleCount--;
    if (maleCount == 0) {
      notifyAll();
    }
  }

  public synchronized void enterFemale() throws InterruptedException {
    while (maleCount > 0) {
      wait();
    }
    femaleCount++;
  }

  public synchronized void leaveFemale() {
    femaleCount--;
    if (femaleCount == 0) {
      notifyAll();
    }
  }
}
