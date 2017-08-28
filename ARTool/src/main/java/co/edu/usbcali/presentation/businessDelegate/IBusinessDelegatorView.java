package co.edu.usbcali.presentation.businessDelegate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Service;

import co.edu.usbcali.dataaccess.dto.Db_RecoveryDTO;
import co.edu.usbcali.dataaccess.dto.UsuarioDTO;
import co.edu.usbcali.modelo.Db_Recovery;
import co.edu.usbcali.modelo.Funcion;
import co.edu.usbcali.modelo.Tabla;
import co.edu.usbcali.modelo.Trigger;
import co.edu.usbcali.modelo.Usuario;
import co.edu.usbcali.modelo.Vista;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
* @author Zathura Code Generator http://zathuracode.org/
* www.zathuracode.org
*
*/
public interface IBusinessDelegatorView {
	public String guardarTablas(String url, String user, String password, String db_id);
	public String guardarVistas(String url, String user, String password, String db_id);
	public String guardarFunciones (String connectString, String usuario, String password,String db_id);
	public ArrayList [][]  consultarTablas(String db_id);
	public ArrayList<Funcion> consultarFunciones(String db_id);
	public ArrayList<Vista> consultarVistas(String db_id);
	public Long obtenerNumeroFunciones(String db_id);
	public Long obtenerNumeroVistas(String db_id);
	public Long obtenerNumeroTablas(String db_id);
	// usuario
	public Usuario consultarUsuario(String login, String password) throws Exception;
	public Usuario consultarUsuarioCorreo(String correo) throws Exception;
	public Usuario consultarUsuarioLogin(String login) throws Exception;
	public String guardarUsuario(UsuarioDTO usuarioDTO) throws Exception;
	
	//bd
	public ArrayList<Db_Recovery> obtenerConexiones(String usuario_id);
	public String guardarDB(Db_RecoveryDTO db_RecoveryDTO) throws Exception;
	public Db_Recovery consultar_conexion(String usuario_id, String conexion);
	public Db_Recovery consultar_db_por_id(String db_id);
	
	
	public String guardarTriggers (String connectString, String usuario, String password, String db_id);
	public ArrayList<Trigger> consultarTriggers(String db_id);
	public Long obtenerNumeroTriggers(String db_id);
}

