package test2;

import test2.service.Model;

public class main {
    /**
     * ������ ����� ������ ������������ �� ���� ������� �������� ���������, ������� �� ��������� �� ����� ����������� ������
     * ������ �������� � ������ {@code model.userSimulation(1, 4)}
     * �������� �� ������� ������ ������ �����, �� ���������� �����.
     * ������ - �� ������ �����, ������� �������� �������� � �����
     */
    //TODO: ������ ������� � ��������� ������ ������� ������
    public static void main(String[] args){
        Model model = new Model();
        model.userSimulation(100, 4);// ����� ��� ������������ ������ ���������, � ����������� ���������������� �������
        model.userSimulation(1,4);
        model.userSimulation(5,100);
    }
}
