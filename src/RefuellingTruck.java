public class RefuellingTruck {
    private boolean occupied;

    public RefuellingTruck() {
        occupied = false;
    }

    public synchronized void refuel() throws InterruptedException {
        while (occupied) {
            wait();
        }
        occupied = true;
    }

    public synchronized void endRefuel() {
        occupied = false;
        notifyAll();
    }
}
