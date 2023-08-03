package test1;

import test1.service.Handler;

public class main {
    /**
     * ������ Runnable ������������ �� ���� ������� �������� ���������, ������� �� ��������� �� ����� ����������� ������
     * ������ �������� � ������ {@code model.pressBtnOnFloorAndThenPressOnBtnIntoElevator(1, 4)}
     * �������� �� ������� ������ ������ �����, �� ���������� �����.
     * ������ - �� ������ �����, ������� �������� �������� � �����
     */
    //TODO: ������ ������� �������� ������� ������
    public static void main(String[] args) {
        Handler handler = new Handler();

        Runnable runnable1 = () -> {
            try {
                handler.pressBtnOnFloorAndThenPressOnBtnIntoElevator(1, 4);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable runnable2 = () -> {
            try {
                handler.pressBtnOnFloorAndThenPressOnBtnIntoElevator(5, 4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };


        Runnable runnable3 = () -> {
            try {
                handler.pressBtnOnFloorAndThenPressOnBtnIntoElevator(7, 3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable runnable4 = () -> {
            try {
                handler.pressBtnOnFloorAndThenPressOnBtnIntoElevator(10, 5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);
        Thread thread4 = new Thread(runnable4);

        try {
            thread1.start();
            Thread.sleep(500);

            thread2.start();
            Thread.sleep(500);

            thread3.start();
            Thread.sleep(500);

            thread4.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
