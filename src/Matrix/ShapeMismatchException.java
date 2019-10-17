package Matrix;

public class ShapeMismatchException extends Exception {

  private int x1;
  private int x2;
  private int y1;
  private int y2;

  ShapeMismatchException(int x1, int y1, int x2, int y2) {
    super();
    this.x1 = x1;
    this.x2 = x2;
    this.y1 = y1;
    this.y2 = y2;
  }

  @Override
  public String getMessage() {
    return "ShapeMismatchException : m1 of shape (" + this.x1 + ", " + this.y1 + ") and m2 of shape (" + this.x2 + ", "
        + this.y2 + ")";
  }
}
