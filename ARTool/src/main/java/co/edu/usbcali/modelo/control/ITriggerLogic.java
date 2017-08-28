package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Trigger;

public interface ITriggerLogic  {
	
	public String guardarTriggers (String connectString, String usuario, String password, String db_id);
	public ArrayList<Trigger> consultarTriggers(String db_id);
	public Long obtenerNumeroTriggers(String db_id);
}
