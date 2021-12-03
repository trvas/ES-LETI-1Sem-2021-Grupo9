package es.grupo9;

import java.text.DecimalFormat;

public class Utils{

    // Price per hour worked
    private static int price = 20;

    /**
     * Sets the price per hour to be used to calculate costs.
     * @param newPrice new price value.
     */
    public static void setPrice(int newPrice) {
        price = newPrice;
    }

    /**
     * Returns the cost based on the hours worked with a default price of 20 per hour.
     * @param hours hours worked.
     * @return int cost of the hours worked.
     */
    public static Double getCost(Double hours) {
        DecimalFormat df = new DecimalFormat("#.##");

        Double cost = hours * price;

        return Double.valueOf(df.format(cost));
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