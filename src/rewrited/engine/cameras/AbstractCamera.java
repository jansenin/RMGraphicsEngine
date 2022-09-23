package rewrited.engine.cameras;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;

import static java.lang.Math.*;

public abstract class AbstractCamera implements Camera{
    protected Vec3d position;

    protected double viewAngleX = 70; // default
    protected double viewAngleY = 70; // default

    protected final Vec3d defaultCameraDirection = new Vec3d(0, 0, 1); // normalized
    protected Vec3d cameraDirection = new Vec3d(defaultCameraDirection); // normalized

    public AbstractCamera(Vec3d position) {
        this.position = new Vec3d(position);
        cameraDirection = new Vec3d(defaultCameraDirection);
    }
    public AbstractCamera(Vec3d position, Vec3d cameraDirection) {
        this.position = new Vec3d(position);
        this.cameraDirection = new Vec3d(cameraDirection);
    }

    public AbstractCamera(Vec3d position, double viewAngleX, double viewAngleY) {
        this(position);
        this.viewAngleX = viewAngleX;
        this.viewAngleY = viewAngleY;
    }

    public AbstractCamera(Vec3d position, Vec3d cameraDirection, double viewAngleX, double viewAngleY) {
        this(position, viewAngleX, viewAngleY);
        this.cameraDirection = new Vec3d(cameraDirection);
    }

    public AbstractCamera() {
        this(new Vec3d(0, 0, 0));
    }

    protected Vec3d getScreenRotationY(Vec3d vector, double rotation) { // in degrees
        vector = new Vec3d(vector);
        vector.normalize();
        rotation *= PI / 180;
        Vec3d A = new Vec3d(vector);
        Vec3d B = new Vec3d(vector.z, 0, -vector.x);
        B.normalize();
        Vec3d C = new Vec3d();
        C.cross(A, B);

        Vec3d result = new Vec3d();
        C.mul(sin(rotation));
        A.mul(cos(rotation));
        result.add(C);
        result.add(A);

        if (abs(abs(result.y) - 1) < Utils.epsilon) return vector;
        return result;
    }

    protected Vec3d getScreenRotationX(Vec3d vec3d, double rotation) { // in degrees
        return Utils.yRotation(vec3d, rotation);
    }

    public void rotateScreenY(double rotation) { // in degrees
        Vec3d res = getScreenRotationY(cameraDirection, rotation);
        this.cameraDirection = res;
    }

    public void rotateScreenX(double rotation) { // in degrees
        Vec3d res = getScreenRotationX(cameraDirection, rotation);
        this.cameraDirection = res;
    }

    @Override
    public Vec3d getDirection() {
        return new Vec3d(cameraDirection);
    }

    @Override
    public Vec3d getPosition() {
        return new Vec3d(position);
    }

    @Override
    public abstract Vec3d getDirectionVectorFromDisplayCoordinate(int displayWidth, int displayHeight, int x, int y);

    @Override
    public void moveTowards(double amount) {
        Vec3d moveVector = getDirection();
        moveVector.mul(amount);
        move(moveVector);
    }

    protected Vec3d getRightVector() {
        return new Vec3d(cameraDirection.z, 0, -cameraDirection.x);
    }

    protected Vec3d getLeftVector() {
        return new Vec3d(-cameraDirection.z, 0, cameraDirection.x);
    }

    public void moveRight(double amount) {
        Vec3d moveDirection = getRightVector();
        moveDirection.mul(amount);
        move(moveDirection);
    }

    public void moveLeft(double amount) {
        Vec3d moveDirection = getLeftVector();
        moveDirection.mul(amount);
        move(moveDirection);
    }

    @Override
    public void moveBackwards(double amount) {
        moveTowards(-amount);
    }

    @Override
    public void setViewAngle(double xAngle, double yAngle) {
        this.viewAngleX = xAngle;
        this.viewAngleY = yAngle;
    }

    @Override
    public void setPosition(Vec3d position) {
        this.position = new Vec3d(position);
    }

    @Override
    public void setCameraDirection(Vec3d vector) {
        cameraDirection = new Vec3d(vector);
        cameraDirection.normalize();
    }

    @Override
    public double getViewAngleX() {
        return viewAngleX;
    }

    @Override
    public double getViewAngleY() {
        return viewAngleY;
    }

    @Override
    public void move(Vec3d movement) {
        this.position.add(movement);
    }
}
