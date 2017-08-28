package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.edu.usbcali.dataaccess.dao.ITablaDAO;
import co.edu.usbcali.dataaccess.dao.TablaDAO;
import co.edu.usbcali.modelo.Tabla;



@Scope("singleton")
@Service("TablasLogic")
public class TablaLogic implements ITablaLogic{

	@Autowired
	private ITablaDAO tablaDAO;

	@Override
	public String guardarTablas(String url, String user, String password, String db_id) {
		return tablaDAO.guardarTablas(url, user, password, db_id);
	}

	@Override
	public ArrayList[][] consultarTablas(String db_id) {
		return tablaDAO.consultarTablas(db_id);
	}
	@Override
	public Long obtenerNumeroTablas(String db_id){
		
		return tablaDAO.obtenerNumeroTablas(db_id);
	}



}
