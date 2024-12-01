package adventofcode.aoc2024.common;

import adventofcode.aoc2024.PuzzleInputFetcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPuzzleSolver {


  protected final PuzzleInputFetcher puzzleInputFetcher;

  protected AbstractPuzzleSolver(PuzzleInputFetcher puzzleInputFetcher) {
    this.puzzleInputFetcher = puzzleInputFetcher;
  }

  public void solvePuzzle(int day, int year) {
    System.out.println("Résolution du puzzle du jour " + day + ", année " + year);
    String input = puzzleInputFetcher.getPuzzleInput(year, day);

    if (input != null) {
      log.info("Puzzle input acquired : \n{}", input.substring(0, 20) + "...");
      // now really solve the puzzle
      solveSpecificPuzzle(input);
    } else {
      log.warn("Aucun input disponible pour le jour {}.", day);
    }
  }

  // Abstract method to implement in child classes
  protected abstract void solveSpecificPuzzle(String input);
}
