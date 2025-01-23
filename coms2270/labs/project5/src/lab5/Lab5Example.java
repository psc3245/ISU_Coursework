package lab5;

public class Lab5Example
{
  public static void main(String[] args)
  {
    System.out.println(longestRun("aabbbccd"));
    System.out.println("Expected 3");
    System.out.println(longestRun("aaa"));
    System.out.println("Expected 3");
    System.out.println(longestRun("aabbbb"));
    System.out.println("Expected 4");
  }
  
 
  public static int longestRun(String s)
  {
	  int count = 1;
	  int max = 1;
	  
	  char currChar = s.charAt(0);
	  
	  for (int i = 1; i < s.length(); i++) {
		  
		  if (s.charAt(i) == currChar) {
			  count += 1;
			  
			  if (count > max) {
				  max = count;
			  }
		  }
		  else {
			  count = 1;
		  }
		  
		  currChar = s.charAt(i);
	  }
	  
	  return max;
  }

  
  
}
