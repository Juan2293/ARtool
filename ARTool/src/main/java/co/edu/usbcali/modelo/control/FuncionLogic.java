package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import co.edu.usbcali.dataaccess.dao.IFuncionDAO;
import co.edu.usbcali.modelo.Funcion;

@Scope("singleton")
@Service("FuncionLogic")
public class FuncionLogic implements IFuncionLogic{
	
	@Autowired
	private IFuncionDAO funcionDAO;

	@Override
	public String guardarFunciones(String connectString, String usuario, String password, String db_id) {
		return funcionDAO.guardarFunciones(connectString, usuario, password,db_id);
	}

	@Override
	public ArrayList<Funcion> consultarFunciones(String db_id) {
		return funcionDAO.consultarFunciones(db_id);
	}

	@Override
	public Long obtenerNumeroFunciones(String db_id) {
		return funcionDAO.obtenerNumeroFunciones(db_id);
	}
	
	

}
