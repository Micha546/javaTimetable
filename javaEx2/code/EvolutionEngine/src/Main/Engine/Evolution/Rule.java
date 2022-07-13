package Main.Engine.Evolution;

import java.io.Serializable;

public interface Rule extends Serializable {
    double getPercentOfEnforcement(Solution solution, AlgorithmManager manager, Object... args);

    enum Severity{
        Hard, Soft
    }
}
