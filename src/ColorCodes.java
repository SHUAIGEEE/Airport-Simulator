import java.util.concurrent.ThreadLocalRandom;

public class ColorCodes {
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String LIGHTBLUE = "\u001B[38;5;45m";
    public static final String PINK = "\u001B[38;5;206m";
    public static final String BOLD_ITALIC = "\u001B[4m";
    public static final String ATC_WHITE = "\u001B[30;47;1m";
    public static final String EMERGENCY = "\u001B[30;43;1m";
    public static final String UNDERLINE_ON = "\u001B[4m";
    public static final String UNDERLINE_OFF = "\u001B[24m";
    public static final String RESET = "\u001B[0m";

    public static final String[] colorArray = {RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, LIGHTBLUE, PINK};

    public static String generateRandomColorCode() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, colorArray.length);
        return colorArray[randomIndex];
    }


}
