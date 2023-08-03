package test2.service;

import test2.exception.ElevatorException;
import test2.models.Elevator;
import test2.models.ElevatorStatus;
import test2.models.Floor;

import java.util.*;

public class Model {
    /**
     * Мапа, содержащая объект лифта, инициализированный в конструкторе,
     * и поток, который в конструкторе задается как null.
     */
    Map<Elevator, Thread> elevator78ThreadMap = Collections.synchronizedMap(new HashMap<>());

    private final List<Floor> floors = Collections.synchronizedList(new ArrayList<>());
    /**
     * Временной промежуток, за который лифт перемещается с одного этажа на другой
     */
    private static final int MOVE_EVENT = 1000;
    /**
     * Временной промежуток, открытия или закрытия двери лифта
     */
    private static final int DOOR_EVENT = 500;
    /**
     * Временной промежуток, за который пассажир заходит в лифт, активируя нажимную пластину в его полу
     *
     * @see Elevator
     */
    private static final int PERSON_MOVES_EVENT = 500;

    /**
     * Общее количество лифтов
     */
    private static final int ELEVATORS_COUNT = 2;
    /**
     * Общее количество этажей
     */
    private static final int FLOORS_COUNT = 20;
    /**
     * Приблизительное время, для ожидания освобождения одного лифта.
     * Добавлено для обхода возможного переполнения стека.
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
     * Метод инициирующий начало работы лифта, передавая туда номера этажи
     * @param floorNumber - Этаж на который был вызван лифт
     * @param pressedButton - Кнопка этажа, на которую нажал пассажир, после того как зашел в лифт
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
     * Поиск свободного лифта
     * @return elevator - лифт найден
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
     * Жизненный цикл лифта. В него входит:
     * движение до этажа на котором пассажир вызвал лифт (1),
     * нажатие на кнопку этажа на который нужно пользователю, (2)
     * движение до пункта назначения (3)
     * завершение работы (4)
     * @param floorNumber - Этаж на который был вызван лифт
     * @param pressedButton - Кнопка этажа, на которую нажал пассажир, после того как зашел в лифт
     * @throws InterruptedException - ошибка пробрасываемая потоками
     * @throws ElevatorException - ошибка, пробрасываемая когда нажатая кнопка выходит за границы допустимого значения этажей
     */
    private void goElevatorToFloor(int floorNumber, int pressedButton) throws InterruptedException, ElevatorException {
        Thread.sleep(SOF_PROTECTION);// защита от переполнения стека (StackOverFlowError)
        Elevator elevator = getFreeElevator();// поиск свободного лифта

        if (elevator != null) { // Если есть свободный лифт
            if (floorNumber > FLOORS_COUNT || floorNumber <= 0) {
                throw new ElevatorException("Floor with number: " + floorNumber + " going outside the building", 1);
            }
            runIfThreadNotActive(elevator, () -> {
                try {
                    floors.get(floorNumber - 1).btnPress();// Имитация нажатия на кнопку вызова лифта на этаже
                    if (elevator.getFloorNum() != floorNumber) {
                        closeDoor(elevator);
                        moveElevatorToFloor(elevator, floorNumber);//(1)
                        openDoor(elevator);
                    }
                    floors.get(floorNumber - 1).setBtnStats(false);// Отжатие кнопки вызова

                    Thread.sleep(PERSON_MOVES_EVENT);// Пассажир заходит в лифт
                    elevator.liftPressurePlateActive();// Имитаци активации нажимной плиты в лифте
                    elevator.pressFloorBtn(pressedButton);// Пассажир нажимает кнопку в лифте
                    pressFloorButtonIntoElevatorEvent(elevator);// Имитация нажатия кнопки в лифте, и дальнейшее движение до пункта назначения (2,3)
                    openDoor(elevator);// Открытие дверей лифта

                    Thread.sleep(PERSON_MOVES_EVENT);// Пассажир выходит из лифта

                    deActivatedThread(elevator);// Работа лифта выполнена, он освобождается для следующей задачи (4)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ElevatorException e) {
                    throw new RuntimeException(e);
                }
            });
        } else { // Иначе рекурсивно вызываем текущий метод, до того момента пока один из лифтов не осввободится
            goElevatorToFloor(floorNumber, pressedButton);
        }
    }

    /**
     * Реакция системы на нажатие кнопки выбора этажа в лифте и дальнейшее передвижение лифта на выбранный этаж
     * @param elevator - лифт, в котором была нажата кнопка
     * @throws ElevatorException - ошибка, пробрасываемая когда нажатая кнопка выходит за границы допустимого значения этажей
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
     * Функция, перемещающая лифт на заданную позицию.
     * После изменения позиции лифта на один этаж, поток высвобождается, что-бы другие лифты, так-же смогли двигаться
     * @param elevator - лифт, который перемещается на нужный этаж
     * @param newFloor - этаж, на который нужно переместить лифт
     */
    public void moveElevatorToFloor(Elevator elevator, int newFloor) throws InterruptedException {
        int elevatorFloorNum = elevator.getFloorNum();
        if (newFloor < elevatorFloorNum) {// Определение в какую сторону движется лифт
            updateElevatorStatus(elevator, ElevatorStatus.RIDES_DOWN);
            for (int i = elevatorFloorNum; i >= newFloor; i--) {
                System.out.println("Move down elevator with id: " + elevator.getId() + " to " + i + " floor");
                elevator.setFloorNum(i);

                updateDataAtFloors();

                Thread.sleep(MOVE_EVENT);
                Thread.yield();
            }
            //printFloorsState(); // Вывод данных с табло каждого этажа. Закомментировано, для более читаемого отображения движения лифтов
        } else if (newFloor > elevatorFloorNum) {
            updateElevatorStatus(elevator, ElevatorStatus.RIDES_UP);
            for (int i = elevatorFloorNum; i <= newFloor; i++) {
                System.out.println("Move up elevator with id: " + elevator.getId() + " to " + i + " floor");
                elevator.setFloorNum(i);

                updateDataAtFloors();

                Thread.sleep(MOVE_EVENT);
                Thread.yield();
            }
            //printFloorsState(); // Вывод данных с табло каждого этажа. Закомментировано, для более читаемого отображения движения лифтов
        }

    }

    private void updateElevatorStatus(Elevator elevator, ElevatorStatus elevatorStatus) {
        updateDataAtFloors();
        elevator.setElevatorStatus(elevatorStatus);
        printElevatorState(elevator);
    }

    /**
     * Проверка на занятость потока у лифта, и выполнения последовательность команд
     * @param elevator - лифт у которого нужно выполнить код
     * @param block - последовательность команд
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
     * Обновление информации о лифтах у списка этажей
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
