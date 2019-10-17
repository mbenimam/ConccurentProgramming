package Reservation;

import java.util.LinkedList;;

public class Groupe implements Runnable {

  private int m_effectif;
  private int m_id;
  private Salle m_salle;
  private LinkedList<Integer> m_places;
  private static int id = 1;

  Groupe(int effectif, Salle salle) {
    this.m_effectif = effectif;
    this.m_id = Groupe.id;
    this.m_salle = salle;
    this.m_places = new LinkedList<Integer>();
    Groupe.id++;
  }

  // consomateur
  @Override
  public String toString() {
    return m_id + "\n" + m_places.toString();
  }

  public void annuler(int n) {
    m_salle.annuler(n, m_id, m_places);
    // System.out.println(this);
  }

  public void run() {
    while (m_places.isEmpty()) {
      m_salle.reserver(m_effectif, m_id, m_places);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    annuler(m_effectif);

    System.out.println(this);
  }
}
