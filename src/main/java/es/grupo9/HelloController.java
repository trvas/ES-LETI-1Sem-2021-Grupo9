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
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Objects;


/**
 * HelloController class. Connects the GitHub API and the Trello API all together with the UI.
 */
public class HelloController {
    @FXML
    TextField TrelloKeyInput, TrelloTokenInput, TrelloBoardInput, GitKeyInput, GitNameInput, GitRepoInput;
    @FXML
    Button id1, id2, id3, id4, idHome, Search, Export;
    @FXML
    Pane Pane, GithubPane;
    @FXML
    TabPane Sprint1Pane, Sprint2Pane, Sprint3Pane;

    @FXML
    WebView ReadMe, ProjectIds,
            MeetingsText1, DoneText1, SprintDate1,
            MeetingsText2, DoneText2, SprintDate2,
            MeetingsText3, DoneText3, SprintDate3;

    @FXML
    ComboBox<String> CommitsComboBox,
            MComboBox1, DComboBox1,
            MComboBox2, DComboBox2,
            MComboBox3, DComboBox3;

    @FXML
    TableView<Object> commitsTable, tagsTable,
            meetingsTable1, doneTable1, reviewTable1,
            meetingsTable2, doneTable2, reviewTable2,
            meetingsTable3, doneTable3, reviewTable3;
    @FXML
    TableColumn<Object, Object> mMember, mActivities, mHours, mCost,
            dMember, dActivities, dHours, dCost,
            rMember, rEstimated, rHours, rCost;
    @FXML
    TableColumn<Object, Object> mMember2, mActivities2, mHours2, mCost2,
            dMember2, dActivities2, dHours2, dCost2,
            rMember2, rEstimated2, rHours2, rCost2;
    @FXML
    TableColumn<Object, Object> mMember3, mActivities3, mHours3, mCost3,
            dMember3, dActivities3, dHours3, dCost3,
            rMember3, rEstimated3, rHours3, rCost3;

    @FXML
    TableColumn<Object, Object> Branch, Description, Date;
    @FXML
    TableColumn<Object, Object> Tag, TagDate;

    @FXML
    Tab TabMeetings1, TabDone1,
            TabMeetings2, TabDone2,
            TabMeetings3, TabDone3;

    @FXML
    PieChart PieReview1_2, PieReview1_1, PieMeetings1, PieDone1,
            PieReview2_1, PieReview2_2, PieMeetings2, PieDone2,
            PieReview3_1, PieReview3_2, PieMeetings3, PieDone3;
    @FXML
    Slider SliderCost;

    TrelloManager trelloManager;
    GitManager gitManager;
    DecimalFormat df = new DecimalFormat("#.##");
    int i, j, i2, j2, i3, j3, k = 0;


    /**
     * Handler for when one of the side buttons is clicked.
     * Sends the Pane picked to the front.
     *
     * @param e Button click.
     */
    @FXML
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.id1) {
            Pane.toFront();
        } else if (e.getSource() == this.id2) Sprint1Pane.toFront();
        else if (e.getSource() == this.id3) Sprint2Pane.toFront();
        else if (e.getSource() == this.id4) Sprint3Pane.toFront();
        else if (e.getSource() == this.idHome) GithubPane.toFront();
    }

    /**
     * Handler for when Search button is clicked.
     * This connects to Trello and Github and gets all the information needed to create all the pages and tabs.
     *
     * @param e Button click.
     * @throws Exception see {@link es.grupo9.GitManager#GitManager(String, String, String)}.
     */
    @FXML
    public void searchAction(ActionEvent e) throws Exception {

        if (e.getSource() == this.Search) {
            String TRELLO_KEY = this.TrelloKeyInput.getText();
            String TRELLO_TOKEN = this.TrelloTokenInput.getText();
            String TRELLO_BOARD = this.TrelloBoardInput.getText();
            String GIT_KEY = this.GitKeyInput.getText();
            String GIT_NAME = this.GitNameInput.getText();
            String GIT_REPO = this.GitRepoInput.getText();

            // Creates new instances of TrelloManager and GitManager with the provided values
            trelloManager = new TrelloManager(TRELLO_KEY, TRELLO_TOKEN, TRELLO_BOARD);
            gitManager = new GitManager(GIT_KEY, GIT_NAME, GIT_REPO);

            // Gets Github collaborators and ReadMe content.
            gitManager.getCollaborators();
            ReadMe.getEngine().loadContent(new Markdown4jProcessor().process(gitManager.getReadMe()));

            // Gets the project ids (project name, members and start date)
            StringBuilder ids = new StringBuilder("<b>Projeto:</b> " + trelloManager.getProjectName() +
                    "\n\n<b>Elementos:</b> ");

            for (Member member : trelloManager.getMembers()) {
                ids.append("\n- ").append(member.getFullName()).append(" ");
            }

            ids.append("\n\n<b>Data de início:</b> ").append(trelloManager.getBeginningDate());
            ProjectIds.getEngine().loadContent(new Markdown4jProcessor().process(ids.toString()));

            // Sets the cost based on the value on the slider
            Utils.setPrice((int) SliderCost.getValue());
            System.out.println((int) SliderCost.getValue());

            // Gets all the information needed for the Trello tabs
            // Sets the sprint tabs combo boxes with the card names (from the Meetings and Done lists)
            trelloManager.getMeetings(1).forEach(f -> MComboBox1.getItems().add(f.getName()));
            trelloManager.getFinishedSprintBacklog(1).forEach(f -> DComboBox1.getItems().add(f.getName()));
            trelloManager.getMeetings(2).forEach(f -> MComboBox2.getItems().add(f.getName()));
            trelloManager.getFinishedSprintBacklog(2).forEach(f -> DComboBox2.getItems().add(f.getName()));
            trelloManager.getMeetings(3).forEach(f -> MComboBox3.getItems().add(f.getName()));
            trelloManager.getFinishedSprintBacklog(3).forEach(f -> DComboBox3.getItems().add(f.getName()));


            // Adds all the sprint dates to each sprint page
            SprintDate1.getEngine().loadContent(new Markdown4jProcessor().process(trelloManager.getSprintDate(1)));
            SprintDate2.getEngine().loadContent(new Markdown4jProcessor().process(trelloManager.getSprintDate(2)));
            SprintDate3.getEngine().loadContent(new Markdown4jProcessor().process(trelloManager.getSprintDate(3)));

            // Sets the revivew table for each sprint page
            setReviewTable(1, reviewTable1, createTableColumnArray(rMember, rHours, rEstimated, rCost), PieReview1_1, PieReview1_2);
            setReviewTable(2, reviewTable2, createTableColumnArray(rMember2, rHours2, rEstimated2, rCost2), PieReview2_1, PieReview2_2);
            setReviewTable(3, reviewTable3, createTableColumnArray(rMember3, rHours3, rEstimated3, rCost3), PieReview3_1, PieReview3_2);

            // Sets the git page combo box with the collaborator names
            gitManager.getCollaborators().forEach(f -> CommitsComboBox.getItems().add(f));

        }
    }

    /**
     * Exports the CSV file with all the information presented.
     *
     * @param e Button click.
     * @throws Exception see {@link es.grupo9.Utils#exportCSV(TrelloManager, GitManager)}.
     */
    @FXML
    public void export(ActionEvent e) throws Exception {
        if (e.getSource() == this.Export) {
            Utils.exportCSV(trelloManager, gitManager);
        }
    }

    // adicionar javadoc
    @FXML
    public void home(Event e) throws Exception {
        if (k == 0) {
            if (e.getSource() == this.idHome) {
                setTabsTable();
                k = 1;
            }
        }
    }

    /* -- github -- */

    /**
     * Sets the Commits Table values to the ones of the member picked in the Commits Combo Box.
     *
     * @throws IOException See {@link GitManager#getCollaborators()} .
     */
    @FXML
    public void setCommitsComboBox() throws IOException {
        gitManager.getCollaborators().forEach(f -> {
            if (Objects.equals(this.CommitsComboBox.getValue(), f)) {
                try {
                    setCommitsTable(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets the Tags table values.
     *
     * @param data         ObservableList<Object> Data to be added to the table.
     * @param tableView    TableView<Object> Table view to be set.
     * @param tableColumns TableColumn<Object, Object>[] Table columns to be set.
     */
    private void setTableTags(ObservableList<Object> data, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns) {
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<>("tag"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<>("tagdate"));

        tableView.setItems(data);
    }

    /**
     * Gets all the information necessary to fill the Tags table from Github.
     *
     * @throws Exception See {@link GitManager#getBranchesInRepository()}.
     */
    public void setTabsTable() throws Exception {
        ObservableList<Object> data = FXCollections.observableArrayList();

        for (int k = 0; k < gitManager.getTag().size(); k++) {
            data.add(new TableData(String.valueOf(gitManager.getTag().get(k)[0]), String.valueOf(gitManager.getTag().get(k)[1])));
        }

        setTableTags(data, tagsTable, new TableColumn[]{Tag, TagDate});
    }

    /**
     * Sets the Commits table values.
     *
     * @param data         ObservableList<Object> Data to be added to the table.
     * @param tableView    TableView<Object> Table view to be set.
     * @param tableColumns TableColumn<Object, Object>[] Table columns to be set.
     */
    private void setTableCommits(ObservableList<Object> data, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns) {
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<>("branch"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<>("description"));
        tableColumns[2].setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.setItems(data);
    }

    /**
     * Gets all the information necessary to fill the Commits table from Github.
     *
     * @param userName Collaborator username.
     * @throws Exception See {@link GitManager#getBranchesInRepository()}.
     */
    public void setCommitsTable(String userName) throws Exception {
        ObservableList<Object> data = FXCollections.observableArrayList();

        for (String branches : gitManager.getBranchesInRepository()) {
            var a = gitManager.getCommits(userName, branches);
            for (GitManager.CommitHttpRequest commit : a.commits) {
                data.add(new TableData(branches, commit.commitMessage, commit.commitDate));
            }
        }
        Collections.reverse(data);
        setTableCommits(data, commitsTable, new TableColumn[]{Branch, Description, Date});
    }

    /* -- general -- */

    /**
     * Generalized constructor to create an array of four Table Columns.
     *
     * @param col1 TableColumn - First table column to be added.
     * @param col2 TableColumn - Second table column to be added.
     * @param col3 TableColumn - Third table column to be added.
     * @param col4 TableColumn - Fourth table column to be added.
     * @return A TableColumn of array of Table Columns.
     */
    public TableColumn<Object, Object>[] createTableColumnArray(TableColumn<Object, Object> col1, TableColumn<Object, Object> col2,
                                                                TableColumn<Object, Object> col3, TableColumn<Object, Object> col4) {
        TableColumn<Object, Object>[] tableColumns = new TableColumn[4];
        tableColumns[0] = col1;
        tableColumns[1] = col2;
        tableColumns[2] = col3;
        tableColumns[3] = col4;

        return tableColumns;
    }

    /**
     * Constructor for each Review table.
     *
     * @param sprintNumber int Number of the sprint.
     * @param reviewTable  A TableView of Review table to be set.
     * @param tableColumn  A TableColumn of Table Columns to be set.
     * @param pieReview1   PieChart Pie chart for the hours worked.
     * @param pieReview2   PieChart Pie chart for the estimated hours.
     * @throws IOException see {@link es.grupo9.TrelloManager#getSprintHoursByMember(String, int)}.
     */
    public void setReviewTable(int sprintNumber, TableView<Object> reviewTable, TableColumn<Object, Object>[] tableColumn, PieChart pieReview1, PieChart pieReview2) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart1 = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart2 = FXCollections.observableArrayList();
        Double[] globalStats = new Double[]{0.0, 0.0, 0.0};

        for (Member member : trelloManager.getMembers()) {
            Double[] stats = trelloManager.getSprintHoursByMember(member.getFullName(), sprintNumber);
            setTableInfo(data, pieChart1, member, stats);
            String s2 = member.getFullName();
            pieChart2.add(new PieChart.Data(s2, stats[1]));

            // Sums up all the member's values to return the global total
            globalStats[0] += stats[0];
            globalStats[1] += stats[1];
            globalStats[2] += stats[2];

        }
        pieReview1.setData(pieChart1);
        pieReview2.setData(pieChart2);
        setTableItems(data, globalStats, reviewTable, tableColumn);
    }

    /**
     * Constructor for each Meetings table.
     *
     * @param sprintNumber  int Number of the sprint.
     * @param meetingsTable A TableView of Review table to be set.
     * @param tableColumn   A TableColumn of Table Columns to be set.
     * @param pieMeetings   PieChart Pie chart for the hours worked.
     * @throws IOException see {@link es.grupo9.TrelloManager#getNotCommittedActivitiesByMember(String, int)}.
     */
    public void setMeetingsTable(int sprintNumber, TableView<Object> meetingsTable, TableColumn<Object, Object>[] tableColumn, PieChart pieMeetings) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
        Double[] globalActivities = new Double[]{0.0, 0.0, 0.0};

        for (Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getNotCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            setTableInfo(data, pieChart, member, activities);

            // Sums up all the member's values to return the global total
            globalActivities[0] += activities[0];
            globalActivities[1] += activities[1];
            globalActivities[2] += activities[2];
        }
        pieMeetings.setData(pieChart);
        setTableItems(data, globalActivities, meetingsTable, tableColumn);
    }


    /**
     * Constructor for each Done table.
     *
     * @param sprintNumber int Number of the sprint.
     * @param doneTable    A TableView of Review table to be set.
     * @param tableColumn  A TableColumn of Table Columns to be set.
     * @param pieDone      PieChart Pie chart for the hours worked.
     * @throws IOException see {@link es.grupo9.TrelloManager#getCommittedActivitiesByMember(String, int)}.
     */
    public void setDoneTable(int sprintNumber, TableView<Object> doneTable, TableColumn<Object, Object>[] tableColumn, PieChart pieDone) throws IOException {
        ObservableList<Object> data = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
        Double[] globalActivities = new Double[]{0.0, 0.0, 0.0};

        for (Member member : trelloManager.getMembers()) {
            Double[] activities = trelloManager.getCommittedActivitiesByMember(member.getFullName(), sprintNumber);
            setTableInfo(data, pieChart, member, activities);

            // Sums up all the member's values to return the global total
            globalActivities[0] += activities[0];
            globalActivities[1] += activities[1];
            globalActivities[2] += activities[2];
        }
        pieDone.setData(pieChart);
        setTableItems(data, globalActivities, doneTable, tableColumn);
    }

    /**
     * Sets each table's details (Trello related).
     *
     * @param data         ObservableList<Object> Data to be added to the table.
     * @param dataArray    Double[] Array with hours worked, estimated and costs.
     * @param tableView    TableView<Object> Table view to be set.
     * @param tableColumns TableColumn<Object, Object>[] Table columns to be set.
     */
    private void setTableItems(ObservableList<Object> data, Double[] dataArray, TableView<Object> tableView, TableColumn<Object, Object>[] tableColumns) {
        data.add(new TableData("global", Double.valueOf(df.format(dataArray[0])), Double.valueOf(df.format(dataArray[1])), Double.valueOf(df.format(dataArray[2]))));
        tableColumns[0].setCellValueFactory(new PropertyValueFactory<>("member"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory<>("activities"));
        tableColumns[2].setCellValueFactory(new PropertyValueFactory<>("hours"));
        tableColumns[3].setCellValueFactory(new PropertyValueFactory<>("cost"));

        tableView.setItems(data);
    }

    /**
     * Adds the total global values to the table and creates the Pie Chart.
     *
     * @param data     ObservableList<Object> Data to be added to the table.
     * @param pieChart PieChart Pie chart with the hours worked.
     * @param member   String Name of the member.
     * @param array    Double[] Array with the total global values.
     */
    private void setTableInfo(ObservableList<Object> data, ObservableList<PieChart.Data> pieChart, Member member, Double[] array) {
        data.add(new TableData(member.getFullName(), Double.valueOf(df.format(array[0])),
                Double.valueOf(df.format(array[1])), Double.valueOf(df.format(array[2]))));

        String s = member.getFullName();
        pieChart.add(new PieChart.Data(s, array[0]));
    }


    /* -- sprint 1 --*/

    /**
     * Handler for when the Sprint 1 tab is clicked.
     *
     * @param t Event tab click.
     * @throws IOException see {@link #setMeetingsTable(int, TableView, TableColumn[], PieChart)}.
     */
    @FXML
    public void setTabs1(Event t) throws IOException {
        if (i == 0) {
            if (t.getSource() == this.TabMeetings1) {
                setMeetingsTable(1, meetingsTable1, createTableColumnArray(mMember, mActivities, mHours, mCost), PieMeetings1);
                i = 1;
            }
        }
        if (j == 0) {
            if (t.getSource() == this.TabDone1) {
                setDoneTable(1, doneTable1, createTableColumnArray(dMember, dActivities, dHours, dCost), PieDone1);
                j = 1;
            }
        }
    }

    /**
     * Sets the Combo Box values to equal each card name and show their description when picked.
     *
     * @throws IOException see {@link es.grupo9.TrelloManager#getMeetings(int)}.
     */
    @FXML
    public void setMComboBox1() throws IOException {
        trelloManager.getMeetings(1).forEach(f -> {
            try {
                if (Objects.equals(this.MComboBox1.getValue(), f.getName()))
                    MeetingsText1.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                MeetingsText1.getEngine().loadContent(f.getDesc());
            }
        });
    }

    /**
     * Sets the Combo Box values to equal each card name and show their description when picked.
     *
     * @throws IOException see {@link es.grupo9.TrelloManager#getFinishedSprintBacklog(int)}.
     */
    @FXML
    public void setDComboBox1() throws IOException {
        trelloManager.getFinishedSprintBacklog(1).forEach(f -> {
            try {
                if (Objects.equals(this.DComboBox1.getValue(), f.getName()))
                    DoneText1.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                DoneText1.getEngine().loadContent(f.getDesc());
            }
        });
    }

    /* -- sprint 2 --*/

    /**
     * Handler for when a Sprint 2 tab is clicked.
     *
     * @param t Event Tab click.
     * @throws IOException see {@link #setMeetingsTable(int, TableView, TableColumn[], PieChart)} and {@link #setDoneTable(int, TableView, TableColumn[], PieChart)}.
     */
    @FXML
    public void setTabs2(Event t) throws IOException {
        if (i2 == 0) {
            if (t.getSource() == this.TabMeetings2) {
                setMeetingsTable(2, meetingsTable2, createTableColumnArray(mMember2, mActivities2, mHours2, mCost2), PieMeetings2);
                i3 = 1;
            }
        }
        if (j2 == 0) {
            if (t.getSource() == this.TabDone2) {
                setDoneTable(2, doneTable2, createTableColumnArray(dMember2, dActivities2, dHours2, dCost2), PieDone2);
                j3 = 1;
            }
        }
    }

    /**
     * Sets the Combo Box values to equal each card name and show their description when picked.
     *
     * @throws IOException see {@link es.grupo9.TrelloManager#getMeetings(int)}.
     */
    @FXML
    public void setMComboBox2() throws IOException {
        trelloManager.getMeetings(2).forEach(f -> {
            try {
                if (Objects.equals(this.MComboBox2.getValue(), f.getName()))
                    MeetingsText2.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                MeetingsText2.getEngine().loadContent(f.getDesc());
            }
        });
    }

    /**
     * Sets the Combo Box values to equal each card name and show their description when picked.
     *
     * @throws IOException see {@link es.grupo9.TrelloManager#getFinishedSprintBacklog(int)}.
     */
    @FXML
    public void setDComboBox2() throws IOException {
        trelloManager.getFinishedSprintBacklog(2).forEach(f -> {
            try {
                if (Objects.equals(this.DComboBox2.getValue(), f.getName()))
                    DoneText2.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                DoneText2.getEngine().loadContent(f.getDesc());
            }
        });
    }

    /* --- sprint 3 --- */

    /**
     * Handler for when a Sprint 3 tab is clicked.
     *
     * @param t Event Tab click.
     * @throws IOException see {@link #setMeetingsTable(int, TableView, TableColumn[], PieChart)} and {@link #setDoneTable(int, TableView, TableColumn[], PieChart)}.
     */
    @FXML
    public void setTabs3(Event t) throws IOException {
        if (i3 == 0) {
            if (t.getSource() == this.TabMeetings3) {
                setMeetingsTable(3, meetingsTable3, createTableColumnArray(mMember3, mActivities3, mHours3, mCost3), PieMeetings3);
                i3 = 1;
            }
        }
        if (j3 == 0) {
            if (t.getSource() == this.TabDone3) {
                setDoneTable(3, doneTable3, createTableColumnArray(dMember3, dActivities3, dHours3, dCost3), PieDone3);
                j3 = 1;
            }
        }
    }

    /**
     * Sets the Combo Box values to equal each card name and show their description when picked.
     *
     * @throws IOException ...
     */
    @FXML
    public void setMComboBox3() throws IOException {
        trelloManager.getMeetings(3).forEach(f -> {
            try {
                if (Objects.equals(this.MComboBox3.getValue(), f.getName()))
                    MeetingsText3.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                MeetingsText3.getEngine().loadContent(f.getDesc());
            }
        });
    }

    /**
     * Sets the Combo Box values to equal each card name and show their description when picked.
     *
     * @throws IOException ...
     */
    @FXML
    public void setDComboBox3() throws IOException {
        trelloManager.getFinishedSprintBacklog(3).forEach(f -> {
            try {
                if (Objects.equals(this.DComboBox3.getValue(), f.getName()))
                    DoneText3.getEngine().loadContent(new Markdown4jProcessor().process(f.getDesc()));
            } catch (StringIndexOutOfBoundsException | IOException ex) {
                DoneText3.getEngine().loadContent(f.getDesc());
            }
        });
    }
}