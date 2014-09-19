import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Gui extends JFrame {

	protected final int constantX = 10;
	protected final int constantY = 37;

	// private ArrayList<BigShape> drawings = new ArrayList<BigShape>();
	private Stack<ArrayList<BigShape>> stack = new Stack<ArrayList<BigShape>>();
	private Stack<ArrayList<BigShape>> redoStack = new Stack<ArrayList<BigShape>>();

	private JPanel contentPane;
	private int x1 = 0, x2 = 0, y1 = 0, y2 = 0, currentX = 0, currentY = 0;

	private String type = "";
	private boolean draw;
	private boolean brush;
	private boolean newShape;
	private boolean found;
	private boolean delete;
	private boolean select;
	private boolean stroke;
	private boolean move;
	private boolean check;
	private boolean newClass;
	private int chosenIndex = -1;

	private BigShape temp = null;
	private Color brushOut;
	private Color brushIn;

	private int index = 17;// java.awt.color[r=17]
	private Color color = Color.BLACK;
	private String urlPath;
	private String className;

	File file = new File("C:\\Users\\Omar\\Desktop\\trial");
	private Graphics2D graphics = (Graphics2D) getGraphics();

	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1600, 903);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);

		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			// ---------------------- MOUSE DRAGGED
			// ---------------------------------
			public void mouseDragged(MouseEvent mouse) {
				x2 = mouse.getX() + constantX;
				y2 = mouse.getY() + constantY;
				if (y2 < 105) {
					y2 = 105;
				}
				draw = false;
				newShape = true;
				if (!select) {
					chosenIndex = -1;
					repaint();
				} else {
					newShape = false;
					brush = false;
					draw = false;
					delete = false;
					stroke = false;
					move = true;
					// checking the user wants to move the object or resizing
					// it.
					// we have four points where the user can resize from.

					if (Math.abs(currentX - temp.x1) <= 4
							&& Math.abs(currentY - temp.y1) <= 4) {
						x1 = x2;
						y1 = y2;

						x2 = temp.x2;
						y2 = temp.y2;

					} else if (Math.abs(currentX - temp.x1) <= 4
							&& Math.abs(currentY - temp.y2) <= 4) {
						x1 = x2;

						x2 = temp.x2;
						y1 = temp.y1;
					} else if (Math.abs(currentX - temp.x2) <= 4
							&& Math.abs(currentY - temp.y1) <= 4) {
						y1 = y2;

						x1 = temp.x1;
						y2 = temp.y2;

					} else if (Math.abs(currentX - temp.x2) <= 4
							&& Math.abs(currentY - temp.y2) <= 4) {
						x1 = temp.x1;
						y1 = temp.y1;

					} else {
						x1 = temp.x1 + (x2 - currentX);
						y1 = temp.y1 + (y2 - currentY);

						x2 = temp.x2 + (x2 - currentX);
						y2 = temp.y2 + (y2 - currentY);
					}
					repaint();

				}
			}
		});

		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			// ---------------------- MOUSE PRESSED---------------------------
			public void mousePressed(MouseEvent mouse) {
				x1 = mouse.getX() + constantX;
				y1 = mouse.getY() + constantY;
				if (y1 < 105) {
					y1 = 105;
				}
				
				findShape(x1, y1);
				currentX = x1;
				currentY = y1;

			}// end mouse pressed.

			@Override
			// ---------------------- MOUSE RELEASED-------------------------
			public void mouseReleased(MouseEvent mouse) {
				x2 = mouse.getX() + constantX;
				y2 = mouse.getY() + constantY;
				if (y2 < 105) {
					y2 = 105;
				}
				draw = true;
				newShape = true;
				if (!select) {
					chosenIndex = -1;
					stroke = false;
					repaint();
				} else {
					newShape = false;
					draw = true;
					delete = true;
					brush = false;
					stroke = false;
					move = true;
					// checking the user wants to move the object or resizing
					// it.
					// we have four points where the user can resize from.
					if (Math.abs(currentX - temp.x1) <= 4
							&& Math.abs(currentY - temp.y1) <= 4) {
						x1 = x2;
						y1 = y2;

						x2 = temp.x2;
						y2 = temp.y2;

					} else if (Math.abs(currentX - temp.x1) <= 4
							&& Math.abs(currentY - temp.y2) <= 4) {
						x1 = x2;

						x2 = temp.x2;
						y1 = temp.y1;
					} else if (Math.abs(currentX - temp.x2) <= 4
							&& Math.abs(currentY - temp.y1) <= 4) {
						y1 = y2;

						x1 = temp.x1;
						y2 = temp.y2;
					} else if (Math.abs(currentX - temp.x2) <= 4
							&& Math.abs(currentY - temp.y2) <= 4) {
						x1 = temp.x1;
						y1 = temp.y1;

					} else {
						x1 = temp.x1 + (x2 - currentX);
						y1 = temp.y1 + (y2 - currentY);

						x2 = temp.x2 + (x2 - currentX);
						y2 = temp.y2 + (y2 - currentY);
					}
					repaint();
				}// end if !select.
			}// end mouse released

			@Override
			public void mouseClicked(MouseEvent mouse) {
				x1 = mouse.getX() + constantX;
				y1 = mouse.getY() + constantY;
				if (y1 < 105) {
					y1 = 105;
				}
				
				findShape(x1, y1);
				if (!select) {
					newShape = false;
					move = false;
					delete = false;
					repaint();
				} else {
					stroke = true;
					delete = false;
					repaint();
				}
			}
		});// end mouseListener.
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ---------------------- RECTANGLE ---------------------------------
		ImageIcon rectangle = new ImageIcon("./src/rectangle.jpg");
		JButton btnRectangle = new JButton(new ImageIcon(
				(rectangle.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				type = "rectangle";
				brush = false;
				select = false;
				stroke = false;
				newClass = false;
				check =false;
			}
		});
		btnRectangle.setBounds(569, 0, 70, 70);
		contentPane.add(btnRectangle);

		// ---------------------- ELLIPSE ---------------------------------
		ImageIcon ellipse = new ImageIcon("./src/ellipse.jpg");
		JButton btnEllipse = new JButton(new ImageIcon(
				(ellipse.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnEllipse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = "ellipse";
				brush = false;
				select = false;
				stroke = false;
				newClass = false;
				check =false;
			}
		});
		btnEllipse.setBounds(781, 0, 70, 70);
		contentPane.add(btnEllipse);

		// ---------------------- SQUARE ---------------------------------
		ImageIcon square = new ImageIcon("./src/square.jpg");
		JButton btnSquare = new JButton(new ImageIcon(
				(square.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				type = "square";
				brush = false;
				select = false;
				stroke = false;
				newClass = false;
				check =false;
			}
		});
		btnSquare.setBounds(499, 0, 70, 70);
		contentPane.add(btnSquare);

		// ---------------------- CIRCLE ---------------------------------
		ImageIcon circle = new ImageIcon("./src/circle.jpg");
		JButton btnCircle = new JButton(new ImageIcon(
				(circle.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = "circle";
				brush = false;
				select = false;
				stroke = false;
				newClass = false;
				check =false;
			}
		});
		btnCircle.setBounds(711, 0, 70, 70);
		contentPane.add(btnCircle);

		// ---------------------- TRIANGLE ---------------------------------
		ImageIcon triangle = new ImageIcon("./src/triangle.jpg");
		JButton btnTriangle = new JButton(new ImageIcon(
				(triangle.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = "triangle";
				brush = false;
				select = false;
				stroke = false;
				newClass = false;
				check =false;
			}
		});
		btnTriangle.setBounds(640, 0, 70, 70);
		contentPane.add(btnTriangle);

		// ---------------------- LINE ---------------------------------
		ImageIcon line = new ImageIcon("./src/line.jpg");
		JButton btnLine = new JButton(new ImageIcon(
				(line.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				type = "line";
				brush = false;
				select = false;
				stroke = false;
				newClass = false;
				check =false;
			}
		});
		btnLine.setBounds(429, 0, 70, 70);
		contentPane.add(btnLine);

		// ---------------------- COLOR CHOOSER---------------------------------
		ImageIcon colour = new ImageIcon("./src/colour.png");
		JButton btnColor = new JButton(new ImageIcon(
				(colour.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnColor.setBorder(null);
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brush = false;
				draw = false;
				newShape = false;
				select = false;
				stroke = false;
				newClass = false;
				color = JColorChooser
						.showDialog(null, "Pick your color", color);
				if (color == null) {
					color = Color.BLACK;
				}

			}
		});
		// JColorChooser x = new JColorChooser();
		// x.setBounds(0,60, 290, 200);
		// contentPane.add(x);

		btnColor.setBounds(851, 0, 70, 70);
		contentPane.add(btnColor);

		// ---------------------- BRUSH ---------------------------------
		ImageIcon brush1 = new ImageIcon("./src/brush.jpg");
		JButton btnBrush = new JButton(new ImageIcon(
				(brush1.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnBrush.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brush = true;
				select = false;
				stroke = false;
				newClass = false;
				check =true;
			}
		});
		btnBrush.setBounds(920, 0, 70, 70);
		contentPane.add(btnBrush);

		// ---------------------- UNDO ---------------------------------
		ImageIcon undo = new ImageIcon("./src/undo.jpg");
		JButton btnUndo = new JButton(new ImageIcon(
				(undo.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if (numberOfShapes > 0) {
				// numberOfShapes--;
				// }
				if (stack.size() != 0) {
					redoStack.push(stack.pop());
				}
				newShape = false;
				brush = false;
				draw = false;
				select = false;
				stroke = false;
				newClass = false;
				repaint();
			}
		});
		btnUndo.setBounds(1441, 0, 70, 70);
		contentPane.add(btnUndo);

		// ---------------------- REDO---------------------------------
		ImageIcon redo = new ImageIcon("./src/redo.jpg");
		JButton btnRedo = new JButton(new ImageIcon(
				(redo.getImage()).getScaledInstance(70, 70,
						java.awt.Image.SCALE_SMOOTH)));
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if (numberOfShapes < counterMax) {
				// numberOfShapes++;
				// }

				if (redoStack.size() != 0) {
					stack.push(redoStack.pop());

				}

				newShape = false;
				brush = false;
				draw = false;
				select = false;

				newClass = false;
				stroke = false;
				repaint();
			}
		});
		btnRedo.setBounds(1512, 0, 70, 70);
		contentPane.add(btnRedo);

		// ---------------------- DELETE---------------------------------
		JButton btnDelete = new JButton("delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newShape = false;
				brush = false;
				draw = false;
				delete = true;
				stroke = false;
				move = false;
				newClass = false;
				repaint();
			}
		});
		btnDelete.setBounds(1106, 0, 70, 70);
		contentPane.add(btnDelete);

		// ---------------------- SELECT---------------------------------
		JButton btnSelect = new JButton("select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newShape = false;
				brush = false;
				draw = false;
				select = true;
				newClass = false;
				stroke = false;
				check =true;
			}
		});
		btnSelect.setBounds(1036, 0, 70, 70);
		contentPane.add(btnSelect);

		// -------------------------------------------------------------------
		// Save ---------------------------------------------------------
		JButton btnSaveXML = new JButton("Save XML");
		btnSaveXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(Gui.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					// System.out.println(file);
					// This is where a real application would open the file.
				}
				saveXMLFile();
				// saveJSONFile();
			}
		});
		btnSaveXML.setBounds(0, 0, 97, 70);
		contentPane.add(btnSaveXML);

		// ---------------------------------------------------------------------
		// File Chooser --------------------------------------------------------

		// ---------------------------------------------------------------------
		// LOAD ------------------------------------------------
		JButton btnLoadXML = new JButton("Load XML");
		btnLoadXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(Gui.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					// System.out.println(file);
					// This is where a real application would open the file.
				}

				loadXMLFile();
				// loadJSONFile();
			}
		});
		btnLoadXML.setBounds(98, 0, 100, 70);
		contentPane.add(btnLoadXML);

		JButton btnLoadClass = new JButton("Load Class");
		btnLoadClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(Gui.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					// System.out.println(file);
				}
				urlPath = file.toString();
				urlPath = urlPath.replaceAll("\\\\", "/");
				urlPath = "file:/" + urlPath;
				className = "";
				for (int i = urlPath.length() - 1; i >= 0; i--) {
					if (urlPath.charAt(i) == '/') {
						i = 0;
					} else {
						className += urlPath.charAt(i);
					}

				}// end for(i).
				className = new StringBuilder(className).reverse().toString();
				className = className.substring(0, className.length() - 6);

				JButton btnNewClass = new JButton(className);
				btnNewClass.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent mouse) {
						newClass = true;
					}
				});
				btnNewClass.setBounds(1342, 0, 80, 70);
				contentPane.add(btnNewClass);
			}// end method action.
		});
		btnLoadClass.setBounds(1240, 0, 97, 70);
		contentPane.add(btnLoadClass);

		JButton saveJson = new JButton("Save Json");
		saveJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(Gui.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					// System.out.println(file);
					// This is where a real application would open the file.
				}
				saveJSONFile();
			}
		});
		saveJson.setBounds(199, 0, 97, 70);
		contentPane.add(saveJson);

		JButton loadJson = new JButton("Load Json");
		loadJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(Gui.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					// System.out.println(file);
					// This is where a real application would open the file.
				}
				loadJSONFile();
			}
		});
		loadJson.setBounds(296, 0, 97, 70);
		contentPane.add(loadJson);

	}// end constructor;\

	// ---------------------- PAINT
	// ---------------------------------------------------------------------------

	public void paint(Graphics g) {

		super.paint(g);

		ArrayList<BigShape> drawings = new ArrayList<BigShape>();
		ArrayList<BigShape> copy = new ArrayList<BigShape>();

		if (stack.size() > 0) {
			copy = stack.peek();

		}

		graphics = (Graphics2D) g;
		// graphics.clearRect(825, 0, 2000, 2000);
		graphics.clearRect(0, 105, 2000, 2000);
		graphics.setColor(Color.LIGHT_GRAY);
		// graphics.fillRect(0, 0, 285, 105);
		// graphics.fillRect(852, 0, 308, 105);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 105, 2000, 2000);

		// drawing and painting all the previous shapes.
		for (int i = 0; i < copy.size(); i++) {
			drawings.add(copy.get(i));

			if (delete) {
				System.out.println(chosenIndex);
				if (i != chosenIndex) {
					drawings.get(i).paint(graphics);
				}
			} else if (stroke) {
				if (i == chosenIndex) {
					drawings.get(i).setStroke(true);
					drawings.get(i).paint(graphics);

					// drawing the four rectangle for resizing.
					BigShape selected = copy.get(i);
					if (!selected.getType().equals("line")) {
						g.drawRect(selected.x1 - 2, selected.y1 - 2, 4, 4);
						g.fillRect(selected.x1 - 2, selected.y1 - 2, 4, 4);

						g.drawRect(selected.x2 - 2, selected.y1 - 2, 4, 4);
						g.fillRect(selected.x2 - 2, selected.y1 - 2, 4, 4);

						g.drawRect(selected.x1 - 2, selected.y2 - 2, 4, 4);
						g.fillRect(selected.x1 - 2, selected.y2 - 2, 4, 4);

						g.drawRect(selected.x2 - 2, selected.y2 - 2, 4, 4);
						g.fillRect(selected.x2 - 2, selected.y2 - 2, 4, 4);
					} else {
						g.drawRect(selected.x1 - 2, selected.y1 - 2, 4, 4);
						g.fillRect(selected.x1 - 2, selected.y1 - 2, 4, 4);

						g.drawRect(selected.x2 - 2, selected.y2 - 2, 4, 4);
						g.fillRect(selected.x2 - 2, selected.y2 - 2, 4, 4);
					}// end if shape!=line.
				} else {
					drawings.get(i).setStroke(false);
					drawings.get(i).paint(graphics);
				}// end if i==index.
			} else {
				drawings.get(i).setStroke(false);
				drawings.get(i).paint(graphics);
			}// if delete.
		}// end for(i).

		if (newClass) {
			try {
				URL classUrl;
				classUrl = new URL(urlPath);

				URL[] classUrls = { classUrl };
				URLClassLoader ucl = new URLClassLoader(classUrls);

				Class myClass = ucl.loadClass(className);
				Class[] type = { int.class, int.class, int.class, int.class,
						Color.class, Color.class };
				Object whatInstance = myClass.getConstructor(type).newInstance(
						x1, x2, y1, y2, color, Color.WHITE);

				Class[] t = { Graphics.class };
				Method m = myClass.getMethod("paint", t);
				Object[] arg = { graphics };
				m.invoke(whatInstance, arg);

				if (draw) {
					drawings.add((BigShape) whatInstance);
					stack.add(drawings);
				}
				// Method[]methods = myClass.getDeclaredMethods();
				// for(Method m :methods){
				// String Mname = m.getName();
				// }// end for.

			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			if (!stroke) {
				if (delete && chosenIndex != -1 && drawings.size() > 0
						&& drawings.size() > chosenIndex) {
					drawings.remove(chosenIndex);
					// chosenIndex = -1;
					delete = false;
					stack.add(drawings);

				} else if (brush) {
					if (chosenIndex != -1) {
						drawings.remove(chosenIndex);
					}
					BigShape shape;
					if (type.equals("line")) {
						shape = new Line(temp.x1, temp.x2, temp.y1, temp.y2,
								color, color);
					} else if (type.equals("rectangle")) {
						shape = new Rect(temp.x1, temp.x2, temp.y1, temp.y2,
								brushOut, color);
					} else if (type.equals("ellipse")) {
						shape = new Ellipse(temp.x1, temp.x2, temp.y1, temp.y2,
								brushOut, color);
					} else if (type.equals("triangle")) {
						shape = new Triangle(temp.x1, temp.x2, temp.y1,
								temp.y2, brushOut, color);
					} else if (type.equals("square")) {
						shape = new Square(temp.x1, temp.x2, temp.y1, temp.y2,
								brushOut, color);
					} else if (type.equals("circle")) {
						shape = new Circle(temp.x1, temp.x2, temp.y1, temp.y2,
								brushOut, color);
					} else {
						shape = new Rect(0, 0, 0, 0, brushOut, brushIn);
					}

					// drawing the shape.
					shape.setStroke(false);
					shape.paint(graphics);

					// adding the shape into the arrayList.
					drawings.add(shape);
					stack.add(drawings);
				}
				if (newShape == true) {
					BigShape shape;
					System.out.println(type);
					if (type.equals("line")) {
						shape = new Line(x1, x2, y1, y2, color, Color.WHITE);
					} else if (type.equals("rectangle")) {
						shape = new Rect(x1, x2, y1, y2, color, Color.WHITE);
					} else if (type.equals("ellipse")) {
						shape = new Ellipse(x1, x2, y1, y2, color, Color.WHITE);
					} else if (type.equals("triangle")) {
						shape = new Triangle(x1, x2, y1, y2, color, Color.WHITE);
					} else if (type.equals("square")) {
						shape = new Square(x1, x2, y1, y2, color, Color.WHITE);
					} else if (type.equals("circle")) {
						shape = new Circle(x1, x2, y1, y2, color, Color.WHITE);
					} else {
						shape = new Rect(0, 0, 0, 0, color, Color.WHITE);
					}
					// drawing the shape adding the shape into the arrayList.
					graphics.setColor(color);
					if (draw) {
						shape.setStroke(false);
						shape.paint(graphics);
						drawings.add(shape);
						stack.push(drawings);
					} else {
						shape.setStroke(true);
						shape.paint(graphics);
					} // end if draw.

					found = false;
				} else if (move) {
					BigShape shape;
					if (type.equals("line")) {
						shape = new Line(x1, x2, y1, y2, temp.outerColor,
								temp.innerColor);
					} else if (type.equals("rectangle")) {
						shape = new Rect(x1, x2, y1, y2, temp.outerColor,
								temp.innerColor);
					} else if (type.equals("ellipse")) {
						shape = new Ellipse(x1, x2, y1, y2, temp.outerColor,
								temp.innerColor);
					} else if (type.equals("triangle")) {
						shape = new Triangle(x1, x2, y1, y2, temp.outerColor,
								temp.innerColor);
					} else if (type.equals("square")) {
						shape = new Square(x1, x2, y1, y2, temp.outerColor,
								temp.innerColor);
					} else if (type.equals("circle")) {
						shape = new Circle(x1, x2, y1, y2, temp.outerColor,
								temp.innerColor);
					} else {
						shape = new Rect(0, 0, 0, 0, temp.outerColor,
								temp.innerColor);
					}
					// drawing the shape adding the shape into the arrayList.

					if (draw) {
						shape.setStroke(false);
						shape.paint(graphics);
						drawings.add(shape);
						stack.push(drawings);
					} else {
						shape.setStroke(true);
						shape.paint(graphics);
					}
					move = false;
				}// end if delete.

			}// end if (!stroke);
		}// if(newClass);
	}// method paint.

	public BigShape findShape(int x1, int y1) {
		ArrayList<BigShape> drawings = new ArrayList<BigShape>();
		if (stack.size() != 0) {
			drawings = stack.peek();
		}
	//	check =true;
		if (check) {
			boolean line = false;
			for (int i = 0; i < drawings.size(); i++) {
				if (drawings.get(i).getType().equals("line")) {
					BigShape l = drawings.get(i);
					double a = Math.sqrt(Math.pow(l.x1 - x1, 2)
							+ Math.pow(l.y1 - y1, 2));
					double b = Math.sqrt(Math.pow(l.x2 - x1, 2)
							+ Math.pow(l.y2 - y1, 2));
					double c = Math.sqrt(Math.pow(l.x1 - l.x2, 2)
							+ Math.pow(l.y2 - l.y1, 2));
					if (Math.abs(a + b - c) < 1) {
						temp = l;
						chosenIndex = i;
						line = true;
						found = true;
						type = "line";
					}
				}
			}
			if (!line) {
				double minArea = Integer.MAX_VALUE;
				for (int i = 0; i < drawings.size(); i++) {
					if (drawings.get(i).getShape().contains(x1, y1)) {
						if (drawings.get(i).getArea() < minArea) {
							found = true;
							minArea = drawings.get(i).getArea();
							temp = drawings.get(i);
							chosenIndex = i;
						}
					}
				}// end for i.
				if (found) {
					type = temp.getType();
					brushOut = temp.getOuterColor();
				}
			}
		}
		return temp;
	}// end method findLine().

	public void saveXMLFile() {

		// move = false;
		// select = false;
		// newClass = false;
		// draw = false;
		// newShape = false;

		ArrayList<BigShape> drawings = new ArrayList<BigShape>();
		if (stack.size() > 0) {
			drawings = stack.peek();
		}// end if stack

		try {
			Element shapesRoot = new Element("Shapes");
			Document doc = new Document();
			doc.setRootElement(shapesRoot);

			for (int i = 0; i < drawings.size(); i++) {
				Element shape = new Element("Shape");
				shape.setAttribute(new Attribute("type", drawings.get(i)
						.getType()));
				shape.addContent(new Element("type").setText(drawings.get(i)
						.getType()));
				shape.addContent(new Element("x1").setText(drawings.get(i).x1
						+ ""));
				shape.addContent(new Element("x2").setText(drawings.get(i).x2
						+ ""));
				shape.addContent(new Element("y1").setText(drawings.get(i).y1
						+ ""));
				shape.addContent(new Element("y2").setText(drawings.get(i).y2
						+ ""));
				shape.addContent(new Element("outerColor").setText(drawings
						.get(i).outerColor + ""));
				shape.addContent(new Element("innerColor").setText(drawings
						.get(i).innerColor + ""));

				doc.getRootElement().addContent(shape);

			}// end for i.

			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();
			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());

			xmlOutput.output(doc, new FileWriter(file));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// end method save XML File.

	public void loadXMLFile() {

		// if(file != null){
		try {

			SAXBuilder builder = new SAXBuilder();

			Document document = (Document) builder.build(file);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("Shape");

			ArrayList<BigShape> copy = new ArrayList<BigShape>();
			stack.clear();

			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);

				BigShape shape;
				String typeShape = node.getChildText("type");
				int x1a = Integer.parseInt(node.getChildText("x1"));
				int x2a = Integer.parseInt(node.getChildText("x2"));
				int y1a = Integer.parseInt(node.getChildText("y1"));
				int y2a = Integer.parseInt(node.getChildText("y2"));
				String outColor = node.getChildText("outerColor");
				String inColor = node.getChildText("innerColor");

				int[] arr1 = getColorInts(outColor);
				int[] arr2 = getColorInts(inColor);

				Color a = new Color(arr1[0], arr1[1], arr1[2]);
				Color b = new Color(arr2[0], arr2[1], arr2[2]);

				if (typeShape.equals("line")) {
					shape = new Line(x1a, x2a, y1a, y2a, a, b);
				} else if (typeShape.equals("rectangle")) {
					shape = new Rect(x1a, x2a, y1a, y2a, a, b);
				} else if (typeShape.equals("ellipse")) {
					shape = new Ellipse(x1a, x2a, y1a, y2a, a, b);
				} else if (typeShape.equals("triangle")) {
					shape = new Triangle(x1a, x2a, y1a, y2a, a, b);
				} else if (typeShape.equals("square")) {
					shape = new Square(x1a, x2a, y1a, y2a, a, b);
				} else if (typeShape.equals("circle")) {
					shape = new Circle(x1a, x2a, y1a, y2a, a, b);
				} else {
					shape = new Rect(0, 0, 0, 0, Color.WHITE, Color.WHITE);
				}

				copy.add(shape);
			}// end for i.
			ArrayList<BigShape> drawings = new ArrayList<BigShape>();
			for (int i = copy.size() - 1; i >= 0; i--) {
				drawings.add(copy.get(i));
			}

			stack.push(drawings);
			System.out.println(drawings.size());
			repaint();

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }//end if.

	}// end method load XML File.

	public int[] getColorInts(String word) {

		boolean comma = false;
		int indexAt = index;
		String num = "";
		int[] arr = new int[3];
		while (comma == false) {
			if (word.charAt(indexAt) == ',' || word.charAt(indexAt) == ']') {
				comma = true;
				indexAt += 3;
			} else {
				num += word.charAt(indexAt);
				indexAt++;
			}
		}// end while.
		arr[0] = Integer.parseInt(num);

		comma = false;
		num = "";
		while (comma == false) {
			if (word.charAt(indexAt) == ',' || word.charAt(indexAt) == ']') {
				comma = true;
				indexAt += 3;
			} else {
				num += word.charAt(indexAt);
				indexAt++;
			}
		}// end while.
		arr[1] = Integer.parseInt(num);

		comma = false;
		num = "";
		while (comma == false) {
			if (word.charAt(indexAt) == ',' || word.charAt(indexAt) == ']') {
				comma = true;
				indexAt += 3;
			} else {
				num += word.charAt(indexAt);
				indexAt++;
			}
		}// end while.
		arr[2] = Integer.parseInt(num);

		return arr;
	}// end method.

	public void saveJSONFile() {

		ArrayList<BigShape> drawings = new ArrayList<BigShape>();
		if (stack.size() != 0) {
			drawings = stack.peek();
		}
		try {
			FileWriter fr = new FileWriter(file);

			for (int i = 0; i < drawings.size(); i++) {
				JSONObject obj = new JSONObject();
				obj.put("type", drawings.get(i).getType());
				obj.put("x1", drawings.get(i).x1);
				obj.put("x2", drawings.get(i).x2);
				obj.put("y1", drawings.get(i).y1);
				obj.put("y2", drawings.get(i).y2);
				String colorA = drawings.get(i).getOuterColor().toString();
				int[] arr = getColorInts(colorA);
				obj.put("outerColor1", arr[0]);
				obj.put("outerColor2", arr[1]);
				obj.put("outerColor3", arr[2]);

				String colorB = drawings.get(i).getInnerColor().toString();
				arr = getColorInts(colorB);
				obj.put("innerColor1", arr[0]);
				obj.put("innerColor2", arr[1]);
				obj.put("innerColor3", arr[2]);

				fr.write(obj.toJSONString() + "\n");

			}// end for i.

			fr.flush();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.print(obj);

	}// end method save json.

	public void loadJSONFile() {

		JSONParser parser = new JSONParser();
		ArrayList<BigShape> drawings = new ArrayList<BigShape>();

		try {
			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);

			String line = "";
			while ((line = bf.readLine()) != null) {
				if (line.charAt(line.length() - 1) != '}') {
					line += bf.readLine();
				}
				Object obj = parser.parse(line);

				JSONObject jsonObject = (JSONObject) obj;
				String t = (String) jsonObject.get("type");

				long xA = (Long) jsonObject.get("x1");
				long xB = (Long) jsonObject.get("x2");
				long yA = (Long) jsonObject.get("y1");
				long yB = (Long) jsonObject.get("y2");

				long colorA1 = (Long) jsonObject.get("outerColor1");
				long colorA2 = (Long) jsonObject.get("outerColor2");
				long colorA3 = (Long) jsonObject.get("outerColor3");
				Color colorA = new Color((int) colorA1, (int) colorA2,
						(int) colorA3);
				long colorB1 = (Long) jsonObject.get("innerColor1");
				long colorB2 = (Long) jsonObject.get("innerColor2");
				long colorB3 = (Long) jsonObject.get("innerColor3");
				Color colorB = new Color((int) colorB1, (int) colorB2,
						(int) colorB3);

				BigShape shape;
				if (t.equals("line")) {
					shape = new Line((int) xA, (int) xB, (int) yA, (int) yB,
							colorA, colorB);
				} else if (t.equals("rectangle")) {

					shape = new Rect((int) xA, (int) xB, (int) yA, (int) yB,
							colorA, colorB);
				} else if (t.equals("ellipse")) {
					shape = new Ellipse((int) xA, (int) xB, (int) yA, (int) yB,
							colorA, colorB);
				} else if (t.equals("triangle")) {
					shape = new Triangle((int) xA, (int) xB, (int) yA,
							(int) yB, colorA, colorB);
				} else if (t.equals("square")) {
					shape = new Square((int) xA, (int) xB, (int) yA, (int) yB,
							colorA, colorB);
				} else if (t.equals("circle")) {
					shape = new Circle((int) xA, (int) xB, (int) yA, (int) yB,
							colorA, colorB);
				} else {
					shape = new Rect(0, 0, 0, 0, brushOut, brushIn);
				}
				drawings.add(shape);
			}// end while

			stack.push(drawings);

			repaint();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end method load

	public void dynamicLoading() {
		try {
			URL classUrl;
			classUrl = new URL("");

			URL[] classUrls = { classUrl };
			URLClassLoader ucl = new URLClassLoader(classUrls);

			Class myClass = ucl.loadClass("test");

			Class[] type = { int.class, int.class, int.class, int.class,
					Color.class, Color.class };
			Object WhatInstance = myClass.getConstructor(type).newInstance();
			
			// Method[]methods = myClass.getDeclaredMethods();
			// for(Method m :methods){
			// String Mname = m.getName();
			// }// end for.

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// end dynamic loading.
}// end class
