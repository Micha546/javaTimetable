package Main.Engine.Evolution;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface AlgorithmManager extends Serializable {
    CrossOver getCrossOver();
    Integer getCrossOverCutSize();
    Object[] getCrossOverArguments();
    List<Mutation> getMutations();
    Object[] getMutationArguments(Mutation mutation);
    Integer getMutationProbability(Mutation mutation);
    Selection getSelection();
    Object[] getSelectionArguments();
    Integer getElitism();
    Set<Rule> getRules();
    Rule.Severity getRuleSeverity(Rule rule);
    Object[] getRuleArguments(Rule rule);
    Integer getHardRuleWeight();
    Integer getInitialPopulationSize();
    InformationCarrier getDTOManager();
    InformationCarrier getDTOCrossOver(CrossOver crossOver);
    InformationCarrier getDTOMutation(Mutation mutation);
    InformationCarrier getDTOSelection(Selection selection);
    InformationCarrier getDTORule(Rule rule);
    void setCrossOver(InformationCarrier dtoCrossOver);
    void setSelection(InformationCarrier dtoSelection);
    void addMutation(InformationCarrier dtoMutation);
    void deleteMutation(InformationCarrier dtoMutation);
    void changeMutationProbability(InformationCarrier dtoMutation, Integer newProbability);
}
