package Servlets;

import InformationHolders.User;
import Managers.UserManager;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "userNameListServlet", urlPatterns = {"/MainPage/userListServlet"})
public class UserNameListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            List<String> usersList = userManager.getUsers().stream().map(User::getName).collect(Collectors.toList());
            String json = gson.toJson(usersList);
            out.println(json);
            out.flush();
        }
    }
}
