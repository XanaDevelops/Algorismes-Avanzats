package Model;

import static org.junit.jupiter.api.Assertions.*;

class CalculIdiomesTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }


    @org.junit.jupiter.api.Test
    void calcularTestESP_CAT(){
        double d = 0.0;
        Dades dades = new Dades();
        CalculIdiomes c = new CalculIdiomes(Idioma.FRA,Idioma.DEU);
        try {
            c.run();
            System.out.println( dades.getDistancia(Idioma.FRA, Idioma.DEU));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.err.println("POSAR A DADES: "+ d);
        }
    }
}