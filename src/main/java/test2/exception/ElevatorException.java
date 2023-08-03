package test2.exception;

/**
 * Класс ошибки, в текущей реализации используется только для уведомления пользователя, о том, что он вызвал лифт на несуществующий этаж
 */
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
