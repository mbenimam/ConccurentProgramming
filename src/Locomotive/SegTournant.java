package Locomotive;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SegTournant implements Runnable {

  private int pointedGateway = 0;
  private int currentLocoId = -1;

  private HangarPool pHangars;

  private final ReentrantLock lock = new ReentrantLock(true);
  private final ReentrantLock lockP = new ReentrantLock();
  private final Condition enterred = lock.newCondition();
  private final Condition enterredAck = lock.newCondition();
  private final Condition called = lock.newCondition();
  private final Condition calledAck = lock.newCondition();
  private final Condition readyToMove = lock.newCondition();
  private final Condition readyToMoveAck = lock.newCondition();
  private final Condition empty = lock.newCondition();
  private final Condition emptyAck = lock.newCondition();

  private boolean ready = false;

  public SegTournant(HangarPool pHangars) {
    this.pHangars = pHangars;
  }

  public void trace(String m, int color) {
    String beg = "";
    String end = "";
    if (color != 0) {
      beg = (char) 27 + "[" + color + "m";
      end = " " + (char) 27 + "[0m";
    }

    System.out.println(beg + m + end);
    // Thread.yield();
  }

  public void appeler(int n) throws InterruptedException {
    lockP.lock();
    // bricolage
    // while (!ready) {
    // Thread.sleep(100);
    // }
    lock.lock();
    // we are sure that tha caller will signal the condition only if the server is
    // ready
    // try {

    while (!ready) {
      calledAck.awaitNanos(1000);
    }
    trace("Appelle de Seg tournant", 32);
    called.signal();
    pointedGateway = n;
    ready = false;

    // } finally {
    // lock.unlock();
    // }

  }

  public void entrer(int id) throws InterruptedException {
    // lock.lock();
    // try {
    while (!ready) {
      enterredAck.awaitNanos(10000);
    }
    currentLocoId = id;
    trace("loco " + id + " entre dans le seg tournant", 32);
    enterred.signal();
    ready = false;

    // } finally {
    // lock.unlock();
    //
  }

  public void attendrePositionOk() throws InterruptedException {

    while (!ready) {
      readyToMoveAck.awaitNanos(10000);
    }
    trace("rotation vers gateway " + pointedGateway + " ... ", 32);
    readyToMove.signal();
    ready = false;
  }

  public int getPosition() {
    // trouver un hangar vide

    return pointedGateway;
  }

  public void sortir(int id) throws InterruptedException {

    while (!ready) {
      emptyAck.awaitNanos(10000);
    }

    trace("Loco " + id + " sort du seg tournant", 32);
    empty.signal();
    ready = false;
    currentLocoId = -1;
    // unlock the segTournant
    lock.unlock();
    lockP.unlock();
  }

  // function du serveur

  public void attendreAppel() throws InterruptedException {
    calledAck.signal();
    trace("j'attend un appel d'une loco ...", 33);
    ready = true;
    called.await();
  }

  public void seDeplacer() throws InterruptedException {
    trace("j'attends que loco sois pret pour deplacement", 33);
    readyToMoveAck.signal();
    ready = true;
    readyToMove.await();
    trace("loco se deplace ... ", 32);
    Thread.sleep(1000);
    // Thread.sleep(1000);
  }

  public void attendreEntree() throws InterruptedException {
    trace("j'attends une Loco pour rentrer", 33);
    enterredAck.signal();
    ready = true;
    enterred.await();
    trace("Loco " + currentLocoId + "entre ... ", 32);
    pointedGateway = pHangars.hangarVide();
  }

  public void attendreVide() throws InterruptedException {
    trace("j'attends que Seg tournant devient libre", 33);
    emptyAck.signal();
    ready = true;
    empty.await();
    trace("seg Tournant vide", 32);
  }

  public void run() {
    try {
      // aquire the lock as long
      lock.lock();
      while (true) {
        //
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
