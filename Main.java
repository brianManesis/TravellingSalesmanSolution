package Project;

import Wordle.Dictionary;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {
    public static Dictionary coords = new Dictionary("C://schoolcoords.txt");
    public static Random random = new Random();
    public static int numSchools = coords.getSize();
    public static Schools[] schools = new Schools[numSchools];
    public static ArrayList<Integer> solution = new ArrayList<>();

    public static void main(String[] args) {


        for (int i = 0; i < numSchools; i++) {
            String temp = coords.getWord(i);
            String[] coordcopy = temp.split(",");
            double latitude = Double.parseDouble(coordcopy[1]);
            double longitude = Double.parseDouble(coordcopy[2]);
            int SchoolNum = Integer.parseInt(coordcopy[0]);
            schools[i] = new Schools(latitude, longitude, numSchools, SchoolNum);
        }
        double minDistance = 10000.0;
        ArrayList<Schools> bestRoute = new ArrayList<>();
        ArrayList<Schools> route = new ArrayList<>();

        for(int i = 0; i<1000;i++) {
            route = nearestNeighbor();
            double tempDist = totalDistance(route);
            if(tempDist<minDistance){
                minDistance = tempDist;
                bestRoute = route;
            }
        }

        ArrayList<Schools> opted = twoOpt(bestRoute);
            for(int i = 0; i< opted.size();i++){
                solution.add(opted.get(i).SchoolNum);
            }
            String solutionString = solution.get(0)+"";
            for (int i = 1; i < solution.size(); i++) {
                solutionString = solutionString+","+solution.get(i);
            }
            System.out.println(solutionString);
            System.out.println(totalDistance(opted)+" km");
        System.out.println(isValidSoluion(solution));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GUI(opted));
        frame.setSize(700,700);
        frame.setLocation(200,200);
        frame.setVisible(true);
    }


    public static ArrayList<Schools> nearestNeighbor() {
        ArrayList<Schools> route = new ArrayList<>();

        for(int i = 0; i<numSchools; i++){
            for(int j = 0; j<numSchools; j++){
                double dist = distance(schools[i].latitude,schools[i].longitude,schools[j].latitude,schools[j].longitude);
                if(dist>0) {
                    schools[i].distances[j] = dist+random.nextInt(5);
                }
                else{
                    schools[i].distances[j] = 100000;
                }
            }
        }
        int found = findMostIsolated();
        Schools current = schools[found];
        route.add(current);
        schools[found].wasVisited = true;

        for(int i = 0; i<numSchools;i++){
            int indexOfclosest = findClosestNonVisted(current);
            if(indexOfclosest != -1 ){
                Schools closest = schools[indexOfclosest];
                closest.wasVisited = true;
                route.add(closest);
                current = closest;
            }
        }
        int indexOfstart = 0;
        for(int i = 0; i<route.size();i++){
            if(route.get(i).SchoolNum == 0){
                indexOfstart = i;
            }
        }
        ArrayList<Schools> routeFromMaynooth = new ArrayList<>();

        for(int i = indexOfstart;i<route.size();i++){
            routeFromMaynooth.add(route.get(i));
        }
        for(int i = 0;i<indexOfstart;i++){
            routeFromMaynooth.add(route.get(i));
        }
        routeFromMaynooth.add(schools[0]);

        for(int i = 0; i<schools.length;i++){
            schools[i].wasVisited = false;
        }
        return routeFromMaynooth;
    }

    public static int findClosestNonVisted(Schools current){
        double[] dists = current.distances;
        for(int i = 0; i<dists.length;i++){
            int index = current.findClosest();

            if(!schools[index].wasVisited){
                return index;
            }
            else{
                current.distances[index] = 100000;
            }
        }
        return -1;
    }


    public static boolean isValidSoluion(ArrayList<Integer> route){
        ArrayList<Integer> routecopy = route;
        Collections.sort(routecopy);
        int first = routecopy.remove(0);
        if(first!=0){
            return false;
        }
        for(int i=0;i<route.size();i++){
           if(routecopy.get(i)!=i){
               return false;
           }
        }
        return true;
    }

    public static ArrayList<Schools> twoOpt(ArrayList<Schools> route){
        ArrayList<Schools> opt = new ArrayList<>();
        double bestDistance = totalDistance(route);
        boolean swapMade = true;

        while(swapMade){
            swapMade = false;
            for(int i = 1; i<route.size()-1;i++){
                for(int j = i+1; j<route.size()-1;j++){
                    ArrayList<Schools> temp = swap(route,i,j);
                    double tempDist = totalDistance(temp);
                    if(tempDist<bestDistance){
                        opt = temp;
                        bestDistance = tempDist;
                        swapMade = true;
                    }
                }
            }
        }
        return opt;
    }


    public static ArrayList<Schools> swap(ArrayList<Schools> route, int start,int end){
        ArrayList<Schools> opt = new ArrayList<>();
        for(int i = 0; i<start;i++){
            opt.add(route.get(i));
        }
        for(int i = end; i>=start; i--){
            opt.add(route.get(i));
        }
        for(int i = end+1;i<route.size();i++){
            opt.add(route.get(i));
        }
        return opt;
    }

    public static int findMostIsolated(){
        double max = schools[0].longitude;
        int index = 0;
        for(int i = 0; i<schools.length;i++){
            double temp = schools[i].longitude;
            if(temp>max){
                max = temp;
                index = i;
            }
        }
        return index;
    }

    public static double totalDistance(ArrayList<Schools> tour){
        double totalDist = 0.0;

        for(int i = 0; i<tour.size()-1;i++){
            Schools start = tour.get(i);
            Schools end = tour.get(i+1);
            double tempDist = distance(start.latitude,start.longitude,end.latitude,end.longitude);

            totalDist = totalDist+tempDist;
        }
        return totalDist;
    }

    public static double distance(double x1,double y1,double x2, double y2){
        double radius = 6371;
        double lat1 = Math.toRadians(x1);
        double long1 = Math.toRadians(y1);
        double lat2 = Math.toRadians(x2);
        double long2 = Math.toRadians(y2);
        double Deltalong = Math.abs(long1-long2);

        double sigma = Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(Deltalong));
        return radius*sigma;
    }
}
