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

  private List<Integer> leftList;
  private List<Integer> rightList;

  public PuzzleSolver2024Day1(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    Map<Integer, Integer> parsedInput = inputParser(input);
    int sum = 0;
    long sumPartTwo = 0;

    // resolves part 1
    for (Map.Entry<Integer, Integer> entry : parsedInput.entrySet()) {
      log.debug("Clé: {}, Valeur: {}", entry.getKey(), entry.getValue());
      int absoluteDistance = Math.abs(entry.getKey() - entry.getValue());
      log.debug("Absolute distance :{}", absoluteDistance);
      sum += absoluteDistance;
    }
    log.warn("La réponse à la partie 1 est: {}", sum);

    // resolves part 2
    for (Integer elementOfLeftList : leftList) {
      long count = rightList.stream()
          .filter(elementOfRightList -> elementOfRightList.equals(elementOfLeftList)) // Utilisez equals pour comparer
          .count();
      sumPartTwo += (count * elementOfLeftList);
    }
    log.warn("La réponse à la partie 2 est: {}", sumPartTwo);

  }


  private Map<Integer, Integer> inputParser(String input) {
    String[] splitInput = input.trim().split("\\s+"); //splits on one or several spaces
    Map<Integer, Integer> parsedInput = new LinkedHashMap<>();
    leftList = new ArrayList<>(splitInput.length / 2);
    rightList = new ArrayList<>(splitInput.length / 2);

    for (int i = 0; i < splitInput.length; i++) {
      if (i % 2 == 0) {
        leftList.add(Integer.valueOf(splitInput[i]));
      } else {
        rightList.add(Integer.valueOf(splitInput[i]));
      }
    }
    if (leftList.size() != rightList.size()) {
      throw new IllegalArgumentException("Uneven number of elements in the splitInput array");
    }
    leftList.sort(Comparator.naturalOrder());
    rightList.sort(Comparator.naturalOrder());
    for (int i = 0; i < leftList.size(); i++) {
      parsedInput.put(leftList.get(i), rightList.get(i));
    }
    return parsedInput;
  }
}
