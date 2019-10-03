package ThreadedWindow;
import graphic.*;
import graphic.Window;

import java.awt.*;

public class WindowThread{
    public static void main(String[] args){
        Window w = new Window(640, 480, "Multithreaded Window");
        DessineLigne l1 = new DessineLigne(w, new Point(10,20), new Point(300, 350));
        DessineLigne l2 = new DessineLigne(w, new Point(150,300), new Point(10, 20));
        DessineLigne l3 = new DessineLigne(w, new Point(150,300), new Point(300, 350));
        //w.plotLine(new Point(10,20), new Point(300, 350));
        //w.plotLine(new Point(150,300), new Point(10, 20));
        //w.plotLine(new Point(150,300), new Point(300, 350));
        l1.start();
        l2.start();
        l3.start();
        DessineLigneRunnable r1 = new DessineLigneRunnable(w, new Point(150,250), new Point(400, 400));
        DessineLigneRunnable r2 = new DessineLigneRunnable(w, new Point(200,100), new Point(150, 250));
        DessineLigneRunnable r3 = new DessineLigneRunnable(w, new Point(200,100), new Point(400, 400));
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        t1.start();
        t2.start();
        t3.start();
    }
}
class DessineLigne extends Thread{
    Point p1;
    Point p2;
    Window w;
    DessineLigne(Window w, Point p1, Point p2){
        super();
        this.p1 = p1;
        this.p2 = p2;
        this.w = w;
    }
    @Override
    public void run() {
        w.plotLine(p1, p2);
    }
}
class DessineLigneRunnable implements Runnable{
    Point p1;
    Point p2;
    Window w;
    DessineLigneRunnable(Window w, Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
        this.w = w;
    }
    public void run() {
        w.plotLine(p1, p2);
    }
}