package Model;

import static org.junit.jupiter.api.Assertions.*;

class CalculIdiomesTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }


    @org.junit.jupiter.api.Test
    void calcularTestESP_CAT(){
        double d = 0.0;
        CalculIdiomes c = new CalculIdiomes(new Dades(),Idioma.FRA,Idioma.DEU);
        try {
            d = c.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.err.println("POSAR A DADES: "+ d);
        }
    }
}