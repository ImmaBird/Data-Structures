package train;

import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author
 */
public class TrainRoute extends HBox{
	
	private int distanceBetweenStations;
	private PrintWriter log;
	private Station[] stations;
	private ArrayList<TrainOnTrack> trains = new ArrayList<TrainOnTrack>();
	
	/**
	 * Initializes the private variables that it gets from the simulation.
	 * 
	 * @param stations
	 * @param distanceBetweenStations
	 * @param log
	 */
	TrainRoute(Station[] stations,int distanceBetweenStations,PrintWriter log) {
		this.distanceBetweenStations = distanceBetweenStations;
		this.log = log;
		this.stations = stations;
		
		setPadding(new Insets(10));
	}
	
	/**
	 * Simulates the next tick. This entails checking for arriving and departing trains.
	 * 
	 * @param log
	 */
	public void nextTick() {
		
		// Checks all the stations for trains that are ready to leave
		for(Station station : stations) {
			// If the station has a train thats ready put it on the track
			while(station.hasReadyTrain()) {
				// Puts the train in a holding class to help count down its time here
				Train train = station.getTrain();
				TrainOnTrack trainOnTrack = new TrainOnTrack(train,distanceBetweenStations+1);
				trains.add(trainOnTrack);
				getChildren().add(trainOnTrack);
				// Mentions this in the log
				log.println("Train " + train.getName() + " Departing from station " + station.getStationNumber() + ".");
			}
		}
		
		// Checks all the trains on the track if they are ready to enter a station
		for(int i = 0 ; i < trains.size() ; i++) {
			// When the train is ready it is placed in it's destination station
			if(trains.get(i).isReady()) {
				// Gets a reference to the train inside its wrapper class
				Train train = trains.get(i).getTrain();
				// Removes the trains from the GUI
				getChildren().remove(trains.get(i));
				// Removes this train from the list
				trains.remove(i);
				i--;
				// Makes note of its arrival in the log
				log.println("Train " + train.getName() + " Arriving at station " + train.getDestination() + ".");
				// Puts the train in the appropriate station
				stations[train.getDestination()].acceptTrain(train);
				// Changes the trains destination and current station
				train.newDestination();
			}
		}
		
	}
	
	/**
	 * A wrapper class for train to help with counting down its time spent here.
	 */
	private class TrainOnTrack extends VBox{
		
		private Train train;
		private int count;
		private Label countLabel;
		
		/**
		 * Adds the train to the wrapper and sets the count based on the distance.
		 * 
		 * @param train
		 * @param count
		 */
		public TrainOnTrack(Train train,int count) {
			this.train = train;
			this.count = count;
			countLabel = new Label("Distance Left: "+count);
			getChildren().add(countLabel);
			getChildren().add(train);
			setPadding(new Insets(10));
		}
		
		/**
		 * Checks if the train is ready if not it decrements it's count.
		 * 
		 * @return
		 */
		public boolean isReady() {
			if(count == 0) {
				return true;
			} else {
				count--;
				countLabel.setText("Distance Left: "+count);
				return false;
			}
		}
		
		/**
		 * Returns the train from this wrapper.
		 * 
		 * @return
		 */
		public Train getTrain() {
			getChildren().remove(train);
			return train;
		}
	}
	
}
