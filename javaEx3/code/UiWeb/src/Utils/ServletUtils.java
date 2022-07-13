package Utils;

import Consts.Constants;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;
import Managers.UploadedFilesManager;
import Managers.UserManager;
import com.sun.deploy.net.HttpRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    private static final Object userManagerLock = new Object();
    private static final Object uploadedFilesManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext)
    {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(Constants.USER_MANAGER_SINGLETON_NAME) == null) {
                servletContext.setAttribute(Constants.USER_MANAGER_SINGLETON_NAME, new UserManager());
            }
        }
        return (UserManager) servletContext.getAttribute(Constants.USER_MANAGER_SINGLETON_NAME);
    }

    public static UploadedFilesManager getUploadedFilesManager(ServletContext servletContext)
    {
        synchronized (uploadedFilesManagerLock) {
            if (servletContext.getAttribute(Constants.UPLOADED_FILES_MANAGER_SINGLETON_NAME) == null) {
                servletContext.setAttribute(Constants.UPLOADED_FILES_MANAGER_SINGLETON_NAME, new UploadedFilesManager());
            }
        }
        return (UploadedFilesManager) servletContext.getAttribute(Constants.UPLOADED_FILES_MANAGER_SINGLETON_NAME);
    }
}
