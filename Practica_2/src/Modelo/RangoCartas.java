package Modelo;

import java.util.HashSet;
import java.util.Set;

public class RangoCartas {
    private Set<String> rangos;
    private double porcentaje;

    public RangoCartas(String rangoInput) {
        if (rangoInput == null || rangoInput.trim().isEmpty()) {
        	rangos = new HashSet<>();
            this.porcentaje = 0;
            return;
        }
        rangos = new HashSet<>();
        procesarRango(rangoInput);
    }

    private void procesarRango(String rangoInput) {
        String[] partes = rangoInput.split(",");

        for (String parte : partes) {
            parte = parte.trim();
            if (parte.isEmpty()) continue;

            if (parte.contains("+")) {
                agregarRangoMayor(parte);
            } else if (parte.contains("-")) {
                agregarRangoIntervalo(parte);
            } else {
                rangos.add(parte);
            }
        }

        this.porcentaje = (double) (rangos != null ? rangos.size() : 0) / 169 * 100;
    }

    private void agregarRangoMayor(String parte) {
        String carta = parte.substring(0, 2);
        boolean suited = parte.endsWith("s");
        char valorCarta = carta.charAt(0);

        for (char i = valorCarta; i <= 'A'; i++) {
            String nuevaCarta = String.valueOf(i);
            if (i == 'T') {
                nuevaCarta = "T";
            }
            if (suited) {
                if (rangos != null) {
                    rangos.add(nuevaCarta + 's');
                }
            } else {
                if (rangos != null) {
                    rangos.add(nuevaCarta + 's');
                    rangos.add(nuevaCarta + 'o');
                }
            }
        }
    }

    private void agregarRangoIntervalo(String parte) {
        String[] limites = parte.split("-");
        String limiteInferior = limites[0].trim();
        String limiteSuperior = limites[1].trim();

        boolean suited = limiteInferior.endsWith("s") && limiteSuperior.endsWith("s");

        char cartaInferior = limiteInferior.charAt(0);
        char cartaSuperior = limiteSuperior.charAt(0);

        for (char i = cartaInferior; i <= cartaSuperior; i++) {
            String nuevaCarta = String.valueOf(i);
            if (i == 'T') {
                nuevaCarta = "T";
            }
            if (suited) {
                if (rangos != null) {
                    rangos.add(nuevaCarta + 's');
                }
            } else {
                if (rangos != null) {
                    rangos.add(nuevaCarta + 's');
                    rangos.add(nuevaCarta + 'o');
                }
            }
        }
    }

    public Set<String> getRangos() {
        return rangos;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void actualizarPorcentaje(double nuevoPorcentaje) {
        this.porcentaje = nuevoPorcentaje;
    }
}
