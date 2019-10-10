package Convoyeur;


public class Chargeur impements Runnable{

  private Chariot m_chariot;
  private AleaStock m_stock;

  public Chargeur(Chariot chariot, Aleastock stock){
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
