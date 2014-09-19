import java.awt.*;
import java.awt.geom.GeneralPath;

public class Line extends BigShape {

	public Line(int a, int b, int c, int d, Color e, Color f) {
		super(a, b, c, d, e, f);
	}

	@Override
	public Shape getShape() {

		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 1);

		polyline.moveTo(x1, y1);

		polyline.lineTo(x2, y2);

		return polyline;
	}

	public Color getOuterColor() {
		return outerColor;
	}

	public Color getInnerColor() {
		return innerColor;
	}

	public double getArea() {
		double area = 0;
		return area;
	}

	public void setInnerColor(Color c) {
		outerColor = c;
	}

	@Override
	public String getType() {
		return "line";
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 1);

		polyline.moveTo(x1, y1);

		polyline.lineTo(x2, y2);


		if (stroke) {
			float dash1[] = { 10.0f };
			BasicStroke bs2 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
			graphics.setStroke(bs2);
		}else{
			BasicStroke defalt = new BasicStroke();
			graphics.setStroke(defalt);
		}
		graphics.setColor(outerColor);
		graphics.draw(polyline);

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
