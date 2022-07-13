package Main.Engine.Evolution.TimeTableSolution.Classes;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.SolutionFactory;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class TimeTableFactory implements SolutionFactory {

    @Override
    public Solution createRandom(AlgorithmManager manager, Object... args)
    {
        TimeTableUtils.checkArgs(this.getClass().getName(), new Class<?>[] {}, args);
        TimeTableUtils.assertManagerIsTimeTableManager(this.getClass().getName(), manager);

        TimeTableManager timeTableManager = (TimeTableManager) manager;

        Random rand = new Random();
        int tuplesNumber = timeTableManager.getAllGrades().stream()
                .mapToInt(grade -> grade.getSubjectIdToWeeklyHours().values().stream().mapToInt(i -> i).sum())
                .sum();

        int offset = rand.nextInt(5);
        if(rand.nextBoolean())
            offset *= -1;

        tuplesNumber += offset;

        if(tuplesNumber > timeTableManager.getDays() * timeTableManager.getHours())
            tuplesNumber = timeTableManager.getDays() * timeTableManager.getHours();
        else if (tuplesNumber < timeTableManager.getDays())
            tuplesNumber = timeTableManager.getDays();

        return createRandomTimeTable(tuplesNumber, timeTableManager);
    }

    @Override
    public Solution copy(Solution solution) {
        TimeTableUtils.assertSolutionIsTimeTable(solution, this.getClass().getName());

        TimeTable timeTable = (TimeTable) solution;
        Set<TimeTableTuple> copiedTuples = timeTable.getTuples().stream()
                .map(tuple -> new TimeTableTuple(
                        tuple.getDay(),
                        tuple.getHour(),
                        tuple.getTeacher(),
                        tuple.getGrade(),
                        tuple.getSubject()))
                .collect(Collectors.toSet());
        return new TimeTable(timeTable.getDaysLimit(), timeTable.getHoursLimit(), copiedTuples);
    }

    private TimeTable createRandomTimeTable(int numberOfTuplesToCreate, TimeTableManager manager)
    {
        Set<TimeTableTuple> newTuplesSet = new HashSet<>();
        for(int i = 0; i < numberOfTuplesToCreate; ++i)
            newTuplesSet.add(createRandomTuple(manager));

        return new TimeTable(manager.getDays(), manager.getHours(), newTuplesSet);
    }

    public TimeTableTuple createRandomTuple(TimeTableManager manager)
    {
        return new TimeTableTuple(
                manager.getRandomDay(),
                manager.getRandomHour(),
                manager.getRandomTeacher(),
                manager.getRandomGrade(),
                manager.getRandomSubject()
        );
    }

    public TimeTable createTimeTable(Set<TimeTableTuple> tuples, int days, int hours)
    {
        return new TimeTable(days, hours, tuples);
    }
}
