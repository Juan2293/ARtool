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

import co.edu.usbcali.modelo.Funcion;
import co.edu.usbcali.modelo.Tabla;

@Scope("singleton")
@Repository("FuncionesDAO")
public class FuncionDAO implements IFuncionDAO {


	@Override
	public String guardarFunciones(String url, String usuario, String password,String db_id) {
		String  sql_vistas = " SELECT routine_name, routine_definition  FROM information_schema.routines  where specific_schema = 'public' and data_type != 'trigger';";
		ArrayList<Funcion> funciones = new ArrayList<>();
		Funcion funcion_item;
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
							funcion_item = new Funcion();

							if (st.countTokens()>=2) {

								funcion_item.setNombre_funcion(resultSet.getString(1));
								funcion_item.setNombre_relacion("Select");
								funcion_item.setNombre_tabla(st.nextToken());
								funciones.add(funcion_item);


							}else if (st.countTokens()==1){

								funcion_item.setNombre_funcion(resultSet.getString(1));
								funcion_item.setNombre_relacion("Select");
								funcion_item.setNombre_tabla(string.trim());
								funciones.add(funcion_item);

							}


						}

					}// fin while


					mat_join = pat_join.matcher(mat.group(0).replace("(", ""));

					while(mat_join.find()){

						String tablas_sin_comas[] = mat_join.group(0).replaceAll("(?i)JOIN|(?i)USING|(?i)LEFT|(?i)RIGTH|(?i)LEFT|(?i)CROSS|(?i)ON|;", "").trim().split(",");
						StringTokenizer st;

						for (String string : tablas_sin_comas) {


							st = new StringTokenizer(string.trim());
							funcion_item = new Funcion();

							if (st.countTokens()>=2) {

								funcion_item.setNombre_funcion(resultSet.getString(1));
								funcion_item.setNombre_relacion("Select");
								funcion_item.setNombre_tabla(st.nextToken());
								funciones.add(funcion_item);

							}else if (st.countTokens()==1){

								funcion_item.setNombre_funcion(resultSet.getString(1));
								funcion_item.setNombre_relacion("Select");
								funcion_item.setNombre_tabla(st.nextToken());
								funciones.add(funcion_item);

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


							funcion_item = new Funcion();
							funcion_item.setNombre_funcion(resultSet.getString(1));
							funcion_item.setNombre_relacion("Insert");
							funcion_item.setNombre_tabla(tablas_sin_comas[i].trim());
							funciones.add(funcion_item);
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

							funcion_item = new Funcion();
							if (st.countTokens()==2) {


								String tablas_con_alias[] = string.trim().split(" ");

								for (int j = 0; j < tablas_con_alias.length; j++) {


									if (j==0||j%2==0) {

										funcion_item.setNombre_funcion(resultSet.getString(1));
										funcion_item.setNombre_relacion("Update");
										funcion_item.setNombre_tabla(tablas_con_alias[j].trim());
										funciones.add(funcion_item);
									}

								}

							}else if (st.countTokens()==1){


								funcion_item.setNombre_funcion(resultSet.getString(1));
								funcion_item.setNombre_relacion("Update");
								funcion_item.setNombre_tabla(string.trim());
								funciones.add(funcion_item);
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
							funcion_item = new Funcion();

							if (st.countTokens()==2) {


								String tablas_con_alias[] = string.trim().split(" ");

								for (int j = 0; j < tablas_con_alias.length; j++) {


									if (j==0||j%2==0) {

										funcion_item.setNombre_funcion(resultSet.getString(1));
										funcion_item.setNombre_relacion("Delete");
										funcion_item.setNombre_tabla(tablas_con_alias[j].trim());
										funciones.add(funcion_item);
									}

								}
							}else if (st.countTokens()==1){


								funcion_item.setNombre_funcion(resultSet.getString(1));
								funcion_item.setNombre_relacion("Delete");
								funcion_item.setNombre_tabla(string);
								funciones.add(funcion_item);
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
			for (Funcion funcion : funciones) {
				ODocument doc = new ODocument("Funcion");
				doc.field("tabla_nombre",funcion.getNombre_tabla());
				doc.field("funcion_nombre",funcion.getNombre_funcion());
				doc.field("funcion_relacion", funcion.getNombre_relacion());
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
	public ArrayList<Funcion> consultarFunciones(String db_id) {

		ArrayList<Funcion> funciones = new ArrayList<>();
		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		String sql ="Select * from Funcion  where db_id = :db_id ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);
		
		Funcion funcion=null;
		if (!resultado.isEmpty()) {
			
			for (ODocument funcion_query : resultado) {
				funcion = new Funcion();
				funcion.setNombre_funcion(funcion_query.field("funcion_nombre"));
				funcion.setNombre_relacion(funcion_query.field("funcion_relacion"));
				funcion.setNombre_tabla(funcion_query.field("tabla_nombre"));
				funciones.add(funcion);
				
			}
		}

		return funciones;
	}


	@Override
	public   Long obtenerNumeroFunciones(String db_id) {

	
		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		
		String sql ="  select count(Distinct(funcion_nombre)) from Funcion where db_id = :db_id";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		return resultado.get(0).field("count");
	}
	




}
