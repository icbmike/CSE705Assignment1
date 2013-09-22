import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class State {

	private int _stateID;
	private String owner;
	
	private HashMap<String, Set<State>> _transitions;

	public State(int state, String owner) {
		this._stateID = state;
		this.owner = owner;
		_transitions = new HashMap<String, Set<State>>();
	}

	public void addSuccessor(String action, State state) {
		
		if(!_transitions.containsKey(action)){
			_transitions.put(action, new HashSet<State>());
		}
		_transitions.get(action).add(state);
		
	}

	public Set<State> getSuccessors(String action) {
		return _transitions.get(action);
	}

	public Set<String> getTransitions() {
		return _transitions.keySet();
	}
	
	@Override
	public String toString() {
		return owner + _stateID;
	}
	
	public String niceOutput(){
		return "state(" + owner + _stateID + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _stateID;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (_stateID != other._stateID)
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

}
