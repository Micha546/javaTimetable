package Main.Engine.Evolution;

import java.io.Serializable;

public interface Solution extends Serializable {
    double calculateFitness(AlgorithmManager manager);
    InformationCarrier getDTOSolution();
}
