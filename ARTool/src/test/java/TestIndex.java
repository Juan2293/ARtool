import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.edu.usbcali.modelo.Funcion;

public class TestIndex {
	
	public static void main(String[] args) {
		
		
		
		
	
		String tablas [] = {"usuario", "tipo_usuario", "vista", "funciones"};
		
		
		  String Str = new String("Welcome to Tutorialspoint.com");
	      System.out.print("Found Index :" );
	      
	      
//		while (sTexto.toLowerCase().indexOf("select") > -1) {
//			System.out.println(sTexto);
//	          sTexto = sTexto.substring(sTexto.indexOf(
//	            sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
//	          contador++; 
//	    }
		
		for (int i = 0; i < 1000; i++) {
			String sTextoBuscado = "SELECT";
		      String sTexto = "";
		      int contador =0;
			while (sTexto.toLowerCase().indexOf("select") > -1) {
				System.out.println(sTexto);
		          sTexto = sTexto.substring(sTexto.indexOf(
		            sTextoBuscado)+sTextoBuscado.length(),sTexto.length());
		          contador++; 
		    }
		}
		
			
	    

	    
	}
	
	
//	public void recuperarInfDb(){
//		
//		String	cadena = resultSet.getString(2).replaceAll("[\n\r]","");
//
//		//************** INICIO SELECT ***********************
//		Pattern pat = Pattern.compile("(SELECT)(.*?)(;)", Pattern.CASE_INSENSITIVE) ;
//		Pattern pat_from = Pattern.compile("(FROM)(.*?)(WHERE|ORDER BY|JOIN|LEFT|NATURAL|RIGHT|INNER|;)", Pattern.CASE_INSENSITIVE);
//		Pattern pat_join = Pattern.compile("(join)(.*?)(on|using)",Pattern.CASE_INSENSITIVE);
//
//		Matcher mat = pat.matcher(cadena.replaceAll("[\n\r]",""));
//		Matcher mat_from;
//		Matcher mat_join;
//		//				Matcher mat_join;
//
//		// encontrar Selects 
//		while(mat.find()){
//
//			//					System.out.println(mat.group(0));   
//
//			mat_from = pat_from.matcher(mat.group(0));
//
//			// desde el FROM hasta el WHERE
//			while(mat_from.find()){
//
//				String tablas_sin_comas[] = mat_from.group(0).replaceAll("(?i)FROM|(?i)WHERE|(?i)ORDER BY|(?i)JOIN|(?i)NATURAL|(?i)RIGHT|(?i)INNER|(?i)LEFT|;", "").trim().split(",");
//
//				StringTokenizer st;
//				for (String string : tablas_sin_comas) {
//
//
//					st = new StringTokenizer(string.trim());
//					funcion_item = new Funcion();
//
//					if (st.countTokens()>=2) {
//
//						funcion_item.setNombre_funcion(resultSet.getString(1));
//						funcion_item.setNombre_relacion("Select");
//						funcion_item.setNombre_tabla(st.nextToken());
//						funciones.add(funcion_item);
//
//
//					}else if (st.countTokens()==1){
//
//						funcion_item.setNombre_funcion(resultSet.getString(1));
//						funcion_item.setNombre_relacion("Select");
//						funcion_item.setNombre_tabla(string.trim());
//						funciones.add(funcion_item);
//
//					}
//
//
//				}
//
//			}// fin while
//
//
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
//					funcion_item = new Funcion();
//
//					if (st.countTokens()>=2) {
//
//						funcion_item.setNombre_funcion(resultSet.getString(1));
//						funcion_item.setNombre_relacion("Select");
//						funcion_item.setNombre_tabla(st.nextToken());
//						funciones.add(funcion_item);
//
//					}else if (st.countTokens()==1){
//
//						funcion_item.setNombre_funcion(resultSet.getString(1));
//						funcion_item.setNombre_relacion("Select");
//						funcion_item.setNombre_tabla(st.nextToken());
//						funciones.add(funcion_item);
//
//					}
//
//
//				}
//
//
//
//			}
//		}// fin select while
//		
//	}
}
