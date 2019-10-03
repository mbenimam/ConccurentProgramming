package Reservation;

import java.util.ArrayList;

public class ReservationTest{
  public static void main(String[] args){


    Group[] groupes = new Group[10];
    Thread[] threads = new Thread[10];
    Salle s = new Salle(5,5);
    System.out.println(s);
    for(int i=0; i<10; i++){
      groupes[i] = new Group(5);
      threads[i] = new Thread(groupes[i]);
      threads[i].start();
    }




    for(int i=0; i<10; i++){
      threads[i].join();
    }
  }
}
