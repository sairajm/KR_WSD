package edu.asu.kr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cortical.retina.client.LiteClient;
import it.uniroma1.lcl.babelnet.BabelNet;
import it.uniroma1.lcl.babelnet.BabelSense;
import it.uniroma1.lcl.jlt.util.Language;

public class wordSense {
	
	
	public HashMap<String, Integer> getFrequency(String[] sentences)
	{
		
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
				builder.append(buff.readLine());
			}
			
			String paragraph = builder.toString();	
			String[] sentences = paragraph.split(".");
			buff.close();
			return sentences;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return null;
	}
}
