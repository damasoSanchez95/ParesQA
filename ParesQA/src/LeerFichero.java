import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LeerFichero {
	
	private static Instance nuevaInstancia;
	private Instance.Campos camposInstancia;
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
					Instance instancia=null;
					instancia=leerInstancia(br,cadena);
					nuevaInstancia = new Instance(instancia.getId(), instancia.getName(), instancia.getBody());
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
			
			if(array[i].contains("id")) {
				arrayBueno[contador]=array[i];
				contador++;
			}
			
			if(array[i].contains("name")){
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
				}
		}	
	}
	
	public static void meterEnLista(String arrayBueno[], ArrayList<String> claves){
		for(int i=0;i<arrayBueno.length;i++)
			if(arrayBueno[i]!=null)
				claves.add(arrayBueno[i]);
	}
	
	//NO
	 //String body, String id, Instance.Campos campos
	public static Instance leerInstancia(BufferedReader br, String cadena) throws Exception{
		boolean parar=false;
		boolean falloBody=false;
		Instance nuevaInstancia = new Instance(null,null,null);
		ArrayList<String> listaClaves = new ArrayList<String>();
		String [] cadenaDividida;
		cadenaDividida=cadena.split(" ");
		String[] arrayBueno = new String[cadenaDividida.length];
		reemplazo(cadenaDividida,arrayBueno); //Aqui estamos dividiendo la cadena
		meterEnLista(arrayBueno,listaClaves);
		
		Iterator<String> it = listaClaves.iterator();
		nuevaInstancia.setId(it.next());
		nuevaInstancia.setName(it.next());
		
		//HASTA AQUI BIEN
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

			String body ="caca";
			nuevaInstancia.setBody(body); //body será lo que hemos leido en annotation
		
		while(!cadena.contains("<ports"))
			cadena = br.readLine(); //para que avance
		
			while(!cadena.contains("</ports>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
		}
		return nuevaInstancia;
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

