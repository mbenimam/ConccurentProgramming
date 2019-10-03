package Reservation;

public class Salle{

  private int m_nbRangs;
  private int m_nbPlacesParRangs;
  private boolean[][] m_placesLibres;

  Salle(int nbRangs, int nbPlacesParRangs){
    m_nbRangs = nbRangs;
    m_nbPlacesParRangs = nbPlacesParRangs;
    m_placesLibres = new boolean[m_nbPlacesParRangs][m_nbRangs];
  }
  capaciteOK(int n){
    int cpt=0;
    for(boolean[] row: m_placesLibres){
      for(boolean libre: row){
        if(libre)cpt++;
      }
    }
  }
  public int nContiguesAuRangI (int n, int i){

    int cpt = 0;
    int index = 0;

    for(int j=0; j<m_nbPlacesParRangs; j++]){
      if(m_placesLibres[i][j]){
        cpt++;
      }else{
        if(cpt >= n){
          return index;
        }
        index = j;
        cpt=0
      }
      return -1;
    }

    return maxContigu;
  }
  public boolean reserverContigues(int n){
    int r = -1;
    for(int i=0; i<m_nbRangs; i++){
      r = nContiguesAuRangI(n, i);
      if(r){
        for(int j=r; j<r+n; j++){
          m_placesLibres[i][j] = true;
        }
        return true;
      }
    }
    return false;
  }
  // producteur
  public synchronized boolean reserver(int n){
    if(capaciteOK(n)){
      if(reserverContigues(n)){
        return true;
      }else{
        int tmp = n;
        for(int i=0; i<m_nbRangs; i++){
          for(int j=0; j<m_nbPlacesParRangs; j++){
            if(!m_placesLibres[i][j]){
              m_placesLibres[i][j] = true;
              tmp--;
              if(tmp == 0){
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }


  @overide
  public String toString(){
    for(boolean[] row: m_placesLibres){
        System.out.println(Arrays.toString(row));
      }
    }
  }
}
