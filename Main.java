import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choiceStr, sourceFile, resultFile, firstFile, secondFile;
        // start of loop of comands
        loop:
        while (true) {
            System.out.println("Enter command (comp, decomp, size, equal, about, exit): ");
            choiceStr = sc.next();
            //izvēlas funkcijju
            switch (choiceStr) {
                //herta elizabete anete
                case "comp":
                    System.out.print("source file name: ");
                    sourceFile = sc.next();
                    System.out.print("archive name: ");
                    resultFile = sc.next();
                    compress(sourceFile, resultFile);
                    break;
                    //liāna estere
                case "decomp":
                    System.out.print("archive name: ");
                    sourceFile = sc.next();
                    System.out.print("file name: ");
                    resultFile = sc.next();
                    decompress(sourceFile, resultFile);
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
                default:
                    System.out.println("Invalid command.");
            }
        }
        sc.close();
    }

  // compress calculations print
  // print info for user
    public static void compress(String sourceFile, String resultFile) {
        try {
            String content = readFileContent(sourceFile);
            System.out.println("Original content size: " + content.length() + " bytes");

            if (content.length() < 5) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
                    writer.write("RAW\n" + content);
                }
                System.out.println("Small file saved without compression.");
                return;
            }

            String lz77Compressed = lz77Compress(content);
            System.out.println("LZ77 compressed size: " + lz77Compressed.length());

            Map<Character, String> huffmanCodes = buildHuffmanTree(lz77Compressed);
            String huffmanEncoded = encodeHuffman(lz77Compressed, huffmanCodes);
            System.out.println("Huffman encoded size (in bits): " + huffmanEncoded.length());

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(resultFile));
                 BitOutputStream bitOut = new BitOutputStream(resultFile + ".bin")) {

                out.writeObject(huffmanCodes); // Huffman table
                for (char c : huffmanEncoded.toCharArray()) {
                    bitOut.writeBit(c == '1' ? 1 : 0); // Write individual bits
                }
                System.out.println("File compressed successfully and saved to: " + resultFile + ".bin");
            }

        } catch (IOException e) {
            System.out.println("Error during compression: " + e.getMessage());
        }
    }

    private static String readFileContent(String sourceFile) throws IOException {
        return new String(Files.readAllBytes(Paths.get(sourceFile)));
    }
    // elizabete
    private static String lz77Compress(String input) {
        StringBuilder result = new StringBuilder();
        int windowSize = 1024;
        int bufferSize = 15;
        int i = 0;
        // lz77 comp for loop to get the code like - (0,0,a)
        while (i < input.length()) {
            int maxMatchLength = 0, offset = 0;
            // herta
            for (int j = 1; j <= bufferSize && i + j <= input.length(); j++) {
                String match = input.substring(i, i + j);
                int searchStart = Math.max(0, i - windowSize);
                String window = input.substring(searchStart, i);

                int index = window.indexOf(match);
                if (index != -1 && j > 2) {
                    maxMatchLength = j;
                    offset = window.length() - index;
                }
            }

            if (maxMatchLength > 2) {
                result.append("(").append(offset).append(",").append(maxMatchLength).append(")");
                i += maxMatchLength;
            } else {
                result.append(input.charAt(i));
                i++;
            }
        }
        return result.toString();
    }
    //anete
    private static Map<Character, String> buildHuffmanTree(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.freq));
        for (var entry : freqMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }
        // get frequency
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq);
            parent.left = left;
            parent.right = right;
            pq.add(parent);
        }

        Map<Character, String> codes = new HashMap<>();
        generateCodes(pq.poll(), "", codes);
        return codes;
    }
// write in file comp
    private static void generateCodes(Node node, String code, Map<Character, String> codes) {
        if (node == null) return;
        if (node.left == null && node.right == null) {
            codes.put(node.ch, code);
        }
        generateCodes(node.left, code + "0", codes);
        generateCodes(node.right, code + "1", codes);
    }

    private static String encodeHuffman(String input, Map<Character, String> codes) {
        StringBuilder encoded = new StringBuilder();
        for (char c : input.toCharArray()) {
            encoded.append(codes.get(c));
        }
        return encoded.toString();
    }

    public static void decompress(String sourceFile, String resultFile) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(sourceFile));
            @SuppressWarnings("unchecked")
            Map<Character, String> huffmanCodes = (Map<Character, String>) in.readObject();
            StringBuilder encodedData = new StringBuilder();

            try (BitInputStream bitIn = new BitInputStream(sourceFile + ".bin")) {
                while (true) {
                    int bit = bitIn.readBit();
                    if (bit == -1) break;
                    encodedData.append(bit == 1 ? '1' : '0');
                }
            }
            in.close();

            String lz77Decompressed = decodeHuffman(encodedData.toString(), huffmanCodes);
            String originalContent = lz77Decompress(lz77Decompressed);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
                writer.write(originalContent);
            }
            System.out.println("File decompressed successfully.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during decompression: " + e.getMessage());
        }
    }
    //estere
    private static String decodeHuffman(String encodedData, Map<Character, String> huffmanCodes) {
        StringBuilder decoded = new StringBuilder();
        Map<String, Character> reverseCodes = new HashMap<>();

        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            reverseCodes.put(entry.getValue(), entry.getKey());
        }

        StringBuilder temp = new StringBuilder();
        for (char c : encodedData.toCharArray()) {
            temp.append(c);
            if (reverseCodes.containsKey(temp.toString())) {
                decoded.append(reverseCodes.get(temp.toString()));
                temp.setLength(0);
            }
        }
        return decoded.toString();
    }
    // liāna
    private static String lz77Decompress(String compressed) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < compressed.length()) {
            // Check if the current character starts an LZ77 reference
            if (compressed.charAt(i) == '(') {
                // Find the end of the offset and length
                int offsetEnd = compressed.indexOf(',', i);
                int lengthEnd = compressed.indexOf(')', offsetEnd);

                // If the format is malformed (missing comma or parenthesis), handle it
                if (offsetEnd == -1 || lengthEnd == -1) {
                    result.append(compressed.charAt(i));  // Append the character anyway
                    i++;
                    continue;
                }

                try {

                    String offsetStr = compressed.substring(i + 1, offsetEnd).trim();
                    String lengthStr = compressed.substring(offsetEnd + 1, lengthEnd).trim();


                    if (!offsetStr.matches("\\d+") || !lengthStr.matches("\\d+")) {
                        
                        result.append(compressed.charAt(i));  // Append the character anyway
                        i++;
                        continue;
                    }


                    int offset = Integer.parseInt(offsetStr);
                    int length = Integer.parseInt(lengthStr);


                    if (offset > result.length() || length < 0) {

                        result.append(compressed.charAt(i));  // Append the character anyway
                        i++;
                        continue;
                    }


                    int start = result.length() - offset;
                    for (int j = 0; j < length; j++) {
                        result.append(result.charAt(start + j));
                    }


                    i = lengthEnd + 1;
                } catch (NumberFormatException e) {

                    result.append(compressed.charAt(i));
                    i++;
                }
            } else {

                result.append(compressed.charAt(i));
                i++;
            }
        }

        return result.toString();
    }



    public static boolean equal(String firstFile, String secondFile) {
        try {
            return Arrays.equals(Files.readAllBytes(Paths.get(firstFile)), Files.readAllBytes(Paths.get(secondFile)));
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static void size(String sourceFile) {
        File file = new File(sourceFile);
        System.out.println("size: " + file.length() + " bytes");
    }


    public static void about() {
        System.out.println("241RDB250 Elizabete Smirnova");
        System.out.println("241RDB206 Estere Gristiņa");
        System.out.println("241RDB227 Liāna Usāne");
        System.out.println("241RDB180 Anete Kupča");
        System.out.println("241RDB177 Herta Matisone");
    }

    static class Node {
        char ch;
        int freq;
        Node left, right;

        Node(char ch, int freq) {
            this.ch = ch;
            this.freq = freq;
        }
    }

    public static class BitOutputStream implements Closeable {
        private FileOutputStream out;
        private int currentByte;
        private int numBitsFilled;

        public BitOutputStream(String fileName) throws IOException {
            out = new FileOutputStream(fileName);
            currentByte = 0;
            numBitsFilled = 0;
        }

        public void writeBit(int bit) throws IOException {
            if (bit != 0 && bit != 1)
                throw new IllegalArgumentException("Bit must be 0 or 1");
            currentByte = (currentByte << 1) | bit;
            numBitsFilled++;
            if (numBitsFilled == 8) {
                out.write(currentByte);
                currentByte = 0;
                numBitsFilled = 0;
            }
        }

        @Override
        public void close() throws IOException {
            while (numBitsFilled != 0)
                writeBit(0);
            out.close();
        }
    }
// close if error
    public static class BitInputStream implements Closeable {
        private FileInputStream in;
        private int currentByte;
        private int numBitsRemaining;

        public BitInputStream(String fileName) throws IOException {
            in = new FileInputStream(fileName);
            currentByte = 0;
            numBitsRemaining = 0;
        }

        public int readBit() throws IOException {
            if (numBitsRemaining == 0) {
                currentByte = in.read();
                if (currentByte == -1)
                    return -1;
                numBitsRemaining = 8;
            }
            numBitsRemaining--;
            return (currentByte >> numBitsRemaining) & 1;
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }
}

