package adventofcode.aoc2024.solvers;

import static java.lang.Long.parseLong;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PuzzleSolverClass("2024-7")
public class PuzzleSolver2024Day7 extends AbstractPuzzleSolver {

  public PuzzleSolver2024Day7(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    Map<Long, List<Integer>> parsedInput = parseInput(input);
    log.debug("total d'élément dans la map : {}", parsedInput.size()); // ✅

    List<Long> keysToRemove = new ArrayList<>(); // used to reduce the size of the Map to be processed in part 2
    // PART I
    long sum = 0L;
    for (Map.Entry<Long, List<Integer>> equation : parsedInput.entrySet()) {
      if (verifyEquation(equation, false)) {
        sum += equation.getKey();
        keysToRemove.add(equation.getKey());
      }
    }
    log.warn("Résultat 2024-7 partie 1 : résultats de la somme de calibration = {}.", sum); //✅

    // PART II
    keysToRemove.forEach(parsedInput::remove); // reduce the size of the parsedInput Map from all the already found solutions
    log.debug("total d'élément dans la map : {}", parsedInput.size()); // OK, 333 entries removed, stays 517 to check.
    for (Map.Entry<Long, List<Integer>> equation : parsedInput.entrySet()) {
      if (verifyEquation(equation, true)) {
        sum += equation.getKey();
      }
    }
    log.warn("Résultat 2024-7 partie 2 : résultats de la somme de calibration, ajusté au 3ème opérateur = {}.", sum); //✅
  }

  private Map<Long, List<Integer>> parseInput(String input) {
    String[] rows = input.trim().split("\\R");
    Map<Long, List<Integer>> parsedInput = HashMap.newHashMap(rows.length);
    for (String row : rows) {
      String[] rowElements = row.split(":");
      String[] numbersAsString = rowElements[1].trim().split("\\s+");
      List<Integer> numbers = Arrays.stream(numbersAsString).map(Integer::parseInt).toList();
      parsedInput.put(parseLong(rowElements[0]), numbers);
    }

    return parsedInput;
  }

  private boolean verifyEquation(Map.Entry<Long, List<Integer>> equation, boolean partTwo) {
    long expectedResult = equation.getKey();
    List<Integer> operands = equation.getValue();
    if (operands == null || operands.isEmpty()) {
      log.warn("Liste vide pour la clé {}", expectedResult);
      return false;
    }

    return canReachExpectedResult(expectedResult, operands, 0, operands.getFirst(), partTwo);

  }

  private boolean canReachExpectedResult(long expectedResult, List<Integer> operands, int index, long current, boolean partTwo) {
    if (current > expectedResult) {
      return false; // Pruning if current exceeds the expected result
    }
    // when the end of the list is reached
    if (index == operands.size() - 1) {
      return current == expectedResult;
    }

    // get next operand
    int next = operands.get(index + 1);

    // Test both possibilities with addition and multiplication
    boolean additionResult = canReachExpectedResult(expectedResult, operands, index + 1, current + next, partTwo);
    boolean multiplicationResult = canReachExpectedResult(expectedResult, operands, index + 1, current * next, partTwo);
    if (partTwo) {
      boolean concatenationResult = canReachExpectedResult(expectedResult, operands, index + 1, concatenate(current, next), true);
      return additionResult || multiplicationResult || concatenationResult;
    }
    return additionResult || multiplicationResult;
  }

  private long concatenate(long current, int next) {
    return Long.parseLong(current + String.valueOf(next));
  }

}
