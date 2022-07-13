package Main.Engine.Evolution.TimeTableSolution.Classes;

import Main.Engine.Xml.AutoGenerated.ETTSubject;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Subject implements Serializable {
    private Integer id;
    private String name;

    public Subject(ETTSubject ettSubject)
    {
        this.id = ettSubject.getId();
        this.name = ettSubject.getName();
    }

    public Subject(Integer _id, String _name)
    {
        this.id = _id;
        this.name = _name;
    }

    public Subject(Subject other)
    {
        this.id = other.id;
        this.name = other.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
