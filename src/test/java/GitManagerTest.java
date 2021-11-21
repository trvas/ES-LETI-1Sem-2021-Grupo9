import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GitManagerTest {

    GitManager gitManager;

    @BeforeEach
    void setUp() throws IOException {
        gitManager = new GitManager("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", "Henrique-DeSousa", "test_repo" );
    }

    @AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void testMain() {

    }

    @org.junit.jupiter.api.Test
    void login() throws IOException {
        GitManager.login();
    }

    @org.junit.jupiter.api.Test
    void testConnect() throws IOException {
        GitManager.connect();
    }

    @org.junit.jupiter.api.Test
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
       Assert.assertEquals(info , GitManager.userInfo());
    }

    @org.junit.jupiter.api.Test
    void repositoriesUnderUser() throws IOException {
        Assertions.assertNotEquals(null, GitManager.repositoriesUnderUser());
    }

    @org.junit.jupiter.api.Test
    void getReadMe() throws IOException {
        Assertions.assertNotEquals(null, GitManager.getReadMe("test_repo"));
    }

    @org.junit.jupiter.api.Test
    void getFileContentInMain() throws IOException {
        Assertions.assertNotEquals(null, GitManager.getFileContentInMain("test_repo", "calc2.0.py", "57f8f3f938071e99da9b6323d07209805ad9813f"));
    }

    @org.junit.jupiter.api.Test
    void getBranches() throws Exception {
        Assertions.assertNotEquals(null, GitManager.getBranches("test_repo"));
    }

    @org.junit.jupiter.api.Test
    void getCommitData() throws IOException {
        Assertions.assertNotEquals(null, GitManager.getCommitData("test_repo" , "Henrique-DeSousa"));
    }
}