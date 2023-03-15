import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Interprete {

    public static void main(String[] args) {
        
        int opcion = 0;
        
        try{
            while(true){

                System.out.println("Menú");
                System.out.println("1 Ejecutar comando de LISP");
                System.out.println("2 Salir");

                System.out.print("Ingrese la opción a elegir: ");
          
                opcion = Key.readInt();

                switch(opcion){
                    case 1:
                
                        String path = "";
                        System.out.print("Ingrese el Path del archivo: ");
                        
                      
                        path = Key.readString();
                    
                        DataManager file= new DataManager();
                        
                        file.setPathFile(path);
                 
                        if(file.getExists()){
                           
                            System.out.println(String.format( "Expresión a Evaluar: ",archivo.getDataFile()));

                            runLisp(archivo.getListInstruccion());
                        }
                        else{
                            System.out.println(String.format("El archivo de la ruta no fue encontrado", path));
                        }
                        break;

                    case 2:

                        System.exit(0);
                }
            
            }
        }
        catch(Exception e){
            System.out.println(String.format("Ha ocurrido un problema: ",e.toString()));
        }
        
    }
