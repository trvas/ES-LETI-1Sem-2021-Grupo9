import org.kohsuke.github.*;
import java.io.IOException;
import java.util.*;


public class GitManager extends GHAppInstallation{

    private static String toDelete = "henriquevsousa@hotmail.com";
    private static String GITHUB_OAUTH; // gitHub's token
    private static String GITHUB_LOGIN;  //uses the user's username
    //"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" Tatiana // "trvas"
    //"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"; Henrique // "Henrique-DeSousa"

    private static GHUser user = new GHUser();
    private static GitHub githubLogin;
    private static ArrayList<String> finali = new ArrayList<String>();
    private static boolean valid = false;

    private static Map<String,GHRepository> repositories = new HashMap<String, GHRepository>();

    public static void main(String[] args) throws IOException {
        login();
        userInfo();
        repInfo();
    }

        public static void login() throws IOException { // login for the user
            Scanner scan = new Scanner(System.in);  // Create a Scanner object

            while (!valid){
                System.out.println("Enter username: ");
                String useTemp = scan.nextLine();  // Read user input

                System.out.println("Enter Token: ");
                String authTemp = scan.nextLine(); // read token
                new GitManager(authTemp, useTemp);
                connect();
            }//System.out.println("Username is: " + GITHUB_LOGIN + "\nToken is: " + GITHUB_OAUTH);  // Output user input
        }

        public GitManager(String ID, String userName){
            this.GITHUB_LOGIN = userName;
            this.GITHUB_OAUTH = ID;
        }

        public static void connect() throws IOException {
            githubLogin = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, GITHUB_LOGIN).build();
                if(githubLogin.isCredentialValid()) {
                    valid = true;
                    githubLogin.connect(GITHUB_LOGIN, GITHUB_OAUTH);
                    user = githubLogin.getUser(GITHUB_LOGIN);
                }
        }

        public static String userInfo() throws IOException {

            String name = user.getName();
            long userID = user.getId();

            String token = user.getAvatarUrl(); // add this to the UI
            System.out.println(token);

            String login = user.getLogin();
            String email = user.getEmail(); if(email == null) email = "Your email isn't public";
            String bio = user.getBio(); if(bio == null)bio   = "No bio available";
            String location = user.getLocation(); if(location == null)location   = "Unknown location";
            String twtUser = user.getTwitterUsername(); if(twtUser == null)twtUser = "Not available";
            String company = user.getCompany(); if(company == null) company = "You don't have a company!";


            String info = "\n" + email + ";\n" + name + ";\n" + login +";\n" + bio  + ";\n" + location +";\n"+ twtUser + ";\n"+ company + ".\n";
            System.out.println(info);
            return info;
        }

        public static void repInfo() throws IOException{
                int repoCount = user.getPublicRepoCount(); if(repoCount == 0){ String noRepo = "You have no Public Repositories"; }
                Optional<Integer> privRepoCount = user.getTotalPrivateRepoCount(); // needs proper token to find the private

                //System.out.println("Number of Private Repositories: " + privRepoCount.get());

                repositories = user.getRepositories();
                System.out.println(repoCount);

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
                System.out.println(p[1]);


            }
}