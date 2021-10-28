import org.kohsuke.github.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class GitManager extends GHAppInstallation{

    private static String GITHUB_REPO_NAME;
    private static String toDelete = "henriquevsousa@hotmail.com";
    private static String GITHUB_OAUTH; // gitHub's token
    private static String GITHUB_LOGIN;  //uses the user's username
    //"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" Tatiana // "trvas"
    //"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"; Henrique // "Henrique-DeSousa"

    private static GHUser user = new GHUser();
    private static GitHub githubLogin;
    private static ArrayList<String> finali = new ArrayList<String>();
    private static boolean valid = false;
    private static boolean getUserInfo = false; //If box checked then retrieve the user information OPTIONAL
    private static GHContent content;
    private static String q = "ES-LETI-1Sem-2021-Grupo9";

    private static Map<String,GHRepository> repositories = new HashMap<String, GHRepository>();

    public static void main(String[] args) throws IOException {
        login();
        if(getUserInfo != false)
            userInfo();
        repInfo();
        getContent(q, "README.md");
    }

        public static void login() throws IOException { // login for the user // TEMP INTERFACE
            Scanner scan = new Scanner(System.in);  // Create a Scanner object

            while (!valid){
                System.out.println("Enter username: ");
                String useTemp = scan.nextLine();  // Read user input

                System.out.println("Enter Token: ");
                String authTemp = scan.nextLine(); // read user's token
                new GitManager(authTemp, useTemp, null);

            }
            //System.out.println("Username is: " + GITHUB_LOGIN + "\nToken is: " + GITHUB_OAUTH);  // Output user input
        }

        public GitManager(String ID, String USERNAME, String REPO_NAME) throws IOException {
            this.GITHUB_LOGIN = USERNAME;
            this.GITHUB_OAUTH = ID;
            this.GITHUB_REPO_NAME = REPO_NAME;
            connect();
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
            //System.out.println(info);
            //System.out.println(userID);
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
               // System.out.println(p[1]);


            }

            public static void getContent(String name, String k) throws IOException {
                System.out.println(user.getRepository(name).getDefaultBranch());
                InputStream contents = user.getRepository(name).getFileContent(k).read();
                String cnt = new String(contents.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(cnt);
                //System.out.println(content.read());
            }
}