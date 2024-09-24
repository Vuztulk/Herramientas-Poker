package App;

import Controlador.Controller;
import Modelo.Model;
import Vista.View;

public class Main {
	public static void main(String[] args) {

		Model model = new Model();
		View vista = new View();
		Controller control = new Controller(model, vista);
		
		control.procesarOrden(args[0], args[1], args[2]);

	}
}
