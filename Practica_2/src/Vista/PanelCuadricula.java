package Vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelCuadricula extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<JButton> botonesRango = new ArrayList<>();
	private JugadorFrame jugadorFrame;
	
    public PanelCuadricula(String[] valores, JugadorFrame jugadorFrame) {
        super(new GridLayout(13, 13, 2, 2));
        this.jugadorFrame = jugadorFrame;
        agregarBotonesRango(valores);
    }

    private void agregarBotonesRango(String[] valores) {
        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores.length; j++) {
                String textoBoton;
                Color colorFondo;

                if (i == j)
                    textoBoton = valores[i] + valores[i];
                else if (i < j)
                    textoBoton = valores[i] + valores[j] + "s";
                else
                    textoBoton = valores[j] + valores[i] + "o";

                if (i == j)
                    colorFondo = new Color(140, 230, 140);
                else if (i < j)
                    colorFondo = new Color(255, 180, 190);
                else
                    colorFondo = new Color(170, 210, 230);

                JButton boton = crearBotonRango(textoBoton, colorFondo);
                add(boton);
                botonesRango.add(boton);
            }
        }
    }

    private JButton crearBotonRango(String textoBoton, Color colorFondo) {
        JButton boton = new JButton(textoBoton);
        boton.setBackground(colorFondo);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        
        boton.addActionListener(e -> {
            boton.setSelected(!boton.isSelected());
            boton.setBackground(boton.isSelected() ? new Color(180, 120, 160) : colorFondo);
            jugadorFrame.actualizarSeleccion();
        });
        
        return boton;
    }

    public List<JButton> getBotonesRango() {
        return botonesRango;
    }
}
