package co.edu.usbcali.modelo.control;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import co.edu.usbcali.dataaccess.dao.ITriggerDAO;
import co.edu.usbcali.modelo.Trigger;

@Scope("singleton")
@Service("TriggerLogic")
public class TriggerLogic implements ITriggerLogic {
	
	@Autowired
	private ITriggerDAO triggerDAO;

	@Override
	public String guardarTriggers(String url, String usuario, String password, String db_id) {
		return triggerDAO.guardarTriggers(url, usuario, password, db_id);
	}

	@Override
	public ArrayList<Trigger> consultarTriggers(String db_id) {
		return triggerDAO.consultarTriggers(db_id);
	}

	@Override
	public Long obtenerNumeroTriggers(String db_id) {
		return triggerDAO.obtenerNumeroTriggers(db_id);
	}
	
	

}
