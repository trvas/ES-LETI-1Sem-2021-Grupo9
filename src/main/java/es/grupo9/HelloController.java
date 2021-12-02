package es.grupo9;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import org.markdown4j.Markdown4jProcessor;
import org.trello4j.model.Member;

import java.io.IOException;
import java.util.Objects;

public class HelloController{
    @FXML
    TextField Input1,Input2,Input3,Input4,Input5,Input6;
    @FXML
    Button id1,id2,id3,id4,Search;
    @FXML
    Pane Pane;
    @FXML
    TabPane TabPane1,TabPane2,TabPane3;
    @FXML
    WebView MeetingsText,DoneText;
    @FXML
    ComboBox<String> comboBox,comboBox2;
    @FXML
    TableView<Object> meetingsTable, doneTable, reviewTable;
    @FXML
    TableColumn<Object, Object> mMember, mActivities, mHours, mCost,
                                dMember, dActivities, dHours, dCost,
                                rMember, rEstimated, rHours, rCost;


    TrelloManager trelloManager;

    /**
     * Handler for when the button is clicked
     * @param e Button click
     */
    @FXML
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.id1){Pane.toFront();}
        else if(e.getSource() == this.id2) TabPane1.toFront();
        else if(e.getSource() == this.id3) TabPane2.toFront();
        else if(e.getSource() == this.id4) TabPane3.toFront();
    }

    @FXML
    public void searching(ActionEvent e) throws IOException {
        if(e.getSource() == this.Search){
            String API_KEY = this.Input1.getText();
            String TOKEN = this.Input2.getText();
            String BOARD_ID = this.Input3.getText();
            String GIT_KEY = this.Input4.getText();
            String TOKEN_G = this.Input5.getText();
            String DDD = this.Input6.getText();

            trelloManager = new TrelloManager(
                    "e3ee0d6a1686b4b43ba5d046bbce20af",
                    "80644fefce741495acc2f1ebf7174b536ae31a6c5c425622fbf5477f82463b84",
                    "614de300aa6df33863299b6c");

            trelloManager.getMeetings(1).forEach(f-> {
                    comboBox.getItems().add(f.getName());
            });
            trelloManager.getFinishedSprintBacklog(1).forEach(f-> {
                comboBox2.getItems().add(f.getName());
            });

            setReviewTable(1);
            setMeetingsTable(1);
            setDoneTable(1);
        }
    }


    @FXML
    public void setComboBox(ActionEvent e) throws IOException {
        trelloManager.getMeetings(1).forEach(f-> {
            try {
                if(Objects.equals(this.comboBox.getValue(), f.getName()))
                    MeetingsText.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                MeetingsText.getEngine().loadContent(f.getDesc());
            }
        });
    }

    @FXML
    public void setComboBox2(ActionEvent e) throws IOException {
        trelloManager.getFinishedSprintBacklog(1).forEach(f-> {
            try {
                if(Objects.equals(this.comboBox2.getValue(), f.getName()))
                    DoneText.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                DoneText.getEngine().loadContent(f.getDesc());
            }
        });
    }


    public void setReviewTable(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        for(Member member : trelloManager.getMembers()) {
            Double[] stats = trelloManager.getSprintHoursByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), stats[0], stats[1], stats[2]));
        }

        Double[] globalStats = trelloManager.getSprintHours(sprintNumber);
        setTableItems(data, globalStats, reviewTable, new TableColumn[]{rMember, rEstimated, rHours, rCost});
    }

    public void setMeetingsTable(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        for(Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getNotCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), activities[0], activities[1], activities[2]));
        }

        Double[] globalActivities = trelloManager.getNotCommittedActivities(sprintNumber);
        setTableItems(data, globalActivities, meetingsTable, new TableColumn[]{mMember, mActivities, mHours, mCost});
    }


    public void setDoneTable(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        for(Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), activities[0], activities[1], activities[2]));
        }

        Double[] globalActivities = trelloManager.getCommittedActivities(sprintNumber);

        setTableItems(data, globalActivities, doneTable, new TableColumn[]{dMember, dActivities, dHours, dCost});
    }

    public void setTableItems(ObservableList<Object> data, Double[] dataArray, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns){
        addData(data, dataArray);

        tableView.setItems(data);

        tableColumns[0].setCellValueFactory(new PropertyValueFactory<Object, Object>("member"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<Object, Object>("activities"));
        tableColumns[2].setCellValueFactory(new PropertyValueFactory<Object, Object>("hours"));
        tableColumns[3].setCellValueFactory(new PropertyValueFactory<Object, Object>("cost"));
    }

    private void addData(ObservableList<Object> data, Double[] globalValues){
        data.add(new TableData("global", globalValues[0], globalValues[1], globalValues[2]));
    }

}


