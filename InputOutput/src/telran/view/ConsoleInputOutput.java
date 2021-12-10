package telran.view;

import java.io.*;


public class ConsoleInputOutput implements InputOutput {
	BufferedReader reader = new BufferedReader(
			new InputStreamReader(System.in));
	@Override
	public String readString(String prompt) {
		writeObjectLine(prompt);
		String res = null;
		try {
			res = reader.readLine();
//			if(res==null) {
//				throw new RuntimeException();
//			}
		} catch (IOException e) {
		}
		return res;
	}

	@Override
	public void writeObect(Object obj) {
		System.out.print(obj);
	}
}
