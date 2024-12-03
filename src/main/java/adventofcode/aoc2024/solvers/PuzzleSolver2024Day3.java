package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PuzzleSolverClass("2024-3")
public class PuzzleSolver2024Day3 extends AbstractPuzzleSolver {

  // Regex built with help of regex101.com : mul\(([0-9]{1,3}),([0-9]{1,3})\)
  // Adapted for Java language : mul\\((\\d{1,3}),(\\d{1,3})\\)
  Pattern patternPartOne = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

  // Regex built with help of regex101.com : do\(\)|don't\(\)|mul\(([0-9]{1,3}),([0-9]{1,3})\)
  // Java version : do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)
  Pattern patternPartTwo = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)"); // 807 matches

  public PuzzleSolver2024Day3(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    // PART 1
    long sum = 0;
    Matcher matcher = patternPartOne.matcher(input);

    // Don't use a map as it would not store doubles twice, thus eliminating a number of valid matches
    List<Pair<Integer, Integer>> elementsToMultiply = new ArrayList<>();
    while (matcher.find()) {
      int x = Integer.parseInt(matcher.group(1));
      int y = Integer.parseInt(matcher.group(2));
      elementsToMultiply.add(new Pair<>(x, y));
    }

    log.info("Combien de couples trouvés : {} ", elementsToMultiply.size());
    for (Pair<Integer, Integer> pair : elementsToMultiply) {
      sum += (long) pair.first() * pair.second();
    }
    log.warn("Résultat 2024-3 partie 1 : la somme des instructions vaut {}.", sum); // Yay, it works! 🤩

    // PART 2
    long sumPartTwo = 0;
    // stores the do() or don't() status, starts enabled
    boolean enabled = true;
    List<Pair<Integer, Integer>> elementsPartTwo = new ArrayList<>();
    Matcher partTwoMatcher = patternPartTwo.matcher(input);

    while (partTwoMatcher.find()) {
      String instruction = partTwoMatcher.group();
      if (instruction.equals("do()")) {
        enabled = true;
      } else if (instruction.equals("don't()")) {
        enabled = false;
      } else if (instruction.startsWith("mul(") && enabled) {
        int x = Integer.parseInt(partTwoMatcher.group(1));
        int y = Integer.parseInt(partTwoMatcher.group(2));
        elementsPartTwo.add(new Pair<>(x, y));
      }
    }
    for (Pair<Integer, Integer> pair : elementsPartTwo) {
      sumPartTwo += (long) pair.first() * pair.second();
    }

    log.info("Combien de couples trouvés : {} ", elementsPartTwo.size()); // 342 matchs
    log.warn("Résultat 2024-3 partie 2 : la somme des instructions, corrigées par les 'do' et 'don't, vaut {}.", sumPartTwo);
  }


  // Internal record class for Pairs, as a Map would replace entries with the same X key ...
  //(523 results in a map VS 732 found using Pair)
  private record Pair<X, Y>(X first, Y second) {

  }

}
