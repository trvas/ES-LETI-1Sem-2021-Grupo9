import org.trello4j.Trello;
import org.trello4j.TrelloImpl;

public class TrelloManager{

    public static void TrelloManager(String[] args) {
        Trello trello = new TrelloImpl("e3ee0d6a1686b4b43ba5d046bbce20af",null);

        System.out.print(trello.getBoard("Projeto").getName());
    }



}