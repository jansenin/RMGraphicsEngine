package rewrited.engine.sceneObjects;

        import com.sun.javafx.geom.Vec3d;

public interface WithDefinedBoundingBox {
    boolean isInBoundingBox(Vec3d vector);
}
