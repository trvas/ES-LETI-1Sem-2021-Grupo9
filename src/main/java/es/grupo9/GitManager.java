package es.grupo9;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

public class GitManager{

    private static String GITHUB_REPO_NAME;
    private static String GITHUB_OAUTH;
    private static String GITHUB_LOGIN;
    public static boolean valid = false;
    private static GitHub githubLogin;
    private static GHUser user = new GHUser();

    /**
     *
     * @param ID gitHub's token
     * @param USERNAME uses the user's username
     * @param REPO_NAME
     * @throws IOException
     */
    public GitManager(String ID, String USERNAME, String REPO_NAME) throws IOException {
        this.GITHUB_LOGIN = USERNAME;
        this.GITHUB_OAUTH = ID;
        this.GITHUB_REPO_NAME = REPO_NAME;
        connect();
    }


    /**
     * Takes the user information inputed into the UI to connect to the GitHub
     * if that information matches the one in github, it will connect otherwise it loops.
     * @throws IOException to avoid problems
     */
    public static void connect() throws IOException {
        githubLogin = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
        if(githubLogin.isCredentialValid()) {
            valid = true;
            githubLogin.connect(GITHUB_LOGIN, GITHUB_OAUTH);
            user = githubLogin.getUser(GITHUB_LOGIN);
        }
    }

    public static void main(String[] args) {


    }
}