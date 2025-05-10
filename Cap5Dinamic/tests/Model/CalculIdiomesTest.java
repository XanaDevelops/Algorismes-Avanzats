package Model;

import static org.junit.jupiter.api.Assertions.*;

class CalculIdiomesTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }


    @org.junit.jupiter.api.Test
    void calcularTestESP_CAT(){
        CalculIdiomes c = new CalculIdiomes();
        double d = c.calcularDistanciaIdiomes(Idioma.ESP, Idioma.CAT);
    }
}