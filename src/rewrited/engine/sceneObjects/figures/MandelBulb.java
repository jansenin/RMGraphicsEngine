package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;

import java.awt.*;
import java.util.function.Function;

public class MandelBulb extends FigureBase implements WithDefinedBoundingBox {
    private double power = 8;

    public MandelBulb(Function<Vec3d, Color> colorFunction, double power) {
        super();
        setDistanceAndColorFunctions(point -> distanceFunction(point, this.power), colorFunction);
        this.power = power;
    }

    public MandelBulb(Vec3d position, Function<Vec3d, Color> colorFunction, double power) {
        this(colorFunction, power);
        setPosition(position);
    }

    public MandelBulb(Vec3d position, double scale, Function<Vec3d, Color> colorFunction, double power) {
        this(position, colorFunction, power);
        setScale(scale);
    }

    public MandelBulb(Vec3d position, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction, double power) {
        this(position, scale, colorFunction, power);
        setRotation(xRotation, yRotation, zRotation);
    }

    @Override
    public boolean isInBoundingBox(Vec3d vector) {
        vector = getUntransformedVector(vector);
        return vector.length() < 5;
    }

    public static double distanceFunction(Vec3d point, double power) {
        double dr = 1;
        double r = 0;
        Vec3d z = new Vec3d(point);
        for (int i = 0;i < 1000;i++) {
            r = z.length();
            if (r > 2) break;
            double theta = Math.acos(z.z / r) * power;
            double phi = Math.atan2(z.y, z.x) * power;
            double zr = Math.pow(r, power);
            dr = Math.pow(r, power - 1) * power * dr + 1;

            z = new Vec3d(Math.sin(theta) * Math.cos(phi), Math.sin(phi) * Math.sin(theta), Math.cos(theta));
            z.mul(zr);
            z.add(point);
        }
        return 0.5 * Math.log(r) * r / dr;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }
}