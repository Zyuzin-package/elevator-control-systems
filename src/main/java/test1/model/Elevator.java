package test1.model;

import lombok.ToString;

import static test1.service.Model.*;

@ToString
public class Elevator  {
    private int id;
    private volatile int floorNum = 1;
    private volatile ElevatorStatus elevatorStatus = ElevatorStatus.STANDS_WITH_DOORS_OPEN;

    public Elevator(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void move() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
       // System.out.println("Floor number " + floorNum + " for elevator with id: " + id);
        this.floorNum = floorNum;
    }

    public ElevatorStatus getElevatorStatus() {
        return elevatorStatus;
    }

    public void setElevatorStatus(ElevatorStatus elevatorStatus) {
        if (elevatorStatus != this.elevatorStatus) {
            System.out.println("Now elevator with id " + id + " status: " + elevatorStatus.name());
            this.elevatorStatus = elevatorStatus;
            updateFloorsList();
        } else {
            System.out.println("Elevator with id: " + id + " hase already status: " + elevatorStatus.name());
        }
    }

    public void pressFloorBtn(int newFloor) {
        if (elevatorStatus != ElevatorStatus.RIDES_DOWN || elevatorStatus != ElevatorStatus.RIDES_UP) {
            goToFloor(newFloor);
        } else {
            Thread.yield();
        }
    }

    public void goToFloor(int newFloor) {
//        if (newFloor != floorNum) {
            synchronized (this) {
                setElevatorStatus(ElevatorStatus.CLOSES_DOORS);
                if (newFloor < floorNum) {
                    setElevatorStatus(ElevatorStatus.RIDES_DOWN);
//                    System.out.println(RED_TEXT + "DEBUG korka| " + DEFAULT_TEXT +floorList.get(newFloor).toString());
//                    floorList.get(newFloor).setBtnStats(false);
//                    System.out.println(RED_TEXT + "DEBUG korka| " + DEFAULT_TEXT +floorList.get(newFloor).toString());
                    for (int i = floorNum; i >= newFloor; i--) {
                        move();
                        System.out.println("Move down elevator with id: " + id + " to " + i + " floor");
                        setFloorNum(i);
                       // System.out.println(RED_TEXT + "DEBUG| " + DEFAULT_TEXT +"floorList: " + floorList);
                        Thread.yield();
                    }
                } else if (newFloor > floorNum) {
                    setElevatorStatus(ElevatorStatus.RIDES_UP);
                    for (int i = floorNum; i <= newFloor; i++) {
                        move();
                        System.out.println("Move up elevator with id: " + id + " to " + i + " floor");
                        setFloorNum(i);
                       // System.out.println(RED_TEXT + "DEBUG| " + DEFAULT_TEXT +"floorList: " + floorList);
                        Thread.yield();
                    }
                }

                System.out.println("Elevator with id: " + id + " has arrived on floor: " + newFloor + "\n");
                setElevatorStatus(ElevatorStatus.STANDS_WITH_DOORS_OPEN);
            }
//        } else {
//            setElevatorStatus(ElevatorStatus.STANDS_WITH_DOORS_OPEN);
//        }
    }


    public synchronized void updateFloorsList() {
        for (Floor f : floorList) {
            if (id == 1) {
                f.setElevator1Floor(floorNum);
                f.setElevator1Status(elevatorStatus);
            } else if (id == 2) {
                f.setElevator2Floor(floorNum);
                f.setElevator2Status(elevatorStatus);
            }
        }

    }

    public void pressDoorsClosedBtn() {
        System.out.println("Pressing the button to CLOSE the door");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setElevatorStatus(ElevatorStatus.CLOSES_DOORS);
    }

    public void pressDoorsOpenBtn() {
        System.out.println("Pressing the button to OPEN the door");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setElevatorStatus(ElevatorStatus.OPENS_DOORS);
    }

    public void pressDispatcherBtn() {
        System.out.println("Pressing the button to call the dispatcher");
    }

    public void doorsMovementListener() {

    }

    public void doorsNoMovementListener() {

    }


}
