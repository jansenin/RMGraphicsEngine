package rewrited.engine.sceneObjects.figureCombiners;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.sceneObjects.figures.Figure;
import rewrited.engine.sceneObjects.WithDefinedBoundingBox;
import rewrited.engine.sceneObjects.figures.FigureBase;

public class InfiniteReplicator extends FigureBase implements WithDefinedBoundingBox {
    double modulo;

    public InfiniteReplicator(Figure figure, double modulo) {
        this.modulo = modulo;
        setDistanceAndColorFunctions(
                (point) -> figure.getDistance(Utils.getModdedPosition(point, modulo)),
                (point) -> figure.getColor(Utils.getModdedPosition(point, modulo))
        );
    }

    @Override
    public boolean isInBoundingBox(Vec3d vector) {
        return true;
    }
}