import java.awt.*;

import javax.swing.*;

import java.io.*;
import java.awt.event.*;

public class ImageEditor extends JFrame implements ActionListener{
	private ImageCanvas orig, alter;
	String file = "./src/flower.jpg";
	
//	private JButton mirrorHoriz;
//	private JButton mirrorVert;
//	private JButton redify;
//	private JButton blueify;
//	private JButton greenify;
//	private JButton negative;
//	private JButton grayscale;
//	private JButton sharpen;
//	private JButton blur;
//	private JButton lock;
//	
//	private JButton resize;
//	private JButton transform;
//	private JButton stretchx;
//	private JButton stretchy;
//	private JButton rotate;
	
	
	private JButton rotate, resize, transform, stretchHoriz, stretchVert;
	private JMenuItem open, testIt, mirrorHoriz, mirrorVert, redify, blueify, greenify, negative, grayscale, sharpen, blur, reset;
	JPanel  matrix, matrixButton, allText, left, right, resizeButton, rotateButton, stretch;
	private JTextField transform00,transform01,transform10,transform11;
	private JTextField rotatetext, resizetext, stretchx, stretchy;
	
	
	public ImageEditor(){
		super("Image Editor");
		makeMenu();
		
		allText = new JPanel(new GridLayout(1,3));//panel that matrixButton, left, and right will all go on
		
		left = new JPanel(new GridLayout(2,1));//Panel for items on the bottom left (resize and rotate)
		right = new JPanel(new GridLayout(2,2));//Panel for items on the bottom right(stretch x and y)

		
		orig.setImage(new File(file));
		//Manual Transform Matrix
		matrix = new JPanel(new GridLayout(2,2));
		matrixButton = new JPanel(new GridLayout(2,1));
		transform00 = new JTextField();
		transform01 = new JTextField();
		transform10 = new JTextField();
		transform11 = new JTextField();
		matrix.add(transform00);
		matrix.add(transform01);
		matrix.add(transform10);
		matrix.add(transform11);
		transform = new JButton("Transform");
		transform.addActionListener(this);
		matrixButton.add(transform);
		matrixButton.add(matrix);
		
		//Left JPanel that has resize and rotate
		resizetext = new JTextField();
		resize = new JButton("Resize");
		rotate = new JButton("Rotate");
		rotatetext = new JTextField();
		resizeButton = new JPanel(new GridLayout(2,1));
		rotateButton = new JPanel(new GridLayout(2,1));
		resizeButton.add(resize);
		resizeButton.add(resizetext);
		rotateButton.add(rotate);
		rotateButton.add(rotatetext);
		left.add(resizeButton);
		left.add(rotateButton);
		
		resize.addActionListener(this);
		rotate.addActionListener(this);
		
		//Right JPanel holds stretchx and stretchy
		stretchx = new JTextField();
		stretchy = new JTextField();
		stretchHoriz = new JButton("Stretch Horizontally");
		stretchVert = new JButton("Stretch Vertically");
		right.add(stretchHoriz);
		right.add(stretchx);
		right.add(stretchVert);
		right.add(stretchy);
		
		stretchHoriz.addActionListener(this);
		stretchVert.addActionListener(this);

		
		//buttons.add(reset);//adds the buttons to the JPanel
		
		
		
		alter = new ImageCanvas();
		alter.setImage(new File(file));
		
		alter.tester();
		
		JPanel stuff = new JPanel();
		stuff.setLayout(new GridLayout(1,2));
		stuff.add(new JScrollPane(orig));
		stuff.add(new JScrollPane(alter));
		this.add(stuff, BorderLayout.CENTER);//adds the JPanel of Pictures to the window
		//this.add(buttons, BorderLayout.SOUTH);//adds the JPanel of buttons to the bottom of the window
		allText.add(left);
		allText.add(matrixButton);
		allText.add(right);
		this.add(allText, BorderLayout.SOUTH);
		
		
		//finishing up
		this.setSize(900,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	private void makeMenu(){
		JMenuBar bar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu color = new JMenu("Color");
		JMenu transform = new JMenu("Transform");
		
		
		
		
		
		orig = new ImageCanvas();
		
		mirrorHoriz = new JMenuItem("Mirror Horizontally");
		mirrorVert = new JMenuItem("Mirror Vertically");
		redify = new JMenuItem("Red");
		blueify = new JMenuItem("Blue");
		greenify = new JMenuItem("Green");
		negative = new JMenuItem("Negative");
		grayscale = new JMenuItem("Grayscale");
		sharpen = new JMenuItem ("Sharpen");
		blur = new JMenuItem("Blur");
		
		
		
		
		
		mirrorHoriz.addActionListener(this);
		mirrorVert.addActionListener(this);
		redify.addActionListener(this);
		blueify.addActionListener(this);
		greenify.addActionListener(this);
		negative.addActionListener(this);

		sharpen.addActionListener(this);
		blur.addActionListener(this);
		
		
		
		
		
		
		color.add(mirrorHoriz);
		color.add(mirrorVert);
		color.add(redify);
		color.add(greenify);
		color.add(blueify);
		color.add(negative);
		color.add(blur);
		color.add(sharpen);
		
		
		open = new JMenuItem("Open");
		open.addActionListener(this);
		file.add(open);
		
		reset = new JMenuItem("Reset");
		reset.addActionListener(this);
		

		testIt = new JMenuItem("Test");
		testIt.addActionListener(this);
		file.add(testIt);
		
		file.add(reset);
		
		bar.add(file);
		bar.add(color);
		//bar.add(transform);
		
		this.setJMenuBar(bar);
	}
	
	public static void main(String[] args) {new ImageEditor();}

	public void reset(){//makes the alter image the same as the original again
		alter.arrayToImg(orig.imgToArray());
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==open){
			JFileChooser jfc = new JFileChooser();
			int result = jfc.showOpenDialog(this);
			if(result == JFileChooser.CANCEL_OPTION)
				return;
			File f = jfc.getSelectedFile();
			orig.setImage(f);
			alter.setImage(f);
			this.repaint();
		}
		if(e.getSource()==testIt){
			alter.tester();
		}
		
		if(e.getSource() == reset){
			reset();
			
		}

		//If the button is pressed, it calls the corresponding function in ImageCanvas
		if(e.getSource() == mirrorHoriz)
			alter.mirrorH(true);
		if(e.getSource() == mirrorVert)
			alter.mirrorV(true);
		if(e.getSource() == redify)
			alter.red();
		if(e.getSource() == blueify)
			alter.blue();
		if(e.getSource() == greenify)
			alter.green();
		if(e.getSource() == negative)
			alter.negative();
		if(e.getSource() == blur)
			alter.blur();
		if(e.getSource() == sharpen)
			alter.sharpen();
		
		
		if(e.getSource() == transform){
			double[][] matrix = new double[2][2];
			matrix [0][0] = Double.parseDouble(transform00.getText());
			matrix [0][1] = Double.parseDouble(transform01.getText());
			matrix [1][0] = Double.parseDouble(transform10.getText());
			matrix [1][1] = Double.parseDouble(transform11.getText());
			alter.transform(matrix);
		}
		
		if(e.getSource() == resize)
			alter.resize(Double.parseDouble(resizetext.getText()));
		
		if(e.getSource() == rotate)
			alter.rotate(Double.parseDouble(rotatetext.getText()));
		
		if(e.getSource() == stretchHoriz)
			alter.stretchHorizontally(Double.parseDouble(stretchx.getText()));
		
		if(e.getSource() == stretchVert)
			alter.stretchVertically(Double.parseDouble(stretchy.getText()));
		
		
	}
}
