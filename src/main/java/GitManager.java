import org.kohsuke.github.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * GitManager class, allowing for the search in GIT using an API to get specific information
 */
public class GitManager {

    private static String GITHUB_OAUTH; // gitHub's token //"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"
    private static String GITHUB_LOGIN;  //uses the user's username // "roguezilla"
    private static String GITHUB_REPO_NAME = "test_repo";  /*starboard  ES-LETI-1Sem-2021-Grupo9 */
    private static String GITHUB_FILE_TO_READ = "calc2.0.py"; //File we want to read
    private static String commitRef = "57f8f3f938071e99da9b6323d07209805ad9813f"; // Reference to get the file from.

    private static GHUser userOfLogin;
    private static GitHub githubLogin;

    private static boolean valid = false; //if the flag is set to true, then it moves
    private static boolean getUserInfo = true; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN

    private static Set<String> collaboratorNames = new HashSet<>();
    private static List<CommitsData> commitsList = new ArrayList<>();
    private static List<String> info = new ArrayList<>();
    private static Map<String, GHRepository> repositoriesMap = new HashMap<>();
    private static Map<String, String> repositoriesUserData = new HashMap<String, String>();

    /**
     * The main function of this class
     *
     * @param args normal thing in a main
     * @throws Exception due to the functions it's calling, GitHub or GHUser being null
     */
    public static void main(String[] args) throws Exception {
        login();
        getCollabs(GITHUB_REPO_NAME);
        if (getUserInfo != false) {
            userInfo();
        }
        repositoriesUnderUser();
        getFileContentInMain(GITHUB_REPO_NAME, GITHUB_FILE_TO_READ, commitRef);
        getReadMe(GITHUB_REPO_NAME);
        getCommitData(GITHUB_REPO_NAME, "glrss-iscte");
        getBranches(GITHUB_REPO_NAME);
    }

    /**
     * Class constructor
     *
     * @param AUTH      receives the user TOKEN
     * @param USERNAME  receives the user's LOGIN
     * @param REPO_NAME receives the user desired repository
     * @throws IOException throws exception when GitHub is null or GHUser is null
     */
    public GitManager(String AUTH, String USERNAME, String REPO_NAME) throws IOException {
        this.GITHUB_LOGIN = USERNAME;
        this.GITHUB_OAUTH = AUTH;
        this.GITHUB_REPO_NAME = REPO_NAME;
        this.githubLogin = new GitHubBuilder().withOAuthToken(this.GITHUB_OAUTH, this.GITHUB_LOGIN).build();
        this.userOfLogin = this.githubLogin.getUser(GITHUB_LOGIN);
    }

    /**
     * This function's purpose is to log in the user into the GITHUB database. If the login is appropriate it connects
     *
     * @throws IOException throws when the GitManager is null
     */
    public static void login() throws IOException { // login for the user // TEMP INTERFACE
        Scanner scan = new Scanner(System.in);  // Create a Scanner object

        while (!valid) {
            System.out.println("Enter username: ");
            String useTemp = scan.nextLine();  // Read user input
            System.out.println("Enter Token: ");
            String authTemp = scan.nextLine(); // read user's token
            new GitManager(authTemp, useTemp, GITHUB_REPO_NAME);
            connect();
        }
    }

    /**
     * This function is to complement the previous function by verifying that the information provided is correct
     *
     * @throws IOException throws when GitHub or GHuser is null.
     */
    public static void connect() throws IOException {
        if (githubLogin.isCredentialValid()) {
            valid = true;
            githubLogin.connect(GITHUB_LOGIN, GITHUB_OAUTH);
            userOfLogin = githubLogin.getUser(GITHUB_LOGIN);
        }
    }

    public static String getCollabs(String repositoryName) throws IOException {
        GHRepository collaboratorsRepository = userOfLogin.getRepository(repositoryName);

        collaboratorNames = collaboratorsRepository.getCollaboratorNames();
        String collaborators = collaboratorNames.toString();

        System.out.println("Collaborators for the following Repository: " + repositoryName + "\nAre: " + collaborators);
        String out = "Collaborators for the following Repository: " + repositoryName + "\nAre: " + collaborators;
        return out;
    }

    public static List<String> fillList() {
        List<String> collaborators = new ArrayList<>();

        collaboratorNames.forEach(s -> {
            collaborators.add(s);
        });
        return collaborators;
    }

    /**
     * This function's purpose is to gather all the users information from a repository.
     *
     * @return all the information from the Collaborators from the repository in question.
     * @throws IOException thrown when the GHuser is null
     */
    public static List<String> userInfo() throws IOException {
        GHUser user;
        String info;
        List<String> collaboratorsInfo = new ArrayList<>();

        for (int i = 0; i < fillList().size(); i++) {
            user = githubLogin.getUser(fillList().get(i));

            String name = user.getName();
            long userID = user.getId();
            URL url = user.getHtmlUrl();

            String avatarUrl = user.getAvatarUrl();
            String login = user.getLogin();
            String email = getUserInformation(user.getEmail(), "Your email isn't public");
            String bio = getUserInformation(user.getBio(), "No bio available");
            String location = getUserInformation(user.getLocation(), "Unknown location");
            String twtUser = getUserInformation(user.getTwitterUsername(), "Not available");
            String company = getUserInformation(user.getCompany(), "You don't have a company");

            info = url + "\n" + avatarUrl + "\n" + name + ";\n" + login + ";\n" + email + ";\n" + bio + ";\n" + location + ";\n" + twtUser + ";\n" + company + ".\n";
            collaboratorsInfo.add(info);
        }
        System.out.println(collaboratorsInfo);
        return collaboratorsInfo;
    }

    /**
     * This function is simply to ease the previous one in the aspect that if any information is missing, it's simply replaced.
     *
     * @param userInformation this gets the user information from git
     * @param backup          this is set so if the previous param is null it returns it
     * @return information which corresponds to the querry.
     */
    private static String getUserInformation(String userInformation, String backup) {
        String information = userInformation;
        if (information == null) information = backup;
        return information;
    }

    /**
     * Gathers all the repositories that the user has or participated in, although only shows the public ones it does also count the privates
     *
     * @return returns a String with the user's repositories.
     * @throws IOException thrown when the GHuser is null
     */
    public static String repositoriesUnderUser() throws IOException {
        GHUser user;

        for (int i = 0; i < fillList().size(); i++) {
            user = githubLogin.getUser(fillList().get(i));

            repositoriesMap = user.getRepositories();
        }

        List<String> nameofRepos = new ArrayList<>();
        for (GHRepository c: repositoriesMap.values()) {
            nameofRepos.add(c.getName());
        }
        System.out.println(nameofRepos);

           /* for(int c = 0; c < repositoriesMap.size(); c++) {
                repositoriesUserData.put(user.getLogin(), repositoriesMap.get(c).);

            }

        }
        System.out.println(repositoriesMap);
        System.out.println(repositoriesUserData);
            /* Um mapa com o nome do User e os reps que tem*/




           // String stringUserData = repositoriesUserData.toString();
          //  System.out.println(repositoriesUserData);

           // return stringUserData;
        return null;
    }

        public static String numberOfRepositoriesOwned() throws IOException {
            Boolean pubFlag = false;
            Boolean privFlag = false;
            String publicRepositories = "Number of Public Repositories: ";
            String privateRepositories = "Number of Private Repositories: ";

            int repoCount = userOfLogin.getPublicRepoCount();
            if (userOfLogin.getPublicRepoCount() == 0) {
                pubFlag = true;
                publicRepositories = "You have no Public Repositories";
            }

            Optional<Integer> privateRepositoryCount = userOfLogin.getTotalPrivateRepoCount();
            if (privateRepositoryCount.isEmpty()) {
                privFlag = true;
                privateRepositories = "You have no Private Repositories";
            }
            if ((privFlag && pubFlag) == true)
                return (publicRepositories + "\n" + privateRepositories);

            repositoriesMap = userOfLogin.getRepositories();
            return (publicRepositories + repoCount + "\n" + privateRepositories + "\n" + privateRepositoryCount.get());
        }
        /**
         * Gets the README from any repository that was given and reads the data in it.
         *
         * @param repositoryName the repository to look for
         * @return contentReadMe returns the content of the README file
         * @throws IOException thrown when the GHuser is null
         */
        public static String getReadMe (String repositoryName) throws IOException {
            GHContent readMeContents = userOfLogin.getRepository(repositoryName).getReadme();
            InputStream readMe = readMeContents.read();
            String contentReadMe = new String(readMe.readAllBytes(), StandardCharsets.UTF_8);

            System.out.println("ReadME contents: \n" + contentReadMe);
            return contentReadMe;
        }

        /**
         * Retrieves and reads the content of the file in question
         *
         * @param repositoryName The repository in which to look for the file
         * @param fileName       The name of the file
         * @param ref            The commit reference of the file.
         * @return returns the conents of the file, be it in python, java, or any other language.
         * @throws IOException thrown when the GHuser is null
         */
        public static String getFileContentInMain (String repositoryName, String fileName, String ref) throws IOException {
            InputStream fileReading = userOfLogin.getRepository(repositoryName).getFileContent(fileName, ref).read();
            String fileContents = new String(fileReading.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("File contents:\n" + fileContents);
            return fileContents;
        }

        /**
         * Returns all the branches in the repository in question
         *
         * @param repositoryName the name of the repository
         * @return returns a list with the names of the branches.
         * @throws Exception thrown when the GHuser is null
         */
        public static List<String> getBranches (String repositoryName) throws Exception {
            GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);

            Map<String, GHBranch> getRepos = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
            List<String> nams = new ArrayList<>();
            List<String> nums = new ArrayList<>();

            getRepos.forEach((r, s) -> {
                nams.add(r);
                nums.add(s.getSHA1());
            });
            System.out.println("\nThe branches in the following Repository: " + repositoryName + " are: " + nams);
            System.out.println("Branch: " + nams + " SHA1: " + nums);

            return nams;
        }

        /**
         * Gets the commit's data from the repository by user
         * @param repositoryName name of the repository to look
         * @param user_Login     name of the user to retrieve the commits.
         * @throws IOException throws when GitHub is null.
         * @return returns a String which contains the initial and final commit from the repository main branch
         */
        public static String getCommitData (String repositoryName, String user_Login) throws IOException {
            GHUser temp = githubLogin.getUser(user_Login);
            GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);

            List<GHCommit> commits = getRepo.listCommits().toList();
            commits.forEach((s) -> {
                try {
                    CommitsData commitData = new CommitsData(s.getCommitShortInfo(), s.getCommitDate(), s.getAuthor());
                    commitsList.add(commitData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            System.out.println("Total number of commits in: " + getRepo.getName() + " is: " + commitsList.size());
            String latest = ("\nLatest commit: " + commitsList.get(0).getDescription() + "\nDate: " + commitsList.get(0).getDate() + "\nUser: " + commitsList.get(0).getUserName() + "\n");
            String initial = ("\nInitial commit: " + commitsList.get(commitsList.size() - 1).getDescription() + "\nDate: " + commitsList.get(commitsList.size() - 1).getDate() + "\nUser: " + commitsList.get(commits.size() - 1).getUserName() + "\n");
            String both = initial + latest;
            System.out.println(both);
            return both;
        }

        /**
         * Gets the number of commits from a certain user.
         * @param repositoryName name of the repository to look
         * @param user_Login     name of the user to retrieve the commits.
         * @throws IOException throws when GitHub is null.
         * @return returns a String which contains the initial and final commit from the repository main branch
         */
        public static String numberofCommits (String repositoryName, String user_Login) throws IOException {
            GHUser temp = githubLogin.getUser(user_Login);
            GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);

            List<GHCommit> commits = getRepo.listCommits().toList();
            commits.forEach((s) -> {
                try {
                    CommitsData commitData = new CommitsData(s.getCommitShortInfo(), s.getCommitDate(), s.getAuthor());
                    commitsList.add(commitData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            for (int i = 0; i < commitsList.size() - 1; i++) {
                if (commitsList.get(i).getUserName().equals(temp.getLogin())) {
                    info.add(commitsList.get(i).getDescription());
                }
            }
            String number = "The user: " + temp.getLogin() + "\nHas these commits: " + info.toString() + "\nWith a total of: " + info.size();

            return number;
        }

        public record Info(String name, String repoName) {
            public String getName(){
                return name;
            }
            public String repoName(){
                return repoName;
            }
        }

        /**
         * Simply a neste class for the Commits, where the data is separated in 3 components, Date, Username and Description.
         */
        public static class CommitsData {
            private GHCommit.ShortInfo description;
            private Date date;
            private String userName;

            /**
             * constructor of the nested class CommitsData
             * @param description receives the description of a commit
             * @param date        receives the date of publishing of the commit
             * @param userName    receives the name of the person who created the commit
             * @throws IOException thrown due to the GHUser possibility of being null
             */
            CommitsData(GHCommit.ShortInfo description, Date date, GHUser userName) throws IOException {
                this.date = date;
                this.description = description;
                this.userName = userName.getLogin();
            }

            /**
             * A getfunction for the date
             * @return returns the date of the commits
             */
            public Date getDate() {
                return date;
            }

            /**
             * a getfunction for the description
             * @return returns the description of the commits
             */
            public String getDescription() {
                return description.getMessage();
            }

            /**
             * a getfucntion for the user's login
             * @return returns the username of the commit creator
             */
            public String getUserName() {
                return userName;
            }
        }
    }