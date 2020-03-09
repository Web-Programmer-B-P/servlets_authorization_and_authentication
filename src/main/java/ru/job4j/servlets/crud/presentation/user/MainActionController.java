package ru.job4j.servlets.crud.presentation.user;

import ru.job4j.servlets.crud.logic.user.Validate;
import ru.job4j.servlets.crud.logic.user.ValidateUserService;
import ru.job4j.servlets.crud.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;

@WebServlet("/list")
public class MainActionController extends HttpServlet {
    private static final String ID_PARAMETER = "id";
    private static final Validate LOGIC = ValidateUserService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession();
        String action = req.getParameter("action");
        String idParam = req.getParameter(ID_PARAMETER);
        String imageName = req.getParameter("image");
        int id = 0;
        if (action.equals("add")) {
            User user = new User();
            LOGIC.add(fill(user, req));
        }

        if (idParam != null) {
            if (action.equals("update")) {
                id = Integer.parseInt(req.getParameter(ID_PARAMETER));
                User userFromStorage = LOGIC.findById(id);
                User userAfterModify = fill(userFromStorage, req);
                if (httpSession != null) {
                    User currentSessionUser = (User) httpSession.getAttribute("user");
                    if (currentSessionUser.getId() == userFromStorage.getId()) {
                        userAfterModify.setRoleId(currentSessionUser.getRoleId());
                        httpSession.setAttribute("user", userAfterModify);
                    }
                    LOGIC.update(userAfterModify);
                }
            }

            if (action.equals("delete")) {
                if (imageName != null) {
                    deleteImageBeforeDeleteUser(imageName);
                }
                id = Integer.parseInt(req.getParameter(ID_PARAMETER));
                LOGIC.delete(id);
            }
        }
    }

    private void deleteImageBeforeDeleteUser(String fileName) {
        File fileToTrash = new File(UploadFileController.PATH_TO_SAVE_IMAGES + fileName);
        if (fileToTrash.exists()) {
            fileToTrash.delete();
        }
    }

    private User fill(User user, HttpServletRequest req) {
        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String email = req.getParameter("email");
        String image = req.getParameter("image");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String country = req.getParameter("country");
        String city = req.getParameter("city");
        if (name != null) {
            user.setName(name);
        }
        if (login != null) {
            user.setLogin(login);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (image != null) {
            if (!image.equals(user.getPhotoId())) {
                deleteImageBeforeDeleteUser(user.getPhotoId());
            }
            user.setPhotoId(image);
        }
        if (password != null) {
            user.setPassword(password);
        }
        if (role != null) {
            user.setRoleId(Integer.parseInt(role));
        }
        if (country != null) {
            user.setCountry(country);
        }
        if (city != null) {
            user.setCity(city);
        }
        user.setCreateDate(System.currentTimeMillis());
        return user;
    }
}