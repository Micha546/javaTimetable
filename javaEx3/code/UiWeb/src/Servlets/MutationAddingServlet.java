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

@WebServlet(name = "mutationAddingServlet", urlPatterns = {"/AlgorithmView/mutationAddingServlet"})
public class MutationAddingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int algorithmId = Integer.parseInt(request.getParameter("id"));
        User currentUser = ServletUtils.getUserManager(getServletContext()).getCurrentUser(request.getSession(false));
        AlgorithmWrapper wrapper = currentUser.getAlgorithmOrNull(algorithmId);

        DTOTimeTableMutation newMutation = convertRequestToDtoMutation(request);

        if(newMutation != null)
        {
            wrapper.addMutation(newMutation);
        }
        else
        {
            response.setStatus(401);
            response.getOutputStream().println("type sent was not Flipping or Sizer");
            response.getOutputStream().flush();
        }
    }

    private DTOTimeTableMutation convertRequestToDtoMutation(HttpServletRequest request)
    {
        String type = request.getParameter("type");
        int chance = Integer.parseInt(request.getParameter("chance"));

        DTOTimeTableMutation newMutation;

        List<Object> args = new ArrayList<>();

        if(type.equals("Flipping"))
        {
            args.add(Integer.parseInt(request.getParameter("maxTuples")));
            args.add(getArgsComponent(request.getParameter("component")));

            newMutation = new DTOTimeTableMutation(
                    TimeTableMutations.valueOf(request.getParameter("component")),
                    chance,
                    args
            );
        }
        else if(type.equals("Sizer"))
        {
            args.add(Integer.parseInt(request.getParameter("totalTuples")));

            newMutation = new DTOTimeTableMutation(
                    TimeTableMutations.valueOf(request.getParameter("type")),
                    chance,
                    args
            );
        }
        else
        {
            newMutation = null;
        }

        return newMutation;
    }

    private char getArgsComponent(String componentFromJSon)
    {
        String str = componentFromJSon.substring("Flipping".length());
        char component = str.charAt(0);
        component = component == 'G' ? 'C' : component;
        return component;
    }
}
