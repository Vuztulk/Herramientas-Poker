package Vista;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class JugadorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public JugadorFrame(int playerId) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JLabel label = new JLabel("Panel del Jugador " + playerId);
        contentPane.add(label, BorderLayout.NORTH);
        
        JTextField rangoInput = new JTextField("Introduce tu rango aquí");
        contentPane.add(rangoInput, BorderLayout.CENTER);

        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        contentPane.add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}
