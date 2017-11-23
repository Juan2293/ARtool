
var toggle_presionado= false;

$(document).ready(function(){
	$('.menu_abajo:has(ul)').click(function(e){
		e.preventDefault();

		if($(this).hasClass('activado')){
			$('.menu_abajo_jerarquia').removeClass('activado');	
			$('.menu_abajo_jerarquia ul').slideUp();
			$(this).removeClass('activado');
			$(this).children('ul').slideUp();
		}
		else {
			$('.menu_abajo_jerarquia ul').slideUp();
			$('.menu_abajo_jerarquia').removeClass('activado');	
			$('.menu_abajo ul').slideUp();
			$('.menu_abajo li').removeClass('activado');
			$(this).addClass('activado');
			$(this).children('ul').slideDown();
		}
	});

	$('.menu_abajo_jerarquia:has(ul)').click(function(e){

		e.preventDefault();

		if($(this).hasClass('activado')){
			$('.menu_abajo').removeClass('activado');	
			$('.menu_abajo ul').slideUp();
			$(this).removeClass('activado');
			$(this).children('ul').slideUp();

		}
		else {
			$('.menu_abajo ul').slideUp();	
			$('.menu_abajo').removeClass('activado');	
			$('.menu_abajo_jerarquia ul').slideUp();
			$('.menu_abajo_jerarquia li').removeClass('activado');
			$(this).addClass('activado');
			$(this).children('ul').slideDown();
		}
	});

	//toggle
	$(".side").toggle();
	$(".side").css("visibility","visible");
	$("#btn").click(function(){
		if (toggle_presionado==false) {
			toggle_presionado=true;
			document.getElementById("btn").style.visibility = "hidden";		
		}
		$(".side").toggle("fast");

		if (networkSide!=null) {
			setTimeout( "networkSide.fit(nodes_side)" , 500 );
		}

	});	

	$("#close_side").click(function(){
		$(".side").toggle("fast");
		document.getElementById("btn").style.visibility = "visible";
		toggle_presionado=false;
	});


	$(".btn_medidas").click(function(){
		var tablas= Object.keys(nodes.get({returnType:"Object"})).length;
		var edges= allEdges;
		var grado_medio=(2*edges)/tablas;
		var gradoDelGrafo= edges*2;

		if(tablas==0){
			$(".medidas_grado_medio").text(""+0)
			$(".medidas_grado_grafo").text(""+0)
		}
		else {
			$(".medidas_grado_medio").text(""+grado_medio)
			$(".medidas_grado_grafo").text(""+gradoDelGrafo)
		}

		$(".medidas_imple").text("Tbi");
		$(".medidas_imple").css("color","red");


	});

});


function bajar_buscador(){
	$("#tags").toggle("fast");
	$("#tags").val("")
}


//vis.js
var nodes;
var edges;
var network;
var allNodes;
var allEdges =0;

//validaciones
var tablas_activas =  false;
var vistas_activas =  false;
var funciones_activas = false;
var triggers_activos = false;


//nodos en el buscador
var filtro_items =[];



function cargarContenido(){

	// se carga el nombre del usuario que ingresó 
	$.ajax({

		url: "../controller/clienteRest/obtenerInfoUsuario",
		type : "GET",
		traditional : true,
		contentType : "application/json",

	}).then(function(data) {
		$("#nombre_sesion").text(data.nombre);
	});

	// si hay una base de datos seleccionada se cargan los datos de ésta para el panel 
	// en el viualizador
	$.ajax({

		url: "../controller/clienteRest/obtenerInfoUsuDb",
		type : "GET",
		traditional : true

	}).then(function(data) {
		//$("#nombre_sesion").text(data.nombre.toUpperCase());

		$("#nombre_conexion_titulo").text(data.nombre_conexion);
		$("#nombre_db_titulo").text(data.nombre);
		$("#nombre_db_side").text(data.nombre);
		$("#fecha_db_side").text(data.fecha);

	});

}
// se ejecuta la función para cargar el contenido
cargarContenido();

function startNetwork() {



	var options={
			nodes:{

				borderWidth: 2,
				shape: "dot",
				color:{

					highlight: "#34495E"
				}
			},
			"edges": {
				"smooth": {
					"forceDirection": "none",
					"roundness": 1
				},
				color: {

					highlight:'#FBA622'

				}
			},
			physics: {
				"forceAtlas2Based": {
					"springLength": 100
				},
				"minVelocity": 0.75,
				"solver": "forceAtlas2Based",	
				"timestep": 0.7
			},
			layout: {
				improvedLayout:true
			},

			interaction: {
				navigationButtons: true,
				keyboard: true,
				hover:false,
				dragNodes: true,
				zoomView: true,
				dragView: true 
			},

			groups:{
				tablas: {
					level:1,color: {background:"red" , border: 'red'}
				},
				vistas: {
					level:2,color:{ background:"blue" , border: "blue"}
				},
				funciones: {
					level:3,color:{ background:"green" , border: "green" }
				},
				triggers:{

					level:4, color:{ background:"yellow", border:"yellow"}
				}
			}		     

	} ;



	nodes = new vis.DataSet();
	edges = new vis.DataSet();

	var container = document.getElementById("mynetwork");
	var data = {
			nodes: nodes,
			edges: edges
	};

	network = new vis.Network(container, data, options);
	network.on("click",neighbourhoodHighlight);
	optionSide = options;

}


startNetwork();



function cambiar_a_jerarquia(){


	var optionsJerarquia={

			interaction: {
				navigationButtons: true,
				keyboard: true
			},
			"edges": {
				smooth:{
					enabled: false,
					type:'continuous'
				},
				color: {
					highlight:'#FBA622'
				}
			},
			layout: {
				hierarchical: {
					sortMethod: "directed",
					levelSeparation: 300,
					nodeSpacing: 100,
					treeSpacing:200,
					edgeMinimization:true,
					parentCentralization:true,
					direction:"UD",
					blockShifting: true,
				}
			},
			nodes:{

				borderWidth: 2,
				shape: "dot",
				"fixed": {
					"x": false,
					"y": false
				}
			}
			,
			physics:false
			,
			groups:{
				tablas: {
					level:1,color: {background:"red" , border: 'red'}
				},
				vistas: {
					level:2,color:{ background:"blue" , border: "blue"}
				},
				funciones: {
					level:3,color:{ background:"green" , border: "green" }
				},
				triggers:{

					level:4, color:{ background:"yellow", border:"yellow"}
				}
			}

	} ;

	optionSide = optionsJerarquia;
	network.setOptions(optionsJerarquia);
	//network.redraw();


}


function cambiar_a_grafo(){

	var options={

			physics: {
				enabled:true,
				"forceAtlas2Based": {
					"springLength": 100
				},
				"minVelocity": 0.75,
				"solver": "forceAtlas2Based",	
				"timestep": 0.7
			},

			layout: {
				randomSeed: undefined,
				hierarchical: {
					enabled:false
				}
			},
			"edges": {
				physics:true,
				"smooth": {
					enabled: true,
					type: "dynamic",
					"forceDirection": "none",
					"roundness": 1
				},
				color: {
					highlight:'#FBA622'
				}
			},
			nodes:{
				borderWidth: 2,
				shape: "dot",
				color:{

					highlight: "#34495E"
				}
			},

			interaction: {
				navigationButtons: true,
				keyboard: true,
				hover:false,
				dragNodes: true,
				zoomView: true,
				dragView: true 
			},

			groups:{
				tablas: {
					level:1,color: {background:"red", border:"red"}
				},
				vistas: {
					level:2,color:{ background:"blue", border:"blue"}
				},
				funciones: {
					level:3,color:{ background:"green", border:"green"}
				},
				triggers:{

					level:4, color:{ background:"yellow", border:"yellow"}
				}
			}
	} ;

	optionSide = options;
	network.setOptions(options);


}
function borrar_grafo(){

	if (network !==null){
		network.destroy();
		network = null;
		nodes =null;
		edges=null;
		allNodes=null;

	}

	startNetwork();

	allEdges =0;
	funciones_activas = false;
	tablas_activas = false;
	vistas_activas = false;
	triggers_activos = false;


	numero_funciones =0;
	numero_vistas=0;
	numero_tablas=0;
	numero_triggers =0;


	numero_fk=0;

	numero_select_vistas =0;

	numero_select_funciones =0;
	numero_delete_funciones =0;
	numero_insert_funciones =0;
	numero_update_funciones =0;

	numero_select_triggers =0;
	numero_delete_triggers =0;
	numero_insert_triggers =0;
	numero_update_triggers =0;




	$("#numero_nodos").text(0);
	$("#numero_edges").text(0);
	$("#numero_tablas").text(0);
	$("#numero_funciones").text(0);
	$("#numero_vistas").text(0);
	$("#numero_triggers").text(0);


	$("#numero_fk").text(0);

	$("#numero_select_vistas").text(0);

	$("#numero_select_funciones").text(0);
	$("#numero_update_funciones").text(0);
	$("#numero_delete_funciones").text(0);
	$("#numero_insert_funciones").text(0);

	$("#numero_select_triggers").text(0);
	$("#numero_update_triggers").text(0);
	$("#numero_delete_triggers").text(0);
	$("#numero_insert_triggers").text(0);



	infoArray.splice(0, infoArray.length);
	nodosPrueba.splice(0, nodosPrueba.length);
	edgesPrueba.splice(0, edgesPrueba.length);
	colorTablas.splice(0, colorTablas.length);
	colorVistas.splice(0, colorVistas.length);
	colorFunciones.splice(0, colorFunciones.length);
	colorTriggers.splice(0, colorTriggers.length);
	intContadorFunciones=0;
	intContadorTablas=0;
	intContadorVistas=0;
	intContadorTriggers=0;
	
	filtro_items=[];
	autocompletar();


}

var array_nodes_conect;
var array_edges_conect;
var nodes_side;
var edges_side;
var optionSide;
var networkSide;
function drawSide( params ) {

	nodes_side = new vis.DataSet();
	edges_side = new vis.DataSet();

	// crear a network
	var container = document.getElementById('network_side');

	var data = {
			nodes: nodes_side,
			edges: edges_side
	};

	networkSide = new vis.Network(container, data, optionSide);

	// se agregan los nodos 
	for (var i = 0; i < array_nodes_conect.length; i++) {

		nodes_side.add(nodes.get(array_nodes_conect[i]));
	}

	allNodesSide =  nodes_side.get({returnType:"Object"});


	// se quita el gris del nodo si es seleccionado luego de que otro está seleccionado.
	for (var i = 0; i<array_nodes_conect.length; i++) {

		allNodesSide[array_nodes_conect[i]].color = undefined;
		if (allNodesSide[array_nodes_conect[i]].hiddenLabel !== undefined) {
			allNodesSide[array_nodes_conect[i]].label = allNodesSide[array_nodes_conect[i]].hiddenLabel;
			allNodesSide[array_nodes_conect[i]].hiddenLabel = undefined;
		}
	}
	//se actualiza las prioridades del nodo
	var updateArray = [];
	for (nodeId in allNodesSide) {
		if (allNodesSide.hasOwnProperty(nodeId)) {
			updateArray.push(allNodesSide[nodeId]);
		}
	}
	nodes_side.update(updateArray);


	// se agregan los edges
	for (var i = 0; i < array_edges_conect.length; i++) {
		edges_side.add(edges.get(array_edges_conect[i]));
	}


//	networkSide.fit(nodes_side);

	mostrar_informacion_nodo(params);


}



var highlightActive = false;
function neighbourhoodHighlight(params) {

	// if something is selected:
	if (params.nodes.length > 0) {


	
		highlightActive = true;
		var i,j;
		var selectedNode = params.nodes[0];
		var degrees = 2;

		var connectedNodes = network.getConnectedNodes(selectedNode);
		array_edges_conect = params.edges;
		array_nodes_conect = connectedNodes;
		array_nodes_conect.push(selectedNode);
		// mark all nodes as hard to read.
		for (var nodeId in allNodes) {
			allNodes[nodeId].color = 'rgba(200,200,200,0.5)';
			if (allNodes[nodeId].hiddenLabel === undefined) {
				allNodes[nodeId].hiddenLabel = allNodes[nodeId].label;
				allNodes[nodeId].label = undefined;
			}
		}
		//var connectedNodes = network.getConnectedNodes(selectedNode);
		var allConnectedNodes = [];

		// get the second degree nodes
		for (i = 1; i < degrees; i++) {
			for (j = 0; j < connectedNodes.length; j++) {
				allConnectedNodes = allConnectedNodes.concat(network.getConnectedNodes(connectedNodes[j]));

			}
		}

		// all second degree nodes get a different color and their label back
		for (i = 0; i < allConnectedNodes.length; i++) {
			allNodes[allConnectedNodes[i]].color = 'rgba(150,150,150,0.75)';
			if (allNodes[allConnectedNodes[i]].hiddenLabel !== undefined) {
				allNodes[allConnectedNodes[i]].label = allNodes[allConnectedNodes[i]].hiddenLabel;
				allNodes[allConnectedNodes[i]].hiddenLabel = undefined;
			}
		}

		// all first degree nodes get their own color and their label back
		for (i = 0; i < connectedNodes.length; i++) {
			allNodes[connectedNodes[i]].color = undefined;
			if (allNodes[connectedNodes[i]].hiddenLabel !== undefined) {
				allNodes[connectedNodes[i]].label = allNodes[connectedNodes[i]].hiddenLabel;
				allNodes[connectedNodes[i]].hiddenLabel = undefined;
			}
		}

		// the main node gets its own color and its label back.
		//		allNodes[selectedNode].color = undefined;
		allNodes[selectedNode].color  = 'rgba(52, 73, 94,1.0)';
		if (allNodes[selectedNode].hiddenLabel !== undefined) {
			allNodes[selectedNode].label = allNodes[selectedNode].hiddenLabel;
			allNodes[selectedNode].hiddenLabel = undefined;

		}

		drawSide( params );
	}
	else if (highlightActive === true) {


		// reset all nodes
		for (var nodeId in allNodes) {
			allNodes[nodeId].color = undefined;
			if (allNodes[nodeId].hiddenLabel !== undefined) {
				allNodes[nodeId].label = allNodes[nodeId].hiddenLabel;
				allNodes[nodeId].hiddenLabel = undefined;
			}
		}
		highlightActive = false

	}

	// transform the object into an array
	var updateArray = [];
	for (nodeId in allNodes) {
		if (allNodes.hasOwnProperty(nodeId)) {
			updateArray.push(allNodes[nodeId]);
		}
	}
	nodes.update(updateArray);
}

var numero_tablas = 0;
var numero_fk=0;
function obtener_tablas(){

	if (!tablas_activas) {



		$.ajax({

			url: "../controller/clienteRest/getTablas"

		}).then(function(tablas) {



			for (var i = 0; i < tablas[0][0].length; i++) {

				if (!nodes.get(tablas[0][0][i])) {
					nodes.add({id:tablas[0][0][i], label:tablas[0][0][i], group:'tablas', level:1   });
					filtro_items.push(tablas[0][0][i]);
					numero_tablas++;
				}

			}
	

			for (var i = 0; i < tablas[0][1].length; i++) {
				edges.add({from: tablas[0][1][i].nombreTabla, to: tablas[0][1][i].nombreLlaveForanea, label: 'fk',arrows:'to', font:{align:'llave_foranea'}});
				allEdges++;
				numero_fk++;

				//archivo
				if(intContadorTablas==0){
					infoArray.push([[tablas[0][1][i].nombreTabla,tablas[0][1][i].nombreLlaveForanea ]]);
					nodosPrueba.push(tablas[0][1][i].nombreTabla);
					edgesPrueba.push(tablas[0][1][i].nombreLlaveForanea);
					colorTablas.push(tablas[0][1][i].nombreTabla);
					colorTablas.push(tablas[0][1][i].nombreLlaveForanea);

				}

			}
			intContadorTablas++;



			allNodes =  nodes.get({returnType:"Object"});

			// numero de nodos
			$("#numero_nodos").text(Object.keys(nodes.get({returnType:"Object"})).length);
			$("#numero_edges").text(allEdges);

			$("#numero_tablas").text(numero_tablas);
			$("#numero_fk").text(numero_fk);



		});

		tablas_activas=true;




	}

}

var numero_vistas = 0;
var numero_select_vistas =0;
function obtener_vistas(){

	if (!vistas_activas) {


		$.ajax({

			url: "../controller/clienteRest/getVistas"

		}).then(function(vistas) {



			// si las tablas no están pintadas, se grafican las tablas y las vistas.
			if (!tablas_activas) {

				for (var i = 0; i < vistas.length; i++) {

					if (!nodes.get(vistas[i].nombre_vista)) {
						nodes.add({id:vistas[i].nombre_vista, label:vistas[i].nombre_vista, group:'vistas', level:2});
						filtro_items.push(vistas[i].nombre_vista);
						numero_vistas++;
					}
					if (!nodes.get(vistas[i].tabla_relacionada)) {
						nodes.add({id:vistas[i].tabla_relacionada, label:vistas[i].tabla_relacionada, group:'tablas', level:1});
						filtro_items.push(vistas[i].tabla_relacionada);
						numero_tablas++;
					}
				}	

				// si ya están las tablas pintadas, sólo se grafica las vistas
			}else{

				for (var i = 0; i < vistas.length; i++) {

					if (!nodes.get(vistas[i].nombre_vista)) {
						nodes.add({id:vistas[i].nombre_vista, label:vistas[i].nombre_vista, group:'vistas', level:2});
						filtro_items.push(vistas[i].nombre_vista);

						// suma el numero de vistas en la pantalla
						numero_vistas++;
					}
				}
			}


			for (var i = 0; i < vistas.length; i++) {
				edges.add({from: vistas[i].nombre_vista, to: vistas[i].tabla_relacionada, label: 'vistas',arrows:'to', font:{align:'llave_foranea'}});
				allEdges++;
				numero_select_vistas++;

				if(intContadorVistas==0){
					infoArray.push([[vistas[i].nombre_vista,vistas[i].tabla_relacionada ]]);
					nodosPrueba.push(vistas[i].nombre_vista);
					edgesPrueba.push(vistas[i].tabla_relacionada);
					colorVistas.push(vistas[i].nombre_vista);

				}

			}

			intContadorVistas++;

			allNodes =  nodes.get({returnType:"Object"});


			// numero de nodos
			$("#numero_nodos").text(Object.keys(nodes.get({returnType:"Object"})).length);
			$("#numero_edges").text(allEdges);

			$("#numero_tablas").text(numero_tablas);
			$("#numero_vistas").text(numero_vistas);
			$("#numero_select_vistas").text(numero_select_vistas);


		});




		vistas_activas=true;

	}

}

var numero_funciones = 0;
var numero_select_funciones =0;
var numero_update_funciones =0;
var numero_delete_funciones=0;
var numero_insert_funciones =0;

function obtener_funciones(){

	if (!funciones_activas) {




		$.ajax({

			url: "../controller/clienteRest/getFunciones"

		}).then(function(funciones) {


			// si las tablas no están pintadas, se grafican las tablas y las funciones.
			if (!tablas_activas) {



				for (var i = 0; i < funciones.length; i++) {


					if (!nodes.get(funciones[i].nombre_funcion)) {
						nodes.add({id:funciones[i].nombre_funcion, label:funciones[i].nombre_funcion, group:'funciones', level:3});
						filtro_items.push(funciones[i].nombre_funcion);
						numero_funciones++;

					}


					if (!nodes.get(funciones[i].nombre_tabla)) {
						nodes.add({id:funciones[i].nombre_tabla, label:funciones[i].nombre_tabla, group:'tablas', level:1});
						filtro_items.push(funciones[i].nombre_tabla);
						numero_tablas++;

					}


				}

				// si ya están las tablas pintadas, sólo se grafica las funciones
			}	else{


				for (var i = 0; i < funciones.length; i++) {
					if (!nodes.get(funciones[i].nombre_funcion)) {
						nodes.add({id:funciones[i].nombre_funcion, label:funciones[i].nombre_funcion, group:'funciones', level:3});
						filtro_items.push(funciones[i].nombre_funcion);

						// se suman el numero de nodos de funciones en pantalla
						numero_funciones++;
					}
				}
			}

			for (var i = 0; i < funciones.length; i++) {
				edges.add({from: funciones[i].nombre_funcion, to: funciones[i].nombre_tabla, label:funciones[i].nombre_relacion ,arrows:'to', font:{align:'llave_foranea'}});
				allEdges++;
				if (funciones[i].nombre_relacion=="Select") {
					numero_select_funciones++;
				}
				if (funciones[i].nombre_relacion=="Update") {
					numero_update_funciones++;
				}
				if (funciones[i].nombre_relacion=="Insert") {
					numero_insert_funciones++;
				}
				if (funciones[i].nombre_relacion=="Delete") {
					numero_delete_funciones++;
				}

				// archivo
				if(intContadorFunciones==0){
					infoArray.push([[funciones[i].nombre_funcion,funciones[i].nombre_tabla ]]);
					nodosPrueba.push(funciones[i].nombre_funcion);
					edgesPrueba.push(funciones[i].nombre_tabla);
					colorFunciones.push(funciones[i].nombre_funcion);


				}
			}
			intContadorFunciones++;

			allNodes =  nodes.get({returnType:"Object"});


			// numero de nodos
			$("#numero_nodos").text(Object.keys(nodes.get({returnType:"Object"})).length);
			$("#numero_edges").text(allEdges);


			$("#numero_tablas").text(numero_tablas);
			$("#numero_funciones").text(numero_funciones);

			$("#numero_select_funciones").text(numero_select_funciones);
			$("#numero_update_funciones").text(numero_update_funciones);
			$("#numero_delete_funciones").text(numero_delete_funciones);
			$("#numero_insert_funciones").text(numero_insert_funciones);

		});

		funciones_activas=true;



	}


}


var numero_triggers = 0;
var numero_select_triggers =0;
var numero_update_triggers =0;
var numero_delete_triggers =0;
var numero_insert_triggers =0;
function obtener_triggers(){

	if (!triggers_activos) {

		$.ajax({

			url: "../controller/clienteRest/getTriggers"

		}).then(function(triggers) {


			// si las tablas no están pintadas, se grafican las tablas y los triggers.
			if (!tablas_activas) {

				for (var i = 0; i < triggers.length; i++) {


					if (!nodes.get(triggers[i].nombre_trigger)) {
						nodes.add({id:triggers[i].nombre_trigger, label:triggers[i].nombre_trigger, group:'triggers', level:4});
						filtro_items.push(triggers[i].nombre_trigger);
						numero_triggers++;
					}


					if (!nodes.get(triggers[i].nombre_tabla)) {
						nodes.add({id:triggers[i].nombre_tabla, label:triggers[i].nombre_tabla, group:'tablas', level:1});
						filtro_items.push(triggers[i].nombre_tabla);
						numero_tablas++;

					}


				}

				// si ya están las tablas pintadas, sólo se grafica los triggers
			}	else{


				for (var i = 0; i < triggers.length; i++) {
					if (!nodes.get(triggers[i].nombre_trigger)) {
						nodes.add({id:triggers[i].nombre_trigger, label:triggers[i].nombre_trigger, group:'triggers', level:4});
						filtro_items.push(triggers[i].nombre_trigger);

						// se suman el numero de nodos de funciones en pantalla
						numero_triggers++;
					}
				}
			}

			for (var i = 0; i < triggers.length; i++) {
				edges.add({from: triggers[i].nombre_trigger, to: triggers[i].nombre_tabla, label:triggers[i].nombre_relacion ,arrows:'to', font:{align:'llave_foranea'}});
				allEdges++;
				if (triggers[i].nombre_relacion=="SELECT") {
					numero_select_triggers++;
				}
				if (triggers[i].nombre_relacion=="UPDATE") {
					numero_update_triggers++;
				}
				if (triggers[i].nombre_relacion=="INSERT") {
					numero_insert_triggers++;
				}
				if (triggers[i].nombre_relacion=="DELETE") {
					numero_delete_triggers++;
				}

				// archivo
				if(intContadorTriggers==0){
					infoArray.push([[triggers[i].nombre_trigger,triggers[i].nombre_tabla ]]);
					nodosPrueba.push(triggers[i].nombre_trigger);
					edgesPrueba.push(triggers[i].nombre_tabla);
					colorTriggers.push(triggers[i].nombre_trigger);
				}
			}
			intContadorTriggers++;

			allNodes =  nodes.get({returnType:"Object"});
			
			console.log(allNodes)


			// numero de nodos
			$("#numero_nodos").text(Object.keys(nodes.get({returnType:"Object"})).length);
			$("#numero_edges").text(allEdges);


			$("#numero_tablas").text(numero_tablas);
			$("#numero_triggers").text(numero_triggers);

			$("#numero_select_triggers").text(numero_select_triggers);
			$("#numero_update_triggers").text(numero_update_triggers);
			$("#numero_delete_triggers").text(numero_delete_triggers);
			$("#numero_insert_triggers").text(numero_insert_triggers);

		});

		triggers_activos=true;



	}


}

function graficar_todo(){
	obtener_tablas();
	obtener_vistas();
	obtener_triggers();
	obtener_funciones();
}






//recive el id del nodo y lo busca en la pantalla
function focus_node(nodo){

	options={
			scale:1.0,
			offset:{x:0,y:0},
			animation:{
				duration:1000,		
			}
	};

	network.focus(nodo,options);
	network.selectNodes([nodo]);

	//	mostrar_informacion_nodo(nodo);

}


//search
function autocompletar() {

	var items  = filtro_items;

	$( "#tags" ).autocomplete({
		source: items,
		messages: {
			noResults: '',
			results: function() {}
		},
		select: function(event, ui) {

			if (nodes.get(ui.item.label)) {

				focus_node(ui.item.label);
			}
		}
	});

// http://dinu.github.io/bootstrap-chosen/

	$( "#boton_buscar" ).click(function() {
		if (nodes.get($( "#tags" ).val().trim())) {

			//funcion
			focus_node($( "#tags" ).val().toString());
		}

	});
	$("#tags").keypress(function (e) {

		if (nodes.get($( "#tags" ).val().trim())) {
			//funcion
			focus_node($( "#tags" ).val().toString());
		}

	});

}


autocompletar();
function mostrar_informacion_nodo(params){

	var numero_tablas_nodo_selec = 0;
	var numero_vistas_nodo_selec = 0;
	var numero_funciones_nodo_selec = 0;
	var numero_triggers_nodo_selec = 0;


	try {

		var nodos_conectados = network.getConnectedNodes(nodes._data[params.nodes.toString()].id);


		$( "#list-tablas-side li" ).remove();
		$( "#list-vistas-side li" ).remove();
		$( "#list-funciones-side li" ).remove();
		$( "#list-triggers-side li" ).remove();

		for (var i = 0; i < nodos_conectados.length; i++) {


			var edges_label  = getEdgesDelNodo(nodes._data[nodos_conectados[i]].id);
			if (nodes._data[nodos_conectados[i]].group==="tablas") {

				numero_tablas_nodo_selec++;




				$("#list-tablas-side").append(

						"<li class='list-group-item'>"+ nodos_conectados[i] +"<br> <small>"+ edges_label +"</small></li>"


				);

			}
			if (nodes._data[nodos_conectados[i]].group==="vistas") {

				numero_vistas_nodo_selec++;


				$("#list-vistas-side").append(

						"<li class='list-group-item'>"+ nodos_conectados[i] +"<br> <small>"+ edges_label +"</small></li>"

				);

			}
			if (nodes._data[nodos_conectados[i]].group==="funciones") {

				numero_funciones_nodo_selec++;


				$("#list-funciones-side").append(

						"<li class='list-group-item'>"+ nodos_conectados[i] +"<br> <small>"+ edges_label +"</small></li>"

				);

			}

			if (nodes._data[nodos_conectados[i]].group==="triggers") {

				numero_triggers_nodo_selec++;


				$("#list-funciones-side").append(

						"<li class='list-group-item'>"+ nodos_conectados[i] +"<br> <small>"+ edges_label +"</small></li>"

				);

			}

		}

		$("#numero_tablas_badge").text(numero_tablas_nodo_selec);
		$("#numero_vistas_badge").text(numero_vistas_nodo_selec);
		$("#numero_funciones_badge").text(numero_funciones_nodo_selec);
		$("#numero_triggers_badge").text(numero_triggers_nodo_selec);
	}
	catch(err) {

		console.log(err)


	}



}
//recuperar edge del nodo
function getEdgesDelNodo(nodeId) {

	var edges_labels = getEdgesOfNode(nodeId) ;

	var aux = " ";
	for (var int = 0; int < edges_labels.length; int++) {

		aux +=edges_labels[int].label + " ";
	}


	return aux;    


}

function getEdgesOfNode(nodeId) {
	return edges_side.get().filter(function (edge) {
		return edge.from === nodeId || edge.to === nodeId;
	});
}

function centrar_grafo(){

	network.fit(nodes);
	//	networkSide.zoomExtend();
}


//exportar


var colorTablas=[];
var colorVistas=[];
var colorFunciones=[];
var colorTriggers=[];

var nodosPrueba=[];
var edgesPrueba=[];
var infoArray = [];


var intContadorTablas=0;
var intContadorVistas=0;
var intContadorFunciones=0
var intContadorTriggers=0;

function descargarArchivo(contenidoEnBlob, nombreArchivo) {

	if(colorTablas.length>0 || colorVistas.length>0 || colorFunciones.length>0 || colorTriggers.length>0){
		//creamos un FileReader para leer el Blob
		var reader = new FileReader();
		//Definimos la función que manejará el archivo
		//una vez haya terminado de leerlo
		reader.onload = function (event) {
			//Usaremos un link para iniciar la descarga 
			var save = document.createElement('a');
			save.href = event.target.result;
			save.target = '_blank';
			//Truco: así le damos el nombre al archivo 
			save.download = nombreArchivo || 'grafo.graphML';
			var clicEvent = new MouseEvent('click', {
				'view': window,
				'bubbles': true,
				'cancelable': true
			});
			//Simulamos un clic del usuario
			//no es necesario agregar el link al DOM.
			save.dispatchEvent(clicEvent);
			//Y liberamos recursos...
			(window.URL || window.webkitURL).revokeObjectURL(save.href);
		};
		//Leemos el blob y esperamos a que dispare el evento "load";
		reader.readAsDataURL(contenidoEnBlob);

	}
	else {
		alert("Debe graficar algo");
	}

};

function generarXml() {
	var texto = [];
	texto.push('<?xml version="1.0" encoding="UTF-8"?>');
	texto.push('<graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">');
	texto.push('<key id="d0" for="node" attr.name="color" attr.type="string">');
	texto.push('<default>yellow</default>');
	texto.push('</key>');
	texto.push('<key id="d1" for="edge" attr.name="weight" attr.type="double"/>');
	texto.push('<graph id="G" edgedefault="directed">');	
	for (var i=0; i<colorTablas.length; i++){
		texto.push('<node id="'+colorTablas[i]+'"><data key="d0">red</data></node>'); 
	}
	for (var j=0; j<colorVistas.length; j++){
		texto.push('<node id="'+colorVistas[j]+'"><data key="d0">blue</data></node>'); 
	}
	for (var x=0; x<colorFunciones.length; x++){
		texto.push('<node id="'+colorFunciones[x]+'"><data key="d0">green</data></node>'); 
	}

	for (var i = 0; i < colorTriggers.length; i++) {
		texto.push('<node id="'+colorTriggers[i]+'"><data key="d0">yellow</data></node>'); 
	}
	for (var we=0; we<edgesPrueba.length; we++){
		texto.push('<node id="'+edgesPrueba[we]+'"><data key="d0">red</data></node>'); 
	}
	for (var w=0 ; w<nodosPrueba.length; w++){
		texto.push('<edge id="e'+w+'" source="'+nodosPrueba[w]+'" target="'+edgesPrueba[w]+'"/>');
	}


	texto.push('</graph>');
	texto.push('</graphml>');
	//No olvidemos especificar el tipo MIME correcto 
	return new Blob(texto, {
		type: 'application/graphML'
	});


};
window.addEventListener('DOMContentLoaded', function(){

	document.getElementById('boton-xml').addEventListener('click', function () {

		descargarArchivo(generarXml(), 'grafo.graphML');
	}, false);

});



function convertArrayOfObjectsToCSV(args) {
	var result, ctr, keys, columnDelimiter, lineDelimiter, data;

	data = args.data || null;
	if (data == null || !data.length) {
		return null;
	}

	columnDelimiter = args.columnDelimiter || ',';
	lineDelimiter = args.lineDelimiter || '\n';

	keys = Object.keys(data[0]);

	result = '';
	result += lineDelimiter;

	data.forEach(function(item) {
		ctr = 0;
		keys.forEach(function(key) {
			if (ctr > 0) result += columnDelimiter;

			result += item[key];
			ctr++;
		});
		result += lineDelimiter;
	});

	return result;
}

function downloadCSV(args) {

	if(infoArray.length>0){
		var data, filename, link;

		var csv = convertArrayOfObjectsToCSV({
			data: infoArray
		});
		if (csv == null) return;

		filename = args.filename || 'export.csv';

		if (!csv.match(/^data:text\/csv/i)) {
			csv = 'data:text/csv;charset=utf-8,' + csv;
		}
		data = encodeURI(csv);

		link = document.createElement('a');
		link.setAttribute('href', data);
		link.setAttribute('download', filename);
		link.click();

	}
	else {
		alert("Debe graficar algo");
	}

}


