package Servlets;

import InformationHolders.User;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableMutation;
import Main.Engine.Evolution.TimeTableSolution.Mutations.TimeTableMutations;
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

@WebServlet(name = "mutationDeletingServlet", urlPatterns = {"/AlgorithmView/mutationDeletingServlet"})
public class MutationDeletingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TimeTableMutations mutation = TimeTableMutations.valueOf(request.getParameter("type"));
        int chance = Integer.parseInt(request.getParameter("chance"));
        List<Object> args = new ArrayList<>();

        switch (mutation)
        {
            case FlippingSubject:
            case FlippingTeacher:
            case FlippingGrade:
            case FlippingHour:
            case FlippingDay:
                int maxTuples = Integer.parseInt(request.getParameterValues("arguments[]")[0]);
                char component = request.getParameterValues("arguments[]")[1].charAt(0);
                args.add(maxTuples);
                args.add(component);
                break;
            case Sizer:
                int totalTuples = Integer.parseInt(request.getParameterValues("arguments[]")[0]);
                args.add(totalTuples);
                break;
        }

        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        AlgorithmWrapper wrapper = currentUser.getAlgorithmOrNull(algorithmId);

        wrapper.deleteMutation(new DTOTimeTableMutation(
                mutation, chance, args
        ));
    }
}
