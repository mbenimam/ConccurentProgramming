package Convoyeur;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Chariot {

  private int m_poidMax;
  private int m_nbMax;

  // definition des condition de lock
  final ReentrantLock mutex = new ReentrantLock();
  final Condition full = mutex.newCondition();
  final Condition empty = mutex.newCondition();

  // condition de notification
  private boolean m_plein = false;
  private boolean m_empty = true;
  // compteur d'elements
  static int count = 0;
  static int poid = 0;
  // liste des elements du chariot
  private ArrayList<AleaObj> m_elements;
  private boolean actif = true;

  public Chariot(int poidMax, int nbMax) {
    m_poidMax = poidMax;
    m_nbMax = nbMax;
    // liste de taille maximale nbMax
    m_elements = new ArrayList<AleaObj>(nbMax);
  }

  // verification
  public boolean isEmpty() {
    mutex.lock();
    try {
      return m_empty;
    } finally {
      mutex.unlock();
    }

  }

  // charger des elements dans le chariot
  public void charger(AleaObj obj) throws InterruptedException, Exception {
    mutex.lock();
    System.out.println("chargeur Owned mutex");

    try {
      while (m_empty == false) {
        empty.await();
      }

      if (poid + obj.getPoid() <= m_poidMax && count + 1 <= m_nbMax) {

        m_elements.add(obj);
        count++;
        poid += obj.getPoid();

        System.out.println(this);
      } else {
        System.out.println("count = " + count + " Poid = " + poid);
        setFull();
        throw new Exception("reinsert element");

      }

    } finally {
      mutex.unlock();
    }

  }

  public AleaObj decharger() throws InterruptedException, Exception {
    /*
     * mutex.lock(); try { if (count == 0) { full.await(); } AleaObj obj =
     * m_elements.remove(0); count--; poid -= obj.getPoid(); // si on a tout
     * dechargé if (count == 0) empty.signal();
     *
     * } finally { mutex.unlock(); }
     */
    mutex.lock();
    System.out.println("dechargeur Owned mutex");
    try {
      while (m_plein == false && actif) {
        // System.out.println("ZHIDAUHZAIDHIUZAHDIHZAIUDHIZAIUDA");
        full.await();
      }
      if (count == 0 && !m_empty) {
        setEmpty();
        return null;
      }
      if (m_elements.size() != 0) {

        AleaObj tmp = m_elements.remove(0);
        count--;
        poid -= tmp.getPoid();
        if (count == 0) {
          setEmpty();
        }
        System.out.println(this);
        return tmp;

      } else {
        throw new ArrayIndexOutOfBoundsException();
      }
    } finally {
      mutex.unlock();
    }

  }

  public void setFull() {
    mutex.lock();
    try {
      m_empty = false;
      m_plein = true;
      System.out.println("full");
      full.signalAll();
    } finally {
      mutex.unlock();
    }
  }

  public void setEmpty() {
    mutex.lock();
    try {
      m_empty = true;
      m_plein = false;
      System.out.println("empty");
      empty.signalAll();
    } finally {
      mutex.unlock();
    }

  }

  public synchronized int getCpt() {
    return count;
  }

  public boolean conditionArret() {
    return count >= m_nbMax || poid >= m_poidMax;
  }

  public synchronized boolean getActif() {
    return actif;
  }

  public synchronized void setActif(boolean a) {
    actif = a;
  }

  @Override
  public String toString() {
    mutex.lock();
    try {
      return "Chariot : " + m_elements.toString(); // + "empty = " + m_empty + " m_plein = " + m_plein;
    } finally {
      mutex.unlock();
    }

  }
}
