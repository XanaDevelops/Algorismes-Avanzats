import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatriuTest {

    Matriu a, b, c;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        a = new Matriu(3);
        b = new Matriu(3);
        c = new Matriu(3);

        int[][] avals = new int[][]{{1,2,3}, {4,5,6}, {7,8,9}};
        int[][] bvals = new int[][]{{9,8,7}, {6,5,4}, {3,2,1}};
        int[][] cvals = new int[][]{{30, 24, 18},{84, 69, 54}, {138, 114, 90}};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                a.set(i, j, avals[i][j]);
                b.set(i, j, bvals[i][j]);
                c.set(i, j, cvals[i][j]);
            }
        }
    }

    @org.junit.jupiter.api.Test
    void sumar() {
    }

    @org.junit.jupiter.api.Test
    void multiplicar() {
        Matriu test = a.multiplicar(b);
        System.out.println(test);
        System.out.println(c);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(c.get(i, j), test.get(i, j));
            }
        }
    }
}