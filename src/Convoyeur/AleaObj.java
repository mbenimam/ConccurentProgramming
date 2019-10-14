package Convoyeur;



public class AleaObj{

  private int m_poid;


  public AleaObj(int min, int max){
    m_poid = min + (int)(Math.random()) * ((max-min)+1);
  }

  public int getPoid(){
    return m_poid;
  }


  @Override
  public String toString(){
    return "Marchandise poid : " + m_poid;
  }
}
