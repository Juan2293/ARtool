package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Vista;

public interface IVistaLogic {
	public String guardarVistas(String url, String user, String password, String db_id);
	public ArrayList<Vista> consultarVistas(String db_id);
	public Long obtenerNumeroVistas(String db_id);
}
