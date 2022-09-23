package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.WithDefinedNormal;

import java.awt.*;
import java.util.function.Function;

public class YZPlane extends FigureBase implements WithDefinedNormal {
    public YZPlane(Function<Vec3d, Color> colorFunction) {
        super(YZPlane::distanceFunction, colorFunction);
    }

    public YZPlane(double xPosition, Function<Vec3d, Color> colorFunction) {
        this(colorFunction);
        setPosition(new Vec3d(xPosition, 0, 0));
    }

    public YZPlane(double xPosition, double scale, Function<Vec3d, Color> colorFunction) {
        this(xPosition, colorFunction);
        setScale(scale);
    }

    public YZPlane(double xPosition, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction) {
        this(xPosition, scale, colorFunction);
        setRotation(xRotation, yRotation, zRotation);
    }

    public static double distanceFunction(Vec3d point) {
        return Math.abs(point.x);
    }

    public static Vec3d normalFunction(Vec3d point) {
        return point.x >= 0 ? new Vec3d(1, 0, 0) : new Vec3d(-1, 0, 0);
    }

    public Vec3d getNormal(Vec3d point) {
        return calculateNormal(YZPlane::normalFunction, point);
    }
}