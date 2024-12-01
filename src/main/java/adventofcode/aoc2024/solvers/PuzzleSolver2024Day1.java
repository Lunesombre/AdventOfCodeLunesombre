package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@PuzzleSolverClass("2024-1")
public class PuzzleSolver2024Day1 extends AbstractPuzzleSolver {

  public PuzzleSolver2024Day1(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    Map<Integer, Integer> parsedInput = inputParser(input);
    int sum = 0;

    for (Map.Entry<Integer, Integer> entry : parsedInput.entrySet()) {
      log.debug("Clé: {}, Valeur: {}", entry.getKey(), entry.getValue());
      int absoluteDistance = Math.abs(entry.getKey() - entry.getValue());
      log.debug("Absolute distance :{}", absoluteDistance);
      sum += absoluteDistance;
    }
    log.warn("La réponse est: {}", sum);

  }


  private Map<Integer, Integer> inputParser(String input) {
    String[] splitInput = input.trim().split("\\s+"); //splits on one or several spaces
    Map<Integer, Integer> parsedInput = new LinkedHashMap<>();
    List<Integer> keys = new ArrayList<>(splitInput.length / 2);
    List<Integer> values = new ArrayList<>(splitInput.length / 2);

    for (int i = 0; i < splitInput.length; i++) {
      if (i % 2 == 0) {
        keys.add(Integer.valueOf(splitInput[i]));
      } else {
        values.add(Integer.valueOf(splitInput[i]));
      }
    }
    if (keys.size() != values.size()) {
      throw new IllegalArgumentException("Uneven number of elements in the splitInput array");
    }
    keys.sort(Comparator.naturalOrder());
    values.sort(Comparator.naturalOrder());
    for (int i = 0; i < keys.size(); i++) {
      parsedInput.put(keys.get(i), values.get(i));
    }
    return parsedInput;
  }
}
