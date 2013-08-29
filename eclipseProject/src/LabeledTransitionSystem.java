import java.util.HashMap;

public class LabeledTransitionSystem {

	private HashMap<Integer, State> _states;

	public LabeledTransitionSystem() {
		_states = new HashMap<Integer, State>();
	}

	public void addSuccesor(int state, String action, int successor) {
		_states.get(state).addSuccessor(action, _states.get(successor));

	}

	public void addState(int state) {
		_states.put(state, new State(state));
	}

}
