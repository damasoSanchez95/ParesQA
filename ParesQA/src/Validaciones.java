import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Logger;
import Excepciones.*; 

public class Validaciones {
	
	static final String CTE_EXPRESION = "exp_";
	static final String CTE_JOIN = "jnr_";
	static final String CTE_SORTER = "srt_";
	static final String CTE_FILTRO = "fil_";
	static final String CTE_UNION = "uni_";
	static final String CTE_AGREGACION = "agg_";
	static final String CTE_ROUTER = "rtr_";
	static final String CTE_RANKING = "rnk_";
	static final String CTE_NORMALIZER = "nrm_";
	
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
	
	public void comprobarNomenglatura(){
		for(int transformacion=0; transformacion<listaTransformaciones.size();transformacion++){
			if(!listaTransformaciones.get(transformacion).getNombre().contains("lectura") && !listaTransformaciones.get(transformacion).getNombre().contains("read") && !listaTransformaciones.get(transformacion).getNombre().contains("escritura")  && !listaTransformaciones.get(transformacion).getNombre().contains("write")){
				//System.out.println(listaTransformaciones.get(transformacion).getNombre().substring(0, 4));
				if(listaTransformaciones.get(transformacion).getNombre().substring(0, 1).contains("e") && !CTE_EXPRESION.equals(listaTransformaciones.get(transformacion).getNombre().substring(0, 4))){
					System.out.println("LA CAJA " + listaTransformaciones.get(transformacion).getNombre() + " NO CUMPLE CON LA NOMBLAGURA.");
				}
				else if(listaTransformaciones.get(transformacion).getNombre().substring(0, 1).contains("j") && !CTE_JOIN.equals(listaTransformaciones.get(transformacion).getNombre().substring(0, 4))){
					System.out.println("LA CAJA " + listaTransformaciones.get(transformacion).getNombre() + " NO CUMPLE CON LA NOMBLAGURA.");
				}
				else if(listaTransformaciones.get(transformacion).getNombre().substring(0, 1).contains("f") && !CTE_FILTRO.equals(listaTransformaciones.get(transformacion).getNombre().substring(0, 4))){
					System.out.println("LA CAJA " + listaTransformaciones.get(transformacion).getNombre() + " NO CUMPLE CON LA NOMBLAGURA.");
				}
				else if(listaTransformaciones.get(transformacion).getNombre().substring(0, 1).contains("u") && !CTE_UNION.equals(listaTransformaciones.get(transformacion).getNombre().substring(0, 4))){
					System.out.println("LA CAJA " + listaTransformaciones.get(transformacion).getNombre() + " NO CUMPLE CON LA NOMBLAGURA.");
				}
				else if(listaTransformaciones.get(transformacion).getNombre().substring(0, 1).contains("a") && !CTE_AGREGACION.equals(listaTransformaciones.get(transformacion).getNombre().substring(0, 4))){
					System.out.println("LA CAJA " + listaTransformaciones.get(transformacion).getNombre() + " NO CUMPLE CON LA NOMBLAGURA.");
				}
			}
		}
	}
	
	private void comprobarInstanciaTransformacion(int instanciaPartida, int posicionTransformacion){
		boolean QueCampo=false;
		String CampoFallo="";	

		if(listaInstancia.get(instanciaPartida).getCampos().size() != listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size()){
			QueCampo=false;
			CampoFallo="";
			//System.out.println("CAMPO DEL OBJETO " +listaObjetos.get(indiceObjeto).getCampos().get(w).getId());
			for(int campoTransformacion=0; campoTransformacion<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size(); campoTransformacion++){
				QueCampo=false;
				for(int x=0; x<listaInstancia.get(instanciaPartida).getCampos().size() && !QueCampo; x++){											
					if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getId().equals(listaInstancia.get(instanciaPartida).getCampos().get(x).getTransformationField())){
						QueCampo=true;											
					}
					else{
						CampoFallo=listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getName();
					}
				}

				
				if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getYaMostradoInput())
					if(!QueCampo && listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getInput()){
						listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).setYaMostradoInput(true);
						System.out.println("");
						System.out.println("El campo " + CampoFallo.toUpperCase() + " NO RECIBE NINGUNA FLECHA");
						System.out.println("ALTURA: "+ listaTransformaciones.get(posicionTransformacion).getNombre() );

				}
				
				if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getYaMostradoOutput2())
					if(!QueCampo && listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getOutput()){
					listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).setYaMostradoOutput2(true);
					System.out.println("");
					System.out.println("El campo " + CampoFallo.toUpperCase() + " NO VA A NINGUN LADO Y ESTA DECLARADO COMO OUTPUT");
					System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
				}
			}
		}
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
	
	public void conexionOwnerTablas(){
		boolean noOwner=false;
		boolean encontradaConexion=false;
		boolean encontradoPropietario=false;
		for(int j =0; j<listaTransformaciones.size(); j++){
			noOwner=false;
			int contador=0;
			encontradaConexion=false;
			encontradoPropietario=false;
			if(listaTransformaciones.get(j).getNombre().contains("lectura") || listaTransformaciones.get(j).getNombre().contains("read") || listaTransformaciones.get(j).getNombre().contains("write") || listaTransformaciones.get(j).getNombre().contains("escritura")){
				System.out.println("");
				if(listaTransformaciones.get(j).getOwnerTabla()==null){
					System.out.println("LA TABLA " + listaTransformaciones.get(j).getNombre() + " NO TIENE EL PROPIETARIO PUESTO");
					noOwner=true;
				}
					
				for (Entry<Parametro, String> entry : tablaParametros.entrySet()) {
					contador++;
					Parametro clave = entry.getKey();
					if(listaTransformaciones.get(j).getConexionTabla().equals((clave.getId()))) {
						encontradaConexion=true;
					}
					else
						if(contador == tablaParametros.size() && !encontradaConexion)
						System.out.println("LA TABLA " + listaTransformaciones.get(j).getNombre() + " NO TIENE UN PARAMETRO EN LA CONEXION");
				
					if(!noOwner && listaTransformaciones.get(j).getOwnerTabla().equals(clave.getId())){
						encontradoPropietario=true;
					}
					else
						if(contador==tablaParametros.size() && !encontradoPropietario && !noOwner)
							System.out.println("LA TABLA " + listaTransformaciones.get(j).getNombre() + " NO TIENE UN PARAMETRO EN EL PROPIETARIO");
					
				} //CIERRE FOR PARAMETROS
			} //CIERRE IF PARA VER SI ES UNA TABLA
		} //CIERRE FOR TRANSFORMACIONES
			
	}
	
	public void executionParameters(){
		for (Entry<Parametro, String> entry : tablaParametros.entrySet()) {
			Parametro clave = entry.getKey();
			try {
				if( (!tablaExecutionParameters.containsKey(clave.getId())) && (clave.getName().contains("DRIVER") || clave.getName().contains("EXECUTOR") || clave.getName().contains("PARTITIONS")))
					throw new SinParametroPre("NO ESTA EL PARAMETRO " + clave.getName() + " DENTRO DE LOS TIEMPO DE EJECUCION DE LA FORMA ADECUADA");
			}catch(SinParametroPre e){
					System.out.println(e.getLocalizedMessage());
				}
			}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void ValidacionesPuertos(){

		boolean toPortsEsNull=false;
		boolean transformacionEncontrada=false;
		boolean encontradoDataRecords=false;
		boolean instanciaEncontrada=false;
		boolean tipo_idRef[]= new boolean[2];	
		boolean campoCorrecto=false;
		boolean objetoMapping=false;
		boolean comprobado=false;
		boolean borrado=false;
		boolean escritura=false;

		int InstanciaPartida=0;//declaramos variables que sera el indice de instancia de partida y de final
		int InstanciaDestino=0;
		int contadorDataRecord=0;

		//Declaramos los objetos
		ArrayList<ArrayList<String>> tablaComparacionCampos = new ArrayList<ArrayList<String>>();
		DataRecord nuevaDataRecord = new DataRecord(null,null, null);
		Iobject nuevoObjeto = new Iobject(null,null,null,tipo_idRef[1],null);	
		Instance nuevaInstancia = new Instance(null,null,null,null,null, null, null);
		AbstractTransformation nuevaTransformacion = new AbstractTransformation(null,null,null,null,null, null,null,null,null);

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
				encontradoDataRecords=false;
				while(contadorDataRecord <listaDataRecords.size() &&  !encontradoDataRecords){
					if(listaObjetos.get(indiceObjeto).getId().equals(listaDataRecords.get(contadorDataRecord).getId())) {
						nuevaDataRecord=listaDataRecords.get(contadorDataRecord);
						encontradoDataRecords=true;
					}	
					else
						contadorDataRecord++;
				}
				contadorDataRecord=0;
				if(!encontradoDataRecords) //HABRA QUE HACER UNA EXCEPCION (NO DEBERIA ENTRAR NUNCA)
					System.out.println("FALLO, NO HEMOS ENCONTRADO ESE DATARECORDS CUANDO ES UN ID DE REFERENCIA A ÉL");
				else{ //COMO DATARECORDS ES IGUAL QUE UN OBJETO IGUALOS SUS CAMPOS, PARA DESPUES JUGAR CON ÉL DIRECTAMENTE
					nuevoObjeto.setName(nuevaDataRecord.getName());
					nuevoObjeto.setCampos(nuevaDataRecord.getCampos());
				}	
			}

			//System.out.println(" NUEVO OBJETO" + nuevoObjeto.getName());


			//AQUI SEA UN OBJETO DE REFERENCIA O UNO NORMAL LLEGAMOS CON EL NOMBRE Y LA LISTA DE CAMPOS CORRESPONDIENTE

			//Entonces cogemos el name y debemos buscar que nombre de las instancias CONTIENE ese nombre porque tendra "lectura_" y no sera exactamente igual
			//hay que comparar si es de lectura o escritura pero solo se sabe al compararlo con el nombre de la instancia

			if(!objetoMapping){
				for(int indiceInstancia=0; indiceInstancia<listaInstancia.size() && !escritura; indiceInstancia++){ //Bucle de instancias, para buscar por que instancia empiezas
					nuevaInstancia=listaInstancia.get(indiceInstancia);
					//System.out.println(" NUEVA INSTANCIA " + nuevaInstancia.getName());
					if(nuevaInstancia.getName().contains(nuevoObjeto.getName())){ //DE LA LISTA DE INSTANCIAS SOLO HABRA UNO QUE CONTENGA ESE NOMBRE, LA INSTANCIA DEL OBJETO
						//el nombre existe por lo que la instancia esta bien
						instanciaEncontrada=true;

						instanciaEncontrada=false;
						InstanciaPartida=indiceInstancia;//guardamos la posicion de la instancia de partida del array de instancias
						String NombreAComprobar=listaInstancia.get(indiceInstancia).getName();

						while(!NombreAComprobar.contains("FINAL")){
							//si no entra en el while significa que hemos cogido un objeto con escritura o hemos llegado a la ultima instancia que es la de escritura	
							//necesitamos coger el toInstance de esa instancia
							
							if(!NombreAComprobar.contains("escritura") && !NombreAComprobar.contains("write")){
							
								String siguienteId=listaInstancia.get(InstanciaPartida).getToInstance();//importante ya que lo tendremos en cuenta con los toPort y FromPort	
								InstanciaDestino=SiguienteInstancia(siguienteId,instanciaEncontrada,InstanciaDestino);
							}

							//Aqui busco su la transformacion que le corresponda a esta instancia
							instanciaEncontrada=false;
							int posicionTransformacion=busquedaTransformacion(transformacionEncontrada,InstanciaPartida);
							
							if(!listaTransformaciones.get(posicionTransformacion).getNombre().contains("escritura") && !listaTransformaciones.get(posicionTransformacion).getNombre().contains("write") && !listaTransformaciones.get(posicionTransformacion).getNombre().contains("lectura") && !listaTransformaciones.get(posicionTransformacion).getNombre().contains("read"))
								comprobarInstanciaTransformacion(InstanciaPartida,posicionTransformacion);
							
							
							
							//AQUI TENEMOS LAS DOS INSTANCIAS (LA ACTUAL Y LA SIGUIENTE) DE LAS CUALES HAY QUE COMPROBAR SUS PUERTOS Y TAMBIEN LA POSICION DE LA TRANSFORMACION QUE LE CORRESPONDA.
							
							transformacionEncontrada=false;
							
							//CASO DE lectura
							if(NombreAComprobar.contains("lectura") || NombreAComprobar.contains("read")){
								//AQUI COMPROBAMOS QUE LOS CAMPOS SON CORRECTOS Y EXISTEN EN LA 2 INSTANCIA, PERO NO ESTAMOS COMPARANDO LOS PUERTOS AUN
								int ContadorComprobaciones=0; //Contador de arrayList, hay un arrayList de campos
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){	 //For para recorrer todos los campos de la instancia de origen					
									campoCorrecto=false;
									boolean[] ArrayBoleanos= new boolean[2];
									//metodo para recorrer la instancia destino y compararla con la de partida
									ArrayBoleanos=recorrerInstanciaDestino(InstanciaDestino,campoCorrecto,InstanciaPartida,indiceCampoDeLaInstanciaPartida,false,NombreAComprobar);
									campoCorrecto=ArrayBoleanos[0];

									
									//if(!campoCorrecto)
										//System.out.println("El id " + listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId()+ " de la instancia de partida no se encuentra EN LA INSTANCIA DESTINO");

									//else{
										//creamos un nuevo arraylista dentro del ya existente cada arraylist será un CAMPO con LOS PUERTOS(toPort,precision,tipo,escala, NAME)
										tablaComparacionCampos.add(new ArrayList<String>());
										tablaComparacionCampos.get(ContadorComprobaciones).add(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts());
										//metemos el dato y en comprobacion feature meteremos todos los demas campos al hacerlas comprobaciones 
										ComprobacionFeatures(InstanciaPartida,indiceObjeto,indiceCampoDeLaInstanciaPartida,tablaComparacionCampos,ContadorComprobaciones,posicionTransformacion);
										ContadorComprobaciones++;//sumamos uno al contador del arraylist general para que al siguiente paso del bucle se cree otro ARRAYLIST PARA OTRO CAMPO
									//}
								}
							} //RECORREMOS TODOS LOS CAMPOS Y VAMOS CREANDO ARRAYLIST CON LOS PUERTOS DE CADA CAMPO 

							
							//CASOS
							else if(NombreAComprobar.contains("escritura") || NombreAComprobar.contains("write") || NombreAComprobar.contains("agg") ||NombreAComprobar.contains("exp") || NombreAComprobar.contains("jnr") || NombreAComprobar.contains("fil")  || NombreAComprobar.contains("uni")){	
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){
									campoCorrecto=false;
									toPortsEsNull=false;
									comprobado=false;
									//en la primera parte comparamos los campos en la siguiente los puertos y en la tercera el añadido de un nuevo campo (solo en las expresiones, xq es por si hemos creado un nuevo campo)

									//1º PARTE (AQUI VEMOS SI LOS CAMPOS DE LA INSTANCIA PARTIDA ESTAN EN LA ISNTANCIA DESTINO O NO)
									boolean[] ArrayBoleanos= new boolean[2]; 
									//metodo para recorrer la instancia destino y compararla con la de partida
									
									ArrayBoleanos=recorrerInstanciaDestino(InstanciaDestino,campoCorrecto,InstanciaPartida,indiceCampoDeLaInstanciaPartida,toPortsEsNull,NombreAComprobar);
									campoCorrecto=ArrayBoleanos[0];
									toPortsEsNull=ArrayBoleanos[1];

									
									//AQUI SE PUEDE HACER LA COMPROBACION DE LOS OUTPUTS CON LOS TOPORTS
									
									if(!NombreAComprobar.contains("escritura") && !NombreAComprobar.contains("write"))
										comprobacionToPortsOutputs(posicionTransformacion,InstanciaPartida,indiceCampoDeLaInstanciaPartida);
									
									//Deberia entrar en los campos que no te lleves a la instancia destino.
									//if(!campoCorrecto)
										//System.out.println("El id " + listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId()+ " de la instancia de partida no se encuentra en la de destino");

									//2º PARTE
									boolean NuevoCampoo=true;//solo es util para la tercera parte SOLO DE EXP
									for(int posicionArrayCampos=0; posicionArrayCampos<tablaComparacionCampos.size();posicionArrayCampos++){							
										//COMPARAS EL ID CON EL TO_PORT DEL ANTERIOR
										if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId().equals(tablaComparacionCampos.get(posicionArrayCampos).get(0)) && !comprobado){																							
											if(NombreAComprobar.contains("jnr") || NombreAComprobar.contains("uni")) {
												if(tablaComparacionCampos.get(posicionArrayCampos).size() > 1) {
													
													//if(borrado) {//Si hemos borrado, tenemos que volver a comprobar todo de nuevo ya que el arrayCampos ya no es el mismo
														//posicionArrayCampos=0;
													//}
												
													NuevoCampoo=ComprobacionCamposJoinUnion(InstanciaPartida, indiceCampoDeLaInstanciaPartida, tablaComparacionCampos, posicionArrayCampos, posicionTransformacion);		
												
												NuevoCampoo=false;//solo es util para la tercera parte SOLO DE EXP
												comprobado=true;
												}
											}
											
											else if(NombreAComprobar.contains("agg") || NombreAComprobar.contains("exp") || NombreAComprobar.contains("fil")){
												if(tablaComparacionCampos.size() > 1)
													NuevoCampoo=ComprobacionCamposExpresionFiltro(InstanciaPartida, indiceCampoDeLaInstanciaPartida, tablaComparacionCampos, posicionArrayCampos, posicionTransformacion);
											}

										
											else if(NombreAComprobar.contains("escritura") || NombreAComprobar.contains("write")) {
													ComprobacionCamposEscritura(InstanciaPartida, indiceCampoDeLaInstanciaPartida, tablaComparacionCampos, posicionArrayCampos, posicionTransformacion);		
											}
										}
									}
									//3º PARTE SOLO PARA LAS EXCEPCIONES Y PORQUE SIGNIFICA QUE VA A SER UN CAMPO NUEVO					
									if((NombreAComprobar.contains("agg") || NombreAComprobar.contains("uni") || NombreAComprobar.contains("jnr") || NombreAComprobar.contains("exp") || NombreAComprobar.contains("fil")) && NuevoCampoo && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null){													
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
									
//									else if(NombreAComprobar.contains("escritura") || NombreAComprobar.contains("write")){
//
//										for(int posicionArrayCampos=0; posicionArrayCampos<tablaComparacionCampos.size();posicionArrayCampos++)
//											if(tablaComparacionCampos.size() > 1)
//												NuevoCampoo=ComprobacionCamposEscritura(InstanciaPartida, indiceCampoDeLaInstanciaPartida, tablaComparacionCampos, posicionArrayCampos, posicionTransformacion);
//										
//									}
									
									
									
									
								}	
							}
							
							


							//CASO DE escritura
							if(NombreAComprobar.contains("escritura") || NombreAComprobar.contains("write") ) {
								escritura=true;

								
								//CON ESTO COMPROBAMOS SI ALGUN CAMPO POR CONECTARSE EN LA TABLA WRITE
								if(listaInstancia.get(InstanciaPartida).getCampos().size() != listaObjetos.get(indiceObjeto).getCampos().size() && indiceObjeto == listaObjetos.size()-1){
									boolean QueCampo=false;
									String CampoFallo="";	
									for(int w=0; w<listaObjetos.get(indiceObjeto).getCampos().size(); w++){
										QueCampo=false;
										//System.out.println("CAMPO DEL OBJETO " +listaObjetos.get(indiceObjeto).getCampos().get(w).getId());
										for(int v=0; v<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size(); v++){
											
											if(listaObjetos.get(indiceObjeto).getCampos().get(w).getId().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(v).getColumna())){											
												for(int x=0; x<listaInstancia.get(InstanciaPartida).getCampos().size() ; x++){											
													if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(v).getFeature().equals(listaInstancia.get(InstanciaPartida).getCampos().get(x).getStructural_feature())){
														QueCampo=true;											
													}else{
														CampoFallo=listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(v).getName();
													}
												}
											}
					
										}
										if(!QueCampo){
											System.out.println("");
											System.out.println("El campo " + CampoFallo.toUpperCase() +" de la tabla de escritura" + " esta sin flechita");
										}
						
									}
									
									NombreAComprobar="FINAL";
								}

								
								else {

									//en la instancia estan solo los q se han conectado, en el objeto todos.
									if(!listaInstancia.get(InstanciaPartida).getName().contains(listaObjetos.get(indiceObjeto).getName())){

										//Compruebas del campo de su instancia (sus puertos) con los puertos del campo de su propio objeto (INNECESARIO)
										for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(InstanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++)
												ComprobacionFeatures(InstanciaPartida,indiceObjeto,indiceCampoDeLaInstanciaPartida,tablaComparacionCampos,0,posicionTransformacion);

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
		
//private void comprobarCamposTr
	
	
	
private void comprobacionToPortsOutputs(int posicionTransformacion, int instanciaPartida,int indiceCampoDeLaInstanciaPartida) {

	for(int campoTransformacion=0; campoTransformacion<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size();campoTransformacion++){
		if(listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getId())){
			if(listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts() == null && listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getOutput()) {
				if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getYaMostradoOutput()){
				System.out.println("");
				System.out.println("El campo " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).getName() + " NO VA A NINGUN LADO Y ESTA DECLARADO COMO OUTPUT");
				System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
				listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(campoTransformacion).setYaMostradoOutput(true);
				}
			}
		}	
	}
}

//	private void comprobarArrayConObjetoDeEscritura(int instanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos, int posicionTransformacion) {
//		int objetoEncontrado=0;
//		
//		//Sacamos el objetoEncontrado (el de escritura)
//		for(int objeto=0; objeto<listaObjetos.size();objeto++)
//			if(objeto==listaObjetos.size()-1)
//				objetoEncontrado=objeto;
//		
//		for(int campoInstancia=0; campoInstancia<listaInstancia.get(instanciaPartida).getCampos().size();campoInstancia++){
//			for(int campoArray=0; campoArray<tablaComparacionCampos.size();campoArray++)
//				if(listaInstancia.get(instanciaPartida).getCampos().get(campoInstancia).getId().equals(tablaComparacionCampos.get(campoArray).get(0))){
//					if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(campoArray).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))){
//				}
//		}
//		
//	}

	private boolean[] recorrerInstanciaDestino(int InstanciaDestino,boolean campoCorrecto,int InstanciaPartida,int indiceCampoDeLaInstanciaPartida,boolean toPortsEsNull,String NombreAComprobar){	
		boolean[] ArrayBoleanos= new boolean[2];
		
		for(int indiceCampoDeLaInstanciaDestino=0; indiceCampoDeLaInstanciaDestino<listaInstancia.get(InstanciaDestino).getCampos().size() && !campoCorrecto; indiceCampoDeLaInstanciaDestino++){ //For para recorrer todos los campos de la instancia de destino		
			if( NombreAComprobar.contains("agg") || NombreAComprobar.contains("exp") || NombreAComprobar.contains("jnr") || NombreAComprobar.contains("uni") || NombreAComprobar.contains("fil") ){
				if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()==null) 
					toPortsEsNull=true;									
			}		
			
			//aqui recorremos los campos de la instacia DE LLEGADA ESTO LO HACEMOS TANTO SI ES EXP COMO JOIN COMO lectura SOLO QUE SI toPortsEsNull es false significa que es jnr o exp y no hay que hacerlo porque no hay toPorts
			if(toPortsEsNull==false && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId().equals(listaInstancia.get(InstanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getFromPorts()) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts().equals(listaInstancia.get(InstanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getId()))
				campoCorrecto=true;			
		}
		
		ArrayBoleanos[0]=campoCorrecto;
		ArrayBoleanos[1]=toPortsEsNull;
		return ArrayBoleanos;
	}

	private int busquedaTransformacion(boolean transformacionEncontrada, int instanciaPartida) {
			int posicionTransformacion=0;
			
			for(int t=0; t<listaTransformaciones.size() && !transformacionEncontrada; t++)
				if(listaTransformaciones.get(t).getNombre().contains(listaInstancia.get(instanciaPartida).getName())) {
					posicionTransformacion=t;//cojemos la posicion de la transformacion en la lista de transformaciones
						transformacionEncontrada=true;
				}	
			return posicionTransformacion;
	}

	private int SiguienteInstancia(String siguienteId, boolean instanciaEncontrada, int instanciaDestino) {

		for(int k=0; k<listaInstancia.size() &&  !instanciaEncontrada; k++){
			//habra que comparar el toInstance con el id de la siguiente instancia
			if(siguienteId.equals(listaInstancia.get(k).getId())){
				instanciaDestino=k;//guardamos la posicion  de la instancia de destino del array de instancias						
				instanciaEncontrada=true;
			}
		}
		return instanciaDestino;
	}	

	private boolean comprobacionesParaLosPuertosTransformacionesObjetos(int posicionTransformacion, int posicionCampoTransformacion, int indiceObjeto, int posicionCampoObjeto){
		
		if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType() !=null) {
		//	if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType().contains("decimal"))
			//	return listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getPrecision().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getPrecision())&&listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType())&&listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getEscala().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getEscala());
			//else
				return listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getPrecision().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getPrecision())&&listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getType().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType());
		}
		else return true; //para los campos que solo tengan feature, id, y el otro campo
	}
	
	
	//ESTE METODO DEBERIA IR SIEMPRE BIEN YA QUE ESTAS COMPROBANDO LOS CAMPOS DEL OBJETO CON SU PROPIA TRANSFORMACION
	public void ComprobacionFeatures(int InstanciaPartida,int indiceObjeto,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int ContadorComprobaciones, int posicionTransformacion){
		//comprobamos si el nombre de la transformacion concuerda con el del objeto
		boolean campoComprobado=false;
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
						if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(posicionCampoTransformacion).getColumna().equals(listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getId()) && comprobacionesParaLosPuertosTransformacionesObjetos(posicionTransformacion,posicionCampoTransformacion,indiceObjeto,posicionCampoObjeto)){
							//Aqui comprobamos si tanto la columna,nombre,precision,tipo, nullable y scala coinciden con el objeto 
							//ATENCION CON LA ESCALA QUE HABRA QUE COMPROBAR QUE NO SEA NULL
							campoCorrecto=true;
							if(!listaTransformaciones.get(posicionTransformacion).getNombre().contains("escritura") && !listaTransformaciones.get(posicionTransformacion).getNombre().contains("write") && campoCorrecto){
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
			}
		}
	}
	
	public boolean ComprobacionCamposExpresionFiltro(int InstanciaPartida,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int posicionArrayCampos,int posicionTransformacion){
		boolean campoCorrecto=false;
		boolean encontrado=false;
		boolean nuevoCampo=true;
		boolean fallaTipo=false;
		boolean fallaPrecision=false;
		boolean yaEnseñado=false;
		int dondeFalla=0;
		
		
		for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !encontrado;t++){
			//System.out.println(t);
			if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getId())) {
				if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))){
					//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
					campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS	
				}

				//fallaPrecision		
				if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1))){
					fallaPrecision=true;
					dondeFalla=t;
				}

				//fallaTipo	
				if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))){
					fallaTipo=true;
					dondeFalla=t;
				}

				if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getYaMostrado()) {
					listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).setYaMostrado(true);
					if(fallaPrecision){
						System.out.println("");
						System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
						System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
						yaEnseñado=true;
						System.out.println("MOTIVO DEL FALLO: Precision. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(1) + " y es  " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getPrecision()); 
						//System.out.println("");
	
					}
					if(fallaTipo) {
						if(yaEnseñado) {
							System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getType());	
							System.out.println("");
						}
						else {
							System.out.println("");
							System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
							System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
							System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getType());	
							System.out.println("");
	
							yaEnseñado=true;
						}
					}
				}
				encontrado=true;
			} //cierre del transformationField=id
		} //CierreFor
			
		
		if(campoCorrecto){
			//si el campo esta ok, pasamos a actualizar el arraylist y ahora tendra un nuevo toPort este campo en el caso de las transformaciones.

			//******************************ESTO SOLO SI ES EXPRESION******************************
			//ACTUALIZAMOS EL TOPORTS

				if(( listaTransformaciones.get(posicionTransformacion).getNombre().contains("agg") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("exp") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("fil")) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null){
					tablaComparacionCampos.get(posicionArrayCampos).set(0,listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts());
					nuevoCampo=false;
				}

				//Esto será cuando un campo deje de tener toPorts, es decir ya no continuará
				else if(( listaTransformaciones.get(posicionTransformacion).getNombre().contains("agg") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("exp") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("fil")) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()==null)
					tablaComparacionCampos.remove(posicionArrayCampos);

				//******************************ESTO SOLO SI ES EXPRESION******************************	
		}
		return nuevoCampo;
	} 
	
	
	public void ComprobacionCamposEscritura(int InstanciaPartida,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int posicionArrayCampos,int posicionTransformacion){
		boolean campoCorrecto=false;
		boolean encontrado=false;
		boolean nuevoCampo=true;
		boolean fallaTipo=false;
		boolean fallaPrecision=false;
		boolean yaEnseñado=false;
		int dondeFalla=0;
		
		
		for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !encontrado;t++){
			if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision()!=null && listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType()!=null ){
				//System.out.println(t);
				if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getStructural_feature().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getFeature())) {
					if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))){
						//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
						campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS	
					}

					//fallaPrecision		
					if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1))){
						fallaPrecision=true;
						dondeFalla=t;
					}

					//fallaTipo	
					if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))){
						fallaTipo=true;
						dondeFalla=t;
					}

					if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getYaMostrado()) {
						listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).setYaMostrado(true);
						if(fallaPrecision){
							System.out.println("");
							System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
							System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
							yaEnseñado=true;
							System.out.println("MOTIVO DEL FALLO: Precision. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(1) + " y es  " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getPrecision()); 
							//System.out.println("");

						}
						if(fallaTipo) {
							//listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).setYaMostrado(true);
							if(yaEnseñado) {
								System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getType());	
								System.out.println("");
							}
							else {
								System.out.println("");
								System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
								System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
								System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getType());	
								System.out.println("");

								yaEnseñado=true;
							}
						}
					}
					encontrado=true;
				} //cierre del transformationField=id
			} //Cierre if para ver que no es null las cosas 
		} //CierreFor
			
		
		if(campoCorrecto){
			//si el campo esta ok, pasamos a actualizar el arraylist y ahora tendra un nuevo toPort este campo en el caso de las transformaciones.

			//******************************ESTO SOLO SI ES EXPRESION******************************
			//ACTUALIZAMOS EL TOPORTS

				if(( listaTransformaciones.get(posicionTransformacion).getNombre().contains("agg") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("exp") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("fil")) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null){
					tablaComparacionCampos.get(posicionArrayCampos).set(0,listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts());
					nuevoCampo=false;
				}

				//Esto será cuando un campo deje de tener toPorts, es decir ya no continuará
				else if(( listaTransformaciones.get(posicionTransformacion).getNombre().contains("agg") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("exp") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("fil")) && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()==null)
					tablaComparacionCampos.remove(posicionArrayCampos);

				//******************************ESTO SOLO SI ES EXPRESION******************************	
		}
		//return nuevoCampo;
	} 
	
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean ComprobacionCamposJoinUnion(int InstanciaPartida,int indiceCampoDeLaInstanciaPartida, ArrayList<ArrayList<String>> tablaComparacionCampos,int posicionArrayCampos,int posicionTransformacion){
		boolean campoCorrecto=false;
		boolean encontrado=false;
		boolean cambiado=false;
		boolean borrado=false;
		boolean mirarEnDetalle=false;
		boolean fallaTipo=false;
		boolean fallaPrecision=false;
		boolean mirarEnPrincipal=false;
		boolean yaEnseñado=false;
		int dondeFalla=0;
		boolean nuevoCampo=true;
		
		
		//HAY QUE HACER ALGO PARA QUE MIRE SI ESTA EN DETALLE O EN PRINCIPAL Y UNA VEZ AHI YA MIRAR. XQ SINO PUEDE QE COMPARE UN CAMPO CON LO Q NO DEBE Y DÉ CORRECT CUANDO NO DEBERIA.
		
		for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !encontrado;t++){

			//Que mire en ambas
				if((t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().size()-1 && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getId())))
					mirarEnPrincipal=true;
				
				else if((t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().size()-1 && listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getId())))
					mirarEnDetalle=true;

			//Que mire solo en Principal
				if(mirarEnPrincipal){
					if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getId())) {
						if((listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2)))))
							//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
							campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS	
						
						if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1))){
							fallaPrecision=true;
							dondeFalla=t;
						}
							
						
						if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))) {
							fallaTipo=true;
							dondeFalla=t;
						}
							
						if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getYaMostrado()) {
							listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).setYaMostrado(true);
							if(fallaPrecision){
								System.out.println("");
								System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
								System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
								yaEnseñado=true;
								System.out.println("MOTIVO DEL FALLO: Precision. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(1) + " y es  " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(dondeFalla).getPrecision()); 
								//System.out.println("");
	
								yaEnseñado=true;
							}
							
							if(fallaTipo) {
								if(yaEnseñado) {
									System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(dondeFalla).getType());	
									System.out.println("");
								}
								else {
									System.out.println("");
									System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
									System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
									System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(dondeFalla).getType());	
									System.out.println("");
	
									yaEnseñado=true;
								}
							} //Cierre del fallaTipo
						}
						encontrado=true;
					} //Cierre del getId=getTransformationFiel
				}//Cierre mirarEnPrincipal

			//Que mire solo en Detalle
			if(mirarEnDetalle){
				if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getId())) {
						if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getTransformationField().equals(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getId()) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))))) {
							//COMPROBAMOS LOS CAMPOS DE LA INSTANCIA A LOS DEL ARRAYlIST QUE SON LOS DE LA ISNTANCIA ANTERIOR 
							campoCorrecto=true;//Los CAMPOS ESTAN CORRECTOS		
						}
					
						if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1))) // && (listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))))) {
						{
							fallaPrecision=true;
							dondeFalla=t;
						}
							
						
						if(/*(listaTransformaciones.get(posicionTransformacion).getCamposTransformacionPrincipal().get(t).getPrecision().equals(tablaComparacionCampos.get(posicionArrayCampos).get(1)) && */ !listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(t).getType().equals(tablaComparacionCampos.get(posicionArrayCampos).get(2))) {
							fallaTipo=true;
							dondeFalla=t;
						}
						
						if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).getYaMostrado()) {
							listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(dondeFalla).setYaMostrado(true);
							if(fallaPrecision){
								System.out.println("");
								System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
								System.out.println("ALTRURA : " + listaTransformaciones.get(posicionTransformacion).getNombre());
								yaEnseñado=true;
								System.out.println("MOTIVO DEL FALLO: Precision. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(1) + " y es  " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(dondeFalla).getPrecision()); 
								//System.out.println("");
	
								yaEnseñado=true;
							}
							if(fallaTipo) {
								if(yaEnseñado) {
									System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(dondeFalla).getType());	
									System.out.println("");
								}
								else {
									System.out.println("");
									System.out.println("FALLO EN EL CAMPO: " + tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1));
									System.out.println("ALTURA: " + listaTransformaciones.get(posicionTransformacion).getNombre());
									System.out.println("MOTIVO DEL FALLO: Tipo. Deberia ser " + tablaComparacionCampos.get(posicionArrayCampos).get(2) + "  y es " + listaTransformaciones.get(posicionTransformacion).getCamposTransformacionDetalle().get(dondeFalla).getType());	
									System.out.println("");
	
									yaEnseñado=true;
								}
							} //Cierre del fallaTipo
						}
						encontrado=true;
				}	
			} //Cierre mirarEnDetalle

		} //Cierre del for


		if(campoCorrecto){
			encontrado=false;
			//si el campo esta ok, pasamos a actualizar el arraylist y ahora tendra un nuevo toPort este campo en el caso de las transformaciones.


			//******************************ESTO SOLO SI ES JOIN******************************
			if(listaTransformaciones.get(posicionTransformacion).getNombre().contains("jnr") || listaTransformaciones.get(posicionTransformacion).getNombre().contains("uni") ){
				//TENDREMOS QUE UTILIZAR EL NOMBRE		
				//hay que ver si el nombre de la transformacion de salida del join su id tiene un transformation field que significa que la variable continua a la siguiente instancia y debemos meterla en el array
				for(int t=1; t<=listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().size() && !cambiado;t++){
					if(!listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t-1).getName().contains("sig_") &&  listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t-1).getName().contains(tablaComparacionCampos.get(posicionArrayCampos).get(tablaComparacionCampos.get(posicionArrayCampos).size()-1))){

						for(int r=0; r<listaInstancia.get(InstanciaPartida).getCampos().size() && !encontrado;r++){
							//ACTUALIZAMOS EL TOPORT
							encontrado=false;
							if(listaInstancia.get(InstanciaPartida).getCampos().get(r).getToPorts() !=null) {
								if(listaTransformaciones.get(posicionTransformacion).getCamposTransformacion().get(t-1).getId().equals(listaInstancia.get(InstanciaPartida).getCampos().get(r).getTransformationField())){													
									tablaComparacionCampos.get(posicionArrayCampos).set(0,listaInstancia.get(InstanciaPartida).getCampos().get(r).getToPorts());
									encontrado=true;
									cambiado=true;
									nuevoCampo=false;
								}
							}
						}

						if(!encontrado){ //BORRAMOS ESTE CAMPO DEL ARRAYLIST xq significa que ya no le vamos a usar en adelante
							tablaComparacionCampos.remove(posicionArrayCampos);
							borrado=true;
							cambiado=true;
						}
					}
				}
			}	
		}
		return nuevoCampo;
	} //Cierre del join
} //Cierre de clase
