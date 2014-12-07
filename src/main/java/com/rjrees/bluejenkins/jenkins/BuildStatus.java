package com.rjrees.bluejenkins.jenkins;

/**
 * Created by Gordon on 03/12/2014.
 */
public class BuildStatus {
    private boolean isBuilding;
    private String lastCompletedResult;
    private String name;

    public BuildStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastCompletedResult() {
        return lastCompletedResult;
    }

    public void setLastCompletedResult(String lastCompletedResult) {
        this.lastCompletedResult = lastCompletedResult;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public void setBuilding(boolean isBuilding) {
        this.isBuilding = isBuilding;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name : " + this.getName() + ",");
        sb.append("Building : " + this.isBuilding() + ",");
        sb.append("Last status : " + this.getLastCompletedResult() + "\n");
        return sb.toString();
    }

}
