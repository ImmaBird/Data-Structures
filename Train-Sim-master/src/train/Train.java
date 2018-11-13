package train;

import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Train actions / objectives
 *  - knows it's current station
 *  - knows it's capacity
 *  - if full or not
 *  - tell a station it's arrived
 *  - hold passengers and kill passengers
 */
public class Train extends VBox{
	private Label people = new Label("People: 0");
	private PrintWriter log;
	private boolean forward;
    private int currentStation;
    private int numberOfStations;
    private int maxCapacity = 50;
    private int currentCapacity = 0;
    private int name;
    private ArrayList<Passenger> cabbin = new ArrayList<Passenger>();

    // Sets up the train gui and private variables
    public Train(int name,int currentStation,PrintWriter log,int numberOfStations,boolean forward) {
        this.currentStation = currentStation;
        this.log = log;
        this.forward = forward;
        this.numberOfStations = numberOfStations;
        this.name = name;
        
        newDestination();
        
        getChildren().add(new Label("Train " + name));
        getChildren().add(people);
    }
    
    // Returns the name of this train
    public int getName() {
    	return name;
    }

    // Returns the number of the station that the train is currently in
    public int getCurrentStation() {
    	return currentStation;
    }

    // Returns the intended destination of this train
	public int getDestination() {
		int destination;
		if(forward) {
			destination = currentStation+1;
			destination = (destination == numberOfStations)? 0 : destination;
		} else {
			destination = currentStation-1;
			destination = (destination == -1)? numberOfStations-1 : destination;
		}
		return destination;
	}

	// Removes the passengers from the train that belong at this station
	public int unboard() {
		isReady = false;
		int count = 0;
		for(int i = 0 ; i < cabbin.size() && !this.isEmpty() ; i++) {
			if(cabbin.get(i).getDestination() == currentStation) {
				cabbin.remove(i);
				currentCapacity--;
				people.setText(""+currentCapacity);
				count++;
			}
		}
		log.println(count + " passengers unboarded from train " + name + "."); // Unboarding message
		return count;
	}

	// Boards the passengers onto this train from the platform up too the max capacity
	public void board(Passenger person) {
		currentCapacity++;
		cabbin.add(person);
		people.setText(""+currentCapacity);
	}

	// Returns true if this train is full
	public boolean isFull() {
		return currentCapacity == maxCapacity;
	}

	// Returns true if this train is empty
	public boolean isEmpty() {
		return currentCapacity == 0;
	}

	// Returns true if the train is ready to leave
	private boolean isReady = false;
	public boolean isReady() {
		return isReady;
	}
	
	// Tells the train that it is ready to leave
	public void makeReady() {
		isReady = true;
	}

	// Updates the trains currentStation
	public void newDestination() {
		currentStation = getDestination();
	}
	
	// Returns true if the train is inbound
	public boolean getDirection() {
		return forward;
	}

}
