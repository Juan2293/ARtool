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

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import co.edu.usbcali.dataaccess.dao.TriggerDAO;
import co.edu.usbcali.modelo.Trigger;
import co.edu.usbcali.modelo.Vista;

public class Test {

	// vsalespersonsalesbyfiscalyearsdata
	static String vista_query = 
			" SELECT jc.jobcandidateid, "+
					" FROM holo, ( SELECT unnesting.jobcandidateid, "+
					"etenrmanete libreeee" +
					" FROM ( SELECT jobcandidate.jobcandidateid, "+
					"AS education "+
					"FROM humanresources.jobcandidate) unnesting) jc, LALa ; ";


	public static void sinconexion(){

		//		ResultSet rs = statement.executeQuery(sql_vistas);


		Pattern pat = Pattern.compile("(SELECT)(.*?)(;)", Pattern.CASE_INSENSITIVE) ;
		Pattern pat_from = Pattern.compile("(FROM)(.*?)(WHERE|ORDER BY|JOIN|LEFT|NATURAL|RIGHT|INNER|;)", Pattern.CASE_INSENSITIVE);
		Pattern pat_join = Pattern.compile("(join)(.*?)(on|using)",Pattern.CASE_INSENSITIVE);
		Pattern pat_select_en_from = Pattern.compile("(SELECT)(.*?)(FROM)",Pattern.CASE_INSENSITIVE);

		//			


		Matcher mat;
		Matcher mat_from;
		Matcher mat_join;
		Matcher mat_select_en_from;

		//		while(rs.next()){


		String	cadena = vista_query.replaceAll("[\n\r]","");
		mat =  pat.matcher(cadena);



		while(mat.find()){

			mat_from = pat_from.matcher(mat.group(0).replace("(", ""));

			while(mat_from.find()){

//				System.out.println(mat_from.group(0));
//				System.out.println();
				mat_select_en_from = pat_select_en_from.matcher(mat_from.group(0));


				String tablas_sin_comas[] = mat_from.group(0).replaceAll("(?i)FROM|(?i)WHERE|(?i)ORDER BY|;	", "").trim().split(",");
				StringTokenizer st;

				for (String string : tablas_sin_comas) {

//					System.out.println(string);

					st = new StringTokenizer(string.trim());


					if (st.countTokens()==2 || st.countTokens()==3) {

						System.out.println(st.nextToken()+ "-- 2 FROM --");
						//						vista.setNombre_vista(rs.getString(1));


					}else if (st.countTokens()==1){

						System.out.println(st.nextToken()+ "-- 1 FROM --");
						//						vista.setNombre_vista(rs.getString(1));


					}
				}

			}

//			mat_join = pat_join.matcher(mat.group(0).replace("(", ""));
//
//			while(mat_join.find()){
//
//				String tablas_sin_comas[] = mat_join.group(0).replaceAll("(?i)JOIN|(?i)USING|(?i)LEFT|(?i)RIGTH|(?i)LEFT|(?i)CROSS|(?i)ON|;", "").trim().split(",");
//				StringTokenizer st;
//
//				for (String string : tablas_sin_comas) {
//
//
//					st = new StringTokenizer(string.trim());
//					//					vista = new Vista();
//
//					if (st.countTokens()>=2) {
//
//						System.out.println(st.nextToken()+ "-- 2 JOIN --");
//						//						vista.setNombre_vista(rs.getString(1));
//						//						vista.setTabla_relacionada(st.nextToken());
//						//						vistas_y_relaciones.add(vista);
//
//
//					}else if (st.countTokens()==1){
//
//						System.out.println(st.nextToken()+ "-- 1 JOIN --");
//						//						vista.setNombre_vista(rs.getString(1));
//						//						vista.setTabla_relacionada(st.nextToken());
//						//						vistas_y_relaciones.add(vista);
//
//					}
//
//
//				}
//			}
			
			
		}


		//		}// fin while


		//		System.out.println(vista);

	}



	public static  String guardarVistas() {

		String url ="jdbc:postgresql://localhost:5432/Adventureworks";
		String user = "postgres";
		String password = "postgres";
		//		String db_id;


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


			Pattern  pat_from_select =  Pattern.compile("SELECT", Pattern.CASE_INSENSITIVE) ;
			Matcher mat_from_select;
			//			


			Matcher mat;
			Matcher mat_from;
			Matcher mat_join;


			while(rs.next()){


				String	cadena = rs.getString(2).replaceAll("[\n\r]","");

				//				System.out.println("**************************************************************************");
				//				System.out.println("");
				//				System.out.println("**********************  "+rs.getString(1)+ "******************************");
				//				System.out.println("");
				//				System.out.println(cadena);
				//				System.out.println("");
				mat =  pat.matcher(cadena);



				while(mat.find()){

					mat_from = pat_from.matcher(mat.group(0).replace("(", ""));

					while(mat_from.find()){

						String tablas_sin_comas[] = mat_from.group(0).replaceAll("(?i)FROM|(?i)WHERE|(?i)ORDER BY|;	", "").trim().split(",");
						StringTokenizer st;

						for (String string : tablas_sin_comas) {

							mat_from_select = pat_from_select.matcher(string);

							//							if (!mat_from_select.find()) {


							st = new StringTokenizer(string.trim());
							vista = new Vista();

							//												System.out.println(st.nextToken()+ " --> FROM");

							if (st.countTokens()==2 || st.countTokens()==3) {
								//													System.out.println(rs.getString(1)+" -> vista");
								//									System.out.println(st.nextToken()+ " -> FROM 2");


								//								vista.setNombre_vista(rs.getString(1));
								//								vista.setTabla_relacionada(st.nextToken());
								vistas_y_relaciones.add(vista);


							}else if (st.countTokens()==1){

								//													System.out.println(rs.getString(1)+" -> vista");
								//									System.out.println(st.nextToken().replace(";", "")+ " -> FROM 1");
								//								vista.setNombre_vista(rs.getString(1));
								//								vista.setTabla_relacionada(st.nextToken());
								vistas_y_relaciones.add(vista);

							}

						}

					}

				}


			}// fin while


			rs.close();
			statement.close();
			connection.close();




		} catch (Exception e) {

			System.out.println(e.getMessage());


		}


		return "";
	}


	public static void main(String[] args) {
		//		guardarVistas();

		sinconexion();




		//						String  string = "person.person";
		//						
		//						String arreglo []= string.split("\\.");
		//						
		//						System.out.println(arreglo.length);
		//						for (String string2 : arreglo) {
		//							System.out.println(string2);
		//						}
	}

}
