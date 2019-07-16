
import java.util.Vector;

/**
 *
 * @author Aviya
 */
public class Var {

    char name;
    boolean Avidence;
    boolean Hidden;
    Vector<String> values;
    Vector<Var> parents;
    Vector<Var> children;
    String[][] CPT;

    public Var(char name) {
        this.name = name;
        values = new Vector<>();
        parents = new Vector<>();
          children= new Vector<Var>();
    }

    public char getName() {
        return name;
    }

    public Vector<Var> getChild() {
        return children;
    }

    public Vector<Var> getParents() {
        return parents;

    }

    public void addValue(String val) {
        values.add(val);
    }

    public void addParent(Var parent) {
        parents.add(parent);
    }

    public void addChild(Var child) {
        children.add(child);
    }

    public Vector<String> getValues() {
        return values;
    }

    public boolean getColored() {
        return Hidden;
    }

    public String[][] getCPT() {
        return CPT;
    }

    public void setCPT(String[][] newCPT) {
        CPT = new String[newCPT.length][newCPT[0].length];
        for (int i = 0; i < newCPT.length; i++) {
            for (int j = 0; j < newCPT[0].length; j++) {
                CPT[i][j] = newCPT[i][j];
            }
        }

    }
    public void PrintChild(){
        for (int i=0;i<children.size();i++){
            System.out.println(children.get(i).getName());
        }
    }
     public void PrintPar(){
         System.out.println("Print Par");
        for (int i=0;i<parents.size();i++){
            System.out.println(parents.get(i).getName());
        }
    }

    public void printCPT() {
        for (int i = 0; i < CPT.length; i++) {
            for (int j = 0; j < CPT[0].length; j++) {
                System.out.print(CPT[i][j] + ",");
            }
            System.out.println();
        }
    }

}
