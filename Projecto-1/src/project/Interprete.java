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

    public static void runLisp(Object value) throws Exception{
        try{
   
            List instruccions = (List)value;
                        
            List tempIns = new ArrayList();
            

            DataManager archivo = new DataManager();
            
            for(int control=0;control<instruccions.size();control++){
                tempIns.add(archivo.getInstruccion(archivo.getTokens(Delimit, instruccions.get(control).toString() )));
             
                List<Defun> deFun = new ArrayList<>();

                for (Object i: tempIns) {
    
                    List instruccion = null;
            
                    if(i instanceof ArrayList){
                        instruccion = (List) i;
                    }
                    else if(i instanceof String){
                        instruccion = Arrays.asList(tempIns.toString().split(" "));
                       
                    }
                    else{
                         instruccion = tempIns;
                    }
    
    
                    if(instruccion.contains("atom")){
    
                      
                        if(instruccion.size() == 2 ){
                       
                            if( (new functionEvaluation()).isAtom(instruccion.get(1))){
                                System.out.print("Resultado: True");
                            }
                            else{
                                System.out.print("Resultado: invalid");
                            }
                        }
               
                        else{
                            System.out.println("La función de atom posee errores en la sintaxis");
                        }
                    }
                    else if(instruccion.contains("defun")){
                        List subList = (List) instruccion.get(2);
                        Defun newFunc = new Defun(instruccion.get(1).toString(), subList.get(0), subList.get(1));
                        deFun.add(newFunc);
                    } else if (instruccion.contains("list")){
                        List<Object> list = new functionEvaluation().toList(instruccion.subList(1, instruccion.size()));
                        System.out.println(list);
                    } else if (instruccion.contains("equal")){
                        if( (new functionEvaluation()).isEqual(instruccion.get(1), instruccion.get(2))){
                            System.out.print("Resultado: True");
                        }
                        else{
                            System.out.print("Resultado: invalid");
                        }
                    } else if (instruccion.contains(">")){
                        if( (new functionEvaluation()).isGreaterThan(instruccion.get(1), instruccion.get(2))){
                            System.out.print("Resultado: True");
                        }
                        else{
                            System.out.print("Resultado: invalid");
                        }
                    } else if (instruccion.contains("<")){
                        if( (new functionEvaluation()).isLessThan(instruccion.get(1), instruccion.get(2))){
                            System.out.print("Resultado: True");
                        }
                        else{
                            System.out.print("Resultado: invalid");
                        }
                    }
                    else if (instruccion.contains("+") || instruccion.contains("-") || instruccion.contains("*") || instruccion.contains("/")){
                        Aritmetic calculator = new Aritmetic();
                        System.out.println("Resultado: " + calculator.calculate(instruccion));
                        break;
                     
                    } else if (instruccion.contains("cond")){
    
                        runLisp((new functionEvaluation()).cond(instruccion));
    
                    } else {
                        for (Defun fun: deFun) {
                            if (instruccion.contains(fun.getFunName())){
                                List<Object> tempSubIns = fun.executeInstructions(instruccion.subList(1, instruccion.size()));
                                
                                String sub_instruccion = String.format("(%s)", listToString(tempSubIns));
                                
                             
                                runLisp( archivo.getInstruccion(archivo.getTokens( DELIMITADOR , sub_instruccion )) );
                                
                            }
                        }
                    }
    
                }
    
            }
            catch(Exception e){
                System.out.println("Ha ocurrido un problema al evaluar la expreción, el error es el siguiente: " + e.toString());
            }
        }
        public static String listToString(List value){
            String temps ="";
            
            List templist = (List)value;
            for(int control = 0;control<templist.size(); control++){
                
                if(templist.get(control) instanceof List){
                    temps += "(";
                    temps += listToString((List)templist.get(control)) + "\t";
                    temps += ")";
                }
                else{
                    temps += templist.get(control) + "\t";
                }
                
            }
            
            return temps;
        }
    }
    
            }
