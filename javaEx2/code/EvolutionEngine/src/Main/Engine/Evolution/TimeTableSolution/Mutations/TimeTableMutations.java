package Main.Engine.Evolution.TimeTableSolution.Mutations;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.Mutation;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTable;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableTuple;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum TimeTableMutations implements Mutation {
    FlippingSubject{

    },
    FlippingTeacher{

    },
    FlippingGrade{

    },
    FlippingHour{

    },
    FlippingDay{

    },
    Sizer{
        @Override
        public void mutate(Solution offSpring, TimeTableManager manager, Object... args)
        {
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {Integer.class}, args);
            TimeTableUtils.assertSolutionIsTimeTable(offSpring, this.name());

            TimeTable timeTable = (TimeTable) offSpring;
            Integer totalTuples = (Integer) args[0];

            if(totalTuples == 0)
                return;
            else if(totalTuples < 0)
            {
                int tuplesToDelete = new Random().nextInt(Math.abs(totalTuples)) + 1;

                final int D = timeTable.getDaysLimit();

                if(timeTable.getTuples().size() < D)
                    addTuples(timeTable, manager, D);
                else
                    subtractTuples(timeTable, manager, Math.max(timeTable.getTuples().size() - tuplesToDelete, D));
            }
            else
            {
                int tuplesToAdd = new Random().nextInt(totalTuples) + 1;

                final int DH = timeTable.getDaysLimit() * timeTable.getHoursLimit();

                if(timeTable.getTuples().size() > DH)
                    subtractTuples(timeTable, manager, DH);
                else
                    addTuples(timeTable, manager, Math.min(timeTable.getTuples().size() + tuplesToAdd, DH));
            }
        }

        private void addTuples(TimeTable timeTable, TimeTableManager manager, int sizeToReach)
        {
            if(timeTable.getTuples().size() > sizeToReach)
                throw new RuntimeException("Something went wrong in the Sizer algorithm, addTuples");

            while(timeTable.getTuples().size() < sizeToReach)
            {
                timeTable.getTuples().add(new TimeTableTuple(
                        manager.getRandomDay(),
                        manager.getRandomHour(),
                        manager.getRandomTeacher(),
                        manager.getRandomGrade(),
                        manager.getRandomSubject()
                ));
            }
        }

        private void subtractTuples(TimeTable timeTable, TimeTableManager manager, int sizeToReach)
        {
            if(timeTable.getTuples().size() < sizeToReach)
                throw new RuntimeException("Something went wrong in the Sizer algorithm, subtractTuples");

            List<TimeTableTuple> tuplesList = new ArrayList<>(timeTable.getTuples());
            Collections.shuffle(tuplesList);
            int i = 0;

            while(timeTable.getTuples().size() < sizeToReach)
            {
                timeTable.getTuples().remove(tuplesList.get(i));
            }
        }
    };
    @Override
    public void doMutation(List<Solution> offSpringPopulation,
                           AlgorithmManager manager, Object... args)
    {
        throw new RuntimeException("Got to TimeTableMutations doMutation instead of the one in TimeTableMutationWrapper");

        // legacy code, the program is not supposed to come here at any moment.

        /*
        if(offSpringPopulation.stream().anyMatch(solution -> !(solution instanceof TimeTable)))
            throw new RuntimeException(this.name() + " expects to get a list of TimeTable objects");
        if(!(manager instanceof TimeTableManager))
            throw new RuntimeException(this.name() + " expects to get a TimeTableManager but got "
                    + manager.getClass().getName());

        TimeTableManager timeTableManager = (TimeTableManager) manager;

        Random rand = new Random();

        offSpringPopulation.forEach(offSpring -> {
            if(rand.nextInt(100) <= timeTableManager.getMutationProbability(this))
                this.mutate(offSpring, timeTableManager, args);
        });
        */
    }

    protected void mutate(Solution offSpring, TimeTableManager manager, Object... args)
    {
        TimeTableUtils.checkArgs(this.name(), new Class<?>[] {Integer.class, Character.class}, args);
        TimeTableUtils.assertSolutionIsTimeTable(offSpring, this.name());

        TimeTable timeTable = (TimeTable) offSpring;
        int maxTuples = (int) args[0];
        Random rand = new Random();

        int maxTuplesToMutate = rand.nextInt(maxTuples);
        int tuplesChanged = 0;

        for(TimeTableTuple tuple : timeTable.getTuples())
        {
            if(tuplesChanged >= maxTuplesToMutate)
                break;
            if(rand.nextBoolean())
            {
                tuplesChanged += 1;
                switch (this)
                {
                    case FlippingDay:
                        tuple.setDay(manager.getRandomDay());
                        break;
                    case FlippingHour:
                        tuple.setHour(manager.getRandomHour());
                        break;
                    case FlippingGrade:
                        tuple.setGrade(manager.getRandomGrade());
                        break;
                    case FlippingSubject:
                        tuple.setSubject(manager.getRandomSubject());
                        break;
                    case FlippingTeacher:
                        tuple.setTeacher(manager.getRandomTeacher());
                        break;
                    default:
                        throw new RuntimeException("Need to override mutate for non flipping mutations!");
                }
            }
        }
    }
}
