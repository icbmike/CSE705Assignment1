import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class LabeledTransitionSystem {

	private HashMap<Integer, State> _states;
	private String _name;

	public LabeledTransitionSystem(String name) {
		_name = name;
		_states = new HashMap<Integer, State>();
	}

	public void addSuccesor(int state, String action, int successor) {
		_states.get(state).addSuccessor(action, _states.get(successor));

	}

	public void addState(int state, String owner) {
		_states.put(state, new State(state, owner));
	}

	public State getState(int state){
		return _states.get(state);
	}

	public Set<Integer> getStateIDs() {
		return _states.keySet();
		
	}

	public Collection<State> getStates() {
		return _states.values();
	}

	public String getName() {
		return _name;
	}

	
}
