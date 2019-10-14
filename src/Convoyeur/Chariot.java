package Convoyeur;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Chariot{


  private int m_poidMax;
  private int m_nbMax;


  // definition des condition de lock
  final ReentrantLock mutex = new ReentrantLock();
  final Condition Full = mutex.newCondition();
  final Condition Empty = mutex.newCondition();



  // condition de notification
  private boolean m_plein = false;
  private boolean m_empty = true;
  // compteur d'elements
  int count = 0;
  int poid = 0;
  // liste des elements du chariot
  private ArrayList<AleaObj> m_elements;




  public Chariot(int poidMax, int nbMax){
	  m_poidMax = poidMax;
	  m_nbMax = nbMax;
    // liste de taille maximale nbMax
	  m_elements = new ArrayList<AleaObj>(nbMax);
  }




  // verification
   public boolean isEmpty(){
      return m_elements.isEmpty();
   }





   // charger des elements dans le chariot
   public void charger(AleaObj obj) throws InterruptedException{
		mutex.lock();
		try {
			while(conditionArret()) {
				empty.await();
			}
        m_elements.add(obj);

        count++;
        poid += obj.getPoid();

        if(conditionArret())
          empty.signal();

		}finally {
			mutex.unlock();
		}

   }
public AleaObj decharger() throws InterruptedException{
	mutex.lock();
	try {
		if(count == 0) {
			full.await();
		}
    AleaObj obj = m_elements.remove(0);
		count--;
    poid -= obj.getPoid();
    // si on a tout dechargé
    if(count == 0)
		  empty.signal();

	}finally {
		mutex.unlock();
	}

}


  public boolean conditionArret(){
    return count == m_elements.size() || poid >= poidMax;
  }
  @Override
  public String toString(){

  }
}
