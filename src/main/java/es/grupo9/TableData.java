package es.grupo9;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableData {
    SimpleStringProperty member;
    SimpleDoubleProperty activities;
    SimpleDoubleProperty hours;
    SimpleDoubleProperty cost;

    TableData(String member, Double activities, Double hours, Double cost) {
        this.member = new SimpleStringProperty(member);
        this.activities = new SimpleDoubleProperty(activities);
        this.hours = new SimpleDoubleProperty(hours);
        this.cost = new SimpleDoubleProperty(cost);
    }

    public String getMember() {
        return member.get();
    }

    public void setMember(String m) {
        member.set(m);
    }

    public Double getActivities() {
        return activities.get();
    }

    public void setActivities(Double e) {
        activities.set(e);
    }

    public Double getHours() {
        return hours.get();
    }

    public void setHours(Double h) {
        hours.set(h);
    }

    public Double getCost() {
        return cost.get();
    }

    public void setCost(Double c) {
        cost.set(c);
    }

}
