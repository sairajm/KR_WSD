package edu.asu.kr;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.jlt.util.Language;

public class wordSense {
	
	public static void main(String args[])
	{
		String path = "C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\DataSets\\";
		//String path2 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\apple.txt";
		//String path = "sample.txt";
		int[][] cMatrix = new int[3000][3000];
		for(int i=0; i<1000; i++)
		{
			for(int j=0; j<1000; j++)
			{
				cMatrix[i][j]=0;
			}
		}
		fileProcessor fp = new fileProcessor();
		ArrayList<String> fileNames = fp.readDirectory(path);
		Set<String> arWords = new HashSet<String>();
		HashMap<String,Integer> data = new HashMap<String,Integer>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Set<String> rWords = new HashSet<String>();
		for(int i =0 ; i<fileNames.size(); i++)
		{
			
			String word = fp.getWordName(fileNames.get(i));
			String path1 = path +fileNames.get(i);
			System.out.println("file: "+path1);
			String[] sentences = fp.readSentences(path1);
			System.out.println("*******************");
			corticalHelper ch = new corticalHelper();
			System.out.println("getting related words of " + word);
			//if(!data.containsKey(word))
			{
				map = ch.uniqueRelatedWords(word);
				rWords = map.keySet();
				arWords.addAll(rWords);
				//data.put(word, 0);
			}
			System.out.println("Building matrix for " +arWords.size() + " elements");
			matrixBuilder mb = new matrixBuilder();
			cMatrix = mb.coOccurrenceMatrix(cMatrix, rWords, sentences);
			System.out.println("Matrix built");
			System.out.println("Matrix size now " +cMatrix.length);
		}
		String path3 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\output.txt";
		graphBuilder graph = new graphBuilder();
		try
		{
			graph.writeGDF(cMatrix, path3, arWords);
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
