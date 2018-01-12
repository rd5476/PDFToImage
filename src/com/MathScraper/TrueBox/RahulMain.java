package com.MathScraper.TrueBox;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.text.TextPosition;

public class RahulMain {

	public static void main(String[] args) throws InvalidPasswordException, IOException {
		// TODO Auto-generated method stub
		ParseXML px =new ParseXML();
		px.readXMLFile("C:\\Users\\Rahul\\eclipse-workspace\\XMLToImage\\src\\Output\\XMLMathExp5.txt");
		
		for(String s:px.pagewise.keySet()) {
			System.out.println("Page id : "+s);
			System.out.println(px.pagewise.get(s).keySet().size());       //.pagewise[s].keySet().size());
		}
		
		
		///////////////////////////////////////////////////////////////////
		//	Read pdf 
		///////////////////////////////////////////////////////////////////
		String pdfFileName = "C:\\Users\\Rahul\\eclipse-workspace\\XMLToImage\\src\\MathExp5.pdf";
		
		//Load File
		File file = new File(pdfFileName);
        FileInputStream inpStream = new FileInputStream(file);
        PDDocument documnet = PDDocument.load(inpStream);
		
		read reader = new read(documnet);
	    ArrayList<PageStructure> allPages = reader.readPdf();
	    
	    PageStructure pg1 = allPages.get(0);
	    HashMap<String, XMLCharacter> allCharID= px.pagewise.get("0");
	    HashMap<Integer,characterInfo> pageCharacters = pg1.pageCharacters;
	    
	    
	    int pageNum =0;
	    ArrayList<ArrayList<Integer>> bars = new ArrayList<>();
	    ArrayList<DrawSymbol> expressionComponents = new ArrayList<>();
	    for(String k:allCharID.keySet()) {
	    	float startX=0;
	    	float startY =0;
	    	float fontSize =0;
	    	Integer i = Integer.parseInt(k);
	    	
	    	characterInfo character = pageCharacters.get(i);
	    	System.out.println(character.value);
	    	TextPosition text = character.charInfo;
	    	if(text == null) {
	    		System.out.println("Fraction found");
	    		
	    		startX = character.boundingBox.startX;
	    		startY = character.boundingBox.startY;
	    		System.out.println(startX+" "+startY+" "+(character.boundingBox.width+startX)+" "+(character.boundingBox.height+startY));
	    		ArrayList<Integer> line = new ArrayList<>();
	    		line.add((int) (startX/(0.01f))+500);
	    		line.add((int) (startY/(0.01f)));
	    		line.add((int) ((character.boundingBox.width+startX)/0.01f)+500);
	    		line.add((int) ((character.boundingBox.height+startY)/0.01f));
	    		bars.add(line);
	    		continue;
	    	}
	    	else {

             startX = text.getTextMatrix().getTranslateX();
             startY = text.getTextMatrix().getTranslateY();
         //   System.out.println(startX+":::"+startY);
            fontSize = text.getFontSize();
	    	}
            //PDType1Font font = (PDType1Font) text.getFont();

            drawGlyph glyph=character.glyph;
///////////////////////////////////////////////////////////////////////////////////////
           // expressionComponents.add(new DrawSymbol(glyph, startX, startY) );
           // glyph.draw(type.Normal,null); 
//////////////////////////////////////////////////////////////////////////////////////
           
            
            
            double width = character.boundingBox.width;
            double height = character.boundingBox.height;

          
            
            // Update starting point (Y-axis)
            double heightRatio = height / (glyph.maxY-glyph.minY);
            if (glyph.minY < 0) {
                double decentheight = heightRatio * glyph.minY;
                startY = startY + (float) decentheight;
            } else if (glyph.minY > 0) {
                double baseAccent = heightRatio * glyph.minY;
                startY = startY - (float) baseAccent;
            }
            // Update starting point (X-axis)
            //double widthRatio1 = width / (font.getWidthFromFont(text.getCharacterCodes()[0]));
            double widthRatio=0.01;
           
             if (glyph.minX < 0) {
                double leftMove = widthRatio * glyph.minX;
                startX = startX + (float) leftMove;
            }
             else if (glyph.minX > 0) {
                double rightMove = widthRatio * glyph.minX;
                startX = startX - (float) rightMove;
            }
//////////////////////////////////////////////////////////////////////////////////////////////
           System.out.println(glyph.unicode+"--"+startX+"--"+startY+"--"+widthRatio+" -- "+fontSize);
          
           DrawSymbol temp = new DrawSymbol(glyph,(float)( startX/widthRatio),(float) (startY/widthRatio),fontSize) ;
        
           temp.height = height/widthRatio;
           temp.width = width/widthRatio;
           expressionComponents.add(temp);
           // double glyphStartX = startX/ widthRatio;
           // System.out.println(startX +"--" + glyphStartX );
//////////////////////////////////////////////////////////////////////////////////////////////            
            

        }
      ///////////////////////////////////////////////////////////////////////////////////////////////////////////  
      //expressionComponents
       
        
      
         
//        BufferedImage image = new BufferedImage(expressionComponents.size()*1000, 2500, BufferedImage.TYPE_INT_BGR); 
        
        expressionComponents.sort((o1, o2) -> ((Float)(o1.startX)).compareTo(o2.startX));
        
        float base = expressionComponents.get(0).startX;
        System.out.println("base "+base);
        float baseY = 25000000;
        double maxVal=0;
        DrawSymbol maxY=null;
       
        for(DrawSymbol a :expressionComponents ) {
        	
        //	System.out.println(a.startY+" "+a.height);
        	if(a.startY<baseY) {baseY = a.startY;}
        	if(a.startY+a.height>maxVal) {
        		maxVal=a.startY+a.height;
        		maxY = a;
       // 		System.out.println(a.startY+" "+a.height+" "+a.obj.unicode);
        		}
        	
        }
        System.out.println("base "+baseY);
        System.out.println("MAX Y OBJ "+maxY.obj.unicode);
        
        System.out.println("Height --"+(maxY.startY+maxY.height)+" - " +maxY.height);//maxY.startY-baseY+
        BufferedImage image = new BufferedImage((int) (expressionComponents.get(expressionComponents.size()-1).startX+2000-base), (int)(maxY.startY-baseY+maxY.height), BufferedImage.TYPE_INT_BGR);
    //    BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_INT_BGR);
        //maxY.startY-baseY+maxY.height
        Graphics2D graphic = image.createGraphics();
        //double baseFont = expressionComponents.get(0).fontSize;
        double maxFont=0;
        for(DrawSymbol ds :expressionComponents) {
        	if(ds.fontSize >maxFont)maxFont = ds.fontSize;
        	
        }
        double baseFont = maxFont;
        for(DrawSymbol a :expressionComponents ) {
        	//System.out.println(a.startX);
        	a.startX -= base;
        	a.startY -= baseY;
      //  	System.out.println(a.obj.unicode+"--"+a.startX+"--"+a.startY);
        	//a.obj.minX-=base;
        	a.obj.offset = a.startX+500;
        	a.obj.offsetY = a.startY;
        	a.obj.fontFactor = a.fontSize/baseFont;
        	//System.out.println(a.startY);
        //	a.obj.maxX-=base;
        	a.obj.draw(type.Normal,graphic);
        	
        }
        for(ArrayList<Integer>a:bars) {
        	graphic.setStroke(new BasicStroke(40));
        	graphic.drawLine((int)(a.get(0)-base),(int)( a.get(1)-baseY), (int)(a.get(2)-base), (int) (a.get(3)-baseY));  //Line((int)(a.get(0)-base),(int)( a.get(1)-baseY),(int)( a.get(2)-base),(int) (a.get(3)-baseY));
        	
        	//graphic.fillRect((int)(a.get(0)-base), (int)( a.get(1)-baseY), (int)(a.get(2)-base),(int) (a.get(3)-baseY));
        	System.out.println("Line "+(a.get(0)-base)+" ");
        }
        //Flip the image
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        System.out.println(tx.toString());
      //  System.out.println("Transform = "+ (int)(maxY.startY+maxY.height));
        tx.translate(0, -(int)(maxY.startY+maxY.height+50));
        System.out.println(tx.toString());
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        
        image = op.filter(image, null);
        

        File output = new File("Rahuloutput.png");
        ImageIO.write(image, "png", output);

	    	
	    	
	    	////////////////////////////////////////////////////////////////////////////////////
	    }
	    
	    
	    
	    
	    
		
	

}
