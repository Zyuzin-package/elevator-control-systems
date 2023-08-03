package test1;

import test1.service.Model;

public class main {
    /**
     * Каждый Runnable, представляет из себя порядок действий пассажира, которые он совершает во время пользования лифтом
     * первый параметр в методе {@code model.pressBtnOnFloorAndThenPressOnBtnIntoElevator(1, 4)}
     * отвечает за нажатие кнопки вызова лифта, на конкретном этаже.
     * Вторая кнопка - за кнопку этажа, которую пассажир нажимает в лифте
     */

    public static void main(String[] args) {
        Model model = new Model();

        Runnable runnable1 = () -> {
            try {
                model.pressBtnOnFloorAndThenPressOnBtnIntoElevator(1, 4);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable runnable2 = () -> {
            try {
                model.pressBtnOnFloorAndThenPressOnBtnIntoElevator(5, 4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };


        Runnable runnable3 = () -> {
            try {
                model.pressBtnOnFloorAndThenPressOnBtnIntoElevator(7, 3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable runnable4 = () -> {
            try {
                model.pressBtnOnFloorAndThenPressOnBtnIntoElevator(10, 5);
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
