package Locomotive;

public class Loco implements Runnable {

  private SegAcceuil sAcceuil;

  private SegTournant sTournant;

  private HangarPool pHangars;

  private int m_id;

  private static int id = 0;

  public Loco(SegAcceuil sAcceuil, SegTournant sTournant, HangarPool pHangars) {

    this.sAcceuil = sAcceuil;
    this.sTournant = sTournant;
    this.pHangars = pHangars;
    this.m_id = Loco.id++;
    // System.out.println("m_id = " + m_id);
  }

  public void run() {
    try {
      // entrer dans le segment d'acceuil uniquement quand c'est disponible
      sAcceuil.reserver();
      // on a reserver le segment d'acceuil, on appelle alors le segment tournant pour
      // pointer sur le SegAcceuil
      sTournant.appeler(0);
      // on attend que le segment tourne
      sTournant.attendrePositionOk();
      // segment tournant pointe sur le seg acceuil, loco peut entrer
      sTournant.entrer(m_id);
      // on sort du segment d'acceuil pour laisser la place
      sAcceuil.liberer();
      // attendre que le seg tournant pointe sur un hangar
      sTournant.attendrePositionOk();
      // on entre dans le segment pointe par seg tournant
      pHangars.getHangar(sTournant.getPosition()).entrer(m_id);
      // on sort du segTournant pour le liberer
      sTournant.sortir(m_id);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      // System.out.println("Loco " + m_id + " interrupted should not happen");
    }
  }
}
