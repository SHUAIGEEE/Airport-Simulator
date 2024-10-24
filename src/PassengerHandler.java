import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class PassengerHandler implements Runnable{

    private int passengerCount;
    private Random random = new Random();
    private Airplane airplane;
    private CountDownLatch latch;
    private AirportController airportController;

    public PassengerHandler(CountDownLatch latch, AirportController airportController) {
        this.latch = latch;
        this.airportController = airportController;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    @Override
    public void run() {
        for (int i = 1; i <= passengerCount; i++) {
            if (random.nextInt(2) + 1 == 1) {
                System.out.println("Passenger " + i + ": I'm embarking to " + airportController.printAirplaneWithColor(airplane) + ".");
            } else {
                System.out.println("Passenger " + i + ": I'm disembarking from " + airportController.printAirplaneWithColor(airplane) + ".");
            }
            try {
                Random random = new Random();
                int time = random.nextInt(500);
                Thread.sleep(time);
                airplane.addWaitingTime(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
        airportController.displayMessage("| " + airportController.printAirplaneWithColor(airplane)
                + ": All passenger done embarking & disembarking. |", airplane);

        // Signal that this process is complete
        latch.countDown();
    }
}
