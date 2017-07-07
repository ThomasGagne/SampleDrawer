import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


public class SampleHelper {

	private static final double mSpeedMod = 130.671875;

	public static int getSpeed(int length) {
		return (int) (length * mSpeedMod);
	}

	public static void exportData(byte[] data) {
		JFileChooser chooser = new JFileChooser();
	      int chosenOption = chooser.showSaveDialog(null);
	      if (chosenOption == JFileChooser.APPROVE_OPTION) {

	    	  FileOutputStream fos = null;
	    	  try {
	    		  // Our data representation is off by 128 so we can draw/edit it easily
	    		  // This changes it so that the resulting file matches what the screen shows
	    		  byte[] outputData = new byte[data.length];
	    		  for(int i = 0; i < data.length; i++) {
	    			  outputData[i] = (byte) (data[i] + 128);
	    		  }

	    		  fos = new FileOutputStream(chooser.getSelectedFile());
	    		  fos.write(outputData);
	    	  } catch (FileNotFoundException e) {
	    		  JOptionPane.showMessageDialog(null,
	    				    "Error: the selected file could not be saved properly.",
	    				    "Error",
	    				    JOptionPane.ERROR_MESSAGE);
	    		  e.printStackTrace();
	    	  } catch (IOException e) {
	    		  JOptionPane.showMessageDialog(null,
	    				    "Error: the selected file could not be saved properly.",
	    				    "Error",
	    				    JOptionPane.ERROR_MESSAGE);
	    		  e.printStackTrace();
	    	  } finally {
	    		  try {
    			      fos.close();
	    		  } catch (IOException e) {
	    			  e.printStackTrace();
	    		  }
	    	  }
	     }
	}

	public static byte[] importData() {
		JFileChooser chooser = new JFileChooser();
	    int chosenOption = chooser.showOpenDialog(null);
	    if (chosenOption == JFileChooser.APPROVE_OPTION) {
	    	try {
	    		byte[] imported = Files.readAllBytes(Paths.get(chooser.getSelectedFile().getAbsolutePath()));
	    		byte[] result = new byte[imported.length];
	    		// Our data representation is off by 128 so we can draw/edit it easily
	    		// This changes it so that the screen matches the file's actual data
	    		for(int i = 0; i < result.length; i++) {
	    			result[i] = (byte) (imported[i] - 128);
	    		}
	    		return result;
	    	} catch (IOException e) {
	    		JOptionPane.showMessageDialog(null,
    				    "The selected file could not be opened.",
    				    "Error",
    				    JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return null;
			}
	    } else {
	      return null;
	    }
	}

	public static void generateStatic(byte[] sample) {
		Random r = new Random();

		for(int i = 0; i < sample.length; i++) {
			sample[i] = (byte) r.nextInt(255);
		}
	}

	public static void generateTriangle(byte[] sample) {
		for(int i = 0; i < sample.length / 2; i++) {
			sample[i] = (byte) (127 + ((256 * i) / (sample.length / 2)));
		}

		for(int i = sample.length / 2; i < sample.length; i++) {
			sample[i] = (byte) (127 - ((256 * i) / (sample.length / 2)));
		}
	}

	public static void generateFlat(byte[] sample) {
		for(int i = 0; i < sample.length; i++) {
			sample[i] = 0;
		}
	}

	public static void generateSquare(byte[] sample) {
		for(int i = 0; i < sample.length / 2; i++) {
			sample[i] = 127;
		}

		for(int i = sample.length / 2; i < sample.length; i++) {
			sample[i] = -127;
		}
	}

	public static void generateSawtooth(byte[] sample) {
		for(int i = 0; i < sample.length; i++) {
			sample[i] = (byte) (i * ((double) (256) / sample.length) - 127);
		}
	}

	public static void generateSine(byte[] sample) {
		for(int i = 0; i < sample.length; i++) {
			double angle = 2.0 * Math.PI * i / sample.length;
			sample[i] = (byte)(Math.sin(angle) * 127f);
		}
	}

}
