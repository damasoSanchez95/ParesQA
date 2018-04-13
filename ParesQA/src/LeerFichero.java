import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LeerFichero {

	public static void leerContenido(String archivo) throws Exception {
	    FileReader fr = null;
	    BufferedReader br = null;
		
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
					leerInstancia(br,cadena);
				}
			}
		}
		catch(Exception e) {
			throw new Exception("No se ha podido abrir ese archivo. No existe");
		}
		finally {
			br.close();
		}
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
	public static void leerInstancia(BufferedReader br, String cadena) throws IOException{
		System.out.println("INSTANCIA");
		
		//Instance nuevaInstancia;
		//Instance.Campos campos; //clase interna de la clase Instance para los campos
		
		//if(!cadena.contains("annotations")) //es xq no tiene cuerpo asiq malo
			//lanzar una excepcion que te diga que no tiene cuerpo
		
		//else{
		//nuevaInstancia.setBody(body); //body será lo que hemos leido en annotation
		
		while(!cadena.contains("<ports"))
			cadena = br.readLine(); //para que avance
		
			while(!cadena.contains("</ports>")) { //mientras no sea el final
				System.out.println(cadena);
				cadena = br.readLine(); //para que avance
				}
		}
	//}
	
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

