package test1.service;


import test1.model.Elevator;
import test1.model.ElevatorStatus;
import test1.model.Floor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model {
    final List<Elevator> elevatorList = Collections.synchronizedList(new ArrayList<>());
    public static final List<Floor> floorList = Collections.synchronizedList(new ArrayList<>());

    public static final String RED_TEXT = "\u001B[31m";
    public static final String DEFAULT_TEXT = "\u001B[0m";

    public Model() {
        for (int i = 0; i <= 19; i++) {
            Floor floor = new Floor(i + 1);
            floorList.add(floor);
        }
        elevatorList.add(new Elevator(1));
        elevatorList.add(new Elevator(2));

    }


    public void pressBtnOnFloorAndThenPressOnBtnIntoElevator(int num, int floorNum) throws InterruptedException {
        while (true) {

            for (Elevator el : elevatorList) {
                if (el.getElevatorStatus() == ElevatorStatus.STANDS_WITH_DOORS_OPEN) {
                    el.goToFloor(num);

                    System.out.println(RED_TEXT + "DEBUG| " + DEFAULT_TEXT + "Before press" + elevatorList);
                    System.out.println("Man press to button with num: " + floorNum + " in elev with id: " + el.getId());
                    el.pressFloorBtn(floorNum);
                    System.out.println(RED_TEXT + "DEBUG| " + DEFAULT_TEXT +"After press" + elevatorList);
                    return;
                }
            }

        }
    }

}
