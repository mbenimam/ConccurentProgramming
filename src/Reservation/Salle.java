package Reservation;

import java.util.Arrays;

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
  public boolean reserverContigues(int n, int id){
    int r = -1;
    for(int i=0; i<m_nbRangs; i++){
      r = nContiguesAuRangI(n, i);
      if(r != -1){
        for(int j=r; j<r+n; j++){
          m_placesLibres[i][j] = id;
        }
        return true;
      }
    }
    return false;
  }
  // producteur
  public synchronized boolean reserver(int n, int id){
    if(capaciteOK(n)){
      if(reserverContigues(n, id)){
    	    System.out.println(this);
        return true;
      }else{
        int tmp = n;
        for(int i=0; i<m_nbRangs; i++){
          for(int j=0; j<m_nbPlacesParRangs; j++){
            if(m_placesLibres[i][j] == 0){
              m_placesLibres[i][j] = id;
              tmp--;
              if(tmp == 0){
            	    System.out.println(this);
                return true;
                
              }
            }
          }
        }
      }
    }
    System.out.println(this);
    return false;
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
