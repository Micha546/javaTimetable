package Main.Console;

public enum ViewBestSolutionSubMenu {
    GoBack {
        @Override
        public String toString() {
            return "0. Back to main menu";
        }
    },
    Raw{
        @Override
        public String toString() {
            return "1. Raw format";
        }
    },
    ByTeacher{
        @Override
        public String toString() {
            return "2. Watch teachers time tables";
        }
    },
    ByGrade {
        @Override
        public String toString() {
            return "3. Watch Grades time tables";
        }
    };
}
