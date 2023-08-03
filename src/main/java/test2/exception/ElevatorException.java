package test2.exception;

public class ElevatorException extends Exception{
    private int number;

    public ElevatorException(String message, int number) {
        super(message);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
