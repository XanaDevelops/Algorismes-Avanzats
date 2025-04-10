package controlador;

import model.Dades;
import model.TipoPunt;
import model.punts.Punt;
import model.generadors.GeneradorUniforme;
import model.generadors.GeneradorGaussia;
import model.generadors.GeneradorExponencial;
import model.calculs.ParellaPropera_fb;
import model.calculs.ParellaPropera_dv;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

// Controlador Main que actua de pont entre la vista i el model
public class Main implements Comunicar {
    // Camps del controlador
    private Comunicar finestra;
    private Dades dades;
    private ExecutorService executor;
    private final AtomicInteger activeTasks = new AtomicInteger(0);

    // Inicialització de l'aplicació
    private void init() {

        dades = new Dades(new ArrayList<>(), TipoPunt.p2D);
        executor = Executors.newFixedThreadPool(4);
        executor.execute(() -> {
            finestra = new Finestra(this, dades);
        });
    }

    public static void main(String[] args) {
        new Main().init();
    }

    @Override
    public void comunicar(String msg) {
        String[] params = msg.split(":");
        switch (params[0]) {
            case "pintar":
            case "color":
            case "tempsReal":
                // Propagar missatges directament a la vista
                finestra.comunicar(msg);
                break;

            case "generar":
                if (params.length < 6) {
                    System.err.println("MAIN: format 'generar' incorrecte - " + msg);
                    return;
                }
                String tipusDist = params[1];    // "uniforme", "gaussia", "exponencial"
                String dimensio = params[2];     // "2D" o "3D"

                int numPunts, min, max;
                try {
                    numPunts = Integer.parseInt(params[3]);
                    min = Integer.parseInt(params[4]);
                    max = Integer.parseInt(params[5]);
                } catch (NumberFormatException e) {
                    System.err.println("MAIN: paràmetres numèrics invàlids - " + msg);
                    return;
                }
                // Generar els punts segons el tipus de distribució
                List<Punt> nousPunts;
                TipoPunt tp = dimensio.equalsIgnoreCase("3D") ? TipoPunt.p3D : TipoPunt.p2D;
                try {
                    switch (tipusDist.toLowerCase()) {
                        case "uniforme":
                            GeneradorUniforme genU = new GeneradorUniforme(numPunts, min, max);
                            nousPunts = (tp == TipoPunt.p3D) ? genU.genera3D() : genU.genera2D();
                            break;
                        case "gaussia":
                            // Espera 2 extres: mitjana i desviació estàndard
                            double mitjana = (params.length > 6) ? Double.parseDouble(params[6]) : (min + max) / 2.0;
                            double desviacio = (params.length > 7) ? Double.parseDouble(params[7]) : Math.max(1.0, (max - min) / 4.0);
                            GeneradorGaussia genG = new GeneradorGaussia(numPunts, min, max, mitjana, desviacio);
                            nousPunts = (tp == TipoPunt.p3D) ? genG.genera3D() : genG.genera2D();
                            break;
                        case "exponencial":
                            // Espera 1 extra: lambda
                            double lambda = (params.length > 6) ? Double.parseDouble(params[6]) : 1.0;
                            GeneradorExponencial genE = new GeneradorExponencial(numPunts, min, max, lambda);
                            nousPunts = (tp == TipoPunt.p3D) ? genE.genera3D() : genE.genera2D();
                            break;
                        default:
                            System.err.println("MAIN: distribució '" + tipusDist + "' desconeguda");
                            return;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("MAIN: paràmetres extra invàlids - " + msg);
                    return;
                }
                // Inicialitzar noves dades amb els punts generats
                dades = new Dades(nousPunts, tp);
                // Mostrar els punts generats a la vista
                finestra.comunicar("pintar");
                break;

            case "executar":
                if (params.length < 2) {
                    System.err.println("MAIN: missatge 'executar' sense algorisme - " + msg);
                    return;
                }
                String algorisme = params[1];

                int total = dades.getPunts().size();
                if (params.length > 2) {
                    try {
                        int limit = Integer.parseInt(params[2]);
                        if (limit > 0 && limit < total) {
                            total = limit;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                // Executar l'algorisme corresponent de manera concurrent
                switch (algorisme) {
                    case "fb":
                        activeTasks.incrementAndGet();
                        executor.execute(() -> {
                            new ParellaPropera_fb(dades).run();
                        });
                        break;
                    case "dv":  // divideix i venceràs
                        activeTasks.incrementAndGet();
                        executor.execute(() -> {
                            new ParellaPropera_dv(dades).run();
                        });
                        break;
                    default:
                        System.err.println("MAIN: execució de '" + algorisme + "' desconeguda");
                        break;
                }
                finestra.comunicar("pintar");
                break;

            case "aturar":
                List<Runnable> pendents = executor.shutdownNow();
                System.out.println("Tasques cancel·lades: " + pendents.size());
                activeTasks.set(0);
                finestra.comunicar("aturar");
                break;

            case "borrar":
                this.comunicar("aturar");
                dades.getPunts().clear();
                dades.clearForcaBruta();
                dades.clearDividirVencer();
                finestra.comunicar("pintar");
                break;

            default:
                System.err.println("MAIN: missatge no reconegut - " + msg);
                break;
        }
    }
}
