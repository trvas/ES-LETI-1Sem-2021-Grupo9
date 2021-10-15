import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Board;
import org.trello4j.model.Card;
import org.trello4j.model.Member;

import java.util.List;

public class TrelloManager{

    public static final String BOARD_ID = "614de300aa6df33863299b6c";
    public static Trello trello = new TrelloImpl("e3ee0d6a1686b4b43ba5d046bbce20af","");
    public static Board board = trello.getBoard("614de300aa6df33863299b6c");

    public static void main(String[] args) {
        System.out.println(trello.getBoard("614de300aa6df33863299b6c").getName());

        List<Member> test = trello.getMembersByBoard(BOARD_ID,null);



        System.out.println(test);
        System.out.println("");


        for(Member m: test){
            String name = m.getFullName();
            System.out.println(name);
            System.out.println("");

        }

        List<org.trello4j.model.List> boardLists = trello.getListByBoard(BOARD_ID,null);

        for(org.trello4j.model.List bl: boardLists){
            String listName = bl.getName();
            List<Card> cards = trello.getCardsByList(bl.getId());
            System.out.println(listName);
            for(Card c: cards)
                System.out.println(c.getName());
            System.out.println("");
        }

    }



}