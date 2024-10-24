import java.util.Comparator;

public class AirplaneComparator implements Comparator<Airplane> {
    @Override
    public int compare(Airplane a1, Airplane a2) {
        int priorityDiff = Integer.compare(a2.getPriorityLevel(), a1.getPriorityLevel());
        if (priorityDiff == 0) {
            // If the priority levels are the same, order the airplanes by time (FIFO)
            return a1.getTime().compareTo(a2.getTime());
        }
        return priorityDiff;
    }
}