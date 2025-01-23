package lab6;

public class Student
{
  private String name;
  private double quizAverage;
  
  public Student(String givenName, double givenAverage)
  {
    name = givenName;
    quizAverage = givenAverage;
  }
  
  public String getName()
  {
    return name;
  }
  
  public double getAverage()
  {
    return quizAverage;
  }
}