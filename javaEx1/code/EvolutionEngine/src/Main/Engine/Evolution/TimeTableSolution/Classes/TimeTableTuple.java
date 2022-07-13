package Main.Engine.Evolution.TimeTableSolution.Classes;

import java.io.Serializable;
import java.util.Objects;

public class TimeTableTuple implements Serializable, Cloneable {
    private Integer day;
    private Integer hour;
    private Teacher teacher;
    private Grade grade;
    private Subject subject;

    @Override
    public Object clone(){
        try{
            return super.clone();
        }
        catch (CloneNotSupportedException ex)
        {
            throw new Error("Something went wrong with TimeTableTuple.clone");
        }
    }

    public TimeTableTuple copy()
    {
        return (TimeTableTuple) this.clone();
    }

    public TimeTableTuple(Integer day, Integer hour, Teacher teacher, Grade grade, Subject subject) {
        this.day = day;
        this.hour = hour;
        this.teacher = teacher;
        this.grade = grade;
        this.subject = subject;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Grade getGrade() {
        return grade;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableTuple that = (TimeTableTuple) o;
        return Objects.equals(day, that.day) && Objects.equals(hour, that.hour) && Objects.equals(teacher, that.teacher) && Objects.equals(grade, that.grade) && Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, teacher, grade, subject);
    }

    @Override
    public String toString() {
        return "TimeTableTuple{" +
                "day=" + day +
                ", hour=" + hour +
                ", teacher=" + teacher +
                ", grade=" + grade +
                ", subject=" + subject +
                '}';
    }
}
