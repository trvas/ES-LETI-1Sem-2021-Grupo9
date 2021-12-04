import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

class GitManagerTest {

    GitManager gitManager;

    @BeforeEach
    void setUp() throws IOException {
        gitManager = new GitManager("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", "Henrique-DeSousa", "test_repo");
    }

    @Test
    void main() {
    }

    @Test
    void setGithubBranchName() {
        gitManager.setGithubBranchName("master");
        Assertions.assertEquals("master", gitManager.getGithubBranchName());
    }

    @Test
    void setCommitReference() {
        gitManager.setCommitReference("059178ff832ae4b5372cd2ffa5d0a44ac1644d4d");
        Assertions.assertEquals("059178ff832ae4b5372cd2ffa5d0a44ac1644d4d", gitManager.getCommitReference());
    }

    @Test
    void setGithubFileName() {
        gitManager.setGithubFileName("README.md");
        Assertions.assertEquals("README.md", gitManager.getGithubFileName());
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
        gitManager.getCollaborators("test_repo");
        List<String> nul = gitManager.userInfo();

        Assertions.assertEquals(nul, gitManager.userInfo());
    }

    @Test
    void userRepositories() throws IOException {
        Map<String, List<String>> urp = gitManager.userRepositories();

        Assertions.assertEquals(urp, gitManager.userRepositories());
    }

    @Test
    void numberOfRepositoriesOwned() throws IOException {
        @NotNull List<String> nro = gitManager.numberOfRepositoriesOwned();
        Assertions.assertEquals(nro, gitManager.numberOfRepositoriesOwned());
    }

    @Test
    void getBranchesInRepository() throws Exception {
        @NotNull List<String> bra = gitManager.getBranchesInRepository("test_repo");
        Assertions.assertEquals(bra, gitManager.getBranchesInRepository("test_repo"));
    }

    @Test
    void testGetReadMe() throws IOException {
        Assertions.assertNotEquals(null, gitManager.getReadMe("test_repo"));
    }

    @Test
    void getFiles() throws Exception {
        @NotNull Map<String, List<String>> files = gitManager.getFiles("test_repo");
        Assertions.assertEquals(files, gitManager.getFiles("test_repo"));
    }

    @Test
    void readFileContent() throws IOException {
        Assertions.assertNotEquals(null, gitManager.readFileContent("test_repo", "README.md", "059178ff832ae4b5372cd2ffa5d0a44ac1644d4d"));
    }

    @Test
    void getCommitDataFromRoot() throws IOException {
        @NotNull List<GitManager.CommitsDataGit> rawData = gitManager.getCommitDataFromRoot("test_repo");
        Assertions.assertEquals(rawData, gitManager.getCommitDataFromRoot("test_repo"));
    }

    @Test
    void getCommitBranches() throws IOException {
        String rawCom = "GitManager$CommitUnpack@3a320ade";
        Assertions.assertNotEquals(rawCom, gitManager.getCommitFromBranches("Henrique-DeSousa", "main"));
    }

    @Test
    void commitsInRoot() throws IOException {
        String bruh = """
        The user: Henrique-DeSousa
        Has these commits: [Update kekwtest, Create kekwtest, Initial commit] in the Root: main
        With a total of: 3

        Initial commit: Initial commit
        Date: Sun Nov 21 14:45:13 WET 2021
        User: Henrique-DeSousa

        Latest commit: Update kekwtest
        Date: Sun Nov 21 16:14:31 WET 2021
        User: Henrique-DeSousa
        """;

        Assertions.assertEquals(bruh, gitManager.commitsInRoot("test_repo", "Henrique-DeSousa"));

    }

    @Test
    void getTag() throws IOException {
        String tag = gitManager.getTag("test_repo");
        Assertions.assertEquals(tag, gitManager.getTag("test_repo"));
    }
}