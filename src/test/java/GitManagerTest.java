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
    void testMain() {

    }

    @Test
    void login() throws IOException {
       // GitManager.login();
    }

    @Test
    void testConnect() throws IOException {
       // GitManager.connect();
    }

    @Test
    void testUserInfo() throws IOException {
        String info =
"""
https://avatars.githubusercontent.com/u/73469590?v=4
Henrique Vieira De Sousa;
Henrique-DeSousa;
Your email isn't public;
test;
Portugal;
Not available;
ISCTE.
""";
      // Assert.assertEquals(info , GitManager.userInfo());
    }

    @Test
    void repositoriesUnderUser() throws IOException {
        //Assertions.assertNotEquals(null, GitManager.repositoriesUnderUser());
    }

    @Test
    void getReadMe() throws IOException {
       // Assertions.assertNotEquals(null, GitManager.getReadMe("test_repo"));
    }

    @Test
    void getFileContentInMain() throws IOException {
        //Assertions.assertNotEquals(null, GitManager("test_repo", "calc2.0.py", "57f8f3f938071e99da9b6323d07209805ad9813f"));
    }

    @Test
    void getBranches() throws Exception {
     //   Assertions.assertNotEquals(null, GitManager.getBranches("test_repo"));
    }

    @Test
    void getCommitData() throws IOException {
     //   Assertions.assertNotEquals(null, GitManager.getCommitData("test_repo"));
    }
}