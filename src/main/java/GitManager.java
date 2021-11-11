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
    // "roguezilla"
    //"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"
    private static GHUser user = new GHUser();
    private static GitHub githubLogin;
    private static ArrayList<String> finali = new ArrayList<String>();
    private static boolean valid = false;
    private static boolean getUserInfo = false; //If box checked then retrieve the user information OPTIONAL TO BE USED AS BOOLEAN
    private static GHContent content;
    private static String q = "starboard";  /*ES-LETI-1Sem-2021-Grupo9*/
    private static List<ComsData> coms = new ArrayList<>();

    private static Map<String,GHRepository> repositories = new HashMap<String, GHRepository>();
    private static Map<GHUser, GHCommit> info = new HashMap<>();

    public static void main(String[] args) throws IOException {
        login();
        if(getUserInfo != false) {
            userInfo();
        }
        repInfo();
        getContent(q, "main.py");
        getCommitData(q, user);
    }

        public static void login() throws IOException { // login for the user // TEMP INTERFACE
            Scanner scan = new Scanner(System.in);  // Create a Scanner object

            while (!valid){
                System.out.println("Enter username: ");
                String useTemp = scan.nextLine();  // Read user input

                System.out.println("Enter Token: ");
                String authTemp = scan.nextLine(); // read user's token
                new GitManager(authTemp, useTemp, q);

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
            System.out.println(info);
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
                    //get every collaborator info; add-on later on
                    // give the user the option to see the collaborators and their projects

                    finali.add(t.getKey()); //adding Keys to the String [name]

                    String owner = t.getValue().getOwnerName();
                   // Set<String> collabs = t.getValue().getCollaboratorNames();

                  
                 
                    long id = t.getValue().getId();
                   //System.out.println("Rep Key: " + a + ". Owner: " + owner + ". ID: " + id);
                   // System.out.println(collabs);
                }

                String k = finali.toString();
                //String[] p = k.split(", "); no point in using this, better to give the user the full String/List of Repositories and just ask them to select one to view further info
               System.out.println(k);
               // System.out.println(p[1]);


            }

            public static void getContent(String repositoryName, String fileToRead) throws IOException {
                String defaultBranch = user.getRepository(repositoryName).getDefaultBranch();
                if(user.getRepository(repositoryName).getReadme() != null) {
                    GHContent readMeContents = user.getRepository(repositoryName).getReadme();
                    InputStream readMe = readMeContents.read();
                    String cnt = new String(readMe.readAllBytes(), StandardCharsets.UTF_8);
                }

                InputStream fileContent = user.getRepository(repositoryName).getFileContent(fileToRead).read();
                String fileContents = new String(fileContent.readAllBytes(), StandardCharsets.UTF_8);


                System.out.println(fileContents);
               // System.out.println(content.read());
            }

            public static void getCommitData(String repositoryName, GHUser name_User) throws IOException {
                GHRepository getRepo = githubLogin.getRepository(name_User.getLogin() + "/" + repositoryName);
                List<GHCommit> commits = getRepo.listCommits().toList();
                 commits.forEach((s) ->  {
                     try {
                         ComsData commitData = new ComsData(s.getCommitShortInfo() , s.getCommitDate(), s.getCommitter());
                         coms.add(commitData);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });
                System.out.println();
                System.out.println("Lastest commit: " + coms.get(0).getDescription() +  "\nDate: " + coms.get(0).getDate() + "\nUser: " + coms.get(0).getUserName());
                System.out.println("First commit: " + coms.get(coms.size()-1).getDescription() + "\nDate: " + coms.get(coms.size()-1).getDate());

                //get ~Total number of commits
                //System.out.println(getRepo.getCompare(commits.get(0), commits.get(1)).getTotalCommits());
            }




            public static class ComsData{
                private GHCommit.ShortInfo description;
                private Date date;
                private String userName;

                ComsData(GHCommit.ShortInfo description, Date date, GHUser userName) throws IOException {
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