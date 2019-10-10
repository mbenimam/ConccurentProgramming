package Convoyeur;

import java.util.ArrayList;


public class AleaStock{


  private int m_taille;
  private ArrayList<AleaObj> stock;



  public AleaStock(int taille){
    m_taille = taille;
    stock = new ArrayList<AleaObj>(m_taille) stock;
  }
  public void addAlea(int min, int max){
    stock.add(new AleaObj(min, max));
  }

  public boolean empty(){
    return stock.isEmty();
  }

  
  @Override
  public String toString(){
    return stock.toString();
  }

}
