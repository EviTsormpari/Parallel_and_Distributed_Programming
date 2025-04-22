
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// This program converts a color image (RGB) to grayscale using a weighted average for each pixel. 
// The image is processed in parallel using multiple threads, each responsible for a portion of the image. 
// The program then saves the grayscale image to a new file after processing.

public class RGBtoGrayScale {
   public static void main(String args[]) {
		
		String fileNameR = null;
		String fileNameW = null;
		
		//Input and Output files using command line arguments
		// if (args.length != 2) {
		// 	System.out.println("Usage: java RGBtoGrayScale <file to read > <file to write>");
		// 	System.exit(1);
		// }
		// fileNameR = args[0];
		// fileNameW = args[1];
		
		//The same without command line arguments
		fileNameR = "original.jpg";
		fileNameW = "new.jpg";
				
		//Reading Input file to an image
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(fileNameR));
		} catch (IOException e) {}
		
		//Coefficinets of R G B to GrayScale
		double redCoefficient = 0.299;
		double greenCoefficient = 0.587;
		double blueCoefficient = 0.114;

		int numThreads = Runtime.getRuntime().availableProcessors();
		int size = img.getHeight();

		//  Static block partitioning
		int block = size / numThreads;
		int from = 0;
		int to = 0; 

		//thread creation
		RGBThread threads[] = new RGBThread[numThreads]; 

		//start timing
		long start = System.currentTimeMillis();

		// thread execution   
		for(int i = 0; i < numThreads; i++) 
		{
			from = i * block;
			to = i * block + block;
			if (i == (numThreads - 1)) to = size;
			threads[i] = new RGBThread(from, to, img, redCoefficient, greenCoefficient,blueCoefficient);
			threads[i].start();
		}
      
		// wait for threads to terminate            
		for(int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {}
		}
	
	    //Stop timing
	    long elapsedTimeMillis = System.currentTimeMillis()-start;
       
		//Saving the modified image to Output file
		try {
		  File file = new File(fileNameW);
		  ImageIO.write(img, "jpg", file);
		} catch (IOException e) {}
      
		System.out.println("Done...");
		System.out.println("time in ms = "+ elapsedTimeMillis);
   }
}

class RGBThread extends Thread {

	private int from, to;
	private double redCoefficient;
	private double greenCoefficient;
	private double blueCoefficient;
	BufferedImage img;

	public RGBThread(int from, int to, BufferedImage img, double redCoefficient, double greenCoefficient, double blueCoefficient) {
		this.from = from;
		this.to = to;
		this.img = img;
		this.redCoefficient = redCoefficient;
		this.greenCoefficient = greenCoefficient;
		this.blueCoefficient = blueCoefficient;
	}

	public void run() {
		for (int y = from; y < to; y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				//Retrieving contents of a pixel
				int pixel = img.getRGB(x,y);
				//Creating a Color object from pixel value
				Color color = new Color(pixel, true);
				//Retrieving the R G B values, 8 bits per r,g,b
				//Calculating GrayScale
				int red = (int) (color.getRed() * redCoefficient);
				int green = (int) (color.getGreen() * greenCoefficient);
				int blue = (int) (color.getBlue() * blueCoefficient);
				//Creating new Color object
				color = new Color(red+green+blue, 
				                  red+green+blue, 
				                  red+green+blue);
				//Setting new Color object to the image
				img.setRGB(x, y, color.getRGB());
			}
		}
	}

}