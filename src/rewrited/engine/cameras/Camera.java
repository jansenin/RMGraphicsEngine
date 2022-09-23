package rewrited.engine.cameras;

import com.sun.javafx.geom.Vec3d;

public interface Camera {
    void moveTowards(double amount);

    void moveBackwards(double amount);

    void moveRight(double amount);

    void moveLeft(double amount);

    void move(Vec3d movement);

    void setViewAngle(double xAngle, double yAngle);

    void setPosition(Vec3d position);

    void setCameraDirection(Vec3d vector);

    void rotateScreenX(double rotation);

    void rotateScreenY(double rotation);

    Vec3d getPosition();

    double getViewAngleX();

    double getViewAngleY();

    Vec3d getDirection();

    Vec3d getDirectionVectorFromDisplayCoordinate(int displayWidth, int displayHeight, int x, int y);
}
