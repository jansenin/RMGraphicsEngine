package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.sceneObjects.WithDefinedNormal;

import java.awt.*;
import java.util.function.Function;

public class Sphere extends FigureBase implements WithDefinedNormal{
    public Sphere(Function<Vec3d, Color> colorFunction) {
        super(Sphere::distanceFunction, colorFunction);
    }

    public Sphere(Vec3d position, Function<Vec3d, Color> colorFunction) {
        this(colorFunction);
        setPosition(position);
    }

    public Sphere(Vec3d position, double scale, Function<Vec3d, Color> colorFunction) {
        this(position, colorFunction);
        setScale(scale);
    }

    public Sphere(Vec3d position, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction) {
        this(position, scale, colorFunction);
        setRotation(xRotation, yRotation, zRotation);
    }

    @Override
    public Vec3d getNormal(Vec3d vector) {
        return calculateNormal(point -> {
            Vec3d result = new Vec3d(point);
            result.normalize();
            return result;
        }, vector);
    }

    public static double distanceFunction(Vec3d point) {
        return point.length() - 1;
    }

    public double getRadius() {
        return getScale();
    }
}