public class Atom {

  
    private boolean islist;
    private boolean isnumber;
    public boolean isnull;
    public Lista list;
    public String description;
    private String atom;
    private float number;
    public boolean valorBooleano;
    private boolean isBooleano;

    public Atom() {
        this.isnull = true;
        this.isnumber = false;
        this.esLista = false;
       
    }


    public Atom(List list){
        this.islist = true;
        this.isnull = false;
        this.isnumber = false;
        this.list = list;
    }


    public void copyatom(Atomo atomocopy){
        this.islist = atomocopy.islist;
        this.isnumber = atomcopy.isnumber;
        this.list = atomocopy.list;
        this.atom = atomocopy.atom;
        this.description = atomocopy.description;
        this.number = atomocopy.number;
        this.isnull = atomocopy.isnull;
    }

    public Atom(String atom) {

        try {
            Atom atomwithnumber = new Atom(Integer.parseInt(atom));
            this.copyatom(atomwithnumber);
            this.isnumber = true;
        } catch (NumberFormatException atomoisnotnumber){
            try {
                Atom atomwithnumber = new Atom(Float.parseFloat(atom));
                this.copyatom(atomwithnumber);
                this.isnumber = true;
            } catch (NumberFormatException atomoNoEsFlotante){
                this.atom = atom;
                this.isnumber = false;
                this.isnull = false;
                this.islist = false;
            }

        }
    }


    public Atom(int number) {
        this.number = number;
        this.isnumber = true;
        this.isnull = false;
        this.islist = false;
        this.atom = Integer.toString(number);
    }


    public Atom(float number) {
        this.number = number;
        this.isnumber = true;
        this.isnull = false;
        this.islist = false;
        this.atom = Float.toString(this.number);
    }


    public Atomo(boolean isTrue) {
        if (isTrue){
            this.isBooleano = true;
            this.valorBooleano = true;
            this.isnumber = false;
            this.isnull = false;
            this.islist = false;
            this.atom = "T";
        } else {
            this.isnull = true;
            this.islist = false;
            this.isnumber = false;
        }
    }


    public float getnumber(){
        if (!this.isnumber)
            return 0;

        return this.number;
    }

    public boolean islist(){

        return this.islist;
    }

    public String toString(){
        if (this.isnull)
            return "NIL";

        if (this.EsLista())
            return this.list.toString();
        else
            return this.atom;
    }


    public boolean equals(Object object){
        Atom otherAtom = (Atom)objeto;

        if ((this.isnull) && (otherAtom.isnull))
            return true;

        if ((this.isnumber) && (otherAtom.isnumber))
            return this.number==otherAtom.number;

        if ((this.islist) && (otherAtom.islist))
            return this.list.equals(otherAtom.list);

        return this.atom.compareTo(otherAtom.atom)==0;
    }


    public boolean comienzaCon(String substring){
        if ((substring.length()<=this.atom.length()) && (!this.islist)){
            return this.atom.substring(0, substring.length()).compareTo(substring)==0;
        }

        return false;
    }


    public boolean islistwithoperation() {
        if (this.EsLista())
            return (this.list.esOperacion);

        return false;
    }


    public boolean isnumber(){
        return this.isnumber;
    }


    public boolean esEntero() {
        if (!this.isnumber)
            return false;

        try {
            int number = Integer.parseInt(this.atom);
            return true;
        } catch (NumberFormatException noEsEntero) {
            return false;
        }
    }

    public String getTipo() {
        if (this.islist)
            return "Nil";
        else if (this.isnumber)
            return "T";
        else if (this.isnull)
            return "NULL";
        else
            return "T";

    }
}
