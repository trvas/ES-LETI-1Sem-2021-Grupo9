package es.grupo9;

import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
// import org.trello4j.model.Board;
import org.trello4j.model.Card;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TrelloManager{

    private static Trello trello;
//  private static Board board;

    private static String boardId;

    public TrelloManager(String API_KEY, String TOKEN, String BOARD_ID) {

        boardId = BOARD_ID;
        trello = new TrelloImpl(API_KEY, TOKEN);
  //    Board board = trello.getBoard(boardId);
    }


    public static void main(String[] args) {
    //    TrelloManager trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.BOARD_ID);
    }


    /**
     * Gets the ID of cards from the Backlog pertaining to a specific Sprint. Each Increment list has
     * a #SPRINT(NUMBER) on its label. This method iterates over the board lists until it finds one
     * related to the sprint wanted.
     *
     * @param sprintNumber Sprint the user wants the cards from.
     * @return List<Card> list of cards from the desired Sprint.
     */
    public static List<Card> getFinishedSprintBacklog(int sprintNumber) {
        // Initialize auxiliary variables

        // Get the list of cards from the board
        List<Card> cards = trello.getCardsByList(getBoardListIdByName("#SPRINT" +  sprintNumber + " - Increment"));

        // Returns the cards from the Done list of the sprint asked
        return new ArrayList<>(cards);
    }

    /**
     * Returns the ID of a Board List provided its name.
     * @param listName name of the Board the user wants the ID from.
     * @return String ID of the Board List.
     */
    public static String getBoardListIdByName(String listName){
        String listId = "";
        List<org.trello4j.model.List> boardLists = trello.getListByBoard(boardId, (String) null);
        for(org.trello4j.model.List boardList: boardLists) {
            if (boardList.getName().contains(listName)) {
                listId = boardList.getId();
            }
        }



        return listId;
    }

    /**
     * Returns a HashMap with all the cards of the meetings in each Sprint.
     * @return HashMap<Integer, List<Card>> HashMap with the Sprint Number and list of cards (meetings) from that sprint.
     */
    public static HashMap<Integer, List<Card>> getMeetings(){
        HashMap<Integer, List<Card>> meetings = new HashMap<>();

        // Get all the Meetings cards
        for(int sprintNumber = 1; sprintNumber < getSprintCount() + 1; sprintNumber++){
            meetings.put(sprintNumber, trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Meetings")));
        }

        return meetings;
    }

    /**
     * Gets the number of current Sprints.
     * @return int number of Sprints so far.
     */
    public static int getSprintCount(){
        // Get "Sprints" list cards
        List<Card> cards = new ArrayList<>(trello.getCardsByList(getBoardListIdByName("Sprints")));

        return cards.size();
    }

    /**
     * Gets the hours of the Sprints.
     * @return total hours the sprint
     */

    public static String getCardDate(String listDate) {
        List<String> cardsSprint = new ArrayList<>();

        String str = Arrays.deepToString(cardsSprint.toArray());

        List<Card> cards2 = new ArrayList<>();
        for (Card card2 : cards2) {

            if (card2.getName().contains("Sprints")) {
               card2.getDesc().toString();
                cardsSprint.addAll(Collections.singleton(card2.getDesc().toString()));

            }

        }


        return str;
    }



    public String getSprintDate(int sprintNumber) throws IOException {
        String date = "";

        for (Card sprint : trello.getCardsByList(getBoardListIdByName("Sprints"))) {
            if(sprint.getName().contains(String.valueOf(sprintNumber))) {
                date = sprint.getDesc();
            }
        }

        return date;
    }




    }





















































































































































































































































































































































































































