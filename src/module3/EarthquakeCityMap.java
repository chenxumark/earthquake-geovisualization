package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	// radius of moderate earthquake 
	public static final float RADIUS_MODERATE = 14;
	// radius of light earthquake
	public static final float RADIUS_LIGHT = 8;
	// radius of minor earthquake
	public static final float RADIUS_MINOR = 4;
	
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    //TODO: Add code here as appropriate
	    addEarthQuakeMarkers(earthquakes,markers);
	    map.addMarkers(markers);
	}
	private void addEarthQuakeMarkers(List<PointFeature> earthquakes, List<Marker> markers){
		PointFeature f;
		if (earthquakes.size() > 0) {
			for (int i=0; i<earthquakes.size(); i++){
				f = earthquakes.get(i);
				SimplePointMarker marker = createMarker(f);
				
				markers.add(marker);
			}
	    }
		return;
	}	
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		styleMarker(marker, feature);
		return marker;
	}
	private void styleMarker(SimplePointMarker marker, PointFeature f){
    	Object magObj = f.getProperty("magnitude");
    	float mag = Float.parseFloat(magObj.toString());
    	
    	if (mag < THRESHOLD_LIGHT) {
    		marker.setRadius(RADIUS_MINOR);
    		marker.setColor(color(0, 0, 255));
    	}
    	else if (mag < THRESHOLD_MODERATE){
    		marker.setRadius(RADIUS_LIGHT);
    		marker.setColor(color(255, 0, 255));
    	}
    	else{
    		marker.setRadius(RADIUS_MODERATE);
    		marker.setColor(color(255, 0, 0));
    	}
    }
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey() 
	{	
		// Remember you can use Processing's graphics methods here
		fill(255,255,255);
		rect(25,60,150,250);
		fill(0, 102, 153);
		text("Earthquake Key", 50, 75);
		
		fill(255, 0, 0);
		ellipse(35, 100, 10, 10);
		fill(0, 0, 0);
		text("5.0+", 50, 100);
		
		fill(255, 0, 255);
		ellipse(35, 130, 6, 6);
		fill(0, 0, 0);
		text("4.0+", 50, 130);
		
		fill(0, 0, 255);
		ellipse(35, 160, 4, 4);
		fill(0, 0, 0);
		text("below 4.0", 50, 160);
		
	}
}
