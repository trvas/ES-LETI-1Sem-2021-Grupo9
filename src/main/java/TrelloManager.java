import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

import java.util.ArrayList;
import java.util.List;

public class TrelloManager{

    private static final String BOARD_ID = "614de300aa6df33863299b6c";

    private static final Trello trello = new TrelloImpl("e3ee0d6a1686b4b43ba5d046bbce20af",config.MY_TOKEN);
    private static final Board board = trello.getBoard("614de300aa6df33863299b6c");
    private static final List<Member> members = trello.getMembersByBoard(BOARD_ID,null);


    public static void main(String[] args) {

    }


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