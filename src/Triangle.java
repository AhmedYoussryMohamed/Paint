import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class Triangle extends BigShape {

	private int point1x;
	private int point2x;
	private int point3x;
	private int point1y;
	private int point2y;
	private int point3y;

	public Triangle(int a, int b, int c, int d, Color e , Color f) {
		super(a, b, c, d, e ,f);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Shape getShape() {
		generateVertices();
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);

		polyline.moveTo(point1x, point1y);

		polyline.lineTo(point2x, point2y);
		polyline.lineTo(point3x, point3y);
	//	polyline.lineTo(point1x, point1y);
		polyline.closePath();
		

		return polyline;
	}
	
	public Color getOuterColor() {
		return outerColor;
	}

	public Color getInnerColor() {
		return innerColor;
	}
	
	public double getArea() {
		double area = Math.abs(p1x-p2x) * Math.abs(p3y-p1y) * 0.5;
		return area;
	}
	
	public void setInnerColor(Color c) {
		innerColor =c;
	}

	private void generateVertices() {
		if (y2 > y1) {
			point1x = p1x;
			point1y = p1y;

			point2x = p2x;
			point2y = p2y;

			point3x = p3x + (Math.abs(p4x - p3x) / 2);
			point3y = p3y;

		} else {
			point1x = p3x;
			point1y = p3y;

			point2x = p4x;
			point2y = p4y;

			point3x = p1x + (Math.abs(p1x - p2x) / 2);
			point3y = p1y;
		}
	}

	@Override
	public String getType() {
		return "triangle";
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		generateVertices();
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 3);

		polyline.moveTo(point1x, point1y);

		polyline.lineTo(point2x, point2y);
		polyline.lineTo(point3x, point3y);
	//	polyline.lineTo(point1x, point1y);
		polyline.closePath();
		
		
		if (innerColor != Color.WHITE) {
			graphics.setPaint(innerColor);
			graphics.fill(polyline);
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
		graphics.draw(polyline);
		stroke =false;
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
