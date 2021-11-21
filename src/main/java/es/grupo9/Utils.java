package es.grupo9;

public class Utils{

    /**
     * Returns the cost based on the hours worked and the cost per hour.
     * @param hours hours worked.
     * @param cost cost per hour.
     * @return int cost of the hours worked.
     */
    public static int getCost(int hours, int cost) {
        return hours * cost;
    }

    /**
     * Returns the cost based on the hours worked with a default cost of 20.
     * @param hours hours worked.
     * @return  int cost of the hours worked.
     */
    public static int getCost(int hours) {
        return getCost(hours, 20);
    }

}