package Main.Console;

import Main.Engine.Evolution.Algorithm;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableFactory;
import Main.Engine.Evolution.TimeTableSolution.Classes.TimeTableManager;
import Main.Engine.Xml.XmlReader;

public enum MainMenu {
    ExitProgram{
        @Override
        public String toString() {
            return "0. Exit";
        }
    },
    LoadXmlFile{
        @Override
        public String toString() {
            return "1. Load settings from a Xml file";
        }
    },
    PrintAlgorithmStatus{
        @Override
        public String toString() {
            return "2. Print the Loaded Xml file status";
        }

        public void sayHi()
        {}
    },
    RunStopAlgorithm{
        @Override
        public String toString() {
            return "3. Run/Stop the algorithm";
        }
    },
    ViewBestSolution{
        @Override
        public String toString() {
            return "4. View the best solution of the algorithm";
        }
    },
    WatchAlgorithm{
        @Override
        public String toString() {
            return "5. Watch the algorithm as it goes";
        }
    },
    SaveAlgorithm{
        @Override
        public String toString() {
            return "6. Save algorithm to a file";
        }
    },
    LoadAlgorithm{
        @Override
        public String toString() {
            return "7. Load an algorithm from a save file";
        }
    };

}
