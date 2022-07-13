package Main.Engine.Evolution;

import java.io.Serializable;
import java.util.List;

public interface CrossOver extends Serializable {
    List<Solution> doCrossOver(List<Solution> parentPopulation, Integer offSpringPopulationSize,
                               SolutionFactory factory, Integer cutSize, Object... args);
}
