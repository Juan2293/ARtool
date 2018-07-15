package co.edu.usbcali.dataaccess.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import co.edu.usbcali.modelo.Db_Recovery;
import co.edu.usbcali.modelo.Tabla;
import co.edu.usbcali.modelo.Usuario;

@Scope("singleton")
@Repository("Db_RecoveryDAO")
public class Db_RecoveryDAO implements IDb_RecoveryDAO {

	
	@Override
	public   String guardarDB(Db_Recovery db_Recovery) {

		Long  db_id =null;
		ODatabaseDocumentTx db =null;
		ODatabaseDocumentTx oDatabaseDocumentTx =null;
		try {
		    oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
			db = oDatabaseDocumentTx.open("admin", "admin");

			OSequence	seq = null;
			try {
				seq = 
						db.getMetadata().getSequenceLibrary().createSequence("idseq", 
								OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(1L).setIncrement(1).setCacheSize(20));
			} catch (Exception e) {
				seq = db.getMetadata().getSequenceLibrary().getSequence("idseq");

			}
			
			

			// guardar
			ODocument doc = new ODocument("Db_Recovery");
			java.util.Date fecha = new Date();
			doc.field("fecha",fecha);
			doc.field("nombre",db_Recovery.getNombre());
			doc.field("nombre_conexion",db_Recovery.getNombre_conexion());
			doc.field("usuario_id",db_Recovery.getUsuario_id());
			db_id = seq.next();
			seq.next();
			
			doc.field("db_id",db_id);

			doc.save();
			db.close();
		
			
			return db_id.toString();

		} catch (Exception e) {

			db.close();

		}
		return db_id.toString();
	}

	@Override
	public ArrayList<Db_Recovery> obtenerConexiones(String usuario_id) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query para consultar el usuario
		String sql ="select * from Db_Recovery where usuario_id = :usuario_id ORDER BY db_id ASC;";

		// parametros
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("usuario_id", usuario_id);

		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);
		ArrayList<Db_Recovery> conexiones = new ArrayList<>();

		Db_Recovery db_Recovery=null;


		for (ODocument oDocument : resultado) {
			db_Recovery = new Db_Recovery();

			if (oDocument.field("db_id")!=null) {
				db_Recovery.setDb_id(oDocument.field("db_id").toString());
			}

			// se convierte  el formato de la fecha a un String 
			Date date = oDocument.field("fecha");;
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
			
			db_Recovery.setFecha(formatedDate);
			db_Recovery.setNombre(oDocument.field("nombre"));
			db_Recovery.setNombre_conexion(oDocument.field("nombre_conexion"));
			conexiones.add(db_Recovery);
		}
		
		// esta la agregue
		db.close();

		return conexiones;
	}

	@Override
	public Db_Recovery consultar_conexion(String usuario_id, String conexion) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query para consultar la conexion
		String sql ="select * from Db_Recovery where usuario_id = :usuario_id  and nombre_conexion = :nombre_conexion ";

		// parametros
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("usuario_id", usuario_id);
		params.put("nombre_conexion", conexion);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);
		Db_Recovery db_Recovery = null;

		if(!resultado.isEmpty()) {

			db_Recovery = new Db_Recovery();
			for (ODocument oDocument : resultado) {
				db_Recovery.setDb_id(oDocument.field("db_id").toString());
				db_Recovery.setFecha(oDocument.field("fecha").toString());
				db_Recovery.setNombre(oDocument.field("nombre"));
				db_Recovery.setNombre_conexion(oDocument.field("nombre_conexion"));

			}
			db.close();
			return db_Recovery;
		}

		db.close();
		return db_Recovery;
	}
	
	@Override
	public Db_Recovery consultar_db_por_id(String db_id) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query para consultar la conexion
		String sql ="select * from Db_Recovery where db_id = :db_id   ";

		// parametros
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);
		
		Db_Recovery db_Recovery = null;

		if(!resultado.isEmpty()) {

			db_Recovery = new Db_Recovery();
			for (ODocument oDocument : resultado) {
				db_Recovery.setDb_id(oDocument.field("db_id").toString());
				Date date = oDocument.field("fecha");;
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
				db_Recovery.setFecha(formatedDate);
				db_Recovery.setNombre(oDocument.field("nombre"));
				db_Recovery.setNombre_conexion(oDocument.field("nombre_conexion"));

			}
			db.close();
			return db_Recovery;
		}

		db.close();
		return db_Recovery;
	}








}
