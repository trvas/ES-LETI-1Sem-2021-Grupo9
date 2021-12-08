package es.grupo9;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Creates the property types for the TableData.
 */
public class TableData {
    SimpleStringProperty member;
    SimpleDoubleProperty activities;
    SimpleDoubleProperty hours;
    SimpleDoubleProperty cost;
    SimpleStringProperty branch;
    SimpleStringProperty description;
    SimpleStringProperty date;
    SimpleStringProperty tag;
    SimpleStringProperty tagdate;

    TableData(String branch, String description, String date){
        this.branch= new SimpleStringProperty(branch);
        this.description= new SimpleStringProperty(description);
        this.date= new SimpleStringProperty(date);
    }

    TableData(String tag, String tagdate){
        this.tag = new SimpleStringProperty(tag);
        this.tagdate = new SimpleStringProperty(tagdate);
    }

    TableData(String member, Double activities, Double hours, Double cost) {
        this.member = new SimpleStringProperty(member);
        this.activities = new SimpleDoubleProperty(activities);
        this.hours = new SimpleDoubleProperty(hours);
        this.cost = new SimpleDoubleProperty(cost);
    }

    // Getters and setters needed because they're implicitly called.

    /**
     * Gets the member name.
     * @return String Member name.
     */
    public String getMember() {
        return member.get();
    }

    /**
     * Sets the member name to a new value.
     * @param m New member name.
     */
    public void setMember(String m) {
        member.set(m);
    }

    /**
     * Gets the activities.
     * @return Double Activities.
     */
    public Double getActivities() {
        return activities.get();
    }

    /**
     * Sets the activities to a new value.
     * @param e New activities value.
     */
    public void setActivities(Double e) {
        activities.set(e);
    }


    /**
     * Gets the hours.
     * @return Double Hours.
     */
    public Double getHours() {
        return hours.get();
    }

    /**
     * Sets the hours to a new value.
     * @param h New hours values.
     */
    public void setHours(Double h) {
        hours.set(h);
    }

    /**
     * Gets the cost.
     * @return Double Cost.
     */
    public Double getCost() {
        return cost.get();
    }

    /**
     * Sets the cost to a new value.
     * @param c New cost value.
     */
    public void setCost(Double c) {
        cost.set(c);
    }

    /**
     * Gets the date of a commit.
     * @return String Commit date.
     */
    public String getDate() { return date.get();}

    /**
     * Sets the commit date to a new value.
     * @param p New date value.
     */
    public void setDate(String p) {
        date.set(p);
    }

    /**
     * Gets the branch name.
     * @return String Branch name.
     */
    public String getBranch() {
        return branch.get();
    }

    /**
     * Sets the branch name to a new value.
     * @param b New branch name.
     */
    public void setBranch(String b) {
        branch.set(b);
    }

    /**
     * Gets the description of a tag.
     * @return String Tag description.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Sets the tag description to a new value.
     * @param d New tag description.
     */
    public void setDescription(String d) {
        description.set(d);
    }

    /**
     * Gets the name of a tag.
     * @return String Tag name.
     */
    public String getTag() { return tag.get(); }

    /**
     * Sets the tag name to a new value.
     * @param l New tag name.
     */
    public void setTag(String l) {
        tag.set(l);
    }

    /**
     * Gets the date of a tag.
     * @return String Tag date.
     */
    public String getTagdate() { return tagdate.get();}

    /**
     * Sets the tag date to a new value.
     * @param p New tag date.
     */
    public void setTagdate(String p) {
        tagdate.set(p);
    }

}
