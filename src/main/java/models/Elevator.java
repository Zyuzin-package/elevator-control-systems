package models;

import service.ElevatorHandler;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    private int floorNum = 1;
    private ElevatorStatus elevatorStatus = ElevatorStatus.STANDS_WITH_DOORS_OPEN;
    ElevatorHandler elevatorHandler;


    public Elevator(ElevatorHandler elevatorHandler) {
        this.elevatorHandler = elevatorHandler;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        if(floorNum > elevatorHandler.getFloors().size() || floorNum < 0){
            System.out.println("No such button");
            return;
        }
        if (floorNum==this.floorNum){
            System.out.println("Elevator stays on the floor " + floorNum);
            return;
        }

        System.out.println("Floor number: " + floorNum);
        this.floorNum = floorNum;
    }

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
    }

    public void pressFloorBtn(int floorNum) {
      elevatorHandler.moveElevatorToFloor(floorNum,this);
    }

    public void pressDoorsClosedBtn() {
        System.out.println("Pressing the button to CLOSE the door");
        elevatorStatus = ElevatorStatus.CLOSES_DOORS;
    }

    public void pressDoorsOpenBtn() {
        System.out.println("Pressing the button to OPEN the door");
        elevatorStatus = ElevatorStatus.OPENS_DOORS;
    }

    public void pressDispatcherBtn() {
        System.out.println("Pressing the button to call the dispatcher");
    }

    public void doorsMovementListener() {

    }

    public void doorsNoMovementListener() {

    }
}
