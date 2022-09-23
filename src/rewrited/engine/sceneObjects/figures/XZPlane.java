package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.WithDefinedNormal;

import java.awt.*;
import java.util.function.Function;

public class XZPlane extends FigureBase implements WithDefinedNormal {
    public XZPlane(Function<Vec3d, Color> colorFunction) {
        super(XZPlane::distanceFunction, colorFunction);
    }

    public XZPlane(double yPosition, Function<Vec3d, Color> colorFunction) {
        this(colorFunction);
        setPosition(new Vec3d(0, yPosition, 0));
    }

    public XZPlane(double yPosition, double scale, Function<Vec3d, Color> colorFunction) {
        this(yPosition, colorFunction);
        setScale(scale);
    }

    public XZPlane(double yPosition, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction) {
        this(yPosition, scale, colorFunction);
        setRotation(xRotation, yRotation, zRotation);
    }

    public static double distanceFunction(Vec3d point) {
        return Math.abs(point.y);
    }

    public static Vec3d normalFunction(Vec3d point) {
        return point.y >= 0 ? new Vec3d(0, 1, 0) : new Vec3d(0, -1, 0);
    }

    public Vec3d getNormal(Vec3d point) {
        return calculateNormal(XZPlane::normalFunction, point);
    }
}