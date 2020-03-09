package ru.job4j.servlets.crud.presentation.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import ru.job4j.servlets.crud.logic.user.Validate;
import ru.job4j.servlets.crud.logic.user.ValidateStub;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.dom.*", "javax.management.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest({MainActionController.class})
public class MainActionControllerTest {
    private static final String FIELD_NAME = "LOGIC";
    private Validate validate;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    @Before
    public void setUp() {
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        validate = new ValidateStub();
        Whitebox.setInternalState(MainActionController.class, FIELD_NAME, validate);
    }

    @Test
    public void whenAddUserThenStoreIt() throws IOException {
        setParametersToRequest(req, resp, "add", "Nickolas", "Chinese", null);
        String actual = validate.findAll().iterator().next().getLogin();
        String expected = "Chinese";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenDeleteUser() throws IOException {
        setParametersToRequest(req, resp, "add", "Nickolas", "Chinese", null);
        setParametersToRequest(req, resp, "add", "Nickolas", "Chinese", null);
        setParametersToRequest(req, resp, "delete", null, null, "1");
        int actual = validate.findAll().size();
        int expected = 1;
        assertThat(actual, is(expected));
    }

    @Test
    public void whenUpdateUser() throws IOException {
        setParametersToRequest(req, resp, "add", "Nickolas", "Chinese", null);
        setParametersToRequest(req, resp, "update", "Blade", "First", "1");
        String actual = validate.findAll().iterator().next().getName();
        String expected = "Blade";
        assertThat(actual, is(expected));
    }

    @Test
    public void whenFindAllReturnColl() throws IOException {
        setParametersToRequest(req, resp, "add", "Nickolas", "Chinese", null);
        setParametersToRequest(req, resp, "add", "Blade", "First", null);
        setParametersToRequest(req, resp, "add", "Rambo", "First Blood", null);
        int actual = validate.findAll().size();
        int expected = 3;
        assertThat(actual, is(expected));
    }

    private void setParametersToRequest(HttpServletRequest req, HttpServletResponse resp,
                                        String action, String name, String login, String id) throws IOException {
        when(req.getParameter("action")).thenReturn(action);
        when(req.getParameter("id")).thenReturn(id);
        when(req.getParameter("name")).thenReturn(name);
        when(req.getParameter("login")).thenReturn(login);
        new MainActionController().doPost(req, resp);
    }
}