package Locomotive;

import java.util.Arrays;

public class LocomotiveDemo {

  public static void main(String[] args) {

    int n = 17; // Integer.parseInt(args[1]);
    // structures
    SegAcceuil sAcceuil = new SegAcceuil();
    HangarPool pHangars = new HangarPool(n);
    SegTournant sTouranant = new SegTournant(pHangars);
    // locomotives
    Loco[] locos = new Loco[n];
    // Arrays.fill(locos, new Loco(sAcceuil, sTouranant, pHangars));
    Thread[] locoThreads = new Thread[n];

    // the turntable loops forever, so it must not keep the JVM alive after the locos finish
    Thread server = new Thread(sTouranant);
    server.setDaemon(true);
    server.start();

    for (int i = 0; i < n; i++) {
      locos[i] = new Loco(sAcceuil, sTouranant, pHangars);
      locoThreads[i] = new Thread(locos[i]);
      locoThreads[i].start();
    }

    for (int i = 0; i < n; i++) {
      try {
        locoThreads[i].join();
      } catch (InterruptedException e) {
        System.out.println("Loco Interreupted");
      }

    }
  }
}
