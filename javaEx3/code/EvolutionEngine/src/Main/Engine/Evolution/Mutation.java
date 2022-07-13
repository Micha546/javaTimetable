package Main.Engine.Evolution;

import java.io.Serializable;
import java.util.List;

public interface Mutation extends Serializable {
    void doMutation(List<Solution> offSpringPopulation, AlgorithmManager manager, Object... args);
}
