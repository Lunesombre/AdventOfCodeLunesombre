package adventofcode.aoc2024;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PuzzleSolver {

  private final PuzzleInputFetcher puzzleInputFetcher;

  public PuzzleSolver(PuzzleInputFetcher puzzleInputFetcher) {
    this.puzzleInputFetcher = puzzleInputFetcher;
  }

  public void solvePuzzle(int day, int year) {
    System.out.println("Résolution du puzzle du jour " + day + ", année " + year);
    // Logique de résolution spécifique à chaque puzzle
    String input = puzzleInputFetcher.getPuzzleInput(year, day);
    if (null != input) {
      log.info("Puzzle input aquired : \n{}", input.substring(0, 20) + "...");
    }
  }
}
