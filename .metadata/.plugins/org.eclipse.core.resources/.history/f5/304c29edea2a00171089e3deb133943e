package edu.asu.kr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.cortical.retina.client.FullClient;
import io.cortical.retina.client.LiteClient;
import io.cortical.retina.model.Context;
import io.cortical.retina.model.Term;

public class corticalHelper {
	
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
	public List<String> allKeyWords(String s)
	{
		FullClient fullClient = new FullClient("f517da50-1d80-11e7-b22d-93a4ae922ff1");
		List<String> keywords = new ArrayList<>();
		try
		{
			keywords = fullClient.getKeywordsForText(s);
		}
		catch(Exception e)
		{
			System.out.println(e);
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
	
	public HashMap<String, Integer> uniqueRelatedWords(String word)
	{
		HashMap<String, Integer> map;
		List<Term> relatedWords;
		try
		{
			relatedWords = getRelatedWords(word);
			map = removeDuplicates(relatedWords);
			return map;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return null;
	}
}
