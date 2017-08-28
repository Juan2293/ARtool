var table;
var dataTable_activa = false;
function guardarDb() {

	// metodo de  la libreria pace para hacer una animación de carga 
	Pace.start();
	
	var url_db = $("#url_input").val().trim();
	var nombre_conexion  = $("#conexion_input").val().trim();
	var usuario_db = $("#usuario_bd_input").val().trim();
	var password_db = $("#password_input").val().trim();

	dat = JSON.stringify({ "url_db": url_db, "nombre_conexion":nombre_conexion, "usuario_db":usuario_db, "password_db":password_db });

	$.ajax({

		url: "../controller/clienteRest/guardarDb",
		type : "POST",
		traditional : true,
		contentType : "application/json",
		dataType : "json",
		data : dat,
		success: function(data) {

			if (data.codigoError == "10" || data.codigoError == "50" ) {
				$("#alerta_conexion").attr('class', 'alert alert-danger alert-dismissible fade hide');
				$("#alerta_conexion" ).text( data.mensajeError );
				$("#alerta_conexion").fadeTo(2000, 500).slideUp(500, function(){
					$("#alerta_conexion").slideUp(500);
				});

			}

			if (data.codigoError == "-1") {

				$("#alerta_conexion").attr('class', 'alert alert-success alert-dismissible fade hide');
				$("#alerta_conexion" ).text( data.mensajeError );
				$("#alerta_conexion").fadeTo(2000, 500).slideUp(500, function(){
					$("#alerta_conexion").slideUp(500);
				});
				// para que se recargue de nuevo el datatable con el nuevo registro.
				contenido_usuario();

			}
			
			// metodo de  la libreria pace para pausar la animación de carga 
			Pace.stop();


		}

	});
}


function contenido_usuario() {

	// información del usuario
	$.ajax({

		url: "../controller/clienteRest/obtenerInfoUsuario",
		type : "GET",
		traditional : true,
		contentType : "application/json",

	}).then(function(data) {
		$("#nombre_sesion").text(data.nombre);
	})

	// información de las conexiones del usuario
	$.ajax({

		url: "../controller/clienteRest/getConexiones",
		type : "GET",
		traditional : true,
		contentType : "application/json",

	}).then(function(data) {


		var array_temp = new Array();

		//se crea un arreglo donde cada nombre de conexion se le asigna su id
		jQuery.each( data, function( i, val ) {
			array_temp[val.nombre_conexion] = val.db_id;
		});


		//se llena el datatable la primera vez con los datos del usuario DataTable 
		if (!dataTable_activa) {

			jQuery.each( data, function( i, val ) {
				$("#table_body").append(
						//se le asigna el id  de la conexion a cada fila
						"<tr data-id="+val.db_id+">"+
						"<td class='colum1'>"+(i+1)+"</td>"+
						"<td>"+val.nombre_conexion+"</td>"+
						"<td>"+val.nombre+"</td>"+
						"<td>"+val.fecha+"</td>"+
						"<td class='colum_btn'></td>"+
						"</tr>"
				);

			});

			//se cambia el idioma del datatable y se crea el botón para seleccionar la bd
			dataTable_activa = true;
			table = $('#dataTable').DataTable({

				"language": {
					"decimal":        "",
					"emptyTable":     "No hay registros disponibles",
					"info":           "_TOTAL_ registros ",
					"infoEmpty":      "Mostrando 0 de 0 registros",
					"infoFiltered":   "(filtered from _MAX_ total entries)",
					"infoPostFix":    "",
					"thousands":      ",",
					"lengthMenu":     "Registros: _MENU_",
					"loadingRecords": "Loading...",
					"processing":     "Processing...",
					"search":         "Buscar:",
					"zeroRecords":    "No se encontraron registros de la busqueda",
					"paginate": {
						"first":      "Primero",
						"last":       "Último",
						"next":       "Siguiente",
						"previous":   "Anterior"
					}
				}
			,
			"columnDefs": [ 


			               {
			            	   "targets": -1,
			            	   "data": null,
			            	   "defaultContent":

			            		   "<button type='button'  class=' btn btn-default btn_elegir_db' aria-label='Left Align' style='cursor:pointer' data-toggle='modal' data-target='#modal_seleccionar' id='btn_seleccion' >"+
			            		   "<span><i class='fa fa-check' aria-hidden='true'></i></span> "+
			            		   "</button>"

			               }]

			});

			//agregar nueva bd
		}else if (dataTable_activa){

			//se agrega una nueva fila al datatable, las columnas se separan por comas ',' 
			var rowNode = table
			.row.add( [ data.length, data[data.length-1].nombre_conexion,  data[data.length-1].nombre, data[data.length-1].fecha ,data[data.length-1].db_id ] )
			.draw().node();

			$( rowNode ).find('td').parents('tr').attr('data-id',data[data.length-1].db_id );
			$( rowNode ).find('td').eq(0).addClass('colum1');
			$( rowNode ).find('td').eq(4).addClass('colum_btn');


			array_temp[data[data.length-1].nombre_conexion] = data[data.length-1].db_id;
		}

		var fila_actual = null;
		var db_id_info = null; // variable para servicio que trae la información de la bd en el modal

		$('#dataTable tbody').on( 'click', 'button', function () {
			fila_actual = table.row( $(this).parents('tr') ).data();

			db_id_info = $(this).parents('tr').attr("data-id");

		} );

		$('#modal_seleccionar').on('show.bs.modal', function (e) {

			$.ajax({

				url: "../controller/clienteRest/obtenerInfoBd/"+db_id_info,
				type : "GET",
				traditional : true,
				contentType : "application/json",

			}).then(function(data) {

				$("#numero_tablas").text(data.numero_tablas);
				$("#numero_vistas").text(data.numero_vistas);
				$("#numero_funciones").text(data.numero_funciones);
				$("#numero_triggers").text(data.numero_triggers);


			});
		})

		$('#modal_seleccionar').on('hidden.bs.modal', function (e) {
			$("#numero_tablas").text(0);
			$("#numero_vistas").text(0);
			$("#numero_funciones").text(0);
			$("#numero_triggers").text(0);
		})

		$('#elegir_db').click(function () {


			if (fila_actual!=null) {

				dat = JSON.stringify({ "db_id": array_temp[fila_actual[1]]});
				$.ajax({
					// se envia el id de la conexión dependiendo de la columna donde éste en el datatable. 
					url: "../controller/clienteRest/seleccionarConexion",
					type : "POST",
					traditional : true,
					contentType : "application/json",
					dataType : "json",
					data : dat

				}).then(function(data) {

					$("#alerta_seleccion_db").fadeTo(3000, 500).slideUp(500, function(){
						$("#alerta_seleccion_db").slideUp(500);
					});
				});
			}

		} );


	});

}



contenido_usuario();





