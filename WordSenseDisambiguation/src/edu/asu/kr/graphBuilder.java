package edu.asu.kr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
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

}
