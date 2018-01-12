package com.MathScraper.TrueBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.MathScraper.TrueBox.Main.Inpfilename;
import static com.MathScraper.TrueBox.Main.OutDir;
import static com.MathScraper.TrueBox.Main.fileseparator;
enum doctype{
    Normal,Filtered
}
public class drawBBOX {
    PDDocument document;
    ArrayList<PageStructure> allPages;

    drawBBOX(PDDocument doc, ArrayList<PageStructure> allPages){
        this.allPages=allPages;
        this.document=doc;

    }

    public void draw(int pagenum,doctype t) throws IOException {
       // drawPageBBOX(pagenum,allPages.get(pagenum).pageCharacters,allPages.get(pagenum).bars,t);
        drawPageBBOX(pagenum,allPages.get(pagenum).pageCharacters,allPages.get(pagenum).pageCompundCharacters,t);
    }

    public void drawPDF(doctype t) throws IOException {
        for(int i=0;i<allPages.size();i++){
            draw(i,t);
        }
    }

    //public void drawPageBBOX(int pagenum,HashMap<Integer, characterInfo> charList,ArrayList<Bars> bars, doctype t) throws IOException {
    public void drawPageBBOX(int pagenum,HashMap<Integer, characterInfo> charList,HashMap<Integer, compundCharacter> comChar, doctype t) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = document.getPage(pagenum);
        doc.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true);

        Iterator iter = charList.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry pair = (Map.Entry) iter.next();
            characterInfo character = (characterInfo) pair.getValue();

            BBOX box = character.boundingBox;
            if(character.charInfo==null){
                contentStream.addRect(box.startX, box.startY, box.width, box.height);
                contentStream.setLineWidth((float)0.2);
                contentStream.setStrokingColor(Color.PINK);
                contentStream.stroke();
            }else {
                contentStream.addRect(box.startX, box.startY, box.width, box.height);
                //contentStream.addRect(character.charInfo.getTextMatrix().getTranslateX(), character.charInfo.getTextMatrix().getTranslateY(), character.charInfo.getWidth(), character.charInfo.getHeight());
                if (t == doctype.Filtered) {
                    contentStream.setNonStrokingColor(Color.WHITE);
                    contentStream.fill();
                } else if (t == doctype.Normal) {
                    contentStream.setLineWidth((float) 0.2);
                    contentStream.setStrokingColor(Color.GREEN);
                    contentStream.stroke();
                } else {
                    System.out.println("Invalid doctype input for character::" + character.value);
                }
            }
        }
        //for(Bars bar: bars )
        //for(Bars bar: comChar ){
        Iterator iter2 = comChar.entrySet().iterator();

        while(iter2.hasNext()){
            Map.Entry pair =(Map.Entry) iter2.next();

            compundCharacter com = (compundCharacter) pair.getValue();
            BBOX box = com.boundingBox;
            //System.out.println(box.startX+" "+ box.startY+" "+ box.width+" "+ box.height);
            contentStream.addRect(box.startX, box.startY, box.width, box.height);
            if(t==doctype.Filtered) {
                contentStream.setNonStrokingColor(Color.WHITE);
                contentStream.fill();
            }
            else if(t==doctype.Normal){

                contentStream.setStrokingColor(Color.RED);
                contentStream.stroke();
            }
            else{
                System.out.println("Invalid doctype input");
            }
        }


        contentStream.closeAndStroke();
        contentStream.close();
        File outDirectory = new File(OutDir);
        if(!outDirectory.isDirectory()){
            outDirectory.mkdir();
        }
        String outfilename = OutDir+fileseparator +Inpfilename+"_"+ (t==doctype.Normal?"normal":"filtered")+ pagenum + ".pdf";
        File file = new File(outfilename);
        doc.save(file);
        doc.close();

    }

}
