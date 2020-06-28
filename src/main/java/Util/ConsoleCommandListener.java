package Util;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleCommandListener {
    private static final String QUIT_COMMAND = ".quit";
    
    public ConsoleCommandListener() {
    }
    
    public void run() throws NoSuchElementException {
        Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (!(scanner.nextLine().trim()).equals(QUIT_COMMAND)) {
        }
        scanner.close();
    }
}
