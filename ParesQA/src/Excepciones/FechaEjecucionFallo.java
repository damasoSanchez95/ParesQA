package Excepciones;

public class FechaEjecucionFallo extends Exception{

	public String getMessage(){
        
        String mensaje="Error: EL PARAMETRO P_s_FechaEjecucion NO TIENE LA FECHA POR DEFECTO 9999-12-31";
        return mensaje;       
    }
}
