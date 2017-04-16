package edu.asu.kr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.cortical.retina.client.LiteClient;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.jlt.util.Language;

public class wordSense {
	
	public static void main(String args[])
	{
		String path = "C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\sample.txt";
		//String path = "sample.txt";
		wordSense obj = new wordSense();
		HashMap<String, Integer> words = new HashMap<>();
		String[] sentences = obj.readSentences(path);
		System.out.println("*******************");
		for(String s : sentences)
		{
			System.out.println(s);
		}
		System.out.println("*******************");
		words = obj.getFrequency(sentences);
		Set<String> set = words.keySet();
		System.out.println("Words and Frequency");
		for(String s : set)
		{
			System.out.println("key " + s + " value " + words.get(s));
		}
		System.out.println("*******************");
		int[][]cMatrix = obj.coOccurrenceMatrix(words, sentences);
		int length = words.size();
		for(int i=0; i<length; i++)
		{
			for(int j=0; j<length; j++)
			{
				System.out.print(cMatrix[i][j] + " ");
			}
			System.out.print(" \n");
		}
		
	}
	public int[][] coOccurrenceMatrix(HashMap<String, Integer> words, String[] sentences)
	{
		int length = words.size();
		int[][] cMatrix = new int[length][length];
		for(int i=0; i<length; i++)
		{
			for(int j=0; j<length; j++)
			{
				cMatrix[i][j]=0;
			}
		}
		Set<String> keys = words.keySet();
		int outer = 0;
		for(String key : keys)
		{
			boolean start = false;
			for(String s : sentences)
			{
				String[] word = s.split(" ");
				for(String w : word)
				{
					w = w.toLowerCase();
					if(start)
					{
						int index = getIndex(keys, w);
						int inner = cMatrix[outer][index];
						inner = inner + 1;
						cMatrix[outer][index] = inner;
					}
					if(w.equals(key))
					{
						start = true;
					}
					
				}
				start = false;
			}	
			outer = outer + 1;
		}
		
		return cMatrix;
	}
	public int getIndex(Set<String> keys, String w)
	{
		int count = 0;
		for(String s : keys)
		{
			if(s.equals(w))
			{
				return count;
			}
			count = count + 1;
		}
		return count;
	}
	public HashMap<String, Integer> getFrequency(String[] sentences)
	{
		HashMap<String, Integer> words = new HashMap<>();
		for(String s : sentences)
		{
			String[] word = s.split(" ");
			for(String w : word)
			{
				if(words.containsKey(w.toLowerCase())==false)
				{
					words.put(w.toLowerCase(), 1);
				}
				else
				{
					int count = words.get(w.toLowerCase());
					count = count + 1;
					words.put(w.toLowerCase(), count);
				}
			}
		}
		return words;
	}
	public List<BabelSense> getSenses(String word)throws IOException
	{
		BabelNet bn = BabelNet.getInstance();
		List<BabelSense> senses = bn.getSenses(word, Language.EN);
		
		return senses;
	}
	public List<List<String>> getKeywords(String[] sentences)
	{
		LiteClient lite = new io.cortical.retina.client.LiteClient("f517da50-1d80-11e7-b22d-93a4ae922ff1");
		List<List<String>> keywords = new ArrayList<>();
		for(String s : sentences)
		{
			try
			{
				keywords.add(lite.getKeywords(s));
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		
		
		return keywords;
	}

	public String[] readSentences(String path)
	{
		try
		{
			FileReader reader = new FileReader(path);
			BufferedReader buff = new BufferedReader(reader);
			StringBuilder builder = new StringBuilder();
			while(buff.readLine()!=null)
			{
				//System.out.println(buff.readLine());
				builder.append(buff.readLine());
			}
			buff.close();
			String paragraph = builder.toString();
			//System.out.println(paragraph);
			//http://stackoverflow.com/questions/21430447/how-to-split-paragraphs-into-sentences
			String[] sentences = paragraph.split("(?i)(?<=[.?!])\\S+(?=[a-z])"); 
			return sentences;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return null;
	}
}
