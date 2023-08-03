package test2.service;

import test2.exception.ElevatorException;
import test2.models.Elevator;
import test2.models.ElevatorStatus;
import test2.models.Floor;

import java.util.*;

public class Model {
    /**
     * ����, ���������� ������ �����, ������������������ � ������������,
     * � �����, ������� � ������������ �������� ��� null.
     */
    Map<Elevator, Thread> elevator78ThreadMap = Collections.synchronizedMap(new HashMap<>());

    private final List<Floor> floors = Collections.synchronizedList(new ArrayList<>());
    /**
     * ��������� ����������, �� ������� ���� ������������ � ������ ����� �� ������
     */
    private static final int MOVE_EVENT = 1000;
    /**
     * ��������� ����������, �������� ��� �������� ����� �����
     */
    private static final int DOOR_EVENT = 500;
    /**
     * ��������� ����������, �� ������� �������� ������� � ����, ��������� �������� �������� � ��� ����
     *
     * @see Elevator
     */
    private static final int PERSON_MOVES_EVENT = 500;

    /**
     * ����� ���������� ������
     */
    private static final int ELEVATORS_COUNT = 2;
    /**
     * ����� ���������� ������
     */
    private static final int FLOORS_COUNT = 20;
    /**
     * ��������������� �����, ��� �������� ������������ ������ �����.
     * ��������� ��� ������ ���������� ������������ �����.
     */
    private static final int SOF_PROTECTION = MOVE_EVENT + (DOOR_EVENT * 2) + (PERSON_MOVES_EVENT * 2) / ELEVATORS_COUNT;

    public Model() {
        List<Elevator> elevators = new ArrayList<>();
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

    /**
     * ����� ������������ ������ ������ �����, ��������� ���� ������ �����
     * @param floorNumber - ���� �� ������� ��� ������ ����
     * @param pressedButton - ������ �����, �� ������� ����� ��������, ����� ���� ��� ����� � ����
     */
    public void userSimulation(int floorNumber, int pressedButton) {
        try {
            goElevatorToFloor(floorNumber, pressedButton);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ElevatorException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����� ���������� �����
     * @return elevator - ���� ������
     */
    public Elevator getFreeElevator() {
        for (Elevator elevator : elevator78ThreadMap.keySet()) {
            if (elevator.getElevatorStatus() == ElevatorStatus.STANDS_WITH_DOORS_OPEN) {
                return elevator;
            }
        }
        return null;
    }

    /**
     * ��������� ���� �����. � ���� ������:
     * �������� �� ����� �� ������� �������� ������ ���� (1),
     * ������� �� ������ ����� �� ������� ����� ������������, (2)
     * �������� �� ������ ���������� (3)
     * ���������� ������ (4)
     * @param floorNumber - ���� �� ������� ��� ������ ����
     * @param pressedButton - ������ �����, �� ������� ����� ��������, ����� ���� ��� ����� � ����
     * @throws InterruptedException - ������ �������������� ��������
     * @throws ElevatorException - ������, �������������� ����� ������� ������ ������� �� ������� ����������� �������� ������
     */
    private void goElevatorToFloor(int floorNumber, int pressedButton) throws InterruptedException, ElevatorException {
        Thread.sleep(SOF_PROTECTION);// ������ �� ������������ ����� (StackOverFlowError)
        Elevator elevator = getFreeElevator();// ����� ���������� �����

        if (elevator != null) { // ���� ���� ��������� ����
            if (floorNumber > FLOORS_COUNT || floorNumber <= 0) {
                throw new ElevatorException("Floor with number: " + floorNumber + " going outside the building", 1);
            }
            runIfThreadNotActive(elevator, () -> {
                try {
                    floors.get(floorNumber - 1).btnPress();// �������� ������� �� ������ ������ ����� �� �����
                    if (elevator.getFloorNum() != floorNumber) {
                        closeDoor(elevator);
                        moveElevatorToFloor(elevator, floorNumber);//(1)
                        openDoor(elevator);
                    }
                    floors.get(floorNumber - 1).setBtnStats(false);// ������� ������ ������

                    Thread.sleep(PERSON_MOVES_EVENT);// �������� ������� � ����
                    elevator.liftPressurePlateActive();// ������� ��������� �������� ����� � �����
                    elevator.pressFloorBtn(pressedButton);// �������� �������� ������ � �����
                    pressFloorButtonIntoElevatorEvent(elevator);// �������� ������� ������ � �����, � ���������� �������� �� ������ ���������� (2,3)
                    openDoor(elevator);// �������� ������ �����

                    Thread.sleep(PERSON_MOVES_EVENT);// �������� ������� �� �����

                    deActivatedThread(elevator);// ������ ����� ���������, �� ������������� ��� ��������� ������ (4)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ElevatorException e) {
                    throw new RuntimeException(e);
                }
            });
        } else { // ����� ���������� �������� ������� �����, �� ���� ������� ���� ���� �� ������ �� ������������
            goElevatorToFloor(floorNumber, pressedButton);
        }
    }

    /**
     * ������� ������� �� ������� ������ ������ ����� � ����� � ���������� ������������ ����� �� ��������� ����
     * @param elevator - ����, � ������� ���� ������ ������
     * @throws ElevatorException - ������, �������������� ����� ������� ������ ������� �� ������� ����������� �������� ������
     */
    private void pressFloorButtonIntoElevatorEvent(Elevator elevator) throws InterruptedException, ElevatorException {
        closeDoor(elevator);
        while (elevator.isPersonInside()) {
            int numOfPressedButton = elevator.getPressedButton();
            if (numOfPressedButton != 0) {
                if (numOfPressedButton > FLOORS_COUNT || numOfPressedButton <= 0) {
                    throw new ElevatorException("Floor with number: " + numOfPressedButton + " going outside the building", 1);
                }
                moveElevatorToFloor(elevator, elevator.getPressedButton());
                Thread.sleep(PERSON_MOVES_EVENT);
                elevator.liftPressurePlateNotActive();
                break;
            }
        }

    }

    private void closeDoor(Elevator elevator) throws InterruptedException {
        updateElevatorStatus(elevator, ElevatorStatus.CLOSES_DOORS);
        Thread.sleep(DOOR_EVENT);
        updateElevatorStatus(elevator, ElevatorStatus.STANDS_WITH_DOORS_CLOSED);
    }

    private void openDoor(Elevator elevator) throws InterruptedException {
        updateElevatorStatus(elevator, ElevatorStatus.OPENS_DOORS);
        Thread.sleep(DOOR_EVENT);
        updateElevatorStatus(elevator, ElevatorStatus.STANDS_WITH_DOORS_OPEN);
    }

    /**
     * �������, ������������ ���� �� �������� �������.
     * ����� ��������� ������� ����� �� ���� ����, ����� ��������������, ���-�� ������ �����, ���-�� ������ ���������
     * @param elevator - ����, ������� ������������ �� ������ ����
     * @param newFloor - ����, �� ������� ����� ����������� ����
     */
    public void moveElevatorToFloor(Elevator elevator, int newFloor) throws InterruptedException {
        int elevatorFloorNum = elevator.getFloorNum();
        if (newFloor < elevatorFloorNum) {// ����������� � ����� ������� �������� ����
            updateElevatorStatus(elevator, ElevatorStatus.RIDES_DOWN);
            for (int i = elevatorFloorNum; i >= newFloor; i--) {
                System.out.println("Move down elevator with id: " + elevator.getId() + " to " + i + " floor");
                elevator.setFloorNum(i);

                updateDataAtFloors();

                Thread.sleep(MOVE_EVENT);
                Thread.yield();
            }
            //printFloorsState(); // ����� ������ � ����� ������� �����. ����������������, ��� ����� ��������� ����������� �������� ������
        } else if (newFloor > elevatorFloorNum) {
            updateElevatorStatus(elevator, ElevatorStatus.RIDES_UP);
            for (int i = elevatorFloorNum; i <= newFloor; i++) {
                System.out.println("Move up elevator with id: " + elevator.getId() + " to " + i + " floor");
                elevator.setFloorNum(i);

                updateDataAtFloors();

                Thread.sleep(MOVE_EVENT);
                Thread.yield();
            }
            //printFloorsState(); // ����� ������ � ����� ������� �����. ����������������, ��� ����� ��������� ����������� �������� ������
        }

    }

    private void updateElevatorStatus(Elevator elevator, ElevatorStatus elevatorStatus) {
        updateDataAtFloors();
        elevator.setElevatorStatus(elevatorStatus);
        printElevatorState(elevator);
    }

    /**
     * �������� �� ��������� ������ � �����, � ���������� ������������������ ������
     * @param elevator - ���� � �������� ����� ��������� ���
     * @param block - ������������������ ������
     */
    private void runIfThreadNotActive(Elevator elevator, Runnable block) throws InterruptedException {
        Thread currentThread = elevator78ThreadMap.get(elevator);
        if (currentThread == null || currentThread.isInterrupted()) {
            currentThread = new Thread(block);
            currentThread.start();
            elevator78ThreadMap.replace(elevator, currentThread);
        }
    }

    /**
     * ���������� ���������� � ������ � ������ ������
     */
    private synchronized void updateDataAtFloors() {
        for (Floor floor : floors) {
            for (Elevator elevator : elevator78ThreadMap.keySet()) {
                floor.updateData(elevator);
            }
        }
    }
    
    private void deActivatedThread(Elevator elevator) throws InterruptedException {
        elevator78ThreadMap.get(elevator).join();
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
