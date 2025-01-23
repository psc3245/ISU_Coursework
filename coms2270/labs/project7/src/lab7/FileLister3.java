package lab7;

import java.io.File;

public class FileLister3 {
	
	public static void main(String[] args ) {
		
		System.out.println(countFiles(new File(".")));
		
	}
	
	public static int countFiles (File f) {
		
		if (!f.isDirectory()) {
			return 1;
		}
		
		int total = 0;
		
		File[] files = f.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			total += countFiles(files[i]);
		}
		
		return total;
	}
	


}
