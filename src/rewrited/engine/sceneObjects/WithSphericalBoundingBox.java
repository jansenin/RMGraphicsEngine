package rewrited.engine.sceneObjects;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class WithSphericalBoundingBox extends FigureBase implements WithDefinedBoundingBox {
    Figure figure;
    double radius;

    public WithSphericalBoundingBox(Figure figure, double radius) {
        super(figure);
        this.figure = figure;
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean isInBoundingBox(Vec3d vector) {
        Vec3d v = new Vec3d(vector);
        v.sub(getPosition());
        return v.length() < radius;
    }
}
