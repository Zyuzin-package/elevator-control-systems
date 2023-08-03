package test2.models;

import lombok.ToString;

@ToString
public class Elevator {
    private int id;
    private int floorNum = 1;
    private ElevatorStatus elevatorStatus = ElevatorStatus.STANDS_WITH_DOORS_OPEN;
    private boolean isPersonInside = false;
    private int pressedButton;

    public Elevator(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public boolean isPersonInside() {
        return isPersonInside;
    }

    public void setPersonInside(boolean personInside) {
        isPersonInside = personInside;
    }

    public int getPressedButton() {
        return pressedButton;
    }

    public void setPressedButton(int pressedButton) {
        this.pressedButton = pressedButton;
    }

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public void pressFloorBtn(int floorNum) {
        this.pressedButton = floorNum;
    }

    public void pressDoorsClosedBtn() {
        setElevatorStatus(ElevatorStatus.CLOSES_DOORS);
    }

    public void pressDoorsOpenBtn() {
        setElevatorStatus(ElevatorStatus.OPENS_DOORS);
    }

    public void pressDispatcherBtn() {
    }

    public void liftPressurePlateActive() {
        isPersonInside = true;
    }

    public void liftPressurePlateNotActive() {
        isPersonInside = false;
    }


}
