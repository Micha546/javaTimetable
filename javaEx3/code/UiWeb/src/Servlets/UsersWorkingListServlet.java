package Servlets;

import InformationHolders.AlgorithmInformationWebVersion;
import InformationHolders.User;
import InformationHolders.UserWorkingListJson;
import Managers.UserManager;
import Utils.AlgorithmWrapper;
import Utils.ServletUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "usersWorkingListServlet", urlPatterns = {"/AlgorithmRun/usersWorkingListServlet"})
public class UsersWorkingListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        Set<User> usersWorking = ServletUtils.getUserManager(getServletContext()).getUsers().stream()
                .filter(user -> !user.equals(currentUser) && user.isWorkingOnAlgorithm(algorithmId))
                .collect(Collectors.toSet());

        List<UserWorkingListJson> jsonUsers = new ArrayList<>();

        for(User user : usersWorking)
        {
            AlgorithmInformationWebVersion info = user.getAlgorithmOrNull(algorithmId).getAlgorithmInformation();
            jsonUsers.add(new UserWorkingListJson(
                    user.getName(), info.getFitness(), info.getGeneration()
            ));
        }

        jsonUsers.sort(Comparator.comparing(UserWorkingListJson::getUsername));

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(jsonUsers);
            out.println(json);
            out.flush();
        }
    }
}
