package model;

import controlador.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XarxaSolverTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Main.main(null);
    }

    @Test
    public void loadImatges(){
        new XarxaSolver();
    }

}