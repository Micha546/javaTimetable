package Servlets;

import InformationHolders.FileInfo;
import Main.Engine.Xml.AutoGenerated.ETTDescriptor;
import Main.Engine.Xml.XmlReader;
import Managers.UploadedFilesManager;
import Utils.ServletUtils;
import Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet(name = "fileUploadServlet", urlPatterns = {"/MainPage/fileServlet"})
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        InputStream fileContent = request.getPart("file").getInputStream();
        String fileName = request.getPart("file").getSubmittedFileName();

        ETTDescriptor descriptor = null;
        try{
            descriptor = XmlReader.createETTDescriptorFromInputStream(fileContent, fileName);
        }
        catch (Exception ex)
        {
            response.setStatus(401);
            if(ex.getMessage() == null)
                response.getOutputStream().println("Could not read file because no file was selected");
            else
                response.getOutputStream().println("Could not read file because " + ex.getMessage());
            return;
        }

        HttpSession session = request.getSession(false);

        UploadedFilesManager uploadedFilesManager = ServletUtils.getUploadedFilesManager(getServletContext());

        uploadedFilesManager.addFileInfo(new FileInfo(
                SessionUtils.getUserNameFromSession(session), descriptor
                ));

        response.getOutputStream().println("file uploaded successfully");
    }
}
