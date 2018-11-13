package train;

/**
 * Passenger actions / objectives
 *  - has destination station
 *  - current station
 */
public class Passenger {
	
    private final int destination;

    // Sets a randomly generated destination
    public Passenger(int destination) {
        this.destination = destination;
    }
    
    // Returns the destination of the passenger
    public int getDestination() {
    	return destination;
    }

}
