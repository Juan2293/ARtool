package co.edu.usbcali.dataaccess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ARG_IN;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import co.edu.usbcali.modelo.Funcion;
import co.edu.usbcali.modelo.Vista;

@Scope("singleton")
@Repository("VistaDAO")
public class VistaDAO implements IVistaDAO{

	@Override
	public String guardarVistas(String url, String user, String password, String db_id) {


		ODatabaseDocumentTx db = null;
		Connection connection = null;

		//array para guardar las relaciones por medio de fk entre tablas
		ArrayList<Vista> vistas_y_relaciones = new ArrayList<Vista>();
		Vista vista;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user , password);
			Statement statement = connection.createStatement();
			String  sql_vistas = "select table_name, view_definition from INFORMATION_SCHEMA.views  WHERE table_schema!='pg_catalog' AND table_schema!='information_schema' ";

			//recupero las vistas	
			ResultSet rs = statement.executeQuery(sql_vistas);


			Pattern pat = Pattern.compile("(SELECT)(.*?)(;)", Pattern.CASE_INSENSITIVE) ;
			Pattern pat_from = Pattern.compile("(FROM)(.*?)(WHERE|ORDER BY|JOIN|LEFT|NATURAL|RIGHT|INNER|;)", Pattern.CASE_INSENSITIVE);
			Pattern pat_join = Pattern.compile("(join)(.*?)(on|using)",Pattern.CASE_INSENSITIVE);

			//			


			Matcher mat;
			Matcher mat_from;
			Matcher mat_join;


			while(rs.next()){


				String	cadena = rs.getString(2).replaceAll("[\n\r]","");
				mat =  pat.matcher(cadena);



				while(mat.find()){

					mat_from = pat_from.matcher(mat.group(0).replace("(", ""));

					while(mat_from.find()){

						String tablas_sin_comas[] = mat_from.group(0).replaceAll("(?i)FROM|(?i)WHERE|(?i)ORDER BY|;	", "").trim().split(",");
						StringTokenizer st;

						for (String string : tablas_sin_comas) {


							st = new StringTokenizer(string.trim());
							vista = new Vista();

							if (st.countTokens()>=2) {

								vista.setNombre_vista(rs.getString(1));
								vista.setTabla_relacionada(st.nextToken());
								vistas_y_relaciones.add(vista);


							}else if (st.countTokens()==1){

								vista.setNombre_vista(rs.getString(1));
								vista.setTabla_relacionada(st.nextToken());
								vistas_y_relaciones.add(vista);

							}
						}

					}

					mat_join = pat_join.matcher(mat.group(0).replace("(", ""));

					while(mat_join.find()){

						String tablas_sin_comas[] = mat_join.group(0).replaceAll("(?i)JOIN|(?i)USING|(?i)LEFT|(?i)RIGTH|(?i)LEFT|(?i)CROSS|(?i)ON|;", "").trim().split(",");
						StringTokenizer st;

						for (String string : tablas_sin_comas) {


							st = new StringTokenizer(string.trim());
							vista = new Vista();

							if (st.countTokens()>=2) {

								vista.setNombre_vista(rs.getString(1));
								vista.setTabla_relacionada(st.nextToken());
								vistas_y_relaciones.add(vista);


							}else if (st.countTokens()==1){

								vista.setNombre_vista(rs.getString(1));
								vista.setTabla_relacionada(st.nextToken());
								vistas_y_relaciones.add(vista);

							}


						}
					}
				}


			}// fin while


			rs.close();
			statement.close();
			connection.close();



			ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
			db = oDatabaseDocumentTx.open("admin", "admin");


			//			OSequence seq = db.getMetadata().getSequenceLibrary().getSequence("idseq");
			//			seq.next();
			for (Vista vista_r : vistas_y_relaciones) {
				ODocument doc = new ODocument("Vista");
				doc.field("tabla_nombre",vista_r.getTabla_relacionada());
				doc.field("nombre_vista",vista_r.getNombre_vista()	);
				doc.field("db_id",db_id);
				doc.save();
			}


			db.close();



		} catch (Exception e) {
			
			
			db.close();


		}


		return "";
	}

	@Override
	public ArrayList<Vista> consultarVistas(String db_id) {

		ArrayList<Vista> vistas = new ArrayList<>();
		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query par aconsultar el usuario
		String sql ="Select * from Vista  where db_id = :db_id ";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		Vista vista = null;
		if(!resultado.isEmpty()) {
			for (ODocument vista_query : resultado) {
				vista = new Vista();
				vista.setNombre_vista(vista_query.field("nombre_vista"));
				vista.setTabla_relacionada(vista_query.field("tabla_nombre"));
				vistas.add(vista);
			}

		}


		return vistas;
	}

	@Override
	public Long obtenerNumeroVistas(String db_id) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");


		String sql ="  select count(Distinct(nombre_vista)) from Vista where db_id = :db_id";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("db_id", db_id);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		Long  numero = resultado.get(0).field("count");
		
		db.close();
		return numero;

	}





}