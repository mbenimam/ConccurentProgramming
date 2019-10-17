package Convoyeur;

import java.util.concurrent.locks.ReentrantLock;

public class Chargeur implements Runnable {

  // le chariot a charger
  private Chariot m_chariot;

  // le stock
  private AleaStock m_stock;

  private static final Object khra = new Object();
  private static boolean setted = false;

  // initialisation
  public Chargeur(Chariot chariot, AleaStock stock) {
    m_chariot = chariot;
    m_stock = stock;
  }

  public void run() {
    AleaObj tmp;

    // tant que le stock n'est pas vide
    while (!m_stock.empty()) {
      tmp = m_stock.retirer();

      try {
        // on essaie de charger le chariot
        m_chariot.charger(tmp);
        m_stock.dec();
        // System.out.println("chargement (" + m_stock.getCpt() + ") : " + m_chariot);

      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (Exception e) {

        m_stock.ajouter(tmp);
        /*
         * try { Thread.sleep(1000); } catch (InterruptedException r) {
         *
         * }
         */
      } finally {
        // System.out.println("fucking stock : " + m_stock);
      }

    }
    System.out.println("chargeur a termine");
    synchronized (khra) {
      /*
       * try { Thread.sleep(1000); } catch (InterruptedException r) {
       *
       * }
       */
      if (m_stock.getCpt() == 0 && !setted) {
        setted = true;
        m_chariot.setFull();
        m_chariot.setActif(false);
        System.out.println("stock final should be empty " + m_stock.getCpt());
      }
    }

  }
}
