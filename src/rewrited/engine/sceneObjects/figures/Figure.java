package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;

import java.awt.*;

public interface Figure {
    double getDistance(Vec3d point);

    Color getColor(Vec3d point);

    Vec3d getPosition();

    Figure move(Vec3d movement);

    Figure setPosition(Vec3d position);

    double getScale();

    Figure setScale(double scale);

    double getxRotation();

    Figure setxRotation(double xRotation);

    double getyRotation();

    Figure setyRotation(double yRotation);

    double getzRotation();

    Figure setzRotation(double zRotation);

    Figure setRotation(double xRotation, double yRotation, double zRotation);

    Figure rotate(double xRotation, double yRotation, double zRotation);
}
