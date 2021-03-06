package es.grupo9;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

class GitManagerTest {

    GitManager gitManager;

    @BeforeEach
    void setUp() throws Exception {
        gitManager = new GitManager("AUTH", "Henrique-DeSousa", "test_repo");
        gitManager.getCollaborators();
    }

    @Test
    void setGithubBranchName() {
        gitManager.setGithubBranchName("main");
        Assertions.assertEquals("main", gitManager.getGithubBranchName());
    }

    @Test
    void setCommitReference() {
        gitManager.setCommitReference("dbb04cbad190efce1176dbd3a9a0412a749fb56f");
        Assertions.assertEquals("dbb04cbad190efce1176dbd3a9a0412a749fb56f", gitManager.getCommitReference());
    }

    @Test
    void setGithubFileName() {
        gitManager.setGithubFileName("README.md");
        Assertions.assertEquals("README.md", gitManager.getGithubFileName());
    }

    @Test
    void getCollaborators() throws IOException {
        String expected = """
                         [rfgoo-iscte, trvas, Henrique-DeSousa, glrss-iscte]""";
        Assertions.assertEquals(expected, gitManager.getCollaborators().toString());
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
                Gon??alo Rodrigues ;
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
        Assertions.assertEquals(expected, gitManager.getBranchesInRepository());
    }

    @Test
    void testGetReadMe() throws IOException {
        Assertions.assertNotEquals(null, gitManager.getReadMe());
    }

    @Test
    void getFiles() throws Exception {
        gitManager.getBranchesInRepository();
        Map<String, List<String>> expected = new HashMap<>();
        expected.put("main", Arrays.asList("README.md", "kekwtest"));
        expected.put("master", Arrays.asList(".idea", "README.md", "calc.py", "calc2.0.py"));

        Assertions.assertEquals(expected, gitManager.getFiles());
    }

    @Test
    void readFileContent() throws IOException {
        Assertions.assertNotNull(gitManager.readFileContent( "README.md", "059178ff832ae4b5372cd2ffa5d0a44ac1644d4d"));
    }

    @Test
    void getCommitBranches() throws IOException {
        String expected = "Henrique-DeSousa";
        Assertions.assertEquals(expected, gitManager.getCommits("Henrique-DeSousa", "main").name);
    }

    @Test
    void getTag() throws Exception {
        Assertions.assertFalse(gitManager.getTag().isEmpty());
    }
}