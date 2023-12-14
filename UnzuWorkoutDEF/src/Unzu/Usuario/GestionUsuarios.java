package Unzu.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class GestionUsuarios {

	private static final String FICHEROUSUARIOS = "archivos/usuarios.dat";
	private static final String FICHEROTOKEN = "archivos/token.dat";

	private static Map<String, Usuario> usuarios = null;

	public static Status comprobarUsuario(Usuario usuario) {
		if(!cargarUsuarios()) {
			return Status.ERROR;
		}
		if(usuarios.containsKey(usuario.getEmail())) {
			if(usuarios.get(usuario.getEmail()).getContraseña().equals(usuario.getContraseña())) {
				return Status.CORRECTO;
			}
			return Status.INCORRECTO;
		}
		return Status.INEXISTENTE;
	}

	public static boolean recordarUsuario(Usuario usuario) {
		return guardarToken(new Token(usuario));
	}

	public static boolean addUsuario(Usuario usuario) {
		if(!cargarUsuarios()) {
			return false;
		}
		usuarios.put(usuario.getEmail(), usuario);
		return guardarUsuarios();
	}

	public static Status existeUsuario(Usuario usuario) {
		if(!cargarUsuarios()) {
			return Status.ERROR;
		}
		if(usuarios.containsKey(usuario.getEmail())) {
			return Status.EXISTENTE;
		}
		return Status.INEXISTENTE;
	}
	
	public static Usuario getUsuario(Usuario u) {
		return usuarios.get(u.getEmail());
	}

	@SuppressWarnings("unchecked")
	private static boolean cargarUsuarios() {
		if(usuarios == null) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FICHEROUSUARIOS)))){
				usuarios = (Map<String, Usuario>) ois.readObject();
			}catch(IOException e) {
				usuarios = new HashMap<>();
				try {
					new File(FICHEROUSUARIOS).createNewFile();
				} catch (IOException e1) {
				}
			}catch(ClassNotFoundException e) {
				return false;
			}
		}
		return true;
	}


	private static boolean guardarUsuarios() {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FICHEROUSUARIOS)))) {
			oos.writeObject(usuarios);
		}catch(FileNotFoundException e) {
			return false;
		}catch(IOException e) {
			return false;
		}
		return true;
	}

	private static boolean guardarToken(Token token) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(FICHEROTOKEN)))) {
			oos.writeObject(token);
			token.getUsuario().setToken(token);
			return true;
		}catch(FileNotFoundException e) {
			return false;
		}catch(IOException e) {
			return false;
		}
	}

	public static Token getToken() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(FICHEROTOKEN)))){
			Token t = (Token) ois.readObject();
			cargarUsuarios();
			t.setUsuario(usuarios.get(t.getUsuario().getEmail()));
			return t;
		}catch(IOException e) {
			return null;
		}catch(ClassNotFoundException e) {
			return null;
		}
	}
}
