package Convoyeur;

import java.util.ArrayList;

public class Chariot{

  private int m_poidMax;
  private int m_nbMax;

  private boolean m_plein = false;
  private ArrayList<AleaObj> m_elements;

  public Chariot(int poidMax, int nbMax){
	  m_poidMax = poidMax;
	  m_nbMax = nbMax;
	  m_elements = new ArrayList<AleaObj>();
  }
  public boolean empty(){
    return m_elements.isEmpty();
  }
}
