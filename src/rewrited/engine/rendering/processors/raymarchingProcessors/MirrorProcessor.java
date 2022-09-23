package rewrited.engine.rendering.processors.raymarchingProcessors;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;
import rewrited.engine.rendering.Raymarcher;
import rewrited.engine.rendering.processors.raymarchingProcessors.processorData.Mirror;
import rewrited.engine.sceneObjects.WithDefinedNormal;
import rewrited.engine.sceneObjects.figures.Figure;

public class MirrorProcessor implements RaymarchingProcessor {
    @Override
    public void processDataForStep(Raymarcher.RaymarchStep.StartingData raymarchStartingData, Raymarcher.RaymarchParameters rp, Raymarcher.RaymarchResult result) {

    }

    @Override
    public void processStep(Raymarcher.RaymarchStep raymarchStep, Raymarcher.RaymarchParameters rp, Raymarcher.RaymarchResult result) {
        Vec3d nextPos = raymarchStep.startPoint;
        nextPos.add(raymarchStep.step);
        Figure nextPosClosestFigure = rp.getScene().getMinDistanceFigure(nextPos);
        if (nextPosClosestFigure.getDistance(nextPos) < rp.getMinDistance()) {
            if (raymarchStep.closestFigure instanceof Mirror) {
                Vec3d normal;
                Vec3d point = raymarchStep.startPoint;
                if (raymarchStep.closestFigure instanceof WithDefinedNormal)
                    normal = ((WithDefinedNormal) raymarchStep.closestFigure).getNormal(point);
                else
                    normal = Utils.getNormal(raymarchStep.closestFigure, point);
                Vec3d A = new Vec3d(raymarchStep.step);
                Vec3d reflection = new Vec3d(A);
                normal.mul(2 * -A.dot(normal));
                reflection.add(normal);
                reflection.normalize();
                raymarchStep.normalizedDirection = new Vec3d(reflection);
                raymarchStep.endPoint = new Vec3d(raymarchStep.startPoint);
                reflection.mul(raymarchStep.minDistanceToFigure);
                raymarchStep.endPoint.add(reflection);
                raymarchStep.step = reflection;
            }
        }
    }

    @Override
    public void processResult(Raymarcher.RaymarchResult raymarchResult, Raymarcher.RaymarchParameters rp) {

    }
}
