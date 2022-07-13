package Main.Engine.Evolution.TimeTableSolution.DTO;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.Classes.Subject;

public class DTOSubject implements InformationCarrier {
    private final Subject subject;

    public DTOSubject(Subject subject) {
        this.subject = subject;
    }

    public final Integer getId()
    {
        return subject.getId();
    }

    public final String getName()
    {
        return subject.getName();
    }


}
