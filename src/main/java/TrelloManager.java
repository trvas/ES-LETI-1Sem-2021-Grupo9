import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
import org.trello4j.model.Card;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

public class TrelloManager{

    private static Trello trello;
//    private static Board board;

    private static String BOARD_ID;

    public TrelloManager(String API_KEY, String TOKEN, String BOARD_ID) {

        this.BOARD_ID = BOARD_ID;

        trello = new TrelloImpl(API_KEY, TOKEN);
        Board board = trello.getBoard(BOARD_ID);


    }


    public static void main(String[] args) {

    }



    /**
     * Returns the ID of a Board List provided its name.
     * @param boardName name of the Board the user wants the ID from.
     * @return String ID of the Board List.
     */
    public static String getBoardListIdByName(String boardName){
        String listId = "";
        List<org.trello4j.model.List> boardLists = trello.getListByBoard(BOARD_ID, null);
        for(org.trello4j.model.List boardList: boardLists) {
            if (boardList.getName().contains(boardName)) {
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

}