package com.MathScraper.TrueBox;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.jdom2.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
public class ParseXML {
	
	HashMap<String,HashMap<String , XMLCharacter>> pagewise = new HashMap<>(); 
	
/***
 * 
 * @param filename
 * @return
 */
	 void readXMLFile(String filename){
		
		org.jsoup.nodes.Document doc ;
		try (InputStream in = new FileInputStream(filename);
				BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
			String str = null; 
			StringBuilder sb = new StringBuilder(8192); 
			while ((str = r.readLine()) != null) { 
				sb.append(str); 
				} 
			doc = Jsoup.parse(sb.toString(), "", Parser.xmlParser());
			
			for(Element p: doc.select("Page")) {
				HashMap<String , XMLCharacter> allChars = new HashMap<>();
				//p.
				//p.children();
				
				for(Element e:doc.select("Char")) {
					
					XMLCharacter temp = new XMLCharacter(e.toString());
					allChars.put(temp.charID, temp);
				}
				this.pagewise.put(p.attr("id"), allChars);
				
			}
			} catch (IOException ioe) { 
				ioe.printStackTrace();
				}

		
	}
}

class XMLCharacter{
	float startX,startY,endX,endY;
	String charID;
	String mergeID;
	String pageID ="0";
	String label;
	public XMLCharacter(String XMLChar) {
		org.jsoup.nodes.Document doc = Jsoup.parse(XMLChar, "", Parser.xmlParser());
		
		
		Elements e = doc.select("Char") ;
		    this.label = e.text();
		    
			this.charID =(e.attr("id"));
		    this.mergeID = (e.attr("mergeId"));
		    String coord = (e.attr("BBOX"));
		    String [] all =coord.split(" ");
		    this.startX =Float.parseFloat(all[0]);
		    this.startY =Float.parseFloat(all[1]);
		    this.endX =Float.parseFloat(all[2]);
		    this.endY =Float.parseFloat(all[3]);
		   
		
	}
}
