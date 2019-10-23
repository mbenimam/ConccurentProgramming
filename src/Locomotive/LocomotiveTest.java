package Locomotive;

import java.util.Arrays;

public class LocomotiveTest {

  public static void main(String[] args) {

    int n = 2; // Integer.parseInt(args[1]);
    // structures
    SegAcceuil sAcceuil = new SegAcceuil();
    SegTournant sTouranant = new SegTournant();
    HangarPool pHangars = new HangarPool(n);
    // locomotives
    Loco[] locos = new Loco[n];
    // Arrays.fill(locos, new Loco(sAcceuil, sTouranant, pHangars));
    Thread[] locoThreads = new Thread[n];

    Thread server = new Thread(sTouranant);
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
