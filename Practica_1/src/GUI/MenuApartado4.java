package GUI;

import Controlador.Controller;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class MenuApartado4 extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<String> manos;
	private Controller controller;

	public MenuApartado4(List<String> manos, Controller controller) {
		this.manos = manos;
		this.controller = controller;
		initialize();
	}

	private void initialize() {
	    setLayout(new BorderLayout());

	    JPanel resultadosPanel = new JPanel();
	    resultadosPanel.setLayout(new GridLayout(manos.size(), 1));

	    List<String> resultados = controller.obtenerManoOmagaRaw(manos); 
	    
	    for (int m = 0; m < manos.size(); m++) {
	        String mano = resultados.get(m);
	        String boardInicial = manos.get(m);
	        
	        JPanel manoPanel = new JPanel();
	        manoPanel.setLayout(new BorderLayout());
	        manoPanel.setBorder(new EmptyBorder(0, 0, 80, 0));
	        
	        JLabel boardLabel = new JLabel("Board inicial: " + boardInicial);
	        boardLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        boardLabel.setFont(new Font("Arial", Font.BOLD, 20));
	        manoPanel.add(boardLabel, BorderLayout.NORTH);
	        
	        JPanel cartasPanel = new JPanel();
	        cartasPanel.setLayout(new FlowLayout());

	        for (int i = 0; i < mano.length(); i += 2) {
	            String carta = mano.substring(i, i + 2);
	            String imagePath = UtilidadesGUI.getCartaPath(carta);
	            if (imagePath != null) {
	                ImageIcon icon = new ImageIcon(imagePath);
	                Image img = icon.getImage().getScaledInstance(75, 100, Image.SCALE_SMOOTH);
	                ImageIcon scaledIcon = new ImageIcon(img);
	                JLabel cartaLabel = new JLabel(scaledIcon);
	                cartasPanel.add(cartaLabel);
	            }
	        }
	        manoPanel.add(cartasPanel, BorderLayout.CENTER);
	        resultadosPanel.add(manoPanel);
	    }
	    
	    JScrollPane scrollPane = new JScrollPane(resultadosPanel);
	    add(scrollPane, BorderLayout.CENTER);
	}

}
