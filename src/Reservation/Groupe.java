package Reservation;

public class Groupe implements Runnable{

  private int m_effectif;
  private int m_id;
  private Salle m_salle;
  private static int id = 1;

  Groupe(int effectif, Salle salle){
    this.m_effectif = effectif;
    this.m_id = this.id;
    this.m_salle = salle;
    this.id++;
  }
  // consomateur

  @Override
  public void run(){
    m_salle.reserver(m_effectif, m_id);

  }
}
