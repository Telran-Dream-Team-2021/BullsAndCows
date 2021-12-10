package telran.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {

	public static final String ERROR_PROMPT = "Wrong input, please repeat according to format";

	public String readString(String prompt);

	public void writeObect(Object obj); 

	default void writeObjectLine(Object obj) { 
		writeObect(obj);
		writeObect("\n");
	}

	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {

		while (true) {
			try {
				String string = readString(prompt);
				R res = mapper.apply(string);
				return res;
			} catch (Exception e) {
				writeObjectLine(errorPrompt);
				throw new EndOfInputException();
			}
			
		}
		

	}

	default Integer readInt(String prompt) {
		return readObject(prompt, "No integer number", Integer::parseInt);
	}

	default Integer readInt(String prompt, int min, int max) {
		return readObject(prompt, String.format("Incorrect item [%d - %d]\n", min, max), str -> {
			int num = Integer.parseInt(str);
			if(num < min || num > max) {
				throw new IllegalArgumentException();
			}
			return num;
		});
	}

	default String readStringOption(String prompt, Set<String> options) {
		return readStringPredicate(prompt, "entered string is not among the options",
				str->options.contains(str));
	}

	default Long readLong(String prompt) {
		return readObject(prompt, "no integer number", Long::parseLong);
	}

	default LocalDateTime readDate(String prompt) {
		return readObject(prompt, "Wrong date [yyyy-MM-dd HH:mm]", LocalDateTime::parse);
	}

	default LocalDateTime readDate(String prompt, String formatPattern) {
		return readObject(prompt,"Wrong date in format " + formatPattern, str -> 
			LocalDateTime.parse(str, DateTimeFormatter.ofPattern(formatPattern))
		);
	}

	public default String readStringPredicate(String prompt, String errorMessage, Predicate<String> predicate) {
		return readObject(prompt, errorMessage, str->{

			if(!predicate.test(str)) {
				throw new IllegalArgumentException();
			}
			return str;
		});
	}
}