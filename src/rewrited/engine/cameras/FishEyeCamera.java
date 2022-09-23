package rewrited.engine.cameras;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class FishEyeCamera extends AbstractCamera {
    @Override
    public Vec3d getDirectionVectorFromDisplayCoordinate(int displayWidth, int displayHeight, int x, int y) {
        Vec3d result = Utils.rotateScreen(cameraDirection, (1.0 * x / displayWidth - 1.0 / 2) * viewAngleX, (1.0 * y / displayHeight - 1.0 / 2) * viewAngleY);
        return result;
    }
}
