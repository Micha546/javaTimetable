package Servlets;

import InformationHolders.User;
import Main.Engine.Evolution.TimeTableSolution.CrossOvers.TimeTableCrossOvers;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableCrossOver;
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

@WebServlet(name = "changeCrossoverServlet", urlPatterns = {"/AlgorithmView/changeCrossoverServlet"})
public class ChangeCrossoverServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        AlgorithmWrapper wrapper = currentUser.getAlgorithmOrNull(algorithmId);

        TimeTableCrossOvers crossover = TimeTableCrossOvers.valueOf(request.getParameter("type"));
        int cutSize = Integer.parseInt(request.getParameter("cutSize"));
        List<Object> args = new ArrayList<>();

        if(crossover == TimeTableCrossOvers.AspectOriented)
            args.add(request.getParameterValues("arguments[]")[0]);

        wrapper.changeCrossover(new DTOTimeTableCrossOver(crossover, cutSize, args));
    }
}
