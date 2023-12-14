package gui.sesion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Unzu.UnzuWorkout;
import Unzu.Usuario.Usuario;
import gui.componentes.MensajeCarga;
import gui.componentes.MiBoton;

@SuppressWarnings("serial")
public abstract class VentanaSesion extends JFrame{

	//Atributos estáticos de la ventana
	private static int anchuraVentana = 400;
	private static int alturaVentana = 700;
	protected static Color fondo = new Color(35, 39, 42);
	protected static int numColumnas = 30;

	//Contenedores
	private JPanel panelSuperior;
	private JPanel panelCentral;
	protected JPanel panelDatos;
	private JPanel panelInferior;

	private JPanel panelUsuario;
	private JPanel panelInputUsuario;
	private JPanel panelContraseña;
	private JPanel panelInputContraseña;
	protected JPanel panelMensaje;
	private JPanel panelAceptar;
	protected JPanel panelGuardarDispositivo;

	//JLabels
	private JLabel labelUsuario;
	private JLabel labelContraseña;
	protected JLabel labelMensaje;	
	private JLabel imagenPrincipal;
	protected JCheckBox guardarDispositivo;

	//Botones
	protected MiBoton botonAceptar;

	//Campos de texto
	protected JTextField inputUsuario;
	protected JPasswordField inputContraseña;

	//Eventos que se usarán en herencia
	protected KeyListener cierraConEsc;

	//Variable que indica el estado de la ventana
	protected boolean estaCerrada;

	//Hilo ejecutado al cargar los datos del usuario
	protected MensajeCarga mensajeDeCarga;

	//Usuario que va a hacer uso de la aplicación
	protected static Usuario usuario;
	
	//Variable que indica el borrado del mensaje que sale por pantalla
	protected boolean borrar;
	
	/** Constructor de la ventana 
	 * @param numeroDeDatos Variable que indica el número de datos que va a contener el gridLayout
	 */
	public VentanaSesion(int numeroDeDatos) {
		
		//Se inicializa el label primero que todo para sacar mensajes en la ventana en caso de error
		labelMensaje = new JLabel();
		
		// Inicialización de la ventana
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(anchuraVentana, alturaVentana);
		this.estaCerrada = false;
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/Icono.jpg")));

		panelAceptar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panelCentral = new JPanel(new GridLayout(2, 1));
		panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));

		panelDatos = new JPanel(new GridLayout(numeroDeDatos, 1));
		imagenPrincipal = new JLabel(imagenReescalada("/Logo.jpg", 220, 220));

		panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelInputContraseña = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelMensaje = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panelGuardarDispositivo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

		labelUsuario = new JLabel("Usuario:");
		labelContraseña = new JLabel("Contraseña:");
		inputUsuario = new JTextField(numColumnas);
		inputContraseña = new JPasswordField(numColumnas);

		botonAceptar = new MiBoton(Color.WHITE, fondo.brighter(), 35, 35);


		guardarDispositivo = new JCheckBox("Guardar Dispositivo");

		// Configurar componentes
		botonAceptar.setEnabled(false);
		botonAceptar.setToolTipText("Pulsa aquí para confirmar tus datos");
		botonAceptar.setPreferredSize(new Dimension(anchuraVentana - 40, 40));

		//Añadir componentes a contenedores				
		panelUsuario.add(labelUsuario);
		panelContraseña.add(labelContraseña);
		panelInputUsuario.add(inputUsuario);
		panelInputContraseña.add(inputContraseña);
		panelAceptar.add(botonAceptar);
		panelMensaje.add(labelMensaje);
		panelGuardarDispositivo.add(guardarDispositivo);


		getContentPane().add(panelSuperior, "North");
		getContentPane().add(panelCentral, "Center");
		getContentPane().add(panelInferior, "South");

		panelDatos.add(panelUsuario);
		panelDatos.add(panelInputUsuario);
		panelDatos.add(panelContraseña);
		panelDatos.add(panelInputContraseña);

		panelCentral.add(imagenPrincipal);
		panelCentral.add(panelDatos);


		panelInferior.add(panelAceptar);

		borrar = true;
		
		//EVENTOS
		cierraConEsc = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cerrar(VentanaSesion.this);
				}
			}
		};

		//A todos los elementos que pueden tener foco les hago que cuando se presione el escape se cierren 
		inputUsuario.addKeyListener(cierraConEsc);
		inputContraseña.addKeyListener(cierraConEsc);
		botonAceptar.addKeyListener(cierraConEsc);
		guardarDispositivo.addKeyListener(cierraConEsc);

		inputUsuario.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputUsuario.getText() != null) {
					inputContraseña.requestFocus();
				}
			}
		});

		botonAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				siguienteVentana();
			}
		});

		botonAceptar.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					siguienteVentana();
				}
			}

		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!estaCerrada) {
					cerrar(VentanaSesion.this);
				}
			}
		});
	}

	/**Inicia las animaciones de la ventana
	 * 
	 */
	protected void iniciarAnimaciones() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!estaCerrada) {
					try {
						//Si hay algo escrito en los dos campos, se puede aceptar, sino no
						if (condicionesAceptar()) {
							botonAceptar.setEnabled(true);
						}else {
							//Por si hay algo escrito y luego se borra en algún campo
							botonAceptar.setEnabled(false);
						}
						if (condicionesBorrarMensaje()) {
							labelMensaje.setText(null);
						}
					}catch(NullPointerException e) {
						//Por si hay algo escrito y luego se borra en algún campo
						botonAceptar.setEnabled(false);
					}
				}
			}
		}).start();
	}

	/**Vuelve a la ventana anterior
	 * 
	 */
	protected void anteriorVentana(){
		estaCerrada = true;
		dispose();
	}

	/**Pasa a la siguiente ventana, recogiendo los datos introducidos
	 * 
	 */
	protected abstract void siguienteVentana();

	/**Incia el menu de inicio de sesion/registro
	 * 
	 */
	public void iniciar() {
		setLocationRelativeTo(null);
		setVisible(true);
		iniciarAnimaciones();
	}

	/** Devuelve una imagen reescalada a unas dimensiones especificas
	 * @param ruta Ruta de la imagen
	 * @param ancho	Anchura que se le quiere dar a la imagen
	 * @param alto Altura que se le quiere dar a la imagen
	 * @return Objeto ImageIcon reescalado de una manera "smooth"
	 */
	public static ImageIcon imagenReescalada(String ruta, int ancho, int alto) {  
		return new ImageIcon(new ImageIcon(VentanaSesion.class.getResource(ruta)).getImage().getScaledInstance(ancho, alto,  java.awt.Image.SCALE_SMOOTH));
	}


	/** Método que especifíca las condiciones que deben cumplirse para que se habilite el botón de aceptar
	 * @return true si las condiciones se cumplen, false si no
	 * @throws NullPointerException En caso que en algún campo no se haya escrito nada
	 */
	protected boolean condicionesAceptar() throws NullPointerException{
		return inputUsuario.getText().length() > 0 && inputContraseña.getPassword().length > 0;
	}

	/** Método que especifíca las condiciones que deben cumplirse para borrar el mensaje de información de la pantalla
	 * @return true si las condiciones se cumplen, false si no
	 * @throws NullPointerException En caso que en algún campo no se haya escrito nada
	 */
	protected boolean condicionesBorrarMensaje() throws NullPointerException {
		return (inputUsuario.getText().length() > 0 || inputContraseña.getPassword().length > 0) && borrar || botonAceptar.isEnabled();
	}
	
	/**Resetea los textos de todos los JTextFields de la ventana
	 * @param resetAll Variable que especifica si resetear todos los textos (true), o sólo la contraseña (false)
	 */
	public void resetTextos(boolean resetAll) {
		if(resetAll) {
			inputUsuario.setText(null);
			borrar = true;
		}else {
			borrar = false;
		}
		inputContraseña.setText(null);
	}

	/** Pinta todos los paneles de la ventana de cierto color
	 * @param color	Color del que se pintan los paneles
	 */
	protected void colorPaneles(Color color) {
		panelSuperior.setBackground(color);
		panelCentral.setBackground(color);
		panelInferior.setBackground(color);
		panelDatos.setBackground(color);
		imagenPrincipal.setBackground(color);
		panelUsuario.setBackground(color);
		panelContraseña.setBackground(color);
		panelInputUsuario.setBackground(color);
		panelInputContraseña.setBackground(color);
		panelAceptar.setBackground(color);
		panelMensaje.setBackground(color);
		panelGuardarDispositivo.setBackground(color);
		guardarDispositivo.setBackground(color);
	}

	/** Establece un color a los componentes de la ventana
	 * @param color	Color a establecer
	 */
	protected void colorComponentes() {
		labelMensaje.setForeground(Color.RED);
		labelUsuario.setForeground(Color.WHITE);
		labelContraseña.setForeground(Color.WHITE);
		inputUsuario.setForeground(Color.WHITE);
		inputUsuario.setBackground(Color.BLACK);
		inputContraseña.setForeground(Color.WHITE);
		inputContraseña.setBackground(Color.BLACK);
		inputContraseña.setForeground(Color.WHITE);
		botonAceptar.setBackground(Color.WHITE);
		guardarDispositivo.setForeground(Color.WHITE);
	}

	/**Cierra las ventanas relacionadas con el inicio de sesión y da comienzo a la aplicación principal
	 * 
	 */
	public void fin() {
		estaCerrada = true;
		dispose();
		new UnzuWorkout(usuario);
	}
	
	public JLabel getLabelMensaje() {
		return labelMensaje;
	}
	
	public static Color getColorFondo() {
		return fondo;
	}
	
	/**Cierra una aplicación definitivamente
	 */
	public static void cerrar(JFrame ventana) {
		ventana.setVisible(false);
		String[] s = {"Si", "No"}; //Opciones del JOptionPane
		if (JOptionPane.showOptionDialog(ventana, "¿Realmente desea salir de la aplicación?", "Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, imagenReescalada("/Icono.jpg", 30, 30), s, 0) == 0) {
			//Quiero cerrar el programa entero, no cerrar solo la ventana -> dispose() no me sirve
			System.exit(0);
		}else {
			ventana.setVisible(true);
		}
	}
}