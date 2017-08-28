package co.edu.usbcali.modelo.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import co.edu.usbcali.dataaccess.dao.IDb_RecoveryDAO;
import co.edu.usbcali.dataaccess.dto.Db_RecoveryDTO;
import co.edu.usbcali.modelo.Db_Recovery;

@Scope("singleton")
@Service("Db_RecoveryLogic")
public class Db_RecoveryLogic implements IDb_RecoveryLogic{

	@Autowired
	private IDb_RecoveryDAO recoveryDAO;


	@Override
	public String guardarDB(Db_RecoveryDTO db_RecoveryDTO) throws Exception {
		try {
			if (db_RecoveryDTO.getNombre_conexion()==null || db_RecoveryDTO.getNombre_conexion().equals("")) {
				throw new Exception("Debe ingresar el nombre de la conexion");
			}
			if (db_RecoveryDTO.getUrl_db()==null || db_RecoveryDTO.getUrl_db().equals("")) {
				throw new Exception("Debe ingresar la url de conexión a la base de datos");
			}
			if (db_RecoveryDTO.getUsuario_db()==null ||db_RecoveryDTO.getUsuario_db().equals("")) {
				throw new Exception("Debe ingresar el usuario de la base de datos");
			}
			if (db_RecoveryDTO.getPassword_db()==null ||db_RecoveryDTO.getPassword_db().equals("")) {
				throw new Exception("Debe ingresar el password de la base de datos");
			}
			if (!probar_conexion(db_RecoveryDTO.getUrl_db(), db_RecoveryDTO.getUsuario_db(), db_RecoveryDTO.getPassword_db())) {
				throw new Exception("Compruebe los datos para la conexion a la base de datos");
			}

			String nombre_bd[] = db_RecoveryDTO.getUrl_db().split("/");
		

			Db_Recovery db_Recovery = new Db_Recovery();
			db_Recovery.setNombre(nombre_bd[nombre_bd.length-1]);
			db_Recovery.setNombre_conexion(db_RecoveryDTO.getNombre_conexion());
			db_Recovery.setUsuario_id(db_RecoveryDTO.getUsuario_id());



			return recoveryDAO.guardarDB(db_Recovery);
		} catch (Exception e) {
			throw e;
		}finally {

		}

	}
	
	public static boolean probar_conexion(String url, String usuario, String password){
		
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = DriverManager.getConnection(url, usuario , password);
			Statement statement = connection.createStatement();
			
			statement.close();
			connection.close();
			
			return true;
		} catch (Exception e) {
			
			return false;
		}
		
		
		
		
	}
	
	
	@Override
	public ArrayList<Db_Recovery> obtenerConexiones(String usuario_id) {

		return recoveryDAO.obtenerConexiones(usuario_id);
	}
	@Override
	public Db_Recovery consultar_conexion(String usuario_id, String conexion) {

		return recoveryDAO.consultar_conexion(usuario_id, conexion);
	}

	@Override
	public Db_Recovery consultar_db_por_id(String db_id) {
		return recoveryDAO.consultar_db_por_id(db_id);
	}

}
