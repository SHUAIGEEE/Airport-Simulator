public class Runway {
    private boolean occupied;

    public Runway() {
        occupied = false;
    }

    public synchronized void land() throws InterruptedException {
        while (occupied) {
            wait();
        }
        occupied = true;
    }

    public synchronized void takeoff() throws InterruptedException {
        while (occupied) {
            wait();
        }
        occupied = true;
    }

    public synchronized void release() {
        occupied = false;
        notifyAll();
    }
}
