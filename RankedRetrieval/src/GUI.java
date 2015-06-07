import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class GUI {

	private JFrame frame;
	DefaultTableModel model;
	private JTable table;
	public static JTextField txtTypeYourQuery;
	String col[] = { "Doc#", "Content", "URL" };

	/**
	 * Launch the application.
	 */
	public static void main_gui() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//table = new JTable();
		table = new javax.swing.JTable(){
			//add tooltip to display the full cell text when not displayed
			public String getToolTipText( MouseEvent e )
			{
			int row = rowAtPoint( e.getPoint() );
			int column = columnAtPoint( e.getPoint() );

			Object value = getValueAt(row, column);
			return value == null ? null : value.toString();
			}
			}
			;
		table.setBounds(55, 96, 700, 700);
		JScrollPane jPane = new JScrollPane(table);
		jPane.setBounds(55, 96, 800, 800);
		// frame.getContentPane().add(table);
		frame.getContentPane().add(jPane);

		txtTypeYourQuery = new JTextField();
		txtTypeYourQuery.setBounds(52, 28, 500, 26);
		frame.getContentPane().add(txtTypeYourQuery);
		txtTypeYourQuery.setColumns(20);
		model = new DefaultTableModel(col, 2);

		JButton btnNewButton = new JButton("Search");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int rowCount = model.getRowCount();
					// Remove rows one by one from the end of the table
					for (int j = rowCount - 1; j >= 0; j--) {
						model.removeRow(j);
					}
					table.setRowHeight(50);
					Main.buttonClicked(txtTypeYourQuery.getText());
					int i = 1;
					for (Result r : Main.results) {
						model.addRow(new Object[] { r.fileName, r.paragraph,
								r.url });
						i++;
					}

					// int lines = 2;
					table.setModel(model);
					// table.setRowHeight(table.getRowHeight() * lines);

					//table.getColumnModel().getColumn(1).setCellRenderer(new LineWrapCellRenderer());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


		// table.setDefaultRenderer(String.class, new LineWrapCellRenderer());
		//table.setCellSelectionEnabled(false);
		btnNewButton.setBounds(600, 27, 115, 29);
		frame.getContentPane().add(btnNewButton);
		frame.setTitle("Spatial Retrieval System - Team 11");
	}

	public class LineWrapCellRenderer extends JTextArea implements
			TableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			this.setText((String) value);
			int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
			int textLength = this.getText().length();
			if(this.getColumns()==-1){
				this.setColumns(1);
			}
			int lines = textLength / this.getColumns() + 1;// +1, cause we need
			// at least 1 row.
			int height = fontHeight * lines;
			table.setRowHeight(row, height);
			this.setWrapStyleWord(true);
			this.setLineWrap(true);
			return this;
		}

	}

	class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

		public MultiLineCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager
							.getColor("Table.focusCellForeground"));
					setBackground(UIManager
							.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(1, 2, 1, 2));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}

	}
}
