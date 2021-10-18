import org.kohsuke.github.*;

import java.io.IOException;

public class GitManager{

   String GITHUB_OAUTH = "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";
   String user_ID = "Henrique-DeSousa";

    GitHub github = new GitHubBuilder().withOAuthToken(GITHUB_OAUTH, user_ID).build();

    public GitManager() throws IOException {
    }

    public String getUser_ID() {
        return user_ID;
    }
     public void GHContent(){

     }

    public static void main(String[] args) {
    }
}