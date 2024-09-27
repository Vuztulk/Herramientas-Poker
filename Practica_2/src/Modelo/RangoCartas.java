package Modelo;

import java.util.HashSet;
import java.util.Set;

public class RangoCartas {
    private Set<String> rangos; // Conjunto de cartas en el rango
    private double porcentaje; // Porcentaje de manos que representa el rango

    public RangoCartas(String rangoInput) {
        rangos = new HashSet<>();
        procesarRango(rangoInput);
    }

    private void procesarRango(String rangoInput) {
        String[] partes = rangoInput.split(",");

        for (String parte : partes) {
            parte = parte.trim();
            if (parte.contains("+")) {
                agregarRangoMayor(parte);
            } else if (parte.contains("-")) {
                agregarRangoIntervalo(parte);
            } else {
                rangos.add(parte);
            }
        }

        // Calcular el porcentaje basado en el tamaño del conjunto
        // Por simplicidad, asumo un total de 169 manos posibles en Hold'em
        this.porcentaje = (double) rangos.size() / 169 * 100;
    }

    private void agregarRangoMayor(String parte) {
        String carta = parte.substring(0, 2); // Asumiendo que son dos caracteres
        boolean suited = parte.endsWith("s"); // Verificar si es suited
        char valorCarta = carta.charAt(0); // Valor de la carta (2-10, J, Q, K, A)
        char tipoCarta = carta.charAt(1); // Tipo de carta (suited o offsuit)

        // Agregar cartas superiores
        for (char i = valorCarta; i <= 'A'; i++) {
            String nuevaCarta = String.valueOf(i);
            if (i == 'T') {
                nuevaCarta = "T"; // Manejar el 10 como "T"
            }
            if (suited) {
                rangos.add(nuevaCarta + 's');
            } else {
                rangos.add(nuevaCarta + 's');
                rangos.add(nuevaCarta + 'o');
            }
        }
    }

    private void agregarRangoIntervalo(String parte) {
        String[] limites = parte.split("-");
        String limiteInferior = limites[0].trim();
        String limiteSuperior = limites[1].trim();

        // Asumimos que ambos límites son del mismo tipo (suited u offsuit)
        boolean suited = limiteInferior.endsWith("s") && limiteSuperior.endsWith("s");

        char cartaInferior = limiteInferior.charAt(0); // Valor de la carta inferior
        char cartaSuperior = limiteSuperior.charAt(0); // Valor de la carta superior

        // Agregar cartas entre los límites
        for (char i = cartaInferior; i <= cartaSuperior; i++) {
            String nuevaCarta = String.valueOf(i);
            if (i == 'T') {
                nuevaCarta = "T"; // Manejar el 10 como "T"
            }
            if (suited) {
                rangos.add(nuevaCarta + 's');
            } else {
                rangos.add(nuevaCarta + 's');
                rangos.add(nuevaCarta + 'o');
            }
        }
    }

    public Set<String> getRangos() {
        return rangos;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public String representacionGrafica() {
        // Método para devolver una representación gráfica del rango
        StringBuilder representacion = new StringBuilder();
        for (String rango : rangos) {
            representacion.append(rango).append(", ");
        }
        return representacion.toString().replaceAll(", $", ""); // Eliminar última coma
    }
}
