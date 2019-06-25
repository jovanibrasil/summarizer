package summ.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pipeline<T> {

	private final List<Pipe<T>> pipes;

	@SafeVarargs
	public Pipeline(Pipe<T>... pipes) {
		this.pipes = Arrays.asList(pipes);
	}
	
	public Pipeline() {
		this.pipes = new ArrayList<>();
	}
	
	public void addPipe(Pipe<T> pipe) {
		this.pipes.add(pipe);
	}
	
	public T process(T input) {
		T processed = input;
		for (Pipe<T> pipe : pipes) {
			processed = pipe.process(processed);
		}
		return processed;
	}
	
}
