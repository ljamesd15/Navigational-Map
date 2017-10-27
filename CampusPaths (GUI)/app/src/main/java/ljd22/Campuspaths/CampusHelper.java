package ljd22.Campuspaths;

import android.media.MediaPlayer;

import hw8.*;
import hw5.*;
import java.util.List;

/**
 * This class contains all the methods relating to zooming the map.
 * Created by L. James Davidson on 8/14/2017.
 */

class CampusHelper {

    // Padding for the sides of coordinates so that te lie visibly within the map view.
    private static final float PADDING = 100.f;

    private static final float MAX_ZOOM = 5.f;

    // Constraint sizes of the image.
    private static final float LAYOUT_HEIGHT = 700.f;
    private static final float LAYOUT_WIDTH = 1100.f;

    // Other
    private static MediaPlayer player;
    private static final double AVG_WALKING_SPEED = 297;
    // Feet per minute according to google for younger pedestrians.

    /**
     * Determines the point at which the map image should be scaled at. Also determines the scaling
     * factor to adequately zoom in on the path between two different destinations. This scale
     * factor needs to be applied to a version of the image which has not been zoomed in.
     * @param paths is the list of connections representing the paths from a start point to a
     *              destination.
     * @param initialX is the initial (before scaled by this scale factor provided) x coordinate of
     *                 the bottom right hand corner.
     * @param initialY is the initial (before scaled by this scale factor provided) y coordinate of
     *                 the bottom right hand corner.
     * @requires The list of connections and all the contents within are non-null. initialX and
     * initialY must both be non-null and positive floats.
     * @return An array of float values necessary to zoom into the correct area. The first float
     * value is the x coordinate to zoom in on, the second is the y coordinate to zoom in on, the
     * third is the scale factor which should be applied to both x and y.
     */
    public static float[] determineScaleFactor(List<Connection<Location, Double>> paths,
                                             float initialX, float initialY) {
        float[] solution = new float[3];
        // Get first connection and initialize the values.
        Connection<Location, Double> firstCon = paths.get(0);

        float minX = ((Double) (firstCon.getFrom().entrance.getX())).floatValue();
        float maxX = minX;
        float minY = ((Double) (firstCon.getFrom().entrance.getY())).floatValue();
        float maxY = minY;

        // Go through every point and determine the min and max values of x and y.
        // Only need to examine where the connection is to because what the connection is to the
        // next connection will have the xact same point as the connection from. We initialized
        // with the first from so all locations will be checked through this for each loop.
        for (Connection<Location, Double> con : paths) {
            float currX = ((Double) (con.getTo().entrance.getX())).floatValue();
            float currY = ((Double) (con.getTo().entrance.getY())).floatValue();

            if (minX > currX) {
                minX = currX;
            } else if (maxX < currX) {
                maxX = currX;
            }

            if (minY > currY) {
                minY = currY;
            } else if (maxY < currY) {
                maxY = currY;
            }
        }

        // We now have the max and min values of the x and y points so lets find the range of the
        // x and y coordinates.
        float sizeOfX = maxX - minX;
        float sizeOfY = maxY - minY;

        // Find the focal points of the zoom.
        float pivotXCoord = minX + (sizeOfX / 2);
        float pivotYCoord = minY + (sizeOfY / 2);

        // Find the factors to which we will scale x and y.
        float scaleX = initialX / sizeOfX;
        float scaleY = initialY / sizeOfY;

        // Use helper methods to determine the most appropriate zoom level so that all coordinates
        // of the path are displayed and that the zoom isn't too high.
        float scaleForXAndY = findMinScaleSize(scaleX, scaleY);
        scaleForXAndY = zoomToFitCoord(minX, pivotXCoord, scaleForXAndY, 'x');
        scaleForXAndY = zoomToFitCoord(maxX, pivotXCoord, scaleForXAndY, 'x');
        scaleForXAndY = zoomToFitCoord(minY, pivotYCoord, scaleForXAndY, 'y');
        scaleForXAndY = zoomToFitCoord(maxY, pivotYCoord, scaleForXAndY, 'y');

        // Write data into array and return it.
        solution[0] = pivotXCoord;
        solution[1] = pivotYCoord;
        solution[2] = scaleForXAndY;
        return solution;
    }

    /**
     * Finds the minimum scale size which will be applied to both x and y from the two different
     * scale sizes for x and y.
     * @param scaleX is the scale size for the x coordinates
     * @param scaleY is the scale size for the y coordinates
     * @requires scaleX and scaleY are both non-null.
     * @return The scale size that should be applied to both x and y coordinates.
     */
    private static float findMinScaleSize(float scaleX, float scaleY) {

        float scaleForXAndY;

        // We want to have the same scale factor for x and y so that the image of the map does
        // not get stretched. So I am choosing the smallest scaling factor. Since the scale of 1.f
        // does nothing we will subtract by 1 and find the absolute value of both scale factors.
        // From here we can determine which scale factor is truly the smallest.
        float tempScalex = Math.abs(scaleX - 1.f);
        float tempScaleY = Math.abs(scaleY - 1.f);
        float tempScale = Math.min(tempScalex, tempScaleY);

        // If the x scale factor is chosen we want the scale factor to be equal to scaleX
        // otherwise we want it to be scaleY. If the scale factors are equivalent it does not
        // matter which scale factor we choose.
        if (((Float) tempScale).equals((Float) tempScalex)) {
            scaleForXAndY = scaleX;
        } else { // tempScale equals tempScaleY
            scaleForXAndY = scaleY;
        }

        // Now we also don't want to zoom in too far into the map so the scale factor returned must
        // be less than or equal to MAX_ZOOM.
        if (scaleForXAndY > MAX_ZOOM) {
            scaleForXAndY = MAX_ZOOM;
        }

        return scaleForXAndY;
    }

    /**
     * Determines the appropriate zoom level so that the specified coordinate will be displayed.
     * @param locCoord is the coord that will be displayed with the returned zoom level.
     * @param pivotCoord is the coord for which the zoom will be applied at.
     * @param zoom is the level of zoom that one wishes to see if it will work.
     * @param coordType is the type of coordinate (either character x for an x coordinate or y
     *                  for y coordinate).
     * @requires All parameters must be non-null. locCoord and pivotCoord must be positive floats.
     * coordType must be either the character 'x' or the character 'y'.
     * @return The appropriate zoom level so that locCoord will be displayed.
     */
    private static float zoomToFitCoord(float locCoord, float pivotCoord, float zoom, char coordType) {

        float displayConstraint;

        // Find the difference between the coord we want to display and the point at which we are
        // zooming in on.
        float distance = Math.abs(locCoord - pivotCoord);

        // The distance once zoomed is the distance before zoomed times the multiple by which the
        // image was zoomed.
        //float zoomedDistance = distance * zoom;

        // Find out what the display constraint is. Dividing by 2 because the locCoord will only
        // have half the constraint distance from the center.
        if (coordType == 'x') {
            displayConstraint = LAYOUT_WIDTH / 2;
        } else { // coordType == 'y'
            displayConstraint = LAYOUT_HEIGHT / 2;
        }

        // If the point will not be displayed under the current zoom then change make zoom the
        // maximum zoom so that the coord will be displayed.
        if (zoom > displayConstraint / (distance + PADDING)) {
            zoom = displayConstraint / (distance + PADDING);
        }

        return zoom;
    }

    /**
     * Plays the opening sound for the application specified in the parameter.
     * @param app is the application that wishes to play the opening sound.
     */
    public static void playSound(CampusPathsMainActivity app) {
        int raw_id = R.raw.msxpstartup;
        player = MediaPlayer.create(app.getApplicationContext(), raw_id );
        player.setLooping(false);
        player.setVolume(200.f, 200.f);

        player.start();
    }

    /**
     * This will return the average time it takes to walk this path.
     * @param paths is the set of connections representing the paths to the destination.
     * @requires paths and all of its contents are non-null.
     * @returns An integer represetnting the number of minutes it would take the average young
     * person to walk this distance.
     */
    public static int printAvgTime(List<Connection<Location, Double>> paths) {
        // Find the total length of the path.
        Double total = paths.get(paths.size() - 1).getLabel();

        // This needs to be multiplied by the scale factor as well since the connection labels
        // were scaled during parsing.
        Integer time = (int) Math.ceil(total / (AVG_WALKING_SPEED * ParseCampusData.SCALE_FACTOR));

        return time;
    }
}
