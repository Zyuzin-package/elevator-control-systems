package test2.service;

import test2.exception.ElevatorException;
import test2.models.Elevator;
import test2.models.ElevatorStatus;
import test2.models.Floor;

import java.util.*;

public class Model {
    Map<Elevator, Thread> elevator78ThreadMap = Collections.synchronizedMap(new HashMap<>());

    private final List<Elevator> elevators = Collections.synchronizedList(new ArrayList<>());
    private final List<Floor> floors = Collections.synchronizedList(new ArrayList<>());

    private static final int MOVE_EVENT = 1000;

    private static final int DOOR_EVENT = 500;

    private static final int PERSON_MOVES_EVENT = 500;

    private static final int ELEVATORS_COUNT = 2;

    private static final int FLOORS_COUNT = 20;

    public Model() {
        for (int i = 0; i <= ELEVATORS_COUNT - 1; i++) {
            elevators.add(new Elevator(i + 1));
        }

        for (Elevator e : elevators) {
            elevator78ThreadMap.put(e, null);
        }

        for (int i = 0; i <= FLOORS_COUNT - 1; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public void userSimulation(int floorNumber, int pressedButton) {
        try {
            goElevatorToFloor(floorNumber, pressedButton);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ElevatorException e) {
            e.printStackTrace();
        }
    }

    public Elevator getFreeElevator() {
        for (Elevator elevator : elevator78ThreadMap.keySet()) {
            if (elevator.getElevatorStatus() == ElevatorStatus.STANDS_WITH_DOORS_OPEN) {
                return elevator;
            }
        }
        return null;
    }

    private void goElevatorToFloor(int floorNumber, int pressedButton) throws InterruptedException, ElevatorException {
        Thread.sleep(500);//SOF protection
        Elevator elevator = getFreeElevator();

        if (elevator != null) {
            if (floorNumber > 20 || floorNumber < 0) {
                throw new ElevatorException("Floor with number: " + floorNumber + " going outside the building", 1);
            }
            runIfThreadNotActive(elevator, () -> {
                try {
                    floors.get(floorNumber - 1).btnPress();
                    if (elevator.getFloorNum() != floorNumber) {
                        updateElevatorStatus(elevator, ElevatorStatus.CLOSES_DOORS);
                        Thread.sleep(DOOR_EVENT);
                        updateElevatorStatus(elevator, ElevatorStatus.STANDS_WITH_DOORS_CLOSED);
                        moveElevatorToFloor(elevator, floorNumber);
                        updateElevatorStatus(elevator, ElevatorStatus.OPENS_DOORS);
                        updateElevatorStatus(elevator, ElevatorStatus.STANDS_WITH_DOORS_OPEN);
                    }
                    floors.get(floorNumber).setBtnStats(false);
                    Thread.sleep(PERSON_MOVES_EVENT);// Person go into elevator
                    elevator.liftPressurePlateActive();
                    elevator.pressFloorBtn(pressedButton);
                    pressFloorButtonIntoElevatorEvent(elevator);
                    updateElevatorStatus(elevator, ElevatorStatus.OPENS_DOORS);
                    updateElevatorStatus(elevator, ElevatorStatus.STANDS_WITH_DOORS_OPEN);

                    Thread.sleep(PERSON_MOVES_EVENT);// Person go into elevator

                    deActivatedThread(elevator);//end work Elevator
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } else {
            goElevatorToFloor(floorNumber, pressedButton);//wait when one elevator end his work
        }
    }

    private void pressFloorButtonIntoElevatorEvent(Elevator elevator) throws InterruptedException {
        updateElevatorStatus(elevator, ElevatorStatus.CLOSES_DOORS);
        Thread.sleep(DOOR_EVENT);
        updateElevatorStatus(elevator, ElevatorStatus.STANDS_WITH_DOORS_CLOSED);
        while (elevator.isPersonInside()) {
            if (elevator.getPressedButton() != 0) {
                moveElevatorToFloor(elevator, elevator.getPressedButton());
                Thread.sleep(PERSON_MOVES_EVENT);
                elevator.liftPressurePlateNotActive();
                break;
            }
        }

    }

    public void moveElevatorToFloor(Elevator elevator, int newFloor) throws InterruptedException {

        if (newFloor < elevator.getFloorNum()) {
            updateElevatorStatus(elevator, ElevatorStatus.RIDES_DOWN);
            for (int i = elevator.getFloorNum(); i >= newFloor; i--) {
                System.out.println("Move down elevator with id: " + elevator.getId() + " to " + i + " floor");
                elevator.setFloorNum(i);

                updateDataAtFloors();

                Thread.sleep(MOVE_EVENT);
                Thread.yield();
            }
            //printFloorsState(); // out data about floor
        } else if (newFloor > elevator.getFloorNum()) {
            updateElevatorStatus(elevator, ElevatorStatus.RIDES_UP);
            for (int i = elevator.getFloorNum(); i <= newFloor; i++) {
                System.out.println("Move up elevator with id: " + elevator.getId() + " to " + i + " floor");
                elevator.setFloorNum(i);

                updateDataAtFloors();

                Thread.sleep(MOVE_EVENT);
                Thread.yield();
            }
            //printFloorsState(); // out data about floor
        }

    }

    public void updateElevatorStatus(Elevator elevator, ElevatorStatus elevatorStatus) {
        elevator.setElevatorStatus(elevatorStatus);
        printElevatorState(elevator);
    }

    private void runIfThreadNotActive(Elevator elevator, Runnable block) throws InterruptedException {
        Thread currentThread = elevator78ThreadMap.get(elevator);
        if (currentThread == null || currentThread.isInterrupted()) {
            currentThread = new Thread(block);
            currentThread.start();
            elevator78ThreadMap.replace(elevator, currentThread);
        }
    }

    private synchronized void updateDataAtFloors() {
        for (Floor floor : floors) {
            for (Elevator elevator : elevator78ThreadMap.keySet()) {
                floor.updateData(elevator);
            }
        }
    }

    private void deActivatedThread(Elevator elevator) {
        elevator78ThreadMap.get(elevator).interrupt();
    }

    private synchronized void printElevatorState(Elevator elevator) {
        System.out.println("Elevator with id " + elevator.getId() + " status: " + elevator.getElevatorStatus() + " floor: " + elevator.getFloorNum());
    }

    private synchronized void printFloorsState() {
        for (Floor floor : floors) {
            StringBuilder result = new StringBuilder("The floor with number "
                    + floor.getFloorNum()
                    + " has the status of elevators:\n");
            for (Elevator elevator : floor.getElevatorFloor().keySet()) {
                result.append("Elevator with num: ")
                        .append(elevator.getId())
                        .append(" have status: ")
                        .append(elevator.getElevatorStatus())
                        .append(" and is located on floor with number: ")
                        .append(floor.getElevatorFloor().get(elevator))
                        .append("and then button pressed status is: ")
                        .append(floor.isBtnStats())
                        .append(" ");
            }
            System.out.println(result);
        }

    }

}
