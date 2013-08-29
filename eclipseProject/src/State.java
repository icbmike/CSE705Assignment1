import java.util.HashMap;


public class State {

	private int _stateID;
	private HashMap<String, State> _transitions;
	
	
	public State(int state) {
		this._stateID = state;
		_transitions = new HashMap<String, State>();
	}

	public void addSuccessor(String action, State state) {
		_transitions.put(action, state);
	}

}
