package Main.HelperClasses;

import Main.Logic.MainLogic;

public class StopConditionProgressComplex {

    private final MainLogic.StopConditionType stopConditionType;
    private final Double newProgressValue;

    public StopConditionProgressComplex(
            MainLogic.StopConditionType stopConditionType,
            Double newProgressValue)
    {
        this.stopConditionType = stopConditionType;
        this.newProgressValue = newProgressValue;
    }

    public MainLogic.StopConditionType getStopConditionType() {
        return stopConditionType;
    }

    public Double getNewProgressValue() {
        return newProgressValue;
    }
}
