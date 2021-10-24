import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
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
        List<org.trello4j.model.List> boardLists = trello.getListByBoard(BOARD_ID,null);
        for(org.trello4j.model.List boardList: boardLists) {
            if (boardList.getName().equals(boardName)) {
                listId = boardList.getId();
            }
        }
        return listId;
    }

}