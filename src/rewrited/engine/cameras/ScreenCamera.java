package rewrited.engine.cameras;

import com.sun.javafx.geom.Vec3d;
import rewrited.Utils.Utils;

import static java.lang.Math.*;

public class ScreenCamera extends AbstractCamera {
    @Override
    public Vec3d getDirectionVectorFromDisplayCoordinate(int displayWidth, int displayHeight, int x, int y) {
        double curX = (x - displayWidth / 2.0);
        double curY = (y - displayHeight / 2.0);
        curY *= (displayWidth * tan(viewAngleY / 180 * PI / 2)) / (displayHeight * tan(viewAngleX / 180 * PI / 2));
        double rotationX = atan2(2 * curX * tan(viewAngleX / 180 * PI / 2), displayWidth) / PI * 180;
        double rotationY;
        if (abs(curX) <= 1e-10) rotationY = atan2(curY, displayWidth / (2 * tan(viewAngleX / 180 * PI / 2))) / PI * 180;//atan2(curY, displayHeight / (2 * tan(viewAngleY / 2 / 180 * PI))) / PI * 180;
        else rotationY = atan2(curY, curX / sin(rotationX / 180 * PI)) / PI * 180;
        Vec3d result = Utils.rotateScreen(cameraDirection, rotationX, rotationY);
        return result;
    }
}
