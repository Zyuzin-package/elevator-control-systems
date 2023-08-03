package test2;

import test2.service.Model;

public class main {
    public static void main(String[] args){
        Model model = new Model();
        model.userSimulation(100, 4);
        //Thread.sleep(500);
        model.userSimulation(5,4);
       // Thread.sleep(500);
        model.userSimulation(7,8);
    }
}
