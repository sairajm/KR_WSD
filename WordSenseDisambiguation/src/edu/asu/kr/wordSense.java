package edu.asu.kr;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.jlt.util.Language;

public class wordSense {
	
	public static void main(String args[])
	{
		String path = "C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\sample.txt";
		String path2 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\apple.txt";
		//String path = "sample.txt";
		fileProcessor fp = new fileProcessor();
		String[] sentences = fp.readSentences(path);
		System.out.println("*******************");
		corticalHelper ch = new corticalHelper();
		//List<Term> relatedWords = obj.getRelatedWords("apple");
		HashMap<String, Integer> map = ch.uniqueRelatedWords("apple");
		Set<String> rWords = map.keySet();
		for(String s : rWords)
		{
			System.out.println(s);
		}
		int length = rWords.size();
		int[][] cMatrix = new int[length+1][length+1];
		for(int i=0; i<length; i++)
		{
			for(int j=0; j<length; j++)
			{
				cMatrix[i][j]=0;
			}
		}
		
		matrixBuilder mb = new matrixBuilder();
		cMatrix = mb.coOccurrenceMatrix(cMatrix, rWords, sentences);
		sentences = fp.readSentences(path2);
		cMatrix = mb.coOccurrenceMatrix(cMatrix, rWords, sentences);
		String path3 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\output.txt";
		graphBuilder graph = new graphBuilder();
		try
		{
			graph.writeGDF(cMatrix, path3, rWords);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public List<BabelSense> getSenses(String word)throws IOException
	{
		BabelNet bn = BabelNet.getInstance();
		List<BabelSense> senses = bn.getSenses(word, Language.EN);
		
		return senses;
	}
	
	
}
