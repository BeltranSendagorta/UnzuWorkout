package Unzu.Usuario;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class Token implements Serializable{
	
	private static final long serialVersionUID = 5159873539793904019L;
	
	private String token;
	private ZonedDateTime caducidad;
	private Usuario usuario;

	/**Crea un nuevo token aleatorio que caduca en 3 días
	 * @param usuario usuario al que se le asigna el token
	 */
	public Token(Usuario usuario) {
		this.usuario = usuario;
		this.token = generarToken(usuario.getEmail());
		this.caducidad = ZonedDateTime.now().plusDays(3);
	}
	
	/** Crea un nuevo objeto token a partir de los parámetros especificados
	 * @param token String que representa el token asociado al usuario
	 * @param fecha	Fecha de caducidad del token
	 * @param usuario Usuario al que le corresponde el token
	 * @throws DateTimeParseException Lanza está excepción en el caso que el formato de la fecha sea incorrecto
	 */
	public Token(String token, String fecha, Usuario usuario) throws DateTimeParseException{
		this.token = token;
		this.caducidad = ZonedDateTime.parse(fecha);
		this.usuario = usuario;
	}

	/** Genera un token aleatorio
	 * @param  email del usuario al que generar el token
	 * @return el token generado en base al nombre del usuario. Si el nombre no se ajusta a la representación de cadena como se describe en toString, genera un token no vinculado al nombre de usuario
	 */
	private static String generarToken(String email) {
		try {
			return UUID.fromString(email).toString().toUpperCase();
		}catch(IllegalArgumentException e) {
			return UUID.randomUUID().toString().toUpperCase();
		}
	}

	/** Devuelve el String que representa al token aleatorio
	 * @return el token
	 */
	public String getToken() {
		return token;
	}

	/** Devuelve la fecha de caducidad del token
	 * @return	Objeto ZonedDateTime que representa esa fecha
	 */
	public ZonedDateTime getCaducidad() {
		return caducidad;
	}
	
	/** Devuelve el usuario asociado al token
	 * @return Objeto Usuario asociado al token
	 */
	public Usuario getUsuario() {
		return usuario;
	}
	
	/** Especifíca si un token ha caducado
	 * @return true si el token tiene más de 3 días, false si no
	 */
	public boolean isCaducado() {
		return caducidad.compareTo(ZonedDateTime.now()) < 0;
	}
	
	public void setUsuario(Usuario u) {
		this.usuario = u;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Token) {
			return ((Token) obj).getToken().equals(getToken());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return token.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("Token [%s] de %s con caducidad el %s", token, usuario.toString(), caducidad.toString());
	}
	
}
