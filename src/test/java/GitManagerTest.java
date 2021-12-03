import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

class GitManagerTest {

    GitManager gitManager;

    @BeforeEach
    void setUp() throws IOException {
        gitManager = new GitManager("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", "Henrique-DeSousa", "test_repo" );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void main() {
    }

    @Test
    void setGithubBranchName() {
       // Assertions.assertEquals(branchN, gitManager.setGithubBranchName(branchN));
    }

    @Test
    void setCommitReference() {
       // Assertions.assertNotEquals(null, gitManager.setCommitReference("059178ff832ae4b5372cd2ffa5d0a44ac1644d4d"));
    }

    @Test
    void setGithubFileName() {
        //Assertions.assertEquals(info, gitManager.userInfo());
    }

    @Test
    void getCollaborators() throws IOException {
        Assertions.assertNotEquals(null, gitManager.getCollaborators("test_repo"));
    }

    @Test
    void collaboratorsList() {
        Assertions.assertNotEquals(null, gitManager.collaboratorsList());
    }

    @Test
    void userInfo() throws IOException {
        String info =
"""
[https://github.com/rfgoo-iscte
https://avatars.githubusercontent.com/u/91742965?v=4
Rodrigo Guerreiro;
rfgoo-iscte;
Your email isn't public;
Violence is never the answer... it's the solution!;
Lisboa;
Not available;
ISCTE.
, https://github.com/trvas
https://avatars.githubusercontent.com/u/91333301?v=4
trvas;
trvas;
Your email isn't public;
-_________-;
Unknown location;
Not available;
You don't have a company.
, https://github.com/Henrique-DeSousa
https://avatars.githubusercontent.com/u/73469590?v=4
Henrique Vieira De Sousa;
Henrique-DeSousa;
Your email isn't public;
test;
Portugal;
Not available;
ISCTE.
, https://github.com/glrss-iscte
https://avatars.githubusercontent.com/u/91333400?v=4
Gonçalo Rodrigues ;
glrss-iscte;
Your email isn't public;
No bio available;
Lissabona;
Not available;
You don't have a company.
]

""";
                ;
        Assertions.assertEquals(info, gitManager.userInfo());
    }

    @Test
    void userRepositories() throws IOException {
        String urp =
                """
                        {rfgoo-iscte=[Snake, Space_invaders, test_repo, tic_tac_toe, wheatherApp.py], trvas=[es-testes], Henrique-DeSousa=[ES-2021-ETC1-99, Engineering-Practices-for-Building-Quality-Software, test_repo], glrss-iscte=[]}
                        """;
        Assertions.assertEquals(urp, gitManager.userRepositories());
    }

    @Test
    void numberOfRepositoriesOwned() throws IOException {
        String nro = """
                [
                rfgoo-iscte
                Number of Public Repositories: 5
                You have no visible private Repositories.0,
                trvas
                Number of Public Repositories: 1
                You have no visible private Repositories.0,
                Henrique-DeSousa
                Number of Public Repositories: 3
                Number of Private Repositories: 4,
                glrss-iscte
                You have no Public Repositories.0
                You have no visible private Repositories.0]         
                """;
        Assertions.assertEquals(nro, gitManager.numberOfRepositoriesOwned());
    }

    @Test
    void getBranchesInRepository() throws Exception {
        String bra = "[main, master]";
        Assertions.assertEquals(bra, gitManager.getBranchesInRepository("test_repo"));
    }

    @Test
    void testGetReadMe() throws IOException {
        Assertions.assertNotEquals(null, gitManager.getReadMe("test_repo"));
    }

    @Test
    void getFiles() throws Exception {
        String files = """
                {main=[README.md, kekwtest], master=[.idea, README.md, calc.py, calc2.0.py]}""";
        Assertions.assertEquals(files, gitManager.getFiles("test_repo"));
    }

    @Test
    void readFileContent() throws IOException {
        Assertions.assertNotEquals(null, gitManager.readFileContent("test_repo", "README.md", "059178ff832ae4b5372cd2ffa5d0a44ac1644d4d"));
    }

    @Test
    void getCommitDataFromRoot() throws IOException {
        String rawData = """
                [GitManager$CommitsDataGit@79c97cb, GitManager$CommitsDataGit@2d9caaeb, GitManager$CommitsDataGit@42a15bdc, GitManager$CommitsDataGit@44a59da3]
                [GitManager$CommitsDataGit@79c97cb, GitManager$CommitsDataGit@2d9caaeb, GitManager$CommitsDataGit@42a15bdc, GitManager$CommitsDataGit@44a59da3, GitManager$CommitsDataGit@6e01f9b0, GitManager$CommitsDataGit@2b9ed6da, GitManager$CommitsDataGit@6c61a903, GitManager$CommitsDataGit@658c5a19]""";
        Assertions.assertEquals(rawData, gitManager.getCommitDataFromRoot("test_repo"));
    }

    @Test
    void getCommitBranches() throws IOException {
        String rawCom = """
                GitManager$CommitUnpack@7d1cfb8b""";
        Assertions.assertEquals(rawCom, gitManager.getCommitBranches("Henrique-DeSousa", "main"));
    }

    @Test
    void numberOfCommitsInRoot() throws IOException {
        String comms = """
                The user: Henrique-DeSousa
                Has these commits: [Update kekwtest, Create kekwtest, Initial commit] in the Root: main
                With a total of: 3
                                
                Initial commit: Initial commit
                Date: Sun Nov 21 14:45:13 WET 2021
                User: Henrique-DeSousa
                                
                Latest commit: Update kekwtest
                Date: Sun Nov 21 16:14:31 WET 2021
                User: Henrique-DeSousa""";

        Assertions.assertEquals(comms, gitManager.numberOfCommitsInRoot("test_repo", "Henrique-DeSousa"));
    }

    @Test
    void getTag() throws IOException {
        String tag = """
               {test=Sun Nov 21 14:47:21 WET 2021, Tag2=Sun Nov 21 16:14:31 WET 2021}""";
        Assertions.assertEquals(tag, gitManager.getTag("test_repo"));
    }
}