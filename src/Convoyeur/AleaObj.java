package Convoyeur;
import java.Math;


public class AleaObj{

  private int m_poid;


  public AleaObj(int min, int max){
    m_poid = min + (int)(Math.rabdom()) * ((max-min)+1);
  }

  public int getPoid(){
    return m_poid;
  }


  @Override
  public String toString(){
    return "Marchandise poid : " + m_poid;
  }
}
