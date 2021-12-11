import java.io.Console;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

public class Test {
	public static void main(String[] args) throws InterruptedException, IOException {
		// Get current date time
//        LocalDateTime now = LocalDateTime.now().toInstant(null);

//        System.out.println("Before : " + now);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        String formatDateTime = now.format(formatter);

//        LocalDateTime f = now.format(formatter);

//		System.out.println(System.currentTimeMillis());
//
//		LocalDateTime ldt = LocalDateTime.of(2014, 5, 29, 18, 41);
//		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
//		long millis = zdt.toInstant().toEpochMilli();
//		System.out.println(millis);
//		System.out.print("\033[H\033[2J");
//		ClearConsole();
//		System.out.println("new console");
//		System.out.println(Integer.MAX_VALUE);
//		Date date = new Date(System.currentTimeMillis());
//		System.out.println(date);
//		LocalDateTime time = LocalDateTime.now().withSecond(0).withNano(0);
//		String filePath = String.format("games/%d_%s_%d_%d_%d_%d_%d", 123, "Vladimir", time.getYear(),
//				time.getMonth().getValue(), time.getDayOfMonth(), time.getHour(), time.getMinute());
//		System.out.println(filePath);
//		System.out.println(time);
//		String formatPattern = "yyyy-MM-dd HH:mm";
//		String str = "2021-09-27 18:30";
//		System.out.println(LocalDateTime.parse(str, DateTimeFormatter.ofPattern(formatPattern)));
//		
//		String now = "2016-11-09 10:30";
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//        LocalDateTime formatDateTime = LocalDateTime.parse(now, formatter);
//
//        System.out.println("Before : " + now);
//
//        System.out.println("After : " + formatDateTime);
//
//        System.out.println("After : " + formatDateTime.format(formatter));
//        String rand = new Random().ints(1, 10).distinct().limit(4).toArray().toString();
//        System.out.println(new Random().ints(1, 10).distinct().limit(4).toArray());
//        System.out.println(new Random()
//				.ints(1, 10)
//				.distinct()
//				.limit(4)
//				.boxed()
//				.map(n->Integer.toString(n))
//				.collect(Collectors.joining()));
		Instant time = Instant.now();
		long t = time.toEpochMilli();
		System.out.println(Instant.now().getEpochSecond());

	}

	public static void ClearConsole() throws InterruptedException, IOException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
}
