package Project;
import Wordle.Dictionary;

public class GPStools {
    private static double radius = 6371;

    public static void main(String[] args){
        Dictionary coords = new Dictionary("C://schoolcoords.txt");
        int numSchools = coords.getSize();

       Schools[] schools = new Schools[numSchools];

           for(int i = 0; i<numSchools;i++){
               String temp = coords.getWord(i);
               String[] coordcopy = temp.split("\\s+");
               double latitude = Double.parseDouble(coordcopy[0]);
               double longitude = Double.parseDouble(coordcopy[1]);
               schools[i] = new Schools(latitude,longitude,numSchools,0);
           }
           for(int i = 0; i<numSchools; i++){
               for(int j = 0; j<numSchools; j++){
                       double dist = distance(schools[i].latitude,schools[i].longitude,schools[j].latitude,schools[j].longitude);
                       if(dist>0.5) {
                           schools[i].distances[j] = dist;
                       }
                       else{
                           schools[i].distances[j] = 100000;
                       }
               }
               schools[i].find10thfurthest();
           }
           Schools epicentre = schools[0];
           double min = schools[0].tenthFurthestDistance;

           for(int i = 0; i<numSchools;i++){
                if(schools[i].tenthFurthestDistance<min){
                    epicentre = schools[i];
                    min = schools[i].tenthFurthestDistance;
                }
           }
           System.out.println(epicentre.latitude+" "+ epicentre.longitude+" "+ epicentre.tenthFurthestDistance);
    }
    public static double distance(double x1,double y1,double x2, double y2){
        double lat1 = Math.toRadians(x1);
        double long1 = Math.toRadians(y1);
        double lat2 = Math.toRadians(x2);
        double long2 = Math.toRadians(y2);
        double Deltalong = Math.abs(long1-long2);

        double sigma = Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(Deltalong));
        return radius*sigma;
    }
}
