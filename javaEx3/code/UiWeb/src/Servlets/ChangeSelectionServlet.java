package Servlets;

import InformationHolders.User;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableSelection;
import Main.Engine.Evolution.TimeTableSolution.Selections.TimeTableSelections;
import Utils.AlgorithmWrapper;
import Utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "changeSelectionServlet", urlPatterns = {"/AlgorithmView/changeSelectionServlet"})
public class ChangeSelectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        AlgorithmWrapper wrapper = currentUser.getAlgorithmOrNull(algorithmId);

        TimeTableSelections selection = TimeTableSelections.valueOf(request.getParameter("type"));
        int elitism = Integer.parseInt(request.getParameter("elitism"));
        List<Object> args = new ArrayList<>();

        if(selection == TimeTableSelections.Truncation)
            args.add(Integer.parseInt(request.getParameterValues("arguments[]")[0]));
        else if (selection == TimeTableSelections.Tournament)
            args.add(Double.parseDouble(request.getParameterValues("arguments[]")[0]));

        wrapper.changeSelection(new DTOTimeTableSelection(selection, elitism, args));
    }
}
