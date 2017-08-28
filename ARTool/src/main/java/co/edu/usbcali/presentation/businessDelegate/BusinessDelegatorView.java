package co.edu.usbcali.presentation.businessDelegate;

import co.edu.usbcali.dataaccess.dto.Db_RecoveryDTO;
import co.edu.usbcali.dataaccess.dto.UsuarioDTO;
import co.edu.usbcali.modelo.Db_Recovery;
import co.edu.usbcali.modelo.Funcion;
import co.edu.usbcali.modelo.Trigger;
import co.edu.usbcali.modelo.Usuario;
import co.edu.usbcali.modelo.Vista;
import co.edu.usbcali.modelo.control.IDb_RecoveryLogic;
import co.edu.usbcali.modelo.control.IFuncionLogic;
import co.edu.usbcali.modelo.control.ITablaLogic;
import co.edu.usbcali.modelo.control.ITriggerLogic;
import co.edu.usbcali.modelo.control.IUsuarioLogic;
import co.edu.usbcali.modelo.control.IVistaLogic;
import co.edu.usbcali.modelo.control.TriggerLogic;
import co.edu.usbcali.presentation.businessDelegate.IBusinessDelegatorView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


@Scope("singleton")
@Service("BusinessDelegatorView")
public class BusinessDelegatorView implements IBusinessDelegatorView {

	@Autowired
	private IVistaLogic vistaLogic;

	@Autowired
	private ITablaLogic tablaLogic;

	@Autowired
	private IFuncionLogic funcionLogic;
	
	@Autowired
	private ITriggerLogic triggerLogic;

	@Autowired
	private IUsuarioLogic usuarioLogic;
	@Autowired
	private IDb_RecoveryLogic recoveryLogic;



	@Override
	public String guardarVistas(String url, String user, String password, String db_id)  {
		return vistaLogic.guardarVistas(url, user, password, db_id);
	}

	@Override
	public String guardarFunciones(String connectString, String usuario, String password,String db_id) {
		return funcionLogic.guardarFunciones(connectString, usuario, password, db_id);
	}



	@Override
	public Usuario consultarUsuario(String login, String password) throws Exception {
		return usuarioLogic.consultarUsuario(login, password);
	}



	@Override
	public String guardarDB(Db_RecoveryDTO db_RecoveryDTO)  throws Exception {
		return recoveryLogic.guardarDB(db_RecoveryDTO);
	}



	@Override
	public ArrayList<Db_Recovery> obtenerConexiones(String usuario_id) {
		return recoveryLogic.obtenerConexiones(usuario_id);
	}



	@Override
	public String guardarTablas(String url, String user, String password, String db_id) {
		return tablaLogic.guardarTablas(url, user, password, db_id);
	}


	@Override
	public ArrayList[][] consultarTablas(String db_id) {
		return tablaLogic.consultarTablas(db_id);
	}


	@Override
	public ArrayList<Funcion> consultarFunciones(String db_id) {
		return funcionLogic.consultarFunciones(db_id);
	}


	@Override
	public ArrayList<Vista> consultarVistas(String db_id) {

		return vistaLogic.consultarVistas(db_id);
	}

	@Override
	public String guardarUsuario(UsuarioDTO usuarioDTO) throws Exception{
		return usuarioLogic.guardarUsuario(usuarioDTO);
	}

	@Override
	public Usuario consultarUsuarioCorreo(String correo) throws Exception{
		return usuarioLogic.consultarUsuarioCorreo(correo);
	}

	@Override
	public Usuario consultarUsuarioLogin(String login)  throws Exception{
		return usuarioLogic.consultarUsuarioLogin(login);
	}

	@Override
	public Db_Recovery consultar_conexion(String usuario_id, String conexion) {

		return recoveryLogic.consultar_conexion(usuario_id, conexion);
	}

	@Override
	public Long obtenerNumeroFunciones(String db_id) {
		return funcionLogic.obtenerNumeroFunciones(db_id);
	}

	@Override
	public Long obtenerNumeroVistas(String db_id) {
		return vistaLogic.obtenerNumeroVistas(db_id);
	}

	@Override
	public Db_Recovery consultar_db_por_id(String db_id) {
		return recoveryLogic.consultar_db_por_id(db_id);
	}

	@Override
	public Long obtenerNumeroTablas(String db_id) {
		
		return tablaLogic.obtenerNumeroTablas(db_id);
	}

	@Override
	public String guardarTriggers(String connectString, String usuario, String password, String db_id) {
		return triggerLogic.guardarTriggers(connectString, usuario, password, db_id);
	}

	@Override
	public ArrayList<Trigger> consultarTriggers(String db_id) {
		return triggerLogic.consultarTriggers(db_id);
	}

	@Override
	public Long obtenerNumeroTriggers(String db_id) {
		return triggerLogic.obtenerNumeroTriggers(db_id);
	}








}