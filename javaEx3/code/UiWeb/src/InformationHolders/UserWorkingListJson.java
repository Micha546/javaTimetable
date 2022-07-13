package InformationHolders;

public class UserWorkingListJson {
    private final String username;
    private final Double fitness;
    private final Integer generation;

    public UserWorkingListJson(String username, Double fitness, Integer generation) {
        this.username = username;
        this.fitness = fitness;
        this.generation = generation;
    }

    public String getUsername() {
        return username;
    }

    public Double getFitness() {
        return fitness;
    }

    public Integer getGeneration() {
        return generation;
    }
}
