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
import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GitManager class. Uses the Github API to search for information.
 */
public class GitManager {

    private static String GITHUB_OAUTH; // gitHub's token
    private static String GITHUB_LOGIN; // User's login
    private static String GITHUB_REPO_NAME; //Name of the repository
    private static String GITHUB_BRANCH_NAME; //Name of the branch
    private static String COMMIT_REFERENCE; // Reference to get the file from.
    private static String GITHUB_FILE_NAME; // Name of the file to look
    private final GHUser userOfLogin;
    private final GitHub gitHub;
    private boolean valid = false; // if the flag is set to true, then it moves
    private static final boolean getUserInfo = true; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN
    private List<String> collaboratorsNames = new ArrayList<>();
    private final List<String> branchesName = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    private final String url;
    private final ObjectMapper mapper;

    /**
     * Constructor function, creates an instance of GitManager.
     *
     * @param auth     User's Github TOKEN.
     * @param userName User's Github LOGIN username.
     * @param repoName Github repository name.
     * @throws IOException throws exception when GitHub is null or GHUser is null.
     */
    public GitManager(String auth, String userName, String repoName) throws IOException {
        GITHUB_LOGIN = userName;
        GITHUB_OAUTH = auth;
        GITHUB_REPO_NAME = repoName;
        gitHub = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
        userOfLogin = gitHub.getUser(GITHUB_LOGIN);
        this.url = "https://api.github.com/repos/" + GITHUB_LOGIN + "/" + GITHUB_REPO_NAME;
        this.mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        connect();
    }

    /**
     * This complements the previous function by verifying that the information provided is correct
     *
     * @throws IOException throws when GitHub or GHuser is null.
     */
    public void connect() throws IOException {
        if (gitHub.isCredentialValid()) {
            valid = true;
            GitHub.connect(GITHUB_LOGIN, GITHUB_OAUTH);
        }
    }

    /**
     * Gets the Github branch name.
     *
     * @return String Github branch name.
     */
    public String getGithubBranchName() {
        return GITHUB_BRANCH_NAME;
    }

    /**
     * Gets the Commit Reference.
     *
     * @return String Commit Reference.
     */
    public String getCommitReference() {
        return COMMIT_REFERENCE;
    }

    /**
     * Gets the Github File Name.
     *
     * @return String Github File name.
     */
    public String getGithubFileName() {
        return GITHUB_FILE_NAME;
    }

    /**
     * Sets the Github branch name.
     *
     * @param githubBranchName String New branch name.
     */
    public void setGithubBranchName(String githubBranchName) {
        GITHUB_BRANCH_NAME = githubBranchName;
    }

    /**
     * Sets the Commit Reference.
     *
     * @param commitReference String New reference.
     */
    public void setCommitReference(String commitReference) {
        COMMIT_REFERENCE = commitReference;
    }

    /**
     * Sets the Github File name.
     *
     * @param githubFileName String New file name.
     */
    public void setGithubFileName(String githubFileName) {
        GITHUB_FILE_NAME = githubFileName;
    }

    /*--------------------COLLABORATORS RELATED--------------------*/

    /**
     * Gets the name of the collaborators of the repository.
     *
     * @return String Name of all the collaborators
     * @throws IOException Thrown due to GHUser
     */
    @org.jetbrains.annotations.NotNull
    public List<String> getCollaborators() throws IOException {
        Set<String> collaboratorNames = userOfLogin.getRepository(GITHUB_REPO_NAME).getCollaboratorNames();
        this.collaboratorsNames = new ArrayList<>(collaboratorNames);
        return collaboratorsNames;
    }

    /*--------------------USER RELATED--------------------*/

    /**
     * Gathers all the users' information from a repository.
     *
     * @return A List of All the information from the Collaborators from the repository in question.
     * @throws IOException thrown when the GHuser is null.
     */
    public @NotNull List<String> userInfo() throws IOException {
        GHUser user;
        String info;
        List<String> collaboratorsInfo = new ArrayList<>();

        for (String collaboratorName : collaboratorsNames) {
            info = getUserDataString(collaboratorName);
            collaboratorsInfo.add(info);
        }
        return collaboratorsInfo;
    }

    /**
     * Function to get User Data in a String format.
     *
     * @param collaboratorName Name of GHUser.
     * @return String with all the user Data from their profile.
     * @throws IOException due to GHUser potentially being null
     */
    @NotNull
    private String getUserDataString(String collaboratorName) throws IOException {
        GHUser user;
        String info;
        user = gitHub.getUser(collaboratorName);

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
        return info;
    }

    /**
     * This function is simply to ease the previous one in the aspect that if any information is missing, it's simply replaced.
     *
     * @param userInformation Gets the user information from Github.
     * @param backup          Set so if the previous param is null it returns this String instead.
     * @return String Information which corresponds to the query.
     */
    private String getUserInformation(String userInformation, String backup) {
        String information = userInformation;
        if (information == null) information = backup;
        return information;
    }

    /*--------------------REPOSITORY RELATED--------------------*/

    /**
     * Gathers all the repositories that the user has or participated in, although only shows the public ones it does also count the privates.
     *
     * @return returns a String with the user's repositories.
     * @throws IOException thrown when the GHuser is null
     */
    public Map<String, List<String>> userRepositories() throws IOException {
        Map<String, List<String>> repositoriesUserData = new HashMap<>();
        GHUser user;

        for (String collaboratorsName : collaboratorsNames) {
            user = gitHub.getUser(collaboratorsName);

            Map<String, GHRepository> temp = user.getRepositories();
            repositoriesUserData.put(user.getLogin(), temp.values().stream().map(GHRepository::getName).collect(Collectors.toList()));
        }
        return repositoriesUserData;
    }

    /**
     * Gets the Total number of Public and Private (user's token only) repositories of each collaborator.
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

        for (String collaboratorsName : collaboratorsNames) {
            user = gitHub.getUser(collaboratorsName);

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
     * Returns all the branches in the repository.
     *
     * @return A list of Names of the branches.
     * @throws Exception thrown when the GHuser is null.
     */
    public @NotNull List<String> getBranchesInRepository() throws Exception {
        if (branchesName.isEmpty()) {
            Map<String, GHBranch> getRepos = gitHub.getRepository(userOfLogin.getLogin() + "/" + GITHUB_REPO_NAME).getBranches();
            getRepos.forEach((r, s) -> this.branchesName.add(r));
        }
        return this.branchesName;
    }

    /*--------------------FILE RELATED--------------------*/

    /**
     * Gets the README from the repository and reads the data in it.
     *
     * @return String Content of the README file
     * @throws IOException thrown when the GHuser is null
     */
    public @NotNull String getReadMe() throws IOException {
        GHContent readMeContents = userOfLogin.getRepository(GITHUB_REPO_NAME).getReadme();
        InputStream readMe = readMeContents.read();
        return new String(readMe.readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Gets Branches and their Files from a repository.
     *
     * @return A map of Name of the Branch and the Files inside it.
     * @throws IOException thrown due to the GitHub.
     */
    public @NotNull Map<String, List<String>> getFiles() throws Exception {
        GHRepository getRepo = gitHub.getRepository(userOfLogin.getLogin() + "/" + GITHUB_REPO_NAME);
        Map<String, GHBranch> getRepos = gitHub.getRepository(userOfLogin.getLogin() + "/" + GITHUB_REPO_NAME).getBranches();
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
     * @param fileName Name of the file
     * @param ref      Commit reference of the file.
     * @return String Contents of the file, be it in python, java, or any other language.
     * @throws IOException thrown when the GHuser is null.
     */
    public @NotNull String readFileContent(String fileName, String ref) throws IOException {
        InputStream fileReading = userOfLogin.getRepository(GITHUB_REPO_NAME).getFileContent(fileName, ref).read();
        return new String(fileReading.readAllBytes(), StandardCharsets.UTF_8);
    }

    /*--------------------COMMIT RELATED--------------------*/

    /**
     * Function to get Commits from the Branches, since the API does not allow for that.
     *
     * @param user       User's login to look for their commits in the project.
     * @param branchName Branch in which to look for, for the commits.
     * @return returns a Map into a nested class for it to be able to be processed and used.
     * @throws IOException throws Exception due to .execute and .string.
     */
    public CommitUnpack getCommits(String user, String branchName) throws IOException {
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

    /*--------------------TAGS RELATED--------------------*/

    /**
     * Function to return the Tags that were made in a Repository.
     *
     * @return Returns a Map with the Name of the Tag, and it's Date of publish
     * @throws IOException Thrown due to GitHub
     */
    public List<Object[]> getTag() throws IOException {
        GHRepository getRepo = gitHub.getRepository(userOfLogin.getLogin() + "/" + GITHUB_REPO_NAME);
        List<GHTag> tags = getRepo.listTags().toList();
        List<String> tagNames = new ArrayList<>();
        List<GHCommit> tagCommits = new ArrayList<>();
        List<Date> tagDate = new ArrayList<>();
        List<Object[]> out = new ArrayList<>();

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
            out.add(new Object[]{tagNames.get(i), tagDate.get(i)});
        }
        return out;
    }

    /*--------------------ADDITIONAL CLASSES--------------------*/

    /**
     * Class to allow the HttpRequest data from the commits to be processed and analysed.
     */
    public static class CommitHttpRequest {
        public String commitDate;
        public String commitMessage;

        @SuppressWarnings("unchecked")
        @JsonProperty("commit")
        public void unpack(Map<String, Object> commit) {
            this.commitMessage = (String) commit.get("message");
            Map<String, String> committer = (Map<String, String>) commit.get("author");
            this.commitDate = committer.get("date");
            commitDate = commitDate.substring(0, commitDate.indexOf("T"));
        }
    }

    /**
     * Nested class used in the function to get the commits from the branches, accepts a Map and breaks it down
     * into the several components.
     */
    public static class CommitUnpack {
        String name;
        List<CommitHttpRequest> commits;

        public CommitUnpack(String personName, List<CommitHttpRequest> cms) {
            this.name = personName;
            this.commits = cms;
        }
    }
}