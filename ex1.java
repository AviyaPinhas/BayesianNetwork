/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import javax.script.ScriptEngine;

/**
 *
 * @author Aviya
 */
public class ex1 {

    HashMap<String, Var> variables = new HashMap<String, Var>();

    public ex1() {
        String fileName = "input.txt";
        ReadFile(fileName);
        // answerQueries();
    }

    public void ReadFile(String fileName) {
        String line = "";
        FileReader in;
        try {
            in = new FileReader("C:\\Users\\Aviya\\לימודים\\שנה ד\\קבלת החלטות\\input2.txt");
            BufferedReader bf = new BufferedReader(in);
            line = bf.readLine();
            while (line != null) {
                if (line.contains("Variables")) {
                    createVariables(line);
                }
                if (line.contains("Var") && !line.contains("Variables")) {
                    char varName = line.charAt(4);
                    fillVariableData(varName, bf);
                }
                if (line.contains("Queries")) {
                    while (line!=("")){
                    line = bf.readLine();
                        System.out.println(line);
                    if (line.charAt(0) != 'P') {
                        OrderChildren();
                       System.out.println("Bayes Ball");
                      boolean Flag=  BayesBall(line, variables);
                       System.out.println( "ans BB"+ Flag);
                        
                    } else {
                        System.out.println(" V.E"); 
                        VE ex = new VE(line, variables);
                        System.out.println("ans VE" +ex.GetAns());

                    }
                }
                }
                line = bf.readLine();
            }

            bf.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OrderChildren();

    }

    public void createVariables(String line) {
        String varName = "";
        int index = 11;//the index after the word "Variables: "
        for (int i = index; i < line.length(); i += 2) {
            varName += line.charAt(i);
            char Name = line.charAt(i);
            variables.put(varName, new Var(Name));// Key(String), Var
            varName = "";
        }
        //print Hash   
        for (Map.Entry<String, Var> entry : variables.entrySet()) {
            String key = entry.getKey();
            Var value = entry.getValue();
            //    System.out.println(" VAR- "+ "KEy"+ key+" val "+ value.getName());
            //     System.out.println("Key -"+ key+ " Name  - "+ value.getName()+ " Pare - "+ value.getParents().size());
            for (int i = 0; i < value.getParents().size(); i++) {
                //       System.out.println(value.getParents().get(i));
            }
        }

    }

    public void fillVariableData(char varName, BufferedReader bf) throws IOException {
        String Name = "";
        Name += varName;
        Var currVariable = variables.get(Name);//get the current variable
        Vector ParVal = currVariable.getParents();

        String[] parentArr = null;
        try {
            String line = bf.readLine();
            while (!line.equals("")) {//keep reading the input until he moves to a blank line
                int numOfParentsValues = 1;//, numOfParents=0;
                if (line.contains("Values")) {
                    line = line.substring(8);//after the "Values: " index
                    String[] valuesArr = line.split(", ");
                    for (int i = 0; i < valuesArr.length; i++) {
                        currVariable.addValue(valuesArr[i]);
                    }
                }
                if (line.contains("Parents")) {
                    line = line.substring(9);//after the "Parents: " index
                    parentArr = line.split(",");
                    if (!parentArr[0].equals("none")) {
                        for (int i = 0; i < parentArr.length; i++) {
                            Var tempNew = variables.get((parentArr[i]));
                            currVariable.addParent(tempNew);
                        }

                    }
                }

                if (line.contains("CPT")) {
                    int numOfRows = 1, numOfCols = 1;
                    for (int i = 0; i < currVariable.getParents().size(); i++) {//muliple of all the parents values size
                        if (currVariable.getParents().size() > 0) {//if the var have parent we want to add rows and cols
                            numOfParentsValues = currVariable.getParents().get(i).getValues().size();// chek how many val all parent  
                            numOfRows = numOfRows * (numOfParentsValues);// evye time add rows acording to the val
                        }
                    }
                    numOfRows++;// for the name at the top rows
                    numOfCols = currVariable.getParents().size() + currVariable.getValues().size() - 1;//num of parent+ num of values -1
                    String[][] CPT = new String[numOfRows][numOfCols];

                    for (int k = 0; k < currVariable.getParents().size(); k++) {//fill the parents

                        CPT[0][k] = parentArr[k];// add the parent to the matrix at the top row
                    }

                    for (int j = 1; j < numOfRows; j++) {//fill the CPT

                        line = bf.readLine();
                        String[] cptValuesArr = line.split(",");
                        if (j == 1) {//if its the first time
                            int index = currVariable.getParents().size();
                            for (int i = currVariable.getParents().size(); i < numOfCols; i++) {
                                String value = cptValuesArr[index].substring(1);//without the "="
                                CPT[0][i] = value;
                                index += 2;
                            }
                        }
                        for (int k = 0; k < currVariable.getParents().size(); k++) {//fill the parent values
                            CPT[j][k] = cptValuesArr[k];
                        }
                        int index = currVariable.getParents().size() + 1;
                        for (int l = currVariable.getParents().size(); l < numOfCols; l++) {
                            CPT[j][l] = cptValuesArr[index];
                            index += 2;
                        }
                    }

                    boolean flag = true;
                    String ans = "";
                    double sum = 0;

                    for (int i = 0; i < currVariable.getValues().size(); i++) {
                        String val = currVariable.getValues().get(i);
                        for (int j = 0; j < CPT[0].length; j++) {
                            if (val.equals(CPT[0][j])) {
                                break;
                            } else {
                                flag = false;
                                ans = val;
                            }
                        }

                    }
                    if (currVariable.getValues().size() > 2 || currVariable.getParents().size() == 0) {
                        String[][] NewCPT = new String[CPT.length + 1][CPT[0].length + 1];
                        System.out.println(currVariable.getName());
                        for (int i=0;i<CPT.length;i++){
                            for (int j=0;j<CPT[0].length;j++){
                        //        System.out.println(CPT[i][j]);
                            }
                        }
                        
                        for (int i = 0; i <=CPT[0].length; i++) {
                            NewCPT[1][i] = CPT[i][0];
                        }
                        NewCPT[NewCPT.length - 1][0] = ans;
                        double Sum = 0;
                        for (int i = 1; i < NewCPT.length - 1; i++) {
                            sum += Double.parseDouble(NewCPT[i][NewCPT[0].length - 1]);
                        }
                        double total = 1 - sum;
                        String total2 = String.valueOf(total);
                        NewCPT[NewCPT.length - 1][NewCPT[0].length - 1] = total2;
                        String name = "";
                        name += currVariable.getName();
                        NewCPT[0][0] = name;
                        NewCPT[0][1] = "p(" + currVariable.getName() + ")";

                        currVariable.setCPT(NewCPT);
                    } else {

                        String[][] NewCPT = new String[CPT.length][CPT[0].length + 1];
                        NewCPT[0][NewCPT[0].length - 1] = ans;
                        for (int i = 0; i < CPT.length; i++) {
                            for (int j = 0; j < CPT[0].length; j++) {
                                NewCPT[i][j] = CPT[i][j];

                            }
                        }
                        for (int i = 1; i < CPT.length; i++) {
                            sum = 0;
                            for (int j = currVariable.getParents().size(); j < CPT[0].length; j++) {
                                sum += Double.parseDouble(NewCPT[i][j]);
                            }
                            double total = 1 - sum;
                            String total2 = String.valueOf(total);
                            NewCPT[i][NewCPT[0].length - 1] = total2;
                        }

                        String ALLPER[][] = GetAllPer(NewCPT, currVariable);
                        currVariable.setCPT(ALLPER);
                    }
                    System.out.println("Var - " + currVariable.getName());

                    for (int i = 0; i < currVariable.getCPT().length; i++) {
                        for (int j = 0; j < currVariable.getCPT()[0].length; j++) {
                            System.out.print(currVariable.getCPT()[i][j] + " ");
                        }
                        System.out.println("");
                    }
                    System.out.println("-----------------------");
                }

                line = bf.readLine();
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String[][] GetAllPer(String[][] NewCPT, Var curr) {
        int count = 0;
        count = curr.getValues().size();
        for (int i = 0; i < curr.getParents().size(); i++) {
            Var per = curr.getParents().get(i);
            count = count * per.getValues().size();
        }

        count++;
        int cols = curr.getParents().size() + 2;
        String[][] All = new String[count][cols];
        String name = "";
        for (int i = 0; i < curr.getParents().size(); i++) {
            name = "";
            name += curr.getParents().get(i).getName();
            All[0][i] = name;
        }
        name = "";
        name += curr.getName();
        All[0][All[0].length - 2] = name;

        int countRows = 1;
        int countcols = 0;
        while (countRows < All.length) {
            for (int j = 1; j < NewCPT.length; j++) {
                for (int k = 0; k < curr.getParents().size(); k++) {
                    All[countRows][countcols] = NewCPT[j][k];
                    countcols++;
                    //System.out.println("j= "+j+ " k= "+ k +" All = "+ All[j][k]);
                }

                countcols = 0;
                countRows++;
            }
        }
        // int all the val of cuur var to the option

        int val = 0;
        countcols = 0;
        for (int j = 1; j < All.length; j++) {
            while (countcols < count / curr.getValues().size() && j < All.length) {
                All[j][All[0].length - 2] = curr.getValues().get(val);
                countcols++;
                j++;
            }
            j--;
            countcols = 0;
            val++;

        }
        int num = 0;
        int index = curr.getParents().size();
        String[] probabilty = new String[All.length - 1];
        while (index < NewCPT[0].length) {
            for (int i = 1; i < NewCPT.length; i++) {
                probabilty[num] = NewCPT[i][index];
                num++;
            }
            index++;
        }
        for (int i = 1; i < All.length; i++) {
            All[i][All[0].length - 1] = probabilty[i - 1];
        }

        System.out.println("Var - " + curr.getName());

        for (int i = 0; i < All.length; i++) {
            for (int j = 0; j < All[0].length; j++) {
                System.out.print(All[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("-----------------------");

        return All;
    }

    public void OrderChildren(){

		for (String name: variables.keySet()){
			String key =name.toString();
                        Var CV= variables.get(key);
			Vector<Var> par =CV.getParents();
			if(par.size()>0){
				for (int i = 0; i < par.size(); i++) {
                                    String namePar="" ;
                                    namePar +=CV.getParents().get(i).getName(); 
					variables.get(namePar).children.add(CV);
			
				}
			}

                        
		}
	}
    
   public  boolean BayesBall( String line,HashMap variables){
      String s= "";
      s+= line.charAt(0);
         Var  Start= this.variables.get(s);
         s="";
 s+= line.charAt(2);
         String Gool= s;
         Vector <String> AvidenceVal= new Vector<String>(); 
         String SubLine= line.substring(4);
             int i=1;
             while (SubLine.length()>i){
                 char tempChar=SubLine.charAt(i);
                if (tempChar=='='){
                    String entry= "";
                    entry+=SubLine.charAt(i-1);
                    AvidenceVal.add(entry);
                }
                 i++;
             }
            // System.out.println(AvidenceVal);
         boolean flag= AlgoBayesBall(Start,Gool,AvidenceVal, variables); 
return flag;       
   }
   
    public  boolean AlgoBayesBall(Var  Start, String Gool, Vector  AvidenceVal,HashMap variables){
        ArrayList <Node> closeList = new ArrayList<Node>();
           Queue<Node> in= new LinkedList<Node>();
        in.add(new Node(Start,""));
        while (!in.isEmpty()){
            Node n = in.remove();
           
           if (!AvidenceVal.contains(n.GetVar().getName())){//if the var not avidence
                for (int i=0;i<n.GetVar().getChild().size();i++){// 
                     Var tempChild =n.GetVar().getChild().get(i);
                    Node temp= new Node (tempChild,"F");
                      if (Gool.equals(temp.GetVar().getName())){
                        return false;//לא בת"ל
                      }
                 boolean Flag= false;
                    for (int k=0;k<closeList.size()-1;k++){
                        Node chek= closeList.get(k);
                        if (chek.GetEntry().equals(temp.GetEntry())){
                            if (chek.GetVar().getName()==temp.GetVar().getName()){
                                Flag=true;
                            }
                    }
                    }
                    if (Flag==false){
                        in.add(temp);
                    }
           }
                 for (int i=0;i<n.GetVar().getParents().size();i++){
                     Var tempChild =n.GetVar().getParents().get(i);   
                    Node temp= new Node (tempChild,"son");
                      if (Gool.equals(temp.GetVar().getName())){
                        return false;//לא בת"ל
                      }
                  boolean Flag= false;
                    for (int k=0;k<closeList.size()-1;k++){
                        Node chek= closeList.get(i);
                        if (chek.GetEntry().equals(temp.GetEntry())){
                            if (chek.GetVar().getName()==temp.GetVar().getName()){
                                Flag=true;
                            }
                    }
                    }
                     if (Flag==false){
                        in.add(temp);
                    }
           }            
        }
           else{
               for (int i=0;i<n.GetVar().getParents().size();i++){
                     Var tempChild =n.GetVar().getParents().get(i);   
                    Node temp= new Node (tempChild,"son");
                      if (Gool.equals(temp.GetVar().getName())){
                        return false;//לא בת"ל
                      }
                   boolean Flag= false;
                    for (int k=0;k<closeList.size()-1;k++){
                        Node chek= closeList.get(k);
                        if (chek.GetEntry().equals(temp.GetEntry())){
                            if (chek.GetVar().getName()==temp.GetVar().getName()){
                                Flag=true;
                            }
                   
                    }
                    }
                     if (Flag==false){
                        in.add(temp);
                    }
           }           
           }
           closeList.add(n);
        }
        System.out.println(closeList.size());
        return true;// לא מצאנו דרך להגיע ולכן בתל
    }
    

    
    public static void main(String[] args) {
        
        ex1 ex = new ex1();
        String line2 = "B-E|";
    }
}
