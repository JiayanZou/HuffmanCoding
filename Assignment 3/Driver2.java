
/**
 * Write a description of class Driver2 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.util.ArrayList;
import java.text.DecimalFormat;
public class Driver2
{
    private static final DecimalFormat df = new DecimalFormat("0.00");
    
    public static void main(String[] args)
    {
        HuffmanCoding target = new HuffmanCoding(args[0]);
        //DecimalFormat df = new DecimalFormat("0.00");
        
        target.makeSortedList();
        ArrayList <CharFreq> sortedList = target.getSortedCharFreqList(); double val = 0;
        
        
        
    }
}
