package test2;

import test2.service.Model;

public class main {
    /**
     * Каждый вызов метода представляет из себя порядок действий пассажира, которые он совершает во время пользования лифтом
     * первый параметр в методе {@code model.userSimulation(1, 4)}
     * отвечает за нажатие кнопки вызова лифта, на конкретном этаже.
     * второй - за кнопку этажа, которую пассажир нажимает в лифте
     */
    //TODO: Запуск первого и основного метода решения задачи
    public static void main(String[] args){
        Model model = new Model();
        model.userSimulation(100, 4);// вызов для демонстрации работы валидации, и дальнейшего функционирования системы
        model.userSimulation(1,4);
        model.userSimulation(5,100);
    }
}
