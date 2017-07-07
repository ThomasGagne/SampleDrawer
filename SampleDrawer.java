import javax.swing.JFrame;
import javax.swing.JPanel;


public class SampleDrawer {

	public static final int mWindowHeight = 345;
	public static final int mWindowWidth = 570;

	public static void main(String[] args){
		JFrame frame = new JFrame("Sample Drawer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new SampleDrawerPanel();
		frame.add(panel);
		frame.setResizable(false);

		frame.pack();
		frame.setSize(mWindowWidth, mWindowHeight);
		frame.setVisible(true);
	}
}
