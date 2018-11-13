package train;

import java.io.PrintWriter;
import com.pearson.carrano.ArrayQueue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Station actions / objectives
 * - knows its number(identifier)
 * - holds trains at platforms
 * - holds passengers
 * - ask passenger where its going, send to appropriate platform
 * - board and unboard passengers
 * - accept trains
 * - determine if there is room for trains
 *
 * @author Charles Arnaudo
 */
public class Station extends VBox {
	private PrintWriter log;
    private final int stationNumber;
    private Platform up = new Platform("Inbound");
    private Platform down = new Platform("Outbound");
    
    // Holds passenger and trains
    private class Platform extends VBox {
    	private ArrayQueue<Passenger> people = new ArrayQueue<Passenger>();
    	private Train train;
    	private int peopleCount = 0;
    	private Label peopleLabel = new Label("People: 0");
    	
    	// Sets up the platform in the gui
    	public Platform(String name) {
    		getChildren().add(new Label(name));
    		getChildren().add(peopleLabel);
    		getChildren().add(placeHolder());
    		setPadding(new Insets(10));
    		Border border = new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT));
            setBorder(border);
    	}
    	
    	// Changes the train in this platform
    	public void setTrain(Train train) {
    		this.train = train;
    		getChildren().remove(this.getChildren().size()-1);
    		getChildren().add(train);
    		unboard();
    		board();
    	}
    	
    	// Adds some more passengers to the simulation
    	public void addPassenger(Passenger person) {
    		people.enqueue(person);
    		peopleCount++;
    		peopleLabel.setText("People: "+peopleCount);
    	}
    	
    	// Removes the train from the platform
    	public Train getTrain() {
    		Train tempTrain = train;
    		getChildren().remove(train);
    		getChildren().add(placeHolder());
    		train = null;
    		return tempTrain;
    	}
    	
    	// Used to fill space when a train is not there
    	private VBox placeHolder() {
    		VBox vBox = new VBox();
    		vBox.getChildren().addAll(new Label(),new Label());
			return vBox;
		}

    	// Fills the train with passengers from this platform
		public void board() {
    		int count = 0;
    		while(!train.isFull() && !people.isEmpty()) {
    			train.board(people.dequeue());
    			count++;
    			peopleCount--;
    			peopleLabel.setText("People: "+peopleCount);
    		}
    		log.println(count + " passengers boarded on train " + train.getName() + "."); // Boarding message
    		train.makeReady();
    	}
		
		// Deletes the passengers who's stop is at this station
    	public void unboard() {
    		train.unboard();
			peopleLabel.setText("People: "+peopleCount);
    	}
    	
    	// Returns true if the train is ready to leave
    	public boolean isReady() {
    		if(train != null) {
    			return train.isReady();
    		}
    		return false;
    	}
    }
    
    /**
     * Sets up the gui and private variables for this station.
     *
     * @param stationNumber
     */
    public Station(int stationNumber,PrintWriter log) {
        this.stationNumber = stationNumber;
        this.log = log;
        
        getChildren().add(new Label("Station " + stationNumber));
        getChildren().addAll(up,down);
        Border border = new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT));
        setBorder(border);
        setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,CornerRadii.EMPTY,new Insets(0))));
    }

    /**
     * Accepts a train into this station and determines what platform it belongs too
     *
     * @param train
     */
    public void acceptTrain(Train train) {
        if(train.getDirection()) {
        	up.setTrain(train);
        } else {
        	down.setTrain(train);
        }
    }

    /**
     * Adds passengers to the queue of the platform that would optimize its travel distance
     *
     * @param passenger
     */
    public void addPassenger(Passenger person) {
    	if(person.getDestination() == stationNumber) {
    		return;
    	}
    	
    	if(person.getDestination() > stationNumber) {
        	up.addPassenger(person);
        } else {
        	down.addPassenger(person);
        }
    }

    /**
     * Returns the station number
     *
     * @return
     */
    public int getStationNumber() {
        return stationNumber;
    }

    // Returns a train to the caller and removes it
	public Train getTrain() {
		if(up.isReady()) {
			return up.getTrain();
		} else {
			return down.getTrain();
		}
	}

	// Returns true is a train is ready to be taken
	public boolean hasReadyTrain() {
		return up.isReady() || down.isReady();
	}

}
