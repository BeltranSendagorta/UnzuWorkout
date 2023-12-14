import Unzu.UnzuWorkout;
import Unzu.Usuario.GestionUsuarios;
import Unzu.Usuario.Token;
import gui.sesion.VentanaInicioSesion;

public class Main {

	public static void main(String[] args) {
		Token token = GestionUsuarios.getToken();
		if(token == null || token.isCaducado()) {
			new VentanaInicioSesion().iniciar();	
		}else {
			new UnzuWorkout(token.getUsuario());
		}
	}

}
