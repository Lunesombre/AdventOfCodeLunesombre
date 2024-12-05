package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PuzzleSolverClass("2024-5")
public class PuzzleSolver2024Day5 extends AbstractPuzzleSolver {

  private final List<Pair<Integer, Integer>> printingInstructions = new ArrayList<>();
  private final List<List<Integer>> pagesToPrint = new ArrayList<>();
  private final Pattern printingInstructionOrderPattern = Pattern.compile("(\\d{2})\\|(\\d{2})");


  public PuzzleSolver2024Day5(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    List<List<Integer>> incorrectlyOrderedUpdates = new ArrayList<>();
    int resultPartOne = 0;
    inputParser(input);
    // PART 1
    int count = 0;
    for (List<Integer> pageNumbersOfUpdate : pagesToPrint) {
      count++;
      if (pageNumbersOfUpdate.size() % 2 == 0) {
        log.warn("Update n°{} : Something's fishy, the list of pages numbers in the update should be uneven, this one has {}", count,
            pageNumbersOfUpdate.size());
        break;
      }

      boolean isValid = checkIfValidOrder(pageNumbersOfUpdate);
      if (isValid) {
        resultPartOne += pageNumbersOfUpdate.get(pageNumbersOfUpdate.size() / 2); // gives the index of the middle index as the list is uneven
        // (and the first index is 0, of course).
      } else {
        incorrectlyOrderedUpdates.add(new ArrayList<>(pageNumbersOfUpdate)); // used in part two, uses mutable arrayList
      }
    }
    log.warn("Résultat 2024-5 partie 1 : l'addition des numéros des pages du milieu des updates valides donne : {}", resultPartOne); // yay

    // PART 2
    int resultPartTwo = 0;
    log.debug("incorrectlyOrderedUpdates : {}", incorrectlyOrderedUpdates.size());
    for (List<Integer> incorrectlyOrderedUpdate : incorrectlyOrderedUpdates) {
      sortUpdate(incorrectlyOrderedUpdate);
      resultPartTwo += incorrectlyOrderedUpdate.get(incorrectlyOrderedUpdate.size() / 2);
    }
    log.warn("Résultat 2024-5 partie 2 : l'addition des numéros des pages du milieu des updates non-valides corrigés donne : {}",
        resultPartTwo);

  }

  private void sortUpdate(List<Integer> incorrectlyOrderedUpdate) {
    Map<Integer, Set<Integer>> graph = new HashMap<>();
    Set<Integer> pages = new HashSet<>(incorrectlyOrderedUpdate);
    // Topological sorting
    // Note to self: only works because the input is nice enough to not have local cycles
    // (according to Reddit, there's global cycle ^^')
    // Could use DFS in case then?
    // But then again it worked hehe

    // Build the graph
    for (Integer page : pages) {
      graph.put(page, new HashSet<>());
    }
    for (Pair<Integer, Integer> rule : printingInstructions) {
      if (pages.contains(rule.left) && pages.contains(rule.right)) {
        graph.get(rule.left).add(rule.right);
      }
    }

    // Topological sorting
    List<Integer> result = new ArrayList<>();
    Set<Integer> visited = new HashSet<>();
    Deque<Integer> stack = new ArrayDeque<>();

    for (Integer page : pages) {
      if (!visited.contains(page)) {
        topologicalSort(page, visited, stack, graph);
      }
    }

    while (!stack.isEmpty()) {
      result.add(stack.pop());
    }

    // Updates the original List
    incorrectlyOrderedUpdate.clear();
    incorrectlyOrderedUpdate.addAll(result);
  }

  private void topologicalSort(Integer page, Set<Integer> visited, Deque<Integer> stack, Map<Integer, Set<Integer>> graph) {
    visited.add(page);
    for (Integer neighbor : graph.get(page)) {
      if (!visited.contains(neighbor)) {
        topologicalSort(neighbor, visited, stack, graph);
      }
    }
    stack.push(page);
  }

  private boolean checkIfValidOrder(List<Integer> pageNumbersOfUpdate) {
    for (int i = 0; i < pageNumbersOfUpdate.size(); i++) {
      for (int j = i + 1; j < pageNumbersOfUpdate.size(); j++) {
        int left = pageNumbersOfUpdate.get(i);
        int right = pageNumbersOfUpdate.get(j);
        if (printingInstructions.contains(new Pair<>(right, left))) { // inversion pour voir si une règle opposée existe
          return false;
        }
      }
    }
    return true;
  }


  public void inputParser(String input) {
    printingInstructions.clear();
    pagesToPrint.clear();
    Matcher instructionMatcher = printingInstructionOrderPattern.matcher(input);
    int lastIndex = -1;
    while (instructionMatcher.find()) {
      lastIndex = instructionMatcher.end();
      int left = Integer.parseInt(instructionMatcher.group(1));
      int right = Integer.parseInt(instructionMatcher.group(2));
      printingInstructions.add(new Pair<>(left, right));
    }
    String truncatedInput = input.substring(lastIndex + 2); // +2 to remove the next 2 carriage returns.
    log.debug("input tronqué : {} ...", truncatedInput.substring(0, 20));

    String[] pagesAsArray = truncatedInput.trim().split("[\\ns+]"); // split on new lines and spaces
    log.debug("pagesAsArray : {}", Arrays.stream(pagesAsArray).count());
    for (String pagesNumbersToPrint : pagesAsArray) {
      List<Integer> pagesNumbers = Arrays.stream(pagesNumbersToPrint.split(","))
          .map(Integer::parseInt)
          .toList();
      pagesToPrint.add(pagesNumbers);
    }
  }


  private record Pair<X, Y>(X left, Y right) {

  }
}
