package gui.sesion;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import Unzu.Usuario.GestionUsuarios;
import Unzu.Usuario.Status;
import Unzu.Usuario.Usuario;
import gui.componentes.MensajeCarga;

@SuppressWarnings("serial")
public class VentanaRegistroSesion extends VentanaSesion{

	private JPanel panelConfirmarContraseña;
	private JPanel panelConfirmarContraseña2;

	private JLabel labelConfirmarContraseña;

	private JPasswordField inputConfirmarContraseña;



	/** Crea una nueva ventana para registrar un usuario
	 * @param ventanaAnterior referencia de la ventana desde la que se crea esta ventana
	 */
	public VentanaRegistroSesion() {
		super(8);

		this.setTitle("Registrarme");

		panelConfirmarContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelConfirmarContraseña2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelConfirmarContraseña = new JLabel("Confirmar Contraseña:");
		inputConfirmarContraseña = new JPasswordField(numColumnas);

		panelConfirmarContraseña.add(labelConfirmarContraseña);
		panelConfirmarContraseña2.add(inputConfirmarContraseña);

		inputConfirmarContraseña.addKeyListener(cierraConEsc);

		panelDatos.add(panelConfirmarContraseña);
		panelDatos.add(panelConfirmarContraseña2);
		panelDatos.add(panelMensaje);
		panelDatos.add(panelGuardarDispositivo);

		botonAceptar.setText("Registrarme");

		// Color de los paneles
		colorPaneles(fondo);

		// Color de los componentes
		colorComponentes();

		inputContraseña.addActionListener(e -> {
			if (inputContraseña.getPassword() != null) {
				inputConfirmarContraseña.requestFocus();
			}

		});

		inputConfirmarContraseña.addActionListener(e -> {
			if (inputContraseña.getPassword() != null) {
				botonAceptar.requestFocus();
			}
		});
	}

	@Override
	protected void siguienteVentana() {
		usuario = new Usuario(inputUsuario.getText(), new String(inputConfirmarContraseña.getPassword()), null);
		if(GestionUsuarios.existeUsuario(usuario) == Status.EXISTENTE) {
			resetTextos(true);
			labelMensaje.setText("El usuario que has introducido ya está registrado");
		}else if(!String.valueOf(inputContraseña.getPassword()).equals(String.valueOf(inputConfirmarContraseña.getPassword()))) {
			resetTextos(false);
			labelMensaje.setText("Las contraseñas introducidas deben coincidir");
			inputContraseña.requestFocus();
		}else {
			mensajeDeCarga = new MensajeCarga("Registrando nuevo usuario", "Usuario creado", this, botonAceptar);
			mensajeDeCarga.start();
			if(GestionUsuarios.addUsuario(usuario)) {
				if(guardarDispositivo.isSelected()) {
					if(GestionUsuarios.recordarUsuario(usuario)) {
						labelMensaje.setText("No ha sido posible recordar el usuario en este dispositivo");
					}
				}
				mensajeDeCarga.interrupt();
			}else{
				mensajeDeCarga.hasError();
				mensajeDeCarga.interrupt();
			}
		}
	}	

	@Override
	protected void colorPaneles(Color color) {
		super.colorPaneles(color);
		panelConfirmarContraseña.setBackground(color);
		panelConfirmarContraseña2.setBackground(color);
	}

	@Override
	protected void colorComponentes() {
		super.colorComponentes();
		labelConfirmarContraseña.setForeground(Color.WHITE);
		inputConfirmarContraseña.setForeground(Color.WHITE);
		inputConfirmarContraseña.setBackground(Color.BLACK);
	}

	@Override
	protected boolean condicionesAceptar() throws NullPointerException {
		return super.condicionesAceptar() && inputConfirmarContraseña.getPassword().length > 0;
	}

	@Override
	protected boolean condicionesBorrarMensaje() throws NullPointerException {
		return ((inputUsuario.getText().length() > 0 || inputContraseña.getPassword().length > 0 || inputConfirmarContraseña.getPassword().length > 0) && borrar) || inputContraseña.getPassword().length > 0 || inputConfirmarContraseña.getPassword().length > 0;
	}

	@Override
	public void resetTextos(boolean resetAll) {
		super.resetTextos(resetAll);
		inputConfirmarContraseña.setText(null);		
	}
}
