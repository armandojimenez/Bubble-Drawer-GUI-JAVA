import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BubblePanel extends JPanel {

	private ArrayList<Bubble> bubbleList;
	private int size = 60;
	private Timer timer;
	private final int DELAY = 33; // ms of delay for 30 fps
	private JTable table;
	private JTextField txtSize;
	private JTextField txtSpeed;
	private JButton btnUpdate;
	private JButton btnClear;
	private JCheckBox chkGroup;
	private JCheckBox chkPause;

	public BubblePanel() {

		bubbleList = new ArrayList<Bubble>();

		addMouseListener(new BubbleListener());
		addMouseMotionListener(new BubbleListener());
		addMouseWheelListener(new BubbleListener());

		timer = new Timer(DELAY, new BubbleListener());

		setBackground(Color.BLACK);

		setPreferredSize(new Dimension(2000, 1500));

		table = new JTable();
		add(table);

		JPanel panel = new JPanel();
		panel.setBackground(Color.CYAN);
		add(panel);

		JLabel lblDotSize = new JLabel("Dot Size:");
		lblDotSize.setFont(new Font("Tahoma", Font.PLAIN, 45));
		panel.add(lblDotSize);

		txtSize = new JTextField();
		txtSize.setFont(new Font("Tahoma", Font.PLAIN, 45));
		txtSize.setHorizontalAlignment(SwingConstants.CENTER);
		txtSize.setText("60");
		panel.add(txtSize);
		txtSize.setColumns(3);

		JLabel llblAnimationSpeedfps = new JLabel("Animation Speed (fps):");
		llblAnimationSpeedfps.setFont(new Font("Tahoma", Font.PLAIN, 45));
		panel.add(llblAnimationSpeedfps);

		txtSpeed = new JTextField();
		txtSpeed.setFont(new Font("Tahoma", Font.PLAIN, 45));
		txtSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		txtSpeed.setText("30");
		panel.add(txtSpeed);
		txtSpeed.setColumns(2);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get the bubble size

				int newSize = Integer.parseInt(txtSize.getText());

				// get the animation speed

				int newSpeed = Integer.parseInt(txtSpeed.getText());

				// set the bubble size

				size = newSize;

				// set the animation speed

				timer.setDelay(1000 / newSpeed);
				repaint();

			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 45));
		panel.add(btnUpdate);

		btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 45));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bubbleList = new ArrayList<Bubble>();
				repaint();

			}
		});

		chkGroup = new JCheckBox("Group Bubbles");
		chkGroup.setFont(new Font("Tahoma", Font.PLAIN, 45));
		panel.add(chkGroup);

		chkPause = new JCheckBox("Pause");
		chkPause.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chkPause.isSelected()) {
					timer.stop();
				}

				if (!chkPause.isSelected()) {
					timer.start();
				}
			}
		});
		chkPause.setFont(new Font("Tahoma", Font.PLAIN, 45));
		panel.add(chkPause);
		panel.add(btnClear);

		timer.start();

	}

	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		// draw all the Bubbles from bubbleList
		for (Bubble bubble : bubbleList) {
			page.setColor(bubble.color);
			page.fillOval(bubble.x - bubble.size / 2, bubble.y - bubble.size / 2, bubble.size, bubble.size);
		}

		// count how many bubbles on screen

		page.setColor(Color.GREEN);
		page.drawString("Count: " + bubbleList.size(), 5, 15);

	}

	private class BubbleListener implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// timer.stop(); // stop animation

			// add to the bubbleList my mouse location
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));

			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			// timer.start();
		}

		@Override
		public void mouseDragged(MouseEvent e) {

			// add to the bubbleList my mouse location
			bubbleList.add(new Bubble(e.getX(), e.getY(), size));

			// check to see if the Group Bubbles checkbox is checked
			if (chkGroup.isSelected()) {
				// set the x speed and y speed of this bubble to the previous
				// bubble speed
				bubbleList.get(bubbleList.size() - 1).xspeed = bubbleList.get(bubbleList.size() - 2).xspeed;
				bubbleList.get(bubbleList.size() - 1).yspeed = bubbleList.get(bubbleList.size() - 2).yspeed;

			}

			repaint();

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

			// change size when the wheel is moved
			size -= e.getWheelRotation();

			txtSize.setText("" + size);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// update the location of each bubble for the animation

			for (Bubble bubble : bubbleList)
				bubble.update();

			// repaint screen
			repaint();

		}

	}

	private class Bubble {
		/** A bubble needs an x,y location and a size */

		public int x;
		public int y;
		public int size;
		public Color color;
		public int xspeed;
		public int yspeed;
		private final int MAX_SPEED = 10;

		public Bubble(int newX, int newY, int newSize) {
			x = newX;
			y = newY;
			size = newSize;
			float transparency = (float) Math.random();
			if (transparency < .10)
				transparency = (float) .15;
			color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), transparency);
			xspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
			yspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
			if (xspeed == 0 && yspeed == 0) {
				xspeed = 3;
				yspeed = 3;
			}

		}

		public void update() {
			x += xspeed;
			y += yspeed;

			// collision detection with the edges of the panel

			if (x <= size / 2 || x + size / 2 >= getWidth())
				xspeed = -1 * xspeed;

			if (y <= size / 2 || y + size / 2 >= getHeight())
				yspeed = -yspeed;

		}
	}

}
