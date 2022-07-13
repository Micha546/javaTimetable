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

@WebServlet(name = "changeStatusServlet", urlPatterns = {"/AlgorithmRun/changeStatusServlet"})
public class ChangeStatusServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        AlgorithmWrapper wrapper = currentUser.getAlgorithmOrNull(algorithmId);

        wrapper.changeStatus(AlgorithmWrapper.Status.valueOf(request.getParameter("status")));

        response.getOutputStream().print(wrapper.getStatus().name());
    }
}
