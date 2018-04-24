import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeerFichero {
	
	private static ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private static ArrayList<AbstractTransformation> listaTransformacion;
	private static HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();

	public static ArrayList<Instance> getListaInstancia(){
		return listaInstancia;
	}
	
	public static ArrayList<AbstractTransformation> getListaTransformaciones(){
		return listaTransformacion;
	}
	
	public static HashMap<Parametro, String> getTablaParametros(){
		return tablaParametros;
	}
	
	@SuppressWarnings("null")
	public static void leerContenido(String archivo) throws Exception{
	    FileReader fr = null;
	    BufferedReader br = null;
		
			fr = new FileReader(archivo); //leemos el archivo que pasemos por parametro
			br = new BufferedReader(fr);
			String cadena;

			while((cadena = br.readLine())!=null) { //mientras no sea el final
				if(cadena.contains("<datarecord:")) {
					leerDataRecord(br,cadena);
				}
				
				else if(cadena.contains("<UserDefinedParameter")){ //PARAMETROS
						//Cada parametro será un hashMap que tiene asociado
						//un id y un nombre.
					
						//En este mismo metodo añadimos el parametro con un valor a tablaParametros.
						leerParametros(br,cadena);	
					}
				
				else if(cadena.contains("<AbstractTransformation")){
					leerAbstractTransformation(br,cadena);	
				}
				
				else if(cadena.contains("<Instance")){
					Instance instancia;
					instancia=leerInstancia(br,cadena);
					instancia = new Instance(instancia.getId(), instancia.getName(), instancia.getBody(), instancia.getCampos());
					//mostrarInstancia(instancia);
					listaInstancia.add(instancia); //Añadimos la instancia a la lista
				}
			}
	}
	
	//metodo para mostrar una instancia (para probar)
	private static void mostrarInstancia(Instance instancia) {
		System.out.println(instancia.getId());
		System.out.println(instancia.getName());
		System.out.println(instancia.getBody());
		Iterator<Instance.Campos> it;
		Instance.Campos campo;

		it = instancia.getCampos().iterator();
		while(it.hasNext()){
			campo = it.next();
			System.out.println("NUEVO CAMPOOOO");
			System.out.println(campo.getId());
			System.out.println(campo.getFromPorts());
			System.out.println(campo.getToPorts());
			System.out.println(campo.getStructural_feature());
			System.out.println(campo.getTransformationField());
		}
	}

	//metodo donde vemos que parte de la cadena nos interesa tener o no
	private static void nosInteresa(String array[], String arrayBueno[], boolean from_to[]){
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

			if(array[i].contains("id=")) {
				arrayBueno[contador]=array[i];
				contador++;
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

			//PARA LOS PARAMETROS
			if(array[i].contains("connectionName")){
				arrayBueno[contador]=array[i];
				contador++;		
			}

			if(array[i].contains("valueLiteral")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
		}
	}
	
	//METODO PARA QUITAR TODOS LOS CARACTERES INNECESARIOS, dentro de la palabra que queremos
	@SuppressWarnings("null")
	//el parametro from_to[] solo lo usamos para los campos de las instancias y de las transformaciones
	private static void reemplazo(String array[], String[] arrayBueno, boolean from_to[], boolean tipo){
		
		nosInteresa(array, arrayBueno,from_to);
		
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
					
					//PARA LOS PARAMETROS
					arrayBueno[i]=arrayBueno[i].replace("connectionName=", "");
					arrayBueno[i]=arrayBueno[i].replace("valueLiteral=", "");
					
					arrayBueno[i]=arrayBueno[i].replaceAll("\"", ""); //para quitar las comillas
				}
		}	
	}
	
	//Lo utilizamos para tener una lista con las partes que nos interesen y asi no tener un array con posiciones nulas
	private static void meterEnLista(String arrayBueno[], ArrayList<String> claves){
		for(int i=0;i<arrayBueno.length;i++)
			if(arrayBueno[i]!=null)
				claves.add(arrayBueno[i]);
	}
	
	//Metodo para reinicar un array
	private static void reiniciarArray(String array[]){
		for(int i=0; i<array.length;i++)
			array[i]=null;
	}
	
	//NO
	 //String body, String id, Instance.Campos campos
	@SuppressWarnings("null")
	private static Instance leerInstancia(BufferedReader br, String cadena) throws Exception{
		boolean parar=false;
		boolean falloBody=false; //para controlar cuando una instancia tiene cuerpo o no
		//booleano que nos sirve para saber si queremos coger el type o no
		//en este caso solo queremos el booleano cuando sea un campo de una transfromacion o IOBJECT
		//luego en las instancias siempre será falso.
		boolean type=false; 
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
		reemplazo(cadenaDividida,arrayBueno, null,type); 
		
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
			
		//HACER EXCEPCION BIEN
		if(falloBody) //es xq no tiene cuerpo asiq malo
			//throw new Exception("la instancia " + nuevaInstancia.getName() + " no tiene descripcion puesta");
			System.out.println("la instancia" + nuevaInstancia.getName() + "no tiene descripcion puesta");
		else{ //Aqui entrara cuando si hay body
			//leerBody
			cadena = br.readLine(); //para que avance ya que aqui llega con <annotations>

			cadenaDividida=cadena.split(" ");
			arrayBueno= new String[cadenaDividida.length];
			
			//Metodo para quitar los caracteres innecesarios
			reemplazo(cadenaDividida,arrayBueno, null,type); 
			
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
				if(cadena.contains("<NestedPort") || (cadena.contains("<TransformationFieldPort") && (!nuevaInstancia.getName().contains("Lectura") && !nuevaInstancia.getName().contains("Escritura")))){
					boolean from_to[] = new boolean[2]; //Array para saber si tiene from y to el campo.
					listaClaves.clear(); //limpiamos la lista de elemenos 
					Instance.Campos campo = nuevaInstancia.new Campos(null, null, null, null, null,null);
					
					cadenaDividida=cadena.split(" ");
					arrayBueno= new String[cadenaDividida.length];
					
					//Metodo para quitar los caracteres innecesarios
					reemplazo(cadenaDividida,arrayBueno, from_to,type); 
					
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
							
							//añadimos este campo a la lista de campos
							listaCampos.add(campo);
							cadena = br.readLine(); //para que avance							
						}
					}
				}
				else
					cadena = br.readLine(); //para que avance

			}
			//añadimos la lista de campos a la instancia
			nuevaInstancia.setCampos(listaCampos);
			
			return nuevaInstancia;
		}
	
	//OKI	
	private static void leerDataRecord(BufferedReader br, String cadena) throws IOException{
		System.out.println("DATA RECORDS");
		cadena = br.readLine(); //para que avance
		if(cadena.contains("<columns"))
			while(!cadena.contains("</columns>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
	}
	
	//NO
	private static void leerAbstractTransformation(BufferedReader br, String cadena) throws IOException{
		System.out.println("ABSTRACT TRANSFORMATION");
		
		cadena = br.readLine(); //para que avance
		if(cadena.contains("<relationalFields"))
			while(!cadena.contains("</relationalFields>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
	}
	
	@SuppressWarnings("unchecked")
	private static void leerParametros(BufferedReader br, String cadena) throws IOException{
		//creamos un parametro que va a ser un hashMap
		Parametro parametro = new Parametro(null,null);
		boolean cuerpo=false;
		boolean parar=false;
		String valor;
		//booleano que nos sirve para saber si queremos coger el type o no
		//en este caso solo queremos el booleano cuando sea un campo de una transfromacion o IOBJECT
		//luego en las instancias siempre será falso.
		boolean type=false; 
		
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String clave="";
		String[] arrayBueno = new String[cadenaDividida.length];

						
			
		cadenaDividida=cadena.split(" ");
		arrayBueno= new String[cadenaDividida.length];

		reemplazo(cadenaDividida,arrayBueno, null,type);
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
				System.out.println("el parametro" + clave + "no tiene cuerpo rision");

			//luego lo que nos interesa es el valor del parametro
			while(!cadena.contains("<defaultValue")) 
				cadena = br.readLine(); //para que avance
				
			cadenaDividida=cadena.split(" ");
			arrayBueno= new String[cadenaDividida.length];
			reemplazo(cadenaDividida,arrayBueno, null,type);
			valor=arrayBueno[1];
			reiniciarArray(arrayBueno);	
			cadena = br.readLine(); //para que avance
			
			while(!cadena.contains("</UserDefinedParameter"))
				cadena = br.readLine(); //para que avance
	
			tablaParametros.put(parametro, valor);
	}
}


