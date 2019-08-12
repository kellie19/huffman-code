import java.io.*;
import java.util.*;

// HuffmanTree2 can write the compressing rule and decompress the file.
public class HuffmanTree2 {
   private HuffmanNode overallRoot;
   
   //pre: an array of integers with the index representing the ascii code
   //         of the character and the vaule representing the frequency
   //         of it in the file.
   //post: create a code for the compressing rule of the file
   public HuffmanTree2(int[] count) {
      Queue<HuffmanNode> coding = new PriorityQueue <HuffmanNode>();
      for (int i = 0; i < count.length; i++) {
         if (count[i] > 0) {
            coding.add(new HuffmanNode(i, count[i]));
         }
      }
      coding.add(new HuffmanNode(count.length, 1));
      HuffmanNode first;
      HuffmanNode second;
      while (coding.size() > 1){
         first = coding.remove();
         second = coding.remove();
         coding.add(new HuffmanNode(first.frequency + second.frequency, first, second));
      }
      overallRoot = coding.remove();
   }
   
   //pre: input stream
   //post: read the header of compressing file to find the decompressing rule
   public HuffmanTree2(BitInputStream input) {
      overallRoot = buildTree(input);
   }
   
   //pre: input stream
   //post: read the header of compressing file to find the decompressing rule
   private HuffmanNode buildTree(BitInputStream input) {
      int isNode = input.readBit();
      HuffmanNode root;
      if (isNode == 1) {
         root = new HuffmanNode(read9(input), -1);
      } else {
         root = new HuffmanNode(-1);
         root.left = buildTree(input);
         root.right = buildTree(input);
      }
      return root;
   }
   
   //pre: an array of string
   //post: give the unique code for each character
   public void assign(String[] codes) {
      String path = "";
      assign(codes, overallRoot, path);
   }
   
   //pre: an array of string, a HuffmanNode object, a string of rule
   //post: give the unique code for each character
   private void assign(String[] codes, HuffmanNode root, String path) {
      if (root.left == null && root.right == null) {
         codes[root.bytes] = path;
      } else {
         path += "0";
         assign(codes, root.left, path);
         path = path.substring(0, path.length() - 1) + "1";
         assign(codes, root.right, path);
      }
   }
   
   //pre: a output stream
   //post: write the compressing rule at the header of compressing file
   public void writeHeader(BitOutputStream output) {
      writeHelper(output, overallRoot);
   }
   
   //pre: a output stream, a HuffmanNode object
   //post: write the compressing rule at the header of compressing file
   private void writeHelper(BitOutputStream output, HuffmanNode root) {
      if (root.left == null && root.right == null) {
         output.writeBit(1);
         write9(output, root.bytes);
      } else {
         output.writeBit(0);
         writeHelper(output, root.left);
         writeHelper(output, root.right);
      }
   }
   
   //pre: input stream of rule file, output stream, an integer represents the end of file
   //post: decompress the compressed file to a new file
   public void decode(BitInputStream input, PrintStream output, int eof) {
      int number = findNumber(input, overallRoot);
      while (number != eof) {
         output.write(number);
         number = findNumber(input, overallRoot);
      }
   }
   
   //pre: input stream of rule file, a HuffmanNode object
   //post: decompress the compressed file to a new file
   private int findNumber(BitInputStream input, HuffmanNode root) {
      int path;
      while (root.left != null && root.right != null) {
         path = input.readBit();
         if (path == 0) {
            root = root.left;
         } else {
            root = root.right;
         }
      }
      return root.bytes;
   }

   
   // pre : an integer n has been encoded using write9 or its equivalent
   // post: reads 9 bits to reconstruct the original integer
   private int read9(BitInputStream input) {
       int multiplier = 1;
       int sum = 0;
       for (int i = 0; i < 9; i++) {
           sum += multiplier * input.readBit();
           multiplier = multiplier * 2;
       }
       return sum;
   }

   // pre : 0 <= n < 512
   // post: writes a 9-bit representation of n to the given output stream
   private void write9(BitOutputStream output, int n) {
       for (int i = 0; i < 9; i++) {
           output.writeBit(n % 2);
           n = n / 2;
       }
   }
   
}
