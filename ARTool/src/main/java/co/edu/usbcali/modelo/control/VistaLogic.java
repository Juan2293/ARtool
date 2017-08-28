package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import co.edu.usbcali.dataaccess.dao.IVistaDAO;
import co.edu.usbcali.dataaccess.dao.VistaDAO;
import co.edu.usbcali.modelo.Vista;


@Scope("singleton")
@Service("VistasLogic")
public class VistaLogic implements IVistaLogic {
	
	@Autowired
	private IVistaDAO vistasDao;

	@Override
	public String guardarVistas(String url, String user, String password, String db_id) {

		return vistasDao.guardarVistas(url, user, password, db_id);
	}

	@Override
	public ArrayList<Vista> consultarVistas(String db_id) {
		return vistasDao.consultarVistas(db_id);
	}

	@Override
	public Long obtenerNumeroVistas(String db_id) {
		return vistasDao.obtenerNumeroVistas(db_id);
	}
	
	

}
