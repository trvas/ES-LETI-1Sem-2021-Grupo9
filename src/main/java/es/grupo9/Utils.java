package es.grupo9;

import org.trello4j.model.Member;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {

    // Price per hour worked
    private static int price = 20;

    /**
     * Sets the price per hour to be used to calculate costs.
     *
     * @param newPrice new price value.
     */
    public static void setPrice(int newPrice) {
        price = newPrice;
    }

    /**
     * Returns the cost based on the hours worked with a default price of 20 per hour.
     *
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
     *
     * @param array array of doubles.
     * @return Double sum of all the hours.
     */
    public static Double getSum(Double[] array) {
        double sum = 0.0;

        for (Double dbl : array) if (dbl != null) sum += dbl;

        return sum;
    }

    /**
     * Gets all the necessary information from TrelloManager and GitManager to write into an CSV file.
     *
     * @param trelloManager trelloManager instance.
     * @param gitManager    gitManager instance.
     * @throws Exception if GitHub user is null.
     */
    public static void exportCSV(TrelloManager trelloManager, GitManager gitManager) throws Exception {
        List<String> membersInfo = new ArrayList<>();
        List<String> comActivitiesInfo = new ArrayList<>();
        List<String> notComActivitiesInfo = new ArrayList<>();
        List<String> commitsInfo = new ArrayList<>();

        // Set the column titles
        membersInfo.add("SprintNumber,Member,HoursWorked,HoursEstimated,Cost");
        comActivitiesInfo.add("SprintNumber,Member,NumberOfActivities,HoursWorked,Cost");
        notComActivitiesInfo.add("SprintNumber,Member,NumberOfActivities,HoursWorked,Cost");

        Double[] sprintHours;
        Double[] comActivities;
        Double[] notComActivities;

        Double[] globalHours = new Double[]{0.0, 0.0, 0.0};
        Double[] globalComActivities = new Double[]{0.0, 0.0, 0.0};
        Double[] globalNotComActivities = new Double[]{0.0, 0.0, 0.0};


        DecimalFormat df = new DecimalFormat("#.##");

        // Get all the necessary information from TrelloManager
        for (int sprintNumber = 1; sprintNumber != trelloManager.getSprintCount() + 1; sprintNumber++) {
            for (int i = 0; i != trelloManager.getMemberCount(); i++) {
                String memberName = trelloManager.getMembers().get(i).getFullName();
                sprintHours = trelloManager.getSprintHoursByMember(memberName, sprintNumber);
                comActivities = trelloManager.getCommittedActivitiesByMember(memberName, sprintNumber);
                notComActivities = trelloManager.getNotCommittedActivitiesByMember(memberName, sprintNumber);

                // Add the string with sprint number, member name, hours worked, estimated hours and cost
                membersInfo.add(sprintNumber + "," + memberName + "," + sprintHours[0] + "," + sprintHours[1] + ","
                        + sprintHours[2]);

                // Add the string with sprint number, member name, number of committed activities (Done),
                // hours worked, estimated hours and cost
                comActivitiesInfo.add(sprintNumber + "," + memberName + "," + comActivities[0] + "," + comActivities[1] + ","
                        + comActivities[2]);

                // Add the string with sprint number, member name, number of not committed activities (Meetings),
                // hours worked, estimated hours and cost
                notComActivitiesInfo.add(sprintNumber + "," + memberName + "," + notComActivities[0] + ","
                        + notComActivities[1] + "," + notComActivities[2]);

                // Sum up the global hours and costs
                globalHours[0] += sprintHours[0];
                globalHours[1] += sprintHours[1];
                globalHours[2] += sprintHours[2];

                globalComActivities[0] += comActivities[0];
                globalComActivities[1] += comActivities[1];
                globalComActivities[2] += comActivities[2];

                globalNotComActivities[0] += notComActivities[0];
                globalNotComActivities[1] += notComActivities[1];
                globalNotComActivities[2] += notComActivities[2];
            }

            // Add the global values
            membersInfo.add(sprintNumber + "," + "global" + "," + Double.valueOf(df.format(globalHours[0])) + ","
                    + Double.valueOf(df.format(globalHours[1])) + "," + Double.valueOf(df.format(globalHours[2])));
            comActivitiesInfo.add(sprintNumber + "," + "global" + "," + Double.valueOf(df.format(globalComActivities[0])) + ","
                    + Double.valueOf(df.format(globalComActivities[1])) + "," + Double.valueOf(df.format(globalComActivities[2])));
            notComActivitiesInfo.add(sprintNumber + "," + "global" + "," + Double.valueOf(df.format(globalNotComActivities[0])) + ","
                    + Double.valueOf(df.format(globalNotComActivities[1])) + "," + Double.valueOf(df.format(globalNotComActivities[2])));

            // Reset the global arrays
            globalHours = new Double[]{0.0, 0.0, 0.0};
            globalComActivities = new Double[]{0.0, 0.0, 0.0};
            globalNotComActivities = new Double[]{0.0, 0.0, 0.0};
        }


        // Get all the necessary information from GitManager
        for (String branch : gitManager.getBranchesInRepository()) {
            for (String collaborator : gitManager.getCollaborators()) {
                GitManager.CommitUnpack collabCommits = gitManager.getCommitFromBranches(collaborator, branch);
                for (GitManager.CommitHttpRequest commit : collabCommits.commit) {
                    // Add the string with collaborator name, branch, message and date
                    commitsInfo.add(collaborator + "," + branch + "," + commit.commitMessage + "," + commit.commitDate);
                }
            }
        }

        // Write all the info in a CSV file
        writeCSV(membersInfo, comActivitiesInfo, notComActivitiesInfo, commitsInfo);
    }

    /**
     * Creates the CSV file with all the information in the parameters lists.
     *
     * @param membersInfo          list of hours worked, estimated and cost (per sprint and per member).
     * @param comActivitiesInfo    list of committed activities (per sprint and per member).
     * @param notComActivitiesInfo list of not committed activities (per sprint and per member).
     * @param commitsInfo          list of commits (per member and per branch).
     * @throws IOException if the named file exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason.
     */
    private static void writeCSV(List<String> membersInfo, List<String> comActivitiesInfo,
                                 List<String> notComActivitiesInfo, List<String> commitsInfo) throws IOException {

        // Creates the file
        File csvFile = new File("info.csv");
        FileWriter fileWriter = new FileWriter(csvFile);


        // Writes all the lists, with a header to specify what they are
        fileWriter.write("Hours worked, hours estimated and cost" + "\n");
        for (String string : membersInfo) {
            fileWriter.write(string + "\n");
        }

        fileWriter.write("\n");
        fileWriter.write("Committed activities (Done)" + "\n");
        for (String string : comActivitiesInfo) {
            fileWriter.write(string + "\n");
        }

        fileWriter.write("\n");
        fileWriter.write("Activities that weren't committed (Meetings)" + "\n");
        for (String string : notComActivitiesInfo) {
            fileWriter.write(string + "\n");
        }

        fileWriter.write("\n");
        fileWriter.write("Commits per member" + "\n");

        commitsInfo.add("Member,Branch,Commit,Date");
        Collections.reverse(commitsInfo);

        for (String string : commitsInfo) {
            fileWriter.write(string + "\n");
        }

        fileWriter.close();
    }
}