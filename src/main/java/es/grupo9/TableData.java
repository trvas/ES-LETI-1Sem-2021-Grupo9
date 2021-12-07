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
     * Gets the name of a member.
     * @return String member name.
     */
    public String getMember() {
        return member.get();
    }

    /**
     * @param m the name of a member.
     * Gets the name of a member.
     */
    public void setMember(String m) {
        member.set(m);
    }

    /**
     * Gets the activities.
     * @return Double activities.
     */
    public Double getActivities() {
        return activities.get();
    }

    /**
     * @param e the activities.
     * Get the activities.
     */
    public void setActivities(Double e) {
        activities.set(e);
    }


    /**
     * Gets the hours.
     * @return Double hours.
     */
    public Double getHours() {
        return hours.get();
    }

    /**
     * @param h the hours.
     * Set the hours.
     */
    public void setHours(Double h) {
        hours.set(h);
    }

    /**
     * Gets the cost.
     * @return Double cost.
     */
    public Double getCost() {
        return cost.get();
    }

    /**
     * @param c the cost.
     * Sets the cost.
     */
    public void setCost(Double c) {
        cost.set(c);
    }

    /**
     * Gets the date of a commit.
     * @return String commit date.
     */
    public String getDate() { return date.get();}

    /**
     * @param p the date.
     * Sets the date of a commit.
     */
    public void setDate(String p) {
        date.set(p);
    }

    /**
     * Gets the name of a branch.
     * @return String branch name.
     */
    public String getBranch() {
        return branch.get();
    }

    /**
     * @param b the branch name.
     * Sets the branch name.
     */
    public void setBranch(String b) {
        branch.set(b);
    }

    /**
     * Gets the description of a tag.
     * @return String description of a tag.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * @param d the description.
     * Sets the description of a tag.
     */
    public void setDescription(String d) {
        description.set(d);
    }

    /**
     * Gets the name of a tag.
     * @return String tag name.
     */
    public String getTag() { return tag.get(); }

    /**
     * @param l the name of a tag.
     * Sets name of a tag.
     */
    public void setTag(String l) {
        tag.set(l);
    }

    /**
     * Gets the date of a tag.
     * @return String tag date.
     */
    public String getTagdate() { return tagdate.get();}

    /**
     * @param p the date of a tag.
     * Sets the date of a tag.
     */
    public void setTagdate(String p) {
        tagdate.set(p);
    }

}
