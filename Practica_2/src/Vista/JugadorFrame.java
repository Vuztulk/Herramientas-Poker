package Vista;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Controlador.Controlador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JugadorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelContenido;
    private List<JButton> botonesRango = new ArrayList<>();
    private List<String> seleccionesGuardadas = new ArrayList<>();
    private JTextArea campoSeleccionado;
    private JSlider slider;
    private JTextField campoPorcentaje;
    private Controlador controlador;
    private int idJugador;
    String cartasRanking[];
    
    private JPanel cardSelectionPanel;
    private JPanel resultPanel;
    private Map<String, JButton> cardButtons;
    private List<String> selectedCards;
    private RangeAnalyzer rangeAnalyzer;
    
    public JugadorFrame(Controlador controlador, int idJugador, String rangoInput, JTextField campoTextoJugador) {
        this.controlador = controlador;
        this.idJugador = idJugador;
        this.cartasRanking = new Sklansky_Chubukov().getCartasOrdenadas();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(0, 0, 1200, 800);
        setTitle("Distribución de Hold'em [Jugador " + idJugador + "]");
        
        panelContenido = new JPanel(new BorderLayout(5, 5));
        setContentPane(panelContenido);

        JTabbedPane panelPestanas = new JTabbedPane(JTabbedPane.TOP);
        
        JPanel panelPreflop = new JPanel(new BorderLayout());
        panelPestanas.addTab("Preflop", null, panelPreflop, "Preflop");

        JPanel analysisPanel = createAnalysisPanel();
        panelPestanas.addTab("Análisis", null, analysisPanel, "Analisis de Rango");
        
        JPanel panelCuadricula = new JPanel(new GridLayout(13, 13, 2, 2));
        JPanel panelConBorder = new JPanel(new BorderLayout());
        panelConBorder.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelConBorder.add(panelCuadricula, BorderLayout.CENTER);

        panelPreflop.add(new JScrollPane(panelConBorder), BorderLayout.CENTER);

        String[] valores = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        agregarBotonesRango(panelCuadricula, valores);

        JPanel panelDerecho = crearPanelDerecho();
        panelPreflop.add(panelDerecho, BorderLayout.EAST);

        JPanel panelInferiorCompleto = crearPanelInferior(campoTextoJugador);
        panelPreflop.add(panelInferiorCompleto, BorderLayout.SOUTH);

        procesarRango(rangoInput);
        panelContenido.add(panelPestanas, BorderLayout.CENTER);
        setVisible(true);
    }

    private void agregarBotonesRango(JPanel panelCuadricula, String[] valores) {
        for (int i = 0; i < valores.length; i++) {
            for (int j = 0; j < valores.length; j++) {
                String textoBoton = (i == j) ? valores[i] + valores[i] : (i < j) ? valores[i] + valores[j] + "s" : valores[j] + valores[i] + "o";
                Color colorFondo = (i == j) ? new Color(140, 230, 140) : (i < j) ? new Color(255, 180, 190) : new Color(170, 210, 230);

                JButton boton = crearBotonRango(textoBoton, colorFondo);
                panelCuadricula.add(boton);
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
            actualizarSeleccion();
        });
        return boton;
    }

    private JPanel crearPanelDerecho() {
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(new EmptyBorder(10, 40, 10, 40));
        panelDerecho.add(new JLabel("SHIFT, CTRL, ALT"));
        panelDerecho.add(new JLabel("Modificar selección"));
        panelDerecho.add(Box.createVerticalStrut(10));

        String[] botonesSeleccion = {"Todos", "Cualquier Suited", "Cualquier Broadway", "Cualquier Par", "Limpiar"};
        for (String texto : botonesSeleccion) {
            JButton boton = new JButton(texto);
            boton.addActionListener(e -> manejarBotonSeleccion(texto));
            panelDerecho.add(boton);
            panelDerecho.add(Box.createVerticalStrut(5));
        }
        panelDerecho.add(Box.createVerticalGlue());

        campoSeleccionado = new JTextArea();
        campoSeleccionado.setEditable(false);
        campoSeleccionado.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(campoSeleccionado);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        panelDerecho.add(new JLabel("Seleccionado:"));
        panelDerecho.add(scrollPane);

        return panelDerecho;
    }

    private JPanel crearPanelInferior(JTextField campoTextoJugador) {
    	slider = new JSlider(0, 1690, 0);
        slider.setMajorTickSpacing(169);
        slider.setMinorTickSpacing(84);
        slider.setPaintLabels(false);
        
        campoPorcentaje = new JTextField("0.0%");
        campoPorcentaje.setEditable(true);
        campoPorcentaje.setPreferredSize(new Dimension(55, 20));
        
        slider.addChangeListener(e -> {
            double porcentaje = slider.getValue() / 16.9;
            campoPorcentaje.setText(String.format("%.1f%%", porcentaje));
            controlador.actualizarPorcentajeJugador(idJugador, porcentaje);
            aplicarRanking(porcentaje);
        });
        
        campoPorcentaje.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                actualizarDesdeTexto();
            }
        });
        
        campoPorcentaje.addActionListener(e -> actualizarDesdeTexto());
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.add(slider);
        sliderPanel.add(campoPorcentaje);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botonAceptar = new JButton("Aceptar");
        JButton botonAplicar = new JButton("Aplicar");
        JButton botonCancelar = new JButton("Cancelar");

        botonAceptar.addActionListener(e -> {
            String rangoSeleccionado = getSeleccionesGuardadas();
            campoTextoJugador.setText(rangoSeleccionado);
            controlador.guardarRangoJugador(idJugador, rangoSeleccionado);
            dispose();
        });
        botonAplicar.addActionListener(e -> {
            String rangoSeleccionado = getSeleccionesGuardadas();
            campoTextoJugador.setText(rangoSeleccionado);
            controlador.guardarRangoJugador(idJugador, rangoSeleccionado);
        });
        botonCancelar.addActionListener(e -> dispose());

        panelInferior.add(botonAceptar);
        panelInferior.add(botonCancelar);
        panelInferior.add(botonAplicar);

        JPanel panelInferiorCompleto = new JPanel();
        panelInferiorCompleto.setLayout(new BoxLayout(panelInferiorCompleto, BoxLayout.Y_AXIS));
        panelInferiorCompleto.add(sliderPanel);
        panelInferiorCompleto.add(panelInferior);

        return panelInferiorCompleto;
    }

    private void procesarRango(String rangoInput) {
        controlador.establecerRangoParaJugador(rangoInput, idJugador);
        controlador.getRangosJugador(idJugador).forEach(carta -> botonesRango.stream()
            .filter(b -> b.getText().equals(carta))
            .forEach(b -> {
                b.setSelected(true);	
                b.setBackground(new Color(180, 120, 160));
            })
        );
        actualizarSeleccion();
    }

    private void manejarBotonSeleccion(String accion) {
        String[] broadway = {"A", "K", "Q", "J", "T"};
        
        botonesRango.forEach(b -> {
            boolean esSuited = b.getText().endsWith("s");
            boolean esPar = b.getText().charAt(0) == b.getText().charAt(1);
            String carta1 = b.getText().substring(0, 1);
            String carta2 = b.getText().substring(1, 2);

            switch (accion) {
                case "Todos":
                    b.setSelected(true);
                    b.setBackground(new Color(180, 120, 160));
                    break;
                case "Cualquier Suited":
                    if (esSuited) {
                        b.setSelected(true);
                        b.setBackground(new Color(180, 120, 160));
                    }
                    break;
                case "Cualquier Broadway":
                    if (contiene(broadway, carta1) && contiene(broadway, carta2)) {
                        b.setSelected(true);
                        b.setBackground(new Color(180, 120, 160));
                    }
                    break;
                case "Cualquier Par":
                    if (esPar) {
                        b.setSelected(true);
                        b.setBackground(new Color(180, 120, 160));
                    }
                    break;
                case "Limpiar":
                    b.setSelected(false);
                    b.setBackground(esSuited ? new Color(255, 180, 190) : esPar ? new Color(140, 230, 140) : new Color(170, 210, 230));
                    break;
            }
        });
        actualizarSeleccion();
    }

    private boolean contiene(String[] arr, String objetivo) {
        for (String s : arr) {
            if (s.equals(objetivo)) return true;
        }
        return false;
    }

    private void actualizarSeleccion() {
        StringBuilder seleccionado = new StringBuilder();
        seleccionesGuardadas.clear();
        int totalCartasSeleccionadas = 0;

        for (JButton b : botonesRango) {
            if (b.isSelected()) {
                seleccionado.append(b.getText()).append(", ");
                seleccionesGuardadas.add(b.getText());
                totalCartasSeleccionadas++;
            }
        }

        if (seleccionado.length() > 0) {
            seleccionado.setLength(seleccionado.length() - 2);
        }

        campoSeleccionado.setText(seleccionado.toString());
        
        double porcentaje = (totalCartasSeleccionadas * 100.0) / botonesRango.size();
        campoPorcentaje.setText(String.format("%.1f%%", porcentaje));
        controlador.actualizarPorcentajeJugador(idJugador, porcentaje);
    }

    public String getSeleccionesGuardadas() {
        return String.join(", ", seleccionesGuardadas);
    }
    
    private void aplicarRanking(double porcentaje) {
        int n_valores = (int) Math.round((porcentaje / 100) * 169);
        
        seleccionesGuardadas.clear();
        for (JButton b : botonesRango) {
            b.setSelected(false);

            String textoBoton = b.getText();
            Color colorOriginal = textoBoton.length() == 2 ? new Color(140, 230, 140) : 
                                  textoBoton.endsWith("s") ? new Color(255, 180, 190) : 
                                  new Color(170, 210, 230);
            b.setBackground(colorOriginal);
        }
        
        for (int i = 0; i < n_valores && i < cartasRanking.length; i++) {
            for (JButton b : botonesRango) {
                if (b.getText().equals(cartasRanking[i])) {
                    b.setSelected(true);
                    b.setBackground(new Color(180, 120, 160));
                    seleccionesGuardadas.add(b.getText());
                    break;
                }
            }
        }

        actualizarSeleccion();
    }

    private void actualizarDesdeTexto() {
        try {
            String texto = campoPorcentaje.getText().replace("%", "").replace(",", ".").trim();
            double porcentaje = Double.parseDouble(texto);
            if (porcentaje >= 0 && porcentaje <= 100) {
                slider.setValue((int) (porcentaje * 16.9));
                controlador.actualizarPorcentajeJugador(idJugador, porcentaje);
                aplicarRanking(porcentaje);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduzca un porcentaje valido entre 0 y 100", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            campoPorcentaje.setText(String.format("%.1f%%", slider.getValue() / 16.9));
        }
    }
    
    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        cardSelectionPanel = createCardSelectionPanel();
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JButton analyzeButton = new JButton("Analizar");
        analyzeButton.addActionListener(e -> performAnalysis());

        panel.add(cardSelectionPanel, BorderLayout.WEST);
        panel.add(resultPanel, BorderLayout.CENTER);
        panel.add(analyzeButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCardSelectionPanel() {
        JPanel panel = new JPanel(new GridLayout(13, 4, 2, 2));
        cardButtons = new HashMap<>();
        selectedCards = new ArrayList<>();

        String[] ranks = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] suits = {"h", "s", "d", "c"};
        Color[] suitColors = {Color.ORANGE, Color.LIGHT_GRAY, Color.RED, Color.GREEN};

        for (String rank : ranks) {
            for (int i = 0; i < suits.length; i++) {
                final Color suitColor = suitColors[i];
                String card = rank + suits[i];
                JButton button = new JButton(card);
                button.setPreferredSize(new Dimension(60, 60));
                button.addActionListener(e -> toggleCardSelection(card, button, suitColor));
                cardButtons.put(card, button);
                button.setBackground(suitColor);	
                panel.add(button);
            }
        }
        return panel;
    }

    private void toggleCardSelection(String card, JButton button, Color suitColor) {
        if (selectedCards.remove(card)) {
            button.setBackground(suitColor);
        } else if (selectedCards.size() < 5) {
            selectedCards.add(card);
            button.setBackground(new Color(180, 120, 160));
        }
    }

    private void performAnalysis() {
        if (selectedCards.size() < 3) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos 3 cartas para el board");
            return;
        }

        Set<String> range = new HashSet<>(seleccionesGuardadas);
        rangeAnalyzer = new RangeAnalyzer(range, selectedCards);
        
        Map<String, Double> probabilities = rangeAnalyzer.getHandProbabilities();
        
        updateResultPanel(probabilities);
    }

    private void updateResultPanel(Map<String, Double> probabilities) {
        resultPanel.removeAll();
        resultPanel.setLayout(new BorderLayout());
        
        JLabel totalCombosLabel = new JLabel("Numero total de combos: " + rangeAnalyzer.getTotalCombos());
        totalCombosLabel.setFont(totalCombosLabel.getFont().deriveFont(14f));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(totalCombosLabel);
        resultPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel handListPanel = new JPanel();
        handListPanel.setLayout(new BoxLayout(handListPanel, BoxLayout.Y_AXIS));
        handListPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        int maxLabelWidth = 0;
        for (String handName : probabilities.keySet()) {
            JLabel tempLabel = new JLabel(handName);
            maxLabelWidth = Math.max(maxLabelWidth, tempLabel.getPreferredSize().width);
        }

        final int finalMaxLabelWidth = maxLabelWidth;

        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            JPanel handPanel = new JPanel();
            handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.X_AXIS));
            
            JLabel handLabel = new JLabel(entry.getKey());
            handLabel.setPreferredSize(new Dimension(finalMaxLabelWidth + 10, handLabel.getPreferredSize().height));
            handPanel.add(handLabel);

            handPanel.add(Box.createHorizontalStrut(10));

            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int) Math.round(entry.getValue()));
            progressBar.setStringPainted(true);
            progressBar.setString(String.format("%.1f%%", entry.getValue()));
            progressBar.setPreferredSize(new Dimension(400, progressBar.getPreferredSize().height));
            handPanel.add(progressBar);

            handPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            handListPanel.add(handPanel);
            handListPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane scrollPane = new JScrollPane(handListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        resultPanel.revalidate();
        resultPanel.repaint();
    }
    
}
