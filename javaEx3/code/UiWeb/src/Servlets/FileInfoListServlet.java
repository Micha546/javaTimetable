package Servlets;

import InformationHolders.FileInfo;
import Managers.UploadedFilesManager;
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

@WebServlet(name = "fileInfoListServlet", urlPatterns = {"/MainPage/fileInfoListServlet"})
public class FileInfoListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            UploadedFilesManager uploadedFilesManager = ServletUtils.getUploadedFilesManager(getServletContext());
            List<FileInfo> fileInfoList = uploadedFilesManager.getFileInfoList();
            String json = gson.toJson(fileInfoList);
            out.println(json);
            out.flush();
        }
    }
}
