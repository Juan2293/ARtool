package co.edu.usbcali.dataaccess.dao;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Tabla;

public interface ITablaDAO {

	public String guardarTablas(String url, String user, String password, String db_id);
	public ArrayList [][]  consultarTablas(String db_id);
	public Long obtenerNumeroTablas(String db_id);
	

}
