package Main.Engine.Evolution;

import java.io.Serializable;

public interface SolutionFactory extends Serializable {
    Solution createRandom(AlgorithmManager manager, Object... args);
    Solution copy(Solution solution);
}
