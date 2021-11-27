package es.grupo9;

import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
// import org.trello4j.model.Board;
import org.trello4j.model.Action;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class TrelloManager{

    private static Trello trello;
//  private static Board board;

    private static String boardId;

    public TrelloManager(String API_KEY, String TOKEN, String BOARD_ID) {

        boardId = BOARD_ID;
        trello = new TrelloImpl(API_KEY, TOKEN);
        //    Board board = trello.getBoard(boardId);
    }

    /**
     * Gets the ID of cards from the Backlog pertaining to a specific Sprint. Each Increment list has
     * a #SPRINT(NUMBER) on its label. This method iterates over the board lists until it finds one
     * related to the sprint wanted.
     *
     * @param sprintNumber Sprint the user wants the cards from.
     * @return List<Card> list of cards from the desired Sprint.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public List<Card> getFinishedSprintBacklog(int sprintNumber) throws IOException {
        // Initialize auxiliary variables

        // Get the list of cards from the board
        List<Card> cards = trello.getCardsByList(getBoardListIdByName("#SPRINT" +  sprintNumber + " - Increment"));

        // Returns the cards from the Done list of the sprint asked
        return new ArrayList<>(cards);
    }

    /**
     * Gets the amount of hours worked on a card, provided its ID.
     * @param cardID ID of the Card.
     * @return Double hours worked on a given card.
     */
    public Double getCardHours(String cardID){
        List<Action> comments = trello.getActionsByCard(cardID);
        comments.removeIf(action -> action.getData().getText() == null); // removing null comments

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        Double[] hours = new Double[comments.size()];

        int aux = 0;

        while(aux != comments.size()) {
            for (Action action : comments) {
                if (action.getData().getText().contains("plus!")) {
                    // Normal structure of a comment with plus! = "plus! @NAME #/#"
                    // First split = [plus! @NAME #, #]
                    String[] firstSplit = action.getData().getText().split("/");

                    // Second split = [plus!, @NAME, #]
                    String[] secondSplit = firstSplit[0].split(" ");

                    // Get the last element of the array (hours)
                    hours[aux] = Double.valueOf(secondSplit[secondSplit.length - 1]);
                }
                aux++;
            }
        }

        Double sum = Utils.getSum(hours);

        return Double.valueOf(df.format(sum));
    }

    /**
     * Gets the estimated hours of work on a card, provided its ID.
     * @param cardID ID of the Card.
     * @return Double estimated hours of work on a given card.
     */
    public Double getEstimateCardHours(String cardID){
        List<Action> comments = trello.getActionsByCard(cardID);
        comments.removeIf(action -> action.getData().getText() == null); // removing null comments

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        Double[] hours = new Double[comments.size()];

        int aux = 0;

        while(aux != comments.size()) {
            for (Action action : comments) {
                if (action.getData().getText().contains("plus!")) {
                    // Normal structure of a comment with plus! = "plus! @NAME #/#"
                    // First split = [plus! @NAME #, #]
                    String[] split = action.getData().getText().split("/");

                    // Get the last element of the array (hours)
                    hours[aux] = Double.valueOf(split[split.length - 1]);
                }
                aux++;
            }
        }

        Double sum = Utils.getSum(hours);

        return Double.valueOf(df.format(sum));
    }


    /**
     * Get the total amount of hours worked on a given SPRINT.
     * @param sprintNumber number of the SPRINT.
     * @return Double hours worked on a given SPRINT.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public Double getSprintHours(int sprintNumber) throws IOException {
        List<Card> sprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        // Sum up hours worked on each card
        Double hours = getTotalListHours(sprintList);

        return Double.valueOf(df.format(hours));
    }

    /**
     * Get the amount of hours a given member worked on a given SPRINT. The cost per hour is, by default, 20.
     * @param memberName name of the member.
     * @param sprintNumber number of the SPRINT.
     * @return Double amount of hours a given member worked on the given SPRINT.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public Double getSprintHoursByMember(String memberName, int sprintNumber) throws IOException {
        List<Card> memberSprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

        // Removing cards without the member
        String memberId = getMemberIdByName(memberName);
        memberSprintList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        return Double.valueOf(df.format(getTotalListHours(memberSprintList)));
    }

    /**
     * Get the amount of hours of work estimated on a given SPRINT.
     * @param sprintNumber sprint number.
     * @return Double estimate hours of work on a given SPRINT.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public Double getEstimateSprintHours(int sprintNumber) throws IOException {
        List<Card> sprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        // Sum up hours worked on each card
        Double hours = getEstimateListHours(sprintList);

        return Double.valueOf(df.format(hours));
    }

    /**
     * Get the estimated amount of hours a given member worked on a given SPRINT. The cost per hour is, by default, 20.
     * @param memberName name of the member.
     * @param sprintNumber number of the SPRINT.
     * @return Double estimate amount of hours a given member worked on the given SPRINT.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public Double getEstimateSprintHoursByMember(String memberName, int sprintNumber) throws IOException {
        List<Card> memberSprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

        // Removing cards without the member
        String memberId = getMemberIdByName(memberName);
        memberSprintList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        return Double.valueOf(df.format(getEstimateListHours(memberSprintList)));
    }

    /**
     * Get the work cost of a given SPRINT. The cost per hour is, by default, 20.
     * @param sprintNumber number of the SPRINT.
     * @return Double cost of the work done in the SPRINT.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public Double getSprintCost(int sprintNumber) throws IOException {
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        Double cost = Utils.getCost(getSprintHours(sprintNumber));

        return Double.valueOf(df.format(cost));
    }

    /**
     * Get the cost of the work of a given member on a given SPRINT. The cost per hour is, by default, 20.
     * @param memberName name of the member.
     * @param sprintNumber number of the SPRINT.
     * @return Double cost of the work of the member on the given SPRINT.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public Double getSprintCostByMember(String memberName, int sprintNumber) throws IOException {
        List<Card> memberSprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

        // Removing cards without the member
        String memberId = getMemberIdByName(memberName);
        memberSprintList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        Double cost = Utils.getCost(getTotalListHours(memberSprintList));

        return Double.valueOf(df.format(cost));
    }

    /**
     * Returns a HashMap with all the cards of the meetings in each Sprint.
     * @return HashMap<Integer, List<Card>> HashMap with the Sprint Number and list of cards (meetings) from that sprint.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public HashMap<Integer, List<Card>> getMeetings() throws IOException {
        HashMap<Integer, List<Card>> meetings = new HashMap<>();

        // Get all the Meetings cards
        for(int sprintNumber = 1; sprintNumber < getSprintCount() + 1; sprintNumber++){
            meetings.put(sprintNumber, trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Meetings")));
        }

        return meetings;
    }

    /**
     * Gets the total amount of hours (sum of hours on each card) of a given list of cards.
     * @param sprintList list of all the cards.
     * @return Double sum of all the hours.
     */
    private Double getTotalListHours(List<Card> sprintList) {
        double totalHours = 0.0;

        for (Card card : sprintList) totalHours += getCardHours(card.getId());

        return totalHours;
    }

    /**
     * Gets the estimate amount of hours (sum of hours on each card) of a given list of cards.
     * @param sprintList list of all the cards.
     * @return Double sum of all the estimate hours.
     */
    private Double getEstimateListHours(List<Card> sprintList) {
        double totalHours = 0.0;

        for (Card card : sprintList) totalHours += getEstimateCardHours(card.getId());

        return totalHours;
    }

    /**
     * Returns a List with the amount of activities, hours worked and total cost of all the committed activities. This
     * method works by going over every "Done" list from each SPRINT.
     * @return List<Object> with the following structure [AMOUNT OF ACTIVITIES, HOURS WORKED, COST].
     * @throws IOException refer to {@link #getSprintCount()}
     */
    public List<Object> getCommittedActivities() throws IOException {
        List<Object> activities = new ArrayList<>();
        List<Card> activitiesCount = new ArrayList<>();
        double totalHours = 0.0;
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        for(int i = 1; i != getSprintCount() + 1; i++){
            activitiesCount.addAll(trello.getCardsByList(getBoardListIdByName("#SPRINT" + i + " - Increment")));
            totalHours += getSprintHours(i);
        }

        activities.add(0, activitiesCount.size());
        activities.add(1, Double.valueOf(df.format(totalHours)));
        activities.add(2, Utils.getCost(totalHours));

        return activities;
    }


    /**
     * Returns a List with the amount of activities, hours worked and total cost of all the not committed activities.
     * This method works by going over every "Meetings" list from each SPRINT.
     * @return List<Object> with the following structure [AMOUNT OF ACTIVITIES, HOURS WORKED, COST].
     * @throws IOException refer to {@link #getSprintCount()}
     */
    public List<Object> getNotCommittedActivities() throws IOException {
        List<Object> activities = new ArrayList<>();
        List<Card> activitiesCount = new ArrayList<>();
        double totalHours = 0.0;
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");


        for(int i = 1; i != getSprintCount() + 1; i++){
            List<Card> meetings = trello.getCardsByList(getBoardListIdByName("#SPRINT" + i + " - Meetings"));
            activitiesCount.addAll(meetings);
            totalHours += getTotalListHours(meetings);
        }

        activities.add(0, activitiesCount.size());
        activities.add(1, Double.valueOf(df.format(totalHours)));
        activities.add(2, Utils.getCost(totalHours));

        return activities;
    }

    /**
     * Returns a List with the amount of activities, hours worked and total cost of all the committed activities by member.
     * This method works by going over every "Done" list from each SPRINT and getting the cards with the member on them.
     * @param memberName name of the member.
     * @return List<Object> with the following structure [AMOUNT OF ACTIVITIES, HOURS WORKED, COST].
     * @throws IOException refer to {@link #getSprintCount()}
     */
    public List<Object> getCommittedActivitiesByMember(String memberName) throws IOException {
        List<Object> activities = new ArrayList<>();
        List<Card> activitiesCount = new ArrayList<>();
        double totalHours = 0.0;
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");


        for(int i = 1; i != getSprintCount() + 1; i++){
            List<Card> memberSprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + i + " - Increment"));

            // Removing cards without the member
            String memberId = getMemberIdByName(memberName);
            memberSprintList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

            activitiesCount.addAll(memberSprintList);
            totalHours += getSprintHoursByMember(memberName, i);
        }

        activities.add(0, activitiesCount.size());
        activities.add(1, Double.valueOf(df.format(totalHours)));
        activities.add(2, Utils.getCost(totalHours));

        return activities;
    }

    /**
     * Returns a List with the amount of activities, hours worked and total cost of all the not committed activities by member.
     * This method works by going over every "Meetings" list from each SPRINT and getting the cards with the member on them.
     * @param memberName name of the member.
     * @return List<Object> with the following structure [AMOUNT OF ACTIVITIES, HOURS WORKED, COST].
     * @throws IOException refer to {@link #getSprintCount()}
     */
    public List<Object> getNotCommittedActivitiesByMember(String memberName) throws IOException {
        List<Object> activities = new ArrayList<>();
        List<Card> activitiesCount = new ArrayList<>();
        double totalHours = 0.0;
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");


        for(int i = 1; i != getSprintCount() + 1; i++){
            List<Card> memberMeetingList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + i + " - Meetings"));

            // Removing cards without the member
            String memberId = getMemberIdByName(memberName);
            memberMeetingList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

            activitiesCount.addAll(memberMeetingList);
            totalHours += getTotalListHours(memberMeetingList);
        }

        activities.add(0, activitiesCount.size());
        activities.add(1, Double.valueOf(df.format(totalHours)));
        activities.add(2, Utils.getCost(totalHours));

        return activities;
    }

    /**
     * Returns the ID of a Board List provided its name.
     * @param listName name of the Board the user wants the ID from.
     * @return String ID of the Board List.
     * @throws IOException if list isn't part of the board.
     */
    public String getBoardListIdByName(String listName) throws IOException {
        List<org.trello4j.model.List> boardLists = trello.getListByBoard(boardId);
        for(org.trello4j.model.List boardList : boardLists) {
            if (boardList.getName().contains(listName)) {
                return boardList.getId();
            }
        }
        throw new IOException("List does not exist in this board.");
    }

    /**
     * Returns the ID of a Member provided their name.
     * @param memberName name of the Member the user wants the ID from.
     * @return String ID of the Member.
     * @throws IOException if member isn't part of the board.
     */
    public String getMemberIdByName(String memberName) throws IOException {
        List<Member> memberList = trello.getMembersByBoard(boardId);
        for(Member member : memberList) {
            if (member.getFullName().contains(memberName)) {
                return member.getId();
            }
        }
        throw new IOException("Member does not exist in this board.");
    }

    /**
     * Gets the number of current Sprints.
     * @return int number of Sprints so far.
     * @throws IOException refer to {@link #getBoardListIdByName(String)};
     */
    public int getSprintCount() throws IOException {
        // Get "Sprints" list cards
        List<Card> cards = new ArrayList<>(trello.getCardsByList(getBoardListIdByName("Sprints")));

        return cards.size();
    }
}