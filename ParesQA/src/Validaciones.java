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
	private static ArrayList<Iobject> listaObjetos = new ArrayList<Iobject>();
	private HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();
	private final static Logger LOGGER = Logger.getLogger("default.LeerFichero");
	
	//Constructor Validaciones
	public Validaciones(ArrayList<Instance> listaInstancia, ArrayList<AbstractTransformation> listaTransformaciones , HashMap<Parametro, String> tablaParametros, ArrayList<Iobject> listaObjetos){
		this.listaInstancia=listaInstancia;
		this.listaTransformaciones=listaTransformaciones;
		this.tablaParametros=tablaParametros;
		this.listaObjetos=listaObjetos;
	}
	
	public void parametros(){

	
		for (Entry<Parametro, String> entry : tablaParametros.entrySet()) {
		    Parametro clave = entry.getKey();
		    String valor = entry.getValue();
		    
		    //try{	    
		    if(clave.getName().equals("P_s_FechaEjecucion") && !valor.equals("9999-12-31"))
		    	System.out.println("caca");
		    	//LANZAR EXCEPCION DE QUE EL PARAMETRO P_s_FechaEjecucion NO TIENE LA FECHA POR DEFECTO 9999-12-31 
		    //***Esta hecha solo falta descomentarla***
		    //throw new FechaEjecucionFallo();	
		    //}
		    /*catch(FechaEjecucionFallo ex){
		    	//saco por el log un fallo con el mensaje de la clase
				 LOGGER.log(Level.SEVERE, ex.getMessage());
				
		    }*/
		}
	}
}
