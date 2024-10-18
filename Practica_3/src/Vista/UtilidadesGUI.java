package Vista;

public class UtilidadesGUI {

    public static String getCartaPath(String carta) {
        String valor = carta.substring(0, 1);
        String base = "src/Vista/Imagenes/Cartas/";

        switch (valor) {
            case "A":
                base += "ace_of_";
                break;
            case "K":
                base += "king_of_";
                break;
            case "Q":
                base += "queen_of_";
                break;
            case "J":
                base += "jack_of_";
                break;
            case "T":
                base += "10_of_";
                break;
            default:
                base += valor + "_of_";
                break;
        }

        base += getNombrePalo(carta.charAt(1)) + ".png";
        return base;
    }

    private static String getNombrePalo(char palo) {
        switch (palo) {
            case 'h':
                return "hearts";
            case 'c':
                return "clubs";
            case 'd':
                return "diamonds";
            case 's':
                return "spades";
            default:
                return null;
        }
    }
}
