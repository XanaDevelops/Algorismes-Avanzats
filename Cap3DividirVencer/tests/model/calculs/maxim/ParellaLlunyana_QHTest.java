package model.calculs.maxim;

import model.punts.Punt;
import model.punts.Punt2D;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParellaLlunyana_QHTest {

    ParellaLlunyana_CH p;
    final List<Punt> punts = new ArrayList<Punt>();
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        punts.add(new Punt2D(1,0));
        punts.add(new Punt2D(0,4));
        punts.add(new Punt2D(2,4));
        punts.add(new Punt2D(6,3));
        punts.add(new Punt2D(5,2));
        punts.add(new Punt2D(3,3));

        p = new ParellaLlunyana_CH(punts);
    }

    @org.junit.jupiter.api.Test
    void testRun() {
        p.run();

        assertTrue(true);
    }
}