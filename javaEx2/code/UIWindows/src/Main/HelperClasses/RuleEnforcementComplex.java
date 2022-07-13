package Main.HelperClasses;

import java.util.Objects;

public class RuleEnforcementComplex {
    private final String name;
    private final Integer enforcement;

    public RuleEnforcementComplex(String name, Integer enforcement) {
        this.name = name;
        this.enforcement = enforcement;
    }

    public String getName() {
        return name;
    }

    public Integer getEnforcement() {
        return enforcement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleEnforcementComplex that = (RuleEnforcementComplex) o;
        return Objects.equals(name, that.name) && Objects.equals(enforcement, that.enforcement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, enforcement);
    }
}
