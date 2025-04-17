package model.cues;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciHeapTest {
    Random random = new Random();

    @org.junit.jupiter.api.BeforeEach
    public void setUp(){
        random.setSeed(0);
    }

    @org.junit.jupiter.api.Test
    public void testRandom(){
        final int tam = 1000000;
        Queue<Integer> priorityQueue = new PriorityQueue<>();
        Queue<Integer> fibonacciQueue = new FibonacciHeap<>();
        for (int i = 0; i < tam; i++) {
            int nextInt = random.nextInt(tam);
            priorityQueue.add(nextInt);
            fibonacciQueue.add(nextInt);
            assertEquals(priorityQueue.peek(), fibonacciQueue.peek());
        }


        for (int i = 0; i < tam; i++) {
            int delta = random.nextInt(-tam,tam);
            priorityQueue.add(priorityQueue.poll() + delta);
            fibonacciQueue.add(fibonacciQueue.poll() + delta);
            assertEquals(priorityQueue.peek(), fibonacciQueue.peek());
        }

        while (!priorityQueue.isEmpty() || !fibonacciQueue.isEmpty()){
            assertEquals(fibonacciQueue.poll(), priorityQueue.poll());
        }
        assertEquals(0, priorityQueue.size());
        assertEquals(0, fibonacciQueue.size());

    }
}