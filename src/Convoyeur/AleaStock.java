package Convoyeur;

import java.util.ArrayList;


public class AleaStock{


  // taille maximale du stock
  private int m_taille;
  // le stock
  private ArrayList<AleaObj> stock;




// initialisation
  public AleaStock(int taille){
    m_taille = taille;
    stock = new ArrayList<AleaObj>(m_taille);
  }



  // ajouter un element aleatoire de poid de min a max
  public void addAlea(int min, int max){
    stock.add(new AleaObj(min, max));
  }


  // verifier si le stock est vide
  public boolean empty(){
    return stock.isEmpty();
  }


  // supprimer l'element du stock et le retourner
  public AleaObj retirer(){
    return stock.remove(0);
  }


  @Override
  public String toString(){
    return stock.toString();
  }

}
