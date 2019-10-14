package Convoyeur;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Chariot{

  private int m_poidMax;
  private int m_nbMax;
  
  final ReentrantLock mutex = new ReentrantLock();
  final Condition notFull = mutex.newCondition();
  final Condition notEmpty = mutex.newCondition();

  private boolean m_plein = false;
  int count;
  private ArrayList<AleaObj> m_elements;

  public Chariot(int poidMax, int nbMax){
	  m_poidMax = poidMax;
	  m_nbMax = nbMax;
	  m_elements = new ArrayList<AleaObj>();
  }
   public boolean isEmpty(){
      return m_elements.isEmpty();
   }
   public void charger() throws InterruptedException{
		mutex.lock();
		try {
			if(count == m_elements.size()) {
				notFull.await();
			}
			m_elements.add(new AleaObj(0, 10));
			count++;
			notEmpty.signal();
			
		}finally {
			mutex.unlock();
		}
		
   }
public void decharger() throws InterruptedException{
	mutex.lock();
	try {
		if(count == 0) {
			notEmpty.await();
		}
		count--;
		notFull.signal();
		
	}finally {
		mutex.unlock();
	}
	
}
}
