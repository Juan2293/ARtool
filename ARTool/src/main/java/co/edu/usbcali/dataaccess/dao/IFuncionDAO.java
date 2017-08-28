package co.edu.usbcali.dataaccess.dao;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Funcion;

public interface IFuncionDAO {
	public String guardarFunciones (String url, String usuario, String password, String db_id);
	public Long obtenerNumeroFunciones(String db_id);
	public ArrayList<Funcion> consultarFunciones(String db_id);

	
}
