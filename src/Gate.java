import java.util.concurrent.CountDownLatch;

public class Gate {
    private int id;
    private boolean occupied;
    private Airplane airplane;
    private AirportController airportController;

    public Gate(int id, AirportController airportController) {
        this.id = id;
        occupied = false;
        this.airportController = airportController;
    }

    public void dock(Airplane airplane) {
        // Dock the airplane
        this.airplane = airplane;
        airplane.setGate(this);
        airplane.setStatus(AirplaneStatus.DOCKED);
        occupied = true;
    }

    public void process(int passengerCount) throws InterruptedException {
        // Wait until passengers and ground crew finish their process
        CountDownLatch latch = new CountDownLatch(2);

        PassengerHandler passengerHandler = new PassengerHandler(latch, airportController);
        Thread psgThread = new Thread(passengerHandler);
        GroundCrewHandler groundCrewHandler = new GroundCrewHandler(latch, airportController);
        Thread gcThread = new Thread(groundCrewHandler);

        passengerHandler.setAirplane(airplane);
        passengerHandler.setPassengerCount(passengerCount);
        groundCrewHandler.setAirplane(airplane);

        psgThread.start();
        gcThread.start();

        latch.await();
    }
    public void undock() {
        // Undock the airplane
        Airplane airplane = this.airplane;
        airplane.setGate(null);
        airplane.setStatus(AirplaneStatus.UNDOCKED);
        this.airplane = null;
        occupied = false;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }
}
