import org.kohsuke.github.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GitManager class, allowing for the search in GIT using an API to get specific information
 */
public class GitManager {

    private static String GITHUB_OAUTH; // gitHub's token //"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"
    private static String GITHUB_LOGIN;  //uses the user's username // "roguezilla"
    private static String GITHUB_REPO_NAME = "test_repo";  /*starboard  ES-LETI-1Sem-2021-Grupo9 */
    private static final String commitRef = "57f8f3f938071e99da9b6323d07209805ad9813f"; // Reference to get the file from.

    private static GHUser userOfLogin;
    private static GitHub githubLogin;

    private static boolean valid = false; //if the flag is set to true, then it moves
    private static final boolean getUserInfo = true; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN

    private static Set<String> collaboratorNames = new HashSet<>();
    private static final List<CommitsData> commitsList = new ArrayList<>();
    private static final List<String> info = new ArrayList<>();
    private static final Map<String, List<String>> repositoriesUserData = new HashMap<>();

    /**
     * The main function of this class
     *
     * @param args normal thing in a main
     * @throws Exception due to the functions it's calling, GitHub or GHUser being null
     */
    public static void main(String[] args) throws Exception {
        login();
        getCollaborators(GITHUB_REPO_NAME);
        if (getUserInfo) {
            userInfo();
        }
        repositoriesUnderUser();
        numberOfRepositoriesOwned();
        numberOfCommits(GITHUB_REPO_NAME, "Henrique-DeSousa");
        getFiles(GITHUB_REPO_NAME);
        getFileContent(GITHUB_REPO_NAME, "calc2.0.py", commitRef);
        getReadMe(GITHUB_REPO_NAME);
        getCommitData(GITHUB_REPO_NAME);
        getBranches(GITHUB_REPO_NAME);
        getTag(GITHUB_REPO_NAME);
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
        GITHUB_LOGIN = USERNAME;
        GITHUB_OAUTH = AUTH;
        GITHUB_REPO_NAME = REPO_NAME;
        githubLogin = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
        userOfLogin = githubLogin.getUser(GITHUB_LOGIN);
    }

    /**
     * This function's purpose is to log in the user into the GITHUB database. If the login is appropriate it connects
     *
     * @throws IOException throws when the GitManager is null
     */
    public static void login() throws IOException { // login for the user // TEMP INTERFACE
        new GitManager("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", "Henrique-DeSousa", GITHUB_REPO_NAME);
        connect();
    }

    /**
     * This function is to complement the previous function by verifying that the information provided is correct
     *
     * @throws IOException throws when GitHub or GHuser is null.
     */
    public static void connect() throws IOException {
        if (githubLogin.isCredentialValid()) {
            valid = true;
            GitHub.connect(GITHUB_LOGIN, GITHUB_OAUTH);
        }
    }

    /**
     * Function used to get the name of the collaborators of a specific Repository
     *
     * @param repositoryName Name of the repository to fetch the Collaborators
     * @return returns a string with the name of all the collaborators
     * @throws IOException Thrown due to GHUser
     */
    public static String getCollaborators(String repositoryName) throws IOException {
        GHRepository collaboratorsRepository = userOfLogin.getRepository(repositoryName);

        collaboratorNames = collaboratorsRepository.getCollaboratorNames();
        String collaborators = collaboratorNames.toString();

        return "Collaborators for the following Repository: " + repositoryName + "\nAre: " + collaborators;
    }

    /**
     * Function to return a List of the collaborators to be used in other functions to gather information regarding all the contributors
     *
     * @return List with the names of each contributor.
     */
    public static List<String> collaboratorsList() {
        return new ArrayList<>(collaboratorNames);
    }

    /**
     * This function's purpose is to gather all the users' information from a repository.
     *
     * @return all the information from the Collaborators from the repository in question.
     * @throws IOException thrown when the GHuser is null
     */
    public static List<String> userInfo() throws IOException {
        GHUser user;
        String info;
        List<String> collaboratorsInfo = new ArrayList<>();

        for (int i = 0; i < collaboratorsList().size(); i++) {
            user = githubLogin.getUser(collaboratorsList().get(i));

            String name = user.getName();
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
        return collaboratorsInfo;
    }

    /**
     * This function is simply to ease the previous one in the aspect that if any information is missing, it's simply replaced.
     *
     * @param userInformation this gets the user information from git
     * @param backup          this is set so if the previous param is null it returns it
     * @return information which corresponds to the query.
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
    public static Map<String, List<String>> repositoriesUnderUser() throws IOException {
        GHUser user;

        for (int i = 0; i < collaboratorsList().size(); i++) {
            user = githubLogin.getUser(collaboratorsList().get(i));

            Map<String, GHRepository> temp = user.getRepositories();
            repositoriesUserData.put(user.getLogin(), temp.values().stream().map(GHRepository::getName).collect(Collectors.toList()));
        }
        return repositoriesUserData;
    }

    /**
     * Function to use to get the Total number of Public and Private( user's token only) repositories of each collaborator
     * @return String containing the information mentioned above
     * @throws IOException thrown due to GHUser
     */
    public static List<String> numberOfRepositoriesOwned() throws IOException {
        GHUser user;
        String publicRepositories;
        String privateRepositories;
        List<String> out = new ArrayList<>();
        int repoCount;
        int privateRepoCount;
        Optional<Integer> privateRepositoryCount;

        for (int i = 0; i < collaboratorsList().size(); i++) {
            user = githubLogin.getUser(collaboratorsList().get(i));

            publicRepositories = "Number of Public Repositories: ";
            privateRepositories = "Number of Private Repositories: ";

            repoCount = user.getPublicRepoCount();
            if (user.getPublicRepoCount() == 0) {
                publicRepositories = "You have no Public Repositories.";
            }

            privateRepositoryCount = user.getTotalPrivateRepoCount();
            if (privateRepositoryCount.isEmpty()) {
                privateRepoCount = 0;
                privateRepositories = "You have no visible private Repositories.";
            } else {
                privateRepoCount = privateRepositoryCount.get();
            }
            out.add("\n" + user.getLogin() + "\n" + publicRepositories + repoCount + "\n" + privateRepositories + privateRepoCount);
        }
        return out;
    }

    /**
     * Function to get Branches and their Files from a RepositoryName
     * @param repositoryName The name of the Repository to inspect
     * @return returns a map containing a Map containing the name of the Branch and the Files inside it
     * @throws IOException thrown due to the GitHub
     */
    public static Map<GHTree, List<String>> getFiles(String repositoryName) throws IOException {
        GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);
        Map<String, GHBranch> getRepos = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
        List<String> branchesSha = new ArrayList<>();

        Map<GHTree, List<String>> files = new HashMap<>();
        List<String> filesInBranch = new ArrayList<>();
        GHTree tree;
        //GHTreeEntry treeEntry;
        //html request for the names of the branches / tree

        getRepos.forEach((r, s) -> branchesSha.add(s.getSHA1()));

        for (String s : branchesSha){
            tree = getRepo.getTree(s);

            for (int j = 0; j < tree.getTree().size(); j++) {
                filesInBranch.add(tree.getTree().get(j).getPath());
            }
            files.put(tree, filesInBranch);
            filesInBranch = new ArrayList<>();
        }
        System.out.println(files);
        System.out.println(branchesSha);
        return files;

    }

    /**
     * Gets the README from any repository that was given and reads the data in it.
     *
     * @param repositoryName the repository to look for
     * @return contentReadMe returns the content of the README file
     * @throws IOException thrown when the GHuser is null
     */
    public static String getReadMe(String repositoryName) throws IOException {
        GHContent readMeContents = userOfLogin.getRepository(repositoryName).getReadme();
        InputStream readMe = readMeContents.read();
        return new String(readMe.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Retrieves and reads the content of the file in question
     *
     * @param repositoryName The repository in which to look for the file
     * @param fileName       The name of the file
     * @param ref            The commit reference of the file.
     * @return returns the contents of the file, be it in python, java, or any other language.
     * @throws IOException thrown when the GHuser is null
     */
    public static String getFileContent(String repositoryName, String fileName, String ref) throws IOException {
        InputStream fileReading = userOfLogin.getRepository(repositoryName).getFileContent(fileName, ref).read();
        return new String(fileReading.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Returns all the branches in the repository in question
     *
     * @param repositoryName the name of the repository
     * @return returns a list with the names of the branches.
     * @throws Exception thrown when the GHuser is null
     */
    public static List<String> getBranches(String repositoryName) throws Exception {
        GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);
        Map<String, GHBranch> getRepos = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
        List<String> branchesName = new ArrayList<>();

        getRepos.forEach((r, s) -> branchesName.add(r));
        return branchesName;
    }

    /**
     * Gets the commits' data from the repository by user
     *
     * @param repositoryName name of the repository to look
     * @return returns a String which contains the initial and final commit from the repository main branch
     * @throws IOException throws when GitHub is null.
     */
    public static String getCommitData(String repositoryName) throws IOException {
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

        String totalCommits = ("\nTotal number of commits in: " + getRepo.getName() + " is: " + commitsList.size());
        String latest = ("\nLatest commit: " + commitsList.get(0).getDescription() + "\nDate: " + commitsList.get(0).getDate() + "\nUser: " + commitsList.get(0).getUserName() + "\n");
        String initial = ("\nInitial commit: " + commitsList.get(commitsList.size() - 1).getDescription() + "\nDate: " + commitsList.get(commitsList.size() - 1).getDate() + "\nUser: " + commitsList.get(commits.size() - 1).getUserName() + "\n");
        return initial + latest + totalCommits;
    }

    /**
     * Gets the number of commits from a certain user.
     *
     * @param repositoryName name of the repository to look
     * @param user_Login     name of the user to retrieve the commits.
     * @return returns a String which contains the initial and final commit from the repository main branch
     * @throws IOException throws when GitHub is null.
     */
    public static String numberOfCommits(String repositoryName, String user_Login) throws IOException {
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
        for (CommitsData commitsData : commitsList) {
            if (commitsData.getUserName().equals(temp.getLogin())) {
                info.add(commitsData.getDescription());
            }
        }
        return "The user: " + temp.getLogin() + "\nHas these commits: " + info.toString() + "\nWith a total of: " + info.size();

    }

    /**
     * Function to return the Tags that were made in a Repository.
     *
     * @param repositoryName Name of the repository to fetch Tags
     * @return Returns a Map with the Name of the Tag, and it's Date of publish
     * @throws IOException Thrown due to GitHub
     */
    public static Map<String, Date> getTag(String repositoryName) throws IOException {
        GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);
        List<GHTag> tags = getRepo.listTags().toList();
        List<String> tagNames = new ArrayList<>();
        List<GHCommit> tagCommits = new ArrayList<>();
        List<Date> tagDate = new ArrayList<>();
        Map<String, Date> out = new HashMap<>();

        tags.forEach(s -> {
            tagNames.add(s.getName());
            tagCommits.add(s.getCommit());
        });

        tagCommits.forEach((s) -> {
            try {
                tagDate.add(s.getCommitDate());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < tagNames.size(); i++) {
            out.put(tagNames.get(i), tagDate.get(i));
        }
        System.out.println(out);
        return out;
    }

    public static void getTree(String repositoryName) throws IOException {
        GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);

    }

    /**
     * Simple nested class for the Commits, where the data is separated in 3 components, Date, Username and Description.
     */
    public static class CommitsData {
        private final GHCommit.ShortInfo description;
        private final Date date;
        private final String userName;

        /**
         * constructor of the nested class CommitsData
         *
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
         * A function to get the date
         *
         * @return returns the date of the commits
         */
        public Date getDate() {
            return date;
        }

        /**
         * a function to get the description
         *
         * @return returns the description of the commits
         */
        public String getDescription() {
            return description.getMessage();
        }

        /**
         * a function to get the user's login
         *
         * @return returns the username of the commit creator
         */
        public String getUserName() {
            return userName;
        }
    }
}