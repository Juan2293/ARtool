
function consultarUsuario() {


	var usuario = ($("#usuario").val().trim()=="") ? null : $("#usuario").val().trim();
	var password = ($("#password").val().trim()=="") ? null : $("#password").val().trim();

	$.ajax({

		url: "controller/clienteRest/getUsuario/"+usuario+"/"+password+"",
		type : "GET",
		traditional : true,
	}).then(function(data) {

		console.log(data);
		if (data.codigoError == "10" || data.codigoError == "50" ) {
			$("#alerta_usuario").attr('class', 'alert alert-danger alert-dismissible fade show');
			$("#alerta_usuario" ).text( data.mensajeError );
			$("#alerta_usuario").fadeTo(3000, 500).slideUp(500, function(){
				$("#alerta_usuario").slideUp(500);
			});
		}
		if (data.codigoError == "-1") {
			document.location.href="/ARTool/Pages/visualizador.html"
		}
	});

}

function guardarUsuario() {

	var login = $("#r_usuario").val().trim();
	var password = $("#r_password").val().trim();
	var password_con = $("#r_confirmar_password").val().trim();
	var correo = $("#r_email").val().trim();
	var nombre = $("#r_nombre").val().trim();

	dat = JSON.stringify({ "login": login, "password":password, "correo":correo, "nombre":nombre, "password_con":password_con });
	$.ajax({

		url: "controller/clienteRest/guardarUsuario",
		type : "POST",
		traditional : true,
		contentType : "application/json",
		dataType : "json",
		data : dat,
	}).then(function(data) {

		console.log(data);
		if (data.codigoError == "10" || data.codigoError == "50" ) {
			$("#alerta_registrar").attr('class', 'alert alert-danger alert-dismissible fade show');
			$("#alerta_registrar" ).text( data.mensajeError );
			$("#alerta_registrar").fadeTo(10000, 500).slideUp(500, function(){
				$("#alerta_registrar").slideUp(500);
			});
		}

		if (data.codigoError == "-1") {

			$("#alerta_registrar").attr('class', 'alert alert-success alert-dismissible fade show');
			$("#alerta_registrar" ).text( data.mensajeError );
			$("#alerta_registrar").fadeTo(10000, 500).slideUp(500, function(){
				$("#alerta_registrar").slideUp(500);
			});
		}

	});

}

//Borrar la información del registro cunado se clickea el tab del login
$('#tab-login').click(function(){

	$("#r_usuario").val( "" );
	$("#r_password").val( "" );
	$("#r_confirmar_password").val( "" );
	$("#r_email").val( "" );
	$("#r_nombre").val( "" );
	$("#usuario").val( "" );
	$("#password").val( "" );
	$('#alerta_registrar').hide();

});



