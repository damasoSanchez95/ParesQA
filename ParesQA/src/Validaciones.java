import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import Excepciones.*; 

public class Validaciones {
	
	
	private ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private ArrayList<AbstractTransformation> listaTransformaciones= new ArrayList<AbstractTransformation>();
	private ArrayList<Iobject> listaObjetos = new ArrayList<Iobject>();
	private ArrayList<DataRecord> listaDataRecords= new ArrayList<DataRecord>();
	private HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();
	private HashMap<String, String> tablaExecutionParameters = new HashMap<String, String>();
	private final static Logger LOGGER = Logger.getLogger("default.LeerFichero");
	private boolean descripcionMapping;
	
	
	//Constructor Validaciones
	public Validaciones(boolean descripcionMapping, ArrayList<Instance> listaInstancia, ArrayList<AbstractTransformation> listaTransformaciones , HashMap<Parametro, String> tablaParametros, ArrayList<Iobject> listaObjetos, HashMap<String, String> tablaExecutionParameters, ArrayList<DataRecord> listaDataRecords){
		this.listaInstancia=listaInstancia;
		this.listaTransformaciones=listaTransformaciones;
		this.tablaParametros=tablaParametros;
		this.listaObjetos=listaObjetos;
		this.tablaExecutionParameters=tablaExecutionParameters;
		this.descripcionMapping = descripcionMapping;
		this.listaDataRecords=listaDataRecords;
	}
	
	public void parametros(){
		try {
			for (Entry<Parametro, String> entry : tablaParametros.entrySet()) {
			    Parametro clave = entry.getKey();
			    String valor = entry.getValue();
			    
			    if(clave.getName().equals("P_s_FechaEjecucion") && !valor.equals("9999-12-31"))
			    	throw new FechaEjecucionFallo("El parametro P_s_FechaEjecucion no tiene puesto el valor 9999-12-31");
			}
		}catch(FechaEjecucionFallo e) {
			System.out.println(e.getLocalizedMessage());
		}		
	}
	public void descripcionMapping(){
		try{
		if(!descripcionMapping)
			throw new DescripcionMapping("EL MAPPING QUE ESTÁ COMPROBANDO NO TIENE DESCRICPCION");	
		}catch(DescripcionMapping e){
			System.out.println(e.getLocalizedMessage());
			}
		}
	
	public void executionParameters(){
		for (Entry<Parametro, String> entry : tablaParametros.entrySet()) {
			Parametro clave = entry.getKey();
			try {
				if( (!tablaExecutionParameters.containsKey(clave.getId())) && (clave.getName().contains("DRIVER") || clave.getName().contains("EXECUTOR")))
					throw new SinParametroPre("NO ESTA EL PARAMETRO " + clave.getName() + " DENTRO DE LOS TIEMPO DE EJECUCION");
			}catch(SinParametroPre e){
					System.out.println(e.getLocalizedMessage());
				}
			}
		}
	
	public void ValidacionesPuertos(){

		boolean cambiado=false;
		boolean toPortsEsNull=false;
		boolean transformacionEncontrada=false;
		boolean puertoCorrecto = false;
		boolean encontradoDataRecords=false;
		boolean instanciaEncontrada=false;
		boolean tipo_idRef[]= new boolean[2];	
		boolean campoCorrecto=false;
		boolean objetoMapping=false;
		boolean comprobado=false;
		boolean escritura=false;

		int InstanciaPartida=0;//declaramos variables que sera el indice de instancia de partida y de final
		int InstanciaDestino=0;

		//Declaramos los objetos
		ArrayList<ArrayList<String>> tablaComparacionCampos = new ArrayList<ArrayList<String>>();
		DataRecord nuevaDataRecord = new DataRecord(null,null, null);
		Iobject nuevoObjeto = new Iobject(null,null,null,tipo_idRef[1],null);	
		Instance nuevaInstancia = new Instance(null,null,null,null,null, null, null);
		AbstractTransformation nuevaTransformacion = new AbstractTransformation(null,null,null, null,null,null);

		//Iteradores para cada una de las listas
		Iterator<DataRecord> iteradorDataRecords;

		//ITERADORES PARA RECORRER LAS LISTAS CORRESPONDIENTES
		iteradorDataRecords = listaDataRecords.iterator(); 

		//For Externo en el que vamos recorriendo los objetos
		for(int indiceObjeto=0; indiceObjeto<listaObjetos.size(); indiceObjeto++){
			 tablaComparacionCampos = new ArrayList<ArrayList<String>>();
			objetoMapping=false;
			escritura=false;
			//lo primero habra que analizar los datos de los objetos para ver si tienen realacion con las intancias y las transformaciones.

			//cogemos la ref de cada objeto ->Si es true habra que coger el id y buscar si hay algun id igual en algun otro objeto, Si no el nombre y empezar con las comparaciones
			nuevoObjeto = listaObjetos.get(indiceObjeto);

			if(nuevoObjeto.getType().contains("mapping")) //si es el objeto del mapping queremos pasar de él
				objetoMapping=true;

			if(nuevoObjeto.getRef() && nuevoObjeto.getType().contains("datarecord")){ //vemos si es un objeto con id de referencia y que sea un datarecord, si es el mapping pasamos de el
				while(iteradorDataRecords.hasNext() && !encontradoDataRecords){
					nuevaDataRecord=iteradorDataRecords.next();
					if(nuevaDataRecord.getId().equals(nuevoObjeto.getId()))
						encontradoDataRecords=true;
				}

				if(!encontradoDataRecords) //HABRA QUE HACER UNA EXCEPCION (NO DEBERIA ENTRAR NUNCA)
					System.out.println("FALLO, NO HEMOS ENCONTRADO ESE DATARECORDS CUANDO ES UN ID DE REFERENCIA A ÉL");
				else{ //COMO DATARECORDS ES IGUAL QUE UN OBJETO IGUALOS SUS CAMPOS, PARA DESPUES JUGAR CON ÉL DIRECTAMENTE
					nuevoObjeto.setName(nuevaDataRecord.getName());
					nuevoObjeto.setCampos(nuevaDataRecord.getCampos());	
				}	
			}


			//AQUI SEA UN OBJETO DE REFERENCIA O UNO NORMAL LLEGAMOS CON EL NOMBRE Y LA LISTA DE CAMPOS CORRESPONDIENTE

			//Entonces cogemos el name y debemos buscar que nombre de las instancias CONTIENE ese nombre porque tendra "Lectura_" y no sera exactamente igual
			//hay que comparar si es de lectura o escritura pero solo se sabe al compararlo con el nombre de la instancia

			if(!objetoMapping){
				for(int indiceInstancia=0; indiceInstancia<listaInstancia.size() && !escritura; indiceInstancia++){ //Bucle de instancias, para buscar por que instancia empiezas
					nuevaInstancia=listaInstancia.get(indiceInstancia);
					if(nuevaInstancia.getName().contains(nuevoObjeto.getName())){ //DE LA LISTA DE INSTANCIAS SOLO HABRA UNO QUE CONTENGA ESE NOMBRE, LA INSTANCIA DEL OBJETO
						//el nombre existe por lo que la instancia esta bien
						instanciaEncontrada=true;

						instanciaEncontrada=false;
						InstanciaPartida=indiceInstancia;//guardamos la posicion de la instancia de partida del array de instancias
						String NombreAComprobar=listaInstancia.get(indiceInstancia).getName();

						while(!NombreAComprobar.contains("FINAL")){
							//si no entra en el while significa que hemos cogido un objeto con escritura o hemos llegado a la ultima instancia que es la de escritura	
							//necesitamos coger el toInstance de esa instancia
							
							if(!NombreAComprobar.contains("Escritura")){
							
								String siguienteId=listaInstancia.get(InstanciaPartida).getToInstance();//importante ya que lo tendremos en cuenta con los toPort y FromPort
	
								//ESTE FOR SIRVE PARA SACAR LA INSTANCIA DESTINO
								for(int k=0; k<listaInstancia.size() &&  !instanciaEncontrada; k++){
									//habra que comparar el toInstance con el id de la siguiente instancia
									if(siguienteId.equals(listaInstancia.get(k).getId())){
										InstanciaDestino=k;//guardamos la posicion  de la instancia de destino del array de instancias						
										instanciaEncontrada=true;
									}
								}
							}

							//Aqui busco su la transformacion que le corresponda a esta instancia
							int posicionTransformacion=0;
							instanciaEncontrada=false;

							for(int t=0; t<listaTransformaciones.size() && !transformacionEncontrada; t++)
								if(listaTransformaciones.get(t).getNombre().contains(listaInstancia.get(InstanciaPartida).getName())) {
									posicionTransformacion=t;//cojemos la posicion de la transformacion en la lista de transformaciones
 									transformacionEncontrada=true;
								}
							//AQUI TENEMOS LAS DOS INSTANCIAS (LA ACTUAL Y LA SIGUIENTE) DE LAS CUALES HAY QUE COMPROBAR SUS PUERTOS Y TAMBIEN LA POSICION DE LA TRANSFORMACION QUE LE CORRESPONDA.
							
							transformacionEncontrada=false;
							
							//CASO DE LECTURA
							if(NombreAComprobar.contains("Lectura")){
								//AQUI COMPROBAMOS QUE LOS CAMPOS SON CORRECTOS Y EXISTEN EN LA 2 INSTANCIA, PERO NO ESTAMOS COMPARANDO LOS PUERTOS AUN
								int ContadorComprobaciones=0; //Contador de arrayList, hay un arrayList de campos
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){	 //For para recorrer todos los campos de la instancia de origen					
									campoCorrecto=false;
									for(int indiceCampoDeLaInstanciaDestino=0; indiceCampoDeLaInstanciaDestino<listaInstancia.get(InstanciaDestino).getCampos().size() && !campoCorrecto; indiceCampoDeLaInstanciaDestino++){ //For para recorrer todos los campos de la instancia de destino
										//aqui recorremos los campos de la instacia DE LLEGADA 
										if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId().equals(listaInstancia.get(InstanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getFromPorts()) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts().equals(listaInstancia.get(InstanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getId()))
											campoCorrecto=true;
									}

									if(!campoCorrecto)
										System.out.println("El id " + listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId()+ " de la instancia de partida no se encuentra EN LA INSTANCIA DESTINO");

									else{
										//creamos un nuevo arraylista dentro del ya existente cada arraylist será un CAMPO con LOS PUERTOS(toPort,precision,tipo,escala, NAME)
										tablaComparacionCampos.add(new ArrayList<String>());
										tablaComparacionCampos.get(ContadorComprobaciones).add(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts());
										//metemos el dato y en comprobacion feature meteremos todos los demas campos al hacerl as comprobaciones 
										ComprobacionFeatures(InstanciaPartida,indiceObjeto,indiceCampoDeLaInstanciaPartida,tablaComparacionCampos,ContadorComprobaciones,posicionTransformacion);
										ContadorComprobaciones++;//sumamos uno al contador del arraylist general para que al siguiente paso del bucle se cree otro ARRAYLIST PARA OTRO CAMPO
									}
								}
							} //RECORREMOS TODOS LOS CAMPOS Y VAMOS CREANDO ARRAYLIST CON LOS PUERTOS DE CADA CAMPO 

							
							//CASO DE EXPRESION O JNR
							else if(NombreAComprobar.contains("exp") || NombreAComprobar.contains("jnr")){	
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){
									campoCorrecto=false;
									toPortsEsNull=false;
									comprobado=false;
									//en la primera parte comparamos los campos en la siguiente los puertos y en la tercera el añadido de un nuevo campo (solo en las expresiones, xq es por si hemos creado un nuevo campo)

									//1º PARTE (AQUI VEMOS SI LOS CAMPOS DE LA INSTANCIA PARTIDA ESTAN EN LA ISNTANCIA DESTINO O NO)
									for(int indiceCampoDeLaInstanciaDestino=0; indiceCampoDeLaInstanciaDestino<listaInstancia.get(InstanciaDestino).getCampos().size() && !campoCorrecto && !toPortsEsNull; indiceCampoDeLaInstanciaDestino++){
										//aqui recorremos los campos de la instacia DE LLEGADA 
										//HABRA QUE TENER EN CUENTA COMO HACIA EN PHP QUE ALOMEJOR PETA PORQUE AUNQUE SE SALGA DEL IF INTENTA PILLAR EL TOPORT Y NO EXISTE Y HABRA QUE METER UN IF PREVIO
										if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null) {
											if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId().equals(listaInstancia.get(InstanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getFromPorts()) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts().equals(listaInstancia.get(InstanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getId()))
												campoCorrecto=true; //te dice si el campo de la instanciaOrigen esta en la instanciaDestino, que siempre deberia de estar
										}
										else
											toPortsEsNull=true;	
									}

									//Deberia entrar en los campos que no te lleves a la instancia destino.
									if(!campoCorrecto)
										System.out.println("El id " + listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId()+ " de la instancia de partida no se encuentra en la de destino");

									//2º PARTE
									boolean NuevoCampoo=true;//solo es util para la tercera parte SOLO DE EXP
									for(int posicionArrayCampos=0; posicionArrayCampos<tablaComparacionCampos.size();posicionArrayCampos++){							
										//COMPARAS EL ID CON EL TO_PORT DEL ANTERIOR
										if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId().equals(tablaComparacionCampos.get(posicionArrayCampos).get(0)) && !comprobado){																							
											if(NombreAComprobar.contains("jnr")) {
												ComprobacionCamposJoinUnion(InstanciaPartida, indiceCampoDeLaInstanciaPartida, tablaComparacionCampos, posicionArrayCampos, posicionTransformacion);
												NuevoCampoo=false;//solo es util para la tercera parte SOLO DE EXP
												comprobado=true;
											}
											else if(NombreAComprobar.contains("exp"))
												NuevoCampoo=ComprobacionCamposExpresionFiltro(InstanciaPartida, indiceCampoDeLaInstanciaPartida, tablaComparacionCampos, posicionArrayCampos, posicionTransformacion);

										}
									}
									//3º PARTE SOLO PARA LAS EXCEPCIONES Y PORQUE SIGNIFICA QUE VA A SER UN CAMPO NUEVO					
									if(NombreAComprobar.contains("exp") && NuevoCampoo && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null){													
										boolean encontrado=false;
										ArrayList<String> nuevo = new ArrayList<String>();
										nuevo.add(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts());

										for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !encontrado;t++){														
											if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getId().equals(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField())){ 
												nuevo.add(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision());
												nuevo.add(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType());
												
												if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().contains("decimal"))
													nuevo.add(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getEscala());		
												
												nuevo.add(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getName());	
												encontrado=true;											}			
										}		
										tablaComparacionCampos.add(nuevo);
									}	
								}
							}

							//CASO DE ESCRITURA
							else if(NombreAComprobar.contains("Escritura")) {
								escritura=true;

								//en la instancia estan solo los q se han conectado, en el objeto todos.
								if(listaInstancia.get(InstanciaPartida).getName().contains(listaObjetos.get(indiceObjeto).getName())){

									if(listaInstancia.get(InstanciaPartida).getCampos().size() != listaObjetos.get(indiceObjeto).getCampos().size())
										System.out.println("Algun campo de la tabla de escritura esta sin flechita tronco");

									else{ //cuando has llegado aqui y aun no estas en el objeto de  escritura, sino que solo estas en la instancia escritura

										//Compruebas del campo de su instancia (sus puertos) con los puertos del campo de su propio objeto (INNECESARIO)
										for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++)
											ComprobacionFeatures(InstanciaPartida,indiceObjeto,indiceCampoDeLaInstanciaPartida,tablaComparacionCampos,0,posicionTransformacion);
									}
								}
								
								//COMPRUEBAS LOS CAMPOS CON LO DE LA TRANSFORMACION ANTERIOR
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){
									for(int posicionArrayCampos=0; posicionArrayCampos<tablaComparacionCampos.size();posicionArrayCampos++){							
										if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId()==tablaComparacionCampos.get(posicionArrayCampos).get(0))																					
											ComprobacionCamposExpresionFiltro(InstanciaPartida,indiceCampoDeLaInstanciaPartida,tablaComparacionCampos,posicionArrayCampos,posicionTransformacion);
									}
								}
								NombreAComprobar="FINAL";
							}
							
							

							//SI LLEGAMOS AQUI SIGNIFICA QUE HEMOS TERMINADO DE RECORRER LOS PUERTOS DE LA INSTANCIA DE PARTIDA Y COMPARARLO TANTO ELLOS COMO LOS STRUCTURAL FEATURE CON SUS CORRESPONDIENTES CAMPOS
							//Guardamos en name de la siguiente instancia en NombreAComprobar comprobando si hemos llegado a escritura y cambiamos el valor de la instancia partida que es la actual instancia siguiente
							if(!escritura) {
								NombreAComprobar=listaInstancia.get(InstanciaDestino).getName();//aqui guardamos el nombre de la siguiente instancia en el nombre a comprobar
								InstanciaPartida=InstanciaDestino;
							}
						}				
					}					
				}
			}
		}
	}
	
	private boolean comprobacionesParaLosPuertosTransformacionesObjetos(int posicionTransformacion, int posicionCampoTransformacion, int indiceObjeto, int posicionCampoObjeto){
		if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType().contains("decimal"))
			return listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getPrecision().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getPrecision())&&listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType())&&listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getEscala().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getEscala());
		else
			return listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getPrecision().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getPrecision())&&listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType());
	}
	
	
	//ESTE METODO DEBERIA IR SIEMPRE BIEN YA QUE ESTAS COMPROBANDO LOS CAMPOS DEL OBJETO CON SU PROPIA TRANSFORMACION
	public void ComprobacionFeatures(int InstanciaPartida,int indiceObjeto,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int ContadorComprobaciones, int posicionTransformacion){
		//comprobamos si el nombre de la transformacion concuerda con el del objeto
		boolean campoComprobado=false;
		boolean campoEncontrado=false;
		boolean campoCorrecto=false;
		int posicionCampoTransformacion;

		//En este bucle recorres todos los campos de la transformacion en cuestion
		for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !campoComprobado;t++){
			campoCorrecto=false;
			//SIEMPRE DEBERIA ENTRAR EN ESTE IF XQ UN OBJETO SIEMPRE DEBERIA TENER SU PROPIA TRANSFORMACION
			if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getStructural_feature().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getFeature())){
				//SI LLEGAMOS AQUI SIGNIFICA QUE HAY RELACION ENTRE LA INSTANCIA Y LA TRANSFORMACION
				posicionCampoTransformacion = t;
				//ahora deberemos comparar column, nombre,precision y tipo con el objeto para terminar de dar la vuelta a las comprobaciones
					for(int posicionCampoObjeto=0; posicionCampoObjeto<listaObjetos.get(indiceObjeto).getCampos().size() && !campoComprobado;posicionCampoObjeto++){ //RECORRES TODOS LOS CAMPOS DEL OBJETO
						//debemos compararlo con las columna del objeto donde estamos NO DE OTROS OBJETOS
						if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getColumna().equals(listaObjetos.get(InstanciaPartida).getCampos().get(posicionCampoObjeto).getId()) && comprobacionesParaLosPuertosTransformacionesObjetos(posicionTransformacion,posicionCampoTransformacion,indiceObjeto,posicionCampoObjeto)){
							//Aqui comprobamos si tanto la columna,nombre,precision,tipo, nullable y scala coinciden con el objeto 
							//ATENCION CON LA ESCALA QUE HABRA QUE COMPROBAR QUE NO SEA NULL
							campoCorrecto=true;
							if(!listaTransformaciones.get(indiceObjeto).getNombre().contains("Escritura") && campoCorrecto){
								//una vez que se han comrpboado los campos y estan correctos y la instancia no es de escritura metermos los campos de precision tipo y escala en el arraylist dentro del array list (consultar dibujo para dudas)
								tablaComparacionCampos.get(ContadorComprobaciones).add(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getPrecision());
								tablaComparacionCampos.get(ContadorComprobaciones).add(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType());
								
								if(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType().contains("decimal"))
									tablaComparacionCampos.get(ContadorComprobaciones).add(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getEscala());
								
								tablaComparacionCampos.get(ContadorComprobaciones).add(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getName());
							}
							campoComprobado=true;
						}
					}
				
				//NO DEBERIA ENTRAR NUNCA
				if(!campoCorrecto)
					System.out.println("Algun campo no concuerda del colum cuyo id es: " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getColumna() + " y cuyo name es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getName());				
			}
		}
	}
	
	public boolean ComprobacionCamposExpresionFiltro(int InstanciaPartida,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int posicionArrayCampos,int posicionTransformacion){
		boolean campoCorrecto=false;
		boolean encontrado=false;
		boolean nuevoCampo=true;
		
		
			for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !encontrado;t++){
				if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getId())) {
					if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().contains("decimal")) {
						if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)) /*&& (listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getEscala().equals(tablaComparacionCampos.get(posicionArrayCampos).get(3)))*/))
							//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
							campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		

					}
					else{
						if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))))
							campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
					}
							
					
					encontrado=true;
					}
			}
			
		
		if(!campoCorrecto)
			System.out.println("La has cagado en el campo " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1) + " debes revisarlo y volver a pasar el XML al programa!!");
		
		if(campoCorrecto){
			//si el campo esta ok, pasamos a actualizar el arraylist y ahora tendra un nuevo toPort este campo en el caso de las transformaciones.

			//******************************ESTO SOLO SI ES EXPRESION******************************
			//ACTUALIZAMOS EL TOPORTS

				if(listaTransformaciones.get(posicionTransformacion).getNombre().contains("exp") && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null){
					tablaComparacionCampos.get(posicionArrayCampos).set(0,listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts());
					nuevoCampo=false;
				}

				//Esto será cuando un campo deje de tener toPorts, es decir ya no continuará
				else if(listaTransformaciones.get(posicionTransformacion).getNombre().contains("exp") && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()==null)
					tablaComparacionCampos.remove(posicionArrayCampos);

				//******************************ESTO SOLO SI ES EXPRESION******************************	
		}
		return nuevoCampo;
	} 
	
	public void ComprobacionCamposJoinUnion(int InstanciaPartida,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int posicionArrayCampos,int posicionTransformacion){
		boolean campoCorrecto=false;
		boolean encontrado=false;
		boolean cambiado=false;
		
		for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !encontrado;t++){
			
			//Que mire en ambas
			if(t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().size()-1 && t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().size()-1 ){
					if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getId()) || listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getId()) ) {
						if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().contains("decimal") && listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getEscala() != null) {
							if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) || listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)))  && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)) || (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))) /*&& (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getEscala().equals(tablaComparacionCampos.get(posicionArrayCampos).get(3)) || listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getEscala().equals(tablaComparacionCampos.get(posicionArrayCampos).get(3)))*/))
								//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
								campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
		
						}
						else{
							if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) || listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)))  && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)) || (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))))
								campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
						}
								
						
						encontrado=true;
					}
			}
			
			//Que mire solo en Principal
			else if(t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().size()-1){
					if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getId())) {
						if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().contains("decimal") && listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getEscala()!=null) {
							if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)) /*&& (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getEscala().equals(tablaComparacionCampos.get(posicionArrayCampos).get(3)))*/)))
								//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
								campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
							}
							else{
								if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))))
									campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
							}
								
						encontrado=true;
						}
			}
		
				//Que mire solo en Detalle
			else if(t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().size()-1){
				if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getId())) {
					if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().contains("decimal") && listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getEscala()!=null ) {
						if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)) /*&& (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getEscala().equals(tablaComparacionCampos.get(posicionArrayCampos).get(3)))*/)))
							//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
							campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
						}
						else{
							if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))))
								campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
						}
							
					encontrado=true;
					}
		}
	
			
			
	} //Cierre del for
		
		
		
		
		if(!campoCorrecto)
			System.out.println("La has cagado en el campo " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1) + " debes revisarlo y volver a pasar el XML al programa!!");
		
		if(campoCorrecto){
			encontrado=false;
			//si el campo esta ok, pasamos a actualizar el arraylist y ahora tendra un nuevo toPort este campo en el caso de las transformaciones.
			
			
			//******************************ESTO SOLO SI ES JOIN******************************
			if(listaTransformaciones.get(posicionTransformacion).getNombre().contains("jnr")){
				//TENDREMOS QUE UTILIZAR EL NOMBRE		
				//hay que ver si el nombre de la transformacion de salida del join su id tiene un transformation field que significa que la variable continua a la siguiente instancia y debemos meterla en el array
				for(int t=1; t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !cambiado;t++){
					if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t-1).getName().contains(tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1))){

						for(int r=0; r<listaInstancia.get(InstanciaPartida).getCampos().size() && !encontrado;r++){
							//ACTUALIZAMOS EL TOPORT
							encontrado=false;
							if(listaInstancia.get(InstanciaPartida).getCampos().get(r).getToPorts() !=null) {
								if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t-1).getId().equals(listaInstancia.get(InstanciaPartida).getCampos().get(r).getTransformationField())){													
									tablaComparacionCampos.get(posicionArrayCampos).set(0,listaInstancia.get(InstanciaPartida).getCampos().get(r).getToPorts());
									encontrado=true;
									cambiado=true;
								}
							}
						}
						
						if(!encontrado) //BORRAMOS ESTE CAMPO DEL ARRAYLIST xq significa que ya no le vamos a usar en adelante
							tablaComparacionCampos.remove(posicionArrayCampos);
						}
					}
				}	
			}
		} //Cierre del join
		//******************************ESTO SOLO SI ES JOIN******************************
} //Cierre de clase
