import java.util.Date;

public class Airplane implements Runnable{

    private int id;
    private AirportController airportController;
    private Date time;
    private Gate gate;
    private AirplaneStatus status;
    private int priorityLevel;
    private int passengerCount;
    private int waitingTime;

    public Airplane(int id, AirportController airportController, int priorityLevel) {
        this.id = id;
        this.airportController = airportController;
        this.priorityLevel = priorityLevel;
        status = AirplaneStatus.AIRBORNE;
        passengerCount = 0;
        waitingTime = 0;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (status == AirplaneStatus.AIRBORNE) {
                    // Airplane is in the air and needs to land
                    airportController.requestLanding(this);
                } else if (status == AirplaneStatus.LANDED) {
                    // Airplane has landed and needs to dock at a gate
                    airportController.requestDocking(this);
                } else if (status == AirplaneStatus.DOCKED) {
                    // Airplane has docked to a gate and needs to take ground process
                    airportController.requestGroundProcess(this);
                } else if (status == AirplaneStatus.PROCESSED) {
                    // Airplane has done ground process and needs to undock from a gate
                    airportController.requestUndocking(this);
                } else if (status == AirplaneStatus.UNDOCKED) {
                    // Airplane has done undocking and prepare to take off
                    airportController.prepareTakeOff(this);
                } else if (status == AirplaneStatus.TAKEOFF) {
                    // Airplane done preparing and needs to take off
                    airportController.requestTakeoff(this);

                    // Add to linked list to display statistics
                    synchronized (airportController.airplanesAfterTookOff) {
                        airportController.airplanesAfterTookOff.add(this);
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(String.format("Airplane %d was interrupted.", id));
        }
    }

    public int getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public void setStatus(AirplaneStatus status) {
        this.status = status;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void addWaitingTime(int waitingTime) {
        this.waitingTime += waitingTime;
    }
}
