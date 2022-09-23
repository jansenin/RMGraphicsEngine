package rewrited.engine.rendering.processors.raymarchingProcessors;

import com.sun.javafx.geom.Vec3d;
import rewrited.engine.rendering.Raymarcher;

public class BlackHoleProcessor implements RaymarchingProcessor {
    Vec3d blackHolePos = new Vec3d(0, 0, 4);
    double maxStepLength = 1;
    double power = 1;

    private Vec3d getAffectedVector(Vec3d point, Vec3d direction) {
        Vec3d holeDistance = new Vec3d(blackHolePos);
        holeDistance.sub(point);
        Vec3d holeDirection = new Vec3d(blackHolePos);
        holeDirection.sub(point);
        holeDirection.normalize();
        holeDirection.mul(power / Math.pow(holeDistance.length(), 2));
        direction = new Vec3d(direction);
        direction.add(holeDirection);
        direction.normalize();
        return direction;
    }

    @Override
    public void processDataForStep(Raymarcher.RaymarchStep.StartingData raymarchStartingData, Raymarcher.RaymarchParameters rp, Raymarcher.RaymarchResult result) {
        raymarchStartingData.direction = getAffectedVector(raymarchStartingData.startPoint, raymarchStartingData.direction);

    }

    @Override
    public void processStep(Raymarcher.RaymarchStep raymarchStep, Raymarcher.RaymarchParameters rp, Raymarcher.RaymarchResult result) {
        if (raymarchStep.step.length() > maxStepLength) {
            raymarchStep.step.normalize();
            raymarchStep.step.mul(maxStepLength);
        }
        raymarchStep.endPoint = new Vec3d(raymarchStep.startPoint);
        raymarchStep.endPoint.add(raymarchStep.step);
    }

    @Override
    public void processResult(Raymarcher.RaymarchResult raymarchResult, Raymarcher.RaymarchParameters rp) { }
}
