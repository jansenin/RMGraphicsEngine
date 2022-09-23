package rewrited.engine.sceneObjects.figureCombiners;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.figures.FigureBase;

import java.awt.*;
import java.util.function.Function;

public class SmoothMinCombiner extends FigureBase {
    private double k;

    public SmoothMinCombiner(Figure figure1, Figure figure2, double k, Function<Vec3d, Color> colorFunction) {
        this.k = k;
        setDistanceAndColorFunctions(p -> opSmoothUnion(figure1.getDistance(p), figure2.getDistance(p), this.k), colorFunction);
    }

    private static double opSmoothUnion(double d1, double d2, double k ) {
        double h = Utils.clamp( 0.5 + 0.5*(d2-d1)/k, 0.0, 1.0 );
        return Utils.mix( d2, d1, h ) - k*h*(1.0-h);
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }
}
