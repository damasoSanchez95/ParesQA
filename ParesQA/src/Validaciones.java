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
	private HashMap<String, String> tablaExecutionParameters = new HashMap<String, String>();
	private boolean descripcionMapping;
	//Constructor Validaciones
	public Validaciones(boolean descripcionMapping, ArrayList<Instance> listaInstancia, ArrayList<AbstractTransformation> listaTransformaciones , HashMap<Parametro, String> tablaParametros, ArrayList<Iobject> listaObjetos, HashMap<String, String> tablaExecutionParameters ){
		this.listaInstancia=listaInstancia;
		this.listaTransformaciones=listaTransformaciones;
		this.tablaParametros=tablaParametros;
		this.listaObjetos=listaObjetos;
		this.tablaExecutionParameters=tablaExecutionParameters;
		this.descripcionMapping = descripcionMapping;
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
	









}
