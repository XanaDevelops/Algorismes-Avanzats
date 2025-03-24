package model.solvers;

// Importació de les classes necessàries
import model.Dades;
import model.Tipus;
import principal.Comunicar;
import principal.Main;

import java.util.Arrays;

/**
 * Classe encarregada de generar un Triangle de Sierpinski mitjançant recursió.
 * Implementa Runnable per executar-se en un fil separat i Comunicar per interactuar amb el controlador.
 */
public class SierpinskiTriangleSolver extends RecursiveSolver implements Comunicar {
    // Referència a la classe Main, que actua com a controlador principal
    private final Main p;
    // Objecte Dades que conté el tauler i la informació de l'estat actual
    private final Dades data;

    private long startTime;


    /**
     * Constructor de la classe.
     * @param p Instància de Main que s'encarrega de gestionar la comunicació entre components.
     * @param data Objecte que conté la informació del tauler i les dades necessàries per generar el fractal.
     */
    public SierpinskiTriangleSolver(Main p, Dades data) {
        this.p = p;
        this.data = data;

        // Estableix el tipus de fractal que es generarà
        data.setTipus(Tipus.TRIANGLE);

        // Inicialitza la matriu del tauler amb la mida adequada per representar el Triangle de Sierpinski.
        data.setTauler(new int[data.getProfunditat()][data.getProfunditat() * 2 - 1]);

        // Omple tota la matriu amb el valor 0 (caselles buides)
        for (int[] fila : data.getTauler()) {
            Arrays.fill(fila, 0);
        }
    }

    /**
     * Mètode recursiu per generar el Triangle de Sierpinski.
     * Divideix la figura en sub-triangles més petits seguint la recursió fractal.
     *
     * @param x Coordenada X de la posició inicial
     * @param y Coordenada Y de la posició inicial
     * @param mida Mida actual del triangle a generar
     */
    private void generarSierpinski(int x, int y, int mida) {
        // Comprova si el procés s'ha d'aturar
        if (!aturar) {
            // Cas base: Si la mida és 1, pinta una sola casella i retorna
            if (mida == 1) {
                data.setValor(x, y, 1); // Marca la posició com a part del fractal
                  // Informa la vista perquè es refresqui
                esperar(10, 0);
                return;
            }

            // Calcula la nova mida per als sub-triangles
            int novaMida = mida / 2;

            // Crida recursiva per generar els tres sub-triangles
            runThread(() -> generarSierpinski(x, y, novaMida));                // Triangle superior
            runThread(() -> generarSierpinski(x + novaMida, y - novaMida, novaMida)); // Triangle inferior esquerre
            runThread(() -> generarSierpinski(x + novaMida, y + novaMida, novaMida)); // Triangle inferior dret
        }

    }

    /**
     * Mètode que s'executa quan es crea un fil per aquest objecte.
     * Controla el procés de generació del fractal i mesura el temps d'execució.
     */
    @Override
    public void run() {
        // Reinicia la variable de control d'aturada
        aturar = false;
        double constant =1.0;
        double profunditatExp = Math.pow(3, data.getProfunditat()/2);

        if (data.getTipus() == Tipus.TRIANGLE && data.getConstantMultiplicativa()!=null) {
            constant = data.getConstantMultiplicativa();
        }
        double tempsEsperat = constant*profunditatExp;
        if (tempsEsperat > 100000) {
            p.comunicar("tempsEsperat:molt de temps");
        }else {

            p.comunicar("tempsEsperat:" + String.format("%.3f segons", tempsEsperat));
        }
        // Inicia el comptador de temps en nanosegons
        startTime = System.nanoTime();


        // Crida la funció recursiva per generar el fractal
        runThread(() -> generarSierpinski(0, data.getProfunditat() - 1, data.getProfunditat()));
    }



@Override
protected void end() {
    long elapsedTime = System.nanoTime() - startTime - getSleepTime();

    // Converteix a segons (double)
    double tempsReal = elapsedTime / 1_000_000_000.0;
    double tempsEsperat;
    double profunditatExp = Math.pow(3, data.getProfunditat()/2);
    // Calcula la constant multiplicativa

    double constantMultiplicativa = tempsReal / profunditatExp;

    // Desa la constant multiplicativa
    data.setConstantMultiplicativa(constantMultiplicativa);

    // Mostra els resultats

    p.comunicar("tempsReal:"+ String.format( "%.3f segons",tempsReal));
    // Si el procés no s'ha aturat manualment, assegura que es detingui
    if (!aturar) p.comunicar("aturar");
}

    /**
     * Mètode per rebre missatges de comunicació.
     * Si es rep el missatge "aturar", es deté l'execució del fractal.
     *
     * @param s Missatge rebut per comunicar ordres al solver.
     */
    @Override
    public void comunicar(String s) {
        if (s.contentEquals("aturar")) {
            aturar();
        }
    }

    /**
     * Mètode per aturar l'execució del Solver.
     * Estableix la variable stop a true per indicar que el fil ha de finalitzar.
     */
    private void aturar() {
        if(aturar)return;
        aturar = true;
        executor.shutdown();
    }}
