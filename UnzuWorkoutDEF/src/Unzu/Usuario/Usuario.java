package Unzu.Usuario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Unzu.Actividad;

public class Usuario implements Serializable{

	private static final long serialVersionUID = 2317813487930963755L;
	
	private String email;						//nombre del usuario
    private String contraseña;					//contraseña del usuario
    private Token token;						//token asociado al usuario
    private Map<Integer, Set<Actividad>> actividades;

    /**Crea un nuevo usuario con el nombre, contraseña, tiempoJugado, TreeSet de puntuaciones y token especificado
     * @param email       email del usuario
     * @param contraseña contraseña del usuario
     * @param token       token asociado al usuario
     * @param actividades lista de actividades a las que está apuntado el usuario
     */
    public Usuario(String email, String contraseña, Token token, Map<Integer, Set<Actividad>> actividades) {
        this.email = email;
        this.contraseña = contraseña;
        this.token = token;
        this.actividades = actividades;
    }
    
    public Usuario(String email, String contraseña, Token token) {
        this(email, contraseña, token, new HashMap<>());
    }
    
    public Usuario(String email, String contraseña) {
    	this(email, contraseña, null, new HashMap<>());
    }

    /**Devuelve el email del usuario
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**Devuelve la contraseña del usuario
     * @return Contraseña del usuario
     */
    public String getContraseña() {
        return contraseña;
    }

    /**Devuelve el token asociado al usuario
     * @return token del usuario
     */
    public Token getToken() {
        return token;
    }

    /**Cambia el token asociado al usuario
     * @param token nuevo token del usuario
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**Devuelve la lista de actividades a las que está apuntado el usuario
     * @return lista de actividades
     */
    public Map<Integer, Set<Actividad>> getActividades() {
        return actividades;
    }
    
    public boolean addActividad(Actividad actividad) {
        actividades.putIfAbsent(actividad.getNumDia(), new HashSet<Actividad>());
        if(actividades.get(actividad.getNumDia()).add(actividad)) {
        	return GestionUsuarios.addUsuario(this);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return email;
    }
}
