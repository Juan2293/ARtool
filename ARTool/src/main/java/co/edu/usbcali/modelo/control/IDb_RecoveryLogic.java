package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import co.edu.usbcali.dataaccess.dto.Db_RecoveryDTO;
import co.edu.usbcali.modelo.Db_Recovery;

public interface IDb_RecoveryLogic {

	public String guardarDB(Db_RecoveryDTO db_RecoveryDTO)  throws Exception;
	public ArrayList<Db_Recovery> obtenerConexiones(String usuario_id);
	public Db_Recovery consultar_conexion(String usuario_id, String conexion);
	public Db_Recovery consultar_db_por_id(String db_id);
}
