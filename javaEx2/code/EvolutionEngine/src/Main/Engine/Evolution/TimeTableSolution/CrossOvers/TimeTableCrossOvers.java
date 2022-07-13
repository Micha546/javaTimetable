package Main.Engine.Evolution.TimeTableSolution.CrossOvers;

import Main.Engine.Evolution.CrossOver;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.SolutionFactory;
import Main.Engine.Evolution.TimeTableSolution.Classes.Teacher;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableFactory;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableTuple;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;

import java.util.*;
import java.util.stream.Collectors;

public enum TimeTableCrossOvers implements CrossOver {
    DayTimeOriented{
        @Override
        public List<TimeTable> singleCrossOver(TimeTable parent1, TimeTable parent2,
                                               TimeTableFactory factory, Integer cutSize, Object... args)
        {
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {}, args);
            List<TimeTable> offSprings = new ArrayList<>();

            List<List<TimeTableTuple>> parent1TuplesList = new ArrayList<>();
            List<List<TimeTableTuple>> parent2TuplesList = new ArrayList<>();

            for(int i = 1; i <= parent1.getDaysLimit(); ++i)
            {
                for(int j = 1; j <= parent1.getHoursLimit(); ++j)
                {
                    parent1TuplesList.add(new ArrayList<>(parent1.getAllTuplesOfDayHour(i, j)));
                    parent2TuplesList.add(new ArrayList<>(parent2.getAllTuplesOfDayHour(i, j)));
                }
            }

            Set<TimeTableTuple> TuplesOfOffSpring1 = new HashSet<>();
            Set<TimeTableTuple> TuplesOfOffSpring2 = new HashSet<>();

            boolean switchSides = false;
            for(int i = 0; i < parent1.getDaysLimit() * parent1.getHoursLimit(); ++i)
            {
                TuplesOfOffSpring1.addAll(switchSides
                        ? copyTimeTableTuplesList(parent2TuplesList.get(i))
                        : copyTimeTableTuplesList(parent1TuplesList.get(i)));
                TuplesOfOffSpring2.addAll(switchSides
                        ? copyTimeTableTuplesList(parent1TuplesList.get(i))
                        : copyTimeTableTuplesList(parent2TuplesList.get(i)));
                if((i + 1) % cutSize == 0)
                    switchSides = !switchSides;
            }

            offSprings.add(factory.createTimeTable(TuplesOfOffSpring1, parent1.getDaysLimit() ,parent1.getHoursLimit()));
            offSprings.add(factory.createTimeTable(TuplesOfOffSpring2, parent1.getDaysLimit() ,parent1.getHoursLimit()));
            return offSprings;
        }

        private List<TimeTableTuple> copyTimeTableTuplesList(List<TimeTableTuple> listToCopy)
        {
            List<TimeTableTuple> lst = new ArrayList<>();

            listToCopy.forEach(tuple -> {
                lst.add(tuple.copy());
            });

            return lst;
        }
    },
    AspectOriented{
        @Override
        protected List<TimeTable> singleCrossOver(TimeTable parent1, TimeTable parent2,
                                                  TimeTableFactory factory, Integer cutSize, Object... args)
        {
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {String.class}, args);

            String orientation = (String) args[0];

            Map<Integer, List<TimeTableTuple>> idToParent1Tuples = new HashMap<>();
            Map<Integer, List<TimeTableTuple>> idToParent2Tuples = new HashMap<>();

            if(orientation.equals("CLASS"))
            {
                parent1.getTuples().forEach(tuple ->
                        idToParent1Tuples.getOrDefault(tuple.getGrade().getId(), new ArrayList<>()).add(tuple));
                parent2.getTuples().forEach(tuple ->
                        idToParent2Tuples.getOrDefault(tuple.getGrade().getId(), new ArrayList<>()).add(tuple));
            }
            else if(orientation.equals("TEACHER"))
            {
                parent1.getTuples().forEach(tuple ->
                        idToParent1Tuples.getOrDefault(tuple.getTeacher().getId(), new ArrayList<>()).add(tuple));
                parent2.getTuples().forEach(tuple ->
                        idToParent2Tuples.getOrDefault(tuple.getTeacher().getId(), new ArrayList<>()).add(tuple));
            }
            else
                throw new RuntimeException(this.name() + " expects to get CLASS or TEACHER as its argument, but got "
                        + orientation + " instead");

            idToParent1Tuples.values()
                    .forEach(lst -> lst.sort((tuple1, tuple2) -> {
                        if(!tuple1.getDay().equals(tuple2.getDay()))
                            return tuple1.getDay() - tuple2.getDay();
                        else
                            return tuple1.getHour() - tuple2.getHour();
                    }));
            idToParent2Tuples.values()
                    .forEach(lst -> lst.sort((tuple1, tuple2) -> {
                        if(!tuple1.getDay().equals(tuple2.getDay()))
                            return tuple1.getDay() - tuple2.getDay();
                        else
                            return tuple1.getHour() - tuple2.getHour();
                    }));

            Set<TimeTableTuple> offSpring1 = new HashSet<>();
            Set<TimeTableTuple> offSpring2 = new HashSet<>();

            addTuples(idToParent1Tuples.values(), false, offSpring1, offSpring2, cutSize);
            addTuples(idToParent2Tuples.values(), true, offSpring1, offSpring2, cutSize);

            List<TimeTable> offSprings = new ArrayList<>();
            offSprings.add(factory.createTimeTable(offSpring1, parent1.getDaysLimit(), parent2.getHoursLimit()));
            offSprings.add(factory.createTimeTable(offSpring2, parent1.getDaysLimit(), parent2.getHoursLimit()));

            return offSprings;
        }

        private void addTuples(Collection<List<TimeTableTuple>> lists, boolean switchSides,
                               Set<TimeTableTuple> offSpring1, Set<TimeTableTuple> offSpring2,
                               int cutSize)
        {
            int loopCounter = 0;
            for(List<TimeTableTuple> lst : lists)
            {
                for(TimeTableTuple tuple : lst)
                {
                    if(switchSides)
                        offSpring2.add(tuple.copy());
                    else
                        offSpring1.add(tuple.copy());

                    loopCounter += 1;
                    if(loopCounter % cutSize == 0)
                        switchSides = !switchSides;
                }
            }
        }
    };

    @Override
    public List<Solution> doCrossOver(List<Solution> parentPopulation, Integer offSpringPopulationSize,
                                      SolutionFactory factory, Integer cutSize, Object... args)
    {
        if(parentPopulation.stream().anyMatch(solution -> !(solution instanceof TimeTable)))
            throw new RuntimeException(this.name() + " expects to get a List of TimeTables");
        if(!(factory instanceof TimeTableFactory))
            throw new RuntimeException(this.name() + " expects to get a TimeTableFactory but got "
                    + factory.getClass().getName());

        Random rand = new Random();
        TimeTableFactory timeTableFactory = (TimeTableFactory) factory;
        List<TimeTable> timeTableParents = TimeTableUtils.castListToTimeTable(parentPopulation);
        List<Solution> offsprings = new ArrayList<>();

        while(offsprings.size() < offSpringPopulationSize)
        {
            TimeTable parent1 = timeTableParents.get(rand.nextInt(timeTableParents.size()));
            TimeTable parent2 = timeTableParents.get(rand.nextInt(timeTableParents.size()));

            offsprings.addAll(this.singleCrossOver(parent1, parent2, timeTableFactory, cutSize, args));
        }

        if(offsprings.size() > offSpringPopulationSize)
            offsprings.remove(offsprings.size() - 1);

        return offsprings;
    }

    protected abstract List<TimeTable> singleCrossOver(TimeTable parent1, TimeTable parent2,
                                                       TimeTableFactory factory, Integer cutSize, Object... args);

}
