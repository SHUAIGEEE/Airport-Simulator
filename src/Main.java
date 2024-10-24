import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int option;

        while (true) {

            System.out.println("   _____         .__         __________               .__  _____.__            _____  .__                             __   ");
            System.out.println("  /  _  \\   _____|__|____    \\______   \\_____    ____ |__|/ ____\\__| ____     /  _  \\ |__|____________   ____________/  |_");
            System.out.println(" /  /_\\  \\ /  ___/  \\__  \\    |     ___/\\__  \\ _/ ___\\|  \\   __\\|  |/ ___\\   /  /_\\  \\|  \\_  __ \\____ \\ /  _ \\_  __ \\   __\\");
            System.out.println("/    |    \\\\___ \\|  |/ __ \\_  |    |     / __ \\  \\___|  ||  |  |  \\  \\___   /    |    \\  ||  | \\/  |_> >  <_> )  | \\/|  |  ");
            System.out.println("\\____|__  /____  >__(____  /  |____|    (____  /\\___  >__||__|  |__|\\___  > \\____|__  /__||__|  |   __/ \\____/|__|   |__|  ");
            System.out.println("        \\/     \\/        \\/                  \\/     \\/                  \\/          \\/          |__|");

            System.out.println("===========================================================================================================================");
            System.out.println("1. Normal Scenario");
            System.out.println("2. Congested Scenario");
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();
            try {
                option = Integer.parseInt(input);
                break;
            }
            catch (NumberFormatException exception) {
                System.out.println("Invalid Input!");
            }
        }
        boolean isCongestedScenario;
        isCongestedScenario = option != 1;

        AirportController airportController = new AirportController();
        AirplaneGenerator ag = new AirplaneGenerator(airportController, isCongestedScenario);
        Clock clock = new Clock(ag);

        Thread agThread = new Thread(ag);
        Thread clkThread = new Thread(clock);

        agThread.start();
        clkThread.start();

        try {
            clkThread.join();
            agThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Thread acThread = new Thread(airportController);
        acThread.start();
    }
}