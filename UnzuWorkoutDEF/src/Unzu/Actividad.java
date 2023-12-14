package Unzu;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class Actividad implements Serializable{

	private static final long serialVersionUID = 2841035884247668742L;
	
	private String nombre;
	private String dia;

	public Actividad(String nombre, String dia) {
		this.nombre = nombre;
		this.dia = dia;
	}

	public String getDia() {
		return dia;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
	public int getNumDia() {
		switch (dia) {
		case "Lunes":
			return Calendar.MONDAY;
		case "Martes":
			return Calendar.TUESDAY;
		case "Miércoles":
			return Calendar.WEDNESDAY;
		case "Jueves":
			return Calendar.THURSDAY;
		case "Viernes":
			return Calendar.FRIDAY;
		case "Sábado":
			return Calendar.SATURDAY;
		case "Domingo":
			return Calendar.SUNDAY;
		default:
			return 8;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(dia, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Actividad) {
			return ((Actividad) obj).nombre.equals(nombre);
		}
		return false;
			
	}
}
