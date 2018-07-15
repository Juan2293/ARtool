package co.edu.usbcali.modelo.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import co.edu.usbcali.dataaccess.dao.ITablaDAO;
import co.edu.usbcali.dataaccess.dao.IUsuarioDAO;
import co.edu.usbcali.dataaccess.dto.UsuarioDTO;
import co.edu.usbcali.modelo.Usuario;
import co.edu.usbcali.presentation.businessDelegate.BusinessDelegatorView;
import co.edu.usbcali.utilities.Utilidades;

@Scope("singleton")
@Service("UsuarioLogic")
public class UsuarioLogic implements IUsuarioLogic {


	@Autowired
	private IUsuarioDAO usuarioDAO;


	@Override
	public Usuario consultarUsuario(String login, String password) throws Exception {

		try {
			
			return usuarioDAO.consultarUsuario(login, password);

		} catch (Exception e) {

			throw e;
		} finally {

		}

	}
	@Override
	public String guardarUsuario(UsuarioDTO usuarioDTO) throws Exception {

		try {
			if (usuarioDTO.getLogin()==null || usuarioDTO.getLogin().equals("")) {
				throw new Exception("Debe ingresar un usuario");
			}
			if (usuarioDTO.getPassword()==null || usuarioDTO.getPassword().equals("")) {
				throw new Exception("Debe ingresar un password");
			}
			if (usuarioDTO.getPassword_con()==null || usuarioDTO.getPassword_con().equals("")) {
				throw new Exception("Escriba nuevamente el password");
			}
			if (!usuarioDTO.getPassword().equals(usuarioDTO.getPassword_con())) {
				throw new Exception("Los password deben coincidir");
			}
			if (usuarioDTO.getCorreo()==null || usuarioDTO.getCorreo().equals("")) {
				throw new Exception("Debe ingresar un correo");
			}
			if (!Utilidades.validateEmail(usuarioDTO.getCorreo())) {
				throw new Exception("El formato del correo no es valido");
			}
			if (usuarioDTO.getNombre()==null ||usuarioDTO.getNombre().equals("") ) {
				throw new Exception("Debe ingresar un nombre");
			}
			System.out.println("Controll de cambios");
			System.out.println("Control s");
			Usuario usuario = new Usuario();
			usuario.setCorreo(usuarioDTO.getCorreo());
			usuario.setNombre(usuarioDTO.getNombre());
			usuario.setLogin(usuarioDTO.getLogin());
			usuario.setPassword(usuarioDTO.getPassword());

			return usuarioDAO.guardarUsuario(usuario);

		} catch (Exception e) {
			throw e;
		} finally {

		}

	}
	@Override
	public Usuario consultarUsuarioLogin(String login) {
		return usuarioDAO.consultarUsuarioLogin(login);
	}
	@Override
	public Usuario consultarUsuarioCorreo(String correo) {
		return usuarioDAO.consultarUsuarioCorreo(correo);
	}

}

