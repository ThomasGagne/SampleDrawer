import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SampleDrawerPanel extends JPanel{

	private static final long serialVersionUID = 7116512011962239260L;

	private byte[] mSample;
	private final int mDefaultLength = 64;
	private AudioHelper mAudioHelper;

	private JButton mExport, mImport, mPlay, mStop, mStatic, mTriangle, mFlat, mSquare, mSine, mSawtooth;
	private JTextField mSpeed;
	private JSpinner mLength;
	private SpinnerModel mLengthModel;

	public SampleDrawerPanel() {
		mSample = new byte[mDefaultLength];
		mAudioHelper = new AudioHelper();

		setLayout(null);
		setDoubleBuffered(true);

		// Add the play button
		mPlay = new JButton("Play");
		mPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mAudioHelper.play(mSample);
			}
		});
		mPlay.setBounds(85, 5, 75, 25);
		mPlay.setVisible(true);
		add(mPlay);

		// Add the stop button
		mStop = new JButton("Stop");
		mStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mAudioHelper.stop();
			}
		});
		mStop.setBounds(85, 35, 75, 25);
		mStop.setVisible(true);
		add(mStop);

		// Add the speed textfield
		mSpeed = new JTextField(Integer.toString(SampleHelper.getSpeed(mDefaultLength)));
		mSpeed.setHorizontalAlignment(JTextField.RIGHT);
		mSpeed.setBounds(440, 35, 120, 25);
		mSpeed.setVisible(true);
		mSpeed.setEditable(false);
		add(mSpeed);

		// Add the length spinner
		mLengthModel = new SpinnerNumberModel(64, 1, 16384, 1);
		mLengthModel.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent e) {
				// Audio playback can't dynamically change if mSample changes
				mAudioHelper.stop();

				SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
				int newLength = source.getNumber().intValue();
				byte[] newSample = new byte[newLength];

				for(int i = 0; i < newLength; i++) {
					if(i < mSample.length) {
						newSample[i] = mSample[i];
					} else {
						newSample[i] = 0;
					}
				}

				mSample = newSample;
				mSpeed.setText(Integer.toString(SampleHelper.getSpeed(newLength)));
				repaint();
			}

		});
		mLength = new JSpinner(mLengthModel);
		mLength.setBounds(440, 5, 120, 25);
		mLength.setVisible(true);
		add(mLength);

		// Add the export button
		mExport = new JButton("Export");
		mExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.exportData(mSample);
			}
		});
		mExport.setBounds(5, 5, 75, 25);
		mExport.setVisible(true);
		add(mExport);

		// Add the import button
		mImport = new JButton("Import");
		mImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte[] imported = SampleHelper.importData();
				if(imported != null) {
					mSample = imported;
					mSpeed.setText(Integer.toString(SampleHelper.getSpeed(mSample.length)));
					mLengthModel.setValue(mSample.length);
					repaint();
				}
			}
		});
		mImport.setBounds(5, 35, 75, 25);
		mImport.setVisible(true);
		add(mImport);

		// Add the invert button
		mStatic = new JButton("Static");
		mStatic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.generateStatic(mSample);
				repaint();
			}
		});
		mStatic.setBounds(235, 5, 75, 25);
		mStatic.setVisible(true);
		add(mStatic);

		// Add the reverse button
		mTriangle = new JButton("Triangle");
		mTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.generateTriangle(mSample);
				repaint();
			}
		});
		mTriangle.setBounds(305, 5, 75, 25);
		mTriangle.setVisible(true);
		add(mTriangle);

		// Add the static button
		mFlat = new JButton("Flat");
		mFlat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.generateFlat(mSample);
				repaint();
			}
		});
		mFlat.setBounds(165, 5, 75, 25);
		mFlat.setVisible(true);
		add(mFlat);

		// Add the sine button
		mSine = new JButton("Sine");
		mSine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.generateSine(mSample);
				repaint();
			}
		});
		mSine.setBounds(165, 35, 75, 25);
		mSine.setVisible(true);
		add(mSine);

		// Add the sawtooth button
		mSawtooth = new JButton("Saw");
		mSawtooth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.generateSawtooth(mSample);
				repaint();
			}
		});
		mSawtooth.setBounds(235, 35, 75, 25);
		mSawtooth.setVisible(true);
		add(mSawtooth);

		// Add the squarewave button
		mSquare = new JButton("Square");
		mSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleHelper.generateSquare(mSample);
				repaint();
			}
		});
		mSquare.setBounds(305, 35, 75, 25);
		add(mSquare);

		// MouseListener stuff
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				affectMouseChange(e.getX(), e.getY());
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		addMouseMotionListener(new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {}

			public void mouseDragged(MouseEvent e) {
				affectMouseChange(e.getX(), e.getY());
			}
		});
	}

	private void affectMouseChange(int mouseX, int mouseY) {
		// Helps the user draw at the top and bottom edges at the window
		if(mouseY < 67 && mouseY > 57) {
			mouseY = 67;
		} else if(mouseY > SampleDrawer.mWindowHeight - 23 && mouseY < SampleDrawer.mWindowHeight - 13) {
			mouseY = SampleDrawer.mWindowHeight - 24;
		}

		if(mouseX >= 0 && mouseX < SampleDrawer.mWindowWidth && mouseY >= 67 && mouseY < SampleDrawer.mWindowHeight - 23) {
			double lineLength = (double) (SampleDrawer.mWindowWidth) / mSample.length;
			int bite = (int) (mouseX / lineLength);
			int newVal = SampleDrawer.mWindowHeight - mouseY - 23 - 128;
			mSample[bite] = (byte) newVal;
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g){
		// Draw the button separator lines
		g.setColor(Color.lightGray);
		g.drawLine(232, 10, 232, 25);
		g.drawLine(380, 10, 380, 55);
		g.drawLine(82, 10, 82, 55);
		g.drawLine(162, 10, 162, 55);

		// Fill in the drawing area color
		g.setColor(new Color(0xD5F3F5));
		g.fillRect(0, 67, 570, 256);

		// Draw the drawing-area separation lines
		g.setColor(Color.gray);
		g.drawLine(0, 65, 570, 65);
		g.drawLine(0, 66, 570, 66);

		// Draw text with antialiasing
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		g2d.drawString("Length:", 390, 20);
		g2d.drawString("Speed:", 390, 50);

		// Draw the current sample
		double lineLength = (double) (SampleDrawer.mWindowWidth) / mSample.length;
		for(int i = 0; i < mSample.length; i++) {
			g2d.drawLine((int) (i * lineLength), SampleDrawer.mWindowHeight - mSample[i] - 23 - 128, (int) ((i + 1) * lineLength), SampleDrawer.mWindowHeight - mSample[i] - 23 - 128);
		}

	}
}
