package es.grupo9;

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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GitManager class, allowing for the search in GIT using an API to get specific information
 */
public class GitManager {

    private static String GITHUB_OAUTH = "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"; // gitHub's token
    private static String GITHUB_LOGIN = "Henrique-DeSousa"; //User's login
    private static String GITHUB_REPO_NAME = "test_repo"; //Name of the repository
    private static String GITHUB_BRANCH_NAME; //Name of the branch
    private static String COMMIT_REFERENCE; // Reference to get the file from.
    private static String GITHUB_FILE_NAME; // Name of the file to look
    private GHUser userOfLogin;
    private GitHub gitHub;
    private boolean valid = false; // if the flag is set to true, then it moves
    private static boolean getUserInfo = true; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN
    private Set<String> collaboratorNames = new HashSet<>();
    private List<CommitsDataGit> commitsDataRoot = new ArrayList<>();
    private List<String> branchesName = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private String url;
    private ObjectMapper mapper;

    /**
     * The main function of this class
     *
     * @param args normal thing in a main
     * @throws Exception due to the functions it's calling, GitHub or GHUser being null
     */
    public static void main(String[] args) throws Exception {
        GitManager GM = new GitManager(GITHUB_OAUTH, GITHUB_LOGIN, GITHUB_REPO_NAME);
        GM.connect();

        /*
        GM.getCollaborators(GITHUB_REPO_NAME);
        if (getUserInfo) {
            GM.userInfo();
        }
        GM.setGithubBranchName("main");
        GM.setCommitReference("059178ff832ae4b5372cd2ffa5d0a44ac1644d4d");
        GM.setGithubFileName("README.md");
        GM.getBranchesInRepository(GITHUB_REPO_NAME);
        GM.userRepositories();
        GM.numberOfRepositoriesOwned();
        GM.numberOfCommitsInRoot(GITHUB_REPO_NAME, GITHUB_LOGIN);

        GM.getFiles(GITHUB_REPO_NAME);
        GM.getReadMe(GITHUB_REPO_NAME);


        GM.readFileContent(GITHUB_REPO_NAME, GITHUB_FILE_NAME, COMMIT_REFERENCE);

        var a = GM.getCommitBranches(GITHUB_LOGIN, GITHUB_BRANCH_NAME);
        for (var commit : a.commits) {
            System.out.println(commit.commitMessage + " " + commit.commitDate + " " + a.personName);
        }
        GM.getTag(GITHUB_REPO_NAME); */

        System.out.println(GM.collaboratorsList());

    }

    /**
     * Constructor Function, create the object.
     *
     * @param auth     receives the user TOKEN
     * @param userName receives the user's LOGIN
     * @param repoName receives the user desired repository
     * @throws IOException throws exception when GitHub is null or GHUser is null
     */
    public GitManager(String auth, String userName, String repoName) throws IOException {
        this.GITHUB_LOGIN = userName;
        this.GITHUB_OAUTH = auth;
        this.GITHUB_REPO_NAME = repoName;
        gitHub = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
        userOfLogin = gitHub.getUser(GITHUB_LOGIN);
        this.url = "https://api.github.com/repos/" + GITHUB_LOGIN + "/" + GITHUB_REPO_NAME; // trvas + ES-LETI-1Sem-2021-Grupo9
        this.mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    public String getGithubBranchName() {
        return GITHUB_BRANCH_NAME;
    }

    public String getCommitReference() {
        return COMMIT_REFERENCE;
    }

    public String getGithubFileName() {
        return GITHUB_FILE_NAME;
    }

    public void setGithubBranchName(String githubBranchName) {
        this.GITHUB_BRANCH_NAME = githubBranchName;
    }

    public void setCommitReference(String commitReference) {
        this.COMMIT_REFERENCE = commitReference;
    }

    public void setGithubFileName(String githubFileName) {
        this.GITHUB_FILE_NAME = githubFileName;
    }


    /**
     * This function is to complement the previous function by verifying that the information provided is correct
     *
     * @throws IOException throws when GitHub or GHuser is null.
     */
    public void connect() throws IOException {
        if (gitHub.isCredentialValid()) {
            valid = true;
            GitHub.connect(GITHUB_LOGIN, GITHUB_OAUTH);
        }
    }

    /*--------------------COLLABORATORS RELATED--------------------*/


    // check if this is really needed or if getCollaboratorsList is enough
    /**
     * Function used to get the name of the collaborators of a specific Repository
     *
     * @param repositoryName Name of the repository to fetch the Collaborators
     * @return returns a string with the name of all the collaborators
     * @throws IOException Thrown due to GHUser
     */
    @NotNull
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
            user = gitHub.getUser(collaboratorsList().get(i));

            String name = user.getName();
            URL url = user.getHtmlUrl();

            String avatarUrl = user.getAvatarUrl();
            String login = user.getLogin();
            String email = getUserInformation(user.getEmail(), "Email not public");
            String bio = getUserInformation(user.getBio(), "No bio available");
            String location = getUserInformation(user.getLocation(), "Unknown location");
            String twtUser = getUserInformation(user.getTwitterUsername(), "No Twitter available");
            String company = getUserInformation(user.getCompany(), "No company");

            info = url + "\n" + avatarUrl + "\n" + name + ";\n" + login + ";\n" + email + ";\n" + bio + ";\n" + location + ";\n" + twtUser + ";\n" + company + ".\n";
            collaboratorsInfo.add(info);
        }
        // System.out.println(collaboratorsInfo);
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


    // this function returns the repositories of all users
    /**
     * Gathers all the repositories that the user has or participated in, although only shows the public ones it does also count the privates
     *
     * @return returns a String with the user's repositories.
     * @throws IOException thrown when the GHuser is null
     */
    public Map<String, List<String>> userRepositories() throws IOException {
        Map<String, List<String>> repositoriesUserData = new HashMap<>();
        GHUser user;

        for (int i = 0; i < collaboratorsList().size(); i++) {
            user = gitHub.getUser(collaboratorsList().get(i));

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
            user = gitHub.getUser(collaboratorsList().get(i));

            publicRepositories = "Number of Public Repositories: ";
            privateRepositories = "Number of Private Repositories: ";

            repoCount = user.getPublicRepoCount();
            if (user.getPublicRepoCount() == 0) {
                publicRepositories = "No Public Repositories. ";
            }

            privateRepositoryCount = user.getTotalPrivateRepoCount();
            if (privateRepositoryCount.isEmpty()) {
                privateRepoCount = 0;
                privateRepositories = "No visible Private Repositories. ";
            } else {
                privateRepoCount = privateRepositoryCount.get();
            }
            out.add("\n" + user.getLogin() + "\n" + publicRepositories + repoCount + "\n" + privateRepositories + privateRepoCount);
        }
        return out;
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
        Map<String, GHBranch> getRepos = gitHub.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
        getRepos.forEach((r, s) -> this.branchesName.add(r));
        return this.branchesName;
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
        GHRepository getRepo = gitHub.getRepository(userOfLogin.getLogin() + "/" + repositoryName);
        Map<String, GHBranch> getRepos = gitHub.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getBranches();
        List<String> branchesSha = new ArrayList<>();

        Map<String, List<String>> files = new HashMap<>();
        List<String> filesInBranch = new ArrayList<>();
        GHTree tree;

        getRepos.forEach((r, s) -> branchesSha.add(s.getSHA1()));

        for (int i = 0; i < branchesName.size(); i++) {
            tree = getRepo.getTree(branchesSha.get(i));
            for (int j = 0; j < tree.getTree().size(); j++) {
                filesInBranch.add(tree.getTree().get(j).getPath());
            }
            files.put(branchesName.get(i), filesInBranch);
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

    /*--------------------COMMIT RELATED--------------------*/

    /**
     * Gets the commits' data from the root of the Repository and by user
     *
     * @param repositoryName name of the repository to look
     * @return returns a String which contains the initial and final commit from the repository main branch
     * @throws IOException throws when GitHub is null.
     */
    public @NotNull List<CommitsDataGit> getCommitDataFromRoot(String repositoryName) throws IOException {
        GHRepository getRepo = gitHub.getRepository(userOfLogin.getLogin() + "/" + repositoryName);


        List<GHCommit> commits = getRepo.listCommits().toList();
        commits.forEach((s) -> {
            try {
                CommitsDataGit commitData = new CommitsDataGit(s.getCommitShortInfo(), s.getCommitDate(), s.getAuthor());
                commitsDataRoot.add(commitData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return commitsDataRoot;
    }

    /**
     * Function to get Commits from the Branches, since the API does not allow for that.
     *
     * @param user       User's login to look for their commits in the project
     * @param branchName The branch in which to look for, for the commits
     * @return returns a Map into a nested class for it to be able to be processed and used.
     * @throws IOException throws Exception due to .execute and .string
     */
    public CommitUnpack getCommitFromBranches(String user, String branchName) throws IOException {
        List<CommitHttpRequest> commits = new ArrayList<>();

        int page = 1;
        while (true) {
            Request request = new Request.Builder()
                    .addHeader("Authorization", "Bearer " + GITHUB_OAUTH)
                    .url(String.format(this.url + "/commits?" + "&author=" + user + "&sha=" + branchName + "&page=%d", page++)).build();

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
        return new CommitUnpack(user, commits);
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
        GHUser temp = gitHub.getUser(userLogin);
        getCommitDataFromRoot(repositoryName);
        List<String> info = new ArrayList<>();

        for (CommitsDataGit commitsData : commitsDataRoot) {
            if (commitsData.getUserName().equals(temp.getLogin())) {
                info.add(commitsData.getDescription());
            }
        }
        String latest = ("\nLatest commit: " + commitsDataRoot.get(0).getDescription() + "\nDate: " + commitsDataRoot.get(0).getDate() +
                "\nUser: " + commitsDataRoot.get(0).getUserName() + "\n");
        String initial = ("\nInitial commit: " + commitsDataRoot.get(commitsDataRoot.size() - 1).getDescription() + "\nDate: " + commitsDataRoot.get(commitsDataRoot.size() - 1).getDate() +
                "\nUser: " + commitsDataRoot.get(getCommitDataFromRoot(repositoryName).size() - 1).getUserName() + "\n");
        return "The user: " + temp.getLogin() + "\nHas these commits: " + info + "in Root: " + gitHub.getRepository(userOfLogin.getLogin() + "/" + repositoryName).getDefaultBranch() + "\nWith a total of: " + info.size() + "\n" + initial + latest;
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
        GHRepository getRepo = gitHub.getRepository(userOfLogin.getLogin() + "/" + repositoryName);
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
     * Breakdown of Information received from the GitHub API related to commits.
     * params used Date, Username and Description.
     */
    public class CommitsDataGit {
        private final GHCommit.ShortInfo description;
        private final Date date;
        private final String userName;

        /**
         * constructor of the class.
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
     * Class to allow the HttpRequest data from the commits to be processed and analysed
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
     * Nested class used in the function to get the commits from the branches, accepts a Map and breaks it down
     * into the several components.
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