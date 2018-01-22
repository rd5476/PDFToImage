package com.MathScraper.TrueBox;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
public class ParseXML {
	
	HashMap<String,HashMap<String , XMLCharacter>> pagewise = new HashMap<>(); 
	HashMap<Integer,characterInfo> pageCharacter=null;
	static Map<String,String> unicode2label = new HashMap<>();
	static {
		init_generic_symbol_table();
	}
	static void init_generic_symbol_table() {
		
		File gst = new File("src/ocr2uni.csv");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(gst);
		
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = null;
			// if no more lines the readLine() returns null
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				String [] tokens = line.split(",");
				
				if(tokens.length>1) {
				
				//generic_symbol_table.put(tokens[0], tokens[1]);
				unicode2label.put(tokens[1].toLowerCase(), tokens[0]);
				}
				
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Load Complete");
	}
/***
 * 
 * @param filename
 * @return
 * @throws IOException 
 */
	 void readXMLFile(String filename) throws IOException{
		String [] outFileName = filename.split("\\\\");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName[outFileName.length-1].split("\\.")[0]+".lg"));
	    
		org.jsoup.nodes.Document doc ;
		int count=0;
		 ArrayList<String> objList = new ArrayList<>();
		 ArrayList<String> symList = new ArrayList<>();
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
				//////////////////////////////////////////////////////////
				//Write lg file
				for(Element e:doc.select("Char")) {
					//pass complete tag to constructor 
					XMLCharacter temp = new XMLCharacter(e.toString());
					allChars.put(temp.charID, temp);
					 XMLCharacter  xc = temp;
					 String Oline="O, "+count+", " +(int)xc.startY+", "+(int)xc.startX+", "+(int)xc.endY+", "+(int )xc.endX;
					 objList.add(Oline);
					 String label = this.pageCharacter.get(Integer.parseInt(xc.charID)).value;
					 String lgLabel="";
					 if(label.equals("fraction(-)")) lgLabel = "fraction";
					 else{String key ="#x"+Integer.toHexString(label.charAt(0) | 0x10000).substring(1) ;
					 
					 lgLabel =unicode2label.get(key);
				//	 System.out.println(key+" - "+lgLabel);
					 }
					 String Sline="#sym, "+count+", "+lgLabel+", 1.0, "+count;
					 symList.add(Sline);
					 
					 count++;
				}
				this.pagewise.put(p.attr("id"), allChars);
				
			}
			} catch (IOException ioe) { 
				ioe.printStackTrace();
				}

		for(String s:objList) {
		//	System.out.println(s);
			writer.write(s+"\n");
		}
		for(String s:symList) {
		//	System.out.println(s);
			writer.write(s+"\n");
		}
	writer.close();
	//	xml2lg( filename);
	}
	 void xml2lg(String filename) {
		 int count=0;
		 ArrayList<String> objList = new ArrayList<>();
		 ArrayList<String> symList = new ArrayList<>();
		 for(String x:this.pagewise.keySet()) {
			 XMLCharacter  xc = pagewise.get(0).get(x);
			 String Oline="O, "+count+", " +xc.startY+", "+xc.startX+", "+xc.endY+", "+xc.endX;
			 objList.add(Oline);
			 String Sline="#sym, "+count+", "+xc.label+", 1.0, "+count;
			 
			 
			 count++;
		 }
		for(String s:objList) {
			System.out.println(s);
		}
		for(String s:symList) {
			System.out.println(s);
		}
		 /*
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
						
						for(Element e:doc.select("Char")) {
							
							XMLCharacter temp = new XMLCharacter(e.toString());
							allChars.put(temp.charID, temp);
						}
						this.pagewise.put(p.attr("id"), allChars);
						
					}
					} catch (IOException ioe) { 
						ioe.printStackTrace();
						}*/

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
