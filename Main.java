// Elizabete Smirnova 241RDB250
// Estere Gristiņa 241RDB206
// Liāna Usāne 241RDB227
// Anete Kupča 241RDB180
// Herta Matisone 241RDB177


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class Main {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String choiseStr;
		String sourceFile, resultFile, firstFile, secondFile;
		
		loop: while (true) {
			
			choiseStr = sc.next();
								
			switch (choiseStr) {
			case "comp":
				System.out.print("source file name: ");
				sourceFile = sc.next();
				System.out.print("archive name: ");
				resultFile = sc.next();
				comp(sourceFile, resultFile);
				break;
			case "decomp":
				System.out.print("archive name: ");
				sourceFile = sc.next();
				System.out.print("file name: ");
				resultFile = sc.next();
				decomp(sourceFile, resultFile);
				break;
			case "size":
				System.out.print("file name: ");
				sourceFile = sc.next();
				size(sourceFile);
				break;
			case "equal":
				System.out.print("first file name: ");
				firstFile = sc.next();
				System.out.print("second file name: ");
				secondFile = sc.next();
				System.out.println(equal(firstFile, secondFile));
				break;
			case "about":
				about();
				break;
			case "exit":
				break loop;
			}
		}

		sc.close();
	}

	public static void comp(String sourceFile, String resultFile) {
		// LZ77---------------------------------------------------------------


		// lasa failu

        // izveido output failu

        // ar for un if-iem meklē mach
        // lz 77!!!!!

        private void trimSearchBuffer() {
            // Limit search buffer size to 4096 characters
        }

        public static void main() /*string arg*/  {
            // if viss ok
            // izprintē ka viss ok

            // try, error
        }
		//LZ77 the end ------------------------------------------------------------
	}

	public static void decomp(String sourceFile, String resultFile) {
		// TODO: implement this method
		//LZ77(Liāna)---------------------------------------------------------------
		//(Man aptuveni ir ideja kā tas viss varētu strādāt bet vēl viss ir procesā,
		//sorry ka neko gnj nevar saprast manos komentāros.)
		// Lasa saspiestu failu
		// Izveido atjaunoto (dekompresēto) failu
		// Izmanto Triple objektu sarakstu
		// Triple klase (distance, garums, nextChar)
    	static class Triple {
        	int distance;
       		int garums;
        	char nextChar;
    }
	public static Triple fromString(String line) {
            String[] parts = line.split(",");
            return new Triple(
                Integer.parseInt(parts[0]), 
                Integer.parseInt(parts[1]), 
                parts[2].charAt(0)
            );
        }
// Funkcija, kas lasa saspiestos faila datus un tos dekomprimē
    private static String decompress(List<Triple> compressed) {
        StringBuilder decompressed = new StringBuilder();
		// for  Ja distance un length ir 0, pievieno nextChar
	    //if, else
	    //Aprēķina sākuma indeksu
	    // Pievieno atkārtotus simbolus ar for

	    // LZ77 dekompresijas galvenā funkcija
	    public static void decompressFile(String inputFilePath, String outputFilePath) {
        try {
            // Lasa saspiesto failu
           
            }
	catch (IOException e) {
		
	    }
		// Dekomprimē datus ar for un if
	}
		
	
	public static void size(String sourceFile) {
		System.out.print("file name: ");
   		String sourceFile = scanner.nextLine();
		try {
			FileInputStream f = new FileInputStream(sourceFile);
			System.out.println("size: " + f.available()+ " bytes");
			f.close();
		}
		catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		
	}
	
	public static boolean equal(String firstFile, String secondFile) {
		try {
			FileInputStream f1 = new FileInputStream(firstFile);
			FileInputStream f2 = new FileInputStream(secondFile);
			int k1, k2;
			byte[] buf1 = new byte[1000];
			byte[] buf2 = new byte[1000];
			do {
				k1 = f1.read(buf1);
				k2 = f2.read(buf2);
				if (k1 != k2) {
					f1.close();
					f2.close();
					return false;
				}
				for (int i=0; i<k1; i++) {
					if (buf1[i] != buf2[i]) {
						f1.close();
						f2.close();
						return false;
					}
						
				}
			} while (!(k1 == -1 && k2 == -1));
			f1.close();
			f2.close();
			return true;
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public static void about() {
		System.out.println("Elizabete Smirnova 241RDB250");
		System.out.println("Estere Gristiņa 241RDB206");
		System.out.println("Liāna Usāne 241RDB227");
		System.out.println("Anete Kupča 241RDB180");
		System.out.println("Herta Matisone 241RDB177");
	}
}

