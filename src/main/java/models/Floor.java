package models;

public class Floor {
    private int floorNum;

    private int elevator1Floor = 1;
    private ElevatorStatus elevator1Status = ElevatorStatus.STANDS_WITH_DOORS_OPEN;

    private int elevator2Floor = 1;
    private ElevatorStatus elevator2Status = ElevatorStatus.STANDS_WITH_DOORS_OPEN;

    private boolean btnStats = false;
    public Floor(int floorNum) {
        this.floorNum = floorNum;
    }

    public void btnPress(){
        btnStats=true;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getElevator1Floor() {
        return elevator1Floor;
    }

    public void setElevator1Floor(int elevator1Floor) {
        this.elevator1Floor = elevator1Floor;
    }

    public ElevatorStatus getElevator1Status() {
        return elevator1Status;
    }

    public void setElevator1Status(ElevatorStatus elevator1Status) {
        this.elevator1Status = elevator1Status;
    }

    public int getElevator2Floor() {
        return elevator2Floor;
    }

    public void setElevator2Floor(int elevator2Floor) {
        this.elevator2Floor = elevator2Floor;
    }

    public ElevatorStatus getElevator2Status() {
        return elevator2Status;
    }

    public void setElevator2Status(ElevatorStatus elevator2Status) {
        this.elevator2Status = elevator2Status;
    }

    public boolean isBtnStats() {
        return btnStats;
    }

    public void setBtnStats(boolean btnStats) {
        this.btnStats = btnStats;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "floorNum=" + floorNum +
                ", elevator1Floor=" + elevator1Floor +
                ", elevator1Status=" + elevator1Status +
                ", elevator2Floor=" + elevator2Floor +
                ", elevator2Status=" + elevator2Status +
                "}\n";
    }
}
