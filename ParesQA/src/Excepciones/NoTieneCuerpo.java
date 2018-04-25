package Excepciones;

public class NoTieneCuerpo extends Exception{
    

    public String getMessage(){
         
        String mensaje="Error: El archivo no tiene cuerpo";
        return mensaje;       
    }
     
}
