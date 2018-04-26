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
	
	private static HashMap<String, String> tablaExecutionParameters = new HashMap<String, String>();
	private static HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();
	
	public static ArrayList<Instance> getListaInstancia(){
		return listaInstancia;
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
	
	
	@SuppressWarnings("null")
	public static void leerContenido(String archivo) throws Exception {
	    FileReader fr = null;
	    BufferedReader br = null;
	 
		try {
			fr = new FileReader(archivo); //leemos el archivo que pasemos por parametro
			br = new BufferedReader(fr);
			String cadena;

			while((cadena = br.readLine())!=null) { //mientras no sea el final
				
				if (cadena.contains("<HadoopExecutionParameter")) {
					leerExecutionParameters(br, cadena);
				}
				else if(cadena.contains("<UserDefinedParameter")){ //PARAMETROS
					//Cada parametro sera un hashMap que tiene asociado
					//un id y un nombre.
				
					//En este mismo metodo a�adimos el parametro con un valor a tablaParametros.
					leerParametros(br,cadena);	
				}
//				else if(cadena.contains("<transformations")){
//					//CREAR UN OBJETO DE LA CLASE ABSTRACTTRANSFORMATION
//					AbstractTransformation transformacion;
//					transformacion=leerAbstractTransformation(br,cadena);	
//					transformacion= new AbstractTransformation(transformacion.getId(), transformacion.getNombre(), transformacion.getType(), transformacion.getActive(), transformacion.getCampos());
//					listaTransformacion.add(transformacion);
//				}
				else if(cadena.contains("<Instance")){
					//CREAR UN OBJETO DE LA CLASE INSTANCIA
					//id, name, body, campos
					Instance instancia;
					instancia=leerInstancia(br,cadena);
					instancia = new Instance(instancia.getId(), instancia.getName(), instancia.getBody(), instancia.getCampos());
					//System.out.println("NUEVA INSTANCIAAAAAAA");
					listaInstancia.add(instancia);
				}
				else if(cadena.contains("<IObject")){		
					Iobject objeto;
					objeto=leerIObject(br,cadena);
					objeto = new Iobject(objeto.gedId(), objeto.getName(),objeto.getType() ,objeto.getRef(), objeto.getCampos());
					listaObjetos.add(objeto);
					
				}
			}
		}
		catch(Exception e) {
			throw new Exception("ESE ARCHIVO NO EXISTE. REEVISELO");
		}
		finally {
			if(br!=null){
				br.close();
			}
		}
    }	
	
	//metodo para mostrar una instancia (para probar)
//	private static void mostrarInstancia(Instance instancia) {
//		System.out.println(instancia.getId());
//		System.out.println(instancia.getName());
//		System.out.println(instancia.getBody());
//		Iterator<Instance.Campos> it;
//		Instance.Campos campo;
//
//		it = instancia.getCampos().iterator();
//		int contador=0;
//		while(it.hasNext()){
//			campo = it.next();
//			System.out.println("NUEVO CAMPOOOO");
//			System.out.println(campo.getId());
//			System.out.println(campo.getFromPorts());
//			System.out.println(campo.getToPorts());
//			System.out.println(campo.getStructural_feature());
//			System.out.println(campo.getTransformationField());
//		}
//	}
	
	//metodo donde vemos que parte de la cadena nos interesa tener o no
	public static void nosInteresa(String array[], String arrayBueno[], boolean from_to[], boolean tipo_idRef[]){
		int contador=0;
		
		for(int i=0; i<array.length;i++){
			
			
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
			if(array[i].contains("odbcPrecision")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			if(array[i].contains("feature")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			if(array[i].contains("nullable")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
			if (array[i].contains("value.")) {
				arrayBueno[contador] = array[i];
				contador++;
				
			}
			if(array[i].contains("type") && tipo_idRef[0]){
				arrayBueno[contador]=array[i];
				contador++;		
				//tipo_idRef[0]=false;
			}
			
			if(array[i].contains("odbcScale")){
				arrayBueno[contador]=array[i];
				contador++;	
			}
			
			if(array[i].contains("typesystem%2") && !array[i].contains("odbc")){
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
					arrayBueno[i]=arrayBueno[i].replace("odbcScale=", "");
					arrayBueno[i]=arrayBueno[i].replace("nullable=", "");
					arrayBueno[i]=arrayBueno[i].replace("value.=", "");
					arrayBueno[i]=arrayBueno[i].replace("imx:idref=", "");
					arrayBueno[i]=arrayBueno[i].replace("xsi:type=", "");
					
					
					arrayBueno[i]=arrayBueno[i].replaceAll("\"", ""); //para quitar las comillas
					//esto es para los objetos y las transformaciones
					if(arrayBueno[i].contains("typesystem%")){
						arrayBueno[i]=arrayBueno[i].replace("type=smd:com.informatica.adapter.Hive.HiveTypeSystem.typesystem%2F", "");
						//arrayBueno[i]=arrayBueno[i].replace("odbcType=smd:com.informatica.metadata.seed.odbc.ODBC.typesystem%", "");
						//arrayBueno[i]=arrayBueno[i].substring(2, arrayBueno[i].length());						
					}
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

	
	//NO
	 //String body, String id, Instance.Campos campos
	@SuppressWarnings("null")
	private static Instance leerInstancia(BufferedReader br, String cadena) throws Exception{
		boolean parar=false;
		boolean falloBody=false; //para controlar cuando una instancia tiene cuerpo o no
		//booleano que nos sirve para saber si queremos coger el type o no
		//en este caso solo queremos el booleano cuando sea un campo de una transfromacion o IOBJECT
		//luego en las instancias siempre ser� falso.
		Iterator<String> it;
		
		//creacion de una lista de los campos de una instancia
		ArrayList<Instance.Campos> listaCampos = new ArrayList<Instance.Campos>();
		
		//Creamos una nueva instancia
		Instance nuevaInstancia = new Instance(null,null,null,null);
		//Creamos una lista donde meteremos las cosas que nos interesen de la linea
		ArrayList<String> listaClaves = new ArrayList<String>();
		//en Array Bueno estaran solo las palabras que nos interesen de la cadena dividida
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];
		
		
		//Metodo para quitar los caracteres innecesarios
		reemplazo(cadenaDividida,arrayBueno, null,null); 
		
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
				throw new InstanciaSinDescrip(" LA INSTANCIA " + nuevaInstancia.getName() + " NO TIENE DESCRIPCION PUESTA ");

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
			reemplazo(cadenaDividida,arrayBueno, null,null); 
			
			//Metodo para meter en la lista solo lo que nos interese.
			meterEnLista(arrayBueno,listaClaves);
			it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
			it.next(); //para saltarnos el ID del annotation
			nuevaInstancia.setBody(it.next());
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
					reemplazo(cadenaDividida,arrayBueno, from_to,null); 
					
					//Metodo para meter en la lista solo lo que nos interese.
					meterEnLista(arrayBueno,listaClaves);
					
					//Cuando la instancia es un source
					if(nuevaInstancia.getName().contains("Lectura")){
						campo.setTransformationField(null); //Source y Target no tienen este atributo
						it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
						campo.setId(it.next());
						campo.setToPorts(it.next());
						campo.setStructural_feature(it.next());
						listaCampos.add(campo);
						cadena = br.readLine(); //para que avance
					}
					
					//cuando la tabla es un target
					else if(nuevaInstancia.getName().contains("Escritura")){
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
	
	//NO
//	public static AbstractTransformation leerAbstractTransformation(BufferedReader br, String cadena) throws IOException{
//		Iterator<String> it;
//		ArrayList<String> listaTransformaciones = new ArrayList<String>();
//		AbstractTransformation nuevaTransformacion = new AbstractTransformation(null,null,null,null,null);
//		String [] cadenaDividida;
//		cadenaDividida=cadena.split(" ");
//		String[] arrayBueno = new String[cadenaDividida.length];
//		System.out.println("ABSTRACT TRANSFORMATION");
//		
//		cadena = br.readLine(); //para que avance
//		System.out.println(cadena);
//		
//		while(!cadena.contains("</transformations>")) { //mientras no sea el final
//			System.out.println(cadena);
//			if(cadena.contains("<AbstractTransformation")){			
//					cadenaDividida=cadena.split(" ");
//					arrayBueno= new String[cadenaDividida.length];
//					
//					reemplazo(cadenaDividida,arrayBueno,null,false);
//					meterEnLista(arrayBueno,listaTransformaciones);
//					it = listaTransformaciones.iterator(); 
//					nuevaTransformacion.setId(it.next());//ya tenemos el id de las AbstractTransformation(anonymousDso)
//					System.out.println("TENEMOS EL ID PRIMERO: "+ nuevaTransformacion.getId());
//					nuevaTransformacion.setNombre(it.next());//ya tenemos el name de las AbstractTransformation
//					System.out.println("TENEMOS EL NAME PRIMERO: "+ nuevaTransformacion.getNombre());
//					vaciarLista(listaTransformaciones);							
//			}
//			
//			if(cadena.contains("<RelationalField")){			
//				AbstractTransformation.campos campos = nuevaTransformacion.new campos(null, null, null, null, null,null);
//			
//						cadenaDividida=cadena.split(" ");
//						arrayBueno= new String[cadenaDividida.length];
//						
//						reemplazo(cadenaDividida,arrayBueno,null,true);
//						meterEnLista(arrayBueno,listaTransformaciones);
//						it = listaTransformaciones.iterator(); 
//						campos.setID(it.next());
//						System.out.println("TENEMOS EL ID: "+ campos.getId());					
//						campos.setColumna(it.next());
//						System.out.println("TENEMOS LA COLUMNA: "+ campos.getColumna());					
//						campos.setfeature(it.next());
//						System.out.println("TENEMOS EL FEATURE: "+ campos.getFeature());						
//						campos.setName(it.next());
//						System.out.println("TENEMOS EL NOMBRE: "+ campos.getName());						
//						campos.setPrecision(it.next());
//						System.out.println("TENEMOS LA PRECISION: "+ campos.getPrecision());
//						campos.setType(it.next());
//						if(campos.getType().contains("varchar")){campos.setType("string");}
//						System.out.println("TENEMOS EL TIPO: "+ campos.getType());
//						vaciarLista(listaTransformaciones);
//				}		
//			cadena = br.readLine(); //para que avance 
//		
//		}
//		return nuevaTransformacion;
//	}
	
	//OKI
	private static void leerParametros(BufferedReader br, String cadena) throws ParamSinDescripcion, IOException{
		//creamos un parametro que va a ser un hashMap
		Parametro parametro = new Parametro(null,null);
		boolean cuerpo=false;
		boolean parar=false;
		String valor;
		//booleano que nos sirve para saber si queremos coger el type o no
		//en este caso solo queremos el booleano cuando sea un campo de una transfromacion o IOBJECT
		//luego en las instancias siempre ser� falso.
		
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];

						
			
		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		reemplazo(cadenaDividida,arrayBueno, null,null);
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
			reemplazo(cadenaDividida,arrayBueno, null,null);
			valor=arrayBueno[1];
			reiniciarArray(arrayBueno);	
			cadena = br.readLine(); //para que avance
			
			while(!cadena.contains("</UserDefinedParameter"))
				cadena = br.readLine(); //para que avance
	
			tablaParametros.put(parametro, valor);
	}
	
	public static Iobject leerIObject(BufferedReader br, String cadena) throws IOException{

		//booleano para saber si queremos el tipo y para saber si es un id de referencia o el propio
		boolean tipo_idRef[]= new boolean[2];
		tipo_idRef[0]=true;
		tipo_idRef[1]=false;


		Iterator<String> it;
		Iobject nuevoObjeto = new Iobject(null,null,null,tipo_idRef[1],null);

		//creacion de una lista de los campos del objeto
		ArrayList<Iobject.Campo> listaCampos = new ArrayList<Iobject.Campo>();

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
			while(!cadena.contains("</columns")){
				if(cadena.contains("<Column")){
						listaClaves.clear();
		
						//el ultimo es booleano es para indicar si es una particion o no
						Iobject.Campo campo = nuevoObjeto.new Campo(null, null, null, null, null,null);
		
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
						campo.setNullable(it.next());
						campo.setPrecision(it.next());
						
						if(campo.getType().equals("decimal"))
							campo.setEscala(it.next());
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

		return nuevoObjeto;
	}
	
	private static void leerExecutionParameters(BufferedReader br, String cadena) throws IOException {

		Iterator<String> it;
		ExecutionParameters executionParameters = new ExecutionParameters(null, null);
		ArrayList<String> listaParametros = new ArrayList<String>();

		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];


		//Dividimos la cadena para leer las propiedades individualmente
		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		// Metodo para quitar los caracteres innecesarios
		reemplazo(cadenaDividida, arrayBueno, null, null);

		// Metodo para meter los reemplazos en un arratList
		meterEnLista(arrayBueno, listaParametros);

		it = listaParametros.iterator();
		//Metemos los datos en el arraylist principal de executionParameters

		executionParameters.setValue(it.next());
		executionParameters.setName(it.next());
		//Mostramos los datos del arrayList

		vaciarLista(listaParametros);			

		tablaExecutionParameters.put(executionParameters.getValue(), executionParameters.getName());
	}
}
	

