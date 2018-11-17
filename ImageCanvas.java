import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
public class ImageCanvas extends JPanel{
	private BufferedImage img; //the image that appears on this canvas
	//dimensions of the image that is showing on the screen
	private int imgWidth, imgHeight;
	
	private static final int TYPE = BufferedImage.TYPE_INT_ARGB_PRE;
	
	/** ***************** PIXEL FUNCTIONS ****************** **/
	 public static final int A=0, R=1, G=2, B=3;

	 //returns only the red value of the pixel
	 //   EX: pixel = 0x004f2ca5 --> returns 0x0000004f
	 public int howRed(int pixel){return (pixel & 0x00ff0000) >> 16;}

	 //returns only the green value of the pixel
	 //   EX: pixel = 0x004f2ca5 --> returns 0x0000002c 
	 public int howGreen(int pixel){return (pixel & 0x0000ff00) >> 8;}
	  
	 //returns only the blue value of the pixel
	 //   EX: pixel = 0x004f2ca5 --> returns 0x000000a5
	 public int howBlue(int pixel){return (pixel & 0x000000ff);}
	 
	 //returns a new pixel with the specified alpha
	 //    red, green, and blue values
	 //  EX:  combine( 0x00000000, 0x0000004f, 0x0000002c, 0x000000a5) --> 0x004f2ca5
	 public int combine(int a, int r, int g, int b){
		 	if(r < 0)
				r = 0;
			if(g < 0)
				g = 0;
			if(b < 0)
				b = 0;
			if(r > 255)
				r = 255;
			if(g > 255)
				g = 255;
			if(b > 255)
				b = 255;
		 return (a<<24) | (r<<16) | (g<<8) | b;
	 }
    /** ***************************************************   **/
	public ImageCanvas(){
		super();
		this.setBackground(Color.gray);
		this.setPreferredSize(new Dimension(400,400));
		img = new BufferedImage(100,100,TYPE);
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		
	}
	
	public BufferedImage getImage(){return img;}
	public void setImage(File file){
		try{ 
			img = ImageIO.read((file));
			MediaTracker mt = new MediaTracker(new Component(){});
			mt.addImage(img, 0);
			mt.waitForAll();
		}
		catch(Exception ex){ex.printStackTrace();}
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		//pix = imgToArray();
		this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
	}
	

	// *********************Easy pixel manips************************
	
	public void red(){ 
		int[][] grid = imgToArray();
		for(int r = 0; r< imgHeight; r++){ // height is the number of rows. row and y value are the same
			for(int c = 0; c< imgWidth; c++){
				grid[r][c] = combine(0,howRed(grid[r][c]),0,0);//replaces everything that isn't a red value with 0, leaves only the redness
			}
		}
		arrayToImg(grid);
	}
	public void green(){
		int[][] grid = imgToArray();
		for(int r = 0; r< imgHeight; r++){ // height is the number of rows. row and y value are the same
			for(int c = 0; c< imgWidth; c++){
				grid[r][c] = combine(0,0,howGreen(grid[r][c]),0);//leaves only the blueness of an image
			}
		}
		arrayToImg(grid);
	}
	public void blue(){
		int[][] grid = imgToArray();
		for(int r = 0; r< imgHeight; r++){ // height is the number of rows. row and y value are the same
			for(int c = 0; c< imgWidth; c++){
				grid[r][c] = combine(0,0,0,howBlue(grid[r][c]));//leaves only the greenness of an image
			}
		}
		arrayToImg(grid);
	}
	
	public void negative(){																				/** LEFT OFF HERE**/
		int[][] grid = imgToArray();
		
		for(int r = 0; r< imgHeight; r++){ // height is the number of rows. row and y value are the same
			for(int c = 0; c< imgWidth; c++){
				grid[r][c] = combine( 0, 255-howRed(grid[r][c]), 255-howGreen(grid[r][c]), 255-howBlue(grid[r][c]) );//subtracts each color value from 255 to invert each value
			}
		}
		arrayToImg(grid);
	}
	
	
	// *********************END Easy pixel manips************************
	
	// ********************KERNEL STUFF *********************************
	public int[][] blur(){																		
		int[][] grid = imgToArray();
		int [][]blur = new int[imgHeight][imgWidth];
		for(int r = 1; r< imgHeight-1; r++){ // height is the number of rows. row and y value are the same
			for(int c = 1; c< imgWidth-1; c++){
				int red = 0;
				int blue = 0;
				int green = 0;
				for(int rr = r-1; rr<= r+1; rr++){//mini 2d array loops through all pixels surrounding current one (including itself)
					for(int cc = c-1; cc<= c+1; cc++){
						red+=(howRed(grid[rr][cc])/9);
						blue+=(howBlue(grid[rr][cc])/9);
						green+=(howGreen(grid[rr][cc])/9);
					}
				}//end mini 2d array
				blur[r][c] = combine(0, red, green, blue)	;
			}
		}
		arrayToImg(blur);
		return blur;
	}
	
	public void sharpen(){
		int[][] grid = imgToArray();
		
		int [][]sharp = new int[imgHeight][imgWidth];
	
		int sharpr;
		int sharpg;
		int sharpb;
		for(int r = 1; r< imgHeight-1; r++){ // height is the number of rows. row and y value are the same
			for(int c = 1; c< imgWidth-1; c++){
				double red = 0;
				double blue = 0;
				double green = 0;
				
				for(int rr = r-1; rr<= r+1; rr++){//mini 2d array loops through all pixels surrounding current one (including itself)
					for(int cc = c-1; cc<= c+1; cc++){
						if(r==rr && c == cc){//only counts the middle pixel out of the 9; gives all the others values of 0;
							red += 18.0/9*howRed(grid[rr][cc]);
							blue += 18.0/9*howBlue(grid[rr][cc]);
							green += 18.0/9*howGreen(grid[rr][cc]);
							
							
						}
						else{//not the middle pixel
							red += -1.0/9*howRed(grid[rr][cc]);
							blue += -1.0/9*howBlue(grid[rr][cc]);
							green += -1.0/9*howGreen(grid[rr][cc]);
						}
						
						
					}					
				}//end mini 2d array
				sharp[r][c] = combine(0, (int)red, (int)green, (int)blue)	;
			}
		}
		arrayToImg(sharp);
	}
	
	public int[] transformPoint(double[][] matrix, int cx, int ry){
		int x = (int) (matrix[0][0]*cx + matrix[0][1]*ry);
		int y = (int) (matrix[1][0]*cx + matrix[1][1]*ry);
		int[] ans = {x,y};
		return ans;
	}

	/* **************MATRIX STUFF ********************************* */
	
	public void transform(double [][] matrix){
		
		int X = 0, Y=1;
		//int newH = (int) (matrix[1][0]*imgWidth + matrix[1][1]*img);
		// ( imgWidth-1, imgHeight-1) bR
		int[] bR = transformPoint(matrix, imgWidth-1, imgHeight-1);
		// (0, 0)  tL
		int[] tL = transformPoint(matrix, 0, 0);
		//(0, imgHeight-1)  bL
		int[] bL = transformPoint(matrix, 0, imgHeight-1);
		//(imgWidth-1, 0)  tR	
		int[] tR = transformPoint(matrix, imgWidth-1, 0);
		
		int biggestY = Math.max(Math.max(tL[Y], tR[Y]), Math.max(bL[Y], bR[Y]));
		int biggestX = Math.max(Math.max(tL[X], tR[X]), Math.max(bL[X], bR[X]));
		int smallestX = Math.min(Math.min(tL[X], tR[X]), Math.min(bL[X], bR[X]));
		int smallestY = Math.min(Math.min(tL[Y], tR[Y]), Math.min(bL[Y], bR[Y]));
		
		int newW = biggestX - smallestX+1;										/**SHIFTING**/
		int newH = biggestY - smallestY+1;
		
		int [][] orig = imgToArray();
		int [][] trans = new int[newH][newW]; // puts in the new dimensions of the image as the size of the image
		
		for(int ry = 0; ry < orig.length; ry++){
			for(int cx =0; cx< orig[0].length; cx++){
				
				int[] newXY = transformPoint(matrix,cx,ry);
				
				//if(newXY[X]>=0 && newXY[Y]>=0 && newXY[X]< 2*imgWidth && newXY[Y]< 2*imgHeight)
					
					trans[newXY[Y]-smallestY] [newXY[X]-smallestX] = orig[ry][cx];
			}
		}
		arrayToImg(trans);
	}
	
	public void resize(double ratio){
		double[][] matrix = new double[2][2];
		matrix[0][0] = ratio;			matrix[0][1] = 0;
		matrix[1][0] = 0;				matrix[1][1] = ratio;
		transform(matrix);
	}
	
	public void rotate(double angle){
		double[][] matrix = new double[2][2];
		angle*=Math.PI/180;
		matrix[0][0] = Math.cos(angle);		matrix[0][1] = -1*Math.sin(angle);
		matrix[1][0] = Math.sin(angle);		matrix[1][1] = Math.cos(angle);
		transform(matrix);
	}
	
	public void stretchVertically(double v){
		double[][] matrix = new double[2][2];
		matrix[0][0] = 1;		matrix[0][1] = 0;
		matrix[1][0] = 0;		matrix[1][1] = v;
		transform(matrix);
	}
	
	public void stretchHorizontally(double v){
		double[][] matrix = new double[2][2];
		matrix[0][0] = v;		matrix[0][1] = 0;
		matrix[1][0] = 0;		matrix[1][1] = 1;
		transform(matrix);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		((Graphics2D)g).drawImage(img,null,0,0);
		//g.drawImage(img, 0, 0, null);
	}
	
	
	
	
/**  **************** START WITH THESE!  ************** **/
	//Postconditions:  all of the pixels from the original image have been stored
	//  into a 2d array and that 2d array has been returned
	public int[][] imgToArray(){
		int current = 0;//variable for index of current pixel in 1d array of image
		int[][] grid = new int[imgHeight][imgWidth];
		//this puts the pixels into a 1d array.  You want to move them into a 2d array
		int[] pix = img.getRGB(0, 0, imgWidth, imgHeight, null, 0, imgWidth);
		//now do stuff
		for(int r = 0; r< imgHeight; r++){ // height is the number of rows. row and y value are the same
			for(int c = 0; c< imgWidth; c++){
				grid[r][c] = pix[current++];
			}
		}
		
		//this is not what you want to return at all
		//int[][] dummy = new int[5][5];
		//return dummy;
		
		return grid;
	}
	
	//Postconditions:  the pixel values from the given 2d array have been loaded onto
	//  the image
	//HINT:  use this function--> img.setRGB(x,y,val); IMPORTANT:  this function works in an x,y coordinate system (NOT a ROW, COL world)
	public void arrayToImg(int[][] pix){
		imgWidth = pix[0].length;
		imgHeight = pix.length;
		this.setPreferredSize(new Dimension(imgWidth,imgHeight));
		img = new BufferedImage(imgWidth,imgHeight,img.getType());
		//Write code below this comment
		/*
		Your best friend is :  img.setRGB(x,y,val);
		This lets you "paint" onto the image
		You tell it where (x,y) you want to paint
		And what color (pixel value) you want to paint there!
		*/
		for(int r = 0; r< pix.length; r++){
			for(int c = 0; c < pix[0].length; c++){
				img.setRGB(c, r, pix[r][c]);
			}
		}
		
		this.repaint();
	}
	
	//have kids do this first!  just take the pixels and replace them
	public void mirrorV(boolean vert){
		if(!vert)
			return;
		int[][] orig = imgToArray();
		int[][]flip = new int[orig.length][orig[0].length];
		//do stuff
		/** after your have written imageToArray & arrayToImg, do mirror stuff here **/
		for(int r = 0; r<imgHeight; r++){//walks down the 2d array normally
			int far = flip[0].length-1;//
			for(int c = 0; c<imgWidth; c++){//walks backwards through the column
				flip[r][c] = orig[r][far];
				far--;
			}
		}
		//now put the result on the image
		arrayToImg(flip); //MIGHT use a 2nd 2d array here		
	}
	public void mirrorH(boolean vert){
		if(!vert)
			return;
		int[][] orig = imgToArray();
		int[][]flip = new int[orig.length][orig[0].length];
		//do stuff
		/** after your have written imageToArray & arrayToImg, do mirror stuff here **/
		for(int c = 0; c<imgWidth; c++){//walks down the 2d array normally
			int far = flip.length-1;//
			for(int r = 0; r<imgHeight; r++){//walks backwards through the column
				flip[r][c] = orig[far][c];
				far--;
			}
		}
		//now put the result on the image
		arrayToImg(flip); //MIGHT use a 2nd 2d array here		
	}
	
	
	
	public void reset(){
		
		arrayToImg(imgToArray());
	}
	
	//******dummy function for testing **************/
	public void tester(){
		
		
		//red();
		//blue();
		//green();
		//arrayToImg(blah);
		//mirrorH(true);
		//negative();
		//blur();
		//sharpen();
		
		//resize(1);
		//rotate(45);
		//stretchVertically(1);
		//stretchHorizontally(1);
		
		
		//System.out.println("Done testing");
	}


/**  ***************************************************  **/
}
