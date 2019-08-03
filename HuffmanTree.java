// Kellie Gui
// CSE 143 BF with Chrish Thakalath
// Homework 8 Huffman
// This program constructs a HuffmanTree with given input;
// compresseses a text file by using Huffman algorithm
// encodes the file and print the tree with code to the output file in standard format 
// decompresses with given encoded file of standard format and reconstructs a tree from it 
// decodes the huffman code file by using the new tree and print the text to the output file

import java.util.*;
import java.io.*;

public class HuffmanTree {
   private HuffmanNode overallRoot;
   
   // pre: an integer array of frequency of characters
   // post: constructs a HuffmanTree that has the frequency and value presents the character
   //       the tree only has characters which have frequency larger than 0
   //       comparing the frequency of each character and build the tree from lower frequency 
   //       to higher frequency, where the top of the tree is the highest frequency and the 
   //       bottom of the tree has the lowest frequency 
   //       lower frequency is considered "less" than higher frequency 
   //       equal frenquency is considered equal 
   //       
   public HuffmanTree(int[] count) {
      Queue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < count.length; i++) {
         if(count[i] > 0) {
            q.add(new HuffmanNode(i, count[i]));
         }
      }
      q.add(new HuffmanNode(count.length, 1)); // add eof character to the end
      while(q.size() > 1) {
         HuffmanNode node1 = q.remove();
         HuffmanNode node2 = q.remove();
         q.add(new HuffmanNode(0, node1.fre + node2.fre, node1, node2));
      }
      overallRoot = q.remove();
   }
   
   // pre: an input file that in standard format 
   // post: reconstructs the tree following the code in the file
   //       the tree order strictly follows the format in the file
   public HuffmanTree(Scanner input) {
      overallRoot = new HuffmanNode(0,0);
      while(input.hasNextLine()) {
         int n = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = buildTree(input, overallRoot, n, code);
      }
   }
   
   // post: a private helper method to construct the tree
   private HuffmanNode buildTree(Scanner input, HuffmanNode root, int n, String code) {
      if(code.length() == 0) {
         root.cha = n;
      } else if(code.charAt(0) == '0') {
         if(root.left == null) {
            root.left = new HuffmanNode(0,0);
         }
         root.left = buildTree(input, root.left, n, code.substring(1));
      } else {
         if(root.right == null) {
            root.right = new HuffmanNode(0,0);
         }
         root.right = buildTree(input, root.right,n, code.substring(1));
      }
      return root;
   }
   
   
   // pre: an input file of bits, an output file
   // post: decodes the bits file and print the decoded text to the output file
   //       the input file of bits is the path to find the value in the tree
   public void decode(BitInputStream input, PrintStream output, int eof) {
      int number = findNum(overallRoot, input);
      while (number != eof) {
         output.write(number);
         number = findNum(overallRoot, input);
      }
   }
   
   // post: a private helper method to help the find the integer that presents the character
   private int findNum(HuffmanNode root, BitInputStream input) {
      while (root.left != null && root.right != null) {
         int code = input.readBit();
         if (code == 0) {
            root = root.left;
         } else {
            root = root.right;
         }
      }
      return root.cha;
   }
   
   // pre: a given output file
   // post: encode the tree and write the tree to the given output stream in standard format
   //       the first line is the value presented the character and the second line is its code
   //       encode the tree by following the Huffman coding scheme
   //       (adding 0 for left subtree and 1 for right subtree)
   public void write(PrintStream output) {
      write(output, overallRoot, "");
   }
   
   // post: a private helper method to help code and write the tree to the output file
   private void write(PrintStream output, HuffmanNode root, String result) {
      if(root.left == null && root.right == null) {
         output.println(root.cha);
         output.println(result);
      } else {
         write(output, root.left, result + "0");
         write(output, root.right, result + "1");
      }
   }
}