package es.grupo9;

public class Utils{

    /**
     * Returns the cost based on the hours worked and the cost per hour.
     * @param hours hours worked.
     * @param cost cost per hour.
     * @return int cost of the hours worked.
     */
    public static Double getCost(Double hours, int cost) {
        return hours * cost;
    }

    /**
     * Returns the cost based on the hours worked with a default cost of 20.
     * @param hours hours worked.
     * @return  int cost of the hours worked.
     */
    public static Double getCost(Double hours) {
        return getCost(hours, 20);
    }

    /**
     * Calculates the sum of all elements of an array.
     * @param array array of doubles.
     * @return Double sum of all the hours.
     */
    public static Double getSum(Double[] array) {
        double sum = 0.0;

        for(Double dbl : array) if( dbl != null ) sum += dbl;

        return sum;
    }
}