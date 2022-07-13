package Main.Engine.Evolution;

import java.io.Serializable;
import java.util.List;

public interface Selection extends Serializable {
    List<Solution> doSelection(List<Solution> populationSortedByFitness, AlgorithmManager manager, Object... args);
}
