Word Sense Disambiguation with the use of Knowledge Graphs

WSD is a complex task in AI and there have been several approaches to solve this problem, our approach uses Knowledge Graphs. The Knowledge Graph is built with the help of a cooccurrence matrix. Cooccurrence matrices is a very simple measure, based on our evaluation using such a simple measure produced good results when compared to the baseline of SemEval-I. 

How to use:
In wordSenseDisambiguation.java, change the input sentence and in datasets folder, add all training documents related to the word you would like to disambiguate and set the two boolean values in wordSenseDisambiguation to true to build the graph and find the similarity.

Output:
Outputs the cluster which has highest similarity to the context. This will represent the sense of the word for that context.
