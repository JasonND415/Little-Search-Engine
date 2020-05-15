package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine2 {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine2() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if(docFile==null)
		{
			throw new FileNotFoundException("no file");
		}
		HashMap <String,Occurrence> mm=new HashMap<String,Occurrence>(1000,2.0f);
		String j="";
		Scanner s=new Scanner(new File(docFile));
		while(s.hasNext())
		{
				j=s.next();
				j=getKeyword(j);
				if (j==null)
				{
					continue;
				}
				else
				{
					if(mm.containsKey(j))
					{
						mm.get(j).frequency++;
					}
					else
					{
						Occurrence jt=new Occurrence(docFile,1);
						mm.put(j,jt);
					}
				}
		}
		return mm;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String key:kws.keySet())
		{
			Occurrence r=new Occurrence(kws.get(key).document,kws.get(key).frequency);
			if (keywordsIndex.containsKey(key))
			{
				keywordsIndex.get(key).add(r);
				insertLastOccurrence(keywordsIndex.get(key));
			}
			else
			{
				ArrayList <Occurrence>j=new ArrayList<Occurrence>();
				j.add(r);
				keywordsIndex.put(key,j);
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code	
		word=word.toLowerCase();
		boolean r=true;
		int d=0;
		for (int i=0;i<word.length();i++)
		{
			char c=word.charAt(i);
			if ((c=='.' ||  c==',' ||c=='?' ||c==':' ||c==';' ||c=='!') && i==0)
			{
				return null;
			}
			if ((c=='.' ||  c==',' ||c=='?' ||c==':' ||c==';' ||c=='!') && r==true)
			{
				d=i;
				r=false;
			}
			else if ((c=='.' ||  c==',' ||c=='?' ||c==':' ||c==';' ||c=='!') && r==false)
			{
				r=false;
			}
			else if (Character.isLetter(c)==false)
			{
				return null;
			}
			else
			{
				if(d!=0 && r==false)
				{
					return null;
				}
			}
		}
		if (d==0)
		{
			d=word.length();
		}
		String r2=word.substring(0,d);
		if (noiseWords.contains(r2))
		{
			return null;
		}
		return r2;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if(occs.size()==1 || occs.size()==0)
		{
			return null;
		}
		ArrayList<Integer>test=new ArrayList<Integer>();
		int c=occs.size()-1;
		int target=occs.get(c).frequency;
		int low=occs.size()-2;
		int hi=0;
		int middle=0;
		while (hi<=low ) {
			middle=(low+hi)/2;   
			test.add(middle);
			if (occs.get(middle).frequency==target)
			{	
				occs.add(middle, occs.get(c));
				occs.remove(c+1);
				return test;     
			}
			else if(occs.get(middle).frequency>target)
			{
				hi=middle+1;
			}
			else
			{
				low=middle-1;
			}
		}
		if(target>occs.get(middle).frequency)
		{
			occs.add(middle, occs.get(c));
			occs.remove(c+1);
			return test;  
		}
		else
		{
			occs.add(middle+1, occs.get(c));
			occs.remove(c+1);
			return test;  
		}
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		ArrayList <Occurrence>j=new ArrayList<Occurrence>();
		ArrayList <Occurrence>k=new ArrayList<Occurrence>();
		ArrayList <String>list=new ArrayList<String>();
		int c=0;
		int c2=0;
		if(!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2))
		{
			return null;
		}
		if(keywordsIndex.containsKey(kw1))
		{
			while ( c!=keywordsIndex.get(kw1).size())
			{
				j.add(keywordsIndex.get(kw1).get(c));
				c++;
			}
		}
		c=0;
		if(keywordsIndex.containsKey(kw2))
		{
			while (c!=keywordsIndex.get(kw2).size())
			{
				k.add(keywordsIndex.get(kw2).get(c));
				c++;
			}
		}
		c=0;
		String temp1="";
		String temp2="";
		int temp3=0;
		int temp4=0;
		int c3=0;
		boolean no=true;
		while(list.size()!=5)
		{
			if(j.size()>c)
			{
				temp1=j.get(c).document;
				temp3=j.get(c).frequency;
			}
			if(k.size()>c2)
			{
				temp2=k.get(c2).document;
				temp4=k.get(c2).frequency;
			}
			if(temp3==0 && temp4==0)
			{
				break;
			}
			while(c3!=list.size())
			{
				String r=list.get(c3);
				if (temp1.equals(r))
				{
					j.remove(c);
					no=false;
				}
				if (temp2.equals(r))
				{
					k.remove(c2);
					no=false;
				}
				c3++;
			}
			c3=0;
			if (no==true) 
			{
				if (temp3==0)
				{
					list.add(temp2);
					c2++;
				}
				else if (temp4==0)
				{
					list.add(temp1);
					c++;
				}
				else
				{
					if(temp3>=temp4)
					{
						list.add(temp1);
						c++;
					}
					else if (temp3<temp4)
					{
						list.add(temp2);
						c2++;
					}
				}
			}
		no=true;
		temp1="";
		temp2="";
		temp3=0;
		temp4=0;
		}
		return list;
	}
}

