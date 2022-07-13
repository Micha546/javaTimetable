package Utils;

import Consts.Constants;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUserNameFromSession(HttpSession session)
    {
        if(session == null)
            throw new RuntimeException("Session provided is null");

        return (String) session.getAttribute(Constants.USERNAME);
    }

    public static void setUserNameForSession(HttpSession session, String username)
    {
        if(session == null)
            throw new RuntimeException("Session provided is null");

        session.setAttribute(Constants.USERNAME, username);
    }
}
