package com.MathScraper.TrueBox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static String Inpfilename;
    static String OutDir="";
    static com.MathScraper.TrueBox.OsCheck.OSType ostype;
    static  String fileseparator=null;

    public static void main(String args[]) throws IOException, JDOMException, SAXException, ParserConfigurationException, InterruptedException {


        ostype = OsCheck.getOperatingSystemType();
        switch (ostype) {
            case Windows:
                fileseparator = "\\\\";
                break;
            case MacOS:
                fileseparator = "/";
                break;
            case Linux:
                fileseparator = "/";
                break;
            case Other:
                System.out.println("Unidentified OS output will be generated in working environment");
                break;
        }
        long starttime = System.currentTimeMillis();
        String inpfile = "";
        String option = "";
        boolean displayFlag = false;
        boolean printFlag = false;
        boolean boundingboxFlag = false;
        boolean trainingFlag = false;
        boolean filterFlag= false;
        //InputFile and OutputDir
        int argLength = args.length;
        File file;
        String op;
        String op1;
        switch (argLength) {

            case 1:
                inpfile = args[0];
                file = new File(inpfile);
                if (file.exists()) {
                    break;
                } else {
                    System.out.println("Invalid input file");
                    System.exit(0);
                }
                break;
            case 2:
                op = args[0];
                if (op.equals("-b")) {
                    boundingboxFlag = true;
                } else if (op.equals("-d")) {
                    displayFlag = true;
                } else if (op.equals("-p")) {
                    printFlag = true;
                }else if(op.equals("-f")) {
                    System.out.println("matched -f");
                    filterFlag = true;
                }
                inpfile = args[1];
                file = new File(inpfile);
                if (file.exists()) {
                    break;
                } else {
                    System.out.println("Invalid input file");
                    System.exit(0);
                }
                break;
            case 3:
                op = args[0];
                if (op.equals("-b")) {
                    System.out.println("matched -b");
                    boundingboxFlag = true;
                } else if(op.equals("-f")) {
                    System.out.println("matched -f");
                    filterFlag = true;
                }else{
                    System.out.println("Invalid input parameter");
                    System.out.println("*************Usage*************");
                    System.out.println("Please follow the instructions ");
                    System.out.println("java Main [op1] [op2] <input file path> ");
                    System.out.println("op1 optional parameter to write pdf with bounding box");
                    System.out.println("\t-b optional parameter to create PDFs with bounding box");
                    System.out.println("\t-f optional parameter to create PDFs with bounding box");
                    System.out.println("op2 is an option parameter for displaying output on the console  or writing onto a file");
                    System.out.println("\t-d to display");
                    System.out.println("\t-p to print");
                    System.out.println("input file path- PDF file path");
                    System.exit(0);

                }
                op1 = args[1];
                if (op1.equals("-d")) {
                    System.out.println("matched -d");
                    displayFlag = true;
                } else if (op1.equals("-p")) {
                    System.out.println("matched -p");
                    printFlag = true;
                } else {
                    System.out.println("Nothin matched---" + op1);
                    System.out.println("Invalid input parameter");
                    System.out.println("*************Usage*************");
                    System.out.println("Please follow the instructions ");
                    System.out.println("java Main [op1] [op2] <input file path> ");
                    System.out.println("op1 optional parameter to write pdf with bounding box");
                    System.out.println("\t-b optional parameter to create PDFs with bounding box");
                    System.out.println("\t-f optional parameter to create PDFs with bounding box");
                    System.out.println("op2 is an option parameter for displaying output on the console  or writing onto a file");
                    System.out.println("\t-d to display");
                    System.out.println("\t-p to print");
                    System.out.println("input file path- PDF file path");
                    System.exit(0);

                }
                inpfile = args[2];
                file = new File(inpfile);
                if (file.exists()) {
                    break;
                } else {
                    System.out.println("Invalid input file");
                    System.out.println("Invalid input");
                    System.out.println("*************Usage*************");
                    System.out.println("Please follow the instructions ");
                    System.out.println("java Main [op1] [op2] <input file path> ");
                    System.out.println("op1 optional parameter to write pdf with bounding box");
                    System.out.println("\t-b optional parameter to create PDFs with bounding box");
                    System.out.println("\t-f optional parameter to create PDFs with bounding box");
                    System.out.println("op2 is an option parameter for displaying output on the console  or writing onto a file");
                    System.out.println("\t-d to display");
                    System.out.println("\t-p to print");
                    System.out.println("input file path- PDF file path");

                    System.exit(0);
                }
                break;

        }

        String[] filearray = inpfile.split(fileseparator);
        Inpfilename = filearray[filearray.length - 1].split("\\.")[0];
        for (int iter = 0; iter < filearray.length - 1; iter++) {
            OutDir += filearray[iter] + fileseparator;
        }
        OutDir += "Output";


        //Load File
        file = new File(inpfile);
        FileInputStream inpStream = new FileInputStream(file);
        PDDocument documnet = PDDocument.load(inpStream);


        //Readfile
        read reader = new read(documnet);
        ArrayList<PageStructure> allPages = reader.readPdf();


        //DisplayPDF
        if (displayFlag) {
            DisplayPDF display = new DisplayPDF(allPages);
            display.displayPDF();
            System.out.println(display.builder.toString());

        }
        //Print flag
        if (printFlag) {
            long timetaken = System.currentTimeMillis()-starttime;
            String pageMetrics = "<runtime>"+timetaken+"<\\runtime>\n";
            pageMetrics+= "<pagemetrics>\n";
            for(int i=0;i<allPages.size();i++){

                pageMetrics+="\t<page>\n";
                pageMetrics+="\t\t<no>"+i+"<\\no>\n";
                pageMetrics+="\t\t<lines>"+allPages.get(i).meta.linecount+"<\\lines>\n";
                pageMetrics+="\t\t<words>"+allPages.get(i).meta.wordcount+"<\\words>\n";
                pageMetrics+="\t\t<characters>"+allPages.get(i).meta.charactercount+"<\\characters>\n";
                pageMetrics+="\t<\\page>\n";

            }
            pageMetrics+="<\\pagemetrics>\n";


            DisplayPDF display = new DisplayPDF(allPages);
            display.displayPDF();
            //System.out.println(display.xmlFormat);
            try {
                File outDirectory = new File(OutDir);
                if (!outDirectory.isDirectory()) {
                    outDirectory.mkdir();
                }
                File newFile = new File(OutDir + fileseparator + "XML" + Inpfilename + ".txt");
                PrintWriter writer = new PrintWriter(newFile);
                writer.write(pageMetrics+display.builder.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //drawBoundingBox
        if (boundingboxFlag) {
            drawBBOX box = new drawBBOX(documnet, allPages);
            box.drawPDF(doctype.Normal);
            System.out.println();
            System.out.println("Output files generated...@ " + OutDir);

        }

        //Filter text
        if(filterFlag){
            //Load Dictionary

                File dicfile = new File("Dictionary");
                HashMap<String, Integer> wordDictionary =  new HashMap<String,Integer>();
                wordDictionary = loadDictionary(dicfile);
                FilterText filterText = new FilterText(wordDictionary);
                HashMap<Integer, characterInfo> filtercharList;
                drawBBOX box = new drawBBOX(documnet, allPages);
                for (int pageIter = 0; pageIter < allPages.size(); pageIter++) {
                    filtercharList = filterPage(filterText, pageIter, allPages);
                    box.drawPageBBOX(pageIter, filtercharList,allPages.get(pageIter).pageCompundCharacters, doctype.Filtered);
                }
                System.out.println();
                System.out.println("Output files generated....");


        }


        if (trainingFlag) {
            Process p = Runtime.getRuntime().exec("python C:\\Users\\ritvi\\PycharmProjects\\CapstoneProject\\MathScrapper\\main.py");
            while (p.isAlive()) {
                continue;
            }
            File gtfile = new File("C:\\Users\\ritvi\\PycharmProjects\\CapstoneProject\\MATHXML");
            TrainingGTParser gtParser = new TrainingGTParser(gtfile);
            ArrayList<GT> gtList = gtParser.readGT();

            FilterText wordFiltering = new FilterText(allPages);

            wordFiltering.isMath();
            /*
            for (GT g : gtList) {
                System.out.println("Page:" + g.pageid);
                for (int i : g.charid)
                    System.out.println(("\tChar id:" + i));
            }
            */
        }


        long endtime = System.currentTimeMillis()-starttime;
        System.out.println("Time taken ="+endtime+"ms");
    }

    public static HashMap<String, Integer> loadDictionary(File file) throws FileNotFoundException {
        HashMap<String, Integer> wordDictionary =  new HashMap<String,Integer>();
        Scanner scan = new Scanner(new FileReader(file));
        while(scan.hasNext()){
            String word =scan.nextLine();
            word=word.trim().toLowerCase();
            wordDictionary.put(word,0);
        }

        return wordDictionary;
    }

    public static HashMap<Integer,characterInfo> filterPage(FilterText filterText,int page, ArrayList<PageStructure> allPages){
        HashMap<Integer,Words> filtered= filterText.filter(allPages.get(page));
        HashMap<Integer,characterInfo> charList =filterText.getCharacterList(filtered);
        return charList;
    }
}


