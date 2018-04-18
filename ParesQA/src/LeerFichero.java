import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LeerFichero {
	
	private static ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private AbstractTransformation nuevaTransformacion;
	private dataRecord nuevaTabla;
	private Parameters nuevoParametro;
	
	@SuppressWarnings("null")
	public static void leerContenido(String archivo) throws Exception {
	    FileReader fr = null;
	    BufferedReader br = null;
	    // String name="", id ="",body = ""; //atributos de una instancia
	   // Instance.Campos campos = null; //Campos de una instancia
		
		try {
			fr = new FileReader(archivo); //leemos el archivo que pasemos por parametro
			br = new BufferedReader(fr);
			String cadena;

			while((cadena = br.readLine())!=null) { //mientras no sea el final
				if(cadena.contains("<datarecord:"))
					//CREAR UN OBJETO DE LA CLASE DATARECORD
					leerDataRecord(br,cadena);
				else if(cadena.contains("<parameters")){
					//CREAR UN OBJETO DE LA CLASE PARAMETRO
					leerParametros(br,cadena);
				}
				else if(cadena.contains("<AbstractTransformation")){
					//CREAR UN OBJETO DE LA CLASE ABSTRACTTRANSFORMATION
					leerAbstractTransformation(br,cadena);	
				}
				else if(cadena.contains("<Instance")){
					//CREAR UN OBJETO DE LA CLASE INSTANCIA
					//id, name, body, campos
					Instance instancia;
					instancia=leerInstancia(br,cadena);
					instancia = new Instance(instancia.getId(), instancia.getName(), instancia.getBody(), instancia.getCampos());
					listaInstancia.add(instancia);
				}
			}
		}
		catch(Exception e) {
			throw new Exception("No se ha podido abrir ese archivo. No existe");
		}
		finally {
			if(br!=null){
				br.close();
			}
		}
    }	
	
	public static void nosInteresa(String array[], String arrayBueno[]){
		int contador=0;
		
		for(int i=0; i<array.length;i++){
			
			if(array[i].contains("fromPort")) {
				arrayBueno[contador]=array[i];
				contador++;
			}
			
			if(array[i].contains("id")) {
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
			}
			
			if(array[i].contains("structuralFeature")){
				arrayBueno[contador]=array[i];
				contador++;		
			}
		}
	}
	
	
	//METODO PARA QUITAR TODOS LOS CARACTERES INNECESARIOS
	@SuppressWarnings("null")
	public static void reemplazo(String array[], String[] arrayBueno){
		nosInteresa(array, arrayBueno);
		
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
					
				}
		}	
	}
	
	public static void meterEnLista(String arrayBueno[], ArrayList<String> claves){
		for(int i=0;i<arrayBueno.length;i++)
			if(arrayBueno[i]!=null)
				claves.add(arrayBueno[i]);
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
	public static Instance leerInstancia(BufferedReader br, String cadena) throws Exception{
		boolean parar=false;
		boolean falloBody=false;
		boolean dentro=false;
		Iterator<String> it;
		ArrayList<Instance.Campos> listaCampos = new ArrayList<Instance.Campos>();
		
		Instance nuevaInstancia = new Instance(null,null,null,null);
		ArrayList<String> listaClaves = new ArrayList<String>();
		String [] cadenaDividida;
		
		cadenaDividida=cadena.split(" ");
		
		String[] arrayBueno = new String[cadenaDividida.length];
		
		//Metodo para quitar los caracteres innecesarios
		reemplazo(cadenaDividida,arrayBueno); 
		
		//Metodo para meter en la lista solo lo que nos interese.
		meterEnLista(arrayBueno,listaClaves);
		
		it = listaClaves.iterator();
		
		nuevaInstancia.setId(it.next()); //Tenemos id de la instancia
		nuevaInstancia.setName(it.next()); //tenemos nombre de la instancia
		
		vaciarLista(listaClaves);
		
		while(!cadena.contains("annotations") && !parar)
			if((cadena.contains("<fromOutlineLinks")) || cadena.contains("ports")) {
				parar=true; //para si no encontramos annotations y si otra etiqueta, esto ocurrira en caso de fallo.
				falloBody=true;
			}
			else if(cadena.contains("annotations")) //para si encontramos annotations
				parar=true;
			else 
				cadena = br.readLine(); //para que avance si no ha encontrado annotations
			
		//HACER EXCEPCION BIEN
		if(falloBody) //es xq no tiene cuerpo asiq malo
			//throw new Exception("la instancia " + nuevaInstancia.getName() + " no tiene descripcion puesta");
			System.out.println("la instancia" + nuevaInstancia.getName() + "no tiene descripcion puesta");
		else{
			//leerBody
			cadena = br.readLine(); //para que avance ya que aqui llega con <annotations>
			//cadena = <Annotation imx:id="ID_21" xsi:type="description:Description" body="Tabla+actual+temporal+con+la+informaci%C3%B3n+de+las+comisiones."/>

			cadenaDividida=cadena.split(" ");
			arrayBueno= new String[cadenaDividida.length];
			
			//Metodo para quitar los caracteres innecesarios
			reemplazo(cadenaDividida,arrayBueno); 
			
			//Metodo para meter en la lista solo lo que nos interese.
			meterEnLista(arrayBueno,listaClaves);
			
			it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
			it.next(); //para saltarnos el ID del annotation
			nuevaInstancia.setBody(it.next());
			
			//HASTA AQUI BIEN
			while(!cadena.contains("<ports"))
				cadena = br.readLine(); //para que avance

			while(!cadena.contains("</ports>")) { //mientras no sea el final
				if(cadena.contains("<NestedPort")){
					vaciarLista(listaClaves);
					Instance.Campos campos = nuevaInstancia.new Campos(null, null, null, null);
					
					cadenaDividida=cadena.split(" ");
					arrayBueno= new String[cadenaDividida.length];
					
					//Metodo para quitar los caracteres innecesarios
					reemplazo(cadenaDividida,arrayBueno); 
					
					//Metodo para meter en la lista solo lo que nos interese.
					meterEnLista(arrayBueno,listaClaves);
					
					if(nuevaInstancia.getName().contains("Lectura")){
						it = listaClaves.iterator(); //llevamos al iterador al principio de la lista
						campos.setId(it.next());
						campos.setToPorts(it.next());
						campos.setStructural_feature(it.next());
						listaCampos.add(campos);
						cadena = br.readLine(); //para que avance
					}
					else if(nuevaInstancia.getName().contains("Escritura")){
						it=listaClaves.iterator();
						campos.setId(it.next());
						campos.setFromPort(it.next());
						campos.setStructural_feature(it.next());
						campos.setToPorts(null); //Como es escritura no va a ningun lado
						listaCampos.add(campos);
						cadena = br.readLine(); //para que avance
					}
					else{
						
						
					}
				}
				else
					cadena = br.readLine(); //para que avance

			}
			nuevaInstancia.setCampos(listaCampos);

		}
		return nuevaInstancia;
	}
	
	private static void vaciarListaCampos(ArrayList<Instance.Campos> listaCampos) {
			listaCampos.clear();
		
	}

	//OKI	
	public static void leerDataRecord(BufferedReader br, String cadena) throws IOException{
		System.out.println("DATA RECORDS");
		cadena = br.readLine(); //para que avance
		if(cadena.contains("<columns"))
			while(!cadena.contains("</columns>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
	}
	
	//NO
	public static void leerAbstractTransformation(BufferedReader br, String cadena) throws IOException{
		System.out.println("ABSTRACT TRANSFORMATION");
		
		cadena = br.readLine(); //para que avance
		if(cadena.contains("<relationalFields"))
			while(!cadena.contains("</relationalFields>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
	}
	
	//OKI
	public static void leerParametros(BufferedReader br, String cadena) throws IOException{
		System.out.println("PARAMETROSSS");
		cadena = br.readLine(); //para que avance
		
			while(!cadena.contains("</parameters>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
		}



}	

