package co.edu.usbcali.dataaccess.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.sun.mail.iap.Response;

import co.edu.usbcali.modelo.Tabla;
import co.edu.usbcali.modelo.Usuario;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;




@Scope("singleton")
@Repository("TablasDAO")
public class TablaDAO implements ITablaDAO{



	//	@Override
	//	public ArrayList[][] agregarTablas(String url, String user, String password) {
	//
	//		ArrayList<String> tablas=new ArrayList<>();
	//		ArrayList<Tabla> relaciones_fk_tablas = new ArrayList<Tabla>();
	//		ArrayList [][] tablas_relaciones = new ArrayList[1][2];
	//
	//		tablas_relaciones[0][0] = tablas;
	//		tablas_relaciones[0][1] = relaciones_fk_tablas;
	//
	//		try{
	//			Class.forName("org.postgresql.Driver");
	//
	//			Connection connection = DriverManager.getConnection(url, user , password);
	//			Statement statement = connection.createStatement();
	//			ResultSet resultSet = statement.executeQuery("SELECT  tc.table_name, ccu.table_name AS foreign_table_name FROM  information_schema.table_constraints AS tc  JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name   WHERE    tc.table_name != ccu.table_name  ");
	//
	//			Tabla tabla = null;
	//			while(resultSet.next()){
	//				tabla = new Tabla();
	//				tabla.setNombreTabla(resultSet.getString(1));
	//				tabla.setNombreLlaveForanea(resultSet.getString(2));
	//				relaciones_fk_tablas.add(tabla);
	//			}
	//
	//			ResultSet resultSet2 = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';");
	//			while(resultSet2.next()){
	//				tablas.add(resultSet2.getString(1));
	//			}
	//
	//			resultSet.close();
	//			statement.close();
	//			connection.close();
	//
	//
	//		}catch (Exception e){
	//
	//
	//			return null;
	//		}
	//
	//		return tablas_relaciones;
	//
	//		
	//	}

	//	public  String guardarTablas(ArrayList<Tabla> tablas_relaciones, ArrayList<String> tablas, String db_id) {
	//
	//		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
	//		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");
	//
	//
	//		//		OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("idseq");
	//		//		seq.next();
	//		for (Tabla tabla_relacion : tablas_relaciones) {
	//			ODocument doc = new ODocument("Tabla");
	//			doc.field("nombre_foranea",tabla_relacion.getNombreLlaveForanea());
	//			doc.field("nombre_tabla",tabla_relacion.getNombreTabla());
	//			doc.field("db_id",db_id);
	//
	//
	//			doc.save();
	//		}
	//
	//
	//		for (String tabla : tablas) {
	//			ODocument doc = new ODocument("Tabla_Solita");
	//			doc.field("nombre_tabla",tabla);		
	//			doc.field("db_id",2);
	//			doc.save();
	//		}
	//
	//
	//
	//
	//		db.close();
	//
	//
	//		return "";
	//
	//	}

	@Override
	public String guardarTablas(String url, String user, String password, String db_id) {

		ArrayList<String> tablas=new ArrayList<>();
		ArrayList<Tabla> tablas_relaciones = new ArrayList<Tabla>();

		ODatabaseDocumentTx db = null;
		Connection connection = null;
		try{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user , password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT  tc.table_name, ccu.table_name AS foreign_table_name FROM  information_schema.table_constraints AS tc  JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name   WHERE    tc.table_name != ccu.table_name  ");



			Tabla tabla = null;
			while(resultSet.next()){
				tabla = new Tabla();
				tabla.setNombreTabla(resultSet.getString(1).trim());
				tabla.setNombreLlaveForanea(resultSet.getString(2).trim());
				tablas_relaciones.add(tabla);
			}

			ResultSet resultSet2 = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema!='pg_catalog' AND table_schema!='information_schema'");
			while(resultSet2.next()){
				tablas.add(resultSet2.getString(1).trim());
			}

			resultSet.close();
			statement.close();
			connection.close();
			



			//ORIENTDB

			ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
			db = oDatabaseDocumentTx.open("admin", "admin");

			for (Tabla tabla_relacion : tablas_relaciones) {
				ODocument doc = new ODocument("Tabla_Foranea");
				doc.field("nombre_foranea",tabla_relacion.getNombreLlaveForanea());
				doc.field("nombre_tabla",tabla_relacion.getNombreTabla());
				doc.field("db_id",db_id);
				doc.save();
			}

			for (String tabla_r : tablas) {
				ODocument doc = new ODocument("Tabla");
				doc.field("nombre_tabla",tabla_r);		
				doc.field("db_id",db_id);
				doc.save();
			}

			db.close();

		}catch (Exception e){

			try {
				connection.rollback();
				db.rollback();
			} catch (Exception e2) {	
			}
		}

		return "";
	}

	@Override
	public ArrayList[][] consultarTablas(String db_id) {

		ArrayList<String>tablas = new ArrayList<String>();
		ArrayList<Tabla> tablas_fk = new ArrayList<>();
		ArrayList [][] tablas_relaciones = new ArrayList[1][2];

		tablas_relaciones[0][0] = tablas;
		tablas_relaciones[0][1] = tablas_fk;


		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query par aconsultar el usuario
		String sql ="Select * from Tabla  where db_id = :db_id ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id",db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		if(!resultado.isEmpty()) {


			for (ODocument tabla_query : resultado) {

				tablas.add(tabla_query.field("nombre_tabla"));
			}

		}

		String sql_funcion ="Select * from Tabla_Foranea  where db_id = :db_id ";
		Map<String,Object> params_foranea = new HashMap<String,Object>();
		params_foranea.put("db_id", db_id);
		List<ODocument> resultado_foranea = db.command(new OSQLSynchQuery(sql_funcion)).execute(params_foranea);

		if (!resultado_foranea.isEmpty()) {
			Tabla tabla = null;
			for (ODocument foranea_query : resultado_foranea) {
				tabla = new Tabla();
				tabla.setNombreLlaveForanea(foranea_query.field("nombre_foranea"));
				tabla.setNombreTabla(foranea_query.field("nombre_tabla"));
				tablas_fk.add(tabla);
			}
		}

		db.close();



		return tablas_relaciones;
	}
	
	
	@Override
	public Long obtenerNumeroTablas(String db_id) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");


		String sql ="  select count(Distinct(nombre_tabla)) from Tabla where db_id = :db_id";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		Long  numero = resultado.get(0).field("count");
		
		db.close();
		return numero;

	}



}
