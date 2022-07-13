package Main.Engine.Evolution;

import java.io.Serializable;

public interface StopCondition extends Serializable {
    boolean isConditionApplying(Algorithm algorithm);
}
