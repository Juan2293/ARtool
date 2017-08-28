package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import co.edu.usbcali.modelo.Funcion;

public interface IFuncionLogic {
	public String guardarFunciones (String connectString, String usuario, String password, String db_id);
	public ArrayList<Funcion> consultarFunciones(String db_id);
	public Long obtenerNumeroFunciones (String db_id);
	
}
