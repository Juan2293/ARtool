package co.edu.usbcali.dataaccess.dto;

public class Db_RecoveryDTO {
	
	
	private String db_id;
	private Long usuario_id;
	private String nombre;
	private String nombre_conexion;
	private String fecha;
	private String password_db;
	private String usuario_db;
	private String url_db;
	private Long numero_tablas;
	private Long numero_vistas;
	private Long numero_funciones;
	private Long numero_triggers;
	private String mensajeError;
	private String codigoError;
	
	
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
	public String getPassword_db() {
		return password_db;
	}
	public void setPassword_db(String password_db) {
		this.password_db = password_db;
	}
	public String getUsuario_db() {
		return usuario_db;
	}
	public void setUsuario_db(String usuario_db) {
		this.usuario_db = usuario_db;
	}
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public String getCodigoError() {
		return codigoError;
	}
	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}
	public String getUrl_db() {
		return url_db;
	}
	public void setUrl_db(String url_db) {
		this.url_db = url_db;
	}
	public Long getNumero_tablas() {
		return numero_tablas;
	}
	public void setNumero_tablas(Long numero_tablas) {
		this.numero_tablas = numero_tablas;
	}
	public Long getNumero_vistas() {
		return numero_vistas;
	}
	public void setNumero_vistas(Long numero_vistas) {
		this.numero_vistas = numero_vistas;
	}
	public Long getNumero_funciones() {
		return numero_funciones;
	}
	public void setNumero_funciones(Long numero_funciones) {
		this.numero_funciones = numero_funciones;
	}
	public Long getNumero_triggers() {
		return numero_triggers;
	}
	public void setNumero_triggers(Long numero_triggers) {
		this.numero_triggers = numero_triggers;
	}

}
