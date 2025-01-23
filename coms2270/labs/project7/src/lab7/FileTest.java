package lab7;

import java.io.File;

public class FileTest
{
  public static void main(String[] args)
  {
    File f = new File("../project6");
    System.out.println("Does it exist? " + f.exists());
    System.out.println("Is this a directory? " + f.isDirectory());
    System.out.println();
    
    // list the immediate contents of the project6 directory
    File[] files = f.listFiles();
    for (int i = 0; i < files.length; ++i)
    {
      System.out.println(files[i].getName());
    }
  }
}
