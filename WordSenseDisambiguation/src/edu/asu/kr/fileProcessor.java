package edu.asu.kr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class fileProcessor {
	
	public ArrayList<String> readDirectory(String path)
	{
		File folder = new File(path);
		File[] list = folder.listFiles();
		ArrayList<String> fileNames = new ArrayList<String>();
		for(int i=0; i<list.length; i++)
		{
			if(list[i].isFile())
			{
				fileNames.add(list[i].getName());
			}
		}
		
		return fileNames;
	}
	
	public String getWordName(String fileName)
	{
		if(fileName.contains("-"))
		{
			String[] words = fileName.split("-");
			String wordName = words[0];
			return wordName;
		}
		else
		{	
			if(fileName.contains("."))
			{
				System.out.println(fileName);
				String[] w = fileName.split("\\.");
				System.out.println("Word after split "+w.length );
				return w[0];
			}
		}
		
		return null;
	}
	public String readParagraph(String path)
	{
		try
		{
			FileReader reader = new FileReader(path);
			BufferedReader buff = new BufferedReader(reader);
			StringBuilder builder = new StringBuilder();
			if(buff.readLine()==null)
			{
				System.out.println("Nothing read");
			}
			while(buff.readLine()!=null)
			{
				//System.out.println(buff.readLine());
				builder.append(buff.readLine());
			}
			buff.close();
			String paragraph = builder.toString();
			//System.out.println(builder.toString()); 
			return paragraph;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return null;
	}
	public String[] readSentences(String path)
	{
		try
		{
			FileReader reader = new FileReader(path);
			BufferedReader buff = new BufferedReader(reader);
			StringBuilder builder = new StringBuilder();
			if(buff.readLine()==null)
			{
				System.out.println("Nothing read");
			}
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
