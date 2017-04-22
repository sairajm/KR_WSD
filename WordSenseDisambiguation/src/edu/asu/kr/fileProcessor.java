package edu.asu.kr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class fileProcessor {
	
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

}
