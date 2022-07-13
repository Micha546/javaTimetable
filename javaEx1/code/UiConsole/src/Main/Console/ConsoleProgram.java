package Main.Console;

import Main.Engine.Evolution.*;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableFactory;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Evolution.TimeTableSolution.DTO.*;
import Main.Engine.Xml.XmlReader;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleProgram {

    boolean runProgram;
    Algorithm algorithm;
    Thread algorithmThread;

    public ConsoleProgram()
    {
        this.runProgram = true;
        this.algorithm = null;
        this.algorithmThread = new Thread();
    }

    public void run()
    {
        try {
            System.out.println("Time Table evolution algorithm:");
            System.out.println("-------------------------------");
            while(runProgram)
            {
                printMainMenu();
                System.out.println("Choose an option:");
                int input = getLegalMenuPickFromUser(0, MainMenu.values().length - 1);
                switch (MainMenu.values()[input])
                {
                    case ExitProgram:
                        handleExitProgram();
                        break;
                    case LoadXmlFile:
                        handleXmlFileLoading();
                        break;
                    case PrintAlgorithmStatus:
                        handlePrintingAlgorithmInfo();
                        break;
                    case RunStopAlgorithm:
                        handleRunStopAlgorithm();
                        break;
                    case ViewBestSolution:
                        handleShowBestSolution();
                        break;
                    case WatchAlgorithm:
                        handleWatchAlgorithm();
                        break;
                    case SaveAlgorithm:
                        handleSaveAlgorithm();
                        break;
                    case LoadAlgorithm:
                        handleLoadSavedAlgorithm();
                        break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            handleExitProgram();
            System.out.println("Press 'Enter' to continue");
            new Scanner(System.in).nextLine();
            return;
        }
        System.out.println("All went well");
    }

    private void handleExitProgram()
    {
        if(algorithmThread.isAlive())
            stopRunningAlgorithm();
        runProgram = false;
    }

    private void handleXmlFileLoading()
    {
        if(algorithm != null)
        {
            System.out.println("Warning! you are about to load a new file while having another file loaded,");
            System.out.println("this will erase ALL progress of the current running algorithm,");
            System.out.println("do you wish to continue? (y,n)");
            if(!getLegalYesNoFromUser("y", "n"))
                return;
        }

        System.out.println("Please enter the absolute path of the xml file you wish to load:");
        System.out.println("For example: C:\\Users\\MyUser\\Desktop\\MyXmlFile.xml");
        String filePath = new Scanner(System.in).nextLine();
        try{
            TimeTableManager manager = new TimeTableManager(XmlReader.createETTDescriptorFromXmlFile(filePath));
            Algorithm temp = new Algorithm(new TimeTableFactory(), manager);
            stopRunningAlgorithm();
            algorithm = temp;
        }
        catch (RuntimeException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private List<StopCondition> getStopConditionsFromUser()
    {
        List<StopCondition> lst = new ArrayList<>();

        System.out.println("Choose which Stop condition you would like to have:");
        System.out.println("1. Run X generations");
        System.out.println("2. Run until fitness is at least X");

        int legalInput = getLegalMenuPickFromUser(1,2);

        switch(legalInput)
        {
            case 1:
                handleGetStopByGenerations(lst);
                break;
            case 2:
                handleGetStopByFitness(lst);
                break;
            default:
                throw new RuntimeException("need to implement this condition (StopCondition)");
        }

        return lst;
    }

    private void handleGetStopByGenerations(List<StopCondition> lst)
    {
        System.out.println("Please enter the number of Generations you want the algorithm to run:" +
                " (number needs to be higher than 100)");
        int maxGenerationsToRun = getIntAboveMinValueFromUser(100);
        lst.add(algorithm -> algorithm.readGeneration() > maxGenerationsToRun);
    }

    private void handleGetStopByFitness(List<StopCondition> lst)
    {
        System.out.println("Please enter the Fitness you want the solution to achieve:" +
                " (number needs to be higher than 0)");
        int maxFitness = getIntAboveMinValueFromUser(0);
        lst.add(algorithm -> algorithm.readBestFitness() > maxFitness);
    }

    private int getIntAboveMinValueFromUser(int minValue)
    {
        int generations = 0;
        boolean legal;

        do {
            legal = true;
            String generationsStr = new Scanner(System.in).nextLine();
            try {
                generations = Integer.parseInt(generationsStr);
            }
            catch (NumberFormatException ex)
            {
                System.out.println("Please enter a number (no other charters)");
                legal = false;
                continue;
            }

            if(generations < minValue)
            {
                System.out.println("Please enter a number that is higher than " + minValue);
                legal = false;
            }
        }while(!legal);

        return generations;
    }

    private void handlePrintingAlgorithmInfo()
    {
        if(algorithm != null)
        {
            printLoadedAlgorithmInformation(algorithm);
        }
        else
            System.out.println("There is no loaded Xml file, please choose the file to load first");
    }

    private void handleRunStopAlgorithm()
    {
        if(algorithm == null)
            System.out.println("There is no loaded Xml file, please choose the file to load first");
        else if (algorithmThread.isAlive())
        {
            System.out.println("Stopping the algorithm...");
            stopRunningAlgorithm();
        }
        else
        {
            if(algorithm.readGotToStopCondition())
                printAlgorithmGotToStopConditionMessage();
            else
            {
                if(!algorithm.readHasAtLeastOneStopCondition())
                {
                    List<StopCondition> stopConditions = getStopConditionsFromUser();
                    algorithm.addStopConditions(stopConditions);
                }
                if(!algorithm.readIsKeepingLogs())
                {
                    System.out.println("Enter per how many generations you want to keep track of the algorithm logs:");
                    int logsGap = getIntAboveMinValueFromUser(1);
                    algorithm.WriteLogsEveryXGenerations(logsGap);
                }

                System.out.println("Starting the algorithm...");
                startRunningAlgorithm();
            }
        }
    }

    private void handleShowBestSolution()
    {
        if(algorithm == null)
            System.out.println("There is no loaded Xml file, please choose the file to load first");
        else
        {
            System.out.println("How would you like to see the result?");
            printViewBestSolutionSubMenu();
            int input = getLegalMenuPickFromUser(0, ViewBestSolutionSubMenu.values().length - 1);

            Pair<GenerationSolutionFitnessComplex, Map<InformationCarrier, Integer>> syncPair =
                    algorithm.readBestSolutionFitnessAndItsRules();

            switch(ViewBestSolutionSubMenu.values()[input])
            {
                case GoBack:
                    break;
                case Raw:
                    handleRawPrint(syncPair.getKey(), syncPair.getValue());
                    break;
                case ByTeacher:
                    handleTeacherPrint(syncPair.getKey(), syncPair.getValue());
                    break;
                case ByGrade:
                    handleGradePrint(syncPair.getKey(), syncPair.getValue());
                    break;
            }
        }
    }

    private void handleRawPrint(GenerationSolutionFitnessComplex bestSolutionComplex,
                                Map<InformationCarrier, Integer> rulePercent)
    {
        System.out.println("Generation: " + bestSolutionComplex.getGeneration() + ", best fitness: "
                + bestSolutionComplex.getFitness());
        DTOTimeTable bestFitnessTimeTable = (DTOTimeTable) bestSolutionComplex.getSolution();

        new ArrayList<>(bestFitnessTimeTable.getTuples()).stream()
                .sorted((tuple1, tuple2) -> {
                    if(!tuple1.getDay().equals(tuple2.getDay()))
                        return tuple1.getDay() - tuple2.getDay();
                    else if(!tuple1.getHour().equals(tuple2.getHour()))
                        return tuple1.getHour() - tuple2.getHour();
                    else if(!tuple1.getGrade().equals(tuple2.getGrade()))
                        return tuple1.getGrade().getId() - tuple2.getGrade().getId();
                    else
                        return tuple1.getTeacher().getId() - tuple2.getTeacher().getId();
                })
                .forEach(tuple ->
                        System.out.printf("<%2d, %2d, %2d, %2d, %2d>%n",
                        tuple.getDay(),
                        tuple.getHour(),
                        tuple.getGrade().getId(),
                        tuple.getTeacher().getId(),
                        tuple.getSubject().getId()));

        printAdditionalInformation(rulePercent);
    }

    private void handleTeacherPrint(GenerationSolutionFitnessComplex bestSolutionComplex,
                                    Map<InformationCarrier, Integer> rulePercent)
    {
        System.out.println("Generation: " + bestSolutionComplex.getGeneration() + ", best fitness: "
                + bestSolutionComplex.getFitness());
        DTOTimeTable bestFitnessTimeTable = (DTOTimeTable) bestSolutionComplex.getSolution();

        List<DTOTeacher> teachers = ((DTOTimeTableManager) algorithm.readGeneralInformation()).getTeachers().stream()
                .sorted(Comparator.comparingInt(DTOTeacher::getId))
                .collect(Collectors.toList());

        System.out.println("reader: Grade id, Subject id");

        teachers.forEach(teacher -> {
            System.out.println();
            System.out.println("Time Table for teacher: " + teacher.getName() + ", id: " + teacher.getId());
            printTimeTable(bestFitnessTimeTable.getDays(), bestFitnessTimeTable.getHours(),
                    bestFitnessTimeTable.getTuplesByTeacher(teacher), true);
        });

        printAdditionalInformation(rulePercent);
    }

    private void handleGradePrint(GenerationSolutionFitnessComplex bestSolutionComplex,
                                  Map<InformationCarrier, Integer> rulePercent)
    {
        System.out.println("Generation: " + bestSolutionComplex.getGeneration() + ", best fitness: "
                + bestSolutionComplex.getFitness());
        DTOTimeTable bestFitnessTimeTable = (DTOTimeTable) bestSolutionComplex.getSolution();

        List<DTOGrade> grades = ((DTOTimeTableManager) algorithm.readGeneralInformation()).getGrades().stream()
                .sorted(Comparator.comparingInt(DTOGrade::getId))
                .collect(Collectors.toList());

        System.out.println("reader: Teacher id, Subject id");

        grades.forEach(grade -> {
            System.out.println();
            System.out.println("Time Table for Grade: " + grade.getName() + ", id: " + grade.getId());
            printTimeTable(bestFitnessTimeTable.getDays(), bestFitnessTimeTable.getHours(),
                    bestFitnessTimeTable.getTuplesByGrade(grade), false);
        });

        printAdditionalInformation(rulePercent);
    }

    private void printTimeTable(int days, int hours, Set<DTOTimeTableTuples> tuples, boolean byTeacher)
    {
        StringBuilder columnSeparator = new StringBuilder();
        StringBuilder columnValues = new StringBuilder();
        StringBuilder columnHeader = new StringBuilder();

        for(int i = 0; i <= days; ++i)
        {
            columnSeparator.append(i == 0 ? "+-------+" : "-------+");
            columnHeader.append(i == 0 ? "|  H/D  |" : String.format("   %d   |", i));
        }

        System.out.println(columnSeparator);
        System.out.println(columnHeader);
        System.out.println(columnSeparator);

        for(int j = 1; j <= hours; ++j)
        {
            Map<Integer, List<DTOTimeTableTuples>> dayToTuplesOfHour = new HashMap<>();
            for(int i = 1; i <= days; ++i)
            {
                int thisDay = i;
                int thisHour = j;
                dayToTuplesOfHour.put(i, tuples.stream()
                        .filter(tuple -> tuple.getDay().equals(thisDay) && tuple.getHour().equals(thisHour))
                        .sorted(Comparator.comparingInt(tuple ->
                                byTeacher ? tuple.getTeacher().getId() : tuple.getGrade().getId()))
                        .collect(Collectors.toList()));
            }

            boolean firstLoop = true;
            do
            {
                for(int i = 0; i <= days; ++i)
                {
                    columnValues.append(i == 0 ? (firstLoop ? String.format("|   %d   |", j) : "|       |") :
                            (dayToTuplesOfHour.get(i).size() == 0 ? "       |" :
                                    String.format(" %2d,%2d |", byTeacher ?
                                                    dayToTuplesOfHour.get(i).get(0).getGrade().getId() :
                                                    dayToTuplesOfHour.get(i).get(0).getTeacher().getId(),
                                            dayToTuplesOfHour.get(i).get(0).getSubject().getId())));
                    if(i != 0 && dayToTuplesOfHour.get(i).size() != 0)
                        dayToTuplesOfHour.get(i).remove(0);
                }
                firstLoop = false;
                System.out.println(columnValues);
                columnValues = new StringBuilder();
            } while(dayToTuplesOfHour.values().stream().anyMatch(lst -> lst.size() != 0));

            System.out.println(columnSeparator);
        }
    }

    private void printAdditionalInformation(Map<InformationCarrier, Integer> rulesToPrint)
    {
        Map<DTOTimeTableRules, Integer> timeTableRulesEnforcementMap =
                rulesToPrint.entrySet().stream().collect(Collectors.toMap(
                        entry -> (DTOTimeTableRules) entry.getKey(),
                        Map.Entry::getValue
                ));

        List<DTOTimeTableRules> sortedByName = new ArrayList<>(timeTableRulesEnforcementMap.keySet());
        sortedByName.sort(Comparator.comparing(DTOTimeTableRules::getRuleName));

        System.out.println();
        System.out.println("Rules that were calculated on this solution and their percent of enforcement:");
        for(DTOTimeTableRules rule : sortedByName)
            System.out.println("Rule: " + rule.getRuleName() +
                    ", percent of enforcement: " + timeTableRulesEnforcementMap.get(rule));
    }

    private void handleWatchAlgorithm()
    {
        if(algorithm == null)
        {
            System.out.println("There is no loaded Xml file, please choose the file to load first");
        }
        else
        {
            System.out.println("Choose an option:");
            System.out.println("0. Go back");
            System.out.println("1. Watch the algorithm in real time");
            System.out.println("2. Watch the algorithm logs until now");
            switch(getLegalMenuPickFromUser(0,2))
            {
                case 0:
                    break;
                case 1:
                    handleShowAlgorithmRunning();
                    break;
                case 2:
                    handleShowAlgorithmLogs();
                    break;
            }
        }
    }

    private void handleShowAlgorithmRunning()
    {
        if(!algorithmThread.isAlive())
        {
            if(algorithm.readGotToStopCondition())
                printAlgorithmGotToStopConditionMessage();
            else
                System.out.println("The algorithm is not running," +
                        " please run the algorithm (option 3) and then try again.");
        }
        else
        {
            System.out.println("Per how many generations would you like to see results?");
            int generationGap = getIntAboveMinValueFromUser(1);
            algorithm.WriteEveryXGenerationsToMap(generationGap);

            System.out.print("To stop watching the algorithm, press 'Enter'");
            try{Thread.sleep(500);} catch (InterruptedException ex) {}
            System.out.print(".");
            try{Thread.sleep(500);} catch (InterruptedException ex) {}
            System.out.print(".");
            try{Thread.sleep(500);} catch (InterruptedException ex) {}
            System.out.println(".");
            try{Thread.sleep(500);} catch (InterruptedException ex) {}

            Thread enterWaiter = new Thread(
                    () -> new Scanner(System.in).nextLine(),
                    "exit watching algorithm thread"
            );
            enterWaiter.start();


            double lastBestFitness = 0;
            boolean firstLoop = true;
            while(enterWaiter.isAlive())
            {
                List<GenerationSolutionFitnessComplex> lstToPrint = algorithm.readGenerationsSaved();

                for(GenerationSolutionFitnessComplex complex : lstToPrint)
                {
                    if(firstLoop)
                        System.out.println("Generation: " + complex.getGeneration() +
                                ", best fitness: " + complex.getFitness());
                    else
                        System.out.format("Generation: " + complex.getGeneration() +
                                ", best fitness: " + complex.getFitness() +
                                ", change: %+f%n", (complex.getFitness() - lastBestFitness));
                    firstLoop = false;
                    lastBestFitness = complex.getFitness();
                }
            }

            algorithm.stopWriteEveryXGenerationsToMap();
        }
    }

    private void handleShowAlgorithmLogs()
    {
        if(!algorithm.readIsKeepingLogs())
        {
            System.out.println("The algorithm is not keeping track of logs!");
        }
        else
        {
            ArrayList<AlgorithmLog> logs = algorithm.readLogs();

            boolean firstLoop = true;
            double lastBestFitness = 0;
            for(AlgorithmLog log : logs)
            {
                if(firstLoop)
                    System.out.println("Generation: " + log.getGeneration() +
                            ", best fitness: " + log.getFitness());
                else
                    System.out.format("Generation: " + log.getGeneration() +
                            ", best fitness: " + log.getFitness() +
                            ", change: %+f%n", (log.getFitness() - lastBestFitness));
                firstLoop = false;
                lastBestFitness = log.getFitness();
            }
        }
    }

    private void handleSaveAlgorithm()
    {
        if(algorithm == null)
            System.out.println("There is no loaded algorithm to save!");
        else
        {
            boolean isAlgorithmRunning = false;

            String userPath = getPathFromUserAppendSaveFileEnding();

            if(new File(userPath).exists())
            {
                System.out.println("Warning! you are about to override a save file, this will erase the algorithm");
                System.out.println("saved in that file, are you sure you want to continue? (y,n)");
                if(!getLegalYesNoFromUser("y", "n"))
                    return;
            }
            try{
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userPath));
                if(algorithmThread.isAlive())
                {
                    stopRunningAlgorithm();
                    isAlgorithmRunning = true;
                }

                out.writeObject(algorithm);
                out.flush();
                out.close();

                System.out.println("Algorithm saved successfully!");
            }
            catch(FileNotFoundException ex)
            {
                System.out.println("The path provided was illegal " + ex.getMessage());
            }
            catch(IOException ex)
            {
                System.out.println("Something went wrong while trying to save the file...");
                System.out.println(ex.getMessage());
            }
            finally {
                if(isAlgorithmRunning)
                    startRunningAlgorithm();
            }
        }
    }

    private void handleLoadSavedAlgorithm()
    {
        if(algorithm != null)
        {
            System.out.println("Warning! you are about to load a new file while having another file loaded,");
            System.out.println("this will erase ALL progress of the current running algorithm,");
            System.out.println("do you wish to continue? (y,n)");
            if(!getLegalYesNoFromUser("y", "n"))
                return;
        }

        String userPath = getPathFromUserAppendSaveFileEnding();

        if(!(new File(userPath).exists()))
        {
            System.out.println("The save file does not exist");
        }
        else
        {
            try{
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(userPath));

                Algorithm temp = (Algorithm) in.readObject();
                if(algorithmThread.isAlive())
                    stopRunningAlgorithm();

                algorithm = temp;

                System.out.println("Algorithm loaded successfully!");
                System.out.println("The algorithm is not running, you can run it with option 3");
            }
            catch(FileNotFoundException ex)
            {
                System.out.println("The path provided was illegal " + ex.getMessage());
            }
            catch(ClassNotFoundException ex)
            {
                System.out.println("The save file is too old/ is corrupted! (was saved before changes were made " +
                        "to the algorithm class)");
            }
            catch(IOException ex)
            {
                System.out.println("Something went wrong while trying to load the file...");
                System.out.println("The save file is too old/ is corrupted");
            }
        }
    }

    private String getPathFromUserAppendSaveFileEnding()
    {
        System.out.println("Enter the absolute path to the file you want to save to," +
                " including the name of the save file");
        System.out.println("For example: C:\\Users\\MyUser\\Desktop\\MySavedAlgorithm");

        String userPath = new Scanner(System.in).nextLine();

        return userPath.endsWith(".alsf") ? userPath : userPath + ".alsf";
    }

    private void printAlgorithmGotToStopConditionMessage()
    {
        System.out.println("The algorithm has reached one of its stop conditions," +
                " so it can't run anymore,");
        System.out.println("you can watch the latest generation with option 4");
    }

    private void printMainMenu()
    {
        for(MainMenu pick : MainMenu.values())
            System.out.println(pick);
    }

    private void printViewBestSolutionSubMenu()
    {
        for(ViewBestSolutionSubMenu pick : ViewBestSolutionSubMenu.values())
            System.out.println(pick);
    }

    public void printLoadedAlgorithmInformation(Algorithm algorithm)
    {
        InformationCarrier genericCarrier = algorithm.readGeneralInformation();
        int populationSize = algorithm.readPopulationSize();

        if(genericCarrier.getClass() != DTOTimeTableManager.class)
            throw new RuntimeException("There was a problem fetching information from the algorithm");

        DTOTimeTableManager carrier = (DTOTimeTableManager) genericCarrier;

        System.out.println();
        printSubjectInformation(carrier);
        System.out.println();
        System.out.println();
        printTeacherInformation(carrier);
        System.out.println();
        System.out.println();
        printGradeInformation(carrier);
        System.out.println();
        System.out.println();
        printRulesInformation(carrier);
        System.out.println();
        System.out.println();
        printGeneralInformation(carrier, populationSize);
        System.out.println();
    }

    private void printSubjectInformation(DTOTimeTableManager carrier)
    {
        System.out.println("Subjects:");
        System.out.println("----------------------------------------------------------");
        carrier.getSubjects().stream()
                .sorted(Comparator.comparingInt(DTOSubject::getId))
                .forEach(dtoSubject ->
                        System.out.println("Id: " +  dtoSubject.getId() + ", name: " + dtoSubject.getName()));
        System.out.println("----------------------------------------------------------");
    }

    private void printTeacherInformation(DTOTimeTableManager carrier)
    {
        System.out.println("Teachers:");
        carrier.getTeachers().stream()
                .sorted(Comparator.comparingInt(DTOTeacher::getId))
                .forEach(dtoTeacher -> {
                    System.out.println("----------------------------------------------------------");
                    System.out.println("Id: " +  dtoTeacher.getId() + ", name: " + dtoTeacher.getName());
                    System.out.println("Subjects taught:");
                    carrier.getSubjects().stream()
                            .sorted(Comparator.comparingInt(DTOSubject::getId))
                            .filter(dtoSubject -> dtoTeacher.getTaughtSubjectIds().contains(dtoSubject.getId()))
                            .forEach(dtoSubject ->
                                    System.out.println("Id: " +  dtoSubject.getId() +
                                            ", name: " + dtoSubject.getName()));
                    System.out.println("----------------------------------------------------------");
                });
    }

    private void printGradeInformation(DTOTimeTableManager carrier)
    {
        System.out.println("Grades:");
        carrier.getGrades().stream()
                .sorted(Comparator.comparingInt(DTOGrade::getId))
                .forEach(dtoGrade -> {
                    System.out.println("----------------------------------------------------------");
                    System.out.println("Id: " +  dtoGrade.getId() + ", name: " + dtoGrade.getName());
                    System.out.println("Subjects that need to be studied:");
                    carrier.getSubjects().stream()
                            .filter(dtoSubject -> dtoGrade.getSubjectIdToWeeklyHours().containsKey(dtoSubject.getId()))
                            .sorted(Comparator.comparingInt(DTOSubject::getId))
                            .forEach(dtoSubject -> System.out.println("Id: " +  dtoSubject.getId() + ", name: "
                                    + dtoSubject.getName() + " weekly hours: "
                                    + dtoGrade.getSubjectIdToWeeklyHours().get(dtoSubject.getId())));
                    System.out.println("----------------------------------------------------------");
                });
    }

    private void printRulesInformation(DTOTimeTableManager carrier)
    {
        System.out.println("Rules:");
        System.out.println("----------------------------------------------------------");
        carrier.getRules().forEach(dtoRule ->
                System.out.println("Name: " + dtoRule.getRuleName() + ", rule type: " + dtoRule.getRuleSeverity()));
        System.out.println("----------------------------------------------------------");
    }

    private void printGeneralInformation(DTOTimeTableManager carrier, Integer populationSize)
    {
        System.out.println("General Information:");
        System.out.println("----------------------------------------------------------");
        System.out.println("Population Size: " + populationSize);
        System.out.println("Selection type: " + carrier.getSelection().getSelectionName() +
                ", " + SelectionArgsToString(carrier.getSelection()));
        System.out.println("CrossOver type: " + carrier.getCrossOver().getCrossOverName() +
                ", cut size: " + carrier.getCrossOver().getCutSize());
        System.out.println("Mutations:");
        carrier.getMutations().forEach(dtoMutation -> System.out.println("Name: " + dtoMutation.getMutationName()
                + ", probability: " + dtoMutation.getProbability()));
        System.out.println("----------------------------------------------------------");
    }

    private String SelectionArgsToString(DTOTimeTableSelection selection)
    {
        switch (selection.getSelectionName())
        {
            case "Truncation":
                return "topPercent: " + selection.getSelectionArgumentsList().get(0);
            case "RouletteWheel":
                return "";
            case "Tournament":
                return "Predefined tournament equalizer: " + selection.getSelectionArgumentsList().get(0);
            default:
                throw new RuntimeException("SelectionArgsToString is not implemented for "
                        + selection.getSelectionName());
        }
    }

    private int getLegalMenuPickFromUser(int rangeStart, int rangeFinish)
    {
        String userInput;
        int legalUserInputValue = -1;
        boolean legalInput;

        do{
            legalInput = true;
            userInput = new Scanner(System.in).nextLine();

            try{
                legalUserInputValue = Integer.parseInt(userInput);
            }
            catch(NumberFormatException ex)
            {
                System.out.println("Please enter a number between " + rangeStart + " and " + rangeFinish);
                legalInput = false;
                continue;
            }

            if(legalUserInputValue < rangeStart || legalUserInputValue > rangeFinish)
            {
                System.out.println("Please enter a number between " + rangeStart + " and " + rangeFinish);
                legalInput = false;
            }
        }while(!legalInput);

        return legalUserInputValue;
    }

    private boolean getLegalYesNoFromUser(String yesInput, String noInput)
    {
        String userInput;
        boolean legalInput;

        do{
            legalInput = true;
            userInput = new Scanner(System.in).nextLine();

            if(!userInput.equals(yesInput) && !userInput.equals(noInput))
            {
                System.out.println("Please enter " + yesInput + " or " + noInput);
                legalInput = false;
            }
        }while(!legalInput);

        return userInput.equals(yesInput);
    }

    private void stopRunningAlgorithm()
    {
        algorithmThread.interrupt();
        try { algorithmThread.join(); } catch (InterruptedException ex) {}
    }

    private void startRunningAlgorithm()
    {
        algorithmThread = new Thread(algorithm, "Engine thread");
        algorithmThread.start();
    }
}
