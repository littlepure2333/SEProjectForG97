package views;

import bin.TransactionManage;
import views.components.PanelStateMonitor;

import javax.swing.*;
import java.awt.*;

class UserInformationPanel extends JPanel implements PanelStateMonitor {
	/**
	 * Create the panel.
	 */
	UserInformationPanel() {
	}

	/**
	 * 界面组件的重新渲染
	 */
	@Override
	public void update() {
		this.removeAll();
		String[] columnNames = {"Time", "Name", "Type", "ScooterID", "StationName"};


		String[][] data = TransactionManage.getAllTransactions();

		JTable table = new JTable(data, columnNames);
		table.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		table.setBounds(10, 263, 310, -300);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
		this.add(scrollPane);
	}
}