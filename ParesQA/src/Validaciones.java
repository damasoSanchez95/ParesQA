import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import Excepciones.*; 

public class Validaciones {
	
	
	private ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private ArrayList<AbstractTransformation> listaTransformaciones= new ArrayList<AbstractTransformation>();
	private ArrayList<Iobject> listaObjetos = new ArrayList<Iobject>();
	private ArrayList<DataRecord> listaDataRecords= new ArrayList<DataRecord>();
	private HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();
	private HashMap<String, String> tablaExecutionParameters = new HashMap<String, String>();
	private boolean descripcionMapping;
	
	
	//Constructor Validaciones
	public Validaciones(ArrayList<DataRecord> listaDataRecords ,boolean descripcionMapping, ArrayList<Instance> listaInstancia, ArrayList<AbstractTransformation> listaTransformaciones , HashMap<Parametro, String> tablaParametros, ArrayList<Iobject> listaObjetos, HashMap<String, String> tablaExecutionParameters ){
		this.listaInstancia=listaInstancia;
		this.listaTransformaciones=listaTransformaciones;
		this.tablaParametros=tablaParametros;
		this.listaObjetos=listaObjetos;
		this.tablaExecutionParameters=tablaExecutionParameters;
		this.descripcionMapping = descripcionMapping;
		this.listaDataRecords=listaDataRecords;
	}
	
	public void parametros() throws FechaEjecucionFallo{
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
	
	
	
	
/*	
	
	public void ValidacionesPuertos(){
		
		boolean puertoCorrecto = false;
		boolean encontradoDataRecords=false;
		boolean instanciaEncontrada=false;
		boolean tipo_idRef[]= new boolean[2];
		
		int instanciaPartida; //declaramos variables que sera el indice de instancia de partida y la instancia destino
		int instanciaDestino = 0;

		DataRecord nuevaDataRecord = new DataRecord(null,null, null);
		Iobject nuevoObjeto = new Iobject(null,null,null,tipo_idRef[1],null);	
		Instance nuevaInstancia = new Instance(null,null,null,null,null, null, null);
		AbstractTransformation nuevaTransformacion = new AbstractTransformation(null,null,null, null,null,null);


		Iterator<Iobject> iteradorObjetos;
		Iterator<DataRecord> iteradorDataRecords;
		Iterator<Instance> iteradorInstancias;
		Iterator<AbstractTransformation> iteradorAbstractTransformation;

		Instance.Campos campo; //Variable local del campo de una instancia


		//ITERADORES PARA RECORRER LAS LISTAS CORRESPONDIENTES
		iteradorObjetos = listaObjetos.iterator(); 
		iteradorDataRecords = listaDataRecords.iterator(); 
		iteradorInstancias = listaInstancia.iterator();
		iteradorAbstractTransformation = listaTransformaciones.iterator();


		//lo primero habra que analizar los datos de los objetos para ver si tienen realacion con las intancias y las transformaciones
		for(int indiceObjeto=0; indiceObjeto<listaObjetos.size(); indiceObjeto++){ //Recorremos todos los objetos
			nuevoObjeto = listaObjetos.get(indiceObjeto);
			if(nuevoObjeto.getRef() && nuevoObjeto.getType().contains("datarecord")){ //vemos si es un objeto con id de referencia
				while(iteradorDataRecords.hasNext() && !encontradoDataRecords){
					nuevaDataRecord=iteradorDataRecords.next();
					if(nuevaDataRecord.getId()==nuevoObjeto.getId())
						encontradoDataRecords=true;
				}

				if(!encontradoDataRecords) //HABRA QUE HACER UNA EXCEPCION
					System.out.println("FALLO, NO HEMOS ENCONTRADO ESE DATARECORDS CUANDO ES UN ID DE REFERENCIA A ÉL");

				else{ //COMO DATARECORDS ES IGUAL QUE UN OBJETO IGUALOS SUS CAMPOS, PARA DESPUES JUGAR CON ÉL DIRECTAMENTE
					nuevoObjeto.setName(nuevaDataRecord.getName());
					nuevoObjeto.setCampos(nuevaDataRecord.getCampos());	
				}	
			}

			//AQUI SEA UN OBJETO DE REFERENCIA O UNO NORMAL LLEGAMOS CON EL NOMBRE Y LA LISTA DE CAMPOS CORRESPONDIENTE

			//*Entonces cogemos el name y debemos buscar que nombre de las instancias CONTIENE ese nombre porque tendra "Lectura_" y no sera exactamente igual

			//hay que comparar si es de lectura o escritura pero solo se sabe al compararlo con el nombre de la instancia

			for(int indiceInstancia=0; indiceInstancia<listaInstancia.size(); indiceInstancia++){
				nuevaInstancia=listaInstancia.get(indiceInstancia);
				if(nuevaInstancia.getName().contains(nuevoObjeto.getName())){ //DE LA LISTA DE INSTANCIAS SOLO HABRA UNO QUE CONTENGA ESE NOMBRE, LA INSTANCIA DEL OBJETO
					instanciaEncontrada=true;
					//el nombre existe por lo que la instancia esta bien
					
					instanciaPartida=indiceInstancia;//guardamos la posicion de la instancia de partida del array de instancias
					
					String NombreAComprobar=nuevaInstancia.getName();
					
					while(!NombreAComprobar.contains("Escritura")){ //si no entra en el while significa que hemos cogido la instancia de escritura 	
						
						//necesitamos coger el toInstance de esa instancia, para ver cual es la siguiente instancia
						String siguienteId=nuevaInstancia.getToInstance(); //importante ya que lo tendremos en cuenta con los toPort y FromPort
						
						//ESTE FOR SIRVE PARA SACAR LA INSTANCIA DESTINO
						for(int k=0; k<listaInstancia.size(); k++){
							//habra que comparar el toInstance con el id de la siguiente instancia
							if(siguienteId==listaInstancia.get(k).getId()){
								instanciaDestino=k;//guardamos la posicion  de la instancia de destino del array de instancias						
							}
						}
						
						//AQUI TENEMOS LAS DOS INSTANCIAS (LA ACTUAL Y LA SIGUIENTE) DE LAS CUALES HAY QUE COMPROBAR SUS PUERTOS
						
						//*************************COMPROBACION DE PUERTOS Y FEATURES************************

							if(NombreAComprobar.contains("Lectura")){ //Si la instancia de partida es una instancia de LECTURA
								
								//Recorremos todos los campos de la instanciaDePartida
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(instanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){
									puertoCorrecto=false;
									
								for(int indiceCampoDeLaInstanciaDestino=0; indiceCampoDeLaInstanciaDestino<listaInstancia.get(instanciaDestino).getCampos().size() && !puertoCorrecto; indiceCampoDeLaInstanciaDestino++)
									if(listaInstancia.get(instanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getFromPorts()==listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId() && listaInstancia.get(instanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getId()==listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts())
										puertoCorrecto=true;
										
								
								//APROVEHCAMOS EL MISMO BUCLE PARA COMPARAR LOS STRUCTURAL FEATURE. AQUI COMPROBAMOS LA INSTANCIA, EL OBJETO Y LA TRANSFORMACION
								ComprobacionFeatures(instanciaPartida,indiceObjeto,indiceInstancia,indiceCampoDeLaInstanciaPartida);
								}
								
								if(!puertoCorrecto)
									System.out.println("NO SE HA ENCONTRADO EL PUERTO ORIGEN EN LOS PUERTOS DESTINO");
							}
							
							else if(NombreAComprobar.contains("jnr")||NombreAComprobar.contains("exp")){
								//Recorremos todos los campos de la instanciaDePartida
								for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(instanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){
									puertoCorrecto=false;
								
									//los toPorts != null xq tienen que ir a algun lado si o si
								for(int indiceCampoDeLaInstanciaDestino=0; indiceCampoDeLaInstanciaDestino<listaInstancia.get(instanciaDestino).getCampos().size();indiceCampoDeLaInstanciaDestino++)
									if(listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getToPorts()!=null && listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getId()==listaInstancia.get(instanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getFromPorts() && listaInstancia.get(instanciaPartida).getCampos().get(indiceCampoDeLaInstanciaDestino).getToPorts()==listaInstancia.get(instanciaDestino).getCampos().get(indiceCampoDeLaInstanciaDestino).getId())
										puertoCorrecto=true;
											
							}
								
								if(!puertoCorrecto)
									System.out.println("los puertos blablabla son incorrectos");
								
								
						}//*************************COMPROBACION DE PUERTOS Y FEATURES************************					
								

						
						
						

						//SI LLEGAMOS AQUI SIGNIFICA QUE HEMOS TERMINADO DE RECORRER LOS PUERTOS DE LA INSTANCIA DE PARTIDA Y COMPARARLO TANTO ELLOS COMO LOS STRUCTURAL FEATURE CON SUS CORRESPONDIENTES CAMPOS
						//Guardamos en name de la siguiente instancia en NombreAComprobar comprobando si hemos llegado a escritura y cambiamos el valor de la instancia partida que es la actual instancia siguiente
						NombreAComprobar=listaInstancia.get(instanciaDestino).getName();//aqui guardamos el nombre de la siguiente instancia en el nombre a comprobar
						instanciaPartida=instanciaDestino;

					}//SIGNIFICA QUE HEMOS LLEGADO AL OBJETO DE ESCRITURA Y POR TANTO HEMOS ACABADO no entramos al while deberiamos de hacer un if y comprobar el structural feature
					if(NombreAComprobar.contains("Escritura")){
						for(int indiceCampoDeLaInstanciaPartida=0; indiceCampoDeLaInstanciaPartida<listaInstancia.get(instanciaPartida).getCampos().size();indiceCampoDeLaInstanciaPartida++){
							ComprobacionFeatures(instanciaPartida,indiceObjeto,indiceInstancia,indiceCampoDeLaInstanciaPartida);
						}
					}
				}
				else{
					//SI NUNCA ENCUENTRA NADA SIGNIFICA QUE HAY UN PROBLEMA *PONER BANDERA* debera entrar una vez al menos en el if
				}				
			}					
		}

	}

	//SE HA HECHO ESTE METODO YA QUE LOS FEATURES SOLO SE TIENEN QUE HACER SI ES LECTURA O ESCRITURA	

public void ComprobacionFeatures(int InstanciaPartida,int indiceObjeto,int indiceInstancia,int indiceCampoDeLaInstanciaPartida){
		//comprobamos si el nombre de la transformacion concuerda con el del objeto
		int posicionTransformacion = 0;
		boolean campoEncontrado=false;
		boolean transformacionEncontrada=false;
		boolean campoCorrecto=false;
		boolean falloCampo=false;
		int posicionFalloCampo;
		int posicionCampoTransformacion=0;

		for(int t=0; t<listaTransformaciones.size() && !transformacionEncontrada; t++){
			if(listaTransformaciones.get(t).getNombre().contains(listaObjetos.get(indiceObjeto).getName())){
				posicionTransformacion=t;//cojemos la posicion de la transformacion en la lista de transformaciones
			}
		} //DE ESTA MANERA YA TENEMOS A QUE TRANSFORMACION NOS REFERIMOS

		if(transformacionEncontrada){
			//RECORREMOS LOS CAMPOS DE DICHA TRANSFORMACION
			for(int t=0; t<listaTransformaciones.get(posicionTransformacion).getCampos().size() && !campoEncontrado;t++){
				if(listaInstancia.get(InstanciaPartida).getCampos().get(indiceCampoDeLaInstanciaPartida).getStructural_feature()==listaTransformaciones.get(posicionTransformacion).getCampos().get(t).getFeature()){
					campoEncontrado=true;
					posicionCampoTransformacion = t;
				}
			}	
			
			//ahora deberemos comparar column,nombre,precision y tipo con el objeto para terminar de dar la vuelta a las comprobaciones
			if(campoEncontrado){
				//debemos compararlo con las columna del objeto donde estamos NO DE OTROS OBJETOS
				for(int posicionCampoObjeto=0; posicionCampoObjeto<listaObjetos.get(indiceObjeto).getCampos().size() && !falloCampo;posicionCampoObjeto++)
					if(listaTransformaciones.get(posicionTransformacion).getCampos().get(posicionCampoTransformacion).getColumna()==listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getId() && listaTransformaciones.get(posicionTransformacion).getCampos().get(posicionCampoTransformacion).getPrecision()==listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getPrecision() && listaTransformaciones.get(posicionTransformacion).getCampos().get(posicionCampoTransformacion).getType() == listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getType() && listaTransformaciones.get(posicionTransformacion).getCampos().get(posicionCampoTransformacion).getEscala()==listaObjetos.get(indiceObjeto).getCampos().get(posicionCampoObjeto).getEscala()){
					//Aqui comprobamos si tanto la columna,nombre,precision,tipo, nullable y scala coinciden con el objeto 
					campoCorrecto=true;
					falloCampo=true;
					posicionFalloCampo=posicionCampoObjeto;
					} //MIRAR SI LOS CAMPOS DE LAS TRANSFORMACIONES VAN EN EL MISMO ORDEN QE LOS CAMPOS DEL OBJETO. LA RESPUESTA ES Q SI
			}
			else
				System.out.println("CAMPO EN CUESTION NO ENCONTRADO EN LA TRANSFORMACION");
		}
		else
			System.out.println("TRANSFORMACION EN CUESTION NO ENCONTRADA");
		

		if(!campoCorrecto)
			System.out.println("Algun campo no concuerda del colum cuyo id es: " + listaTransformaciones.get(posicionTransformacion).getCampos().get(posicionCampoTransformacion).getColumna() + " y cuyo name es " + listaTransformaciones.get(posicionTransformacion).getCampos().get(posicionCampoTransformacion).getName());
}

		
	//AQUI ESTA LA EXPLICACION DE LO QUE SE ESTA HACIENDO ARRIBA
		
		//while(it.hasNext()){
			/*if(el campo -ref- de cada objeto es true){
				*Cogemos el campo id y deberemos buscar ese campo en TODO el archivo, deberá de devolver true al encontrar algo
				
			}else{
					"si es false el campo -ref-"
					*Entonces cogemos el name y debemos buscar que nombre de las instancias CONTIENE ese nombre porque tendra "Lectura_" y no sera exactamente igual
						
			while(el nombre del siguiente objeto al compararlo con el name la instancia es diferente a escritura){ "tenemos que llegar a compararlo con la instancia porque ahi reside si es de lectura o escritura en ese caso no haremos la movida de avanzar de instancias porque es la ultima:)"
				++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				*Ahora tenemos que coger el -toInstance- a través del -fromInstance- que is el mismo al id o directamente cogiendo el toInstance de esa instancia
					*el -strcturalFeature- lo tendremos que tener en cuenta para compararlo con el -feature- de los relational Field y comparar (nombre,precision,tipo y la columna del objeto del que veniamos de primeras)		
						*El -structuralFeatre- lo cmprobamos con el -feature- de los realtionalField y de ahí cogemos nombre,precision,tipo...) 
						*Lo tendremos que comparar con otras relational field el JoinerField (tantas veces como transformaciones haya en los joiner hay varios) con el column (tantas veces como objetos)
							*Para comprobarlo con el column tendremos que partir del feature del realtionalfield y coger el column (hay que comprobar si su id corresponde al del bojeto y toda la vaina del nombre,precision,tipo...
					
					SI ESTA TODO BIEN PARTIMOS A COGER LOS PUERTOS COMPARARLO CON LA SIGUIENTE INSTANCIA Y PASAR  
				*Tendremos que coger el -toPorts- y el -id- para compararlo con el -id- y el -fromPort- respectivamente de la siguiente instancia		
					*Una vez que tenemos el -toInstance- tendremos que buscar que id de las instancias es igual y comparar los -id- y -fromPort- 
					*Si todo esta correcto pasaremos a la siguiente instancia 
					
					*HABRA QUE TENER EN CUENTA SI ES UNA EXPRESION O UN JOIN PORQUE SI ES UN JOIN SEGUIREMOS HACIA ADELANTE Y NO HACIA EL LADO
					
					SI ES JOIN
					*Lo primero será comprobar  las -transformationField- de la tabla que vengamos con el -id- del JoinerField y comparar todo nombre...
					* Si es un join solamente habra que comparar los toPorts ya que los fromPort e id de una de las dos tablas ya se ha comprobado lo unico que habrá fromPort e Id por comprobar que se harán al ir desde el siguiente objeto
					* cogeremos el toInstance compararemos que id pertenece esa instancia y lo mismo comparareos toport e id
					* Si hemos llegado a la tabla de escritura ya no cambiaremos de instancia pero tenemos que mirar el -structuralfeature- como siempre que es el feature de ahi los campos y el column y los campos
					
					ELSE SI ES EXPRRESION
					*Lo primero será comprobar  las -transformationField- de la tabla que vengamos con el -id- del JoinerField y comparar todo nombre...
					* Hay que comprobar si tiene fromPort y toPort (si es asi el campo viene del anterior y continua) si solo tiene From(viene del anerior pero luego se creara una nueva variable) y to(es una variable creada nueva)
						1:los que tengan ambos: habrá que comprobar el toPort que sea el fromPort del siguiente, el fromPort ya se ha comprobado que era el id del anterior y el id es el toPort del anterior (comprobado) y el from del siguiente (hay que comprobarlo)
						2:los que tengan solo toPort será el id del siguiente (hay que comprobar) y el id el fromPort del siguiente (comprobar)
						3:los que tengan solo fromPort son campos que ya no siguen no hay que comprobar nada ya que se ha comprobado al pasar de la anterior instancia a esta
					*Comprobaremos los puertos necesarios con la siguiente tabla haciendo referencia al toInstance=id de la siguiente instancia	
					
					ELSE SI LLEGAMOS A LA TABLA DE ESCRITURA IREMOS AL SIGUIENTE OBJETO PARA HACER LA PASADA PERTINENTE 
					if(el nombre de la instancia que hemos accedido por id sea igual a escritura){
						
						*Tendremos que leer el siguiente objeto y siemrpe que sea de lectura tendremos que leer el nombre del objeto y compararlo con el nombre de la instancia de ahi si es lectura saldremos del bucle
						*El estilo es el mismo que antes hasta que lleguemos a la tabla de escritura y volvamos a buscar otro objeto. Si los objetos se acaban hemos acabado las validaciones
					}
					
					
				}	
					
			}
			
			
			
			
			
		}*/
}
