import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Validaciones {
	
	
	private ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private ArrayList<AbstractTransformation> listaTransformaciones= new ArrayList<AbstractTransformation>();
	private HashMap<Parametro, String> tablaParametros = new HashMap<Parametro, String>();
	
	//Constructor Validaciones
	public Validaciones(ArrayList<Instance> listaInstancia, ArrayList<AbstractTransformation> listaTransformaciones , HashMap<Parametro, String> tablaParametros){
		this.listaInstancia=listaInstancia;
		this.listaTransformaciones=listaTransformaciones;
		this.tablaParametros=tablaParametros;
	}
	
	public void parametros(){

	
		for (Entry<Parametro, String> entry : tablaParametros.entrySet()) {
		    Parametro clave = entry.getKey();
		    String valor = entry.getValue();
		    
		    if(clave.getName().equals("P_s_FechaEjecucion") && !valor.equals("9999-12-31"))
		    	System.out.println("caca");
		    	//LANZAR EXCEPCION DE QUE EL PARAMETRO P_s_FechaEjecucion NO TIENE LA FECHA POR DEFECTO 9999-12-31
		}
	}
}
