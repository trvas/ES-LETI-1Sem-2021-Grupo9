import org.kohsuke.github.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GitManager extends GHAppInstallation{

    private static String GITHUB_OAUTH; // gitHub's token //"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"
    private static String GITHUB_LOGIN;  //uses the user's username // "roguezilla"
    private static String GITHUB_REPO_NAME = "test_repo";  /*starboard  ES-LETI-1Sem-2021-Grupo9 */
    private static String GITHUB_FILE_TO_READ = "calc2.0.py"; //File we want to read
    private static String commitRef = "57f8f3f938071e99da9b6323d07209805ad9813f"; // Reference to get the file from.

    private static GHUser user;
    private static GitHub githubLogin;

    private static boolean valid = false; //if the flag is set to true, then it moves
    private static boolean getUserInfo = false; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN

    private static Set<String> collaboratorNames = new HashSet<>();
    private static List<String> repositoriesUserData = new ArrayList<>();
    private static List<CommitsData> commitsList = new ArrayList<>();
    private static List<String> info = new ArrayList<>();
    private static Map<String,GHRepository> repositoriesMap = new HashMap<>();



    public static void main(String[] args) throws Exception {
        login();
        if(getUserInfo != false) {
            userInfo();
        }
        repositoriesUnderUser();
        getFileContentInMain(GITHUB_REPO_NAME, GITHUB_FILE_TO_READ, commitRef );
        getReadMe(GITHUB_REPO_NAME);
        getCommitData(GITHUB_REPO_NAME, "glrss-iscte");
        getBranches(GITHUB_REPO_NAME);
    }


    public GitManager(String ID, String USERNAME, String REPO_NAME) throws IOException {
        this.GITHUB_LOGIN = USERNAME;
        this.GITHUB_OAUTH = ID;
        this.GITHUB_REPO_NAME = REPO_NAME;
        connect();
    }

    /**
     * This function's purpose is to log in the user into the GITHUB database. If the login is appropriate it connects
     * @throws IOException throws when the GitManager is null
     */
        public static void login() throws IOException { // login for the user // TEMP INTERFACE
            Scanner scan = new Scanner(System.in);  // Create a Scanner object

            while (!valid){
                System.out.println("Enter username: ");
                String useTemp = scan.nextLine();  // Read user input

                System.out.println("Enter Token: ");
                String authTemp = scan.nextLine(); // read user's token
                new GitManager(authTemp, useTemp, GITHUB_REPO_NAME);
            }
        }

    /**
     * This function is to complement the previous function by verifying that the information provided is correct
     * @throws IOException throws when GitHub or GHuser is null.
     */
    public static void connect() throws IOException{
            githubLogin = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
                if(githubLogin.isCredentialValid()) {
                    valid = true;
                    githubLogin.connect(GITHUB_LOGIN, GITHUB_OAUTH);
                    user = githubLogin.getUser(GITHUB_LOGIN);
                }
        }

    /**
     * This function's purpose is to gather all the user information, the user in question being the person that logged in.
     * @return info which contains the data to be presented.
     * @throws IOException thrown when the GHuser is null
     */
    public static String userInfo() throws IOException{

            String name = user.getName();
            long userID = user.getId();

            String token = user.getAvatarUrl(); // add this to the UI
            System.out.println(token);

            String login = user.getLogin();
            String email = getUserInformation(user.getEmail(), "Your email isn't public");
            String bio = getUserInformation(user.getBio(), "No bio available");
            String location = getUserInformation(user.getLocation(), "Unknown location");
            String twtUser = getUserInformation(user.getTwitterUsername(), "Not available");
            String company = getUserInformation(user.getCompany(), "You don't have a company!");

            String info = "\n" + email + ";\n" + name + ";\n" + login +";\n" + bio  + ";\n" + location +";\n"+ twtUser + ";\n"+ company + ".\n";
            System.out.println(info);
            //System.out.println(userID);
            return info;
        }

    /**
     *  This function is simply to ease the previous one in the aspect that if any information is missing, it's simply replaced.
     * @param userInformation this gets the user information from git
     * @param backup this is set so if the previous param is null it returns it
     * @return information which corresponds to the querry.
     */
    private static String getUserInformation(String userInformation, String backup) {
        String information = userInformation;
        if (information == null) information = backup;
        return information;
    }

    /**
     * Gathers all the repositories that the user has or participated in, although only shows the public ones it does also count the privates
     * @throws IOException thrown when the GHuser is null
     */
    public static void repositoriesUnderUser() throws IOException{
        int repoCount = user.getPublicRepoCount();if(repoCount == 0){ String noRepo = "You have no Public Repositories"; }
        Optional<Integer> privateRepositoryCount = user.getTotalPrivateRepoCount(); // needs proper token to find the private
        System.out.println("Number of Private Repositories: " + privateRepositoryCount.get());

        repositoriesMap = user.getRepositories();
        System.out.println("Total of public repositories: " + repoCount);

            for(Map.Entry<String, GHRepository> t : repositoriesMap.entrySet()){
                repositoriesUserData.add(t.getKey());
                collaboratorNames = t.getValue().getCollaboratorNames();
            }

            String stringUserData = repositoriesUserData.toString();
            System.out.println(stringUserData);
            System.out.println("The collaborators in this repository are: " + collaboratorNames);
    }

    /**
     * Gets the README from any repository that was given and reads the data in it.
     * @param repositoryName the repository to look for
     * @return contentReadMe returns the content of the README file
     * @throws IOException thrown when the GHuser is null
     */
    public static String getReadMe(String repositoryName) throws IOException {
        GHContent readMeContents = user.getRepository(repositoryName).getReadme();
        InputStream readMe = readMeContents.read();
        String contentReadMe = new String(readMe.readAllBytes(), StandardCharsets.UTF_8);

        System.out.println("ReadME contents: \n" + contentReadMe);
        return contentReadMe;
    }

    /**
     *  Retrieves and reads the content of the file in question
     * @param repositoryName The repository in which to look for the file
     * @param fileName The name of the file
     * @param ref The commit reference of the file.
     * @return returns the conents of the file, be it in python, java, or any other language.
     * @throws IOException thrown when the GHuser is null
     */
    public static String getFileContentInMain(String repositoryName, String fileName ,String ref) throws IOException {
        InputStream fileReading = user.getRepository(repositoryName).getFileContent(fileName, ref).read();
        String fileContents = new String(fileReading.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("File contents:\n" + fileContents);
        return fileContents;
    }

    /**
     * Returns all the branches in the repository in question
     * @param repositoryName the name of the repository
     * @throws Exception thrown when the GHuser is null
     * @return returns a list with the names of the branches.
     */
    public static List<String> getBranches(String repositoryName) throws Exception {
        Map<String, GHBranch> getRepo = githubLogin.getRepository(user.getLogin() + "/" + repositoryName).getBranches();
        List<String> nams = new ArrayList<>();
        List<String> nums = new ArrayList<>();

        getRepo.forEach((r, s) -> {
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
     * @param user_Login name of the user to retrieve the commits.
     * @throws IOException throws when GitHub is null.
     */
    public static void getCommitData(String repositoryName, String user_Login) throws IOException {
        GHUser temp = githubLogin.getUser(user_Login);
        GHRepository getRepo = githubLogin.getRepository(user.getLogin() + "/" + repositoryName);

        List<GHCommit> commits = getRepo.listCommits().toList();
        commits.forEach((s) -> {
            try {
                CommitsData commitData = new CommitsData(s.getCommitShortInfo(), s.getCommitDate(), s.getAuthor());
                commitsList.add(commitData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for(int i = 0; i < commitsList.size()-1; i++){
            if (commitsList.get(i).getUserName().equals(temp.getLogin())){
                info.add(commitsList.get(i).getDescription());
            }
        }

        System.out.println("The user: " + temp.getLogin() + " Has these commits: " +  info.toString());
        System.out.println("Total number of commits in: " + getRepo.getName() + " is: " + commitsList.size());
        System.out.println("\nLatest commit: " + commitsList.get(0).getDescription() + "\nDate: " + commitsList.get(0).getDate() + "\nUser: " + commitsList.get(0).getUserName() + "\n");
        System.out.println("Initial commit: " + commitsList.get(commitsList.size() - 1).getDescription() + "\nDate: " + commitsList.get(commitsList.size() - 1).getDate() + "\nUser: " + commitsList.get(commits.size()-1).getUserName() + "\n");
    }

    /**
     * Simply a neste class for the Commits, where the data is separated in 3 components, Date, Username and Description.
     */
    public static class CommitsData {
        private GHCommit.ShortInfo description;
        private Date date;
        private String userName;

        CommitsData(GHCommit.ShortInfo description, Date date, GHUser userName) throws IOException {
            this.date = date;
            this.description = description;
            this.userName = userName.getLogin();
        }

        public Date getDate() {
                    return date;
                }
                public String getDescription() {
            return description.getMessage();
        }
        public String getUserName() {
                    return userName;
                }
            }
}