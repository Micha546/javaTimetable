package Main.HelperClasses;

import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.TimeTableSolution.DTO.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralAlgorithmInformation implements InformationCarrier {

    private final DTOTimeTableManager dtoManager;
    private final Integer generationNumber;
    private final Integer populationSize;

    public GeneralAlgorithmInformation(
            InformationCarrier dtoManager,
            Integer generationNumber,
            Integer populationSize)
    {
        if(!(dtoManager instanceof DTOTimeTableManager))
            throw new RuntimeException("Expected to get DTOTimeTableManager in " + this.getClass().getName());

        this.dtoManager = (DTOTimeTableManager) dtoManager;
        this.generationNumber = generationNumber;
        this.populationSize = populationSize;
    }

    public Integer getGenerationNumber() {
        return generationNumber;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public List<DTOSubject> getAllDtoSubjects()
    {
        return dtoManager.getSubjects();
    }

    public List<DTOTeacher> getAllDtoTeachers()
    {
        return dtoManager.getTeachers();
    }

    public List<DTOGrade> getAllDtoGrades()
    {
        return dtoManager.getGrades();
    }

    public List<DTOTimeTableRules> getAllDtoRules()
    {
        return dtoManager.getRules().stream()
                .sorted(Comparator.comparing(DTOTimeTableRules::getRuleName))
                .collect(Collectors.toList());
    }

    public List<DTOTimeTableMutation> getAllDtoMutations()
    {
        return dtoManager.getMutations();
    }

    public DTOTimeTableCrossOver getDtoCrossOver()
    {
        return dtoManager.getCrossOver();
    }

    public DTOTimeTableSelection getDtoSelection()
    {
        return dtoManager.getSelection();
    }
}
