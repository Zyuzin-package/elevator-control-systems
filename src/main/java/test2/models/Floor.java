package test2.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Floor {
    private int floorNum;
    /**
     * ������ ����������, ��� ������ �������� ����� �� ������� ����� ��������� � ��:
     * {@code
     * ...
     *  ����
     *  ��������:
     *  1. ������� ���� ������ 1
     *  2. ������� ������ ������ 1
     *  3. ������� ���� ������ 2
     *  4. ������� ������ ������ 2
     *   ...
     * }
     * <br>
     * ������ ���� ������ ��� �������� ���������� � �����{@code Elevator} (��� ���������) � ������ ����� {@code Integer}.
     * ������������ ��� ������ ���������� � ������ �����
     * @see test2.service.Model
     */
    private Map<Elevator,Integer> elevatorFloor =  Collections.synchronizedMap(new HashMap<>());

    private volatile boolean btnStats = false;


    public Floor(int floorNum) {
        this.floorNum = floorNum;
    }

    public void btnPress(){
        System.out.println("Pressed btn on floor: " + floorNum);
        btnStats=true;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public void updateData(Elevator elevator){
        elevatorFloor.put(elevator,elevator.getFloorNum());
    }

    public Map<Elevator, Integer> getElevatorFloor() {
        return elevatorFloor;
    }

    public void setElevatorFloor(Map<Elevator, Integer> elevatorFloor) {
        this.elevatorFloor = elevatorFloor;
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
                ", elevatorFloor=" + elevatorFloor +
                ", btnStats=" + btnStats +
                '}';
    }
}
