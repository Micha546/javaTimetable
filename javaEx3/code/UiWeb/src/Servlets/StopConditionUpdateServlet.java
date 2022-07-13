package Servlets;

import InformationHolders.User;
import Utils.AlgorithmWrapper;
import Utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "stopConditionUpdateServlet", urlPatterns = {"/AlgorithmRun/stopConditionUpdateServlet"})
public class StopConditionUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        AlgorithmWrapper wrapper = currentUser.getAlgorithmOrNull(algorithmId);

        AlgorithmWrapper.AlgorithmStopCondition stopCondition =
                AlgorithmWrapper.AlgorithmStopCondition.valueOf(request.getParameter("type"));

        if(request.getParameter("action").equals("add"))
        {
            int value = Integer.parseInt(request.getParameter("value"));
            wrapper.addStopCondition(stopCondition, value);
        }
        else if(request.getParameter("action").equals("remove"))
        {
            wrapper.removeStopCondition(stopCondition);
        }
        else
        {
            response.setStatus(401);
            response.getOutputStream().println("bad action value");
        }
    }
}
