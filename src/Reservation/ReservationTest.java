package Reservation;

import java.util.ArrayList;

public class ReservationTest{
  public static void main(String[] args){


    Groupe[] groupes = new Groupe[10];
    Thread[] threads = new Thread[10];
    Salle s = new Salle(5,5);
    //System.out.println(s);
    for(int i=0; i<10; i++){
      groupes[i] = new Groupe(3, s);
      threads[i] = new Thread(groupes[i]);
      threads[i].start(); 
    }




    for(int i=0; i<10; i++){
      try {
		threads[i].join();
      } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }
      
    
    }

  }
}
