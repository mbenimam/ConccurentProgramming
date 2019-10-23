package Locomotive;

import java.util.concurrent.locks.ReentrantLock;

public class SegAcceuil {

  private final ReentrantLock lock = new ReentrantLock();

  public SegAcceuil() {

  }

  public void reserver() {
    lock.lock();
    System.out.println("SegAcceuil Reserver avec success");
  }

  public void liberer() {
    lock.unlock();
    System.out.println("SegAcceuil liberé avec success");
  }
}
