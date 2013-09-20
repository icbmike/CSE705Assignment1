import java.util.HashMap;
import java.util.Set;

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

	public State getSuccessor(String action) {
		return _transitions.get(action);
	}

	public Set<String> getTransitions() {
		return _transitions.keySet();
	}
	
	@Override
	public String toString() {
		return _stateID + "";
	}

}
