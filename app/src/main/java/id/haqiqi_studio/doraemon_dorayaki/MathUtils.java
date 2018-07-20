package id.haqiqi_studio.doraemon_dorayaki;

import java.util.Random;

public class MathUtils {

    private int genap, ganjil;


    public int generateGanjil () {
        genap = new Random().nextInt(10 - 1) + 1;
        while (genap % 2 != 0) {
            genap = new Random().nextInt(10 - 1) + 1;
        }
        return genap;
    }

    public int generateGenap () {
        genap = new Random().nextInt(10 - 1) + 1;
        while (genap % 2 != 0) {
            genap = new Random().nextInt(10 - 1) + 1;
        }
        return genap;
    }

}
