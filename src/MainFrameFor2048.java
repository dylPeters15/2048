
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrameFor2048{


	public static void addComponentsToPane(JFrame frame, Container pane) {


		pane.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		DataStructureFor2048 data = new DataStructureFor2048();

		JLabel[][] labels = data.getJLabels();

		for (int row = 0; row < labels.length; row++){
			for (int col = 0; col < labels[row].length; col++){
				constraints.gridy = row;
				constraints.gridx = col;
				pane.add(labels[row][col],constraints);
			}
		}


		frame.addKeyListener(new KeyListener(){

			boolean upPressed = false;
			boolean downPressed = false;
			boolean leftPressed = false;
			boolean rightPressed = false;

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				int keyCode = e.getKeyCode();
				switch( keyCode ) { 
				case KeyEvent.VK_UP:
					// handle up 
					upPressed = false;
					break;
				case KeyEvent.VK_DOWN:
					// handle down 
					downPressed = false;
					break;
				case KeyEvent.VK_LEFT:
					// handle left
					leftPressed = false;
					break;
				case KeyEvent.VK_RIGHT :
					// handle right
					rightPressed = false;
					break;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (!upPressed && !downPressed && !rightPressed && !leftPressed){
					int keyCode = e.getKeyCode();
					switch( keyCode ) { 
					case KeyEvent.VK_UP:
						// handle up 
						data.swipeUp();
						upPressed = true;
						break;
					case KeyEvent.VK_DOWN:
						// handle down 
						data.swipeDown();
						downPressed = true;
						break;
					case KeyEvent.VK_LEFT:
						// handle left
						data.swipeLeft();
						leftPressed = true;
						break;
					case KeyEvent.VK_RIGHT :
						// handle right
						data.swipeRight();
						rightPressed = true;
						break;
					}
				}
			}
		});

	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("GridBagLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




		///////////////////////////////////////////////////////////////////////////////////
		//		JMenuBar menuBar = new JMenuBar();
		//		frame.setJMenuBar(menuBar);
		//
		//		JMenu file = new JMenu("File");
		//		menuBar.add(file);
		//
		//		JMenuItem newItem = new JMenuItem("New");
		//		file.add(newItem);
		//		newItem.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				//newClicked();
		//			}
		//		});
		//
		//		JMenuItem open = new JMenuItem("Open");
		//		file.add(open);
		//		open.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				//open();
		//			}
		//		});
		//
		//		JMenuItem save = new JMenuItem("Save");
		//		file.add(save);
		//		save.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				//save();
		//			}
		//		});
		//
		//		JMenuItem saveAll = new JMenuItem("Save All");
		//		file.add(saveAll);
		//		saveAll.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				//saveAll();
		//			}
		//		});
		//
		//		JMenuItem close = new JMenuItem("Close");
		//		file.add(close);
		//		close.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				//close();
		//			}
		//		});
		//
		//		JMenuItem closeAll = new JMenuItem("Close All");
		//		file.add(closeAll);
		//		closeAll.addActionListener(new ActionListener() {
		//
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				// TODO Auto-generated method stub
		//				//closeAll();
		//			}
		//		});
		//		
		///////////////////////////////////////////////////////////////////////////////////

		//Set up the content pane.
		addComponentsToPane(frame,frame.getContentPane());

		//Display the window.
		frame.pack();
		frame.setBounds(100,100,200,200);
		frame.setVisible(true);



	}


	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}