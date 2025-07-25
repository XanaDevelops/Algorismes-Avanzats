package controlador;

import Model.Idioma;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
class MainTest {


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        Main.main(null);

    }

    @BeforeEach
    void setUp() {
    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() throws InterruptedException {
        Thread.sleep(2000);
    }
    @Test
    void calcularTOTS_TOTS(){
        Main.getInstance().calcularTot(false, -1);
    }
    @Test
    void calcularNomesTOTS_L(){
        Main.getInstance().calcular(Idioma.TOTS, Idioma.ESP, -1);
    }
    @Test
    void calcularNomesTOTS_R(){
        Main.getInstance().calcular(Idioma.CAT, Idioma.TOTS, -1);
    }
    @Test
    void calcularESP_CAT() {
        Main.getInstance().calcular(Idioma.ESP, Idioma.CAT, -1);
//        System.out.println(Main.getInstance().getDades().getDistancia(Idioma.ESP, Idioma.CAT));;
    }
}