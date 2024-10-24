public class Clock implements Runnable{

    private AirplaneGenerator airplaneGenerator;

    public Clock(AirplaneGenerator airplaneGenerator) {
        this.airplaneGenerator = airplaneGenerator;
    }

    @Override
    public void run() {
        try {
            // Airport open for 10 seconds
            Thread.sleep(10000);
            notifyClosingTime();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyClosingTime() {
        System.out.println("\n" + ColorCodes.BOLD_ITALIC + "The airport is closing. No airplane can enter." + ColorCodes.RESET + "\n");
        airplaneGenerator.setClosingTime(true);
    }
}
