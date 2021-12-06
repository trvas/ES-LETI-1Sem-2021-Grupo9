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
import java.util.Collections;
import java.util.Objects;

public class HelloController{
    @FXML
    TextField Input1,Input2,Input3,Input4,Input5,Input6;
    @FXML
    Button id1,id2,id3,id4,idHome,Search,Export;
    @FXML
    Pane Pane,PaneHome;
    @FXML
    TabPane TabPane1,TabPane2,TabPane3;

    ///=======//

    @FXML//done I guess
    WebView MeetingsText,DoneText,ReadMe,Sprint1Date,ProjectIds,
            MeetingsText2,DoneText2,Sprint1Date2;
    @FXML
    ComboBox<String> comboBox,comboBox2,comboBox3, McomboBox,DcomboBox;

    @FXML//done
    TableView<Object> meetingsTable, doneTable, reviewTable, commitsTable, tagsTable,
                      meetingsTable2, doneTable2, reviewTable2;
    @FXML
    TableColumn<Object, Object> mMember, mActivities, mHours, mCost,
                                dMember, dActivities, dHours, dCost,
                                rMember, rEstimated, rHours, rCost;
    @FXML//done
    TableColumn<Object, Object> mMember2, mActivities2, mHours2, mCost2,
                                dMember2, dActivities2, dHours2, dCost2,
                                rMember2, rEstimated2, rHours2, rCost2;
    @FXML
    TableColumn<Object, Object> branch, description, date;
    @FXML
    TableColumn<Object, Object> tag, date2;

    @FXML//done
    Tab TabMeetings1, TabDone1,
        TabMeetings2, TabDone2;
    @FXML//done
    PieChart PieReview1_2, PieReview1_1, PieMeetings1, PieDone1,
             PieReview2_1, PieReview2_2, PieMeetings2, PieDone2;
    @FXML
    Slider SliderCost;

    TrelloManager trelloManager;
    GitManager gitManager;
    int i = 0;
    int j = 0;
    int i2 = 0;
    int j2 = 0;
    int k = 0;

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
        else if(e.getSource() == this.idHome) PaneHome.toFront();
    }

    @FXML
    public void searching(ActionEvent e) throws Exception {
        if(e.getSource() == this.Search){
            String API_KEY = this.Input1.getText();
            String TOKEN = this.Input2.getText();
            String BOARD_ID = this.Input3.getText();
            String GIT_KEY = this.Input4.getText();
            String GIT_NAME = this.Input5.getText();
            String REPO_NAME = this.Input6.getText();

            trelloManager = new TrelloManager(
                    "e3ee0d6a1686b4b43ba5d046bbce20af",
                    "80644fefce741495acc2f1ebf7174b536ae31a6c5c425622fbf5477f82463b84",
                    "614de300aa6df33863299b6c");
            gitManager = new GitManager(
                    "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",
                    "henrique-deSousa",
                    "test_repo");

            StringBuilder ids = new StringBuilder("<b>Projeto:</b> " + trelloManager.getProjectName() +
                    "\n\n<b>Elementos:</b> ");

            for (Member member : trelloManager.getMembers()) {
                ids.append("\n- ").append(member.getFullName()).append(" ");
            }

            ids.append("\n\n<b>Data de início:</b> ").append(trelloManager.getBeginningDate());
            ProjectIds.getEngine().loadContent(new Markdown4jProcessor().process(ids.toString()));


            Utils.setPrice((int)SliderCost.getValue());
            trelloManager.getMeetings(1).forEach(f-> comboBox.getItems().add(f.getName()));
            trelloManager.getFinishedSprintBacklog(1).forEach(f-> comboBox2.getItems().add(f.getName()));
            trelloManager.getMeetings(2).forEach(f-> McomboBox.getItems().add(f.getName()));
            trelloManager.getFinishedSprintBacklog(2).forEach(f-> DcomboBox.getItems().add(f.getName()));

            gitManager.getCollaborators().forEach(f->comboBox3.getItems().add(f));
            Sprint1Date.getEngine().loadContent(new Markdown4jProcessor().process(trelloManager.getSprintDate(1)));
            Sprint1Date2.getEngine().loadContent(new Markdown4jProcessor().process(trelloManager.getSprintDate(2)));
            setReviewTable(1);
            setReviewTable2(2);

            gitManager.connect();
            gitManager.getCollaborators();
            ReadMe.getEngine().loadContent(new Markdown4jProcessor().process(gitManager.getReadMe()));
        }
    }

    @FXML
    public void export(ActionEvent e) throws Exception {
        if(e.getSource() == this.Export){
            Utils.exportCSV(trelloManager,gitManager);
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
    public void home(Event e) throws Exception {
        if(k == 0){
        if(e.getSource() == this.idHome) {
            setTabsTable();
            k = 1;
        }
        }
    }

    @FXML
    public void setComboBox() throws IOException {
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
    public void setComboBox2() throws IOException {
        trelloManager.getFinishedSprintBacklog(1).forEach(f-> {
            try {
                if(Objects.equals(this.comboBox2.getValue(), f.getName()))
                    DoneText.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                DoneText.getEngine().loadContent(f.getDesc());
            }
        });
    }

    @FXML
    public void setComboBox3() throws IOException {
        gitManager.getCollaborators().forEach(f-> {
            if(Objects.equals(this.comboBox3.getValue(),f)){
                try {
                    setCommitsTable(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setTabsTable() throws Exception {
        ObservableList<Object> data = FXCollections.observableArrayList();

        for (int k = 0; k <gitManager.getTag().size() ; k++) {
            data.add(new TableData(String.valueOf(gitManager.getTag().get(k)[0]),String.valueOf(gitManager.getTag().get(k)[1])));
        }

        setTableTags(data, tagsTable, new TableColumn[]{tag, date2});
    }


    public void setCommitsTable(String userName) throws Exception {
        ObservableList<Object> data = FXCollections.observableArrayList();

        for(String branches : gitManager.getBranchesInRepository()) {
            var a = gitManager.getCommits(userName,branches);
            for (GitManager.CommitHttpRequest commit : a.commits){
                data.add(new TableData(branches, commit.commitMessage, commit.commitDate));
            }
        }
        Collections.reverse(data);
        setTableCommits(data, commitsTable, new TableColumn[]{branch, description, date});
    }


    public void setReviewTable(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart1 = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart2 = FXCollections.observableArrayList();
        Double[] globalStats = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] stats = trelloManager.getSprintHoursByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), stats[0], stats[1], stats[2]));
            String s1 = member.getFullName();
            pieChart1.add(new PieChart.Data(s1, stats[0]));
            String s2 = member.getFullName();
            pieChart2.add(new PieChart.Data(s2, stats[1]));

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
            data.add(new TableData(member.getFullName(), activities[0], activities[1], activities[2]));
            String a = member.getFullName();
            pieChart.add(new PieChart.Data(a, activities[0]));

            globalActivities[0] += activities[0];
            globalActivities[1] += activities[1];
            globalActivities[2] += activities[2];
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
            data.add(new TableData(member.getFullName(), activities[0], activities[1], activities[2]));
            String s = member.getFullName();
            pieChart.add(new PieChart.Data(s, activities[0]));

            globalActivities[0] += activities[0];
            globalActivities[1] += activities[1];
            globalActivities[2] += activities[2];
        }
        PieDone1.setData(pieChart);
        setTableItems(data, globalActivities, doneTable, new TableColumn[]{dMember, dActivities, dHours, dCost});
    }

    private void setTableItems(ObservableList<Object> data, Double[] dataArray, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns){
        data.add(new TableData("global", dataArray[0], dataArray[1], dataArray[2]));
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<Object, Object>("member"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<Object, Object>("activities"));
        tableColumns[2].setCellValueFactory(new PropertyValueFactory<Object, Object>("hours"));
        tableColumns[3].setCellValueFactory(new PropertyValueFactory<Object, Object>("cost"));

        tableView.setItems(data);
    }

    private void setTableCommits(ObservableList<Object> data, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns) {
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<Object, Object>("branch"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<Object, Object>("description"));
        tableColumns[2].setCellValueFactory(new PropertyValueFactory<Object, Object>("date"));

        tableView.setItems(data);
    }

    private void setTableTags(ObservableList<Object> data, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns) {
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<Object, Object>("tag"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<Object, Object>("date2"));

        tableView.setItems(data);
    }




    //////////////------------------------SPRINT2----------------------------------------/////////////

    public void setReviewTable2(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart1 = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart2 = FXCollections.observableArrayList();
        Double[] globalStats = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] stats = trelloManager.getSprintHoursByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), stats[0], stats[1], stats[2]));
            String s1 = member.getFullName();
            pieChart1.add(new PieChart.Data(s1, stats[0]));
            String s2 = member.getFullName();
            pieChart2.add(new PieChart.Data(s2, stats[1]));

            globalStats[0] += stats[0];
            globalStats[1] += stats[1];
            globalStats[2] += stats[2];

        }
        PieReview2_2.setData(pieChart1);
        PieReview2_1.setData(pieChart2);
        setTableItems(data, globalStats, reviewTable2, new TableColumn[]{rMember2, rEstimated2, rHours2, rCost2});
    }

    public void setMeetingsTable2(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
        Double[] globalActivities = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getNotCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), activities[0], activities[1], activities[2]));
            String a = member.getFullName();
            pieChart.add(new PieChart.Data(a, activities[0]));

            globalActivities[0] += activities[0];
            globalActivities[1] += activities[1];
            globalActivities[2] += activities[2];
        }
        PieMeetings2.setData(pieChart);
        setTableItems(data, globalActivities, meetingsTable2, new TableColumn[]{mMember2, mActivities2, mHours2, mCost2});
    }

    public void setDoneTable2(int sprintNumber) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
        Double[] globalActivities = new Double[] {0.0, 0.0, 0.0};

        for(Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            data.add(new TableData(member.getFullName(), activities[0], activities[1], activities[2]));
            String s = member.getFullName();
            pieChart.add(new PieChart.Data(s, activities[0]));

            globalActivities[0] += activities[0];
            globalActivities[1] += activities[1];
            globalActivities[2] += activities[2];
        }
        PieDone2.setData(pieChart);
        setTableItems(data, globalActivities, doneTable2, new TableColumn[]{dMember2, dActivities2, dHours2, dCost2});
    }

    @FXML
    public void tables1_2(Event t) throws IOException {
        if(i2 == 0){
            if(t.getSource() == this.TabMeetings2){
                setMeetingsTable2(2);
                i2 = 1;
            }
        }
    }

    @FXML
    public void tables2_2(Event t) throws IOException {
        if(j2 == 0){
            if(t.getSource() == this.TabDone2){
                setDoneTable2(2);
                j2 = 1;
            }
        }
    }

    @FXML
    public void setComboBox_2() throws IOException {
        trelloManager.getMeetings(2).forEach(f-> {
            try {
                if(Objects.equals(this.McomboBox.getValue(), f.getName()))
                    MeetingsText2.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                MeetingsText2.getEngine().loadContent(f.getDesc());
            }
        });
    }

    @FXML
    public void setComboBox2_2() throws IOException {
        trelloManager.getFinishedSprintBacklog(2).forEach(f-> {
            try {
                if(Objects.equals(this.DcomboBox.getValue(), f.getName()))
                    DoneText2.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                DoneText2.getEngine().loadContent(f.getDesc());
            }
        });
    }

}