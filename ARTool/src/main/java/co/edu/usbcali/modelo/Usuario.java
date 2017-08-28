package co.edu.usbcali.modelo;

public class Usuario {

	private Long usuario_id;
	private String correo;
	private String nombre;
	private String login;
	private String password;

	public Long getUsuario_id() {
		return usuario_id;
	}
	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Usuario(){
		
		
	}
	public Usuario(Long usuario_id, String correo, String nombre, String login, String password) {
		super();
		this.usuario_id = usuario_id;
		this.correo = correo;
		this.nombre = nombre;
		this.login = login;
		this.password = password;
	}

}
