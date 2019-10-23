package Locomotive;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SegTournant implements Runnable {

  private int pointedGateway = 0;
  private int currentLocoId = 0;

  private final ReentrantLock lock = new ReentrantLock();
  private final ReentrantLock lockP = new ReentrantLock();
  private final Condition enterred = lock.newCondition();
  private final Condition called = lock.newCondition();
  private final Condition readyToMove = lock.newCondition();
  private final Condition empty = lock.newCondition();

  public SegTournant() {

  }

  public void appeler(int n) {
    lockP.lock();
    called.signal();
    pointedGateway = n;
  }

  public void entrer(int id) {
    currentLocoId = id;

    System.out.println("loco " + id + " entre dans le seg tournant");
    enterred.signal();
  }

  public void attendrePositionOk() throws InterruptedException {
    System.out.println("rotation vers gateway " + pointedGateway + " ... ");
    readyToMove.signal();
  }

  public int getPosition() {
    // trouver un hangar vide
    return pointedGateway;
  }

  public void sortir(int id) {
    lock.lock();
    System.out.println("Loco " + id + " sort du seg tournant");
    empty.signal();
    lockP.unlock();
  }

  public void attendreAppel() throws InterruptedException {
    called.wait();
  }

  public void seDeplacer() throws InterruptedException {
    System.out.println("j'attends que loco sois pret pour deplacement");
    readyToMove.wait();
    Thread.sleep(1000);
  }

  public void attendreEntree() throws InterruptedException {
    System.out.println("j'attends une Loco pour rentrer");
    enterred.wait();
  }

  public void attendreVide() throws InterruptedException {
    System.out.println("j'attends que Seg tournant devient libre");
    empty.wait();
  }

  public void run() {
    try {
      lock.lock();
      while (true) {
        attendreAppel();
        seDeplacer();
        attendreEntree();
        seDeplacer();
        attendreVide();
      }
    } catch (InterruptedException e) {

    } finally {
      lock.unlock();
    }
  }
}
