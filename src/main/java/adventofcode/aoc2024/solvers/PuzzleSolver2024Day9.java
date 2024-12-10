package adventofcode.aoc2024.solvers;

import static java.lang.Long.parseLong;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    // Et si on le faisait directement sur l'input de base ?
    List<String> sortedListPartTwo = new ArrayList<>();
    List<Integer> sizesOfSpaces = new ArrayList<>(input.length() / 2 + 1); // list initial size to contain values of uneven indices,
    // +1 is there in case the input.length() is uneven.

    // pour chaque chiffre à chaque index de la String input,
    // si c'est un impair, stocke le bon nombre de "." ✅
    //  dans une autre liste enregistre ce chiffre ✅
    // si c'est un pair, regarde s'il y a un chiffre supérieur ou égal dans la liste de chiffre, si oui :
    // supprime remplace le chiffre de la liste par la différence entre le chiffre de la liste et la valeur du character de la String input
    // trouve la première occurrence de répétition de "." avec le bon chiffre, et remplace le nombre correspondant de points par l'ID
    // si non : ajoute le bon de nombre de répétitions d'id

    // TODO: NOOOOOOOOOOOOOOOOOOOO IT STARTS FROM THE END OF THE STRING: need at least two steps
    int id = 0;
    for (int i = 0; i < input.length(); i++) {
      char charAtI = input.charAt(i);
      int intAtI = Character.getNumericValue(charAtI);
      if (i % 2 == 0) {
//        int space = sizesOfSpaces.stream()
//            .filter(n ->n == charAtI)
//            .findFirst().orElse(-1);
        int index = IntStream.range(0, sizesOfSpaces.size())
            .filter(n -> sizesOfSpaces.get(n) >= intAtI)
            .findFirst()
            .orElse(-1);
        if (index != -1) {
          int difference = sizesOfSpaces.get(index) - intAtI;
          if (difference == 0) {
            sizesOfSpaces.remove(index); // I'm done looping from sizesOfSpaces there so should be ok if mono-threaded
          } else {
            sizesOfSpaces.set(index, difference);
          }
          replaceConsecutiveDots(sortedListPartTwo, intAtI, String.valueOf(id));

        } else {
          for (int j = 0; j < intAtI; j++) {
            sortedListPartTwo.add(String.valueOf(id));
          }
        }
        id++;
      } else {
        for (int j = 0; j < intAtI; j++) {
          sortedListPartTwo.add(".");
          sizesOfSpaces.add(intAtI);
        }
      }
    }

    // Maintenant j'ai une liste de String "ordonnée" et je n'ai plus qu'à boucler dedans
    long newCheckSum = 0;
    for (int i = 0; i < sortedListPartTwo.size(); i++) {
      if (!sortedListPartTwo.get(i).equals(".")) {
        newCheckSum += parseLong(sortedListPartTwo.get(i)) * i;
      }
    }
    log.warn("Résultat 2024-9 partie 2 : le fileSystem checksum est : {}", newCheckSum);


  }

  public void replaceConsecutiveDots(List<String> elements, int n, String replacement) {
    int currentCount = 0; // Compteur pour les "." consécutifs

    for (int i = 0; i < elements.size(); i++) {
      if (elements.get(i).equals(".")) {
        currentCount++; // Incrémente le compteur si l'élément est "."

        // Si on a trouvé au moins n "." consécutifs
        if (currentCount == n) {
          // Remplacer les n premiers "." par le caractère de remplacement
          for (int j = 0; j < n; j++) {
            elements.set(i - j, replacement); // Remplace le "." par le caractère de remplacement
          }
          break; // On sort de la boucle après le remplacement
        }
      } else {
        currentCount = 0; // Réinitialiser le compteur si l'élément n'est pas "."
      }
    }
  }
}
