import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// This program reads an image, modifies the RGB values of each pixel by adding a specified shift value to each color component (red, green, and blue), and saves the modified image to a new file. 
// The image is processed in parallel using multiple threads, where each thread is responsible for a portion of the image. 

public class SetPixels {
   public static void main(String args[]) {
	
		String fileNameR = null;
		String fileNameW = null;
		
		// if (args.length != 2) {
		// 	System.out.println("Usage: java SetPixel <file to read > <file to write");
		// 	System.exit(1);
		// }
		// fileNameR = args[0];
		// fileNameW = args[1];

		//The same without command line arguments
		fileNameR = "original.jpg";
		fileNameW = "new.jpg";
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(fileNameR));
		} catch (IOException e) {}
		
		int redShift = 100;
		int greenShift = 100;
		int blueShift = 100;

		int numThreads = Runtime.getRuntime().availableProcessors();
		int size = img.getHeight();

		//  Static block partitioning
		int block = size / numThreads;
		int from = 0;
		int to = 0; 

		//thread creation
		SetPixelsThread threads[] = new SetPixelsThread[numThreads];

		//start timing
		long start = System.currentTimeMillis();
      
		// thread execution   
		for(int i = 0; i < numThreads; i++) 
		{
			from = i * block;
			to = i * block + block;
			if (i == (numThreads - 1)) to = size;
			threads[i] = new SetPixelsThread(from, to, img, redShift, greenShift,blueShift);
			threads[i].start();
		}
      
		// wait for threads to terminate            
		for(int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {}
		}

		
	
	    long elapsedTimeMillis = System.currentTimeMillis()-start;
       
		try {
		  File file = new File(fileNameW);
		  ImageIO.write(img, "jpg", file);
		} catch (IOException e) {}
      
		System.out.println("Done...");
		System.out.println("time in ms = "+ elapsedTimeMillis);
   }
}

class SetPixelsThread extends Thread {
	private int from, to;
	private int redShift;
	private	int greenShift;
	private	int blueShift;
	BufferedImage img;

	public SetPixelsThread(int from, int to, BufferedImage img ,int redShift, int greenShift, int blueShift) {
		this.from = from;
		this.to = to;
		this.redShift = redShift;
		this.greenShift = greenShift;
		this.blueShift = blueShift;
		this.img = img;
	}

	public void run() {
		for (int y = from; y < to; y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				//Retrieving contents of a pixel
				int pixel = img.getRGB(x,y);
				//Creating a Color object from pixel value
				Color color = new Color(pixel, true);
				//Retrieving the R G B values, 8 bits per r,g,b
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				//Modifying the RGB values
				red = (red + redShift)%256;
				green = (green + greenShift)%256;
				blue = (blue + blueShift)%256;
				//Creating new Color object
				color = new Color(red, green, blue);
				//Setting new Color object to the image
				img.setRGB(x, y, color.getRGB());
			}
		}
	}
}
