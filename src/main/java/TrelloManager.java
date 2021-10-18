import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

import java.util.ArrayList;
import java.util.List;

public class TrelloManager{

    private static final String BOARD_ID = "614de300aa6df33863299b6c";

    private static final String MY_TOKEN = "8279cc618928ba12f81688c9181d9961411010d466989205c1e78da34823bf54";
    private static final String apiKEY = "f0757a32c960061eb33c4d83089f4c87";

    private static final Trello trello = new TrelloImpl(apiKEY, MY_TOKEN);
    private static final Board board = trello.getBoard("614de300aa6df33863299b6c");
    private static final List<Member> members = trello.getMembersByBoard(BOARD_ID,null);


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