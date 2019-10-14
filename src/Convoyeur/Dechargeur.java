package Convoyeur;


public class Dechargeur implements Runnable{

  private Chariot m_chariot;
  private AleaStock m_stock;

  public Dechargeur(Chariot chariot, AleaStock stock){
    m_chariot = chariot;
    m_stock = stock;
  }


  @Override
  public void run(){
    while(!m_stock.empty()){
      try {
		m_chariot.decharger();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
  }
}
