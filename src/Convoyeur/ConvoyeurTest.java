package Convoyeur;

public class ConvoyeurTest{

  public static void main(String[] args){


    AleaStock stock = new AleaStock(100);
    // tableau des producteur
    Chargeur[] chargeurs = new Chargeur[5];
    // tableau des consomateur
    Dechargeur[] dechargeurs = new Dechargeur[5];
    
    Thread[] t_chargeurs = new Thread[5]; 
    Thread[] t_dechargeurs = new Thread[5]; 

    for(int i=0; i<5; i++){
      t_chargeurs[i].start();
      t_dechargeurs[i].start();
    }
    
    
    
    for(int i=0; i<5; i++){
        try {
			t_chargeurs[i].join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			t_dechargeurs[i].join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }


  }
}
