package project;
import java.util.ArrayList;

public class List<T> implements IList<T>{
	ArrayList<T> List = new ArrayList<T>();
	@Override
	public void push(T data) {
		List.add(data);
	}

	@Override
	public T pop() {
		T delete = List.get(List.size()-1);
		List.remove(delete);
		return delete;
	}

	@Override
	public int size() {
		return List.size();
	}
}
