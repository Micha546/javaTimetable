package Main.HelperClasses.TableViewClasses;

import Main.Engine.Evolution.TimeTableSolution.DTO.DTOSubject;

public class SubjectView {

    private final Integer ID;
    private final String name;

    public SubjectView(DTOSubject dtoSubject)
    {
        this.ID = dtoSubject.getId();
        this.name = dtoSubject.getName();
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
