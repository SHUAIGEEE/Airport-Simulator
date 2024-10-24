import java.util.Date;
import java.util.Random;

public class AirplaneGenerator implements Runnable
{
    AirportController airportController;
    private boolean closingTime = false;
    private boolean isCongestedScenario;

    public AirplaneGenerator(AirportController airportController, boolean isCongestedScenario)
    {
        this.airportController = airportController;
        this.isCongestedScenario = isCongestedScenario;
    }

    @Override
    public void run()
    {
        String scenario = isCongestedScenario ? "CONGESTED SCENARIO" : " NORMAL SCENARIO";
        System.out.println("\n=========== " + scenario + " ===========");

        int id = 1;
        if (isCongestedScenario) {

            System.out.println(ColorCodes.EMERGENCY + "<<<<<< Airplane 5 ~> "
                    + ColorCodes.UNDERLINE_ON + "FUEL SHORTAGE" + ColorCodes.UNDERLINE_OFF + " >>>>>>" + ColorCodes.RESET);
            System.out.println();

            Airplane airplane1 = new Airplane(1, airportController, 1);
            airplane1.setTime(new Date());
            Thread thAirplane1 = new Thread(airplane1);
            thAirplane1.start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Airplane airplane2 = new Airplane(2, airportController, 1);
            airplane2.setTime(new Date());
            Thread thAirplane2 = new Thread(airplane2);
            thAirplane2.start();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Airplane airplane3 = new Airplane(3, airportController, 1);
            airplane3.setTime(new Date());
            Thread thAirplane3 = new Thread(airplane3);
            thAirplane3.start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Airplane airplane4 = new Airplane(4, airportController, 1);
            airplane4.setTime(new Date());
            Thread thAirplane4 = new Thread(airplane4);
            thAirplane4.start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Airplane airplane5 = new Airplane(5, airportController, 2);
            airplane5.setTime(new Date());
            Thread thAirplane5 = new Thread(airplane5);
            thAirplane5.start();
        }
        else {
            while(!closingTime)
            {
                Random random = new Random();
                Airplane airplane = new Airplane(id, airportController, 1);
                airplane.setTime(new Date());
                Thread thAirplane = new Thread(airplane);
                thAirplane.start();
                id++;

                try
                {
                    Thread.sleep(random.nextInt(3000));
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (closingTime)
            {
                try {
                    Thread.sleep(5000);
                    System.out.println("\n" + ColorCodes.BOLD_ITALIC + "------CLOSE------");
                    System.out.println("The airport will accept a final airplane. One airplane coming in." + ColorCodes.RESET + "\n");
                    Airplane airplane = new Airplane(id, airportController, 1);
                    airplane.setTime(new Date());
                    Thread thAirplane = new Thread(airplane);
                    thAirplane.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setClosingTime(boolean closingTime) {
        this.closingTime = closingTime;
    }
}