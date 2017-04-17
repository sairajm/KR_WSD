package edu.asu.kr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.cortical.retina.client.FullClient;
import io.cortical.retina.client.LiteClient;
import io.cortical.retina.model.Context;
import io.cortical.retina.model.Term;
import io.cortical.retina.rest.JsonUtil;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.jlt.util.Language;

public class wordSense {
	
	public static void main(String args[])
	{
		String path = "C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\sample.txt";
		String path2 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\apple.txt";
		//String path = "sample.txt";
		wordSense obj = new wordSense();
		HashMap<String, Integer> words = new HashMap<>();
		String[] sentences = obj.readSentences(path);
		System.out.println("*******************");
		List<Term> relatedWords = obj.getRelatedWords("apple");
		HashMap<String, Integer> map = obj.removeDuplicates(relatedWords);
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
		cMatrix = obj.coOccurrenceMatrix(cMatrix, rWords, sentences);
		sentences = obj.readSentences(path2);
		cMatrix = obj.coOccurrenceMatrix(cMatrix, rWords, sentences);
		/*for(int i=0; i<length; i++)
		{
			for(int j=0; j<length; j++)
			{
				System.out.print(cMatrix[i][j] + " ");
			}
			System.out.print(" \n");
		}*/
		String path3 ="C:\\Users\\Sairaj\\workspace\\WordSenseDisambiguation\\src\\edu\\asu\\kr\\output.txt";
		try
		{
			obj.writeGDF(cMatrix, path3, rWords);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
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
	public HashMap<String, Integer> removeDuplicates(List<Term> relatedWords)
	{
		HashMap<String, Integer> rWords = new HashMap<>();
		for(Term s : relatedWords)
		{
			if(!rWords.containsKey(s.getTerm()))
			{
				rWords.put(s.getTerm(), 0);
			}
		}
		return rWords;
	}
	public int[][] coOccurrenceMatrix(int[][] cMatrix,Set<String> words, String[] sentences)
	{	
		Set<String> keys = words;
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
						//System.out.println("Found one " + w);
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
	public List<Term> getRelatedWords(String word)
	{
		//LiteClient lite = new LiteClient("f517da50-1d80-11e7-b22d-93a4ae922ff1");
		FullClient fullClient = new FullClient("f517da50-1d80-11e7-b22d-93a4ae922ff1");
		List<Term> relatedWords = new ArrayList<>();
		try
		{
			//relatedWords = lite.getSimilarTerms(word);
			List<Context> contexts = fullClient.getContextsForTerm(word);
			for(Context c : contexts)
			{
				//System.out.println(c.getContextId());
				relatedWords.addAll(fullClient.getSimilarTermsForTerm(word, c.getContextId(), null, 0, 20, true));
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return relatedWords;
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
