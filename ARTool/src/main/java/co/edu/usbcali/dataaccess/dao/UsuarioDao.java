package co.edu.usbcali.dataaccess.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.sequence.OSequence;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import co.edu.usbcali.modelo.Tabla;
import co.edu.usbcali.modelo.Usuario;

@Scope("singleton")
@Repository("UsuarioDAO")
public class UsuarioDao  implements IUsuarioDAO {


	@Override
	public  Usuario consultarUsuario(String login, String password) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query par aconsultar el usuario
		String sql ="Select * from Usuario  where login = :login and password = :password";

		// parametros
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("login", login);
		params.put("password", password);
		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);

		if(!resultado.isEmpty()) {


			Usuario usuario = new Usuario();

			for (ODocument usuario_query : resultado) {
				usuario.setLogin(usuario_query.field("login"));
				usuario.setCorreo(usuario_query.field("correo"));
				usuario.setPassword(usuario_query.field("password"));
				usuario.setUsuario_id(usuario_query.field("usuario_id"));
				usuario.setNombre(usuario_query.field("nombre"));
			}


			db.close();
			return usuario;


		}else{
			db.close();
			return null;
		}

	}

	@Override
	public String guardarUsuario(Usuario usuario) {


		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		OSequence	seq;
		try {
			seq = 
					db.getMetadata().getSequenceLibrary().createSequence("usuarioseq", 
							OSequence.SEQUENCE_TYPE.ORDERED, new OSequence.CreateParams().setStart(1L).setIncrement(1).setCacheSize(20));
		} catch (Exception e) {
			seq = db.getMetadata().getSequenceLibrary().getSequence("usuarioseq");
		}

		ODocument doc = new ODocument("Usuario");
		doc.field("login",usuario.getLogin());
		doc.field("correo",usuario.getCorreo());
		doc.field("password",usuario.getPassword());
		doc.field("nombre",usuario.getNombre());
		doc.field("usuario_id",seq.next());
		doc.save();

		db.close();

		return "";
	}

	@Override
	public String modificarUsuario(String login, String password) {
		return null;
	}

	@Override
	public Usuario consultarUsuarioCorreo(String correo) {

		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query par aconsultar el usuario
		String sql ="Select * from Usuario  where correo = :correo ";

		// parametros
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("correo", correo);

		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);
		Usuario usuario = null;
		if(!resultado.isEmpty()) {

			usuario = new Usuario();

			for (ODocument usuario_query : resultado) {
				usuario.setLogin(usuario_query.field("login"));
				usuario.setCorreo(usuario_query.field("correo"));
				usuario.setPassword(usuario_query.field("password"));
				usuario.setUsuario_id(usuario_query.field("usuario_id"));
				usuario.setNombre(usuario_query.field("nombre"));
			}


			db.close();
			return usuario;
		}
		db.close();
		return usuario;
	}

	@Override
	public Usuario consultarUsuarioLogin(String login) {
		
		
		ODatabaseDocumentTx oDatabaseDocumentTx = new ODatabaseDocumentTx( "remote:localhost/artool");
		ODatabaseDocumentTx db = oDatabaseDocumentTx.open("admin", "admin");

		// query par aconsultar el usuario
		String sql ="Select * from Usuario  where login = :login ";

		// parametros
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("login", login);

		List<ODocument> resultado = db.command(new OSQLSynchQuery(sql)).execute(params);
		Usuario usuario = null;
		if(!resultado.isEmpty()) {

			usuario = new Usuario();

			for (ODocument usuario_query : resultado) {
				usuario.setLogin(usuario_query.field("login"));
				usuario.setCorreo(usuario_query.field("correo"));
				usuario.setPassword(usuario_query.field("password"));
				usuario.setUsuario_id(usuario_query.field("usuario_id"));
				usuario.setNombre(usuario_query.field("nombre"));
			}


			db.close();
			return usuario;
		}
		db.close();
		return usuario;

	}




}
