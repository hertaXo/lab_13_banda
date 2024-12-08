// Elizabete Smirnova 241RDB250
// Estere Gristiņa 241RDB206
// Liāna Usāne 241RDB227
// Anete Kupča 241RDB180
// Herta Matisone 241RDB177


import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
// neaizmirstam pievienot konkrētās bibliotēkas, ko izmantojāt. Pagaidām pievienoju visas
//import java.util.PriorityQueue;
//import java.util.HashMap;
//import java.util.Map;
//Anete: šitos nedzēst ples man vajadzēs Huffman algoritmam ()


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
		//set max for buffer(herta)
		private static final int SEARCH_BUFFER_SIZE = 4096; // Maximum size of the search buffer
		private static final int LOOKAHEAD_BUFFER_SIZE = 15; // Maximum size of the lookahead buffer
		// lasa failu(Elizabete)

		public static void man(String[] args) {
			Scanner sc = new Scanner(System.in); // Izveido Scanner objektu lietotāja ievades nolasīšanai
			
			System.out.print("Ievadiet faila ceļu (piemēram, test.html): "); // Lūdz ievadīt faila ceļu
			String sourceFile = sc.nextLine(); // Nolasām faila ceļu no lietotāja
	
			checkIfFileExistsAndRead(sourceFile); // Izsauc metodi, kas pārbauda faila esamību un lasa tā saturu
			sc.close(); // Aizver Scanner objektu pēc lietošanas
		}
	
		public static void checkIfFileExistsAndRead(String sourceFile) {
			File file = new File(sourceFile); // Izveido File objektu ar ievadīto ceļu
			if (file.exists() && file.isFile()) { // Pārbauda, vai fails eksistē un ir fails
				System.out.println("Fails atrasts: " + sourceFile);
				readFileContent(sourceFile); // Ja fails pastāv, nolasām tā saturu
			} else {
				System.out.println("Fails nav atrasts: " + sourceFile); // Funkcija ja fails neeksistē
			}
		}
	
		// Metode faila satura nolasīšanai un izdrukāšanai
		public static void readFileContent(String sourceFile) {
			try (FileInputStream fis = new FileInputStream(sourceFile)) {
				int ch;
				StringBuilder content = new StringBuilder(); // Lai saglabātu faila saturu
				while ((ch = fis.read()) != -1) { // Lasa failu pa vienam burtam
					content.append((char) ch); // Pievieno katru nolasīto simbolu saturam
				}
				System.out.println("Faila saturs:");
				System.out.println(content.toString()); // Izvada faila saturu
			} catch (IOException e) {
				System.err.println("Kļūda faila apstrādē: " + e.getMessage());
			}
		}

		// Method to compress a file using LZ77 (herta)
		public static void compressFile(String sourceFile) {
			File file = new File(sourceFile);
	
			if (!file.exists() || !file.isFile()) {
				System.out.println("Fails nav atrasts: " + sourceFile);
				return;
			}
	
			String compressedFilePath = sourceFile + ".lz77";
	
			try (FileInputStream fis = new FileInputStream(sourceFile);
					FileOutputStream fos = new FileOutputStream(compressedFilePath)) {
	
				System.out.println("Saspiest failu: " + sourceFile);
				StringBuilder searchBuffer = new StringBuilder();
	
				int nextChar;
				while ((nextChar = fis.read()) != -1) {
					// Initialize lookahead buffer
					StringBuilder lookaheadBuffer = new StringBuilder();
					lookaheadBuffer.append((char) nextChar);
	
					int matchPosition = -1;
					int matchLength = 0;
	
					// Find the longest match in the search buffer
					for (int length = 1; length <= LOOKAHEAD_BUFFER_SIZE && nextChar != -1; length++) {
						String lookahead = lookaheadBuffer.toString();
						int tempPosition = searchBuffer.indexOf(lookahead);
	
						if (tempPosition != -1) {
							matchPosition = tempPosition;
							matchLength = lookahead.length();
						} else {
							break; // No further match
						}
	
						nextChar = fis.read();
						if (nextChar != -1) {
							lookaheadBuffer.append((char) nextChar);
						}
					}
	
					// Decide whether to encode as match or literal
					String encodedMatch = "~" + matchPosition + "~" + matchLength + "~";
					String literal = lookaheadBuffer.substring(0, matchLength + 1);
	
					if (matchLength > 0 && encodedMatch.length() < literal.length()) {
						// Write encoded match
						char nextLiteral = (matchLength < lookaheadBuffer.length())
								? lookaheadBuffer.charAt(matchLength)
								: '\0';
						fos.write((encodedMatch + nextLiteral).getBytes());
					} else {
						// Write literal
						fos.write(literal.getBytes());
					}
					// Update the search buffer
					// Check that matchLength + 1 doesn't exceed the length of the lookahead buffer
				}
				// eror cach
			}
		}
	}
		

		
		//Huffmanis -----------------------------------------------------------

		class HuffmanNode {
            		char character;   
            		int frequency;    
            		HuffmanNode left; 
            		HuffmanNode right; 

			public HuffmanNode(char character, int frequency) {
                		this.character = character;
                		this.frequency = frequency;
                		this.left = null;  
                		this.right = null; 
			}

			public static int compare(HuffmanNode x, HuffmanNode y) {
                		return x.frequency - y.frequency; 
            }

		//jānolasa jaunais LZ77 fails 
		Map<Character, Integer> freqMap = new HashMap<>();
        	StringBuilder text = new StringBuilder();

        	try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            		String line;
            		while ((line = reader.readLine()) != null) {
                		text.append(line).append("\n");
            		}
        	}
		//for cikls lai izveidotu vārdnīcu ar simboliem un to biežumiem
		for (char c : text.toString().toCharArray()) {
            		freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        	}


		//prioritātes rinda - sakārto vērtības kokā 		
		PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new Comparator<HuffmanNode>() {
            		public int compare(HuffmanNode node1, HuffmanNode node2) {
                		return HuffmanNode.compare(node1, node2); 
            		}
        	});

		//izveidot un aizpildīt Huffman Node sarakstu
		for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            		pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        	}
		//šo un iepriekšējo funkciju centīšos uzrakstīt saprotamāk - viss procesā ------------------------------------------

		//jāizveido pats koks ar while ciklu, pāri palikusī lielākā biežuma vērtība būs sakne
		while (pq.size() > 1) {
            		HuffmanNode left = pq.poll();
            		HuffmanNode right = pq.poll();

            		HuffmanNode merged = new HuffmanNode('\0', left.frequency + right.frequency);
            		merged.left = left;
            		merged.right = right;

            		pq.add(merged);
        	}
		  HuffmanNode root = pq.poll();
		
		//jāizveido vārdnīca ar simbols : Huffmaņa vērtību, jāizsauc Huffman codes metode
		Map<Character, String> huffmanCodes = new HashMap<>();
        	generateHuffmanCodes(root, "", huffmanCodes);
		//jāsaraksta tas viss failā - for cikliņš

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            		writer.write("Huffman Codes: \n");
            		for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                		writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            		}
        	}

		//Huffmanis beigas ----------------------------------------------------------
		

        	private void trimSearchBuffer() {
            		// Limit search buffer size to 4096 characters
        	}

        	public static void main() /*string arg*/  {
            		// if viss ok
            		// izprintē ka viss ok

            		// try, error
        	}//Anete: uhmmm mums jau ir main funkcija augšā tho??

		
		//LZ77 the end ------------------------------------------------------------
	}

// Estere- Huffman method decomp

private static String decompHuffman(String encodedData, Node root) {  //
    StringBuilder decodedString = new StringBuilder(); //Izveido StringBuilder, lai saglabātu dekodētās rakstzīmes, šķērsojot koku.
    Node current = root; // Sāk nolasīt no Huffman koka root


    for (char bit : encodedData.toCharArray()) {
        // Iziet cauri katram bitam (rakstzīme '0' vai '1') kodētajā datu virknē.
        //toCharArray() pārvērš bināro virkni par rakstzīmju masīvu  preikš iterācijas.

        current = (bit == '0') ? current.left : current.right;
        //Pārbauda konkrēto bitu
        //Ja bits ir '0', pāriet uz konkŗetā mezgla kreiso atvasinājumu (current.left).
        //Ja bits ir '1', pāriet uz konkrētā mezgla labo atvasinājumu (current.right).

        //Ja sasniedz leaf node (ir atšifrēts), iegūstam attiecīgo rakstzīmi.
        if (current.isLeaf()) {
            decodedString.append(current.character); //Šeit to iegūto rakstzīmi pievieno rezultātam
            current = root; //Pēc katras reizes "koks" jāiestata no sākuma(no root)
        }
    }

    return decodedString.toString(); // Atgriež pilnībā dekodēto virkni
}

// Esteres koda daļas beigas



public static void decomp(String compressedFilePath) {
		//LZ77(Liāna)---------------------------------------------------------------
	File compressedFile = new File(compressedFilePath);

    // Pārbauda, vai saspiestais fails eksistē un ir fails
    if (!compressedFile.exists() || !compressedFile.isFile()) {
        System.out.println("Saspiestais fails nav atrasts: " + compressedFilePath);
        return;
    }

    // Izveido ceļu dekompresētajam failam, aizstājot paplašinājumu
    String decompressedFilePath = compressedFilePath.replace(".lz77", ".decompressed");
    StringBuilder decompressedData = new StringBuilder(); // Glabās dekompresēto saturu

    try (FileInputStream fis = new FileInputStream(compressedFile);
         FileOutputStream fos = new FileOutputStream(decompressedFilePath)) {

        System.out.println("Atspiež failu: " + compressedFilePath);

        int nextChar;
        while ((nextChar = fis.read()) != -1) { // Lasa saspiestā faila saturu simbolu pa simbolam
            char c = (char) nextChar;

            if (c == '~') { // Pārbauda, vai simbols norāda uz saspiešanas datu sākumu
                StringBuilder matchData = new StringBuilder();
                // Lasa pozīcijas un garuma datus līdz nākamajam "~"
                while ((nextChar = fis.read()) != -1 && (char) nextChar != '~') {
                    matchData.append((char) nextChar);
                }

                // Nosaka pozīciju un garumu, sadalot virkni
                String[] matchParts = matchData.toString().split("~");
                if (matchParts.length >= 2) { // Pārbauda, vai dati ir pareizi formatēti
                    int position = Integer.parseInt(matchParts[0]); // Match sākuma pozīcija
                    int length = Integer.parseInt(matchParts[1]);   // Match garums

                    // Nokopē datus no dekompresētajiem datiem, sākot ar norādīto pozīciju
                    int start = decompressedData.length() - position;
                    for (int i = 0; i < length; i++) {
                        decompressedData.append(decompressedData.charAt(start + i));
                    }
                }

                // Pievieno nākamo burtu (literālu), ja tas ir pieejams
                nextChar = fis.read();
                if (nextChar != -1) {
                    decompressedData.append((char) nextChar);
                }
            } else { // Ja simbols nav "~", tas ir literāls
                decompressedData.append(c);
            }
        }

        // Pieraksta dekompresēto saturu uz failu
        fos.write(decompressedData.toString().getBytes());
        System.out.println("Fails atspiests: " + decompressedFilePath);

    } catch (IOException e) {
        // Apstrādā kļūdas, ja rodas problēmas ar faila apstrādi
        System.err.println("Kļūda faila atspiešanā: " + e.getMessage());
    }
		
}
// Liāna koda daļas beigas
		
	
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

	//huffmaņa koda ģenerators katrai vērtībai
	public static void generateHuffmanCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes){
		if (node == null) return;

        	if (node.left == null && node.right == null) {
            		huffmanCodes.put(node.character, code);
        	}

        	generateHuffmanCodes(node.left, code + "0", huffmanCodes);
        	generateHuffmanCodes(node.right, code + "1", huffmanCodes);
	}
}


