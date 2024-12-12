// Elizabete Smirnova 241RDB250
// Estere Gristiņa 241RDB206
// Liāna Usāne 241RDB227
// Anete Kupča 241RDB180
// Herta Matisone 241RDB177


import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
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
        static class LZ77 {
            private static final int SEARCH_BUFFER_SIZE = 1024;
    
            public static void compress(String sourceFile, String resultFile) throws IOException {
                try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
    
                    StringBuilder searchBuffer = new StringBuilder(SEARCH_BUFFER_SIZE);
                    int nextChar;
                    String currentMatch = "";
                    int matchIndex = 0;
    
                    while ((nextChar = reader.read()) != -1) {
                        String extendedMatch = currentMatch + (char) nextChar;
                        int tempIndex = searchBuffer.indexOf(extendedMatch);
    
                        if (tempIndex != -1) {
                            currentMatch = extendedMatch;
                            matchIndex = tempIndex;
                        } else {
                            if (!currentMatch.isEmpty()) {
                                writer.write("~" + matchIndex + "~" + currentMatch.length() + "~" + (char) nextChar);
                                searchBuffer.append(currentMatch).append((char) nextChar);
                            } else {
                                writer.write((char) nextChar);
                                searchBuffer.append((char) nextChar);
                            }
    
                            if (searchBuffer.length() > SEARCH_BUFFER_SIZE) {
                                searchBuffer.delete(0, searchBuffer.length() - SEARCH_BUFFER_SIZE);
                            }
    
                            currentMatch = "";
                        }
                    }
                }
            }
                
    



        //Huffmanis -----------------------------------------------------------

        class HuffmanNode {
            char character;
            int frequency;
            HuffmanNode left;
            HuffmanNode right;

            public HuffmanNode(char character, int frequency) throws IOException {
                this.character = character;
                this.frequency = frequency;
                this.left = null;
                this.right = null;
            }

            public static int compare(HuffmanNode x, HuffmanNode y) {
                return x.frequency - y.frequency;
            }

            //jānolasa jaunais LZ77 fails
            public Map<Character, Integer> calculateFrequencies(String sourceFile) throws IOException {
                Map<Character, Integer> freqMap = new HashMap<>();
                StringBuilder text = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text.append(line).append("\n");
                    }
                }

                // Biežuma vārdnīca
                for (char c : text.toString().toCharArray()) {
                    freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
                }

                return freqMap;
            }

            //prioritātes rinda - sakārto vērtības kokā
            PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new Comparator<HuffmanNode>() {
                public int compare(HuffmanNode node1, HuffmanNode node2) {
                    return HuffmanNode.compare(node1, node2);
                }
            });

            //izveido un aizpilda Huffman Node sarakstu
            public PriorityQueue<HuffmanNode> createPriorityQueue(Map<Character, Integer> freqMap) throws IOException {
                PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new Comparator<HuffmanNode>() {
                    public int compare(HuffmanNode node1, HuffmanNode node2) {
                        return HuffmanNode.compare(node1, node2);
                    }
                });

                for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
                    pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
                }

                return pq;
            }

            //izveido pašu koku ar while ciklu, pāri palikusī lielākā biežuma vērtība būs sakne
            public HuffmanNode buildHuffmanTree(PriorityQueue<HuffmanNode> pq) throws IOException {
                while (pq.size() > 1) {
                    HuffmanNode left = pq.poll();
                    HuffmanNode right = pq.poll();

                    HuffmanNode merged = new HuffmanNode('\0', left.frequency + right.frequency);
                    merged.left = left;
                    merged.right = right;

                    pq.add(merged);
                }
                return pq.poll(); 
            }
            HuffmanNode root = pq.poll();

            //jāizveido vārdnīca ar simbols : Huffmaņa vērtību, jāizsauc Huffman codes metode
            public Map<Character, String> createHuffmanCodes(HuffmanNode root) {
                Map<Character, String> huffmanCodes = new HashMap<>();
                generateHuffmanCodes(root, "", huffmanCodes);
                return huffmanCodes;
            }
            
            //saraksta tas viss failā - for cikliņš
            public void writeHuffmanCodesToFile(Map<Character, String> huffmanCodes, String resultFile) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
                    writer.write("Huffman Codes: \n");
                    for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                        writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            }

            public static void generateHuffmanCodes(HuffmanNode node, String code, Map<Character, String> huffmanCodes){
                if (node == null) return;

                if (node.left == null && node.right == null) {
                    huffmanCodes.put(node.character, code);
                }

                generateHuffmanCodes(node.left, code + "0", huffmanCodes);
                generateHuffmanCodes(node.right, code + "1", huffmanCodes);
                }
            }   
            //Huffmanis beigas ----------------------------------------------------------
        }
    }
	    

// Estere- Huffman method decomp

    private static void huffmanDecompress(String sourceFile, String resultFile) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(sourceFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            //Metode atver saspiesto failu (sourceFile) lasīšanai, izmantojot ObjectInputStream.
            //ObjectInputStream tiek izmantots, lai lasītu serializētos Java objektus (Huffman kodus un kodētos datus).
            //BufferedWriter ieraksta atspiestos datus izvades failā (resultFile).

            Map<Character, String> huffmanCodes = (Map<Character, String>) in.readObject();
            String encodedData = (String) in.readObject();
            //Huffman kodi (Map <Character, String>) ir deserializēti no faila.
            //saista katru rakstzīmi ar tai atbilstošo bināro Huffman kodu.
            //Tas tika saglabāts saspiešanas laikā.
            //Kodētie dati (virkne) satur saspiestu bitu plūsmu.

            HuffmanNode root = buildHuffmanTreeFromCodes(huffmanCodes);
            //Huffman koks tiek izveidots, izmantojot metodi buildHuffmanTreeFromCodes un huffmanCodes
            //Šis koks vadīs dekodēšanas procesu, kur kustības pa kreisi (0. bits) un pa labi (1. bits) noved pie Leaf mezgliem, kuros ir oriģinālās rakstzīmes


            HuffmanNode currentNode = root;
            //currentNode sākas Huffman koka saknē

            for (char bit : encodedData.toCharArray()) {
                currentNode = (bit == '0') ? currentNode.left : currentNode.right;
                //Katrs bits (0 vai 1) no kodētās datu virknes tiek nolasīts.
                //0 pārvieto rādītāju uz pašreizējā mezgla kreiso pusi; 1 pārvieto uz labo

                if (currentNode.left == null && currentNode.right == null) {
                    writer.write(currentNode.character);
                    //Ja currentNode ir lapas mezgls (kreisais un labais ir nulles), tas nozīmē, ka ir atšifrēta visa rakstzīme.
                    //Lapas mezglā saglabātā rakstzīme (currentNode.character) tiek ierakstīta izvades failā.

                    currentNode = root;
                    //CurrentNode  tiek atiestatīts uz sakni, lai sāktu nākamās rakstzīmes dekodēšanu
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Corrupted file during Huffman decompression.", e);
            //Ja deserializētie objekti (huffmanCodes vai encodedData) ir nederīgi,
            // ClassNotFoundException tiek uztverts un atkārtoti izmests kā IOException ar  kļūdas ziņojumu.
        }
    }
// Esteres koda daļas beigas



    public static void decomp(String compressedFile, String resultFile) {
//LZ77(Liāna)---------------------------------------------------------------
        try (BufferedReader reader = new BufferedReader(new FileReader(compressedFile));
             PrintWriter writer = new PrintWriter(new FileWriter(resultFile))) {

            System.out.println("Decompressing file: " + compressedFile);
            StringBuilder decompressedData = new StringBuilder(); // Holds the decompressed content

            int nextChar;
            while ((nextChar = reader.read()) != -1) {
                char c = (char) nextChar;

                if (c == '~') { // Detected encoded match
                    StringBuilder matchData = new StringBuilder();
                    while ((nextChar = reader.read()) != -1 && (char) nextChar != '~') {
                        matchData.append((char) nextChar);
                    }

                    // Decode match position, length, and next character
                    String[] parts = matchData.toString().split("~");
                    if (parts.length >= 2) {
                        int matchIndex = Integer.parseInt(parts[0]);
                        int matchLength = Integer.parseInt(parts[1]);
                        int start = decompressedData.length() - matchIndex;

                        // Copy the matched content from the decompressed data
                        for (int i = 0; i < matchLength; i++) {
                            decompressedData.append(decompressedData.charAt(start + i));
                        }
                    }

                    // Read and append the next literal character
                    nextChar = reader.read();
                    if (nextChar != -1) {
                        decompressedData.append((char) nextChar);
                    }
                } else {
                    // Append literal character directly
                    decompressedData.append(c);
                }
            }

            // Write the decompressed content to the result file
            writer.print(decompressedData);
            System.out.println("Decompression completed: " + resultFile);

        } catch (IOException e) {
            System.err.println("Error during decompression: " + e.getMessage());
        }
    }

// Liāna koda daļas beigas


    public static void size(String sourceFile) {
        try {
            File file = new File(sourceFile); // Create a File object for the source file
            if (!file.exists()) {
                System.out.println("Error: File not found: " + sourceFile);
                return;
            }
            System.out.println("Size: " + file.length() + " bytes");
        } catch (Exception ex) {
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

