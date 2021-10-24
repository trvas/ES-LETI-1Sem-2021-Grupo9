import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

import java.util.ArrayList;
import java.util.List;

public class TrelloManager{

    private static Trello trello;
    private static Board board;

    private static String BOARD_ID;

    public TrelloManager(String API_KEY, String TOKEN, String BOARD_ID) {

        this.BOARD_ID = BOARD_ID;

        trello = new TrelloImpl(API_KEY, TOKEN);
        Board board = trello.getBoard(BOARD_ID);
    }


    public static void main(String[] args) {

    }


    /**
     * Gets the cards from the Backlog pertaining to a specific Sprint. Each card has a label
     * that specifies which Sprint it's from. This method iterates over the board lists, then
     * card lists of the board wanted to get the ones with the label of the Sprint desired.
     *
     * @param SprintNumber Sprint the user wants the cards from.
     * @return List<Card> list of cards from the desired Sprint.
     */
    public static List<Card> getFinishedSprintBacklog(int SprintNumber) {
        // Initialize auxiliary variables
        List<Card> sprintCards = new ArrayList<>();

        // Get the list of cards from the board
        List<Card> cards = trello.getCardsByList(getBoardListIdByName("Done"));

        // Get the cards with the Sprint label we want
        for(Card card: cards) {
            for (Card.Label label : card.getLabels()) {
                if (label.getName().contains(Integer.toString(SprintNumber))) {
                    sprintCards.add(card);
                }
            }
        }

        return sprintCards;

    }




    /**
     * Returns the ID of a Board List provided its name.
     * @param boardName name of the Board the user wants the ID from.
     * @return String ID of the Board List.
     */
    public static String getBoardListIdByName(String boardName){
        String listId = "";
        List<org.trello4j.model.List> boardLists = trello.getListByBoard(BOARD_ID,null);
        for(org.trello4j.model.List boardList: boardLists) {
            if (boardList.getName().contains(boardName)) {
                listId = boardList.getId();
            }
        }
        return listId;
    }

}