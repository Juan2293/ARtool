package co.edu.usbcali.modelo.control;

import co.edu.usbcali.dataaccess.dto.UsuarioDTO;
import co.edu.usbcali.modelo.Usuario;

public interface IUsuarioLogic {

	
	public Usuario consultarUsuario(String login, String password) throws Exception;
	public String guardarUsuario(UsuarioDTO usuarioDTO) throws Exception;
	public Usuario consultarUsuarioLogin(String login)  throws Exception;
	public Usuario consultarUsuarioCorreo(String correo)  throws Exception;
}
