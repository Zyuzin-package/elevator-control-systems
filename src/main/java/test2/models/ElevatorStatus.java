package test2.models;

public enum ElevatorStatus {
    RIDES_UP("Elevator rides down"),
    RIDES_DOWN("Elevator rides up"),
    OPENS_DOORS("Elevator opens doors"),
    CLOSES_DOORS("Elevator closes doors"),
    STANDS_WITH_DOORS_OPEN("Elevator stands with doors open"),
    STANDS_WITH_DOORS_CLOSED("Elevator stands with doors closed");
    private final String stats;

    ElevatorStatus(String string) {
        this.stats = string;
    }

    public String getStats() {
        return stats;
    }

}
