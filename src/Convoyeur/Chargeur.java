package Convoyeur;


public class Chargeur implements Runnable{


  // le chariot a charger
  private Chariot m_chariot;

  // le stock
  private AleaStock m_stock;



  // initialisation
  public Chargeur(Chariot chariot, AleaStock stock){
    m_chariot = chariot;
    m_stock = stock;
  }


  public void run(){
    AleaObj tmp;
    // tant que le stock n'est pas vide
    while(!m_stock.empty()){
      try {
          tmp = m_stock.retirer()
          // on essaie de charger le chariot
	        m_chariot.charger(tmp);

	    } catch (InterruptedException e) {
		      e.printStackTrace();
	    }
    }
  }
}
