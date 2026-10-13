package Convoyeur;

public class Dechargeur implements Runnable {

  private Chariot m_chariot;
  private AleaStock m_stock;

  public Dechargeur(Chariot chariot, AleaStock stock) {
    m_chariot = chariot;
    m_stock = stock;
  }

  @Override
  public void run() {
    while (m_stock.getCpt() != 0 || !m_chariot.isEmpty()) {
      try {
        m_chariot.decharger();
        // System.out.println("dechargement (" + m_stock.getCpt() + ") : " +
        // m_chariot.isEmpty());
      } catch (Exception e) {
        // System.out.println(e.getClass());
        // m_chariot.setEmpty();
      }
    }
    m_chariot.setActif(false);
    System.out.println("dechargeur a termine");
  }

}
