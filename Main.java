package mindreader;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



// When press a pbutton
public class Main {
	static JFrame frmMain;
	static JPanel pnlMain, pnlProgress;
	static JLabel lblNumber, lblProgress, lblResult;
	static JButton btnRead;
	static JTextField txfInput;
	static JProgressBar prgBar;
	private static SwingWorker<Void, Integer> worker;

	static int WIDTH = 500,
				HEIGHT = 300;

	static class btnReadAction implements ActionListener {
		public void actionPerformed(ActionEvent e)  {
			// System.out.println(e.getActionCommand());
			String inputNumber = txfInput.getText();

			try {
				int number = Integer.parseInt(inputNumber);
				if (number <= 0 || number > 10) {
					JOptionPane.showMessageDialog(null, "Type a number between 1 and 10", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(null, "Type a number", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}


			btnRead.setEnabled(false); // Disable button

			// Create dialog
			final JDialog dlgProgress = new JDialog(frmMain, "Reading mind", true);
			
			pnlProgress = new JPanel();

			lblProgress = new JLabel("Analyzing brainwaves...");
			pnlProgress.add(lblProgress);

			prgBar = new JProgressBar(0, 100);
			prgBar.setStringPainted(true); // Show value
			pnlProgress.add(prgBar);

			dlgProgress.add(pnlProgress, BorderLayout.CENTER);
			dlgProgress.getContentPane().setPreferredSize(new Dimension(300, 50));
			dlgProgress.pack();
			dlgProgress.setLocationRelativeTo(frmMain);
			// dlgProgress.setVisible(true);

			// Check if was closed before the process end
			dlgProgress.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					worker.cancel(true);
				}
			});


			// Simulate progress
			worker = new SwingWorker<Void, Integer>() {
				@Override
				protected Void doInBackground() throws Exception {
					for(int i = 0; i <= 100; i++) {
						if (isCancelled())
							break;

						Thread.sleep(50);
						switch (i) { // Change messages
							case 30:
								lblProgress.setText("Scanning memories...");
								break;
							case 50:
								lblProgress.setText("Calculating probabilities...");
								break;
							case 70:
								lblProgress.setText("Decoding thoughts...");
								break;
						}

						publish(i);
					}
					return null;
				}

				@Override
				protected void process(java.util.List<Integer> chunks) {
					// Update progress bar with last published value
					int progressValue = chunks.get(chunks.size() - 1);
					prgBar.setValue(progressValue);
				}

				@Override
				protected void done() {
					// Close dialog progress
					dlgProgress.dispose();
					if(!isCancelled()) // Was not closed before ending
						JOptionPane.showMessageDialog(frmMain, "You're thinking of the number " + inputNumber, "Mind Reader", JOptionPane.INFORMATION_MESSAGE);
					btnRead.setEnabled(true);
				}
			};
			worker.execute();
			dlgProgress.setVisible(true);
		}
	}


	public static void main(String[] args) {
		frmMain = new JFrame("Mind Reader");
		frmMain.setSize(WIDTH, HEIGHT);
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pnlMain = new JPanel();
		pnlMain.setLayout(new GridBagLayout()); // Align center

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new java.awt.Insets(10, 10, 10, 10); // 10 pixels margin
		gbc.gridx = 0;

		lblNumber = new JLabel();
		lblNumber.setText("Think of a number between 1 and 10");
		gbc.gridy = 0;
		pnlMain.add(lblNumber, gbc);

		txfInput = new JTextField(20);
		gbc.gridy = 1;
		pnlMain.add(txfInput, gbc);

		btnRead = new JButton("Read my mind");
		btnRead.addActionListener(new btnReadAction());
		gbc.gridy = 2;
		pnlMain.add(btnRead, gbc);

		// prgBar = new JProgressBar(0, 100);
		// prgBar.setStringPainted(true); // Show value	
		// gbc.gridy = 3;
		// pnlMain.add(prgBar, gbc);


		frmMain.add(pnlMain);

		frmMain.pack(); // Adjust size
		frmMain.setLocationRelativeTo(null); // Screen center
		frmMain.setResizable(false);
		frmMain.setVisible(true);
	}	
}
