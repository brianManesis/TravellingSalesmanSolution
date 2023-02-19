package Project;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.*;

public class GUI extends JPanel{

    ArrayList<Schools> route;
    public GUI(ArrayList<Schools> route){
        this.route = route;

    }
    public void paintComponent(Graphics grf){
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D)grf;
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        double[] y = new double[route.size()];
        double[] x = new double[route.size()];
        double RADIUS_MAJOR = 6378137.0;
        double RADIUS_MINOR = 6356752.3142;

        for(int i = 0;i< route.size();i++){
            Schools current = route.get(i);
            y[i] = current.latitude;
            y[i] = 4*((Math.log(Math.tan(Math.PI / 4 + Math.toRadians(y[i]) / 2)) * RADIUS_MAJOR)/10000)-2500;

            x[i] = current.longitude;
            x[i] = -1.2*((Math.toRadians(x[i]) * RADIUS_MAJOR)/1000)-800;
        }
        for(int i = 0;i< route.size();i++){
            graph.fill(new Ellipse2D.Double(x[i]-2,y[i]-2,4,4));
        }
        for(int i = 0; i<route.size()-1;i++){
           graph.draw(new Line2D.Double(x[i],y[i],x[i+1],y[i+1]));
        }
    }
}
