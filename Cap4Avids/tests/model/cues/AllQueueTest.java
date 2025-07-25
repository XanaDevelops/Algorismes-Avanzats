package model.cues;

import model.Huffman.Huffman;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AllQueueTest {
    Random random = new Random();

    @org.junit.jupiter.api.BeforeEach
    public void setUp(){
        random.setSeed(0);
    }

    @org.junit.jupiter.api.Test
    public void testRandom() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final int tam = 1000000;
        List<Queue<Integer>> queues = new ArrayList<>();
        for(Huffman.TipusCua c : Huffman.TipusCua.values()){
            queues.add(c.getCua().getConstructor().newInstance());
        }

        for (int i = 0; i < tam; i++) {
            int nextInt = random.nextInt(tam);
            for(Queue<Integer> q: queues){
                q.add(nextInt);
            }
            final int compare = queues.getFirst().peek();
            for (Queue<Integer> q : queues) {
                if(q.peek() != compare){
                    System.err.println("ERROR QUEUE: " + q.getClass().getSimpleName());
                    fail("not equal: " + compare + " != " + q.peek());
                }
            }
        }


        for (int i = 0; i < tam; i++) {
            int delta = random.nextInt(-tam,tam);
            for(Queue<Integer> q: queues){
                q.add(q.poll() + delta);
            }
            final int compare = queues.getFirst().peek();
            for (Queue<Integer> q : queues) {
                if(q.peek() != compare){
                    System.err.println(q.getClass().getSimpleName());
                    fail("not equal" + compare + ", " + q.peek());
                }
            }
        }

        exit:
        while(true){
            if (queues.getFirst().isEmpty()) break exit;
            int compare = queues.getFirst().peek();
            for(Queue<Integer> q: queues){
                if(q.isEmpty()){
                    break exit;
                }
                assertEquals(compare, q.poll());
            }
        }
        for(Queue<Integer> q: queues){
            assertTrue(q.isEmpty());
        }
    }
}