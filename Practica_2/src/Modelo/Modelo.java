package Modelo;

public class Modelo {
    public boolean[][] convertirRangoAGrafico(String rangoTextual) {
        // Ejemplo básico de conversión de texto a un gráfico
        boolean[][] rangoGrafico = new boolean[13][13];
        // Lógica para procesar el rango textual y convertirlo en gráfico
        // Aquí debes implementar el análisis de los rangos (AA, ATs-A8s, etc.)
        return rangoGrafico;
    }

    public String convertirGraficoATexto(boolean[][] rangoGrafico) {
        // Convertir el gráfico de vuelta a texto (AA, JJ, etc.)
        StringBuilder sb = new StringBuilder();
        // Lógica para recorrer el gráfico y convertirlo en texto
        return sb.toString();
    }

	public void setRangoTexto(String rangoTexto) {
		// TODO Auto-generated method stub
		
	}

	public boolean[][] getRangoGrafico() {
		// TODO Auto-generated method stub
		return null;
	}
}

