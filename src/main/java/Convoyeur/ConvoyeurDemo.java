package Convoyeur;

public class ConvoyeurDemo {

  public static void main(String[] args) {

    AleaStock stock = new AleaStock(10);
    for (int i = 0; i < 10; i++) {
      stock.addAlea(1, 10);
    }
    System.out.println("Stock : \n" + stock);
    Chariot chariot = new Chariot(30, 10);
    // tableau des producteur
    Chargeur[] chargeurs = new Chargeur[5];
    // tableau des consomateur
    Dechargeur[] dechargeurs = new Dechargeur[5];

    Thread[] t_chargeurs = new Thread[5];
    Thread[] t_dechargeurs = new Thread[5];

    for (int i = 0; i < 5; i++) {

      chargeurs[i] = new Chargeur(chariot, stock);
      t_chargeurs[i] = new Thread(chargeurs[i]);
      dechargeurs[i] = new Dechargeur(chariot, stock);
      t_dechargeurs[i] = new Thread(dechargeurs[i]);

      t_chargeurs[i].start();
      t_dechargeurs[i].start();
    }

    for (int i = 0; i < 5; i++) {
      try {
        t_chargeurs[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      try {
        t_dechargeurs[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }

  }
}
