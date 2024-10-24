import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GroundCrewHandler implements Runnable{
    private Random random = new Random();
    private Airplane airplane;
    private CountDownLatch latch;
    private AirportController airportController;


    public GroundCrewHandler(CountDownLatch latch, AirportController airportController) {
        this.latch = latch;
        this.airportController = airportController;
    }

    public void setAirplane(Airplane airplane) {
        this.airplane = airplane;
    }

    @Override
    public void run() {
        if (random.nextInt(2) + 1 == 1) {
            System.out.println("GroundCrew: We are refilling supplies for " + airportController.printAirplaneWithColor(airplane) + ".");
            try {
                Random random = new Random();
                int time = random.nextInt(500);
                Thread.sleep(time);
                airplane.addWaitingTime(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("GroundCrew: We are cleaning " + airportController.printAirplaneWithColor(airplane) + ".");
        } else {
            System.out.println("GroundCrew: We are cleaning " + airportController.printAirplaneWithColor(airplane) + ".");
            try {
                Random random = new Random();
                int time = random.nextInt(3000);
                Thread.sleep(time);
                airplane.addWaitingTime(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("GroundCrew: We are refilling supplies for " + airportController.printAirplaneWithColor(airplane) + ".");
        }
        System.out.println();
        airportController.displayMessage("| " + airportController.printAirplaneWithColor(airplane)
                + ": Done refilling & cleaning. |", airplane);

        // Signal that this process is complete
        latch.countDown();
    }
}
