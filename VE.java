
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Aviya
 */
public class VE {

    Vector<String> Hidden;
    Vector<String> Avidence;
    Vector<String> VallAvidence;
    Vector<String> Q;
    HashMap<String, Var> variables;
    ArrayList<Factor2> Factor;
    Vector<String> Names;
    Vector<String> ancestorsOfEvidanceOrQueries;
    String Line;
    String FinalAns;

    public VE(String line, HashMap variables) {
        Line = "";
        this.variables = variables;
        this.Line = line;
        Avidence = new Vector<String>();
        VallAvidence = new Vector<String>();
        Q = new Vector<String>();
        Names = new Vector<String>();
        Hidden = new Vector<String>();
        ancestorsOfEvidanceOrQueries = new Vector<String>();
        Factor = new ArrayList<Factor2>();
        FillData(line);
        CreatFactor();
       FinalAns = Algo(Line);
    }
    public String GetAns(){
        return this.FinalAns;
    }

    public void FillData(String line) {
        int count = 0;
        System.out.println(line);
        String SubLine = line.substring(1);
        int i = 1;
        while (line.length() - 1 > i) {
            char tempChar = line.charAt(i);
            if (tempChar == '=') {
                count++;
                String entry = "";
                entry += line.charAt(i - 1);
                if (count == 1) {
                    Q.add(entry);
                } else {
                    Avidence.add(entry);
                    String AVal = "";
                    i++;
                    while (tempChar != ',') {

                        tempChar = line.charAt(i);
                        AVal += tempChar;
                        i++;
                    }
                    if (AVal.charAt(AVal.length() - 2) == ')') {
                        AVal = AVal.substring(0, AVal.length() - 2);
                        VallAvidence.add(AVal);
                    } else {
                        AVal = AVal.substring(0, AVal.length() - 1);
                        VallAvidence.add(AVal);
                    }
                }

            }

            i++;
        }
        int s = 0;
        for (int k = 0; k < line.length(); k++) {
            //  System.out.println(line.charAt(k));
            if (line.charAt(k) == '-') {
                s = k;
                break;
            }
        }
        i = s;
        boolean firstime = true;
        while (line.length() > i) {
            if (firstime == true) {
                firstime = false;
                String entry = "";
                entry += line.charAt(i - 1);
                if (!Hidden.contains(entry)) {
                    Hidden.add(entry);
                }
            } else if (line.charAt(i) != '-') {
                String entry = "";
                entry += line.charAt(i);
                Hidden.add(entry);
            }

            i++;
        }
    }

    public void CreatFactor() {
        for (Map.Entry<String, Var> entry : variables.entrySet()) {
            String key = entry.getKey();
            Names.add(key);// the new of the Vertex on this hash 
        }
        boolean Flag = false;
        int NumFactor = 0;
        for (Map.Entry<String, Var> entry : variables.entrySet()) {
            NumFactor++;
            String key = entry.getKey();
            Vector<String> VarCPT = new Vector<String>();

            Var currVar = variables.get(key);
            String[][] cpt = currVar.getCPT();

            for (int j = 0; j < cpt[0].length; j++) {
                if (!Names.contains(cpt[0][j])) {
                    break;
                } else {
                    VarCPT.add(cpt[0][j]);// the vertex on this Factor
                }
            }

            for (int i = 0; i < VarCPT.size(); i++) {
                String VC = VarCPT.get(i);
                if (Avidence.contains(VC)) {
                    System.out.println("VC=" + VC);
                    int index = Avidence.indexOf(VC);
                    System.out.println("cpt=" + cpt[0].length);
                    for (int j = 0; j < cpt[0].length - 1; j++) {
                        if (cpt[0][j].equals(VC)) {
                            for (int k = 1; k < cpt.length; k++) {
                                if (!cpt[k][j].equals(VallAvidence.get(index))) {
                                    cpt[k][j] = "null";
                                }
                            }
                        }

                    }

                }

            }
            System.out.println("Var --" + currVar.getName());
            for (int i = 0; i < cpt.length; i++) {
                for (int j = 0; j < cpt[0].length; j++) {
                    System.out.print(cpt[i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("Var Fac----------------------");
            Factor2 FAC = new Factor2(VarCPT, variables, cpt);
            Vector<Vector<String>> FactorMat = FAC.getFactorMat();
            removeCols(FactorMat);
            Factor.add(FAC);
        }
        for (int i = 0; i < Factor.size(); i++) {
            Factor2 F = Factor.get(i);
            F.FillVar();
        }

    }

    public void removeCols(Vector<Vector<String>> Fac) {
        int index = 0;
        for (int i = 0; i < Fac.get(0).size(); i++) {
            if (Avidence.contains(Fac.get(0).get(i))) {
                index = i;
                for (int j = 0; j < Fac.size(); j++) {
                    Fac.get(j).remove(index);
                }
            }
        }
    }

    public String Algo(String Line) {
        while (Hidden.size() > 0) {//while there is a hidden variable to eliminate left
            String currVar = Hidden.get(0);
            System.out.println(currVar);
            Factor2 fac1 = getNextFactorOfVar(currVar);
            //System.out.println("PPRINT FAC 1");
          //  fac1.PrintFactor();
            Factor2 fac2 = getNextFactorOfVar(currVar);
            //System.out.println("PPRINT FAC 2");
            if (fac2 == null) {
              //  System.out.println("[]");
            } else {
               // fac2.PrintFactor();
            }
            if (fac2 == null) {//there is only one factor with that variable, do eliminate
                if (fac1.getVarFVector().size() > 1) {
                    eliminate(fac1, currVar);//eliminate and delete var from hidden
                    Hidden.remove(0);
                } else {
                    Factor.add(fac1);
                    for (int i = 0; i < Hidden.size(); i++) {
                        if (Hidden.get(i) == currVar) {
                            Hidden.remove(i);
                            break;
                        }
                    }
                }
            } else {
                Join(fac1, fac2, currVar);
            }
        }
        while (Factor.size() > 1) {//Join all remaining factors
            Factor2 fac1 = getNextFactor();
            Factor2 fac2 = getNextFactor();
            JoinLast(fac1, fac2);
        }
        Factor2 ans = getFinalanans(Factor.get(0));
        String FinalAns = GetAns(ans, Line);
        return FinalAns;
    }

    public String GetAns(Factor2 ans, String Line) {// get the final factor after nermol ans return the cuurect ans
        System.out.println("line =" + Line);
        String StringAns = "";
        String sub = "";
        int indexSrart = 0;
        int indexEnd = 0;
        for (int i = 0; i < Line.length(); i++) {
            if (Line.charAt(i) == '=') {
                indexSrart = i;
            }
            if (Line.charAt(i) == '|') {
                indexEnd = i;
                break;
            }
        }
        sub = Line.substring(indexSrart + 1, indexEnd);

        Vector<Vector<String>> Mat = new Vector<Vector<String>>();
        Mat = ans.getFactorMat();
        for (int i = 1; i < Mat.size(); i++) {
            if (Mat.get(i).get(0).equals(sub)) {
                return Mat.get(i).get(1);
            }
        }

        return StringAns;
    }

    public void JoinLast(Factor2 one, Factor2 two) {// do join beetween the 2 last factor of the qu
        Vector<Vector<String>> ans = new Vector<Vector<String>>();
        Vector<String> input = new Vector<String>();
        Vector<String> vecclon = new Vector<String>();
        ans.add(one.getFactorMat().get(0));
        for (int i = 1; i < one.getFactorMat().size(); i++) {
            for (int j = 1; j < two.getFactorMat().size(); j++) {
                if (one.getFactorMat().get(i).get(0).equals(two.getFactorMat().get(j).get(0))) {
                    input.add(two.getFactorMat().get(j).get(0));
                    String Sum1 = one.getFactorMat().get(i).get(one.getFactorMat().get(i).size() - 1);
                    String Sum2 = two.getFactorMat().get(j).get(two.getFactorMat().get(j).size() - 1);
                    double sumAns = Double.parseDouble(Sum1) * Double.parseDouble(Sum2);
                    double total = sumAns;
                    String total2 = String.valueOf(sumAns);
                    input.add(total2);
                    vecclon = (Vector) input.clone();
                    input.removeAllElements();
                    ans.add(vecclon);
                }
            }
        }
        Vector<String> var = new Vector<>();
        var.add(ans.get(0).get(0));
        Factor2 answer = new Factor2(var, ans);
        this.Factor.add(answer);
    }

    public double Nermol(Factor2 f) {// culculet the alfa
        double ans = 0;
        double sum = 0;
        String sumstring = "";
        for (int i = 1; i < f.getFactorMat().size(); i++) {
            sumstring = f.getFactorMat().get(i).get(f.getFactorMat().get(i).size() - 1);
            System.out.println(sumstring);
            sum += Double.parseDouble(sumstring);
        }
        ans = 1 / sum;
        return ans;
    }

    public Factor2 getFinalanans(Factor2 fac) {// culculate the final answer at the last factor
        String ans = "";
        double Alfa = Nermol(fac);
        String sumstring = "";
        double sum = 0;
        for (int i = 1; i < fac.getFactorMat().size(); i++) {
            sumstring = fac.getFactorMat().get(i).get(fac.getFactorMat().get(i).size() - 1);
            sum = Double.parseDouble(sumstring) * Alfa;
            double total = sum;
            String total2 = String.valueOf(sum);
            fac.FactorMat.get(i).remove(fac.FactorMat.get(i).size() - 1);
            fac.FactorMat.get(i).add(total2);
        }

        return fac;
    }

    public Factor2 getNextFactorOfVar(String var) {
        Factor2 ans = null;
        for (int i = 0; i < Factor.size(); i++) {
            if (Factor.get(i).isin(var)) {// if the Factor Val include the var
                if (ans == null) {
                    ans = Factor.get(i);
                } else if (Factor.get(i).getFactorSize() < ans.getFactorSize()) {//if this factor is smaller
                    ans = Factor.get(i);
                }
            }
        }
        if (ans != null) {
            Factor.remove(ans);
        }
        return ans;
    }

    public Factor2 getNextFactor() {
        for (int i = 0; i < Factor.size(); i++) {
           // this.Factor.get(i).PrintFactor();
        }
        Factor2 ans = Factor.get(0);
        for (int i = 1; i < Factor.size(); i++) {
            if (Factor.get(i).getFactorSize() < ans.getFactorSize()) {//if this factor is smaller
                ans = Factor.get(i);

            }
        }
        Factor.remove(ans);
        return ans;
    }

    public void eliminate(Factor2 fac, String varToEliminate) {// 
        int index = 0;
        Vector<Vector<String>> MatFactor = fac.getFactorMat();
        Vector<Vector<String>> ans = new Vector<Vector<String>>();
        System.out.println("print fac inter eli");
        fac.PrintFactor();
        Vector<String> vecclone3 = new Vector<String>();
        Vector<String> vecclone2 = new Vector<String>();
        Vector<String> vecclone = new Vector<String>();
        Vector<String> input = new Vector<String>();
        Vector<String> input2 = new Vector<String>();
        Vector<Vector<String>> FactorMat = new Vector<Vector<String>>();
        for (int i = 0; i < MatFactor.get(0).size() - 1; i++) {
            input.add(MatFactor.get(0).get(i));
            if (MatFactor.get(0).get(i).equals(varToEliminate)) {
                index = i;
            }
        }
        boolean Flag = true;
        input.remove(index);
        vecclone = (Vector) input.clone();
        ans.add(vecclone);
        ans.get(0).add("pro");
        input.removeAllElements();
        String SumString = "";
        double sum = 0;
        for (int i = 1; i < MatFactor.size() - 1; i++) {
            sum = 0;
            SumString = "";
            input.removeAllElements();
            for (int j = 0; j < fac.getFactorMat().get(0).size() - 1; j++) {
                input.add(MatFactor.get(i).get(j));
            }
            SumString = MatFactor.get(i).get(MatFactor.get(i).size() - 1);
            sum += Double.parseDouble(SumString);
            input.remove(index);
            for (int k = i + 1; k < fac.getFactorMat().size(); k++) {
                for (int j = 0; j < fac.getFactorMat().get(0).size() - 1; j++) {
                    input2.add(MatFactor.get(k).get(j));
                }
                input2.remove(index);
                Flag = isequals(input, input2);
                input2.removeAllElements();
                if (Flag == true) {
                    SumString = MatFactor.get(k).get(fac.getFactorMat().get(k).size() - 1);

                    sum += Double.parseDouble(SumString);
                    double total = sum;
                    String total2 = String.valueOf(total);
                    input.add(total2);
                    vecclone2 = (Vector) input.clone();
                    ans.add(vecclone2);
                    input.remove(input.size() - 1);
                } else {
                }

            }
        }

        vecclone3 = (Vector) ans.get(0).clone();
        Vector<String> var = vecclone3;
        var.remove(var.size() - 1);
        Factor2 answer = new Factor2(var, ans);
       // answer.PrintFactor();
        System.out.println(answer.getFactorSize());
        this.Factor.add(answer);
    }

    public void Join(Factor2 fac1, Factor2 fac2, String currVar) {// do join between 2 factor
        // remove the 2 factor from the ARRAYLIST of factor and inter the new factor anter join 
        Vector<Vector<String>> MatFactor1 = fac1.getFactorMat();
        Vector<Vector<String>> MatFactor2 = fac2.getFactorMat();
        Vector<Vector<String>> ans = new Vector<Vector<String>>();
        Vector<String> input = new Vector<String>();
        Vector<String> vecclone = new Vector<String>();
        int index2 = 0;
        for (int i = 0; i < MatFactor2.size(); i++) {
            input = MatFactor2.get(i);
            vecclone = (Vector) input.clone();
            ans.add(vecclone);
        }
        for (int i = 0; i < MatFactor2.get(0).size() - 1; i++) {// fine the index Avidence at the factor
            if (MatFactor2.get(0).get(i).equals(currVar)) {
                index2 = i;
            }
        };
        for (int i = 1; i < MatFactor1.size(); i++) {
            String Val = MatFactor1.get(i).get(0);
            for (int j = 0; j < MatFactor2.size(); j++) {
                if (MatFactor2.get(j).get(index2).equals(Val)) {
                    String Sum1 = MatFactor1.get(i).get(MatFactor1.get(0).size() - 1);
                    String Sum2 = MatFactor2.get(j).get(MatFactor2.get(0).size() - 1);
                    double sumAns = Double.parseDouble(Sum1) * Double.parseDouble(Sum2);
                    double total = sumAns;
                    String total2 = String.valueOf(sumAns);
                    ans.get(j).remove(ans.get(j).get(MatFactor2.get(j).size() - 1));
                    ans.get(j).add(total2);
                }
            }
        }
        Vector<String> var = ans.get(0);
        Factor2 answer = new Factor2(var, ans);
        this.Factor.add(answer);

    }

    public void fillAncestorOfEvidanceOrQueries(String currVar) {
        if (variables.get(currVar).getParents().size() == 0) {//if the variable has no parent- stop
            return;
        } else {
            for (int j = 0; j < variables.get(currVar).getParents().size(); j++) {
                char parent = variables.get(currVar).getParents().get(j).getName();
                String sname = "";
                sname += parent;
                if (!ancestorsOfEvidanceOrQueries.contains(parent)) {
                    ancestorsOfEvidanceOrQueries.add(sname);
                    fillAncestorOfEvidanceOrQueries(sname);//find the parent ancestors
                }
            }
        }
    }

    public boolean isAncestor(String var) {
        for (int i = 0; i < ancestorsOfEvidanceOrQueries.size(); i++) {
            if (ancestorsOfEvidanceOrQueries.get(i) == var) {
                return true;
            }
        }
        return false;
    }

    public boolean isequals(Vector<String> one, Vector<String> two) {// if the vector are equals
        for (int i = 0; i < one.size(); i++) {
            if (!one.get(i).equals(two.get(i))) {
                return false;
            }
        }

        return true;

    }

}
