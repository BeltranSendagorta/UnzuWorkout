package gui.sesion;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Unzu.Usuario.GestionUsuarios;
import Unzu.Usuario.Status;
import Unzu.Usuario.Usuario;
import gui.componentes.MensajeCarga;

@SuppressWarnings("serial")
public class VentanaInicioSesion extends VentanaSesion{
	
	JLabel registrarse;
	JPanel panelRegistrarse;
	
	/** Crea una nueva ventana para iniciar sesion con un usuario
	 * 
	 */
	public VentanaInicioSesion() {
		super(7);

		this.setTitle("Iniciar Sesión");

		// Color de los paneles
		colorPaneles(fondo);

		// Color de los componentes
		colorComponentes();
		
		registrarse = new JLabel("¿Aún no tienes una cuenta? Registrate clickando aquí");
		registrarse.setForeground(Color.WHITE);
		
		panelRegistrarse = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelRegistrarse.setBackground(fondo);
		panelRegistrarse.add(registrarse);
		
		panelDatos.add(panelMensaje);
		panelDatos.add(panelRegistrarse);
		panelDatos.add(panelGuardarDispositivo);

		botonAceptar.setText("Iniciar Sesión");

		inputContraseña.addActionListener(e -> {
			if (inputContraseña.getPassword() != null) {
				botonAceptar.requestFocus();
			}
		});
		
		registrarse.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				registrarse.setForeground(Color.WHITE);	
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				registrarse.setForeground(Color.GREEN);	
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(registrarse.getMousePosition() != null) {
					new VentanaRegistroSesion().iniciar();
					estaCerrada = true;
					dispose();
				}
			}
		});
	}

	@Override
	protected void siguienteVentana() {
		usuario = new Usuario(inputUsuario.getText(), new String(inputContraseña.getPassword()), null);
		Status estado = GestionUsuarios.comprobarUsuario(usuario);
		if(estado == Status.INEXISTENTE) {
			resetTextos(true);
			labelMensaje.setText("El usuario introducido no existe");
		}else if(estado == Status.INCORRECTO) {
			resetTextos(false);
			labelMensaje.setText("La contraseña introducida es incorrecta");
			inputContraseña.requestFocus();
		}else if(estado == Status.CORRECTO){
			mensajeDeCarga = new MensajeCarga("Iniciando Sesión", "Sesión iniciada", this, botonAceptar);
			mensajeDeCarga.start();
			if(guardarDispositivo.isSelected()) {
				if(!GestionUsuarios.recordarUsuario(usuario)) {
					labelMensaje.setText("No ha sido posible recordar el usuario en este dispositivo");
				}
			}
			usuario = GestionUsuarios.getUsuario(usuario);
			mensajeDeCarga.interrupt();
		}else {
			resetTextos(true);
			labelMensaje.setText("Error comprobando el usuario");
		}
	}	
}
