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
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import co.edu.usbcali.modelo.Trigger;

@Scope("singleton")
@Repository("TriggerDAO")
public class TriggerDAO implements ITriggerDAO {

	private ODatabaseDocumentTx oDatabaseDocumentTx;
	private ODatabaseDocumentTx db;

//	@Override
//	public String guardarTriggers(String url, String usuario, String password, String db_id) {
//
//		String  sql_vistas = " Select trigger_name, event_manipulation, event_object_table  from information_schema.triggers  where event_object_schema = 'public'; ";
//		ArrayList<Trigger> triggers = new ArrayList<>();
//		Trigger trigger;
//		Connection   connection =null;	
//
//		try {
//
//			try {
//				Class.forName("org.postgresql.Driver");
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			connection = DriverManager.getConnection(url, usuario , password);
//			Statement statement = connection.createStatement();
//			ResultSet resultSet = statement.executeQuery(sql_vistas);
//			while(resultSet.next()){
//
//				trigger = new Trigger();
//				trigger.setNombre_trigger(resultSet.getString(1).trim());
//				trigger.setNombre_relacion(resultSet.getString(2).trim());
//				trigger.setNombre_tabla(resultSet.getString(3).trim());
//				triggers.add(trigger);			
//			}
//
//			connection.close();
//			statement.close();
//			resultSet.close();
//
//
//			oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool" );
//			db = oDatabaseDocumentTx.open("admin", "admin");
//
//
//			for (Trigger trigger2 : triggers) {
//				ODocument doc = new ODocument("Trigger");
//				doc.field("tabla_nombre",trigger2.getNombre_tabla());
//				doc.field("trigger_nombre",trigger2.getNombre_trigger());
//				doc.field("trigger_relacion", trigger2.getNombre_relacion());
//				doc.field("db_id",db_id);
//				doc.save();
//			}
//
//			db.close();
//
//		} catch (SQLException e) {
//
//			try {
//				connection.rollback();
//				db.rollback();
//			} catch (Exception e2) {
//			}
//		}
//
//		return "";
//	}


	@Override
	public ArrayList<Trigger> consultarTriggers(String db_id) {
		
		ArrayList<Trigger> triggers = new ArrayList<>();
		oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool" );
		db = oDatabaseDocumentTx.open("admin", "admin");
	
		
		String sql ="Select * from Trigger  where db_id = :db_id ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery<Object>(sql)).execute(params);

		Trigger trigger=null;
		if (!resultado.isEmpty()) {

			for (ODocument trigger_query : resultado) {
				trigger = new Trigger();
				trigger.setNombre_trigger(trigger_query.field("trigger_nombre"));
				trigger.setNombre_relacion(trigger_query.field("trigger_relacion"));
				trigger.setNombre_tabla(trigger_query.field("tabla_nombre"));
				triggers.add(trigger);

			}
		}

		return triggers;
	}
	
	
	@Override
	public String guardarTriggers(String url, String usuario, String password,String db_id) {
		String  sql_vistas = "SELECT routine_name, routine_definition  FROM information_schema.routines  where specific_schema = 'public' and data_type = 'trigger';";
		ArrayList<Trigger> triggers = new ArrayList<>();
		Trigger trigger_item;
		ODatabaseDocumentTx db = null;
		Connection   connection =null;

		try {

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection = DriverManager.getConnection(url, usuario , password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql_vistas);
			while(resultSet.next()){

				String	cadena = resultSet.getString(2).replaceAll("[\n\r]","");

				//************** INICIO SELECT ***********************
				Pattern pat = Pattern.compile("(SELECT)(.*?)(;)", Pattern.CASE_INSENSITIVE) ;
				Pattern pat_from = Pattern.compile("(FROM)(.*?)(WHERE|ORDER BY|JOIN|LEFT|NATURAL|RIGHT|INNER|;)", Pattern.CASE_INSENSITIVE);
				Pattern pat_join = Pattern.compile("(join)(.*?)(on|using)",Pattern.CASE_INSENSITIVE);

				Matcher mat = pat.matcher(cadena.replaceAll("[\n\r]",""));
				Matcher mat_from;
				Matcher mat_join;
				//				Matcher mat_join;

				// encontrar Selects 
				while(mat.find()){


					mat_from = pat_from.matcher(mat.group(0));

					// desde el FROM hasta el WHERE
					while(mat_from.find()){

						String tablas_sin_comas[] = mat_from.group(0).replaceAll("(?i)FROM|(?i)WHERE|(?i)ORDER BY|(?i)JOIN|(?i)NATURAL|(?i)RIGHT|(?i)INNER|(?i)LEFT|;", "").trim().split(",");

						StringTokenizer st;
						for (String string : tablas_sin_comas) {


							st = new StringTokenizer(string.trim());
							trigger_item = new Trigger();

							if (st.countTokens()>=2) {

								trigger_item.setNombre_trigger(resultSet.getString(1));
								trigger_item.setNombre_relacion("Select");
								trigger_item.setNombre_tabla(st.nextToken());
								triggers.add(trigger_item);


							}else if (st.countTokens()==1){

								trigger_item.setNombre_trigger(resultSet.getString(1));
								trigger_item.setNombre_relacion("Select");
								trigger_item.setNombre_tabla(string.trim());
								triggers.add(trigger_item);

							}


						}

					}// fin while


					mat_join = pat_join.matcher(mat.group(0).replace("(", ""));

					while(mat_join.find()){

						String tablas_sin_comas[] = mat_join.group(0).replaceAll("(?i)JOIN|(?i)USING|(?i)LEFT|(?i)RIGTH|(?i)LEFT|(?i)CROSS|(?i)ON|;", "").trim().split(",");
						StringTokenizer st;

						for (String string : tablas_sin_comas) {


							st = new StringTokenizer(string.trim());
							trigger_item = new Trigger();

							if (st.countTokens()>=2) {

								trigger_item.setNombre_trigger(resultSet.getString(1));
								trigger_item.setNombre_relacion("Select");
								trigger_item.setNombre_tabla(st.nextToken());
								triggers.add(trigger_item);

							}else if (st.countTokens()==1){

								trigger_item.setNombre_trigger(resultSet.getString(1));
								trigger_item.setNombre_relacion("Select");
								trigger_item.setNombre_tabla(st.nextToken());
								triggers.add(trigger_item);

							}
						}

					}
				}// fin select while

				//************** FIN SELECT  ***********************




				//************** INICIO  INERT ***********************
				Pattern pat_insert = Pattern.compile("((INSERT)(.*?)(;))", Pattern.CASE_INSENSITIVE) ;
				Pattern pat_values = Pattern.compile("(into)(.*?)(values|[(])", Pattern.CASE_INSENSITIVE);	
				Matcher mat_insert = pat_insert.matcher(cadena);
				Matcher mat_values;
				while(mat_insert.find()){
					mat_values = pat_values.matcher(mat_insert.group(0));

					while(mat_values.find()){



						String tablas_sin_comas[] = mat_values.group(0).replaceAll("(?i)INTO|(?i)VALUES|[(]", " ").trim().split(",");

						for (int i = 0; i < tablas_sin_comas.length; i++) {


							trigger_item = new Trigger();
							trigger_item.setNombre_trigger(resultSet.getString(1));
							trigger_item.setNombre_relacion("Insert");
							trigger_item.setNombre_tabla(tablas_sin_comas[i].trim());
							triggers.add(trigger_item);
						}



					}

				}


				//************** FIN  INERT ***********************


				//************** INICIO UPDATE ***********************

				Pattern pat_update = Pattern.compile("((update)(.*?)(;))", Pattern.CASE_INSENSITIVE) ;
				Pattern pat_set = Pattern.compile("(UPDATE)(.*?)(set)", Pattern.CASE_INSENSITIVE);	
				Matcher mat_update = pat_update.matcher(cadena);
				Matcher mat_set;


				while(mat_update.find()){


					mat_set = pat_set.matcher(mat_update.group(0));

					while(mat_set.find()){

						String tablas_sin_comas[] = mat_set.group(0).replaceAll("(?i)UPDATE|(?i)SET", " ").trim().split(",");

						StringTokenizer st;

						for (String string : tablas_sin_comas) {

							st = new StringTokenizer(string.trim());

							trigger_item = new Trigger();
							if (st.countTokens()==2) {


								String tablas_con_alias[] = string.trim().split(" ");

								for (int j = 0; j < tablas_con_alias.length; j++) {


									if (j==0||j%2==0) {

										trigger_item.setNombre_trigger(resultSet.getString(1));
										trigger_item.setNombre_relacion("Update");
										trigger_item.setNombre_tabla(tablas_con_alias[j].trim());
										triggers.add(trigger_item);
									}

								}

							}else if (st.countTokens()==1){


								trigger_item.setNombre_trigger(resultSet.getString(1));
								trigger_item.setNombre_relacion("Update");
								trigger_item.setNombre_tabla(string.trim());
								triggers.add(trigger_item);
							}
						}

					}

				}

				//************** FIN UPDATE ***********************


				//************** INICIO DELETE ***********************

				Pattern pat_delete = Pattern.compile("((delete)(.*?)(;))", Pattern.CASE_INSENSITIVE) ;
				Pattern pat_delete_from = Pattern.compile("(FROM)(.*?)(USING|WHERE|;)", Pattern.CASE_INSENSITIVE);	
				Matcher mat_delete= pat_delete.matcher(cadena);
				Matcher mat_delete_from;


				while(mat_delete.find()){



					mat_delete_from =pat_delete_from.matcher(mat_delete.group(0));

					while(mat_delete_from.find()){



						String tablas_sin_comas[] = mat_delete_from.group(0).replaceAll("(?i)FROM|(?i)USING|(?i)WHERE|;", " ").trim().split(",");

						StringTokenizer st;
						for (String string : tablas_sin_comas) {

							st = new StringTokenizer(string.trim());
							trigger_item = new Trigger();

							if (st.countTokens()==2) {


								String tablas_con_alias[] = string.trim().split(" ");

								for (int j = 0; j < tablas_con_alias.length; j++) {


									if (j==0||j%2==0) {

										trigger_item.setNombre_trigger(resultSet.getString(1));
										trigger_item.setNombre_relacion("Delete");
										trigger_item.setNombre_tabla(tablas_con_alias[j].trim());
										triggers.add(trigger_item);
									}

								}
							}else if (st.countTokens()==1){


								trigger_item.setNombre_trigger(resultSet.getString(1));
								trigger_item.setNombre_relacion("Delete");
								trigger_item.setNombre_tabla(string);
								triggers.add(trigger_item);
							}

						}
					}

				}
				//************** FIN DELETE ***********************
			}
			connection.close();
			statement.close();
			resultSet.close();

			ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
			db = oDatabaseDocumentTx.open("admin", "admin");


			//			OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("idseq");
			//			seq.next();
			for (Trigger trigger : triggers) {
				ODocument doc = new ODocument("Trigger");
				doc.field("tabla_nombre",trigger.getNombre_tabla());
				doc.field("trigger_nombre",trigger.getNombre_trigger());
				doc.field("trigger_relacion", trigger.getNombre_relacion());
				doc.field("db_id",db_id);
				doc.save();
			}


			db.close();



		} catch (SQLException e) {

			try {
				connection.rollback();
				db.rollback();
			} catch (Exception e2) {
			}




		}

		return "";

	}


	@Override
	public   Long obtenerNumeroTriggers(String db_id) {

		
		oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool" );
		db = oDatabaseDocumentTx.open("admin", "admin");

		String sql ="  select count(Distinct(trigger_nombre)) from Trigger where db_id = :db_id";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		return resultado.get(0).field("count");
	}



}
