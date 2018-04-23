import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws Exception {
		
		System.out.println("Pon nombre del archivo");
		InputStreamReader isr = new InputStreamReader(System.in); //Leemos el archivo
		BufferedReader br = new BufferedReader (isr);
		Validaciad
		String archivo = br.readLine();
		LeerFichero.leerContenido(archivo);
		Validaciones = Validaciones(LeerFichero.getListaInstancia(), LeerFichero.getListaTransformaciones(), LeerFichero.getTablaParametros());
		System.out.println("ACABÓ");
	}
}
