package GUI;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class panel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JSplitPane splitPane;

	public panel() {
		splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(splitPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
		);

		splitPane.setDividerLocation(300);
		setLayout(groupLayout);
	}
	
}