// Kellie Gui
// CSE 143 BF with Chrish Thakalath
// Homework 8 Huffman
// The class is the tree nodes for the HuffmanTree program
// to store the integer value and the frequency of each character
// It implements the Comparable interface that can compare the frequency between nodes

public class HuffmanNode implements Comparable<HuffmanNode> {
   public int cha; // value of character stored at this HuffmanNode
   public int fre; // frequency of character appearance stored at this HuffmanNode
   public HuffmanNode left; // reference to left node
   public HuffmanNode right; // reference to right node
   
   // pre: an integer to present a character and its frequency
   //      if cha == 0, it means the character value is null
   // post: constructs a HuffmanNode with the value and the frequency
   public HuffmanNode(int cha, int fre) {
      this(cha, fre, null, null);
   }
   
   // pre: an integer to present a character and its frequency
   //      and left and right subtree direcion
   // post: constructs a HuffmanNode that has the value, frequency and left and right rubtree
   public HuffmanNode(int cha, int fre, HuffmanNode left, HuffmanNode right) {
      this.cha = cha;
      this.fre = fre;
      this.left = left;
      this.right = right;
   }
   
   // post: compare the frequency between nodes
   public int compareTo(HuffmanNode other) {
      return fre - other.fre;
   }
}