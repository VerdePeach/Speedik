package com.in726.app.unit.security;

import com.in726.app.database.service.UserService;
import com.in726.app.model.User;
import com.in726.app.security.PasswordChanger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PasswordChangerTest {

    @Mock
    UserService userService = new UserService();

    @InjectMocks
    PasswordChanger passwordChanger = new PasswordChanger();

    @Test
    public void changePasswordPositive() throws Exception {
        var expectedResult = "aa54def9e0bb11c1ebbfc97a9ee63af9e95c4fdf1d032b1ddcc0f21661f748651d2b2b8fb94e9ae041780554db29815daa1c0fe991ddae54eff0c4c28cd9d20c";
        passwordChanger.setOldPassword("old");
        passwordChanger.setNewPassword("new");

        var user = new User();
        user.setPassword("a6d96fa05fb243a13082a21195ebd126fc42f2f158cf5e0042e4f956b60d7edb4b363bcd41ce433b33b7024303d34f0ecaad5071841bd3ada3e99d68f00b89b2");

        passwordChanger.changePassword(user);
        Assert.assertEquals(expectedResult, user.getPassword());
    }

    @Test(expected = Exception.class)
    public void changePasswordNegative() throws Exception {

        passwordChanger.setOldPassword("old");
        passwordChanger.setNewPassword("new");

        var user = new User();
        user.setPassword("world");

        passwordChanger.changePassword(user);
    }

    @Test(expected = Exception.class)
    public void changePasswordNullPointerNegative() throws Exception {
        passwordChanger.changePassword(null);
    }

    @Test
    public void userServiceNoyNullPositive() {
        Assert.assertNotNull(passwordChanger.getUserService());
    }
}
