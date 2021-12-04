package es.grupo9;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Array;
import java.util.*;

class GitManagerTest {

    GitManager gitManager;

    @BeforeEach
    void setUp() throws IOException {
        gitManager = new GitManager("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", "Henrique-DeSousa", "test_repo" );
        gitManager.connect();
        gitManager.getCollaborators("test_repo");
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
        String expected = """
                            Collaborators for the following Repository: test_repo
                            Are: [rfgoo-iscte, trvas, Henrique-DeSousa, glrss-iscte]""";
        Assertions.assertEquals(expected, gitManager.getCollaborators("test_repo"));
    }

    @Test
    void collaboratorsList() {
        List<String> expected = Arrays.asList("rfgoo-iscte", "trvas", "Henrique-DeSousa", "glrss-iscte");
        Assertions.assertEquals(expected, gitManager.collaboratorsList());
    }

    @Test
    void userInfo() throws IOException {
        List<String> expected = Arrays.asList(
                """
                https://github.com/rfgoo-iscte
                https://avatars.githubusercontent.com/u/91742965?v=4
                Rodrigo Guerreiro;
                rfgoo-iscte;
                Email not public;
                Violence is never the answer... it's the solution!;
                Lisboa;
                No Twitter available;
                ISCTE.
                """,
                """
                https://github.com/trvas
                https://avatars.githubusercontent.com/u/91333301?v=4
                trvas;
                trvas;
                Email not public;
                -_________-;
                Unknown location;
                No Twitter available;
                No company.
                """,
                """
                https://github.com/Henrique-DeSousa
                https://avatars.githubusercontent.com/u/73469590?v=4
                Henrique Vieira De Sousa;
                Henrique-DeSousa;
                Email not public;
                test;
                Portugal;
                No Twitter available;
                ISCTE.
                """,
                """
                https://github.com/glrss-iscte
                https://avatars.githubusercontent.com/u/91333400?v=4
                Gonçalo Rodrigues ;
                glrss-iscte;
                Email not public;
                No bio available;
                Lissabona;
                No Twitter available;
                No company.
                """);
        Assertions.assertEquals(expected, gitManager.userInfo());
    }

    @Test
    void userRepositories() throws IOException {
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("rfgoo-iscte", Arrays.asList("Snake", "Space_invaders", "test_repo", "tic_tac_toe", "wheatherApp.py"));
        expected.put("trvas", List.of("es-testes"));
        expected.put("Henrique-DeSousa", Arrays.asList("ES-2021-ETC1-99", "Engineering-Practices-for-Building-Quality-Software", "test_repo"));
        expected.put("glrss-iscte", new ArrayList<>());

        Assertions.assertEquals(expected, gitManager.userRepositories());
    }

    @Test
    void numberOfRepositoriesOwned() throws IOException {
        List<String> expected = Arrays.asList(
                """
                
                rfgoo-iscte
                Number of Public Repositories: 5
                No visible Private Repositories. 0""",
                """
                
                trvas
                Number of Public Repositories: 1
                No visible Private Repositories. 0""",
                """
                
                Henrique-DeSousa
                Number of Public Repositories: 3
                Number of Private Repositories: 4""",
                """
                
                glrss-iscte
                No Public Repositories. 0
                No visible Private Repositories. 0""");
        Assertions.assertEquals(expected, gitManager.numberOfRepositoriesOwned());
    }

    @Test
    void getBranchesInRepository() throws Exception {
        List<String> expected = Arrays.asList("main", "master");
        Assertions.assertEquals(expected, gitManager.getBranchesInRepository("test_repo"));
    }

    @Test
    void testGetReadMe() throws IOException {
        Assertions.assertNotNull(gitManager.getReadMe("test_repo"));
    }

    // returns null. check why
    @Test
    void getFiles() throws Exception {
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("main", Arrays.asList("README.md", "kekwtest"));
        expected.put("master", Arrays.asList(".idea", "README.md", "calc.py", "calc2.0.py"));

        Assertions.assertEquals(expected, gitManager.getFiles("test_repo"));
    }

    @Test
    void readFileContent() throws IOException {
        Assertions.assertNotNull(gitManager.readFileContent("test_repo", "README.md", "059178ff832ae4b5372cd2ffa5d0a44ac1644d4d"));
    }

    @Test
    void getCommitDataFromRoot() throws IOException {
        /*
        String rawData = """
                [GitManager$CommitsDataGit@79c97cb, GitManager$CommitsDataGit@2d9caaeb, GitManager$CommitsDataGit@42a15bdc, GitManager$CommitsDataGit@44a59da3]
                [GitManager$CommitsDataGit@79c97cb, GitManager$CommitsDataGit@2d9caaeb, GitManager$CommitsDataGit@42a15bdc, GitManager$CommitsDataGit@44a59da3, GitManager$CommitsDataGit@6e01f9b0, GitManager$CommitsDataGit@2b9ed6da, GitManager$CommitsDataGit@6c61a903, GitManager$CommitsDataGit@658c5a19]""";
        */
        String expected = "[es.grupo9.GitManager$CommitsDataGit@5ffc5491, es.grupo9.GitManager$CommitsDataGit@705202d1, es.grupo9.GitManager$CommitsDataGit@3c443976, es.grupo9.GitManager$CommitsDataGit@3e58d65e]";
        Assertions.assertEquals(expected, gitManager.getCommitDataFromRoot("test_repo").toString());
    }

    // dá erro
    @Test
    void getCommitBranches() throws IOException {
        String expected = """
                GitManager$CommitUnpack@7d1cfb8b""";
        Assertions.assertEquals(expected, gitManager.getCommitBranches("Henrique-DeSousa", "main"));
    }

    @Test
    void numberOfCommitsInRoot() throws IOException {
        String expected = """
                The user: Henrique-DeSousa
                Has these commits: [Update kekwtest, Create kekwtest, Initial commit] in the Root: main
                With a total of: 3
                                
                Initial commit: Initial commit
                Date: Sun Nov 21 14:45:13 WET 2021
                User: Henrique-DeSousa
                                
                Latest commit: Update kekwtest
                Date: Sun Nov 21 16:14:31 WET 2021
                User: Henrique-DeSousa""";

        Assertions.assertEquals(expected, gitManager.numberOfCommitsInRoot("test_repo", "Henrique-DeSousa"));
    }

    @Test
    void getTag() throws IOException {
        String tag = """
               {test=Sun Nov 21 14:47:21 WET 2021, Tag2=Sun Nov 21 16:14:31 WET 2021}""";
        Assertions.assertEquals(tag, gitManager.getTag("test_repo").toString());
    }
}