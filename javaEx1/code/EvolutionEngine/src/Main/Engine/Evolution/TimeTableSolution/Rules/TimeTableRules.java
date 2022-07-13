package Main.Engine.Evolution.TimeTableSolution.Rules;

import Main.Engine.Evolution.AlgorithmManager;
import Main.Engine.Evolution.InformationCarrier;
import Main.Engine.Evolution.Rule;
import Main.Engine.Evolution.Solution;
import Main.Engine.Evolution.TimeTableSolution.Classes.*;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.DTO.DTOTimeTableRules;
import Main.Engine.Evolution.TimeTableSolution.TimeTableUtils;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public enum TimeTableRules implements Rule {
    TeacherIsHuman{
        @Override
        public double getPercentOfEnforcement(Solution solution, AlgorithmManager manager, Object... args) {
            TimeTableUtils.assertSolutionIsTimeTable(solution, this.name());
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {}, args);
            TimeTableUtils.assertManagerIsTimeTableManager(this.name(), manager);

            TimeTableManager timeTableManager = (TimeTableManager) manager;
            TimeTable timeTable = (TimeTable) solution;
            final int DH = timeTable.getDaysLimit() * timeTable.getHoursLimit();

            Map<Teacher, Integer> legalHoursOfTeacher = timeTableManager.getAllTeachers().stream().
                    collect(Collectors.toMap(teacher -> teacher, teacher -> {
                        Set<TimeTableTuple> tuplesOfTeacher = timeTable.getTuples().stream()
                                .filter(tuple -> tuple.getTeacher() == teacher)
                                .collect(Collectors.toSet());

                        Map<Pair<Integer, Integer>, Integer> hourToGrades = new HashMap<>();

                        for(TimeTableTuple tuple : tuplesOfTeacher)
                        {
                            Pair<Integer, Integer> dayHour = new Pair<>(tuple.getDay(), tuple.getHour());
                            hourToGrades.put(dayHour, hourToGrades.getOrDefault(dayHour, 0) + 1);
                        }

                        int illegalHours = hourToGrades.values().stream()
                                .filter(grades -> grades > 1)
                                .mapToInt(i -> i)
                                .sum();

                        return DH - illegalHours;
                    }));

            Map<Teacher, Double> teacherToLegalDaysPercent = legalHoursOfTeacher.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> (double) entry.getValue() / (double) DH
                    ));

            return teacherToLegalDaysPercent.values().stream().mapToDouble(percent -> percent).sum() /
                    (double) teacherToLegalDaysPercent.size();
        }
    },
    Singularity{
        @Override
        public double getPercentOfEnforcement(Solution solution, AlgorithmManager manager, Object... args) {
            TimeTableUtils.assertSolutionIsTimeTable(solution, this.name());
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {}, args);
            TimeTableUtils.assertManagerIsTimeTableManager(this.name(), manager);

            TimeTableManager timeTableManager = (TimeTableManager) manager;
            TimeTable timeTable = (TimeTable) solution;
            final int DH = timeTable.getDaysLimit() * timeTable.getHoursLimit();

            Map<Grade, Integer> legalHoursOfGrade = timeTableManager.getAllGrades().stream().
                    collect(Collectors.toMap(grade -> grade, grade -> {
                        Set<TimeTableTuple> tuplesOfGrade = timeTable.getTuples().stream()
                                .filter(tuple -> tuple.getGrade() == grade)
                                .collect(Collectors.toSet());

                        Map<Pair<Integer, Integer>, Integer> tuplesNumberOfHour = new HashMap<>();

                        tuplesOfGrade.forEach(tuple -> {
                            Pair<Integer, Integer> dayHour = new Pair<>(tuple.getDay(), tuple.getHour());
                            tuplesNumberOfHour.put(dayHour, tuplesNumberOfHour.getOrDefault(dayHour, 0) + 1);
                        });

                        return DH - tuplesNumberOfHour.values().stream()
                                .filter(tuplesOfHour -> tuplesOfHour > 1)
                                .mapToInt(tuplesOfHour -> tuplesOfHour)
                                .sum();
                    }));

            Map<Grade, Double> gradeToLegalDaysPercent = legalHoursOfGrade.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> (double) entry.getValue() / (double) DH
                    ));

            return gradeToLegalDaysPercent.values().stream().mapToDouble(percent -> percent).sum() /
                    (double) gradeToLegalDaysPercent.size();
        }
    },
    Knowledgeable{
        @Override
        public double getPercentOfEnforcement(Solution solution, AlgorithmManager manager, Object... args) {
            TimeTableUtils.assertSolutionIsTimeTable(solution, this.name());
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {}, args);
            TimeTableUtils.assertManagerIsTimeTableManager(this.name(), manager);

            TimeTableManager timeTableManager = (TimeTableManager) manager;
            TimeTable timeTable = (TimeTable) solution;

            Map<Teacher, Double> teacherToPercent = timeTableManager.getAllTeachers().stream().
                    collect(Collectors.toMap(teacher -> teacher, teacher -> {
                        Set<TimeTableTuple> tuplesOfTeacher = timeTable.getTuples().stream()
                                .filter(tuple -> tuple.getTeacher() == teacher)
                                .collect(Collectors.toSet());
                        if(tuplesOfTeacher.size() == 0)
                            return 1d;
                        else
                        {
                            Set<Subject> taughtSubjects = new HashSet<>();
                            tuplesOfTeacher.forEach(tuple -> taughtSubjects.add(tuple.getSubject()));
                            int numberOfIllegalSubjects = (int) taughtSubjects.stream()
                                    .filter(subject -> !teacher.isknowledgeable(subject))
                                    .count();
                            return (double) (taughtSubjects.size() - numberOfIllegalSubjects) /
                                    (double) taughtSubjects.size();
                        }
                    }));

            return teacherToPercent.values().stream().mapToDouble(percent -> percent).sum() /
                    (double) teacherToPercent.size();
        }
    },
    Satisfactory{
        @Override
        public double getPercentOfEnforcement(Solution solution, AlgorithmManager manager, Object... args) {
            TimeTableUtils.assertSolutionIsTimeTable(solution, this.name());
            TimeTableUtils.checkArgs(this.name(), new Class<?>[] {}, args);
            TimeTableUtils.assertManagerIsTimeTableManager(this.name(), manager);

            TimeTableManager timeTableManager = (TimeTableManager) manager;
            TimeTable timeTable = (TimeTable) solution;
            Map<Grade, Double> gradeToPercent = timeTableManager.getAllGrades().stream().
                    collect(Collectors.toMap(grade -> grade, grade -> {
                        Set<TimeTableTuple> tuplesOfGrade = timeTable.getTuples().stream()
                                .filter(tuple -> tuple.getGrade() == grade)
                                .collect(Collectors.toSet());

                        Map<Integer, Integer> gradeRequestOfHours = grade.getSubjectIdToWeeklyHours();
                        Map<Integer, Integer> actualLearnedHours = new HashMap<>();
                        Map<Integer, Double> subjectLearnedToPercent = new HashMap<>();
                        int badHours = 0;

                        for(TimeTableTuple tuple : tuplesOfGrade)
                        {
                            if(gradeRequestOfHours.containsKey(tuple.getSubject().getId()))
                                actualLearnedHours.put(tuple.getSubject().getId(),
                                        actualLearnedHours.getOrDefault(tuple.getSubject().getId(), 0) + 1);
                            else
                                badHours += 1;
                        }

                        for(Map.Entry<Integer, Integer> entry : gradeRequestOfHours.entrySet())
                        {
                            int hoursThatNeedToBeLearned = entry.getValue();
                            int hoursThatAreLearned = actualLearnedHours.getOrDefault(entry.getKey(), 0);

                            if(hoursThatNeedToBeLearned > hoursThatAreLearned)
                                subjectLearnedToPercent.put(entry.getKey(),
                                        (double) hoursThatAreLearned / (double) hoursThatNeedToBeLearned );
                            else if(hoursThatNeedToBeLearned < hoursThatAreLearned)
                            {
                                subjectLearnedToPercent.put(entry.getKey(),
                                        (double) hoursThatNeedToBeLearned / (double) hoursThatAreLearned );
                            }
                            else
                                subjectLearnedToPercent.put(entry.getKey(), 1d);
                        }

                        int numberOfSubjectsToBeLearned = gradeRequestOfHours.size();
                        double sumOfPercents = subjectLearnedToPercent.values().stream().mapToDouble(val -> val).sum();

                        return sumOfPercents / (double) (numberOfSubjectsToBeLearned + badHours);
                    }));


            return gradeToPercent.values().stream().mapToDouble(val -> val).sum() /
                    (double) gradeToPercent.size();
        }
    };

}
