package lab6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizAverager2
{
  public static void main(String[] args) throws FileNotFoundException
  {
    // read the file and get a list of results
    ArrayList<Student> results = computeAllAverages("scores.txt", 50);
    
    // search the list for anyone named Weasley
    for (Student s : results)
    {
      if (s.getName().endsWith("Weasley"))
      {
        System.out.println(s.getName() + " " + s.getAverage());
      }
    }
  }

  /**
   * Reads a file containing student names and scores, and returns a list
   * of student records.
   */
  private static ArrayList<Student> computeAllAverages(String filename, int pointsPossible) 
      throws FileNotFoundException
  {
    ArrayList<Student> results = new ArrayList<>();
    
    File file = new File(filename);    
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine())
    {
       String line = scanner.nextLine();
       Student result = computeOneAverage(line, pointsPossible);
       results.add(result);
    }
    scanner.close();

    return results;
  }
  
  /**
   * Given a line of text and the total points, returns a student
   * record containing the student's name and average score.
   */
  private static Student computeOneAverage(String line, int pointsPossible)
  {   
    Scanner temp = new Scanner(line);

    String first = temp.next();
    String last = temp.next();
    
    // add up all the scores
    double total = 0.0;
    while (temp.hasNextInt())
    {
      int value = temp.nextInt();
      total += value;
    }
 
    double average = total / pointsPossible;
    Student result = new Student(first + " " + last, average);
    return result;
  }
}
