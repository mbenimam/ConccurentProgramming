package Convoyeur;

import java.util.ArrayList;

public class AleaStock {

  // taille maximale du stock
  private int m_taille;
  // le stock
  private ArrayList<AleaObj> stock;

  private int cpt = 0;

  // initialisation
  public AleaStock(int taille) {
    m_taille = taille;
    stock = new ArrayList<AleaObj>(m_taille);
  }

  // ajouter un element aleatoire de poid de min a max
  public synchronized void addAlea(int min, int max) {
    cpt++;
    stock.add(new AleaObj(min, max));
  }

  // verifier si le stock est vide
  public synchronized boolean empty() {
    return stock.isEmpty();
  }

  // supprimer l'element du stock et le retourner
  public synchronized AleaObj retirer() {
    return stock.remove(0);
  }

  public synchronized void ajouter(AleaObj a) {
    stock.add(a);
  }

  public synchronized int getCpt() {
    return cpt;
  }

  public synchronized void inc() {
    cpt++;
  }

  public synchronized void dec() {
    cpt--;
  }

  @Override
  public String toString() {
    return stock.toString();
  }

}
