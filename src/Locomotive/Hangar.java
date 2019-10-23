package Locomotive;

public class Hangar {
  private static int id = 1;

  private int m_id;
  private boolean occupied = false;

  public Hangar() {
    m_id = Hangar.id++;
  }

  public void entrer(int id) {
    occupied = true;
    System.out.println("Loco " + id + " Enterred hangar " + m_id);
  }

  public boolean isEmpty() {
    return occupied;
  }
}
