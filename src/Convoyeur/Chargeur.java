package Convoyeur;


public class Chargeur implements Runnable{

  private Chariot m_chariot;
  private AleaStock m_stock;

  public Chargeur(Chariot chariot, AleaStock stock){
    m_chariot = chariot;
    m_stock = stock;
  }


  @Override
  public void run(){
    while(!m_stock.empty()){
      try {
		m_chariot.charger();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
  }
}
