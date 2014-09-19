import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

public class Rect extends BigShape {

	public Rect(int a, int b, int c, int d, Color e ,Color f) {
		super(a, b, c, d, e,f);
	}

	public Shape getShape() {
		Shape rectangle = new Rectangle(p1x, p1y, Math.abs(x1 - x2),
				Math.abs(y1 - y2));
		return rectangle;
	}

	public Color getOuterColor() {
		return outerColor;
	}

	
	public Color getInnerColor() {
		return innerColor;
	}

	public double getArea() {
		double area = Math.abs(p2x-p1x)* Math.abs(p3y-p1y);
		return area;
	}

	public void setInnerColor(Color c) {
		innerColor =c;
	}

	@Override
	public String getType() {
		return "rectangle";
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		Shape rectangle = new Rectangle(p1x, p1y, Math.abs(x1 - x2),
				Math.abs(y1 - y2));
		
		if (innerColor != Color.WHITE) {
			graphics.setPaint(innerColor);
			graphics.fill(rectangle);
		}
		
		if(stroke){
			float dash1[] = { 10.0f };
			BasicStroke bs2 = new BasicStroke(1.0f,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
					10.0f, dash1, 0.0f);
			graphics.setStroke(bs2);
		}else{
			BasicStroke defalt = new BasicStroke();
			graphics.setStroke(defalt);
		}
		graphics.setColor(outerColor);
		graphics.draw(rectangle);
		
	}

	@Override
	public void setStroke(boolean f) {
		if (f) {
			stroke = true;
		}else{
			stroke =false;
		}
	}

}
