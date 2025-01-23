package lab6;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import plotter.Plotter;
import plotter.Polyline;

public class TestPlotter
{
	public static void main(String[] args) throws FileNotFoundException
	  {
	    ArrayList<Polyline> list = readFile("hello.txt");
	    Plotter plotter = new Plotter();

	    for (Polyline p : list)
	    {
	      plotter.plot(p);
	    }
	  }

	private static ArrayList<Polyline> readFile(String filename)
			throws FileNotFoundException
	{
		ArrayList<Polyline> points = new ArrayList<Polyline>();
		
		File file = new File(filename);    
		Scanner s = new Scanner(file);
		
		while (s.hasNext()) {
			
			String line = s.nextLine();
			
			if ( !line.equals("") && !(line.charAt(0) == '#') ) {
				points.add(parseOneLine(line));
			}
			
			
		}
		
		return points;

	}


	private static Polyline parseOneLine(String line)
	{
		Polyline p;
		
		Scanner s = new Scanner(line);
		
		if (s.hasNextInt()) {
			int width = s.nextInt();
			String color = s.next();
			
			p = new Polyline(color, width);
		}
		else {
			String color = s.next();
			
			p = new Polyline(color);
		}
		
		while (s.hasNextInt()) {
			
			int x = s.nextInt();
			int y = s.nextInt();
			
			p.addPoint(new Point(x, y));
		}

		return p;
	}
}
