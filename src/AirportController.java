import java.util.*;
import java.util.concurrent.Semaphore;

public class AirportController implements Runnable{

    private static int totalAirplaneNum = 0;
    private final String ATCName = "ATC";
    private Runway runway;
    private Gate[] gates;
    private PriorityQueue<Airplane> landingQueue;
    private PriorityQueue<Airplane> takeoffQueue;
    private Semaphore airportSemaphore;
    private RefuellingTruck refuellingTruck;
    private Map<Integer, String> airplaneColorMap;
    public LinkedList<Airplane> airplanesAfterTookOff;

    public AirportController() {
        runway = new Runway();
        gates = new Gate[3];
        for (int i = 0; i < 3; i++) {
            gates[i] = new Gate(i + 1, this);
        }
        landingQueue = new PriorityQueue<>(new AirplaneComparator());
        takeoffQueue = new PriorityQueue<>(new AirplaneComparator());
        airportSemaphore = new Semaphore(3);
        refuellingTruck = new RefuellingTruck();
        airplaneColorMap = new HashMap<>();
        airplanesAfterTookOff = new LinkedList<>();
    }

    public synchronized void registerAirplaneAndColor(int id, String colorCode) {
        totalAirplaneNum++;
        airplaneColorMap.put(id, colorCode);
    }

    public void addLandingQueue(Airplane airplane) throws InterruptedException {
        System.out.println(printAirplaneWithColor(airplane) + ": Arrive the airport at " + airplane.getTime() + ".");

        synchronized (landingQueue)
        {
            landingQueue.offer(airplane);
            System.out.println(printATCMessage() + ": " + printAirplaneWithColor(airplane) + " got the LANDING queue.");
        }
        Random random = new Random();
        int time = random.nextInt(1000);
        Thread.sleep(time);
        airplane.addWaitingTime(time);
    }

    public synchronized void requestLanding(Airplane airplane) throws InterruptedException {
        registerAirplaneAndColor(airplane.getId(), ColorCodes.generateRandomColorCode());
        addLandingQueue(airplane);
        System.out.println(printAirplaneWithColor(airplane) + ": Request to land.");
        synchronized (landingQueue)
        {
            airplane = (Airplane) ((PriorityQueue<?>) landingQueue).poll();
            System.out.println(printATCMessage() + ": Received " + printAirplaneWithColor(airplane) + " LANDING request.");
        }
        try
        {
            land(airplane);
        }
        catch(InterruptedException iex)
        {
            iex.printStackTrace();
        }
    }

    private void land(Airplane airplane) throws InterruptedException {
        int before = (int) System.currentTimeMillis();
        airportSemaphore.acquire();
        runway.land();
        int after = (int) System.currentTimeMillis() - before;
        airplane.addWaitingTime(after);

        Random random = new Random();
        int time = random.nextInt(3000);

        System.out.println(printATCMessage() + ": " + printAirplaneWithColor(airplane) + " You can land now.");
        displayMessage("| " + printAirplaneWithColor(airplane) + ": We are landing now. |", airplane);
        Thread.sleep(time);
        airplane.addWaitingTime(time);
        System.out.println(printAirplaneWithColor(airplane) + ": We landed and now coasting to Gate.");
        airplane.setStatus(AirplaneStatus.LANDED);

        runway.release();
    }

    public void requestDocking(Airplane airplane) throws InterruptedException {
        for (Gate gate : gates) {
            if (gate.isOccupied()) continue;
            gate.dock(airplane);
            displayMessage("| " + printAirplaneWithColor(airplane) + ": Docked at Gate " + gate.getId() + ". |", airplane);
            break;
        }
        Random random = new Random();
        int time = random.nextInt(3000);
        Thread.sleep(time);
        airplane.addWaitingTime(time);
    }

    public void requestGroundProcess(Airplane airplane) throws InterruptedException {
        // Disembark/Embark + Refill/Cleaning
        Random random = new Random();
        int passengerCount = random.nextInt(50) + 1;
        Gate currentGate = airplane.getGate();
        currentGate.process(passengerCount);
        airplane.setPassengerCount(passengerCount);

        // Refuel
        tryRefuel(airplane);

        airplane.setStatus(AirplaneStatus.PROCESSED);
    }

    public void tryRefuel(Airplane airplane) throws InterruptedException {
        int before = (int) System.currentTimeMillis();
        refuellingTruck.refuel();
        int after = (int) System.currentTimeMillis() - before;
        airplane.addWaitingTime(after);

        Random random = new Random();
        int time = random.nextInt(3000);

        System.out.println("RefuellingCrew: We are refuelling " + printAirplaneWithColor(airplane) + " now.");
        Thread.sleep(time);
        airplane.addWaitingTime(time);
        System.out.println("RefuellingCrew: Done refuelling " + printAirplaneWithColor(airplane) + " now.");

        displayMessage("| " + printAirplaneWithColor(airplane) + ": The fuel is full. |", airplane);
        refuellingTruck.endRefuel();
    }

    public void requestUndocking(Airplane airplane) throws InterruptedException {
        Gate currentGate = airplane.getGate();
        currentGate.undock();
        displayMessage("| " + printAirplaneWithColor(airplane) + ": Undocked at Gate " + currentGate.getId() + ". |", airplane);
        airplane.setStatus(AirplaneStatus.UNDOCKED);

        Random random = new Random();
        int time = random.nextInt(3000);
        Thread.sleep(time);
        airplane.addWaitingTime(time);
    }

    public void prepareTakeOff(Airplane airplane) throws InterruptedException {
        Random random = new Random();
        int time = random.nextInt(3000);
        Thread.sleep(time);
        airplane.addWaitingTime(time);
        airplane.setTime(new Date());
        airplane.setStatus(AirplaneStatus.TAKEOFF);
    }

    public void addTakeOffQueue(Airplane airplane) {
        System.out.println(printAirplaneWithColor(airplane) + ": Leaving the airport.");

        synchronized (takeoffQueue)
        {
            takeoffQueue.offer(airplane);
            System.out.println(printATCMessage() + ": " + printAirplaneWithColor(airplane) + " got the TAKEOFF queue.");
        }
    }

    public void requestTakeoff(Airplane airplane) throws InterruptedException {
        addTakeOffQueue(airplane);
        System.out.println(printAirplaneWithColor(airplane) + ": Request to take off.");
        synchronized (takeoffQueue)
        {
            airplane = (Airplane) ((PriorityQueue<?>) takeoffQueue).poll();
            System.out.println(printATCMessage() + " Received " + printAirplaneWithColor(airplane) + " TAKEOFF request.");
        }
        try
        {
            takeoff(airplane);
        }
        catch(InterruptedException iex)
        {
            iex.printStackTrace();
        }
    }

    private void takeoff(Airplane airplane) throws InterruptedException {
        int before = (int) System.currentTimeMillis();
        runway.takeoff();
        int after = (int) System.currentTimeMillis() - before;
        airplane.addWaitingTime(after);

        Random random = new Random();
        int time = random.nextInt(3000);

        System.out.println(printATCMessage() + ": " + printAirplaneWithColor(airplane) + " You can take off now.");
        displayMessage("| " + printAirplaneWithColor(airplane) + ": We are taking off now. |", airplane);
        Thread.sleep(time);
        airplane.addWaitingTime(time);
        System.out.println(printAirplaneWithColor(airplane) + ": We took off.");

        runway.release();
        airportSemaphore.release();
    }

    public void displayMessage(String message, Airplane airplane) {
        int length = message.length() - airplaneColorMap.get(airplane.getId()).length() - ColorCodes.RESET.length();
        System.out.println("\n" + generateSeparatorLine('=', length));
        System.out.println(message);
        System.out.println(generateSeparatorLine('=', length));
    }

    public String generateSeparatorLine(char symbol, int count) {
        char[] symbolArray = new char[count];
        Arrays.fill(symbolArray, symbol);
        return new String(symbolArray);
    }

    public String printAirplaneWithColor(Airplane airplane) {
        String colorCode = airplaneColorMap.get(airplane.getId());
        return colorCode + "[Airplane" + airplane.getId() + "]" + ColorCodes.RESET;
    }

    public String printATCMessage() {
        return ColorCodes.ATC_WHITE + "[" + ATCName + "]" + ColorCodes.RESET;
    }

    public boolean gatesAreEmpty() {
        for (Gate i : gates) {
            if (i.isOccupied()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (!gatesAreEmpty()) {
            System.out.println(printATCMessage() + ": Gates are not empty.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(printATCMessage() + ": Gates are all empty.");

        while (totalAirplaneNum != airplanesAfterTookOff.size()) {
            System.out.println(printATCMessage() + ": Waiting for all airplane to take off.");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(printATCMessage() + ": All airplanes had took off.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        synchronized (airplanesAfterTookOff) {
            Airplane waitMax = null;
            Airplane waitMin = null;
            int maxWaitingTime = 0;
            int minWaitingTime = 0;
            int totalWaitingTime = 0;
            int avgWaitingTime;
            int totalPassengerServed = 0;
            int totalAirplaneServed = airplanesAfterTookOff.size();

            for (Airplane a : airplanesAfterTookOff) {
                if (maxWaitingTime == 0) {
                    maxWaitingTime = a.getWaitingTime();
                    waitMax = a;
                }
                else {
                    if (a.getWaitingTime() > maxWaitingTime) {
                        maxWaitingTime = a.getWaitingTime();
                        waitMax = a;
                    }
                }
                if (minWaitingTime == 0) {
                    minWaitingTime = a.getWaitingTime();
                    waitMin = a;
                }
                else {
                    if (a.getWaitingTime() < minWaitingTime) {
                        minWaitingTime = a.getWaitingTime();
                        waitMin = a;
                    }
                }
                totalWaitingTime += a.getWaitingTime();
                totalPassengerServed += a.getPassengerCount();
            }

            avgWaitingTime = totalWaitingTime / totalAirplaneServed;

            String message = "|                     STATISTICS                     |";
            System.out.println();
            System.out.println(generateSeparatorLine('=', message.length()));
            System.out.println(message);
            System.out.println(generateSeparatorLine('=', message.length()));

            System.out.println("============ WAITING TIME ============");
            System.out.println("MAX: " + printAirplaneWithColor(waitMax) + " -> " + maxWaitingTime + " milliseconds");
            System.out.println("MIN: " + printAirplaneWithColor(waitMin) + " -> " + minWaitingTime + " milliseconds");
            System.out.println("AVG: " + avgWaitingTime + " milliseconds");
            System.out.println(generateSeparatorLine('=', 38));
            System.out.println("Airplane Served: " + totalAirplaneServed);
            System.out.println("Passenger Served: " + totalPassengerServed);
            System.out.println(generateSeparatorLine('=', 38));
        }
    }
}
