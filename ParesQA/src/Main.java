import java.io.BufferedReader;
import java.io.InputStreamReader;

import Excepciones.FechaEjecucionFallo;

public class Main {

	public static void main(String[] args) throws Exception {
	
			while (true) {
				System.out.println("");
				System.out.println("Pon el nombre del archivo a comprobar");
				InputStreamReader isr = new InputStreamReader(System.in); //Leemos el archivo
				BufferedReader br = new BufferedReader (isr);
				Validaciones validaciones;
				String archivo = br.readLine();
				LeerFichero.leerContenido(archivo);
				LeerFichero.ordenarObjetos(); //se hace para que el de escritura nunca este el primero
				LeerFichero.pasarNombreInstanciaAMinusc();
				LeerFichero.pasarNombreTransformacionesAMinusc();
				
				validaciones = new Validaciones(LeerFichero.getDescripcionMapping(), LeerFichero.getListaInstancia(), LeerFichero.getListaTransformaciones(), LeerFichero.getTablaParametros(), LeerFichero.getListaObjetos(), LeerFichero.getTablaExecutionParameters(), LeerFichero.getListaDataRecords());
				System.out.println("");			
	
				System.out.println("*******COMPROBACION NOMENGLATURA*********");
				System.out.println("");
				
				validaciones.comprobarNomenglatura();
				
				System.out.println("");
				
				
				System.out.println("*******COMPROBACION PARAMETROS*********");
				System.out.println("");
				
				validaciones.parametros();
				
				System.out.println("");
	
				System.out.println("*******COMPROBACION ESQUEMAS DE LAS TABLAS*********");
				System.out.println("");
	
				validaciones.conexionOwnerTablas();
				
				System.out.println("");
	
	
				
				System.out.println("*******COMPROBACION PARAMETROS DE EJECUCION*********");
				System.out.println("");
	
				validaciones.executionParameters();
				
				System.out.println("");			
				
				System.out.println("*******COMPROBACION DESCRIPCION MAPPING*********");
				System.out.println("");
	
				validaciones.descripcionMapping();
				
				System.out.println("");			
	
				
				System.out.println("*******COMPROBACION PUERTOS*********");
				System.out.println("");
	
				validaciones.ValidacionesPuertos();
				
				System.out.println("");
				System.out.println("");
				System.out.println("");
	
				System.out.println("************************************");
				System.out.println("HEMOS ACABADO DE VALIDAR EL MAPPING.");
				System.out.println("************************************");
			}
	}
}
