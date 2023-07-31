package service;

import models.Elevator;
import models.ElevatorStatus;
import models.Floor;

import java.util.ArrayList;
import java.util.List;

public class ElevatorHandler {
    List<Floor> floors = new ArrayList<>();

    public ElevatorHandler() {
        for (int i =0; i<=20;i++){
            floors.add(new Floor(i));
        }
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void moveElevatorToFloor(int floorNum, Elevator elevator) {
        int elevFloor = elevator.getFloorNum();

        if (floorNum < elevFloor) {
            for (int i = elevFloor; i >= floorNum; i--) {
                System.out.println("Move down elevator to " + i + " floor");
                elevator.setFloorNum(i);
            }
        } else if (floorNum > elevFloor) {
            for (int i = elevFloor; i <= floorNum; i++) {
                System.out.println("Move up elevator to " + i + " floor");
                elevator.setFloorNum(i);
            }
        }
    }

}
