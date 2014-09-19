import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.JPanel;


public abstract class BigShape extends JPanel {
	
	protected int x1,x2,y1,y2;
	protected int p1x,p1y,p2x,p2y,p3x,p3y,p4x,p4y;
	protected Color outerColor;
	protected Color innerColor;
	protected boolean stroke=false;
	
	
	public abstract Shape getShape();
	public abstract Color getOuterColor();
	public abstract Color getInnerColor();
	public abstract double getArea();
	public abstract String getType();
	public abstract void paint(Graphics g);
	
	public abstract void setInnerColor(Color c);
	public abstract void setStroke(boolean s);
	
	public BigShape (int a,int b,int c,int d , Color e ,Color f){
		x1 =a;
		x2 =b;
		y1 =c;
		y2 =d;
		outerColor = e;
		innerColor =f;
		
		
		generatePoints();
	}
	
	private void generatePoints(){
		p1x = Math.min(x1, x2);
		p1y = Math.min(y1, y2);

		p2x = Math.min(x1, x2) + Math.abs(x1 - x2);
		p2y = Math.min(y1, y2);
		
		p3x = Math.min(x1, x2);
		p3y = Math.min(y1, y2) + Math.abs(y1 - y2);
		
		p4x = Math.max(x1, x2);
		p4y = Math.max(y1, y2);
	}
	
	

}
