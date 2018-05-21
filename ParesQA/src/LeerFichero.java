import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.midi.Synthesizer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import Excepciones.*; 
import java.util.logging.Level;
import java.util.logging.Logger;


public class LeerFichero {
	
	private static ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private static ArrayList<AbstractTransformation> listaTransformacion = new ArrayList<AbstractTransformation>();
	private static ArrayList<Iobject> listaObjetos = new ArrayList<Iobject>();
	private static ArrayList<DataRecord> listaDataRecords = new ArrayList<DataRecord>();
	private static HashMap<String, String> tablaExecutionParameters = new HashMap<String, String>();
	private static HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();
	private static boolean falloXML;
	private static boolean falloReal;
	private static String dataInterface;
	private static boolean descripcionMapping;
	
	public static void pasarNombreTransformacionesAMinusc(){
		int contador=0;
		while(contador <= listaTransformacion.size()-1){
			listaTransformacion.get(contador).setNombre(listaTransformacion.get(contador).getNombre().toLowerCase());
			contador++;
		}			
	}
	
	public static void pasarNombreInstanciaAMinusc(){
		int contador=0;
		while(contador <= listaInstancia.size()-1){
			listaInstancia.get(contador).setName(listaInstancia.get(contador).getName().toLowerCase());
			contador++;
		}		
	}

	public static boolean getDescripcionMapping(){
		return descripcionMapping;
	}
	
	public static boolean getFalloXML(){
		return falloXML;
	}
	
	public static boolean getFalloReal(){
		return falloReal;
	}
	
	public static ArrayList<Instance> getListaInstancia(){
		return listaInstancia;
	}
	
	public static ArrayList<DataRecord> getListaDataRecords(){
		return listaDataRecords;
	}
	
	public static ArrayList<AbstractTransformation> getListaTransformaciones(){
		return listaTransformacion;
	}
	
	public static ArrayList<Iobject> getListaObjetos() {
		return listaObjetos;
	}
	
	public static HashMap<String, String> getTablaExecutionParameters() {
		return tablaExecutionParameters;
	}
	
	public static HashMap<Parametro, String> getTablaParametros(){
		return tablaParametros;
	}
	
	public static void ordenarObjetos(){
		int instanciaEncontrada=0;
		int objetoEncontrado=0;
		int dataRecordEncontrada=0;
		boolean parar=false;
		boolean dataRecord=true;
		Iobject objetoEscritura = new Iobject(null,null,null,false, null);

		//buscamos la instancia de escritura
		for(int instancia=0; instancia<listaInstancia.size()&&!parar;instancia++)
			if(listaInstancia.get(instancia).getName().contains("Escritura") || listaInstancia.get(instancia).getName().contains("Write")){
				parar=true;
				instanciaEncontrada=instancia;	
			}

		parar=false;

		//buscamos el objeto
		for(int objeto=0; objeto<listaObjetos.size()&&!parar;objeto++)
			if(listaObjetos.get(objeto).getName()!=null)
				if(listaInstancia.get(instanciaEncontrada).getName().contains(listaObjetos.get(objeto).getName())) {
					parar=true;
					objetoEscritura=listaObjetos.get(objeto);
					objetoEncontrado=objeto;
					dataRecord=false;
				}

		//ahora reeordenamos la lista de objetos, para ello borramos el objeto y le metemos al final
		if(!dataRecord) {
			listaObjetos.remove(objetoEncontrado);
			listaObjetos.add(objetoEscritura);
		}

		else { //Si es dataRecord lo buscamos ahi
			parar=false;
			
			//Buscamos el dataRecord en cuestion
			for(int dataRecordIndice=0; dataRecordIndice< listaDataRecords.size() &&!parar;dataRecordIndice++)
				if(listaInstancia.get(instanciaEncontrada).getName().contains(listaDataRecords.get(dataRecordIndice).getName())) {
					dataRecordEncontrada=dataRecordIndice;
					parar=true;
				}

			parar=false;
			
			//Buscamos el objeto en cuestion
			for(int objeto=0; objeto<listaObjetos.size()&&!parar;objeto++)
				if(listaDataRecords.get(dataRecordEncontrada).getId().equals(listaObjetos.get(objeto).getId())) {
					parar=true;
					objetoEscritura=listaObjetos.get(objeto);
					objetoEncontrado=objeto;
					dataRecord=false;
				}
				
			listaObjetos.remove(objetoEncontrado);
			listaObjetos.add(objetoEscritura);
			
		}
	}
	
	@SuppressWarnings("null")
	public static void leerContenido(String archivo) throws Exception {
	    FileReader fr = null;
	    BufferedReader br = null;
	    boolean cuerpoComprobado=false;
	 
		try {
			fr = new FileReader(archivo); //leemos el archivo que pasemos por parametro
			br = new BufferedReader(fr);
			String cadena;

			while((cadena = br.readLine())!=null) { //mientras no sea el final
				
				if (cadena.contains("<HadoopExecutionParameter")) { //TABLA DE ExecutionParameter
					leerExecutionParameters(br, cadena);
				}
				
				else if(cadena.contains("</annotations") && !cuerpoComprobado){
					descripcionMapping=descripcionMapping(br,cadena);
					cuerpoComprobado=true;
				}
				else if(cadena.contains("<UserDefinedParameter")){ //TABLA DE Parameter	
					leerParametros(br,cadena);	
				}
				else if(cadena.contains("<AbstractTransformation")){
					AbstractTransformation transformacion;
					transformacion=leerAbstractTransformation(br,cadena);	
					transformacion= new AbstractTransformation(transformacion.getId(), transformacion.getType(), transformacion.getNombre(), transformacion.getCamposTransformacion(),transformacion.getCamposTransformacionPrincipal(),transformacion.getCamposTransformacionDetalle());
					listaTransformacion.add(transformacion);
				}
				else if(cadena.contains("<Instance")){
					Instance instancia;
					instancia=leerInstancia(br,cadena);
					instancia = new Instance(instancia.getId(), instancia.getType(), instancia.getName(),instancia.getBody(), instancia.getfromInstance(), instancia.getToInstance(), instancia.getCampos());
					//System.out.println("NUEVA INSTANCIAAAAAAA");
					listaInstancia.add(instancia);
				}
				else if(cadena.contains("<IObject")){		
					Iobject objeto;
					objeto=leerIObject(br,cadena);
					if(objeto!=null) {
					objeto = new Iobject(objeto.getId(), objeto.getName(),objeto.getType() ,objeto.getRef(), objeto.getCampos());
					listaObjetos.add(objeto);
					}
				}
				else if(cadena.contains("<datarecord")){
					DataRecord dataRecord;
					dataRecord=leerDataRecord(br,cadena);
					dataRecord = new DataRecord(dataRecord.getId(), dataRecord.getName(), dataRecord.getCampos());
					listaDataRecords.add(dataRecord);
				}
			}
		}
		catch(Exception e) {
			throw new Exception("HA OCURRIDO UN ERROR. COMPRUEBE SI HA INTRODUCIDO BIEN EL ARCHIVO.");
		}
		finally {
			if(br!=null){
				br.close();
			}
		}
    }	
	
	private static DataRecord leerDataRecord(BufferedReader br, String cadena) throws IOException {

		//booleano para saber si queremos el tipo y para saber si es un id de referencia o el propio
		boolean tipo_idRef[]= new boolean[2];
		tipo_idRef[0]=true;
		tipo_idRef[1]=false;


		Iterator<String> it;
		DataRecord dataRecord = new DataRecord(null,null,null);

		//creacion de una lista de los campos del objeto
		ArrayList<CamposObjetosDataRecord> listaCampos = new ArrayList<CamposObjetosDataRecord>();

		//Creamos una lista donde meteremos las cosas que nos interesen de la linea
		ArrayList<String> listaClaves = new ArrayList<String>();

		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];

		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);

		meterEnLista(arrayBueno,listaClaves);
		it = listaClaves.iterator(); 

		dataRecord.setId(it.next()); //Id del dataRecord
		it.next(); //LA CONEXION EN ESTA OCASION NO NOS INTERESA
		dataRecord.setName(it.next()); //Nombre del dataRecord

		while(!cadena.contains("</columns")){ //EMPIEZA EL MUNDO DE LEER CAMPOS
			if(cadena.contains("<Column")){
				listaClaves.clear();

				//el ultimo es booleano es para indicar si es una particion o no
				CamposObjetosDataRecord campo = new CamposObjetosDataRecord(null, null, null, null, null,null);

				cadenaDividida=cadena.split(" ");
				arrayBueno= new String[cadenaDividida.length];

				//Metodo para quitar los caracteres innecesarios
				tipo_idRef[0]=false; //no queremos coger el tipo del campo, solo el typeSystem
				reemplazo(cadenaDividida,arrayBueno, null, tipo_idRef); 

				//Metodo para meter en la lista solo lo que nos interese.
				meterEnLista(arrayBueno,listaClaves);
				it=listaClaves.iterator();
				campo.setId(it.next());
				campo.setType(it.next());
				campo.setName(it.next());
				//campo.setNullable(it.next());
				campo.setPrecision(it.next());

				if(campo.getType().equals("decimal"))
					if(it.hasNext())
						campo.setEscala(it.next());
					//else
						//System.out.println("El campo " + campo.getName() + " de la tabla " + dataRecord.getName() + " es un decimal y no tiene puesto la escala");
				else
					campo.setEscala(null);

				vaciarLista(listaClaves);
				listaCampos.add(campo);
				cadena=br.readLine(); //para pasar a la siguiete instruccion

			}
			else
				cadena=br.readLine();
		}

		dataRecord.setCampos(listaCampos);			

		return dataRecord;
	}

	private static boolean descripcionMapping(BufferedReader br, String cadena) throws IOException {
		boolean descripcionMapping=false;
		cadena=br.readLine(); //pasamos a la siguiente frase donde deberia aprecer characteristics, sino no tiene cuerpo
		
			if(cadena.contains("characteristics")){
				descripcionMapping=true;
			}
		return descripcionMapping;
	}

	//metodo donde vemos que parte de la cadena nos interesa tener o no
	public static void nosInteresa(String array[], String arrayBueno[], boolean from_to[], boolean tipo_idRef[]){
		int contador=0;
		boolean tipoCogido=false;
		boolean precisionCogida=false;
		boolean escalaCogida=false;
		
		for(int i=0; i<array.length;i++){
			
			if(array[i].contains("fromInstance")){
				arrayBueno[contador]=array[i];
				contador++;
			}
			
			if(array[i].contains("toInstance")){
				arrayBueno[contador]=array[i];
				contador++;
			}
			
			
			if(array[i].contains("fromPort")) {
				arrayBueno[contador]=array[i];
				contador++;
				from_to[0]=true; //tiene el campo fromPort
			}
			if(array[i].contains("transformationField")){
				arrayBueno[contador]=array[i];
				contador++;
			}
			
			if(array[i].contains("imx:id")) {
				arrayBueno[contador]=array[i];
				contador++;
				if(array[i].contains("ref"))
					tipo_idRef[1]=true;
			}
			
			if(array[i].contains("name")){
				arrayBueno[contador]=array[i];
				contador++;		
			}	
			
			if(array[i].contains("body")){
				arrayBueno[contador]=array[i];
				contador++;		
			}	
			
			if(array[i].contains("toPorts")){
				arrayBueno[contador]=array[i];
				contador++;		
				from_to[1]=true; //tiene el campo toPort
			}
			
			if(array[i].contains("structuralFeature")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			
			if(array[i].contains("connectionName")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			
			if(array[i].contains("valueLiteral")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			if(array[i].contains("column")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			if((array[i].contains("odbcPrecision") || array[i].contains("precision")) && !precisionCogida){
				arrayBueno[contador]=array[i];
				contador++;		
				precisionCogida=true;
			}
			if(array[i].contains("feature")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
//			if(array[i].contains("nullable")){
//				arrayBueno[contador]=array[i];
//				contador++;		
//			}
			if (array[i].contains("value.")) {
				arrayBueno[contador] = array[i];
				contador++;
				
			}
			
			if(array[i].contains("type") && tipo_idRef[0]){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			
			if((array[i].contains("odbcScale") || array[i].contains("scale")) && !escalaCogida ){
				arrayBueno[contador]=array[i];
				contador++;	
				falloXML=true; //Esto lo usaremos cuando en el XML un campo este mal por falta de algun atributo o algo asi
				escalaCogida=true;
			}
			
			if(array[i].contains("typesystem%2") && !array[i].contains("odbc")){ //cogemos el odbcType
				arrayBueno[contador]=array[i];
				contador++;
				tipoCogido=true;
			}
			
			if(array[i].contains("typesystem%2") && array[i].contains("odbc") && !tipoCogido){
				arrayBueno[contador]=array[i];
				contador++;
			}
		}
	}
	
	//METODO PARA QUITAR TODOS LOS CARACTERES INNECESARIOS
	@SuppressWarnings("null")
	public static void reemplazo(String array[], String[] arrayBueno, boolean from_to[],boolean tipo_idRef[]){
		nosInteresa(array, arrayBueno,from_to,tipo_idRef);

		for(int i=0; i<arrayBueno.length; i++){
			if(arrayBueno[i]!=null) {
				Pattern patron= Pattern.compile("[/><]");
				Matcher caja= patron.matcher(arrayBueno[i]);
				arrayBueno[i]=caja.replaceAll("");	
				arrayBueno[i]=arrayBueno[i].replace("imx:id=", "");
				arrayBueno[i]=arrayBueno[i].replace("name=", "");
				arrayBueno[i]=arrayBueno[i].replace("body=", "");
				arrayBueno[i]=arrayBueno[i].replace("structuralFeature=", "");
				arrayBueno[i]=arrayBueno[i].replace("toPorts=", "");
				arrayBueno[i]=arrayBueno[i].replace("connectionName=", "");
				arrayBueno[i]=arrayBueno[i].replace("valueLiteral=", "");
				arrayBueno[i]=arrayBueno[i].replace("feature=", "");
				arrayBueno[i]=arrayBueno[i].replace("column=", "");
				arrayBueno[i]=arrayBueno[i].replace("odbcPrecision=", "");
				arrayBueno[i]=arrayBueno[i].replace("precision=", "");
				arrayBueno[i]=arrayBueno[i].replace("odbcScale=", "");
				arrayBueno[i]=arrayBueno[i].replace("scale=", "");
				//arrayBueno[i]=arrayBueno[i].replace("nullable=", "");
				arrayBueno[i]=arrayBueno[i].replace("value.=", "");
				arrayBueno[i]=arrayBueno[i].replace("imx:idref=", "");
				arrayBueno[i]=arrayBueno[i].replace("xsi:type=", "");
				arrayBueno[i]=arrayBueno[i].replace("type=", "");
				arrayBueno[i]=arrayBueno[i].replace("odbcType=","");
				arrayBueno[i]=arrayBueno[i].replace("smd:com.informatica.adapter.Hive.HiveTypeSystem.typesystem%2F","");
				arrayBueno[i]=arrayBueno[i].replace("smd:com.informatica.metadata.seed.odbc.ODBC.typesystem%2Fvarchar", "string");
				arrayBueno[i]=arrayBueno[i].replace("smd:com.informatica.metadata.seed.odbc.ODBC.typesystem%2F", "");
				arrayBueno[i]=arrayBueno[i].replace("smd:com.informatica.metadata.seed.platform.Platform.typesystem%2F", "");
				arrayBueno[i]=arrayBueno[i].replace("fromInstance=", "");
				arrayBueno[i]=arrayBueno[i].replace("fromPort==", "");
				arrayBueno[i]=arrayBueno[i].replace("fromPort=", "");
				arrayBueno[i]=arrayBueno[i].replace("toInstance=", "");
				arrayBueno[i]=arrayBueno[i].replace("transformationField=", "");

				arrayBueno[i]=arrayBueno[i].replaceAll("\"", ""); //para quitar las comillas
			}
		}	
	}
	
	public static void meterEnLista(String arrayBueno[], ArrayList<String> claves){
		for(int i=0;i<arrayBueno.length;i++){
			if(arrayBueno[i]!=null)
				claves.add(arrayBueno[i]);
		}
		
	}
	
	public static void reiniciarArray(String arrayBueno[]){
		for(int i=0; i<arrayBueno.length;i++)
			arrayBueno[i]=null;
		
		
	}
	
	public static void vaciarLista(ArrayList<String> listaClaves){
		listaClaves.clear();
	}

	@SuppressWarnings("null")
	private static Instance leerInstancia(BufferedReader br, String cadena) throws Exception{
		boolean parar=false;
		boolean falloBody=false; //para controlar cuando una instancia tiene cuerpo o no
		boolean type_idRef[] = new boolean[2];
		type_idRef[0]=false;
		type_idRef[1]=false;
		//booleano que nos sirve para saber si queremos coger el type o no
		//en este caso solo queremos el booleano cuando sea un campo de una transfromacion o IOBJECT
		//luego en las instancias siempre ser� falso.
		Iterator<String> it;
		
		//creacion de una lista de los campos de una instancia
		ArrayList<Instance.Campos> listaCampos = new ArrayList<Instance.Campos>();
		
		//Creamos una nueva instancia
		Instance nuevaInstancia = new Instance(null,null,null,null,null,null,null);
		//Creamos una lista donde meteremos las cosas que nos interesen de la linea
		ArrayList<String> listaClaves = new ArrayList<String>();
		//en Array Bueno estaran solo las palabras que nos interesen de la cadena dividida
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];
		
		
		//Metodo para quitar los caracteres innecesarios
		reemplazo(cadenaDividida,arrayBueno, null,type_idRef); 
		
		//Metodo para meter en la lista solo lo que nos interese.
		meterEnLista(arrayBueno,listaClaves);
		
		it = listaClaves.iterator();
		
		nuevaInstancia.setId(it.next()); //Tenemos id de la instancia
		nuevaInstancia.setName(it.next()); //tenemos nombre de la instancia
		
		listaClaves.clear(); //limpiamos la lista
		
		while(!cadena.contains("annotations") && !parar)
			if((cadena.contains("<fromOutlineLinks")) || cadena.contains("ports")) {
				parar=true; //para si no encontramos annotations y si otra etiqueta, esto ocurrira en caso de fallo (no tiene cuerpo la instancia en cuestion).
				falloBody=true;
			}
			else if(cadena.contains("annotations")) //para si encontramos annotations, porque sabemos que si va a tener body la instancia
				parar=true;
			else 
				cadena = br.readLine(); //para que avance si aun no ha encontrado annotations ni otra etiqueta
		
		//Si no tiene cuerpo lanzamos la excepcion correspondiente
		if(falloBody){ 
			try {
				throw new InstanciaSinDescrip("LA INSTANCIA " + nuevaInstancia.getName() + " NO TIENE DESCRIPCION PUESTA ");

			}catch(InstanciaSinDescrip e){
				System.out.println(e.getMessage());
			}
		}
		
		else{ //Aqui entrara cuando si hay body
			//leerBody
			cadena = br.readLine(); //para que avance ya que aqui llega con <annotations>
			
			cadenaDividida=cadena.split(" ");
			arrayBueno= new String[cadenaDividida.length];
			
			//Metodo para quitar los caracteres innecesarios
			reemplazo(cadenaDividida,arrayBueno, null,type_idRef); 
			
			//Metodo para meter en la lista solo lo que nos interese.
			meterEnLista(arrayBueno,listaClaves);
			it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
			it.next(); //para saltarnos el ID del annotation
			nuevaInstancia.setBody(it.next());
			listaClaves.clear(); //limpiamos la lista
		}
		
		if(!nuevaInstancia.getName().contains("Write") && !nuevaInstancia.getName().contains("Escritura")){
			while(!cadena.contains("fromOutlineLinks"))
				cadena = br.readLine(); //para que avance

			cadena = br.readLine(); //para que avance
			cadenaDividida=cadena.split(" ");
			arrayBueno= new String[cadenaDividida.length];

			//Metodo para quitar los caracteres innecesarios
			reemplazo(cadenaDividida,arrayBueno, null,type_idRef); 

			//Metodo para meter en la lista solo lo que nos interese.
			meterEnLista(arrayBueno,listaClaves);
			it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
			it.next(); //NOS SALTAMOS EL ID, NO NOS INTERESA
			nuevaInstancia.setfromInstance(it.next());
			nuevaInstancia.setToInstance(it.next());
		}


		//que avance hasta que vea la etiqueta de <ports>
		while(!cadena.contains("<ports"))
			cadena = br.readLine(); //para que avance

		while(!cadena.contains("</ports>")) { //mientras no sea el final
			if(cadena.contains("<NestedPort") || (cadena.contains("<TransformationFieldPort") && (!nuevaInstancia.getName().contains("Lectura") && !nuevaInstancia.getName().contains("Read")  && !nuevaInstancia.getName().contains("Write") && !nuevaInstancia.getName().contains("Escritura")))){
				boolean from_to[] = new boolean[2]; //Array para saber si tiene from y to el campo.

				listaClaves.clear(); //limpiamos la lista de elemenos 
				Instance.Campos campo = nuevaInstancia.new Campos(null, null, null, null, null,null);

				cadenaDividida=cadena.split(" ");
				arrayBueno= new String[cadenaDividida.length];

				//Metodo para quitar los caracteres innecesarios
				reemplazo(cadenaDividida,arrayBueno, from_to,type_idRef); 

				//Metodo para meter en la lista solo lo que nos interese.
				meterEnLista(arrayBueno,listaClaves);

				//Cuando la instancia es un source
				if(nuevaInstancia.getName().contains("Lectura") || nuevaInstancia.getName().contains("Read") ){
					campo.setTransformationField(null); //Source y Target no tienen este atributo
					it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
					campo.setId(it.next());
					campo.setToPorts(it.next());
					campo.setStructural_feature(it.next());
					listaCampos.add(campo);
					cadena = br.readLine(); //para que avance
				}

				//cuando la tabla es un target
				else if(nuevaInstancia.getName().contains("Escritura") || nuevaInstancia.getName().contains("Write") ){
					campo.setTransformationField(null); //Source y Target no tienen este atributo
					it=listaClaves.iterator();
					campo.setId(it.next());
					campo.setFromPort(it.next());
					campo.setStructural_feature(it.next());
					campo.setToPorts(null); //Como es escritura no va a ningun lado
					listaCampos.add(campo);
					cadena = br.readLine(); //para que avance
				}
				//cuando la tabla es una transformacion intermedia
				else{
					//Este atributo es solo para targets/source
					campo.setStructural_feature(null);

					if(from_to[0]){ //vemos si tiene fromPorts
						it=listaClaves.iterator();
						campo.setId(it.next());
						campo.setFromPort(it.next());

						if(from_to[1]) //vemos si tiene el toPorts
							campo.setToPorts(it.next());
						else
							campo.setToPorts(null);

						campo.setTransformationField(it.next());
						listaCampos.add(campo);
						cadena = br.readLine(); //para que avance
					}
					else{ //entra aqui si no tiene fromPorts
						campo.setFromPort(null); 

						it=listaClaves.iterator();
						campo.setId(it.next());

						if(from_to[1])
							campo.setToPorts(it.next());
						else
							campo.setToPorts(null);

						campo.setTransformationField(it.next());

						//a�adimos este campo a la lista de campos
						listaCampos.add(campo);
						cadena = br.readLine(); //para que avance							
					}
				}
			}
			else
				cadena = br.readLine(); //para que avance

		}
		//a�adimos la lista de campos a la instancia
		nuevaInstancia.setCampos(listaCampos);
		return nuevaInstancia;
	}
	
	private static void obtenerCamposTransformacionSourceTarget(AbstractTransformation nuevaTransformacion, BufferedReader br, String cadena, boolean tipo_idRef[], ArrayList<String> listaTransformaciones, Iterator<String> it, String cadenaDividida[], String arrayBueno[],	ArrayList<AbstractTransformation.Campo> listaCampos ) throws IOException{
		while(!cadena.contains("</relationalFields")) { //Mientras no sea el final de los campos
			if(cadena.contains("<RelationalField")){ // Si encuentra un campo
				AbstractTransformation.Campo campo = nuevaTransformacion.new Campo(null, null, null, null, null,null,null);
				falloXML=false;

				cadenaDividida=cadena.split(" ");
				arrayBueno= new String[cadenaDividida.length];

				tipo_idRef[0]=false; //en los campos no queremos el tipo

				reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef); //NULL XQ NO NOS INTERESA NI LOS PUERTOS NI EL TIPO
				meterEnLista(arrayBueno,listaTransformaciones);
				it = listaTransformaciones.iterator(); 
					//FALTA VER Q PASA SI LE QUITAS EL NOMBRE A UN CAMPO O ALGUN OTRO BIXO
					if(listaTransformaciones.size() <= 6 && falloXML || listaTransformaciones.size() <= 5){
						campo.setID(it.next());
						campo.setColumna(it.next());
						campo.setfeature(it.next());
						falloReal=true;
					}
					else{

						campo.setID(it.next());
						campo.setColumna(it.next());
						campo.setfeature(it.next());
						campo.setName(it.next());
						campo.setPrecision(it.next());

						if(listaTransformaciones.size() == 6 && !falloXML) {
							campo.setType(it.next());
							campo.setEscala(null); //ESTO OCURRE CUANDO NO ES UN DECIMAL POR LO TANTO NO TIENE ESCALA
						}					
						else{
							campo.setEscala(it.next());
							campo.setType(it.next());	
						}	
					}

				listaCampos.add(campo);
				vaciarLista(listaTransformaciones);	
			}
			cadena = br.readLine(); //para que avance 
		}
	}	
	
	private static void obtenerCamposTransformacionJoiner(AbstractTransformation nuevaTransformacion, BufferedReader br, String cadena, boolean tipo_idRef[], ArrayList<String> listaTransformaciones, Iterator<String> it, String cadenaDividida[], String arrayBueno[],	ArrayList<AbstractTransformation.Campo> listaCampos ) throws IOException{
		while(!cadena.contains("</joinerFields")) { //Mientras no sea el final de los campos
			if(cadena.contains("<JoinerField")){ // Si encuentra un campo
				AbstractTransformation.Campo campo = nuevaTransformacion.new Campo(null, null, null, null, null,null,null);
				falloXML=false;

				cadenaDividida=cadena.split(" ");
				arrayBueno= new String[cadenaDividida.length];

				tipo_idRef[0]=false; //en los campos no queremos el tipo, solo el typesystem

				vaciarLista(listaTransformaciones);	
				reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef); //NULL XQ NO NOS INTERESA NI LOS PUERTOS NI EL TIPO
				meterEnLista(arrayBueno,listaTransformaciones);
				
				it = listaTransformaciones.iterator(); 
					
					if(listaTransformaciones.size() <= 4 && falloXML || listaTransformaciones.size() <= 3 ){
						campo.setID(it.next());
						falloReal=true;
					}
					else{

						campo.setID(it.next());
						campo.setType(it.next());
						campo.setPrecision(it.next());
						
						if(listaTransformaciones.size() == 4 && !falloXML) {
							campo.setName(it.next());
							campo.setEscala(null); //ESTO OCURRE CUANDO NO ES UN DECIMAL POR LO TANTO NO TIENE ESCALA
						}				
						else{
							campo.setEscala(it.next());
							campo.setName(it.next());	
						}	
					}

				listaCampos.add(campo);
				vaciarLista(listaTransformaciones);	
			}
			cadena = br.readLine(); //para que avance 
		}
	}	
	
	private static void obtenerCamposTransformacionExpressionFilter(AbstractTransformation nuevaTransformacion, BufferedReader br, String cadena, boolean tipo_idRef[], ArrayList<String> listaTransformaciones, Iterator<String> it, String cadenaDividida[], String arrayBueno[],	ArrayList<AbstractTransformation.Campo> listaCampos ) throws IOException{
		while(!cadena.contains("</expressionFields") && !cadena.contains("</filterFields")) { //Mientras no sea el final de los campos
			if(cadena.contains("<ExpressionField") || cadena.contains("<FilterField")){ // Si encuentra un campo
				AbstractTransformation.Campo campo = nuevaTransformacion.new Campo(null, null, null, null, null,null,null);
				falloXML=false;

				cadenaDividida=cadena.split(" ");
				arrayBueno= new String[cadenaDividida.length];

				tipo_idRef[0]=false; //en los campos no queremos el tipo, solo el typesystem

				reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef); //NULL XQ NO NOS INTERESA NI LOS PUERTOS NI EL TIPO
				meterEnLista(arrayBueno,listaTransformaciones);
				it = listaTransformaciones.iterator(); 
					
					if(listaTransformaciones.size() <= 4 && falloXML || listaTransformaciones.size() <= 3 ){
						campo.setID(it.next());
						falloReal=true;
					}
					else{

						campo.setID(it.next());
						campo.setType(it.next());
						campo.setPrecision(it.next());
						
						if(listaTransformaciones.size() == 4 && !falloXML) {
							campo.setName(it.next());
							campo.setEscala(null); //ESTO OCURRE CUANDO NO ES UN DECIMAL POR LO TANTO NO TIENE ESCALA
						}				
						else{
							campo.setEscala(it.next());
							campo.setName(it.next());	
						}	
					}

				listaCampos.add(campo);
				vaciarLista(listaTransformaciones);	
			}
			cadena = br.readLine(); //para que avance 
		}
	}	

	private static void obtenerCamposTransformacionUnion(AbstractTransformation nuevaTransformacion, BufferedReader br, String cadena, boolean tipo_idRef[], ArrayList<String> listaTransformaciones, Iterator<String> it, String cadenaDividida[], String arrayBueno[],	ArrayList<AbstractTransformation.Campo> listaCampos ) throws IOException{
		while(!cadena.contains("</unionFields")) { //Mientras no sea el final de los campos
			if(cadena.contains("<UnionField")){ // Si encuentra un campo
				AbstractTransformation.Campo campo = nuevaTransformacion.new Campo(null, null, null, null, null,null,null);
				falloXML=false;

				cadenaDividida=cadena.split(" ");
				arrayBueno= new String[cadenaDividida.length];

				tipo_idRef[0]=false; //en los campos no queremos el tipo, solo el typesystem

				vaciarLista(listaTransformaciones);	
				reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef); //NULL XQ NO NOS INTERESA NI LOS PUERTOS NI EL TIPO
				meterEnLista(arrayBueno,listaTransformaciones);
				
				it = listaTransformaciones.iterator(); 
					
					if(listaTransformaciones.size() <= 4 && falloXML || listaTransformaciones.size() <= 3 ){
						campo.setID(it.next());
						falloReal=true;
					}
					else{

						campo.setID(it.next());
						campo.setType(it.next());
						campo.setPrecision(it.next());
						
						if(listaTransformaciones.size() == 4 && !falloXML) {
							campo.setName(it.next());
							campo.setEscala(null); //ESTO OCURRE CUANDO NO ES UN DECIMAL POR LO TANTO NO TIENE ESCALA
						}				
						else{
							campo.setEscala(it.next());
							campo.setName(it.next());	
						}	
					}

				listaCampos.add(campo);
				vaciarLista(listaTransformaciones);	
			}
			cadena = br.readLine(); //para que avance 
		}
	}	
	
	private static AbstractTransformation leerAbstractTransformation(BufferedReader br, String cadena) throws IOException{
		Iterator<String> it;
		ArrayList<String> listaTransformaciones = new ArrayList<String>();
		AbstractTransformation nuevaTransformacion = new AbstractTransformation(null,null,null,null,null,null);
		boolean tipo_idRef[] = new boolean[2];
		tipo_idRef[0]=true; //TRUE XQ NOS INTERESA COGER EL TIPO
		tipo_idRef[1]=false; //ESTO LO QUEREMOS SOLO PARA LOS OBJETOS
		
		ArrayList<AbstractTransformation.Campo> listaCamposSalida = new ArrayList<AbstractTransformation.Campo>();
		ArrayList<AbstractTransformation.Campo> listaCamposPrincipal = new ArrayList<AbstractTransformation.Campo>();
		ArrayList<AbstractTransformation.Campo> listaCamposDetalle = new ArrayList<AbstractTransformation.Campo>();

		
		
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];

		reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
		meterEnLista(arrayBueno,listaTransformaciones);
		it = listaTransformaciones.iterator(); 
		nuevaTransformacion.setId(it.next());//ya tenemos el id de las AbstractTransformation(anonymousDso)
		nuevaTransformacion.setType(it.next());
		nuevaTransformacion.setNombre(it.next());//ya tenemos el name de las AbstractTransformation
		vaciarLista(listaTransformaciones);	
		
		if(nuevaTransformacion.getType().contains("source") || nuevaTransformacion.getType().contains("target")){
			obtenerCamposTransformacionSourceTarget(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposSalida);
			nuevaTransformacion.setCamposTransformacion(listaCamposSalida); //salida-OUTPUT o el único
			nuevaTransformacion.setCamposTransformacionPrincipal(null);
			nuevaTransformacion.setCamposTransformacionDetalle(null);
		}
			
		else if(nuevaTransformacion.getType().contains("expression") || nuevaTransformacion.getType().contains("filter")){
			obtenerCamposTransformacionExpressionFilter(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposSalida);
			nuevaTransformacion.setCamposTransformacion(listaCamposSalida); //salida-OUTPUT o el único
			nuevaTransformacion.setCamposTransformacionDetalle(null);
			nuevaTransformacion.setCamposTransformacionPrincipal(null);
		}
			
		else if(nuevaTransformacion.getType().contains("joiner")){
			while(!cadena.contains("JoinerDataInterface"))
				cadena=br.readLine();
			
			cadenaDividida=cadena.split(" ");
			arrayBueno = new String[cadenaDividida.length];
			reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
			meterEnLista(arrayBueno,listaTransformaciones);
				
			it = listaTransformaciones.iterator(); 
			it.next(); //NOS SALTAMOS EL ID DEL UNIONDATAINTERFACE
			it.next(); //NOS SALTAMOS EL TYPE DEL UNIONDATAINTERFACE
			dataInterface=it.next(); //NOS QUEDAMOS CON EL NOMBRE DEL UNIONDATAINTERFACE
			
			if(dataInterface.equals("Salida")){
				obtenerCamposTransformacionJoiner(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposSalida);
				nuevaTransformacion.setCamposTransformacion(listaCamposSalida); //salida-OUTPUT o el único
				dataInterface="";
			}
			
			cadena=br.readLine();
			cadena=br.readLine();

			
			while(!cadena.contains("JoinerDataInterface"))
				cadena=br.readLine();
			
			cadenaDividida=cadena.split(" ");
			arrayBueno = new String[cadenaDividida.length];
			tipo_idRef[0]=true;
			reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
			meterEnLista(arrayBueno,listaTransformaciones);
				
			it = listaTransformaciones.iterator(); 
			it.next(); //NOS SALTAMOS EL ID DEL UNIONDATAINTERFACE
			it.next(); //NOS SALTAMOS EL TYPE DEL UNIONDATAINTERFACE
			dataInterface=it.next(); //NOS QUEDAMOS CON EL NOMBRE DEL UNIONDATAINTERFACE
			
			if(dataInterface.equals("Detalle")){
				obtenerCamposTransformacionJoiner(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposDetalle);
				nuevaTransformacion.setCamposTransformacionDetalle(listaCamposDetalle); //Leemos los campos de Detalle
				dataInterface="";
			}
			
			cadena=br.readLine();
			cadena=br.readLine();

			
			while(!cadena.contains("JoinerDataInterface"))
				cadena=br.readLine();
			
			cadenaDividida=cadena.split(" ");
			arrayBueno = new String[cadenaDividida.length];
			tipo_idRef[0]=true;
			reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
			meterEnLista(arrayBueno,listaTransformaciones);
				
			it = listaTransformaciones.iterator(); 
			it.next(); //NOS SALTAMOS EL ID DEL UNIONDATAINTERFACE
			it.next(); //NOS SALTAMOS EL TYPE DEL UNIONDATAINTERFACE
			dataInterface=it.next(); //NOS QUEDAMOS CON EL NOMBRE DEL UNIONDATAINTERFACE
			
			if(dataInterface.equals("Principal")){
				obtenerCamposTransformacionJoiner(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposPrincipal);
				nuevaTransformacion.setCamposTransformacionPrincipal(listaCamposPrincipal); //Leemos los campos de Principal
				dataInterface="";
			}
		}
			
		else if(nuevaTransformacion.getType().contains("union")){
			while(!cadena.contains("UnionDataInterface"))
				cadena=br.readLine();
			
			cadenaDividida=cadena.split(" ");
			arrayBueno = new String[cadenaDividida.length];
			reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
			meterEnLista(arrayBueno,listaTransformaciones);
				
			it = listaTransformaciones.iterator(); 
			it.next(); //NOS SALTAMOS EL ID DEL UNIONDATAINTERFACE
			it.next(); //NOS SALTAMOS EL TYPE DEL UNIONDATAINTERFACE
			dataInterface=it.next(); //NOS QUEDAMOS CON EL NOMBRE DEL UNIONDATAINTERFACE
			
			if(dataInterface.equals("OUTPUT")){
				obtenerCamposTransformacionUnion(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposSalida);
				nuevaTransformacion.setCamposTransformacion(listaCamposSalida);
				dataInterface="";
			}
			
			cadena=br.readLine();
			cadena=br.readLine();
			
			while(!cadena.contains("UnionDataInterface"))
				cadena=br.readLine();
			
			cadenaDividida=cadena.split(" ");
			arrayBueno = new String[cadenaDividida.length];
			tipo_idRef[0]=true; //para que pille el tipo y entonces no reviente
			reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
			meterEnLista(arrayBueno,listaTransformaciones);
				
			it = listaTransformaciones.iterator(); 
			it.next(); //NOS SALTAMOS EL ID DEL UNIONDATAINTERFACE
			it.next(); //NOS SALTAMOS EL TYPE DEL UNIONDATAINTERFACE
			dataInterface=it.next(); //NOS QUEDAMOS CON EL NOMBRE DEL UNIONDATAINTERFACE
			
			if(!dataInterface.equals("OUTPUT")){ //sera INPUT
				obtenerCamposTransformacionUnion(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposPrincipal);
				nuevaTransformacion.setCamposTransformacionPrincipal(listaCamposPrincipal); //input
				dataInterface="";
			}
			
			cadena=br.readLine();
			cadena=br.readLine();
			
			while(!cadena.contains("UnionDataInterface"))
				cadena=br.readLine();
			
			cadenaDividida=cadena.split(" ");
			arrayBueno = new String[cadenaDividida.length];
			tipo_idRef[0]=true; //para que pille el tipo y entonces no reviente
			reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);
			meterEnLista(arrayBueno,listaTransformaciones);
				
			it = listaTransformaciones.iterator(); 
			it.next(); //NOS SALTAMOS EL ID DEL UNIONDATAINTERFACE
			it.next(); //NOS SALTAMOS EL TYPE DEL UNIONDATAINTERFACE
			dataInterface=it.next(); //NOS QUEDAMOS CON EL NOMBRE DEL UNIONDATAINTERFACE
			
			if(!dataInterface.equals("OUTPUT")){ //sera INPUT
				obtenerCamposTransformacionUnion(nuevaTransformacion,br,cadena,tipo_idRef,listaTransformaciones,it,cadenaDividida,arrayBueno, listaCamposDetalle);
				nuevaTransformacion.setCamposTransformacionDetalle(listaCamposDetalle); //input
				dataInterface="";
			}
			
			nuevaTransformacion.setCamposTransformacionDetalle(null);
		}

		return nuevaTransformacion;
	}
	
	private static void leerParametros(BufferedReader br, String cadena) throws ParamSinDescripcion, IOException{
		//creamos un parametro que va a ser un hashMap
		Parametro parametro = new Parametro(null,null);
		boolean cuerpo=false;
		boolean parar=false;
		boolean type_idRef[]= new boolean[2];
		String valor;
		//booleano que nos sirve para saber si queremos coger el type o no
		//en este caso solo queremos el booleano cuando sea un campo de una transfromacion o IOBJECT
		//luego en las instancias siempre ser� falso.
		
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];

						
			
		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		reemplazo(cadenaDividida,arrayBueno, null,type_idRef);
		parametro.setId(arrayBueno[0]);
		parametro.setName(arrayBueno[1]);

		reiniciarArray(arrayBueno);

			while(!parar){
				if(cadena.contains("<defaultValue"))
					parar=true;
				else if(cadena.contains("<annotations")){
					parar=true;
					cuerpo=true;
				}
				else
					cadena = br.readLine(); //para que avance
			}

			if(!cuerpo)
				try{
					throw new ParamSinDescripcion("EL PARAMETRO " + parametro.getName() + " NO TIENE DESCRIPCION PUESTA");
				}catch(ParamSinDescripcion e) {
					System.out.println(e.getLocalizedMessage());
				}

			//luego lo que nos interesa es el valor del parametro
			while(!cadena.contains("<defaultValue")) 
				cadena = br.readLine(); //para que avance
				
			cadenaDividida=cadena.split(" ");
			arrayBueno= new String[cadenaDividida.length];
			reemplazo(cadenaDividida,arrayBueno, null,type_idRef);
			valor=arrayBueno[1];
			reiniciarArray(arrayBueno);	
			cadena = br.readLine(); //para que avance
			
			while(!cadena.contains("</UserDefinedParameter"))
				cadena = br.readLine(); //para que avance
	
			tablaParametros.put(parametro, valor);
	}
	
	private static Iobject leerIObject(BufferedReader br, String cadena) throws IOException{

		//booleano para saber si queremos el tipo y para saber si es un id de referencia o el propio
		boolean tipo_idRef[]= new boolean[2];
		tipo_idRef[0]=true;
		tipo_idRef[1]=false;


		Iterator<String> it;
		Iobject nuevoObjeto = new Iobject(null,null,null,tipo_idRef[1],null);

		//creacion de una lista de los campos del objeto
		ArrayList<CamposObjetosDataRecord> listaCampos = new ArrayList<CamposObjetosDataRecord>();

		//Creamos una lista donde meteremos las cosas que nos interesen de la linea
		ArrayList<String> listaClaves = new ArrayList<String>();

		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];

		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		reemplazo(cadenaDividida,arrayBueno,null,tipo_idRef);

		if(!arrayBueno[1].contains("folder")) {
			meterEnLista(arrayBueno,listaClaves);
			it = listaClaves.iterator(); 

			if(!tipo_idRef[1]) {
				nuevoObjeto.setId(it.next()); //Id del Objeto
				nuevoObjeto.setType(it.next()); //para saber que es un dataRecord
				it.next(); //para saltarnos el connectionName del iObject ya que no nos interesa
				nuevoObjeto.setName(it.next()); //Nombre del objeto
			}
			else {
				nuevoObjeto.setId(it.next()); //Id del Objeto
				nuevoObjeto.setType(it.next()); //para saber que es un dataRecord
			}

			nuevoObjeto.setRef(tipo_idRef[1]); //ponemos si es un IDRef o no

			if(!nuevoObjeto.getRef()) {
				while(!cadena.contains("</columns")){ //EMPIEZA EL MUNDO DE LEER CAMPOS
					if(cadena.contains("<Column")){
						listaClaves.clear();

						//el ultimo es booleano es para indicar si es una particion o no
						CamposObjetosDataRecord campo = new CamposObjetosDataRecord(null, null, null, null, null,null);

						cadenaDividida=cadena.split(" ");
						arrayBueno= new String[cadenaDividida.length];

						//Metodo para quitar los caracteres innecesarios
						tipo_idRef[0]=false; //no queremos coger el tipo del campo, solo el typeSystem
						reemplazo(cadenaDividida,arrayBueno, null, tipo_idRef); 

						//Metodo para meter en la lista solo lo que nos interese.
						meterEnLista(arrayBueno,listaClaves);
						it=listaClaves.iterator();
						campo.setId(it.next());
						campo.setType(it.next());
						campo.setName(it.next());
						//campo.setNullable(it.next());
						campo.setPrecision(it.next());

						if(campo.getType().equals("decimal"))
							if(it.hasNext())
									campo.setEscala(it.next());
							//else
								//System.out.println("El campo " + campo.getName() + " de la tabla " + nuevoObjeto.getName() + " es un decimal y no tiene puesto la escala");
						else
							campo.setEscala(null);

						vaciarLista(listaClaves);
						listaCampos.add(campo);
						cadena=br.readLine(); //para pasar a la siguiete instruccion

					}
					else
						cadena=br.readLine();
				}
				nuevoObjeto.setCampos(listaCampos);			
			}
		}
		else
			return null;
		
		return nuevoObjeto;
	}
	
	private static void leerExecutionParameters(BufferedReader br, String cadena) throws IOException {

		Iterator<String> it;
		ExecutionParameters executionParameters = new ExecutionParameters(null, null);
		ArrayList<String> listaParametros = new ArrayList<String>();
		boolean type_idRef[] = new boolean[2];

		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];


		//Dividimos la cadena para leer las propiedades individualmente
		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		// Metodo para quitar los caracteres innecesarios
		reemplazo(cadenaDividida, arrayBueno, null, type_idRef);

		// Metodo para meter los reemplazos en un arratList
		meterEnLista(arrayBueno, listaParametros);

		it = listaParametros.iterator();
		//Metemos los datos en el arraylist principal de executionParameters

		if(listaParametros.size() >=2){
			executionParameters.setValue(it.next());
			executionParameters.setName(it.next());
		}//Mostramos los datos del arrayList
		

		vaciarLista(listaParametros);			

		tablaExecutionParameters.put(executionParameters.getValue(), executionParameters.getName());
	}
}
	

