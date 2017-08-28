package co.edu.usbcali.modelo;

public class Db_Recovery {

	private String db_id;
	private Long usuario_id;
	private String nombre;
	private String nombre_conexion;
	private String fecha;


	public Db_Recovery(){
		
		
	}
	public String getDb_id() {
		return db_id;
	}
	public void setDb_id(String db_id) {
		this.db_id = db_id;
	}
	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre_conexion() {
		return nombre_conexion;
	}
	public void setNombre_conexion(String nombre_conexion) {
		this.nombre_conexion = nombre_conexion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Db_Recovery(String db_id, Long usuario_id, String nombre, String nombre_conexion, String fecha) {
		super();
		this.db_id = db_id;
		this.usuario_id = usuario_id;
		this.nombre = nombre;
		this.nombre_conexion = nombre_conexion;
		this.fecha = fecha;
	}



}
