package com.MathScraper.TrueBox;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class RahulDrawGlyph extends drawGlyph{

	 double offset;
	 double offsetY;
	 double fontFactor;
	 static Graphics2D graphics;
	RahulDrawGlyph(GeneralPath glyphPath, int ch, String unicode, float fontSize, int EmSqaure) {
		super(glyphPath, ch, unicode, fontSize, EmSqaure);
		//System.out.println(super.unicode);
		// TODO Auto-generated constructor stub
	}
	
	  public void draw(type t, Graphics2D g){
	        coordinates();
	        graphics = g;
	        BoxCoord();
	        if(t == type.Adjusted)
	            adjustCoordResolution();
	        else
	            adjustCoord();

	    }
	
	public void adjustCoord(){
        GeneralPath newPath = new GeneralPath();
        for(int i=0;i<op.size();i++) {
            switch (op.get(i)) {
                case PathIterator.SEG_CLOSE:
                    newPath.closePath();
                    break;
                case PathIterator.SEG_CUBICTO:
                    newPath.curveTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally1.get(i)+Math.abs(minY))*fontFactor+offsetY,
                            (allx2.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally2.get(i)+Math.abs(minY))*fontFactor+offsetY,
                            (allx3.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally3.get(i)+Math.abs(minY))*fontFactor+offsetY);
                    break;

                case PathIterator.SEG_LINETO:
                    newPath.lineTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                            ((ally1.get(i)+Math.abs(minY)))*fontFactor+offsetY);
                    break;

                case PathIterator.SEG_MOVETO:
                    newPath.moveTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                    		((ally1.get(i)+Math.abs(minY)))*fontFactor+offsetY);
                    break;

                case PathIterator.SEG_QUADTO:
                    newPath.quadTo((allx1.get(i)+Math.abs(minX))*fontFactor+offset,
                            ((ally1.get(i)+Math.abs(minY)))*fontFactor+offsetY,
                            (allx2.get(i)+Math.abs(minX))*fontFactor+offset,
                            (ally2.get(i)+Math.abs(minY))*fontFactor+offsetY);
                    break;

              
            }

        }
       drawGlyph temp = new drawGlyph(newPath,ch,unicode,fontSize,EmSqaure);
        temp.paint(graphics);



    }

}
