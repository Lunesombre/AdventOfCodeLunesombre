package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PuzzleSolverClass("2024-9")
public class PuzzleSolver2024Day9 extends AbstractPuzzleSolver {

  public PuzzleSolver2024Day9(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    // PART I
    List<String> inputAsListOfString = new ArrayList<>();
    int id = 0;
    for (int i = 0; i < input.length(); i++) {
      char charAtI = input.charAt(i);
      int intAtI = Character.getNumericValue(charAtI);
      if (i % 2 == 0) {
        for (int j = 0; j < intAtI; j++) {
          inputAsListOfString.add(String.valueOf(id));
        }
        id++;
      } else {
        for (int j = 0; j < intAtI; j++) {
          inputAsListOfString.add(".");
        }
      }
    }

    // new List<Integer> to get the inputAsListOfString sorted.
    List<Integer> sortedList = new ArrayList<>();

    // The queue is used to store the values that should replace dots.
    Deque<Integer> queue = inputAsListOfString.stream()
        .filter(s -> !s.equals("."))
        .map(Integer::parseInt)
        .collect(Collectors.toCollection(ArrayDeque::new));
    // populating the sorted List
    for (String element : inputAsListOfString) {
      if (!element.equals(".") && !queue.isEmpty()) {
        sortedList.add(Integer.valueOf(element));
        queue.removeFirst();
      } else if (!queue.isEmpty()) {
        sortedList.add(queue.getLast());
        queue.removeLast();
      } else {
        log.warn("queue is empty now, breaking");
        break;
      }
    }

    log.debug("sortedList 20 premiers :");
    for (int i = 0; i < 20; i++) {
      log.debug(String.valueOf(sortedList.get(i)));
    }

    // Lol, at first I overestimated the possible result by a lot of order, using a BigInteger, but a long is enough
    long resultPartI = 0L;

    for (int i = 0; i < sortedList.size(); i++) {
      resultPartI += (long) i * sortedList.get(i);
    }
    log.warn("Résultat 2024-9 partie 1 : le fileSystem checksum est : {}", resultPartI);

    // PART 2
    partTwo(input);
  }

  private void partTwo(String input) {

    List<Integer> sortedListPartTwo = new ArrayList<>();

    List<FileBlock<String, Integer>> inputAsFileBlocks = new ArrayList<>();
    int id = 0;
    for (int i = 0; i < input.length(); i++) {
      char charAtI = input.charAt(i);
      int intAtI = Character.getNumericValue(charAtI);
      if (i % 2 == 0) {
        inputAsFileBlocks.add(new FileBlock<>(String.valueOf(id), intAtI));
        id++;
      } else if (intAtI != 0) {
        inputAsFileBlocks.add(new FileBlock<>(".", intAtI));
      }
    }
//    for (int i = 0; i < 100; i++) {
//      System.out.println(inputAsFileBlocks.get(i));
//    }
  }

  private record FileBlock<X, Y>(X value, Y size) {

  }
}
