package Servlets;

import Consts.Constants;
import Managers.UserManager;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "loginServlet", urlPatterns = {"/LoginPage/loginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userNameFromForm = request.getParameter(Constants.USERNAME);

        if(session == null || SessionUtils.getUserNameFromSession(session) == null)
        {
            session = session == null ? request.getSession(true) : session;

            if(userNameFromForm == null || userNameFromForm.isEmpty())
            {
                response.setStatus(401);
                response.getOutputStream().println("bad username");
            }
            else
            {
                boolean addedUser = userManager.tryAddingUser(userNameFromForm);

                if(addedUser)
                {
                    SessionUtils.setUserNameForSession(session, request.getParameter(Constants.USERNAME));
                    response.getOutputStream().println("/MainPage/Main.html");
                }
                else
                {
                    response.setStatus(401);
                    response.getOutputStream().println("username not available");
                }
            }
        }
        else
        {
            response.getOutputStream().println("/MainPage/Main.html");
        }
    }
}
