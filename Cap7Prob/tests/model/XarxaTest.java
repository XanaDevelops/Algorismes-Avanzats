package model;

import controlador.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class XarxaTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Main.main(new String[0]);
    }

    @org.junit.jupiter.api.Test
    void predecir() {
        Xarxa x = new Xarxa(1, new int[]{3,3}, 1);
        System.out.println(Arrays.toString(x.predecir(new double[]{1.0})));
    }

    @Test
    void entrenar(){
        Xarxa x = new Xarxa(1, new int[]{3,3}, 1);
        x.entrenar(new double[][]{{1.0}, {0.5}}, new double[][]{{0.7}, {0.4}}, 1);
    }
}