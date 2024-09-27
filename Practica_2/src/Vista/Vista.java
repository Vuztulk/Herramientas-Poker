package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Vista extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField rangoInput;
	private JButton convertirButton;
	private JPanel gridPanel;
	private JTextArea outputText;
	private JButton clearButton, anySuitedButton, anyPairButton;
	private JPanel inputPanel_1;
	private JPanel mainPanel;
	private JButton evaluarButton;

	public Vista() {
		super("PokerStove");
		getContentPane().setLayout(null);

		// Panel principal
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 1200, 800);
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		crearJugadores(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 518, 762, 236);
		mainPanel.add(textArea);
		
		JButton boton_evaluar = new JButton("Evaluar");
		boton_evaluar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		boton_evaluar.setBounds(570, 359, 94, 52);
		mainPanel.add(boton_evaluar);
		
		JButton boton_limpiar = new JButton("Limpiar");
		boton_limpiar.setBounds(570, 421, 94, 21);
		boton_limpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mainPanel.add(boton_limpiar);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 800);
		setVisible(true);
	}

	public void crearJugadores(int numJugadores) {
	    JPanel topLeftPanel = new JPanel(new GridLayout(numJugadores, 1));
	    topLeftPanel.setBounds(24, 10, 420, 498);

	    for (int i = 1; i <= numJugadores; i++) {
	        JPanel jugadorPanel = new JPanel(new BorderLayout());

	        final int playerId = i;  // Crear una variable final
	        JButton jugadorButton = new JButton("Player " + playerId);
	        jugadorButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	new JugadorFrame(playerId);
	            }
	        });
	        jugadorPanel.add(jugadorButton, BorderLayout.WEST);

	        JTextField rangoInput = new JTextField(40); 
	        jugadorPanel.add(rangoInput, BorderLayout.CENTER);

	        topLeftPanel.add(jugadorPanel);
	    }
	    mainPanel.add(topLeftPanel);
	}



}
