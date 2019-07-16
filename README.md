#Mapping by Drone
package sonar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
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
public class FinalProject {
    	static final double EARTH_RADIUS = 1000*6371;

    
    
   public static double [] zavit(double yaw, double pitch, double roll ,double dist ){//The function calculates the angle return the new X Y 

double [] arr= new double[3];
double []arrRad= new double[3];
double Ra=0;
arr[0]= yaw;
arr[1]= pitch;
arr[2]= roll;
for (int i=0;i<arr.length;i++){
    arrRad[i]= (3.14*arr[i])/180;
}
 
for (int i=1;i<arr.length;i++){
    arrRad[i]= dist*Math.sin(arrRad[i]);
   // System.out.println(arrRad[i]);
}
double SQRTDPitchDRoll= Math.sqrt(arrRad[1]*arrRad[1]+arrRad[2]*arrRad[2]);
        //System.out.println(SQRTDPitchDRoll);
        
 double ATAN2=Math.atan2(arrRad[1], arrRad[2]);

        double Arc2PI= (ATAN2*180)/3.14;
        double ArcRYaw= ATAN2+arrRad[0];
            double ArcPIYaw= Arc2PI+arr[0];
        double DPitchYaw= SQRTDPitchDRoll*Math.sin(ArcRYaw);
         double DRollYaw= SQRTDPitchDRoll* Math.cos(ArcRYaw);
        double ans []= new double[2];
        ans[0]= DPitchYaw;
        ans[1]= DRollYaw;

              return ans;

    }
    public static double MathFunction(String line,int IndexStart,int IndexEnd){// take the val we want between 2 index
            char temp='?';
            int count =0;
            int start=0;
            int end=0;
            int i=0;
            if (line==null)return 0;
            temp=line.charAt(1);
            if (temp==' ')
                return 0;
            while ( count <1000){
                temp=line.charAt(i);
                //System.out.println("temp : "+i+"  "+temp);
            if (temp==','){
                count++;
                if (count==IndexStart){ 
                    start= i+1;
                  //  System.out.println("Count : "+count);
                } 
                if (count==IndexEnd){
                    end= i-1;

              String number = line.substring(start+1, end+1);
           //  System.out.println(number);
           
             String input =number;
               double x= Double.parseDouble(number);

                     return x;
                    
                }
            } 
           i++;
        }

         return 0;   
        }
    
public static Vector<Double[]> calculation(){// return the pitch, yaw and roll
Vector<Double []> ans =new Vector<Double[]>();
    double yaw,pitch,roll;
 String line="";
    try{
		FileReader in;
			in= new FileReader("C:\\Users\\Aviya\\Desktop\\פרוייקט\\ATT.txt");
			BufferedReader bf = new BufferedReader(in);
			line= bf.readLine();
                       //System.out.println(line);
			while(line!=null){
                       roll= MathFunction(line,3 , 4);
                       pitch= MathFunction(line, 5, 6);
                      yaw= MathFunction(line, 7, 8);
                             double[] destination= zavit(yaw, pitch, roll, 10);
   
         line=bf.readLine();
                       }
                        bf.close();
	in.close();
    }
             catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    


return  ans;
}     
public static double [] ToRad (double Lat,double Lng,double Alt){
    Lat=(3.14*Lat)/180;
     Lng=(3.14*Lng)/180;
      Alt=(3.14*Alt)/180;
    double [] RAD= {Lat,Lng,Alt};
    return RAD;
}

public static void GetLocation () throws FileNotFoundException{
    // the fanction read the text save all the Details we need and creat new text file withe the new GPS point
    String line="";
    String SubCall="";
    Vector<double []> AllPoint= new Vector<double []>();
    Vector<Double> Distans= new Vector<Double>();
    int chek=0;
     double Dist= 10;
    double [] Start= new double[3];
    double roll=0;
     double Time=0.0;
                                   double pitch= 0;
                                   double yaw=0;
    int count=0;
    PrintWriter writer = new PrintWriter("outputExp2.txt");

    try{
		FileReader in;
			in= new FileReader("C:\\Users\\Aviya\\Desktop\\פרוייקט\\ניסוי 2\\ALL2.txt");
			BufferedReader bf = new BufferedReader(in);
			line= bf.readLine();
			while(line!=null){
                           
                                 SubCall= line.substring(0,3);
                                if(SubCall.contains("SON")){
                                     Time= MathFunction(line, 1, 2);
                                     Dist= MathFunction(line, 3, 4)/10;
 
                              }
                                 if(SubCall.contains("GPS")){
                                    
                                   
                                    double LAT= MathFunction(line, 7, 8);//x
                                    double LNG= MathFunction(line, 8, 9);//y
                                     double ALT= MathFunction(line, 9, 10);//z

                                  double XYZ[]= {LAT,LNG,ALT};
                                      count++;
                                      //if (LAT!= 0 && ALT!=0){
                                      if (Time<395925878 || Time>396525623)
                                      {
                                      AllPoint.add(XYZ);
                                     Distans.add(Dist);
                                      }
                                   
                                          Start[0]= LAT;
                                           Start[1]=LNG;
                                            Start[2]= ALT;  
                            
                                 }
                                    if(SubCall.contains("ATT")){
                                    roll= MathFunction(line,3 , 4);
                                    pitch= MathFunction(line, 5, 6);
                                    yaw= MathFunction(line, 7, 8);
                                    double RPW[]= {roll,pitch,yaw};
                                   double call[]= zavit(yaw, pitch, roll,Dist );

                                    }
                                 
                                    
         line=bf.readLine();
                       }
                        
                                        for (int i=0;i<AllPoint.size();i++){
                                        double [] TempArry= AllPoint.elementAt(i);
                                        }
                                        
                                         for (int i=0;i<AllPoint.size();i++){
                                        double [] TempArry= AllPoint.elementAt(i);
                                        if (flatWorldDist(TempArry, Start)!=null){
                                        double[] ArryDist= flatWorldDist(Start, TempArry);
                                        double [] ArryCal= {TempArry[0]+ArryDist[0], TempArry[1]+ArryDist[1], ArryDist[2], Distans.get(i)};
                                      System.out.println(ArryCal[0]+" , " + ArryCal[1]+ " , "+ ArryCal[2]);
                                         writer.println(ArryCal[0]+" , " + ArryCal[1]+ " , "+ ArryCal[2]);

                                        }
                                        }
                                         writer.close();
                                        
                        bf.close();
	in.close();
    }
             catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    


    
}

	public static double[] flatWorldDist(double[] ll1, double[] ll2) {
		double[] ans = new double[3];
		double dx = ll2[1]-ll1[1]; // delta lon east
		double dy = ll2[0]-ll1[0]; // delta lat north
		double dz = ll2[2]-ll1[2]; // delta alt
		if(Math.abs(dx)>0.1 | Math.abs(dy)>0.1) {
                    return null;}
		double x = EARTH_RADIUS * Math.sin(Math.toRadians(dx)) * Math.cos(Math.toRadians(ll1[0]));
		double y = EARTH_RADIUS * Math.sin(Math.toRadians(dy)); 
		ans[0] = x; ans[1]=y; ans[2] = dz;
		return ans;
	}//return the dist between point (x,y,z)
        
   public static void main(String[] args) throws FileNotFoundException {
  GetLocation();

   }
}
