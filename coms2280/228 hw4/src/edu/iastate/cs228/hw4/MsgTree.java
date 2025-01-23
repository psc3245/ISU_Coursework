package edu.iastate.cs228.hw4;

import java.util.Stack;

public class MsgTree {
	
	
    public char payLoadChar;
    
    public MsgTree left;
    
    public MsgTree right;

    /*Can use a static char idx to the tree string for recursive
     * solution, but it is not strictly necessary*/
    
    private static int staticCharIdx = 0;
    
    //Constructor building the tree from a string
    
    
    public MsgTree (String encodingString) {
    	// Set up the stack and the payload char
        Stack<MsgTree> stack = new Stack<>();
        char c = encodingString.charAt(staticCharIdx++);
        this.payLoadChar = c;
        
        String dir = "left";
        stack.push(this);
        MsgTree curr = this;

        // Set up the branches
        while (staticCharIdx < encodingString.length()){
        	
            c = encodingString.charAt(staticCharIdx++);
            MsgTree node = new MsgTree(c);
            
            if (dir.equals("left")){
                curr.left = node;
                if (c == '^'){
                    curr = stack.push(node);
                    dir = "left";
                }
                else {
                    if (!stack.empty()){
                        curr = stack.pop();
                    }
                    dir = "right";
                }
            }
            else {
                curr.right = node;
                if (c == '^'){
                    curr = stack.push(node);
                    dir = "left";
                }   
                else {
                    if (!stack.empty()){
                        curr = stack.pop();
                    }
                    dir = "right";
                }
            }
        }
        

    }
    
    //Constructor for a single node with null children
    public MsgTree(char payLoadChar){
    	
        this.payLoadChar = payLoadChar;
        left = null;
        right = null;
        
    }

    private static String routeTaken; //variable to hold the path traversed through the tree to find a char

    
    //method to print characters and their binary codes
    public static void printCodes(MsgTree root, String code) {
    	
        System.out.println("character   code");
        System.out.println("-------------------------");
        for (int i = 0; i < code.length(); i++){
            char c = code.charAt(i);
            findRoute(root, c, "");
            if (c == '\n') {
                System.out.println("   " + "\\n" + "       " + routeTaken);

            }
            else {
                System.out.println("   " + c + "        " + routeTaken);
            }

        }
    }

    // helper method to find the route we travel on the tree
    private static Boolean findRoute(MsgTree root, char c, String route) {
        if (root != null){
            if (root.payLoadChar == c){
                routeTaken = route;
                return true;
            }
            return findRoute(root.left, c, route) || findRoute(root.right, c, route + 1);
        }
        return false;
    }


    //method to decode the message
    public void decode(MsgTree codes, String msg) {
    	
        System.out.println("MESSAGE:");
        String result = "";
        MsgTree curr = codes;
        
        for (int i = 0; i < msg.length(); i++){
            char act = msg.charAt(i);
            if (curr != null){
                if (curr.payLoadChar != '^'){
                    result += curr.payLoadChar;
                    curr = codes;
                }
                if (act == '0'){
                    curr = curr.left;
                }
                else {
                    curr = curr.right;
                }
                continue;
            }
            curr = codes;
        }
        if (curr != null){
            if (curr.payLoadChar != '^'){
                result += curr.payLoadChar;
            }
        }
        System.out.println(result);
    }
}
