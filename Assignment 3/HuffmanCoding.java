import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding           
{ 
    private String fileName;
    private ArrayList <CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT 
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f)        
    { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList()   
    {
        StdIn.setFile(fileName); char ch = Character.MIN_VALUE; 
        
        ArrayList <Character> character = new ArrayList <Character> ();
        
        //Record all the characters from the input file
        while (StdIn.hasNextChar())
        {
            ch = StdIn.readChar(); character.add(ch);
        }
        
        //Each element of sortedCharFreqList has a character and frequency
        sortedCharFreqList = new ArrayList <CharFreq> ();
        sortedCharFreqList.add(new CharFreq(character.get(0), 1)); 
        
        for (int index = 1; index < character.size(); index++)
        {
            if (character.get(index) == character.get(index - 1))
            {
                for (int place = 0; place < sortedCharFreqList.size(); place++)
                {
                    if (sortedCharFreqList.get(place).getCharacter() == character.get(index))
                    {
                        sortedCharFreqList.get(place).setProbOcc(sortedCharFreqList.get(place).getProbOcc() + 1);
                    }
                }
            }
            
            else
            {
                boolean isExistent = false;
                
                for (int place = 0; place < sortedCharFreqList.size(); place++)
                {
                    if (sortedCharFreqList.get(place).getCharacter() == character.get(index))
                    {
                        isExistent = true;
                    }
                }
                
                if (isExistent)
                {
                    for (int place = 0; place < sortedCharFreqList.size(); place++)
                    {
                        if (sortedCharFreqList.get(place).getCharacter() == character.get(index))
                        {
                            sortedCharFreqList.get(place).setProbOcc(sortedCharFreqList.get(place).getProbOcc() + 1);
                        }
                    }
                }
                
                else
                {
                    sortedCharFreqList.add(new CharFreq(character.get(index), 1));
                }
            }
        }
        
        //Insert the occurence probability of each character
        for (int index = 0; index < sortedCharFreqList.size(); index++) 
        {
            sortedCharFreqList.get(index).setProbOcc(sortedCharFreqList.get(index).getProbOcc() / character.size());
        }
        
        //Add a null companion if sortedCharFreqListhas only one distinct element
        if (sortedCharFreqList.size() == 1)
        {
            int asciiRep = sortedCharFreqList.get(0).getCharacter();
            
            if (asciiRep == 127)
            {
                asciiRep = 0; sortedCharFreqList.add(new CharFreq((char) asciiRep, 0));
            }
            
            else
            {
                asciiRep++; sortedCharFreqList.add(new CharFreq((char) asciiRep, 0));
            }
        }
        
        //Sort the sortedCharFreqList
        Collections.sort(sortedCharFreqList);
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot 
     */
    public void makeTree()           
    {
        Queue <TreeNode> source = new Queue <TreeNode> (), target = new Queue <TreeNode> ();
        TreeNode left, right; 
        
        for (int index = 0; index < sortedCharFreqList.size(); index++)
        {
            source.enqueue(new TreeNode(sortedCharFreqList.get(index), null, null));
        }
        
        while ((!source.isEmpty()) || (target.size() != 1))   
        {
            if (target.isEmpty())
            {
                left = source.dequeue(); right = source.dequeue();
                double sum = left.getData().getProbOcc() + right.getData().getProbOcc();
                target.enqueue(new TreeNode(new CharFreq(null, sum), left, right));
            }
            
            else if (target.size() == 1) 
            {
                if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                {
                    left = target.dequeue(); right = source.dequeue();
                }
                    
                else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                {
                    left = source.dequeue(); right = target.dequeue();
                }
                    
                else
                {
                    left = source.dequeue(); 
                    
                    if (source.isEmpty())
                    {
                        right = target.dequeue();
                    }
                    
                    else
                    {
                        if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                        {
                            right = target.dequeue();
                        }
                    
                        else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                        {
                            right = source.dequeue();
                        }
                    
                        else
                        {
                            right = source.dequeue();
                        }
                    } 
                }
                
                double sum = left.getData().getProbOcc() + right.getData().getProbOcc();
                target.enqueue(new TreeNode(new CharFreq(null, sum), left, right)); 
            }
            
            else //target.size() == 2
            {
                if (source.isEmpty())
                {
                    left = target.dequeue(); right = target.dequeue();
                }
                
                else if (source.size() == 1)
                {
                    if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                    {
                        left = target.dequeue();
                        
                        if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                        {
                            right = target.dequeue();
                        }
                        
                        else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                        {
                            right = source.dequeue();
                        }
                        
                        else
                        {
                            right = source.dequeue();
                        }
                    }
                    
                    else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                    {
                        left = source.dequeue(); right = target.dequeue();
                    }
                    
                    else
                    {
                        left = source.dequeue(); right = target.dequeue();
                    }
                }
                
                else
                {
                    if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                    {
                        left = target.dequeue();
                        
                        if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                        {
                            right = target.dequeue();
                        }
                        
                        else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                        {
                            right = source.dequeue();
                        }
                        
                        else
                        {
                            right = source.dequeue();
                        }
                    }
                    
                    else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                    {
                        left = source.dequeue(); 
                        
                        if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                        {
                            right = target.dequeue();
                        }
                        
                        else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                        {
                            right = source.dequeue();
                        }
                        
                        else
                        {
                            right = source.dequeue();
                        }
                    }
                    
                    else
                    {
                        left = source.dequeue();
                        
                        if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                        {
                            right = target.dequeue();
                        }
                        
                        else if (target.peek().getData().getProbOcc() == source.peek().getData().getProbOcc())
                        {
                            right = source.dequeue();
                        }
                        
                        else
                        {
                            right = source.dequeue();
                        }
                    }
                }
                
                double sum = left.getData().getProbOcc() + right.getData().getProbOcc();
                target.enqueue(new TreeNode(new CharFreq(null, sum), left, right)); 
            } 
        } 
        
        huffmanRoot = target.dequeue();  
    }  
    
    private void traverse(TreeNode parent, String str)
    {
        String rep = str;
        
        if (parent.getData().getCharacter() != null)
        {
            encodings[(int) parent.getData().getCharacter()] = rep; return;
        }
        
        str += "0"; traverse(parent.getLeft(), str); str = str.substring(0, str.length() - 1);
        str += "1"; traverse(parent.getRight(), str);
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() 
    {
        encodings = new String[128]; traverse(huffmanRoot, "");
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) 
    {
        StdIn.setFile(fileName); char letter = Character.MIN_VALUE;
        
        ArrayList <Character> ref = new ArrayList <Character> ();
        
        while (StdIn.hasNextChar())
        {
            letter = StdIn.readChar(); ref.add(letter);
        }
        
        String string = "";
        
        for (int index = 0; index < ref.size(); index++)
        {
            string += encodings[(int) ref.get(index)];
        }
        
        writeBitString(encodedFile, string);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) 
    {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8); String pad = "";
        
        for (int i = 0; i < padding - 1; i++) 
        {
            pad += "0";
        }
        
        pad += "1"; bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) 
        {
            if (c != '1' && c != '0') 
            {
                System.out.println("Invalid characters in bitstring"); return;
            }

            if (c == '1') 
            {
                currentByte += 1 << (7-byteIndex);
            }
            
            byteIndex++;
            
            if (byteIndex == 8) 
            {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++; currentByte = 0; byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try 
        {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes); out.close();
        }
        
        catch (Exception e) 
        {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) 
    { 
        StdOut.setFile(decodedFile); 
        String stream = readBitString(encodedFile); TreeNode ref = huffmanRoot;
        String context = "";
        
        for (int index = 0; index < stream.length(); index++) 
        {
            if (stream.charAt(index) == '0')
            {
                huffmanRoot = huffmanRoot.getLeft();
                
                if ((huffmanRoot.getLeft() == null) && (huffmanRoot.getRight() == null))
                {
                    context += huffmanRoot.getData().getCharacter(); huffmanRoot = ref;
                }
            }
            
            else if (stream.charAt(index) == '1')
            {
                huffmanRoot = huffmanRoot.getRight();
                
                if ((huffmanRoot.getLeft() == null) && (huffmanRoot.getRight() == null))
                {
                    context += huffmanRoot.getData().getCharacter(); huffmanRoot = ref;
                }
            }
        }
        
        StdOut.print(context);
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) 
    {
        String bitString = "";
        
        try 
        {
            FileInputStream in = new FileInputStream(filename); File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes); in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) 
            {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) 
            {
                if (bitString.charAt(i) == '1') 
                {
                    return bitString.substring(i+1);
                }
            }
            
            return bitString.substring(8);
        }
        
        catch (Exception e) 
        {
            System.out.println("Error while reading file!"); return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() 
    { 
        return fileName; 
    }

    public ArrayList <CharFreq> getSortedCharFreqList() 
    { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() 
    { 
        return huffmanRoot; 
    }

    public String[] getEncodings() 
    { 
        return encodings; 
    }
}
