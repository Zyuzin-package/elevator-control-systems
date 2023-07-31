import models.Elevator;
import models.Floor;
import service.ElevatorHandler;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        ElevatorHandler elevatorHandler = new ElevatorHandler();
        List<Floor> floors = new ArrayList<>();
        for (int i =0; i<=19;i++){
            floors.add(new Floor(i));
        }
        elevatorHandler.setFloors(floors);

        Elevator elevator1 = new Elevator(elevatorHandler);

        elevator1.pressFloorBtn(6);
        elevator1.pressFloorBtn(2);
        elevator1.pressFloorBtn(12);
        elevator1.pressFloorBtn(45);
    }
}
