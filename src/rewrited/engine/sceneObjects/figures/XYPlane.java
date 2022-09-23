package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.WithDefinedNormal;

import java.awt.*;
import java.util.function.Function;

public class XYPlane extends FigureBase implements WithDefinedNormal {
    public XYPlane(Function<Vec3d, Color> colorFunction) {
        super(XYPlane::distanceFunction, colorFunction);
    }

    public XYPlane(double zPosition, Function<Vec3d, Color> colorFunction) {
        this(colorFunction);
        setPosition(new Vec3d(0, 0, zPosition));
    }

    public XYPlane(double zPosition, double scale, Function<Vec3d, Color> colorFunction) {
        this(zPosition, colorFunction);
        setScale(scale);
    }

    public XYPlane(double zPosition, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction) {
        this(zPosition, scale, colorFunction);
        setRotation(xRotation, yRotation, zRotation);
    }

    public static double distanceFunction(Vec3d point) {
        return Math.abs(point.z);
    }

    public static Vec3d normalFunction(Vec3d point) {
        return point.z >= 0 ? new Vec3d(0, 0, 1) : new Vec3d(0, 0, -1);
    }

    public Vec3d getNormal(Vec3d point) {
        return calculateNormal(XYPlane::normalFunction, point);
    }
}