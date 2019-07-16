/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aviya
 */
public class Node {
    Var var;
     String entry;
     
     public Node (Var v,String e){
         this.var= v;
         this.entry= e;
     }
     
     public Var GetVar(){
         return this.var;
     }
     
     public String GetEntry(){
         return this.entry;
     }
}
