import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GitManager class, allowing for the search in GIT using an API to get specific information
 */
public class GitManager {

    private static String GITHUB_OAUTH; // gitHub's token //"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"
    private static String GITHUB_LOGIN;  //uses the user's username // "roguezilla"
    private static String GITHUB_REPO_NAME = "test_repo";  /*starboard  ES-LETI-1Sem-2021-Grupo9 */
    private static String commitRef = "57f8f3f938071e99da9b6323d07209805ad9813f"; // Reference to get the file from.

    private GHUser userOfLogin;
    private GitHub githubLogin;

    private boolean valid = false; //if the flag is set to true, then it moves
    private static boolean getUserInfo = true; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN

    private Set<String> collaboratorNames = new HashSet<>();
    private List<CommitsDataGit> commitsRoot = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    private Map<String, List<String>> repositoriesUserData = new HashMap<>();
    private Map<String, Object> commitsPerBranch = new HashMap<>();

    private OkHttpClient client = new OkHttpClient();
    private String url;

    private final ObjectMapper mapper;

    /**
     * The main function of this class
     *
     * @param args normal thing in a main
     * @throws Exception due to the functions it's calling, GitHub or GHUser being null
     */
    public static void main(String[] args) throws Exception {
        GitManager GM = new GitManager("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", "Henrique-DeSousa", GITHUB_REPO_NAME);
        GM.connect();


        GM.getCollaborators(GITHUB_REPO_NAME);
        if (getUserInfo) {
            GM.userInfo();
        }
        GM.repositoriesUnderUser();
        GM.numberOfRepositoriesOwned();
        GM.numberOfCommitsInRoot(GITHUB_REPO_NAME, "Henrique-DeSousa");
        GM.getFiles(GITHUB_REPO_NAME);
        GM.readFileContent(GITHUB_REPO_NAME, "calc2.0.py", commitRef);
        GM.getReadMe(GITHUB_REPO_NAME);
        GM.getBranchesInRepository(GITHUB_REPO_NAME);
        GM.getTag(GITHUB_REPO_NAME);


        var a = GM.getCommitBranches("Henrique-DeSousa", "main");
        for (var commit : a.commits) {
            System.out.println(commit.commitMessage + " " + commit.getCommitDate() + " " + a.personName);
        }
    }

    /**
     * Constructor Function, create the object.
     *
     * @param AUTH     receives the user TOKEN
     * @param USERNAME receives the user's LOGIN
     * @param repoName receives the user desired repository
     * @throws IOException throws exception when GitHub is null or GHUser is null
     */
    public GitManager(String AUTH, String USERNAME, String repoName) throws IOException {
        this.GITHUB_LOGIN = USERNAME;
        this.GITHUB_OAUTH = AUTH;
        this.GITHUB_REPO_NAME = repoName;
        githubLogin = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
        userOfLogin = githubLogin.getUser(GITHUB_LOGIN);
        this.url = "https://api.github.com/repos/" + GITHUB_LOGIN + "/" + GITHUB_REPO_NAME; // trvas + ES-LETI-1Sem-2021-Grupo9
        this.mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }


    /**
     * This function is to complement the previous function by verifying that the information provided is correct
     *
     * @throws IOException throws when GitHub or GHuser is null.
     */
    public void connect() throws IOException {
        if (githubLogin.isCredentialValid()) {
            valid = true;
            GitHub.connect(GITHUB_LOGIN, GITHUB_OAUTH);
        }
    }

    /*--------------------COLLABORATORS RELATED--------------------*/

    /**
     * Function used to get the name of the collaborators of a specific Repository
     *
     * @param repositoryName Name of the repository to fetch the Collaborators
     * @return returns a string with the name of all the collaborators
     * @throws IOException Thrown due to GHUser
     */
    @org.jetbrains.annotations.NotNull
    public String getCollaborators(String repositoryName) throws IOException {
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
    @Contract(" -> new")
    public @NotNull List<String> collaboratorsList() {
        return new ArrayList<>(collaboratorNames);
    }

    /*--------------------USER RELATED--------------------*/

    /**
     * This function's purpose is to gather all the users' information from a repository.
     *
     * @return all the information from the Collaborators from the repository in question.
     * @throws IOException thrown when the GHuser is null
     */
    public @NotNull List<String> userInfo() throws IOException {
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
    private String getUserInformation(String userInformation, String backup) {
        String information = userInformation;
        if (information == null) information = backup;
        return information;
    }

    /*--------------------REPOSITORY RELATED--------------------*/

    /**
     * Gathers all the repositories that the user has or participated in, although only shows the public ones it does also count the privates
     *
     * @return returns a String with the user's repositories.
     * @throws IOException thrown when the GHuser is null
     */
    public Map<String, List<String>> repositoriesUnderUser() throws IOException {
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
     *
     * @return String containing the information mentioned above
     * @throws IOException thrown due to GHUser
     */
    public @NotNull List<String> numberOfRepositoriesOwned() throws IOException {
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

    /*--------------------FILE RELATED--------------------*/

    /**
     * Gets the README from any repository that was given and reads the data in it.
     *
     * @param repositoryName the repository to look for
     * @return contentReadMe returns the content of the README file
     * @throws IOException thrown when the GHuser is null
     */
    public @NotNull String getReadMe(String repositoryName) throws IOException {
        GHContent readMeContents = userOfLogin.getRepository(repositoryName).getReadme();
        InputStream readMe = readMeContents.read();
        return new String(readMe.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Function to get Branches and their Files from a RepositoryName
     *
     * @param repositoryName The name of the Repository to inspect
     * @return returns a map containing a Map containing the name of the Branch and the Files inside it
     * @throws IOException thrown due to the GitHub
     */
    public @NotNull Map<String, List<String>> getFiles(String repositoryName) throws Exception {
        GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);
        Map<String, GHBranch> getRepos = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
        List<String> branchesSha = new ArrayList<>();

        Map<String, List<String>> files = new HashMap<>();
        List<String> filesInBranch = new ArrayList<>();
        GHTree tree;

        getRepos.forEach((r, s) -> branchesSha.add(s.getSHA1()));

        for (int i = 0; i < getBranchesInRepository(repositoryName).size(); i++) {
            tree = getRepo.getTree(branchesSha.get(i));
            for (int j = 0; j < tree.getTree().size(); j++) {
                filesInBranch.add(tree.getTree().get(j).getPath());
            }
            files.put(getBranchesInRepository(repositoryName).get(i), filesInBranch);
            filesInBranch = new ArrayList<>(); // to clean the List before the new entries
        }
        return files;
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
    public @NotNull String readFileContent(String repositoryName, String fileName, String ref) throws IOException {
        InputStream fileReading = userOfLogin.getRepository(repositoryName).getFileContent(fileName, ref).read();
        return new String(fileReading.readAllBytes(), StandardCharsets.UTF_8);
    }

    /*--------------------BRANCHES RELATED--------------------*/

    /**
     * Returns all the branches in the repository in question
     *
     * @param repositoryName the name of the repository
     * @return returns a list with the names of the branches.
     * @throws Exception thrown when the GHuser is null
     */
    public @NotNull List<String> getBranchesInRepository(String repositoryName) throws Exception {
        Map<String, GHBranch> getRepos = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
        List<String> branchesName = new ArrayList<>();

        getRepos.forEach((r, s) -> branchesName.add(r));
        return branchesName;
    }

    /*--------------------COMMIT RELATED--------------------*/

    /**
     * Gets the commits' data from the repository by user
     *
     * @param repositoryName name of the repository to look
     * @return returns a String which contains the initial and final commit from the repository main branch
     * @throws IOException throws when GitHub is null.
     */
    public @NotNull List<CommitsDataGit> getCommitDataFromRoot(String repositoryName) throws IOException {
        GHRepository getRepo = githubLogin.getRepository(userOfLogin.getLogin() + "/" + repositoryName);


        List<GHCommit> commits = getRepo.listCommits().toList();
        commits.forEach((s) -> {
            try {
                CommitsDataGit commitData = new CommitsDataGit(s.getCommitShortInfo(), s.getCommitDate(), s.getAuthor());
                commitsRoot.add(commitData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return commitsRoot;
    }

    /**
     * @param userLogin
     * @param branchName
     * @return
     * @throws IOException
     */
    public CommitUnpack getCommitBranches(String userLogin, String branchName) throws IOException {
        List<CommitHttpRequest> commits = new ArrayList<>();

        int page = 1;
        while (true) {
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + this.GITHUB_OAUTH)
                    .url(String.format(this.url + "/commits?" + "&author=" + userLogin + "&sha=" + branchName + "&page=%d", page++)).build();

            try (Response response = client.newCall(request).execute()) {
                try {
                    var cm = this.mapper.readValue(Objects.requireNonNull(response.body()).string(), CommitHttpRequest[].class);

                    if (cm.length == 0) {
                        break;
                    }

                    commits.addAll(Arrays.asList(cm));
                } catch (MismatchedInputException e) {
                    break;
                }
            }
        }
        return new CommitUnpack(userLogin, commits);
    }

    /**
     * Gets the number of commits from a certain user.
     *
     * @param repositoryName name of the repository to look
     * @param userLogin      name of the user to retrieve the commits.
     * @return returns a String which contains the initial and final commit from the repository main branch
     * @throws IOException throws when GitHub is null.
     */
    public @NotNull String numberOfCommitsInRoot(String repositoryName, String userLogin) throws IOException {
        GHUser temp = githubLogin.getUser(userLogin);
        getCommitDataFromRoot(repositoryName);
        for (CommitsDataGit commitsData : commitsRoot) {
            if (commitsData.getUserName().equals(temp.getLogin())) {
                info.add(commitsData.getDescription());
            }
        }
        String latest = ("\nLatest commit: " + commitsRoot.get(0).getDescription() + "\nDate: " + commitsRoot.get(0).getDate() + "\nUser: " + commitsRoot.get(0).getUserName() + "\n");
        String initial = ("\nInitial commit: " + commitsRoot.get(commitsRoot.size() - 1).getDescription() + "\nDate: " + commitsRoot.get(commitsRoot.size() - 1).getDate() + "\nUser: " + commitsRoot.get(getCommitDataFromRoot(repositoryName).size() - 1).getUserName() + "\n");
        System.out.println("The user: " + temp.getLogin() + "\nHas these commits: " + info.toString() + "\nWith a total of: " + info.size() + "\n" + initial + latest);
        return "The user: " + temp.getLogin() + "\nHas these commits: " + info.toString() + "\nWith a total of: " + info.size() + "\n" + initial + latest;
    }

    /*--------------------TAGS RELATED--------------------*/

    /**
     * Function to return the Tags that were made in a Repository.
     *
     * @param repositoryName Name of the repository to fetch Tags
     * @return Returns a Map with the Name of the Tag, and it's Date of publish
     * @throws IOException Thrown due to GitHub
     */
    public @NotNull Map<String, Date> getTag(String repositoryName) throws IOException {
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
        return out;
    }

    /*--------------------ADDITIONAL CLASSES--------------------*/

    /**
     * Simple nested class for the Commits, where the data is separated in 3 components, Date, Username and Description.
     */
    public class CommitsDataGit {
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
        CommitsDataGit(GHCommit.ShortInfo description, Date date, @NotNull GHUser userName) throws IOException {
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

    /**
     *
     */
    public static class CommitHttpRequest {
        private String commitDate;
        private String commitMessage;

        @SuppressWarnings("unchecked")
        @JsonProperty("commit")
        private void unpack(Map<String, Object> commit) {
            this.commitMessage = (String) commit.get("message");
            Map<String, String> committer = (Map<String, String>) commit.get("author");
            this.commitDate = committer.get("date");
        }

        public String getCommitDate() {
            return commitDate;
        }

        public String getCommitMessage() {
            return commitMessage;
        }
    }

    /**
     *
     */
    public static class CommitUnpack {
        String personName;
        List<CommitHttpRequest> commits;

        public CommitUnpack(String personName, List<CommitHttpRequest> cms) {
            this.personName = personName;
            this.commits = cms;
        }
    }

}