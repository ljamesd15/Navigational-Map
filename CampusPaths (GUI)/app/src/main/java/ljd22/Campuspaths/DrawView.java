package ljd22.Campuspaths;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.util.List;

import hw8.Point;
import hw8.Location;
import hw5.Connection;

/**
 * This class handles all of the drawings on the main activity.
 * Created by L. James Davidson on 8/13/2017.
 */
public class DrawView extends AppCompatImageView {

    // This class does not represent an ADT. It merely performs actions in the specified locations
    // given by parameters.

    // Fields for where the path is from.
    private boolean showFrom = false;
    private Point from;
    private float radiusForFromCircle;

    // Fields for where the path is to.
    private boolean showTo = false;
    private Point to;
    private float radiusForToCircle;

    // Fields about the paths.
    private List<Connection<Location, Double>> paths;
    private boolean showPaths;
    private float lineThickness;

    // Colour constants
    private int PURPLE = Color.rgb(93, 0, 111);
    private int GREEN = Color.rgb(0, 128, 0);
    private int BLUE = Color.rgb(65, 105, 225);

    // Other constants
    private float DEFAULT_LINE_THICKNESS = 10.f;  // This is the thickness of the line at no zoom.
    private float DEFAULT_CIRCLE_RADIUS = 12.5f; // This is the radius of the circle denoting a
                                                 // specified location at no zoom.

    /**
     * This will bring DrawView into a valid state.
     * @param context is the Context for the DrawView.
     * @requires All parameters must be non-null.
     * @effects Creates a DrawView
     */
    public DrawView(Context context) {
        super(context);
    }

    /**
     * This will bring DrawView into a valid state.
     * @param context is the Context for the DrawView.
     * @param attrs is the set of attributes given to this DrawView.
     * @requires All parameters must be non-null.
     * @effects Creates a DrawView
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * This will bring DrawView into a valid state.
     * @param context is the Context for the DrawView.
     * @param attrs is the set of attributes given to this DrawView.
     * @param defStyle is the same parameter as required by DrawViews super class.
     * @requires All parameters must be non-null.
     * @effects Creates a DrawView
     */
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * This will instruct the view to perform certain actions when instructed to draw.
     * @param canvas is the canvas for the DrawView to draw on.
     * @requires canvas to be non-null.
     * @effects Creates the drawings on the specified canvas.
     * @modifies The appearance of the specified canvas.
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(this.lineThickness);

        if (showPaths) {
            paint.setColor(PURPLE);

            // Paint the paths
            for (Connection<Location, Double> con : paths) {
                canvas.drawLine(((Double) con.getFrom().entrance.getX()).floatValue(),
                        ((Double) con.getFrom().entrance.getY()).floatValue(),
                        ((Double) con.getTo().entrance.getX()).floatValue(),
                        ((Double) con.getTo().entrance.getY()).floatValue(), paint);
            }
        }

        if (showFrom) {
            paint.setColor(GREEN);
            canvas.drawCircle(((Double) from.getX()).floatValue(),
                    ((Double) from.getY()).floatValue(), this.radiusForFromCircle, paint);
        }

        if (showTo) {
            paint.setColor(BLUE);
            canvas.drawCircle(((Double) to.getX()).floatValue(),
                    ((Double) to.getY()).floatValue(), this.radiusForToCircle, paint);
        }
    }

    /**
     * Draws a circle on the point where the path is coming from.
     * @param point is the point object denoting where the path is coming from.
     * @param zoom is the amount of zoom being applied to the image.
     * @requires point and zoom must be non-null.
     * @effects Highlights where the path begins at.
     * @modifies The appearance of the canvas which the starting point will be highlighted.
     */
    protected void selectFrom(Point point, float zoom) {
        this.from = point;
        this.showFrom = true;
        this.radiusForFromCircle = DEFAULT_CIRCLE_RADIUS / zoom;
    }

    /**
     * Removes the circle from the point denoting where the path begins at.
     * @effects Removes the circle denoting the beginning of the path.
     * @modifies The appearance of the canvas which the circle was drawn.
     */
    protected void deselectFrom() {
        this.showFrom = false;
    }

    /**
     * Draws a circle on the point where the path is going to.
     * @param point is the point object denoting where the path is going to.
     * @param zoom is the amount of zoom being applied to the image.
     * @requires point and zoom must be non0null.
     * @effects Draws a circle on the point representing the the path destination.
     * @modifies The canvas where the circle is being drawn.
     */
    protected void selectTo(Point point, float zoom) {
        this.to = point;
        this.showTo = true;
        this.radiusForToCircle = DEFAULT_CIRCLE_RADIUS / zoom;
    }

    /**
     * Removes the circle from the point denoting the path destination.
     * @effects Removes the circle from the path destination.
     * @modifies The appearance of the canvas which the destination was highlighted.
     */
    protected void deselectTo() {
        this.showTo = false;
    }

    /**
     * Highlights the paths from the start and to the destination.
     * @param paths is the set of connection denoting the paths from the start and to the end.
     * @param zoom is the amount of zoom being applied to the image.
     * @requires paths and all of its contents must be non-null. zoom must be non-null.
     * @effects Highlights the given path.
     * @modifies The appearance of the canvas which the paths will be highlighted.
     */
    protected void highlightPath(List<Connection<Location, Double>> paths, float zoom) {
        this.paths = paths;
        this.showPaths = true;
        this.lineThickness = DEFAULT_LINE_THICKNESS / zoom;
    }

    /**
     * Removes the highlighting of the given paths.
     * @effects Removes the highlighting of the paths.
     * @modifies The appearance of the canvas which the paths are on.
     */
    protected void deselectPath() {
        this.showPaths = false;
    }

    /**
     * Determines whether or not paths are currently being displayed right now.
     * @return True is paths are currently being displayed.
     */
    protected boolean arePathsDisplayed() {
        return this.showPaths;
    }
}
