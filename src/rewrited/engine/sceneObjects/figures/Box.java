package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.WithDefinedNormal;

import java.awt.*;
import java.util.function.Function;

public class Box extends FigureBase implements WithDefinedNormal {
    public Box(Function<Vec3d, Color> colorFunction) {
        super(Box::distanceFunction, colorFunction);
    }

    public Box(Vec3d position, Function<Vec3d, Color> colorFunction) {
        this(colorFunction);
        setPosition(position);
    }

    public Box(Vec3d position, double scale, Function<Vec3d, Color> colorFunction) {
        this(position, colorFunction);
        setScale(scale);
    }

    public Box(Vec3d position, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Color> colorFunction) {
        this(position, scale, colorFunction);
        setRotation(xRotation, yRotation, zRotation);
    }

    public static double distanceFunction(Vec3d p) {
        Vec3d q = new Vec3d(p);
        q.x = Math.abs(q.x);
        q.y = Math.abs(q.y);
        q.z = Math.abs(q.z);
        q.sub(new Vec3d(0.5, 0.5, 0.5));
        Vec3d result = new Vec3d(Math.max(q.x, 0), Math.max(q.y, 0), Math.max(q.z, 0));
        return result.length() + Math.min(Math.max(q.x,Math.max(q.y,q.z)),0.0);
    }

    public static double distanceFunction2(Vec3d point) {
        double xDistance = Math.abs(point.x) - 1.0 / 2;
        double yDistance = Math.abs(point.y) - 1.0 / 2;
        double zDistance = Math.abs(point.z) - 1.0 / 2;

        return Math.max(xDistance, Math.max(yDistance, zDistance));
    }

    public static Vec3d normalFunction(Vec3d point) {
        Vec3d result;
        double x = point.x, y = point.y, z = point.z;
        double xd = Math.abs(x), yd = Math.abs(y), zd = Math.abs(z);

        if (xd > yd && xd > zd) {
            result = new Vec3d(Utils.sign(x), 0, 0);
        } else if (yd > xd && yd > zd) {
            result = new Vec3d(0, Utils.sign(y), 0);
        } else result = new Vec3d(0, 0, Utils.sign(z));

        return result;
    }

    public Vec3d getNormal(Vec3d point) {
        return calculateNormal(Box::normalFunction, point);
    }
}
