import java.util.ArrayList;
import java.util.HashMap;

public class Validaciones {
	
	
	private ArrayList<Instance> listaInstancia = new ArrayList<Instance>();
	private ArrayList<AbstractTransformation> listaTransformaciones= new ArrayList<AbstractTransformation>();
	private static HashMap<String, String> tablaParametros = new HashMap<String, String>();
	
	//Constructor Validaciones
	public Validaciones(ArrayList<Instance> listaInstancia, ArrayList<AbstractTransformation> listaTransformaciones , HashMap<String, String> tablaParametros){
		this.listaInstancia=listaInstancia;
		this.listaTransformaciones=listaTransformaciones;
		this.tablaParametros=tablaParametros;
	}
}
