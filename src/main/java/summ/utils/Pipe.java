package summ.utils;

public interface Pipe<T> {
	T process(T input);
}
