package views;

import bin.State;
import bin.StationManage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class StationPanel extends JPanel {
	private JPanel userLoginPanel;

	StationPanel(JPanel userLoginPanel){
		this.userLoginPanel = userLoginPanel;
		JPanel myPanel = new MyPanel();

		this.add(new JPanel());
		this.add(myPanel);
		this.add(new JPanel());
		
		this.setLayout(new GridLayout(3,1));
		this.setVisible(true);
	}
	

	
	class MyPanel extends JPanel implements ActionListener{
		MyPanel(){
			GotoButton buttonA = new GotoButton("Station A", userLoginPanel);
			GotoButton buttonB = new GotoButton("Station B", userLoginPanel);
			GotoButton buttonC = new GotoButton("Station C", userLoginPanel);

			buttonA.setFont(new Font("Times New Roman", Font.PLAIN, 40)); 
			buttonB.setFont(new Font("Times New Roman", Font.PLAIN, 40)); 
			buttonC.setFont(new Font("Times New Roman", Font.PLAIN, 40)); 
			
			buttonA.addActionListener(this);
			buttonB.addActionListener(this);
			buttonC.addActionListener(this);
			
			this.add(buttonA);
			this.add(buttonB);
			this.add(buttonC);
			
			this.setLayout(new GridLayout(1,3));
			
		}
		
		public void actionPerformed(ActionEvent e){
			String actionCommand = e.getActionCommand();
			switch (actionCommand) {
				case "Station A":
					State.setCurrentStation(StationManage.findStationById(1));
					System.out.println("station chose: A");
					break;
				case "Station B":
					State.setCurrentStation(StationManage.findStationById(2));
					System.out.println("station chose: B");
					break;
				case "Station C":
					State.setCurrentStation(StationManage.findStationById(3));
					System.out.println("station chose: C");
					break;
			}
		}
		
		
	}
	


}