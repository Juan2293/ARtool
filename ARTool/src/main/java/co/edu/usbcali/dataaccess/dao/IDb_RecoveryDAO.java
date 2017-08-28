package co.edu.usbcali.dataaccess.dao;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Db_Recovery;

public interface IDb_RecoveryDAO {
	public String guardarDB(Db_Recovery db_Recovery);
	public ArrayList<Db_Recovery> obtenerConexiones(String usuario_id);
	public Db_Recovery consultar_conexion(String usuario_id, String conexion);
	public Db_Recovery consultar_db_por_id(String db_id);
}
