import java.io.BufferedReader;
import java.io.InputStreamReader;

import Excepciones.FechaEjecucionFallo;

public class Main {

	public static void main(String[] args) throws Exception {
		
		System.out.println("Pon nombre del archivo");
			InputStreamReader isr = new InputStreamReader(System.in); //Leemos el archivo
			BufferedReader br = new BufferedReader (isr);
			Validaciones validaciones;
			String archivo = br.readLine();
			LeerFichero.leerContenido(archivo);
			LeerFichero.ordenarObjetos(); //se hace para que el de escritura nunca este el primero
			LeerFichero.pasarNombreInstanciaAMinusc();
			LeerFichero.pasarNombreTransformacionesAMinusc();
			
			validaciones = new Validaciones(LeerFichero.getDescripcionMapping(), LeerFichero.getListaInstancia(), LeerFichero.getListaTransformaciones(), LeerFichero.getTablaParametros(), LeerFichero.getListaObjetos(), LeerFichero.getTablaExecutionParameters(), LeerFichero.getListaDataRecords());
			System.out.println("*******COMPROBACION PARAMETROS*********");
			validaciones.parametros();

			
			System.out.println("*******COMPROBACION PARAMETROS DE EJECUCION*********");
			validaciones.executionParameters();
			
			
			
			System.out.println("*******COMPROBACION DESCRIPCION MAPPING*********");
			validaciones.descripcionMapping();
			
			System.out.println("*******COMPROBACION PUERTOS*********");
			validaciones.ValidacionesPuertos();
			System.out.println("HEMOS ACABADO DE VALIDAR EL MAPPING.");
	}
}
