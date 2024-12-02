package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PuzzleSolverClass("2024-2")
public class PuzzleSolver2024Day2 extends AbstractPuzzleSolver {

  public PuzzleSolver2024Day2(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    // Part 1
    String[] inputSplitOnNewLines = input.trim().split("\\R");

    long countSafeReports = 0;

    for (String element : inputSplitOnNewLines) {
      String[] elementSplit = element.trim().split("\\s+");
      List<Integer> elementAsIntegerList = Arrays.stream(elementSplit)
          .map(Integer::parseInt)
          .collect(Collectors.toCollection(ArrayList::new));
      // checks if "levels" in the report are all increasing or decreasing
      List<Integer> unsortedList = new ArrayList<>(elementAsIntegerList);
      if (elementAsIntegerList.getFirst() < elementAsIntegerList.get(1)) {
        elementAsIntegerList.sort(Comparator.naturalOrder());
      } else {
        elementAsIntegerList.sort(Comparator.reverseOrder());
      }
      if (!unsortedList.equals(elementAsIntegerList)) {
        log.debug("the list {} wasn't sorted", unsortedList);
        continue;
      }
      // if the previous part is ok, then verify that the gradual increase/decrease is neither too steep nor shallow
      Integer previousInt = null;
      boolean isSafe = true;
      for (int currentInt : elementAsIntegerList) {
        if (previousInt != null) {
          int variation = Math.abs(currentInt - previousInt);
          if (variation < 1 || variation > 3) {
            isSafe = false;
            break;
          }
        }
        previousInt = currentInt;

      }
      if (isSafe) {
        countSafeReports++;
      }
    }
    log.warn("Résultat 2024-2 partie 1 : {} rapports sont safe !", countSafeReports);

    // PART 2 - PROBLEM DAMPENER 😅
    // I've changed my approach because I was making too much trouble for nothing.
    long countSafeReportsWithDampener = 0;

    for (String element : inputSplitOnNewLines) {
      String[] elementSplit = element.trim().split("\\s+");
      List<Integer> elementAsIntegerList = Arrays.stream(elementSplit)
          .map(Integer::parseInt)
          .collect(Collectors.toCollection(ArrayList::new));

      if (isSafe(elementAsIntegerList) || canBeMadeSafeByProblemDampener(elementAsIntegerList)) {
        countSafeReportsWithDampener++;
      }
    }
    log.warn("Résultat 2024-2 partie 2 : {} rapports sont safe une fois le Dampener appliqué !", countSafeReportsWithDampener);
  }

  private boolean isSafe(List<Integer> report) {
    Integer previousInt = null;
    boolean increasing = false;
    boolean decreasing = false;

    for (int currentInt : report) {
      if (previousInt != null) {
        if (previousInt > currentInt) {
          if (increasing) {
            return false;
          }
          decreasing = true;
        } else if (previousInt < currentInt) {
          if (decreasing) {
            return false;
          }
          increasing = true;
        }

        int variation = Math.abs(currentInt - previousInt);
        if (variation < 1 || variation > 3) {
          return false;
        }
      }
      previousInt = currentInt;
    }
    return true;
  }

  private boolean canBeMadeSafeByProblemDampener(List<Integer> report) {
    for (int i = 0; i < report.size(); i++) {
      List<Integer> modifiedReport = new ArrayList<>(report);
      modifiedReport.remove(i);
      if (isSafe(modifiedReport)) {
        return true;
      }
    }
    return false;
  }
}
