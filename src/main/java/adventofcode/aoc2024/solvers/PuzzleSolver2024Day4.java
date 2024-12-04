package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PuzzleSolverClass("2024-4")
public class PuzzleSolver2024Day4 extends AbstractPuzzleSolver {

  private char[][] parsedInput;
  private final int[][] allDirections = {
      {0, 1}, // right
      {1, 0}, // down
      {0, -1}, // left
      {-1, 0}, // up
      {1, 1}, // diagonal down right
      {1, -1}, // diagonal down left
      {-1, -1}, // diagonal up left
      {-1, 1} // diagonal up right
  };

  private final int[][] diagonalDirections = {
      {1, 1}, // diagonal down right
      {1, -1}, // diagonal down left
      {-1, -1}, // diagonal up left
      {-1, 1} // diagonal up right
  };

  private final Map<Pair<Integer, Integer>, Integer> aPositions = new HashMap<>();

  public PuzzleSolver2024Day4(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    parsedInput = inputParser(input);
    // PART I
    int numberOfXmasFound = countTotalXMAS();
    log.warn("Résultat 2024-4 partie 1 : le nombre de XMAS dans tout les sens trouvé est {}.", numberOfXmasFound); // 2504 yay !

    // PART II
    long numberOfCrossedMAS = countTotalCrossedMAS();
    log.warn("Résultat 2024-4 partie 2 : le nombre de fois où on a deux MAS croisé en X trouvé est {}.", numberOfCrossedMAS);
  }


  private char[][] inputParser(String input) {
    // Splits input on carriage returns
    String[] splitInput = input.split("\\R");
    // Creates the bi-dimensional array at the right size:
    // - Y number of elements in the String[],
    // - X length of the Strings in the String[] (they're all the same length in the input, but might want to check that for more robustness in another context).
    parsedInput = new char[splitInput.length][splitInput[0].length()];
    for (int i = 0; i < splitInput.length; i++) {
      for (int j = 0; j < splitInput[i].length(); j++) {
        parsedInput[i][j] = splitInput[i].charAt(j);
      }
    }
    return parsedInput;
  }

  public int countTotalXMAS() {
    int count = 0;
    for (int i = 0; i < parsedInput.length; i++) {
      for (int j = 0; j < parsedInput[i].length; j++) {
        count += countWordFoundInDirections(allDirections, i, j);
      }
    }
    return count;
  }


  /**
   * Finds and count every XMAS in all allDirections from a starting position in the bi-dimensional array
   *
   * @param row index of row position in the array
   * @param col index of column position in the array
   * @return the number of XMAS found (ranging from 0 to a max of 8)
   */
  private int countWordFoundInDirections(int[][] allowedDirections, int row, int col) {
    String xmas = "XMAS";
    int count = 0;
    for (int[] direction : allowedDirections) {
      if (checkDirection(xmas, row, col, direction[0], direction[1])) {
        count++;
      }
    }
    return count;
  }

  /**
   * Checks if there's XMAS written in a specific direction inside the bi-dimensional array from a starting position
   *
   * @param wordToFind the word to find in the input. For part 1, it's XMAS.
   * @param row        index of row for starting position in the array
   * @param col        index of column for starting position in the array
   * @param dx         horizontal delta for each step (-1, 0 or 1)
   * @param dy         vertical delta for each step (-1, 0 or 1)
   * @return true if it has completed the XMAS sequence, false otherwise.
   */
  private boolean checkDirection(String wordToFind, int row, int col, int dx, int dy) {

    for (int k = 0; k < wordToFind.length(); k++) {
      int newRow = row + k * dx;
      int newCol = col + k * dy;
      if (!isValid(newRow, newCol) || parsedInput[newRow][newCol] != wordToFind.charAt(k)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if a position is valid within the bi-dimensional array
   *
   * @param row index of row to check
   * @param col index of column to check
   * @return true if coordinates are within the limits of the bi-dimensional array, false otherwise
   */
  private boolean isValid(int row, int col) {
    return row >= 0 && row < parsedInput.length && col >= 0 && col < parsedInput[0].length;
  }


  public long countTotalCrossedMAS() {
    for (int i = 0; i < parsedInput.length; i++) {
      for (int j = 0; j < parsedInput[i].length; j++) {
        checkMASInAllowedDirections(i, j);
      }
    }

    // count 'A' positions that have two MAS (crossed as an X as per allowed directions)
    return aPositions.values().stream().filter(count -> count == 2).count();
  }

  private void checkMASInAllowedDirections(int row, int col) {
    for (int[] direction : diagonalDirections) {
      checkMASInDirection(row, col, direction[0], direction[1]);
    }
  }

  private void checkMASInDirection(int row, int col, int dx, int dy) {
    if (isValidMAS(row, col, dx, dy)) {
      // Get the 'A' location for this MAS
      int aRow = row + dx;
      int aCol = col + dy;
      Pair<Integer, Integer> aPosition = new Pair<>(aRow, aCol);

      // Increment counter for this 'A' 's location
      aPositions.merge(aPosition, 1, Integer::sum);
    }
  }

  private boolean isValidMAS(int row, int col, int dx, int dy) {
    return isValid(row, col) && parsedInput[row][col] == 'M' &&
        isValid(row + dx, col + dy) && parsedInput[row + dx][col + dy] == 'A' &&
        isValid(row + 2 * dx, col + 2 * dy) && parsedInput[row + 2 * dx][col + 2 * dy] == 'S';
  }


  private record Pair<X, Y>(X first, Y second) {

  }
}
