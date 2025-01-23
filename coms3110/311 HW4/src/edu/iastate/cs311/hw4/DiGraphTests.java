/**
 * @author not me, I got these tests from piazza.
 * Only used this file to verify that my code in Digraph.java worked.
 */

package edu.iastate.cs311.hw4;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

public class DiGraphTests {
    private static DiGraph<String> G;

    private static void testFromFile(String inFileName, String outFileName) throws IOException {
        FileInputStream inFileStream = new FileInputStream(inFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(inFileStream));
        String[] strLine = br.readLine().split("\\s+");
        int n = Integer.parseInt(strLine[0]);
        int m = Integer.parseInt(strLine[1]);
        G = new DiGraph<String>();
        for(int i=0; i<m; i++){
            strLine = br.readLine().split("\\s+");
            G.addEdge(strLine[0], strLine[1], Integer.parseInt(strLine[2]));
        }
        strLine = br.readLine().split("\\s+");
        String source = strLine[0], destination = strLine[1];
        HashMap<String, Integer> dist = DiGraph.Dijkstra(G, source, destination);

        FileInputStream outFileStream = new FileInputStream(outFileName);
        br = new BufferedReader(new InputStreamReader(outFileStream));
        strLine = br.readLine().split("\\s+");
        int shortestDist = Integer.parseInt(strLine[0]);
        
        // separately checking the destination distance
        assertTrue("Failed for " + inFileName + ". The dist[destination] is wrong.", dist.get(destination) == shortestDist);
        
        strLine = br.readLine().split("\\s+");
        int[] expectedDist = new int[n];
        for(int i=0; i<n; i++) {
        	expectedDist[i] = Integer.parseInt(strLine[i]);
        	assertTrue("Failed for " + inFileName + ". The dist to node " + i + " is wrong.", dist.get(Integer.toString(i)) == expectedDist[i]);
        }
        
        verifyPath(G, source, destination, shortestDist, inFileName);
        
        br.close();
    }

    private static void verifyPath(DiGraph<String> G, String source, String destination, int shortestDist, String inFileName){
        LinkedStack<String> path = G.getPath();
        int dist = 0;
        String u = path.pop();
        assertEquals("Failed for " + inFileName + ". Starting node of the path is wrong.", u, source);
        HashSet<String> visited = new HashSet<>();
        visited.add(u);
        while(!path.isEmpty()){
            String v = path.pop();
            assertTrue("Failed for " + inFileName + ". The edge (" + u + ", " + v + ") does not exist." , G.hasEdge(u, v));
            assertTrue("Failed for " + inFileName + ". The node " + v + " is visited more than once.", !visited.contains(v));
            visited.add(v);
            u = v;
        }
        assertEquals("Failed for " + inFileName + ". The destination node of the path is wrong.", u, destination);
    }

    @Test
    public void test0() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input0.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output0.txt";
        testFromFile(inFileName, outFileName);
    }


    @Test
    public void test1() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input1.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output1.txt";
        testFromFile(inFileName, outFileName);
    }


    @Test
    public void test2() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input2.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output2.txt";
        testFromFile(inFileName, outFileName);
    }


    @Test
    public void test3() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input3.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output3.txt";
        testFromFile(inFileName, outFileName);
    }


    @Test
    public void test4() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input4.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output4.txt";
        testFromFile(inFileName, outFileName);
    }


    @Test
    public void test5() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input5.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output5.txt";
        testFromFile(inFileName, outFileName);
    }


    @Test
    public void test6() throws IOException {
        String inFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/input6.txt";
        String outFileName = "/Users/petercollins/Desktop/hw4_tests/testfiles/output6.txt";
        testFromFile(inFileName, outFileName);
    }
}