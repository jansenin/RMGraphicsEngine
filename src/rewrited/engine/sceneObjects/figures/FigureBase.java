package rewrited.engine.sceneObjects.figures;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.rendering.processors.colorProcessors.processorData.LightProcessorData;
import rewrited.engine.sceneObjects.WithSphericalBoundingBox;
import rewrited.engine.sceneObjects.figureCombiners.*;

import java.awt.*;
import java.util.function.Function;

// firstly applied rotation, then scale, then positioning
// firstly applied xRotation, then yRotation, then zRotation
public class FigureBase implements Figure, LightProcessorData {
    private Vec3d position = new Vec3d(0, 0, 0);
    private double scale = 1;

    // in degrees
    private double xRotation = 0;
    private double yRotation = 0;
    private double zRotation = 0;

    private Function<Vec3d, Double> distanceFunction;
    private Function<Vec3d, Color> colorFunction;

    private double diffuseKoefficient = 0.7;
    private double specularKoefficient = 1;
    private double specularity = 64;
    private double ambientKoefficient = 0.2;

    // public constructors

    public FigureBase(Figure figure) {
        this(figure::getDistance, figure::getColor);
    }

    public FigureBase(Function<Vec3d, Double> distanceFunction, Function<Vec3d, Color> colorFunction) {
        this.distanceFunction = distanceFunction;
        this.colorFunction = colorFunction;
    }

    public FigureBase(Vec3d position, Function<Vec3d, Double> distanceFunction, Function<Vec3d, Color> colorFunction) {
        this(distanceFunction, colorFunction);
        this.position = position;
    }

    public FigureBase(Vec3d position, double scale, Function<Vec3d, Double> distanceFunction, Function<Vec3d, Color> colorFunction) {
        this(position, distanceFunction, colorFunction);
        this.scale = scale;
    }

    public FigureBase(Vec3d position, double scale, double xRotation, double yRotation, double zRotation, Function<Vec3d, Double> distanceFunction, Function<Vec3d, Color> colorFunction) {
        this(position, scale, distanceFunction, colorFunction);
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
    }

    // public constructors

    protected FigureBase() { }

    protected FigureBase(Vec3d position) {
        this.position = position;
    }

    protected FigureBase(Vec3d position, double scale) {
        this(position);
        this.scale = scale;
    }

    protected FigureBase(Vec3d position, double scale, double xRotation, double yRotation, double zRotation) {
        this(position, scale);
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
    }

    // methods

    protected Vec3d getUntransformedVector(Vec3d vector) {
        vector = new Vec3d(vector);
        vector.sub(position);
        vector.mul(1 / scale);
        return Utils.zRotation(Utils.yRotation(Utils.xRotation(vector, -xRotation), -yRotation), -zRotation);
    }

    protected Vec3d getTransformedVector(Vec3d vector) {
        vector = new Vec3d(vector);
        vector = Utils.xRotation(Utils.yRotation(Utils.zRotation(vector, zRotation), yRotation), xRotation);
        vector.mul(scale);
        vector.add(position);
        return vector;
    }

    protected Vec3d calculateNormal(Function<Vec3d, Vec3d> normalFunction, Vec3d point) {
        point = getUntransformedVector(point);

        Vec3d result = normalFunction.apply(point);

        result = getTransformedVector(result);
        result.sub(getPosition());
        result.normalize();
        return result;
    }

    protected boolean calculateIsInBoundingBox(Function<Vec3d, Boolean> boundingBoxFunction, Vec3d vector) {
        vector = getUntransformedVector(vector);
        return boundingBoxFunction.apply(vector);
    }

    protected void setDistanceAndColorFunctions(Function<Vec3d, Double> distanceFunction, Function<Vec3d, Color> colorFunction) {
        this.distanceFunction = distanceFunction;
        this.colorFunction = colorFunction;
    }

    @Override
    public double getDistance(Vec3d point) {
        return distanceFunction.apply(getUntransformedVector(point)) * scale;
    }

    @Override
    public Color getColor(Vec3d point) {
        return colorFunction.apply(getUntransformedVector(point));
    }

    @Override
    public FigureBase move(Vec3d movement) {
        position.add(movement);
        return this;
    }

    @Override
    public Vec3d getPosition() {
        return new Vec3d(position);
    }

    @Override
    public FigureBase setPosition(Vec3d position) {
        this.position = new Vec3d(position);
        return this;
    }

    @Override
    public double getScale() {
        return scale;
    }

    @Override
    public FigureBase setScale(double scale) {
        this.scale = scale;
        return this;
    }

    @Override
    public double getxRotation() {
        return xRotation;
    }

    @Override
    public FigureBase setxRotation(double xRotation) {
        this.xRotation = xRotation;
        return this;
    }

    @Override
    public double getyRotation() {
        return yRotation;
    }

    @Override
    public FigureBase setyRotation(double yRotation) {
        this.yRotation = yRotation;
        return this;
    }

    @Override
    public double getzRotation() {
        return zRotation;
    }

    @Override
    public FigureBase setzRotation(double zRotation) {
        this.zRotation = zRotation;
        return this;
    }

    @Override
    public FigureBase setRotation(double xRotation, double yRotation, double zRotation) {
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
        return this;
    }

    @Override
    public FigureBase rotate(double xRotation, double yRotation, double zRotation) {
        this.xRotation += xRotation;
        this.yRotation += yRotation;
        this.zRotation += zRotation;
        return this;
    }

    @Override
    public double getDiffuseKoefficient() {
        return diffuseKoefficient;
    }

    @Override
    public double getSpecularKoefficient() {
        return specularKoefficient;
    }

    @Override
    public double getSpecularity() {
        return specularity;
    }

    @Override
    public double getAmbientKoefficient() {
        return ambientKoefficient;
    }

    public FigureBase setLightData(double diffuseKoefficient, double specularKoefficient, double specularity, double ambientKoefficient) {
        this.diffuseKoefficient = diffuseKoefficient;
        this.specularKoefficient = specularKoefficient;
        this.specularity = specularity;
        this.ambientKoefficient = ambientKoefficient;
        return this;
    }

    public FigureBase infinitelyReplicated(double mod) {
        return new InfiniteReplicator(this, mod);
    }

    public FigureBase inversed() {
        return new InversedFigure(this);
    }

    public FigureBase shell(double shellThickness) {
        return new Shell(this, shellThickness);
    }

    public FigureBase shelled(double shellThickness) {
        return new ShelledFigure(this, shellThickness);
    }

    public FigureBase add(Figure figure) {
        return new MinCombner(this, figure);
    }

    public FigureBase substract(Figure figure) {
        return new MaxCombiner(this, new InversedFigure(figure));
    }

    public FigureBase intersect(Figure figure) {
        return new MaxCombiner(this, figure);
    }

    public FigureBase smoothAdd(Figure figure, double smoothness, Function<Vec3d, Color> colorFunction) {
        return new SmoothMinCombiner(this, figure, smoothness, colorFunction);
    }

    public FigureBase smoothSubstract(Figure figure, double smoothness, Function<Vec3d, Color> colorFunction) {
        return new SmoothMaxCombiner(this, new InversedFigure(figure), smoothness, colorFunction);
    }

    public FigureBase smoothIntersect(Figure figure, double smoothness, Function<Vec3d, Color> colorFunction) {
        return new SmoothMaxCombiner(this, figure, smoothness, colorFunction);
    }

    public FigureBase withSphericalBoundingBox(double radius) {
        return new WithSphericalBoundingBox(this, radius);
    }
}
