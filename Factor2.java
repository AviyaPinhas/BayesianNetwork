
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Arrays;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Aviya
 */
public class Factor2 {

    Vector<String> VarCPT;// the Vertex at the Factor
    String[][] NewCPT;
    HashMap<String, Var> variables;
    int FactorNum;
    Vector<Integer> rows;
    Vector<Vector<String>> FactorMat;//the final factor
    Vector <String> VarFactor;// the final Element at the Factor
    int SizeFactor;
    public Factor2(Vector<String> VarCPT, HashMap variables, String[][] cpt) {
        VarCPT = VarCPT;
        NewCPT = new String[cpt.length][cpt[0].length];
        variables = variables;
        VarFactor= new Vector<String>();
        rows = new Vector<Integer>();
        FactorMat = new Vector<Vector<String>>();
        BildFac(cpt);
        FillVar();
        SizeFactor=FactorMat.size();
    }
    
    public Factor2(Vector <String >VarFactor, Vector<Vector<String>> FactorMat){
        this.VarFactor= new Vector<String>();
        int count=0;
        SizeFactor=0;
        this.FactorMat = new Vector<Vector<String>>();
        Vector<String> vecclone= new Vector<String>();
         Vector<Vector<String>> vecclone2= new Vector<Vector<String>>();
         vecclone = (Vector) VarFactor.clone();
         vecclone2= (Vector)FactorMat.clone();
           Vector<String> input= new Vector<String>();
         
         for (int i=0;i<FactorMat.size();i++){
             for (int j=0;j<FactorMat.get(0).size();j++){
                 input.add(FactorMat.get(i).get(j));
             }
             vecclone = (Vector) input.clone();
             input.removeAllElements();
             this.FactorMat.add(vecclone);
             count++;
            // System.out.println("Factor2.<init>()"+ this.FactorMat);
         }
        
       for (int k=0;k<this.FactorMat.get(0).size();k++){
      //       System.out.println("Var we want"+this.FactorMat.get(0).get(k));
      this.VarFactor.add(this.FactorMat.get(0).get(k));
     //    System.out.println("Factor2.<init>()"+ this.VarFactor);
       }
      //  System.out.println("end");
        //this.VarFactor= vecclone;
        SizeFactor=this.VarFactor.size();
        // System.out.println("print bild factor"+ count);
         
    }
    

    public int getFactorSize() {
        return SizeFactor;
    }
     public int GetFactorNum() {
        return SizeFactor;
    }
     public boolean isin(String var){
        for (int i = 0; i < VarFactor.size(); i++) {
            if(var.equals(VarFactor.get(i))){
                return true;
            }
        }
        return false;
    }
    public Vector<Vector<String>> getFactorMat() {
        return FactorMat;
    }
     public Vector<String> getVarFVector() {
        return VarFactor;
    }

    public void BildFac(String[][] cpt) {
        int numOfRows = 1;
        Vector<String> input = new Vector();
        Vector<String> vecclone = new Vector();
        int indexFill = 0;
        input.removeAllElements();
        for (int k = 0; k < cpt[0].length; k++) {
            NewCPT[0][k] = cpt[0][k];
            input.add(NewCPT[0][k]);
        }
        vecclone = (Vector) input.clone();
        FactorMat.add(vecclone);
        input.removeAllElements();
        numOfRows = 0;
        indexFill++;
        boolean AllTrue = true;
        for (int i = 1; i < cpt.length; i++) {
            for (int j = 0; j < cpt[0].length; j++) {
                if (cpt[i][j].equals("null")) {
                    AllTrue = false;
                }
            }
            if (AllTrue == true) {
                for (int k = 0; k < cpt[0].length; k++) {
                    NewCPT[indexFill][k] = cpt[i][k];
                    input.add(cpt[i][k]);
                }
                numOfRows++;
                vecclone = (Vector) input.clone();
                FactorMat.add(vecclone);
            }

            input.removeAllElements();
            AllTrue = true;
            indexFill++;
            
        }
      //  System.out.println(FactorMat.toString());
    }
    public  void PrintFactor(){
        
        System.out.println(FactorMat.toString());
    }
        public  void PrintNum(){
        System.out.println(FactorNum);
    }
    public void FillVar(){
       Vector <String>VarF= FactorMat.get(0);
       Object [] temp= VarF.toArray();
       for (int i=0;i<temp.length-1;i++){
           VarFactor.add((String)temp[i]);
       }
    }
    

}
