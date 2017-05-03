package edu.asu.kr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class graphBuilder {
	
	public void writeGDF(int[][] cMatrix, String path, Set<String> rWords) throws IOException
	{
		FileWriter writer = new FileWriter(path);
		BufferedWriter buff = new BufferedWriter(writer);
		buff.write("nodedef> name,label");
		buff.write("\n");
		Iterator<String> it = rWords.iterator();
		for(int i=0; i<rWords.size(); i++)
		{
			buff.write("v"+i+","+it.next());
			buff.write("\n");
		}
		buff.write("edgedef> node1,node2,directed,weight,labelvisible");
		buff.write("\n");
		for(int i=0; i<rWords.size(); i++)
		{
			for(int j=0; j<rWords.size(); j++)
			{
				if(cMatrix[i][j]!=0)
				{
					buff.write("v"+i+","+"v"+j+","+"false"+","+cMatrix[i][j]+","+"true");
					buff.write("\n");
				}
			}
		}
		buff.close();
	}
	
	public void graphStage()
	{
		String path = "C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\DataSets\\";
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
		
		Set<String> rWords = new HashSet<String>();
		
		for(int i =0 ; i<fileNames.size(); i++)
		{
			
			String word = fp.getWordName(fileNames.get(i));
			String path1 = path +fileNames.get(i);
			System.out.println("file: "+path1);
			String[] sentences = fp.readSentences(path1);
			//System.out.println(sentences.length + " no of sentences read");
			String paragraph = fp.readParagraph(path1);
			//System.out.println(paragraph);
			System.out.println("*******************");
			corticalHelper ch = new corticalHelper();
			System.out.println("getting related words of " + word);
			//if(!data.containsKey(word))
			{
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				List<String> keyWords = new ArrayList<String>();
				map = ch.uniqueRelatedWords(word);
				keyWords = ch.allKeyWords(paragraph);
				System.out.println(keyWords.size() + " keywords obtained" + " and " + map.size() + " related words obtained");
				rWords = map.keySet();
				arWords.addAll(rWords);
				arWords.addAll(keyWords);
			}
			System.out.println("Building matrix for " +arWords.size() + " elements");	
		}
		matrixBuilder mb = new matrixBuilder();
		for(int i =0 ; i<fileNames.size(); i++)
		{
			String word = fp.getWordName(fileNames.get(i));
			String path1 = path +fileNames.get(i);
			System.out.println("file: "+path1);
			String[] sentences = fp.readSentences(path1);
			cMatrix = mb.coOccurrenceMatrix(cMatrix, arWords, sentences);
		}
		System.out.println("Matrix built");
		String path3 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\output.gdf";
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

}
