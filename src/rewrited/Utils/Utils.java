package rewrited.Utils;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.sceneObjects.figures.Figure;

import static java.lang.Math.*;

public class Utils {
    public static final double epsilon = 1e-14;

    public static float clamp(float a, float min, float max) {
        return max(min(a, max), min);
    }

    public static double clamp(double a, double min, double max) {
        return max(min(a, max), min);
    }

    public static double mod(double a, double b) {
        return a - b * floor(a / b);
    }

    public static int sign(double a) {
        return a >= 0 ? 1 : -1;
    }

    public static double mix(double x, double y, double a) {
        return x * (1 - a) + y * a;
    }

    public static Vec3d getModdedPosition(Vec3d point, double modulo) {
        return new Vec3d(
                Utils.mod(point.x, modulo),
                Utils.mod(point.y, modulo),
                Utils.mod(point.z, modulo)
        );
    }

    public static boolean isItEqualPoints(Vec3d point1, Vec3d point2, double treshhold) {
        Vec3d a = new Vec3d(point1);
        a.sub(point2);
        return a.length() < treshhold;
    }

    private static double getNormalComponent(double v1, double v2, double v3) {
        if (v1 < v2 && v2 < v3) return v1 - v3;
        if (v1 > v2 && v2 > v3) return v1 - v3;
        return (v3 > v1) ? -(v3 + v1) : (v3 + v1);
    }

    public static Vec3d getNormal2(Figure figure, Vec3d point) {
        Vec3d dxv = new Vec3d(epsilon, 0, 0);
        Vec3d dyv = new Vec3d(0, epsilon, 0);
        Vec3d dzv = new Vec3d(0, 0, epsilon);

        Vec3d px1 = new Vec3d(point);
        px1.add(dxv);
        Vec3d px2 = new Vec3d(point);
        px2.sub(dxv);

        Vec3d py1 = new Vec3d(point);
        py1.add(dyv);
        Vec3d py2 = new Vec3d(point);
        py2.sub(dyv);

        Vec3d pz1 = new Vec3d(point);
        pz1.add(dzv);
        Vec3d pz2 = new Vec3d(point);
        pz2.sub(dzv);

        double dx1 = figure.getDistance(px1), dx2 = figure.getDistance(point), dx3 = figure.getDistance(px2);
        double dy1 = figure.getDistance(py1), dy2 = dx2                      , dy3 = figure.getDistance(py2);
        double dz1 = figure.getDistance(pz1), dz2 = dx2                      , dz3 = figure.getDistance(pz2);

        Vec3d result = new Vec3d(
                getNormalComponent(dx1, dx2, dx3),
                getNormalComponent(dy1, dy2, dy3),
                getNormalComponent(dz1, dz2, dz3));
        result.normalize();
        return result;
    }

    public static Vec3d getNormal(Figure figure, Vec3d point) {
        Vec3d dxv = new Vec3d(epsilon, 0, 0);
        Vec3d dyv = new Vec3d(0, epsilon, 0);
        Vec3d dzv = new Vec3d(0, 0, epsilon);

        Vec3d px1 = new Vec3d(point);
        px1.add(dxv);
        Vec3d px2 = new Vec3d(point);
        px2.sub(dxv);

        Vec3d py1 = new Vec3d(point);
        py1.add(dyv);
        Vec3d py2 = new Vec3d(point);
        py2.sub(dyv);

        Vec3d pz1 = new Vec3d(point);
        pz1.add(dzv);
        Vec3d pz2 = new Vec3d(point);
        pz2.sub(dzv);

        double dx = figure.getDistance(px1) - figure.getDistance(px2);
        double dy = figure.getDistance(py1) - figure.getDistance(py2);
        double dz = figure.getDistance(pz1) - figure.getDistance(pz2);

        Vec3d result = new Vec3d(dx, dy, dz);
        result.normalize();
        return result;
    }

    public static double getAngleBetweenSurfaceAndRay(Raymarcher.RaymarchResult raymarchResult) {
        double lastDistance = raymarchResult.steps.get(raymarchResult.steps.size() - 1).minDistanceToFigure;
        double prelastDistance = raymarchResult.steps.get(raymarchResult.steps.size() - 1 - 1).minDistanceToFigure;
        double alpha = asin((prelastDistance - lastDistance) / prelastDistance);

        return alpha;
    }

    public static Vec3d zRotation(Vec3d vector, double zRotation) { // in degrees
        double alpha = atan2(vector.y, vector.x) + zRotation * PI/180;
        double targetLength = sqrt(vector.y*vector.y + vector.x*vector.x);
        return new Vec3d(cos(alpha) * targetLength, sin(alpha) * targetLength, vector.z);
    }

    public static Vec3d yRotation(Vec3d vector, double yRotation) { // in degrees
        double alpha = atan2(vector.x, vector.z) + yRotation * PI/180;
        double targetLength = sqrt(vector.x*vector.x + vector.z*vector.z);
        return new Vec3d(sin(alpha) * targetLength, vector.y, cos(alpha) * targetLength);
    }

    public static Vec3d xRotation(Vec3d vector, double xRotation) { // in degrees
        double alpha = atan2(vector.z, vector.y) + xRotation * PI/180;
        double targetLength = sqrt(vector.y*vector.y + vector.z*vector.z);
        return new Vec3d(vector.x, cos(alpha) * targetLength, sin(alpha) * targetLength);
    }

    public static Vec3d rotateScreen(Vec3d vector, double rotationX, double rotationY) { // in degrees
        vector = new Vec3d(vector);
        vector.normalize();
        rotationX *= PI / 180;
        rotationY *= PI / 180;
        Vec3d A = new Vec3d(vector);
        Vec3d B = new Vec3d(vector.z, 0, -vector.x);
        B.normalize();
        Vec3d C = new Vec3d();
        C.cross(A, B);

        Vec3d v2 = new Vec3d();
        A.mul(sin(PI / 2 - rotationX));
        B.mul(cos(PI / 2 - rotationX));
        v2.add(A);
        v2.add(B);

        Vec3d v3 = new Vec3d();
        C.mul(sin(rotationY));
        v2.mul(cos(rotationY));
        v3.add(C);
        v3.add(v2);

        return v3;
    }
}
