package Unzu;

import com.toedter.calendar.JCalendar;

import Unzu.Usuario.Usuario;
import gui.sesion.VentanaSesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class UnzuWorkout extends JFrame {

	private Usuario usuario;
	private JCalendar calendar;
	private JTextArea activityTextArea;
	private Map<String, List<Actividad>> actividadesPorDia;

	public UnzuWorkout(Usuario usuario) {
		this.usuario = usuario;
		this.actividadesPorDia = new HashMap<>();

		// Configuración de la ventana
		setTitle("UnzuWorkout");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/Icono.jpg")));

		// Crear componentes de la interfaz
		calendar = new JCalendar();
		activityTextArea = new JTextArea();
		activityTextArea.setEditable(false);
		updateActivityTextArea();
		JScrollPane scrollPane = new JScrollPane(activityTextArea);

		// Escucha de eventos de selección de fecha en el calendario
		calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateActivityList();
			}
		});

		// Configuración del panel principal
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(scrollPane, BorderLayout.EAST);
		mainPanel.add(calendar, BorderLayout.CENTER);

		// Configuración del contenedor principal
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(mainPanel, BorderLayout.CENTER);

		// Cargar actividades desde el archivo de registro
		loadActivities();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				VentanaSesion.cerrar(UnzuWorkout.this);
			}
		});

		// Mostrar la ventana
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void updateActivityList() {
		// Obtener la fecha seleccionada en el calendario
		Date selectedDate = calendar.getDate();

		// Obtener el día de la semana
		Calendar cal = Calendar.getInstance();
		cal.setTime(selectedDate);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// Obtener las actividades del día seleccionado
		String day = getDayOfWeek(dayOfWeek);
		List<Actividad> actividadesDelDia = actividadesPorDia.getOrDefault(day, Collections.emptyList());

		// Mostrar las actividades en la ventana "DayActivitiesDialog"
		if(actividadesDelDia.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Este día no cuenta con ninguna actividad");
		}else {
			DayActivitiesDialog dayActivitiesDialog = new DayActivitiesDialog(this, day, actividadesDelDia);
			dayActivitiesDialog.setVisible(true);
		}
	}

	private String getDayOfWeek(int dayOfWeek) {  
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			return "Lunes";
		case Calendar.TUESDAY:
			return "Martes";
		case Calendar.WEDNESDAY:
			return "Miércoles";
		case Calendar.THURSDAY:
			return "Jueves";
		case Calendar.FRIDAY:
			return "Viernes";
		case Calendar.SATURDAY:
			return "Sábado";
		case Calendar.SUNDAY:
			return "Domingo";
		default:
			return "";
		}
	}

	private void loadActivities() {
		try (Scanner scanner = new Scanner(new File("archivos/actividades.txt"))) {
			String line;
			String day = "";
			while (scanner.hasNextLine()) {
				line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					if (line.endsWith(":")) {
						day = line.substring(0, line.length() - 1);
						actividadesPorDia.put(day, new ArrayList<>());
					} else {
						List<Actividad> actividadesDia = actividadesPorDia.getOrDefault(day, new ArrayList<>());
						Actividad actividad = new Actividad(line, day);
						actividadesDia.add(actividad);
						actividadesPorDia.put(day, actividadesDia);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public boolean addActivityToUsuario(Actividad actividad) {
		if(!usuario.addActividad(actividad)) {
			return false;
		}
		updateActivityTextArea();
		return true;
	}

	private void updateActivityTextArea() {
		activityTextArea.setText(String.format("Actividades de %s\n", usuario.getEmail()));
		for (Entry<Integer, Set<Actividad>> entry : usuario.getActividades().entrySet()) {
			activityTextArea.append(String.format("Actividades de los %s:\n", getDayOfWeek(entry.getKey())));
			int i = 1;
			for(Actividad actividad : entry.getValue()) {
				activityTextArea.append(String.format("\t%d. %s:\n", i, actividad));
				i++;
			}
		}
	}
}
