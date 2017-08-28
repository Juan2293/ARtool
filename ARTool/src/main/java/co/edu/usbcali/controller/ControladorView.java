package co.edu.usbcali.controller;


import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.print.DocFlavor.READER;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.ContextLifecycleScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import co.edu.usbcali.dataaccess.dto.Db_RecoveryDTO;
import co.edu.usbcali.dataaccess.dto.UsuarioDTO;
import co.edu.usbcali.modelo.Db_Recovery;
import co.edu.usbcali.modelo.Funcion;
import co.edu.usbcali.modelo.ResultadoRest;
import co.edu.usbcali.modelo.Trigger;
import co.edu.usbcali.modelo.Usuario;
import co.edu.usbcali.modelo.Vista;
import co.edu.usbcali.presentation.businessDelegate.IBusinessDelegatorView;

@ManagedBean
@ViewScoped
@Controller
@RestController
@RequestMapping("/clienteRest")
@Path("/clienteRest")
public class ControladorView {

	@Autowired
	private IBusinessDelegatorView businessDelegatorView;


	@RequestMapping(value="/getTablas",method=RequestMethod.GET)
	public ArrayList[][] getTablas() {
		String db_id = (String)RequestContextHolder.currentRequestAttributes().getAttribute( "conexion", RequestAttributes.SCOPE_SESSION );
		return businessDelegatorView.consultarTablas(db_id);
	}

	@RequestMapping(value="/getFunciones",method=RequestMethod.GET)
	public ArrayList<Funcion> getFunciones() {
		String db_id = (String)RequestContextHolder.currentRequestAttributes().getAttribute( "conexion", RequestAttributes.SCOPE_SESSION );
		return businessDelegatorView.consultarFunciones(db_id);
	}
	
	@RequestMapping(value="/getTriggers",method=RequestMethod.GET)
	public ArrayList<Trigger> getTriggers() {
		String db_id = (String)RequestContextHolder.currentRequestAttributes().getAttribute( "conexion", RequestAttributes.SCOPE_SESSION );
		return businessDelegatorView.consultarTriggers(db_id);
	}

	@RequestMapping(value="/getVistas",method=RequestMethod.GET)
	public ArrayList<Vista> getVistas() {
		String db_id = (String)RequestContextHolder.currentRequestAttributes().getAttribute( "conexion", RequestAttributes.SCOPE_SESSION );
		return businessDelegatorView.consultarVistas(db_id);
	}

	@RequestMapping(value="/getUsuario/{login}/{password}",method=RequestMethod.GET)
	public UsuarioDTO getUsuario(@PathVariable String login,@PathVariable String password) {

		Usuario usuario;
		UsuarioDTO usuarioDTO;
		try {
			usuario = businessDelegatorView.consultarUsuario(login.trim(), password.trim());
			if (usuario==null) {

				usuarioDTO = new UsuarioDTO();
				usuarioDTO.setCodigoError("10");
				usuarioDTO.setMensajeError("Los  datos con los que estás intentando ingresar no son correctos. Por favor, inténtalo de nuevo.");
				return usuarioDTO;
			}

			usuarioDTO = new UsuarioDTO();
			usuarioDTO.setLogin(usuario.getLogin().trim());
			usuarioDTO.setCorreo(usuario.getCorreo().trim());
			usuarioDTO.setNombre(usuario.getNombre().trim());
			usuarioDTO.setUsuario_id(usuario.getUsuario_id());
			usuarioDTO.setCodigoError("-1");
			usuarioDTO.setMensajeError("ok");
			RequestContextHolder.currentRequestAttributes().setAttribute( "usuario", usuarioDTO, RequestAttributes.SCOPE_SESSION );
			RequestContextHolder.currentRequestAttributes().setAttribute( "conexion", null, RequestAttributes.SCOPE_SESSION );
			

			return usuarioDTO;
		} catch (Exception e) {
			usuarioDTO = new UsuarioDTO();
			usuarioDTO.setCodigoError("50");
			usuarioDTO.setMensajeError(e.getMessage());

			return usuarioDTO;
		}

	}

	@RequestMapping(value="/guardarDb",method=RequestMethod.POST, headers = {"content-type=application/json"})
	public @ResponseBody ResultadoRest guardarDb (@RequestBody Db_RecoveryDTO dbRecoveryDTO ) throws Exception {

		try {
			UsuarioDTO usuario = (UsuarioDTO)RequestContextHolder.currentRequestAttributes().getAttribute( "usuario", RequestAttributes.SCOPE_SESSION );
			Db_Recovery db_Recovery = null;
			db_Recovery = businessDelegatorView.consultar_conexion(usuario.getUsuario_id().toString(), dbRecoveryDTO.getNombre_conexion());

			if (db_Recovery!=null) {
				ResultadoRest resultadoRest = new ResultadoRest();
				resultadoRest.setCodigoError("10");
				resultadoRest.setMensajeError("El nombre de la  conexión ya está registrado");
				return resultadoRest;
			}
			dbRecoveryDTO.setUsuario_id(usuario.getUsuario_id());
			String db_id = businessDelegatorView.guardarDB(dbRecoveryDTO);
			businessDelegatorView.guardarTablas(dbRecoveryDTO.getUrl_db(), dbRecoveryDTO.getUsuario_db(), dbRecoveryDTO.getPassword_db(), db_id);
			businessDelegatorView.guardarVistas(dbRecoveryDTO.getUrl_db(), dbRecoveryDTO.getUsuario_db(), dbRecoveryDTO.getPassword_db(), db_id);
			businessDelegatorView.guardarFunciones(dbRecoveryDTO.getUrl_db(), dbRecoveryDTO.getUsuario_db(), dbRecoveryDTO.getPassword_db(), db_id);
			businessDelegatorView.guardarTriggers(dbRecoveryDTO.getUrl_db(), dbRecoveryDTO.getUsuario_db(), dbRecoveryDTO.getPassword_db(), db_id);
			ResultadoRest resultadoRest = new ResultadoRest();
			resultadoRest.setCodigoError("-1");
			resultadoRest.setMensajeError("La información se recuperó de forma éxitosa");
			return resultadoRest;


		} catch (Exception e) {
			ResultadoRest resultadoRest = new ResultadoRest();
			resultadoRest.setCodigoError("50");
			resultadoRest.setMensajeError(e.getMessage());
			return resultadoRest;
		}

	}
	@RequestMapping(value="/getConexiones",method=RequestMethod.GET)
	public ArrayList<Db_Recovery> getConexiones () {
		UsuarioDTO usuario = (UsuarioDTO)RequestContextHolder.currentRequestAttributes().getAttribute( "usuario", RequestAttributes.SCOPE_SESSION );
		return businessDelegatorView.obtenerConexiones(usuario.getUsuario_id().toString());
	}

	@RequestMapping(value="/obtenerInfoBd/{db_id}",method=RequestMethod.GET)
	public Db_RecoveryDTO obtenerInfoBd (@PathVariable String db_id) {
		
	
		Db_RecoveryDTO db_RecoveryDTO = new Db_RecoveryDTO();
		
		Long numero_funciones= businessDelegatorView.obtenerNumeroFunciones(db_id);
		Long numero_vistas = businessDelegatorView.obtenerNumeroVistas(db_id);
		Long numero_tablas = businessDelegatorView.obtenerNumeroTablas(db_id);
		Long numero_triggers = businessDelegatorView.obtenerNumeroTriggers(db_id);
		
		db_RecoveryDTO.setNumero_funciones(numero_funciones);
		db_RecoveryDTO.setNumero_vistas(numero_vistas);
		db_RecoveryDTO.setNumero_tablas(numero_tablas);
		db_RecoveryDTO.setNumero_triggers(numero_triggers);
		
		return db_RecoveryDTO;
	}

	@RequestMapping(value="/guardarUsuario",method=RequestMethod.POST,  headers = {"content-type=application/json"})
	public @ResponseBody ResultadoRest guardarUsuario(@RequestBody UsuarioDTO usuarioDTO) {

		try {

			if (businessDelegatorView.consultarUsuarioLogin(usuarioDTO.getLogin())!=null) {
				ResultadoRest resultadoRest = new ResultadoRest();
				resultadoRest.setCodigoError("10");
				resultadoRest.setMensajeError("El usuario ya está registrado");
				return resultadoRest;
			}
			if (businessDelegatorView.consultarUsuarioCorreo(usuarioDTO.getCorreo())!=null) {
				ResultadoRest resultadoRest = new ResultadoRest();
				resultadoRest.setCodigoError("10");
				resultadoRest.setMensajeError("El correo que ingresó está registrado");
				return resultadoRest;
			}			

			businessDelegatorView.guardarUsuario(usuarioDTO);
			ResultadoRest resultadoRest = new ResultadoRest();
			resultadoRest.setCodigoError("-1");
			resultadoRest.setMensajeError("El usuario fue creado con éxito");
			return resultadoRest;

		} catch (Exception e) {
			ResultadoRest resultadoRest = new ResultadoRest();
			resultadoRest.setCodigoError("50");
			resultadoRest.setMensajeError(e.getMessage());
			return resultadoRest;
		}

	}

	@RequestMapping(value="/seleccionarConexion",method=RequestMethod.POST, headers = {"content-type=application/json"})
	public @ResponseBody ResultadoRest seleccionarConexion (@RequestBody Db_RecoveryDTO dbRecoveryDTO ) throws Exception {

		try {
			if (dbRecoveryDTO.getDb_id()==null) {
				ResultadoRest resultadoRest = new ResultadoRest();
				resultadoRest.setCodigoError("10");
				resultadoRest.setMensajeError("El id de la conexión es null");
			}
			RequestContextHolder.currentRequestAttributes().setAttribute( "conexion",dbRecoveryDTO.getDb_id(), RequestAttributes.SCOPE_SESSION );
			ResultadoRest resultadoRest = new ResultadoRest();
			resultadoRest.setCodigoError("-1");
			resultadoRest.setMensajeError("Has seleccionado una  nueva conexión");
			return resultadoRest;


		} catch (Exception e) {
			ResultadoRest resultadoRest = new ResultadoRest();
			resultadoRest.setCodigoError("50");
			resultadoRest.setMensajeError(e.getMessage());
			return resultadoRest;
		}

	}
	
	@RequestMapping(value="/obtenerInfoUsuario",method=RequestMethod.GET)
	public UsuarioDTO obtenerInfoUsuario() {
		UsuarioDTO usuario = (UsuarioDTO)RequestContextHolder.currentRequestAttributes().getAttribute( "usuario", RequestAttributes.SCOPE_SESSION );
			
		return usuario;
	}
	
	@RequestMapping(value="/obtenerInfoUsuDb",method=RequestMethod.GET)
	public Db_RecoveryDTO obtenerInfoUsuDb() {
		
		Db_RecoveryDTO db_RecoveryDTO=null;
		try {
			
	
		String db_id = (String)RequestContextHolder.currentRequestAttributes().getAttribute( "conexion", RequestAttributes.SCOPE_SESSION );
		if (!db_id.equals("") || db_id!=null ) {
			Db_Recovery db_Recovery = businessDelegatorView.consultar_db_por_id(db_id);
			db_RecoveryDTO = new Db_RecoveryDTO();
			db_RecoveryDTO.setFecha(db_Recovery.getFecha());
			db_RecoveryDTO.setNombre(db_Recovery.getNombre());
			db_RecoveryDTO.setNombre_conexion(db_Recovery.getNombre_conexion());
			
			return db_RecoveryDTO;
		
		}

		} catch (Exception e) {
		
		
		}
		
		return db_RecoveryDTO;
	}
	
	
}
