import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Action;
import org.trello4j.model.Board;
import org.trello4j.model.Card;

import java.util.ArrayList;
import java.util.List;

public class TrelloManager{

    private static Trello trello;
    private static String boardID;

    public TrelloManager(String API_KEY, String TOKEN, String BOARD_ID) {

        boardID = BOARD_ID;
        trello = new TrelloImpl(API_KEY, TOKEN);
        Board board = trello.getBoard(BOARD_ID);

        //  method for constructing:
        //  TrelloManager trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.BOARD_ID);
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
        List<org.trello4j.model.List> boardLists = trello.getListByBoard(boardID,null);
        for(org.trello4j.model.List boardList: boardLists) {
            if (boardList.getName().contains(listName)) {
                listId = boardList.getId();
            }
        }
        return listId;
    }

    /**
     * Gets the number of current Sprints.
     * @return int number of Sprints so far.
     */
    public static int getSprintCount(){
        // Get "Sprints" cards
        List<Card> cards = new ArrayList<>(trello.getCardsByList(getBoardListIdByName("Sprints")));

        return cards.size();
    }

    public static void main(String[] args) {
        TrelloManager trelloManager = new TrelloManager(config.API_KEY, config.MY_TOKEN, config.BOARD_ID);
        getSprintCost(1);

    }

    /**
     * Gets the amount of hours worked on a card, provided its ID.
     * @param cardID ID of the Card.
     * @return Double hours worked on a given card.
     */
    public static Double getCardHours(String cardID){
        List<Action> comments = trello.getActionsByCard(cardID);
        comments.removeIf(action -> action.getData().getText() == null); // removing null comments

        Double[] hours = new Double[comments.size()];

        int aux = 0;
        Double sum = 0.0;

        while(aux != comments.size()) {
            for (Action action : comments) {
                if (action.getData().getText().contains("plus!")) {
                    String[] firstSplit = action.getData().getText().split("/");
                    String[] secondSplit = firstSplit[0].split(" ");
                    hours[aux] = Double.valueOf(secondSplit[secondSplit.length - 1]);
                }
                aux++;
            }
        }

        for(Double dbl : hours) sum += dbl;

        return sum;
    }

    /**
     *
     * @param sprintNumber number of the Sprint the cost will be calculated for.
     */
    public static Double getSprintCost(int sprintNumber) {
        List<Card> sprintList = trello.getCardsByList(getBoardListIdByName("#SPRINT" + sprintNumber + " - Increment"));
        Double totalHours = 0.0;
        Double cost = 0.0;

        // Sum up hours worked on each card
        for (Card card : sprintList) totalHours += getCardHours(card.getId());

        return cost;

    }

}