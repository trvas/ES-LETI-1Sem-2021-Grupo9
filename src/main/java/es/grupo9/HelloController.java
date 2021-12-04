package es.grupo9;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import org.markdown4j.Markdown4jProcessor;
import org.trello4j.model.Member;

import java.io.IOException;
import java.util.ArrayList;
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
    @FXML
    Tab TabMeetings1, TabDone1;
    @FXML
    PieChart PieReview1_2, PieReview1_1, PieMeetings1, PieDone1;

    TrelloManager trelloManager;
    GitManager gitManager;

    int i = 0;
    int j = 0;

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
            String TRELLO_APIKEY = this.Input1.getText();
            String TRELLO_TOKEN = this.Input2.getText();
            String TRELLO_BOARDID = this.Input3.getText();
            String GIT_ID = this.Input4.getText();
            String GIT_TOKEN = this.Input5.getText();
            String GIT_REPONAME = this.Input6.getText();

            trelloManager = new TrelloManager(TRELLO_APIKEY, TRELLO_TOKEN, TRELLO_BOARDID);
            gitManager = new GitManager(GIT_ID, GIT_TOKEN, GIT_REPONAME);

            trelloManager.getMeetings(1).forEach(f-> {
                    comboBox.getItems().add(f.getName());
            });
            trelloManager.getFinishedSprintBacklog(1).forEach(f-> {
                comboBox2.getItems().add(f.getName());
            });
            setReviewTable(1);
        }
    }

    @FXML
    public void tables1(Event t) throws IOException {
        if(i == 0){
            if(t.getSource() == this.TabMeetings1){
                setMeetingsTable(1);
                i = 1;
            }
        }
    }

    @FXML
    public void tables2(Event t) throws IOException {
        if(j == 0){
            if(t.getSource() == this.TabDone1){
                setDoneTable(1);
                j = 1;
            }
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
        ObservableList<PieChart.Data> pieChart1 = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart2 = FXCollections.observableArrayList();
        Double[] globalStats = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] stats = trelloManager.getSprintHoursByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), stats[0], stats[1], stats[2]));
            String s = member.getFullName();
            pieChart1.add(new PieChart.Data(s, stats[0]));
            pieChart2.add(new PieChart.Data(s, stats[1]));

            globalStats[0] += stats[0];
            globalStats[1] += stats[1];
            globalStats[2] += stats[2];
        }
        PieReview1_1.setData(pieChart1);
        PieReview1_2.setData(pieChart2);
        setTableItems(data, globalStats, reviewTable, new TableColumn[]{rMember, rEstimated, rHours, rCost});
    }

    public void setMeetingsTable(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
        Double[] globalActivities = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getNotCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            ArrayList<Double[]> arrayList = new ArrayList<>();
            arrayList.add(0, activities);
            arrayList.add(1, globalActivities);

            addActivities(member, arrayList, data, pieChart);
        }
        PieMeetings1.setData(pieChart);
        setTableItems(data, globalActivities, meetingsTable, new TableColumn[]{mMember, mActivities, mHours, mCost});
    }


    public void setDoneTable(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
        Double[] globalActivities = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            ArrayList<Double[]> arrayList = new ArrayList<>();
            arrayList.add(0, activities);
            arrayList.add(1, globalActivities);

            addActivities(member, arrayList, data, pieChart);
        }
        PieDone1.setData(pieChart);
        setTableItems(data, globalActivities, doneTable, new TableColumn[]{dMember, dActivities, dHours, dCost});
    }

    private void setTableItems(ObservableList<Object> data, Double[] dataArray, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns){
        data.add(new TableData("global", dataArray[0], dataArray[1], dataArray[2]));
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<>("member"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<>("activities"));
        tableColumns[2].setCellValueFactory(new PropertyValueFactory<>("hours"));
        tableColumns[3].setCellValueFactory(new PropertyValueFactory<>("cost"));

        tableView.setItems(data);
    }

    private void addActivities(Member member, ArrayList<Double[]> arrayList, ObservableList<Object> data, ObservableList<PieChart.Data> pieChart){
        data.add(new TableData(member.getFullName(), arrayList.get(0)[0], arrayList.get(0)[1], arrayList.get(0)[2]));
        String a = member.getFullName();
        pieChart.add(new PieChart.Data(a, arrayList.get(0)[0]));

        arrayList.get(1)[0] += arrayList.get(0)[0];
        arrayList.get(1)[1] += arrayList.get(0)[1];
        arrayList.get(1)[2] += arrayList.get(0)[2];
    }
}



