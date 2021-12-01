package es.grupo9;

import java.text.DecimalFormat;

public class Utils{

    /**
     * Returns the cost based on the hours worked and the price per hour.
     * @param hours hours worked.
     * @param price cost per hour.
     * @return int cost of the hours worked.
     */
    public static Double getCost(Double hours, int price) {
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        Double cost = hours * price;

        return Double.valueOf(df.format(cost));
    }

    /**
     * Returns the cost based on the hours worked with a default price of 20 per hour.
     * @param hours hours worked.
     * @return int cost of the hours worked.
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