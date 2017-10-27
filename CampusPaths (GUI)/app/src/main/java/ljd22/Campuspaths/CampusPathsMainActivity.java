package ljd22.Campuspaths;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;

import hw8.*;
import hw5.*;

/**
 * This class defines the functionality of the main activity page of the campus paths
 * application.
 *
 * Created by L. James Davidson on 8/13/2017.
 */
public class CampusPathsMainActivity extends AppCompatActivity {

    // buttons, listviews, and other GUI items
    private Button findPathButton;
    private Button resetButton;
    private DrawView view;
    private ListView fromBox;
    private ListView toBox;
    private ScaleGestureDetector sgd;

    // Data about the campus
    private Campus UofW;

    // Path finding info
    private Location from;
    private Location to;

    // Other
    private float[] scaleInfo = new float[4];
    private float scale;

    // Constants
    private float MIN_IMAGE_HEIGHT = 700.f;
    private float MIN_IMAGE_WIDTH = 1100.f;

    @Override
    /**
     * This contains all the commands which this application should do when initially started up.
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Play the opening music.
        CampusHelper.playSound(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_paths_main);

        // Process the data
        InputStream pathsInputStream = this.getResources().openRawResource(R.raw.campus_paths);
        InputStream buildingsInputStream = this.getResources()
                .openRawResource(R.raw.campus_buildings);
        this.instantiate(buildingsInputStream, pathsInputStream);

        // Setting up the initial values of the scaling factors.
        scaleInfo[0] = 0.f; // Pivot x point
        scaleInfo[1] = 0.f; // Pivot y point
        scaleInfo[2] = 1.f; // Scale in x direction.
        scaleInfo[3] = 1.f; // Scale in y direction.
        scale = 1.f;

        // Create the items on the main activity
        view = (DrawView) findViewById(R.id.imageView);
        fromBox = (ListView) findViewById(R.id.FromBox);
        toBox = (ListView) findViewById(R.id.ToBox);
        findPathButton = (Button) findViewById(R.id.FindPathButton);
        resetButton = (Button) findViewById(R.id.ResetButton);
        sgd = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());

        // Create adapter and bring in the data to the from and to boxes.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());

        Set<Location> buildings = new TreeSet<Location>(UofW.getBuildings());
        for (Location loc : buildings) {
            adapter.add(loc.shortName);
        }
        fromBox.setAdapter(adapter);
        toBox.setAdapter(adapter);

        // Set the onClickListeners
        fromBox.setOnItemClickListener(fromBoxItemClick);
        toBox.setOnItemClickListener(toBoxItemClick);
        findPathButton.setOnClickListener(findPathButtonCLick);
        resetButton.setOnClickListener(resetButtonClick);

        // Setting up the choice modes and what to do when selected.
        fromBox.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        toBox.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // onCreate gets called when switching from portrait to landscape and vice verse. There
        // is a zooming bug when the displays switch so this is fixed by calling the reset button
        // click after starting everything.
        resetButtonClick.onClick(view);
    }

    /**
     * This will bring the model into a valid state by preparing all the data for this application.
     * @param buildingsFile is the inputStream connected to the data regarding all of the buildings
     *                      on campus.
     * @param pathFile is the inputStream connected to all the data regarding the paths around
     *                 campus.
     * @requires buildingsFile, and pathFile ar both non-null and the files which they are
     * connected to are non-null and have properly formatted data otherwise the application will
     * close.
     */
    private void instantiate(InputStream buildingsFile, InputStream pathFile) {
        try {
            UofW = ParseCampusData.loadInfo(buildingsFile, pathFile);
        } catch (Exception e) {
            // Can't do anything if data is corrupted. Just exit
            System.exit(1);
        }
    }

    /**
     * This contains all the functionality of a click performed on the findPath button.
     */
    private View.OnClickListener findPathButtonCLick = new View.OnClickListener() {
        public void onClick(View v) {
            if (from == null) {
                Toast.makeText(getApplicationContext(), "Please select a starting point",
                        Toast.LENGTH_LONG).show();
            } else if (to == null) {
                Toast.makeText(getApplicationContext(), "Please select a ending point",
                        Toast.LENGTH_LONG).show();
            } else {
                List<String> directions = new ArrayList<String>();
                List<Connection<Location, Double>> paths =
                        UofW.shortestRouteFromTo(from, to, directions);

                // Get scale factor to scale image with.
                scaleInfo = CampusHelper.determineScaleFactor(paths,
                        MIN_IMAGE_WIDTH, MIN_IMAGE_HEIGHT);

                view.setPivotX(scaleInfo[0]);
                view.setPivotY(scaleInfo[1]);
                float zoom = scaleInfo[2];
                view.setScaleX(zoom);
                view.setScaleY(zoom);

                // Since we are zoomed in want to redraw the radius of the circle to be 10.f like
                // it was before the image was zoomed in on.
                view.selectFrom(from.entrance, zoom);
                view.selectTo(to.entrance, zoom);
                view.highlightPath(paths, zoom);

                // Print the average time to get there.
                Integer time = CampusHelper.printAvgTime(paths);

                // Print the time so the user can know how long it will take.
                if (time < 1) {
                    Toast.makeText(getApplicationContext(), "It less than a minute to arrive at "
                                    + to.longName, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "It will take around " + time.toString()
                            + " minutes to arrive at " + to.longName, Toast.LENGTH_LONG).show();
                }

                view.invalidate();
            }
        }
    };

    /**
     * This contains all the functionality of a click on the reset button.
     */
    private View.OnClickListener resetButtonClick = new View.OnClickListener() {
        public void onClick(View v) {
            // Reset all values and deselect everything.
            from = null;
            to = null;
            view.deselectFrom();
            view.deselectTo();
            view.deselectPath();

            // Undo any scaling done.
            view.setPivotX(scaleInfo[0]);
            view.setPivotY(scaleInfo[1]);
            view.setScaleX(1.f);
            view.setScaleY(1.f);

            view.invalidate();
        }
    };

    /**
     * This contains all the functionality of a click performed on the fromBox
     */
    private ListView.OnItemClickListener fromBoxItemClick = new ListView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

            // Getting the location of the point 'from'
            Location tempFrom = UofW.getBuilding((String) fromBox.getItemAtPosition(position));

            // If paths are displayed then reset the image.
            if (view.arePathsDisplayed()) {
                resetButtonClick.onClick(view);
            }

            // If the user clicks on the same building again the deselect it otherwise highlight
            // the new point.
            if (tempFrom.equals(from)) {
                from = null;
                view.deselectFrom();
            } else if (tempFrom.equals(to)) {
                Toast.makeText(getApplicationContext(), "Cannot pick same start and end points.",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                from = tempFrom;
                view.selectFrom(from.entrance, 1.f);
                // 1.f means a zoom of 1 which is no zoom.
            }

            // Deselect any shown paths, request an update on the drawings.
            view.deselectPath();
            view.invalidate();
            Toast.makeText(getApplicationContext(), tempFrom.longName, Toast.LENGTH_LONG).show();
        }
    };

    /**
     * This is the contains all the functionality of a click on the toBox.
     */
    private ListView.OnItemClickListener toBoxItemClick = new ListView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

            // Getting the location of the point 'to'
            Location tempTo = UofW.getBuilding((String) toBox.getItemAtPosition(position));

            // If paths are displayed then reset the image.
            if (view.arePathsDisplayed()) {
                resetButtonClick.onClick(view);
            }

            // If the user clicks on the same building again the deselect it otherwise highlight
            // the new point.
            if (tempTo.equals(to)) {
                to = null;
                view.deselectTo();
            } else if (tempTo.equals(from)) {
                Toast.makeText(getApplicationContext(), "Cannot pick same start and end points.",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                to = tempTo;
                view.selectTo(to.entrance, 1.f);
                // 1.f means a zoom of 1 which is no zoom.
            }

            // Deselect any shown paths, request an update on the drawings.
            view.deselectPath();
            view.invalidate();
            Toast.makeText(getApplicationContext(), tempTo.longName, Toast.LENGTH_LONG).show();
        }
    };

    /**
     * This class contaisn all the functionality of when a pinch to zoom gesture is called on the
     * map.
     */
   private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        /**
         * This method overrides the previous onScale method. It zoom the application should do in
         * the event of a pinching gesture.
         */
        public boolean onScale(ScaleGestureDetector detector) {

            // Always zooms into the center of the image.
            view.setPivotX(MIN_IMAGE_WIDTH / 2);
            view.setPivotY(MIN_IMAGE_HEIGHT / 2);

            scale *= detector.getScaleFactor();

            // This prevents the map from becoming too small.
            float height = view.getMeasuredHeight();
            float width = view.getMeasuredWidth();
            if (height * scale < MIN_IMAGE_HEIGHT) {
                scale = MIN_IMAGE_HEIGHT / height;
            }
            if (width * scale < MIN_IMAGE_WIDTH) {
                scale = MIN_IMAGE_WIDTH / width;
            }

            view.setScaleX(scale);
            view.setScaleY(scale);

            return super.onScale(detector);
        }
    }

    @Override
    /**
     * This tells the application what to do in the case where there is a touch event.
     */
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


}
