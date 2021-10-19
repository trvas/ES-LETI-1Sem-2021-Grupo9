import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.kohsuke.github.GitHub.connect;


public class GitManager extends GHAppInstallation{

    private static String toDelete = "henriquevsousa@hotmail.com";
    private static String GITHUB_OAUTH = "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"; //uses github's token
    private static String username = "Henrique-DeSousa"; //uses the user's username
    private static GHUser user = new GHUser();
    private static GHRepository rep = new GHRepository();
    private static GitHub githubLogin;

    private static Map<String,GHRepository> repositories = new HashMap<String, GHRepository>();

    public static void main(String[] args) throws IOException {
        connect();
        userInfo();
        repInfo();
    }
        public static void connect() throws IOException {
            githubLogin = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, username).build().connectUsingOAuth(GITHUB_OAUTH);
            user = githubLogin.getUser(username);
            //System.out.println(githubLogin.getMyself());

        }

        public static void userInfo() throws IOException {

            String email = user.getBio();
            String name = user.getName();

            System.out.println(email);
            System.out.println(name);

        }

        public static void repInfo() throws IOException{
                int publicRep = user.getPublicRepoCount();


                repositories = user.getRepositories();
                for(Map.Entry<String, GHRepository> t : repositories.entrySet()){
                    String a = t.getKey();
                    String owner = t.getValue().getOwnerName();
                    long id = t.getValue().getId();
                    System.out.println(user.getRepository(a));

                   // System.out.println("Rep Key: " + a + ". Owner: " + owner + ". ID: " + id);
                }
            System.out.println(publicRep);
            long id = rep.getId();
            System.out.println(id);
            }




        //String name = new GHContent().getName();
        //int total = new GHRepository().getDescription();
       // System.out.println(name + "\n");



   /* public void fetch(String test){
        GHRepository repo = GitHub.getRepository(test);
    }*/
}