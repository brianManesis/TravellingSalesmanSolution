package Project;

import java.util.Arrays;

public class Schools{
    public double latitude;
    public double longitude;
    double tenthFurthestDistance;
    double[] distances;
    int SchoolNum;
    int degree = 0;
    boolean wasVisited = false;

    public Schools(double latitude,double longitude,int numSchools,int SchoolNum){
        this.latitude = latitude;
        this.longitude = longitude;
        this.SchoolNum = SchoolNum;
        distances = new double[numSchools];
    }
    public int findClosest(){
        double min = distances[0];
        int index = 0;
        for(int i = 0; i<distances.length;i++){
            double temp = distances[i];
            if(temp<min){
                min = temp;
                index = i;
            }
        }
        return index;
    }
    public void find10thfurthest(){
        Arrays.sort(distances);
        tenthFurthestDistance = distances[9];
    }
    public void printDistances(){
        Arrays.sort(distances);
        for(int i = 0; i<distances.length;i++){
            System.out.println(distances[i]);
        }
    }
}
