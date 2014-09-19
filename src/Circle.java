import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Circle extends Ellipse {

	public Circle(int a, int b, int c, int d, Color e, Color f) {
		super(a, b, c, d, e, f);
		// TODO Auto-generated constructor stub
	}

	public Shape getShape() {
		Shape Ellipse = new Ellipse2D.Double(p1x, p1y, Math.abs(p1x - p4x),
				Math.abs(p1x - p4x));
		return Ellipse;
	}

	public Color getOuterColor() {
		return outerColor;
	}

	public Color getInnerColor() {
		return innerColor;
	}

	public double getArea() {
		double area = ((p2x - p1x) / 2) * 2 * 3.14;
		return area;
	}

	public void setInnerColor(Color c) {
		innerColor = c;
	}

	public String getType() {
		return "circle";
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		Shape Ellipse = new Ellipse2D.Double(p1x, p1y, Math.abs(p1x - p4x),
				Math.abs(p1x - p4x));

		if (innerColor != Color.WHITE) {
			graphics.setPaint(innerColor);
			graphics.fill(Ellipse);
		}

		if (stroke) {
			float dash1[] = { 10.0f };
			BasicStroke bs2 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
			graphics.setStroke(bs2);
		} else {
			BasicStroke defalt = new BasicStroke();
			graphics.setStroke(defalt);
		}
		graphics.setColor(outerColor);
		graphics.draw(Ellipse);

	}// end paint.

	@Override
	public void setStroke(boolean f) {
		if (f) {
			stroke = true;
		} else {
			stroke = false;
		}
	}
}
