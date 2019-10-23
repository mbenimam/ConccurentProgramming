package Locomotive;

import java.util.ArrayList;

public class HangarPool {

  private ArrayList<Hangar> pool;

  public HangarPool(int n) {
    pool = new ArrayList<Hangar>(n);

    for (int i = 0; i < n; i++) {
      pool.add(new Hangar());
    }
  }

  public Hangar getHangar(int num) {
    return pool.get(num);
  }

  public int HangarVide() {
    for (int i = 0; i < pool.size(); i++) {
      if (pool.get(i).isEmpty())
        return i;
    }
    return -1;
  }
}
