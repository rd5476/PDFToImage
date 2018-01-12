package com.MathScraper.TrueBox;

class DrawSymbol {

    RahulDrawGlyph obj;
    float startX;
    float startY;
    double height;
    double width;
    double fontSize;
    public DrawSymbol(drawGlyph obj,float startX,	float startY,double fs) {
        // TODO Auto-generated constructor stub
        this.obj = new RahulDrawGlyph(obj.glyphPath, obj.ch, obj.unicode, obj.fontSize, obj.EmSqaure);
        if(obj==null)System.out.println("kyu");
        this.startX = startX;
        this.startY = startY;
        this.fontSize = fs;
    }
    
  

}