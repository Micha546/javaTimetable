package Main.Engine.Evolution.TimeTableSolution.Classes;

import Main.Engine.Xml.AutoGenerated.ETTTeacher;
import Main.Engine.Xml.AutoGenerated.ETTTeaches;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Teacher implements Serializable {
    private Integer id;
    private String name;
    private Set<Integer> subjectIds;
    private Integer workingHours;

    public Teacher(Integer _id, String _name, Set<Integer> _subjectIds, Integer _workingHours)
    {
        this.id = _id;
        this.name = _name;
        this.subjectIds = new HashSet<>(_subjectIds);
        this.workingHours = _workingHours;
    }

    public Teacher(ETTTeacher ettTeacher)
    {
        this.id = ettTeacher.getId();
        this.name = ettTeacher.getETTName();
        this.subjectIds = generateSetFromETT(ettTeacher.getETTTeaching().getETTTeaches());
        this.workingHours = ettTeacher.getETTWorkingHours();
    }

    private Set<Integer> generateSetFromETT(List<ETTTeaches> ettListOfSubjectsIds)
    {
        Set<Integer> newSet = new HashSet<>();

        for(ETTTeaches ettSubjectId : ettListOfSubjectsIds)
            newSet.add(ettSubjectId.getSubjectId());

        return newSet;
    }

    public Teacher(Teacher other)
    {
        this.id = other.id;
        this.name = other.name;
        this.subjectIds = new HashSet<>(other.subjectIds);
        this.workingHours = other.workingHours;
    }

    public Boolean isknowledgeable(Subject subject)
    {
        return isknowledgeable(subject.getId());
    }

    public Boolean isknowledgeable(Integer subjectId)
    {
        return subjectIds.contains(subjectId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(name, teacher.name) && Objects.equals(subjectIds, teacher.subjectIds) && Objects.equals(workingHours, teacher.workingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subjectIds, workingHours);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subjectIds=" + subjectIds +
                ", workingHours=" + workingHours +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Integer> getSubjectIds() {
        return subjectIds;
    }

    public Integer getWorkingHours() {
        return workingHours;
    }
}
