package Convoyeur;


public class Dechargeur impements Runnable{

  private Chariot m_chariot;
  private AleaStock m_stock;

  public Dechargeur(Chariot chariot, Aleastock stock){
    m_chariot = chariot;
    m_stock = stock;
  }


  @Override
  public void run(){
    while(!m_stock.empty()){
      charger();
    }
  }
}
