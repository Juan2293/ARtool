package co.edu.usbcali.dataaccess.dao;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Trigger;

public interface ITriggerDAO {

	
	public String guardarTriggers (String url, String usuario, String password, String db_id);
	public ArrayList<Trigger> consultarTriggers(String db_id);
	public Long obtenerNumeroTriggers(String db_id);

}
