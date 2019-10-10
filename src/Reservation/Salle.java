package Reservation;

import java.util.Arrays;
import java.util.LinkedList;

public class Salle{

  private int m_nbRangs;
  private int m_nbPlacesParRangs;
  private int[][] m_placesLibres;

  Salle(int nbRangs, int nbPlacesParRangs){
    m_nbRangs = nbRangs;
    m_nbPlacesParRangs = nbPlacesParRangs;
    m_placesLibres = new int[m_nbRangs][m_nbPlacesParRangs];
  }
  boolean capaciteOK(int n){
    int cpt=0;
    for(int[] row: m_placesLibres){
      for(int libre: row){
        if(libre == 0) {
        	cpt++;
        	//System.out.println("cpt = " + cpt);
            if(cpt >= n)return true;
        }
      }
    }
    return false;
  }
  public int  nContiguesAuRangI (int n, int i){

    int cpt = 0;
    int index = 0;

    for(int j=0; j<m_nbPlacesParRangs; j++){
      if(m_placesLibres[i][j] == 0){
        cpt++;
        if(cpt >= n) {
        	return index;
        }
      }else {
          index = j;
          cpt=0;
      }
    }
    return -1;
  }
  public boolean reserverContigues(int n, int id, LinkedList<Integer> ar){
    int r = -1;
    for(int i=0; i<m_nbRangs; i++){
      r = nContiguesAuRangI(n, i);
      if(r != -1){
        for(int j=r; j<r+n; j++){
          m_placesLibres[i][j] = id;
          ar.push(i*m_nbPlacesParRangs+j);
        }
        return true;
      }
    }
    return false;
  }
  // producteur
  public boolean reserver(int n, int id, LinkedList<Integer> ar){
	  synchronized(this) {
		System.out.println( id+" is CONSUMER OWNED LOCK");
	    if(capaciteOK(n)){
	    	
	     if(reserverContigues(n, id, ar)){
	    	    System.out.println(this);
	    	    return true;
	      }else{
	        int tmp = n;
	        for(int i=0; i<m_nbRangs; i++){
	          for(int j=0; j<m_nbPlacesParRangs; j++){
	            if(m_placesLibres[i][j] == 0){
	              m_placesLibres[i][j] = id;
	              ar.push(i*m_nbPlacesParRangs+j);
	              tmp--;
	              if(tmp == 0){
	            	    System.out.println(this);
	            	    notify();
	                return true;
	                
	              }
	            }
	          }
	        }
	      }
	    }
	  }
	    System.out.println(this);
		return false;
	  }

  public  boolean annuler(int n, int id, LinkedList<Integer> ar) {
	  synchronized(this) {
		  System.out.println( id+" is PRODUCER OWNED LOCK");
		  int index;
		  for(int i=0; i<n; i++) {
			  index = ar.pop();
			  m_placesLibres[(int)(index/m_nbPlacesParRangs)][index%m_nbPlacesParRangs] = 0;
		  }
		  notify();
		  return true;
	  }
  }


  @Override
  public String toString(){
	String s = ""; 
    for(int[] row: m_placesLibres){
        s+=Arrays.toString(row)+"\n";
    }
	return s;
  }
}
