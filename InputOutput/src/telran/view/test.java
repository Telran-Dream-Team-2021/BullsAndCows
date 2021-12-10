
package telran.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class test {
	public static void main(String[] args) throws IOException {
		BufferedReader str = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(str.readLine());
	}
}
