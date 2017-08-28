package co.edu.usbcali.dataaccess.dao;

import co.edu.usbcali.modelo.Usuario;

public interface IUsuarioDAO {

	public Usuario consultarUsuario(String login, String password) ;
	public String guardarUsuario(Usuario usuario);
	public String modificarUsuario(String login, String password);
	public Usuario consultarUsuarioCorreo(String correo);
	public Usuario consultarUsuarioLogin(String login);
}
