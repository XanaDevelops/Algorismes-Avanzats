package Model;

import Model.Calcul.CalculIdiomes;
import controlador.Main;
import org.junit.jupiter.api.BeforeAll;

class CalculIdiomesTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Main.main(new String[]{"test"});
    }
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }


    @org.junit.jupiter.api.Test
    void calcularTestESP_ENG() {
        Dades dades = Main.getInstance().getDades();
        CalculIdiomes c = new CalculIdiomes(Idioma.ESP, Idioma.ENG, -1);

        c.run();
        System.out.println(dades.getDistancia(Idioma.ESP, Idioma.ENG));
    }
}