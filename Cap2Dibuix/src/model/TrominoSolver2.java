package model;

import principal.Main;

import java.util.Arrays;

public class TrominoSolver2    implements Model{
    private Dades data;
    private int[][] tauler;
    private int numActual;
    private Main main;
    private static final int RETJOLA = -1;

    public TrominoSolver2(Dades data, Main main) {
        this.data = data;
        this.main = main;

        int midaActual = (int)Math.pow(2, data.getProfunditat());
        long time = (long) (data.getConstantMult()*((Math.pow(4, data.getProfunditat()))/2));
        System.out.println("Temps previst "+ time/1000 + " segons");
//        main.comunicar("Temps previst "+ time/1000 + " segons");

        tauler = new int[midaActual][midaActual];
        numActual = 1;

        // Omplim el tauler amb totes les caselles buides.
        for (int i = 0; i < midaActual; i++) {
            Arrays.fill(tauler[i], 0);
        }


        // Aquesta casella representa el forat original en el tromino.
        int x = data.getxRetjola();
        int y = data.getyRetjola();
        tauler[x][y] = RETJOLA;

        time = System.currentTimeMillis();
        resol();
        time = System.currentTimeMillis() - time;
        data.setConstantMult(time*1.0/(Math.pow(4, data.getProfunditat())));
        System.out.println("Temps real "+ time/1000 + " segons");
//        main.comunicar("Temps real  "+ time/1000 + "segons");

//        main.comunicar("pintar");
        espera(1);


    }



    private void espera(long i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void trominoRec(int mida, int topx, int topy) {

        // Cas base: mida 2x2, col·locar l'última casella
        if (mida == 2) {
            omplirTromino(topx, topy, mida);
//            main.comunicar("omplicarTromino x"+ topx +" y"+ topy +" mida"+ mida );
            espera(1);
            numActual++;
        } else {
            // Cas recursiu
            int[] forat = trobarForat(topx, topy, mida);

            // Utilitzem l'enum Mode per determinar la ubicació del forat i procedir segons correspongui
            Mode mode = determinarMode(forat[0], forat[1], topx, topy, mida);

            // Omplim el tromino central
            omplirTrominoCentral(mode, topx, topy, mida);
//            main.comunicar("omplirTrominoCentral mode"+ mode + " x"+ topx +" y"+ topy +" mida"+ mida ); //algo de l'estil

            // Recursió per als quatre quadrants
            trominoRec(mida / 2, topx, topy);
            trominoRec(mida / 2, topx, topy + mida / 2);
            trominoRec(mida / 2, topx + mida / 2, topy);
            trominoRec(mida / 2, topx + mida / 2, topy + mida / 2);
        }
    }

    // Troba la posició del forat dins un sub-tauler (quadrant).
    private int[] trobarForat(int topx, int topy, int mida) {
        int[] forat = new int[2];
        for (int x = topx; x < topx + mida; x++) {
            for (int y = topy; y < topy + mida; y++) {
                if (tauler[x][y] != 0) {
                    forat[0] = x;
                    forat[1] = y;
                }
            }
        }
        return forat;
    }

    // Determina el mode (quadrant) en què es troba el forat dins el sub-tauler.
    private Mode determinarMode(int foratX, int foratY, int topx, int topy, int mida) {
        if (foratX < topx + mida / 2 && foratY < topy + mida / 2) {
            return Mode.LU;
        } else if (foratX < topx + mida / 2 && foratY >= topy + mida / 2) {
            return Mode.RU;
        } else if (foratX >= topx + mida / 2 && foratY < topy + mida / 2) {
            return Mode.LD;
        } else {
            return Mode.RD;
        }
    }

    // Omple el tromino central segons el mode.
    private void omplirTrominoCentral(Mode mode, int topx, int topy, int mida) {
        switch (mode) {
            case LU:
                tauler[topx + mida / 2][topy + mida / 2 - 1] = numActual;
                tauler[topx + mida / 2][topy + mida / 2] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2] = numActual;
                break;
            case RU:
                tauler[topx + mida / 2][topy + mida / 2 - 1] = numActual;
                tauler[topx + mida / 2][topy + mida / 2] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2 - 1] = numActual;
                break;
            case LD:
                tauler[topx + mida / 2 - 1][topy + mida / 2] = numActual;
                tauler[topx + mida / 2][topy + mida / 2] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2 - 1] = numActual;
                break;
            case RD:
                tauler[topx + mida / 2 - 1][topy + mida / 2] = numActual;
                tauler[topx + mida / 2][topy + mida / 2 - 1] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2 - 1] = numActual;
                break;
        }
        numActual++;
    }

    // Omple un sub-tauler de mida x mida amb el tromino actual.
    private void omplirTromino(int topx, int topy, int mida) {
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if (tauler[topx + i][topy + j] == 0) {
                    tauler[topx + i][topy + j] = numActual;
                }
            }
        }
    }

    // Imprimeix l'objecte actual
    public void imprimir() {
        for (int[] fila : tauler) {
            for (int c : fila) {
                System.out.print("|" + c + "\t");
            }
            System.out.println("|\n");
        }
    }

    @Override
    public int[][] getMatriu() {
        return new int[0][];
    }

    @Override
    public void resol() {

        trominoRec(tauler.length, 0, 0);

    }
//    public BufferedImage dibuixarTrominos() {
//        int cellSize = 800/ (int)(Math.sqrt(this.mida)); // Tamaño de cada celda
//        int width = tauler.length * cellSize;
//        int height = tauler[0].length * cellSize;
//         image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = image.createGraphics();
//
//        // Mapa de colores para los trominós
//        HashMap<Integer, Color> colorMap = new HashMap<>();
//        Random rand = new Random();
//
//        // Dibujar los trominós
//        for (int i = 0; i < tauler.length; i++) {
//            for (int j = 0; j < tauler[i].length; j++) {
//
//                int value = tauler[i][j];
//                if (value == -1) {
//                    g.setColor(Color.BLACK); // Hueco original
//                } else {
//                    colorMap.putIfAbsent(value, new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
//                    g.setColor(colorMap.get(value));
//                }
//                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
//                g.setColor(Color.WHITE);
//                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
//            }
//        }
//
//        g.dispose();
//        return image;
//    }
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawImage(image, 0, 0, null);
//    }


}