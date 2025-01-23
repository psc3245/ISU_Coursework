package lab6;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WordsInLine {

	public static void main(String[] args) throws FileNotFoundException
	{
		File file = new File("story.txt");
		Scanner scanner = new Scanner(file);

		while (scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			System.out.print(line);
			
			if (!line.equals("")) {
				String[] words = line.split(" ");
				System.out.println(" | words: " + words.length);
			}
			else {
				System.out.println();
			}
			

		}
		scanner.close();
	}

}
