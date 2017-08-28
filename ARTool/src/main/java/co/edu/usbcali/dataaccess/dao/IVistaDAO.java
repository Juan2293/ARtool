package co.edu.usbcali.dataaccess.dao;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Funcion;
import co.edu.usbcali.modelo.Tabla;
import co.edu.usbcali.modelo.Vista;

public interface IVistaDAO {

	

	public String guardarVistas(String url, String user, String password, String db_id);
	public ArrayList<Vista> consultarVistas(String db_id);
	public Long obtenerNumeroVistas(String db_id);
}
