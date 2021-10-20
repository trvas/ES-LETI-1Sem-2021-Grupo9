import com.google.gson.JsonArray;
import org.kohsuke.github.*;
import java.io.IOException;
import java.util.*;

import static org.kohsuke.github.GitHub.connect;


public class GitManager extends GHAppInstallation{

    private static String toDelete = "henriquevsousa@hotmail.com";
    private static String GITHUB_OAUTH = "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";//"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"; //uses github's token
    private static String username = "trvas"; // "Henrique-DeSousa"; //uses the user's username
    private static GHUser user = new GHUser();
    private static GHRepository rep = new GHRepository();
    private static GitHub githubLogin;
    private static ArrayList<String> finali = new ArrayList<String>();
    private static String[] keys;

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

        public static String userInfo() throws IOException {

            String name = user.getName();
            long userID = user.getId();
            String login = user.getLogin();
            String email = user.getEmail(); if(email == null) email = "Your email isn't public";
            String bio = user.getBio(); if(bio == null)bio   = "No bio available";
            String location = user.getLocation(); if(location == null)location   = "Unknow location";
            String twtUser = user.getTwitterUsername(); if(twtUser == null)twtUser = "Not available";
            String company = user.getCompany(); if(company == null) company = "You don't have a company!";


            String info = email + ";\n" + name + ";\n" + login +";\n" + bio  + ";\n" + location +";\n"+ twtUser + ";\n"+ company+ ";\n";
            //System.out.println(info);
            return info;

        }

        public static void repInfo() throws IOException{
                int repoCount = user.getPublicRepoCount(); if(repoCount == 0){ String noRepo = "You have no Public Repositories"; }

                repositories = user.getRepositories();

                for(Map.Entry<String, GHRepository> t : repositories.entrySet()){

                    finali.add(t.getKey()); //adding Keys to the String [name]

                    String owner = t.getValue().getOwnerName();
                    Set<String> collabs = t.getValue().getCollaboratorNames();

                  
                 
                    long id = t.getValue().getId();
                   //System.out.println("Rep Key: " + a + ". Owner: " + owner + ". ID: " + id);
                    System.out.println(collabs);
                }

                String k = finali.toString();
                String[] p = k.split(", ");
                System.out.println(p[0]);
                System.out.println(repoCount);
            }




        //String name = new GHContent().getName();
        //int total = new GHRepository().getDescription();
       // System.out.println(name + "\n");
}