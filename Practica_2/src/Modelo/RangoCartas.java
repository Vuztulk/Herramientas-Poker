package Modelo;

import java.util.HashSet;
import java.util.Set;

public class RangoCartas {
    private Set<String> rangos;
    private double porcentaje;
    private static final String ordenCartas = "23456789TJQKA";

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
            if (parte.isEmpty())
            	continue;
            if (parte.contains("+")) {
                agregarRangoMayor(parte);
            } else if (parte.contains("-")) {
                agregarRangoIntervalo(parte);
            } else {
                rangos.add(parte);
            }
        }
        this.porcentaje = (double) rangos.size() / 1326 * 100;
    }

    private void agregarRangoMayor(String parte) {
        String carta = parte.substring(0, 2);
        boolean suited = parte.contains("s");
        boolean offSuited = parte.contains("o");
        char valorCarta = carta.charAt(0);
        char segundaCarta = carta.charAt(1);

        if (valorCarta == segundaCarta) {
            for (int i = getRankValue(valorCarta); i < ordenCartas.length(); i++) {
                char cartaActual = ordenCartas.charAt(i);
                rangos.add(String.valueOf(cartaActual) + String.valueOf(cartaActual));
            }
        } else {
            int indiceValorCarta = ordenCartas.indexOf(valorCarta);
            int indiceSegundaCarta = ordenCartas.indexOf(segundaCarta);

            for (int i = indiceSegundaCarta; i < indiceValorCarta; i++) {
                char cartaActual = ordenCartas.charAt(i);
                if (suited) {
                    rangos.add(valorCarta + String.valueOf(cartaActual) + "s");
                }
                if (offSuited || !suited) {
                    rangos.add(valorCarta + String.valueOf(cartaActual) + "o");
                }
            }
        }
    }

    private void agregarRangoIntervalo(String parte) {
        String[] limites = parte.split("-");
        String limiteInferior = limites[0].trim();
        String limiteSuperior = limites[1].trim();
        boolean suited = limiteInferior.endsWith("s") && limiteSuperior.endsWith("s");
        boolean offSuited = limiteInferior.endsWith("o") && limiteSuperior.endsWith("o");

        char cartaInferiorAlta = limiteInferior.charAt(0);
        char cartaInferiorBaja = limiteInferior.charAt(1);
        char cartaSuperiorAlta = limiteSuperior.charAt(0);
        char cartaSuperiorBaja = limiteSuperior.charAt(1);

        int indiceInferiorAlto = ordenCartas.indexOf(cartaInferiorAlta);
        int indiceInferiorBajo = ordenCartas.indexOf(cartaInferiorBaja);
        int indiceSuperiorAlto = ordenCartas.indexOf(cartaSuperiorAlta);
        int indiceSuperiorBajo = ordenCartas.indexOf(cartaSuperiorBaja);

        // Intercambiar si el límite superior es menor que el límite inferior
        if (indiceSuperiorAlto < indiceInferiorAlto || 
            (indiceSuperiorAlto == indiceInferiorAlto && indiceSuperiorBajo < indiceInferiorBajo)) {
            char tempAlta = cartaInferiorAlta;
            char tempBaja = cartaInferiorBaja;
            cartaInferiorAlta = cartaSuperiorAlta;
            cartaInferiorBaja = cartaSuperiorBaja;
            cartaSuperiorAlta = tempAlta;
            cartaSuperiorBaja = tempBaja;

            indiceInferiorAlto = ordenCartas.indexOf(cartaInferiorAlta);
            indiceInferiorBajo = ordenCartas.indexOf(cartaInferiorBaja);
            indiceSuperiorAlto = ordenCartas.indexOf(cartaSuperiorAlta);
            indiceSuperiorBajo = ordenCartas.indexOf(cartaSuperiorBaja);
        }

        for (int i = indiceInferiorAlto; i <= indiceSuperiorAlto; i++) {
            char cartaAlta = ordenCartas.charAt(i);
            
            int inicioBajo = (i == indiceInferiorAlto) ? indiceInferiorBajo : 0;
            int finBajo = (i == indiceSuperiorAlto) ? indiceSuperiorBajo : (ordenCartas.length() - 1);

            for (int j = inicioBajo; j <= finBajo; j++) {
                char cartaBaja = ordenCartas.charAt(j);
                if (cartaAlta != cartaBaja) {
                    if (suited) {
                        rangos.add(cartaAlta + String.valueOf(cartaBaja) + "s");
                    } else if (offSuited) {
                        rangos.add(cartaAlta + String.valueOf(cartaBaja) + "o");
                    } else {
                        rangos.add(cartaAlta + String.valueOf(cartaBaja));
                    }
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

    private int getRankValue(char rank) {
        return ordenCartas.indexOf(rank);
    }

    public void actualizarPorcentaje(double nuevoPorcentaje) {
        this.porcentaje = nuevoPorcentaje;
    }
}