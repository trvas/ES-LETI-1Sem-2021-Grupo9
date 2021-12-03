package es.grupo9;

import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Action;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrelloManager{

    private static Trello trello;

    private static String boardId;

    public TrelloManager(String API_KEY, String TOKEN, String BOARD_ID) {
        boardId = BOARD_ID;
        trello = new TrelloImpl(API_KEY, TOKEN);
    }

    /**
     * Gets the ID of cards from the Backlog pertaining to a specific Sprint. Each Increment list has
     * a #SPRINT(NUMBER) on its label. This method iterates over the board lists until it finds one
     * related to the sprint wanted.
     *
     * @param sprintNumber Sprint the user wants the cards from.
     * @return List<Card> list of cards from the desired Sprint.
     * @throws IOException see {@link #getBoardListIdByName(String)};
     */
    public List<Card> getFinishedSprintBacklog(int sprintNumber) throws IOException {
        // Get the list of cards from the board
        List<Card> cards = trello.getCardsByList(getBoardListIdByName("#SPRINT" +  sprintNumber + " - Increment"));

        // Returns the cards from the Done list of the sprint asked
        return new ArrayList<>(cards);
    }

    /**
     * Returns the number of hours worked and estimated of a given card.
     * @param cardID ID of the card.
     * @return Double[] with the following format: [HOURS WORKED, HOURS ESTIMATED].
     */
    public Double[] getCardHours(String cardID){
        List<Action> comments = trello.getActionsByCard(cardID);
        comments.removeIf(action -> action.getData().getText() == null); // removing null comments

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        Double[] real = new Double[comments.size()];
        Double[] estimate = new Double[comments.size()];

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
                    real[aux] = Double.valueOf(secondSplit[secondSplit.length - 1]);
                    estimate[aux] = Double.valueOf(firstSplit[firstSplit.length - 1]);

                }
                aux++;
            }
        }

        Double[] hours = new Double[2];
        hours[0] = Double.valueOf(df.format(Utils.getSum(real)));
        hours[1] = Double.valueOf(df.format(Utils.getSum(estimate)));


        return hours;
    }

    /**
     * Returns a Double array with the number of hours worked, hours estimated and the cost of the hours worked of a given member.
     * This method works by iterating through the "Increment" list of the SPRINT requested and deleting the cards that don't
     * have the member requested on them.
     * @param sprintNumber number of the SPRINT.
     * @param memberName name of the member.
     * @return Double[] with the following format: [HOURS WORKED, HOURS ESTIMATED, COST OF HOURS WORKED].
     * @throws IOException see {@link #getBoardListIdByName(String)};
     */
    public Double[] getSprintHoursByMember(String memberName, int sprintNumber) throws IOException {
        List<Card> memberSprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

        // Removing cards without the member
        String memberId = getMemberIdByName(memberName);
        memberSprintList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

        Double real = 0.0;
        Double estimate = 0.0;

        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        // Sum up hours worked on each card
        for (Card card : memberSprintList) {
            Double[] aux = getCardHours(card.getId());
            real += aux[0];
            estimate += aux[1];
        }

        return new Double[] {Double.valueOf(df.format(real)), Double.valueOf(df.format(estimate)), Double.valueOf(df.format(Utils.getCost(real)))};
    }

    /**
     * Returns an array with the amount of committed activities and the total hours worked on those activities by member,
     * as well as the cost. This method works by iterating every "Increment" list of the given SPRINT and removing the cards
     * without the requested member on them.
     * @param memberName name of the member.
     * @param sprintNumber number of the sprint.
     * @return Double[] with the following format [NUMBER OF ACTIVITIES, TOTAL HOURS WORKED, COST OF HOURS WORKED].
     * @throws IOException see {@link #getBoardListIdByName(String)};
     */
    public Double[] getCommittedActivitiesByMember(String memberName, int sprintNumber) throws IOException {
        Double[] activities = new Double[3];
        List<Card> activitiesCount = new ArrayList<>();
        double totalHours = 0.0;
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

         List<Card> memberSprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));

         // Removing cards without the member
         String memberId = getMemberIdByName(memberName);
         memberSprintList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

         activitiesCount.addAll(memberSprintList);
         totalHours += getSprintHoursByMember(memberName, sprintNumber)[0];

         activities[0] = (double) activitiesCount.size();
         activities[1] = Double.valueOf(df.format(totalHours));
         activities[2] = Double.valueOf(df.format(Utils.getCost(totalHours)));

         return activities;
    }

    /**
     * Returns an array with the amount of committed activities and the total hours worked on those activities by member,
     * as well as the cost. This method works by iterating every "Meetings" list of the given SPRINT and removing the cards
     * without the requested member on them.
     * @param memberName name of the member.
     * @param sprintNumber number of the SPRINT.
     * @return Double[] with the following format [NUMBER OF ACTIVITIES, TOTAL HOURS WORKED, COST OF HOURS WORKED].
     * @throws IOException see {@link #getBoardListIdByName(String)};
     */
    public Double[] getNotCommittedActivitiesByMember(String memberName, int sprintNumber) throws IOException {
        Double[] activities = new Double[3];
        List<Card> activitiesCount = new ArrayList<>();
        double totalHours = 0.0;
        // Format to only have 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");

        List<Card> memberMeetingList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Meetings"));

        // Removing cards without the member
        String memberId = getMemberIdByName(memberName);
        memberMeetingList.removeIf(card -> !(card.getIdMembers().contains(memberId)));

        activitiesCount.addAll(memberMeetingList);
        for(Card card : memberMeetingList) {
            totalHours += getCardHours(card.getId())[0];
        }


        activities[0] = (double) activitiesCount.size();
        activities[1] = Double.valueOf(df.format(totalHours));
        activities[2] = Double.valueOf(df.format(Utils.getCost(totalHours)));

        return activities;
    }

    /**
     * Returns a list with all the Meetings of a given SPRINT.
     * @param sprintNumber number of the SPRINT.
     * @return List<Card> list of cards (meetings) of the SPRINT requested.
     * @throws IOException see {@link #getBoardListIdByName(String)};
     */
    public List<Card> getMeetings(int sprintNumber) throws IOException {
        return trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Meetings"));
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
     * @throws IOException see {@link #getBoardListIdByName(String)};
     */
    public int getSprintCount() throws IOException {
        // Get "Sprints" list cards
        List<Card> cards = new ArrayList<>(trello.getCardsByList(getBoardListIdByName("Sprints")));

        return cards.size();
    }

    public int getMemberCount() {
        List<Member> memberList = trello.getMembersByBoard(boardId);
        return memberList.size();
    }

    public List<Member> getMembers(){
        return trello.getMembersByBoard(boardId);
    }

    public static void main(String[] args) throws IOException {
        TrelloManager trelloManager = new TrelloManager(
                "e3ee0d6a1686b4b43ba5d046bbce20af",
                "80644fefce741495acc2f1ebf7174b536ae31a6c5c425622fbf5477f82463b84",
                "614de300aa6df33863299b6c");
    }
}