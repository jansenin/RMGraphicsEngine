package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;

import java.awt.*;
import java.util.function.Function;

public class BoxFractal extends FigureBase implements WithDefinedBoundingBox {
    private int iterationCount;

    public BoxFractal(Function<Vec3d, Color> colorFunction, int iterationCount) {
        super();
        setDistanceAndColorFunctions(point -> distanceFunction(point, this.iterationCount), colorFunction);
        this.iterationCount = iterationCount;
    }

    public BoxFractal(Vec3d position, Function<Vec3d, Color> colorFunction, int iterationCount) {
        this(colorFunction, iterationCount);
        this.setPosition(position);
    }

    public BoxFractal(Vec3d position, double scale, Function<Vec3d, Color> colorFunction, int iterationCount) {
        this(position, colorFunction, iterationCount);
        this.setScale(scale);
    }

    public BoxFractal(Vec3d position, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction, int iterationCount) {
        this(position, scale, colorFunction, iterationCount);
        this.setRotation(xRotation, yRotation, zRotation);
    }


    public static double distanceFunction(Vec3d pos, int iterationCount) {
        pos = new Vec3d(pos);
        double x = pos.x, y=pos.y, z=pos.z;
        x=x*1+0.5;y=y*1+0.5;z=z*1+0.5; //center it by changing position and scale

        double xx=Math.abs(x-0.5)-0.5, yy=Math.abs(y-0.5)-0.5, zz=Math.abs(z-0.5)-0.5;
        double d1=Math.max(xx,Math.max(yy,zz)); //distance to the box
        double d=d1; //current computed distance
        double p=1.0;
        for (int i=1; i<=iterationCount; ++i) {
            double xa = Utils.mod(3.0*x*p,3.0);
            double ya = Utils.mod(3.0*y*p,3.0);
            double za = Utils.mod(3.0*z*p,3.0);
            p*=3.0;

            //we can also translate/rotate (xa,ya,za) without affecting the DE estimate

            xx=0.5-Math.abs(xa-1.5); yy=0.5-Math.abs(ya-1.5); zz=0.5-Math.abs(za-1.5);
            d1=Math.min(Math.max(xx,zz),Math.min(Math.max(xx,yy),Math.max(yy,zz))) / p; //distance inside the 3 axis-aligned square tubes

            d=Math.max(d,d1); //intersection
        }
        //return d*2.0; //the distance estimate. The *2 is because of the scaling we did at the beginning of the function
        return d;
    }

    public static boolean isInBoundingBoxFunction(Vec3d vector) {
        return (Math.abs(vector.x) <= 0.5) || (Math.abs(vector.y) <= 0.5) || (Math.abs(vector.z) <= 0.5);
    }

    @Override
    public boolean isInBoundingBox(Vec3d vector) {
        return calculateIsInBoundingBox(BoxFractal::isInBoundingBoxFunction, vector);
    }

    public double getEdgeLength() {
        return getScale();
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }
}
