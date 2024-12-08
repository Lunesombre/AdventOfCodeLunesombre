package adventofcode.aoc2024.common.util;

public class Utils {

  private Utils() {
  }

  public static char[][] inputToCharMatrixParser(String input) {
    String[] rows = input.trim().split("\\R");
    char[][] map = new char[rows.length][rows[0].length()];
    for (int i = 0; i < rows.length; i++) {
      for (int j = 0; j < rows[i].length(); j++) {
        map[i][j] = rows[i].charAt(j);
      }
    }

    return map;
  }

  public static boolean isValidLocation(int y, int x, char[][] map) {
    return y >= 0 && y < map.length && x >= 0 && x < map[0].length;
  }
}
