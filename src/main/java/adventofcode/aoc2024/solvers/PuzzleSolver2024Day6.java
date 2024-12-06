package adventofcode.aoc2024.solvers;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PuzzleSolverClass("2024-6")
public class PuzzleSolver2024Day6 extends AbstractPuzzleSolver {

  /**
   * "Guard Gallivant"
   */
  public PuzzleSolver2024Day6(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    List<GuardPosition> guardPositions = new ArrayList<>();
    Set<String> distinctPositionsVisited = new HashSet<>();
    char[][] map = inputParser(input);

    // PART I
    GuardPosition initialGuardPosition = getInitialGuardCoordinates(map);
    log.info("Localisation initiale du garde : {},{}, orientation : {}", initialGuardPosition.getX(), initialGuardPosition.getY(),
        initialGuardPosition.getOrientation());
    guardPositions.add(initialGuardPosition);
    distinctPositionsVisited.add(initialGuardPosition.getX() + "," + initialGuardPosition.getY());
    GuardPosition currentGuardPosition = new GuardPosition(initialGuardPosition.getX(), initialGuardPosition.getY(),
        initialGuardPosition.getOrientation());
    int countMoves = 0;
    while (isValid(currentGuardPosition.getY(), currentGuardPosition.getX(), map)) {
      int nextX = currentGuardPosition.getX();
      int nextY = currentGuardPosition.getY();
      // assess move
      switch (currentGuardPosition.getOrientation()) {
        case '^' -> nextY--;
        case 'v' -> nextY++;
        case '>' -> nextX++;
        case '<' -> nextX--;
      }
      if (isValid(nextY, nextX, map)) {
        if (map[nextY][nextX] == '#') {
          char newOrientation = turnRight(currentGuardPosition.getOrientation());
          currentGuardPosition.setOrientation(newOrientation);
        } else {
          guardPositions.add(new GuardPosition(nextX, nextY, currentGuardPosition.getOrientation()));
          currentGuardPosition.setX(nextX);
          currentGuardPosition.setY(nextY);
          distinctPositionsVisited.add(currentGuardPosition.getX() + "," + currentGuardPosition.getY());
          countMoves++;
          if (countMoves % 1000 == 0) {
            log.info("Le garde a déjà fait {} mouvements.", countMoves);
          }
        }
      } else {
        break; // Breaks if off limits
      }
    }
    log.info("Le garde a quitté la pièce depuis x={},y={}", currentGuardPosition.getX(), currentGuardPosition.getY());
    log.warn("Résultat 2024-6 partie 1 : le nombre de positions distinctes parcourues par le garde est {}.", distinctPositionsVisited.size());
//    log.warn("La liste des positions parcourues : ");
//
//    // Quand on accède à la map, c'est map[Y][X] toujours.
//    for (GuardPosition pos : guardPositions) {
//      log.info(pos.toString());
//    }

    // PART II
    // I already have the positions where the guard went and its direction then, in guardPositions.
    // Start on the second position (i=1) as we can't add an obstacle on his starting position
    int numberOfPositionsForSingleObstacleToCauseInfiniteLoop = 0;
    Set<Coordinates<Integer, Integer>> testedObstacleCoordinates = new HashSet<>();
    for (int i = 1; i < guardPositions.size(); i++) {
      if (i % 10 == 0) {
        log.info("{}ème tour de boucle sur {}", i, guardPositions.size());
      }
      int y = guardPositions.get(i).getY();
      int x = guardPositions.get(i).getX();
      if (testedObstacleCoordinates.contains(new Coordinates<>(y, x))) {
        log.info("Avoided redundancy on loop: {}", i);
        continue;
      } else {
        testedObstacleCoordinates.add(new Coordinates<>(y, x));
      }

      List<GuardPosition> tempGuardPositions = new ArrayList<>();
      // creates a copy of the map to modify it
      char[][] tempMap = new char[map.length][];
      for (int j = 0; j < map.length; j++) {
        tempMap[j] = map[j].clone();
      }

      tempMap[y][x] = '#'; // place the obstacle on this position were the guard is supposed to go and see if it makes him loop infinitely

      tempGuardPositions.add(initialGuardPosition);
      GuardPosition guardPosition = new GuardPosition(initialGuardPosition.getX(), initialGuardPosition.getY(),
          initialGuardPosition.getOrientation());
      while (isValid(guardPosition.getY(), guardPosition.getX(), tempMap)) {
        int nextX = guardPosition.getX();
        int nextY = guardPosition.getY();

        switch (guardPosition.getOrientation()) {
          case '^' -> nextY--;
          case 'v' -> nextY++;
          case '>' -> nextX++;
          case '<' -> nextX--;
        }
        if (isValid(nextY, nextX, tempMap)) {
          if (tempMap[nextY][nextX] == '#') {
            guardPosition.setOrientation(turnRight(guardPosition.getOrientation()));
          } else {
            GuardPosition newGuardPosition = new GuardPosition(nextX, nextY, guardPosition.getOrientation());
            if (isGuardInAnInfiniteLoop(tempGuardPositions, newGuardPosition)) {
              numberOfPositionsForSingleObstacleToCauseInfiniteLoop++;
              log.info("Found one, total found :{}", numberOfPositionsForSingleObstacleToCauseInfiniteLoop);
              break;
            }
            tempGuardPositions.add(newGuardPosition);
            guardPosition.setX(nextX);
            guardPosition.setY(nextY);
          }

        } else {
          break; // Breaks if off limits
        }
      }
    }
    log.warn(
        "Résultat 2024-6 partie 2 : le nombre de positions distinctes sur lesquelles placer un unique obstacle pour enfermer le garde dans une boucle infinie est : {}.",
        numberOfPositionsForSingleObstacleToCauseInfiniteLoop);


  }


  private boolean isGuardInAnInfiniteLoop(List<GuardPosition> currentListofGuardPositions, GuardPosition currentPosition) {
    return currentListofGuardPositions.stream()
        .anyMatch(pos -> pos.getX() == currentPosition.getX()
            && pos.getY() == currentPosition.getY()
            && pos.getOrientation() == currentPosition.getOrientation());
  }

  private char[][] inputParser(String input) {
    String[] splitInput = input.trim().split("\\R");
    char[][] map = new char[splitInput.length][splitInput[0].length()];
    for (int i = 0; i < splitInput.length; i++) {
      for (int j = 0; j < splitInput[i].length(); j++) {
        map[i][j] = splitInput[i].charAt(j);
      }
    }

    return map;
  }

  private void displayMap(char[][] map) {
    for (char[] row : map) {
      System.out.println(new String(row));
    }
  }

  private GuardPosition getInitialGuardCoordinates(char[][] map) {
    Set<Character> guardRepresentations = Set.of('^', 'v', '>', '<');
    return IntStream.range(0, map.length)
        .boxed()
        .flatMap(i -> IntStream.range(0, map[i].length)
            .filter(j -> guardRepresentations.contains(map[i][j]))
            .mapToObj(j -> new GuardPosition(j, i, map[i][j])))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No guard found"));
  }

  private static char turnRight(char orientation) {
    return switch (orientation) {
      case '^' -> '>';
      case '>' -> 'v';
      case 'v' -> '<';
      case '<' -> '^';
      default -> throw new IllegalArgumentException("Orientation invalide");
    };
  }


  private boolean isValid(int row, int col, char[][] map) {
    return row >= 0 && row < map.length && col >= 0 && col < map[0].length;
  }


  @Getter
  @Setter
  private static class GuardPosition {

    private int x;
    private int y;
    private char orientation;

    public GuardPosition(int x, int y, char orientation) {
      this.x = x;
      this.y = y;
      this.orientation = orientation;
    }

    @Override
    public String toString() {
      return "GuardPosition{" +
          "x=" + x +
          ", y=" + y +
          ", orientation: " + orientation +
          '}';
    }
  }

  private record Coordinates<Y, X>(Y y, X x) {

  }

}
