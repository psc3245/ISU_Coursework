package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
    	
        System.out.println("Please enter filename to decode:");
        
        // Set up files and scanners
        Scanner s = new Scanner(System.in);
        String file = s.nextLine();
        File f = new File(file);
        Scanner scan = new Scanner(f);
        Scanner lineCounter = new Scanner(f);

        // Accumulator strings
        String encoding = "";
        String secretMsg = "";

        // Count the lines
        int lines = 0;
        while (lineCounter.hasNextLine()){
            lineCounter.nextLine();
            lines++;
        }
        
        // Pull the encoding and the encoded message from the file
        if (lines == 2){
            encoding = scan.nextLine();
            secretMsg = scan.nextLine();    
        }
        else if (lines == 3){
            encoding = scan.nextLine();
            encoding += "\n";
            encoding += scan.nextLine();
            secretMsg = scan.nextLine();
        }
        else {
            System.out.println("That one didn't work.");
            System.exit(0);
        }

        
        String chars = "";
        for (int i = 0; i < encoding.length(); i++){
            if (encoding.charAt(i) != '^'){
                chars += encoding.charAt(i);
            }
        }

        // Set up the tree and decode
        
        MsgTree root = new MsgTree(encoding);
        MsgTree.printCodes(root, chars);

        root.decode(root, secretMsg);

        scan.close();
        lineCounter.close();
        s.close();
        
    }
}
