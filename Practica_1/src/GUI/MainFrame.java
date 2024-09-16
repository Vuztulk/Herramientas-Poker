package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Controller;
import Modelo.Model;
import Vista.View;

import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.JLayeredPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Model model = new Model();
		            View vista = new View();
		            Controller control = new Controller(model, vista);
		            
		            MainFrame mainFrame = new MainFrame(control);
		            mainFrame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame(Controller controller) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1371, 804);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "name_5452761031600");
		
		JButton btnNewButton = new JButton("New button");
		tabbedPane.addTab("New tab", null, btnNewButton, null);
	}
}
