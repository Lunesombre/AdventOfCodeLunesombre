package adventofcode.aoc2024.solvers;

import static adventofcode.aoc2024.common.util.Utils.inputToCharMatrixParser;
import static adventofcode.aoc2024.common.util.Utils.isValidLocation;

import adventofcode.aoc2024.PuzzleInputFetcher;
import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@PuzzleSolverClass("2024-8")
@Component
public class PuzzleSolver2024Day8 extends AbstractPuzzleSolver {

  public PuzzleSolver2024Day8(PuzzleInputFetcher puzzleInputFetcher) {
    super(puzzleInputFetcher);
  }

  @Override
  protected void solveSpecificPuzzle(String input) {
    log.info("Coucou day 8");
    char[][] map = inputToCharMatrixParser(input);

    // PART I
    Set<Coordinates<Integer, Integer>> antinodesLocations;
    Set<Antenna> antennas = IntStream.range(0, map.length)
        .boxed()
        .flatMap(i -> IntStream.range(0, map[i].length)
            .filter(j -> map[i][j] != '.')
            .mapToObj(j -> new Antenna(
                map[i][j],
                new Coordinates<>(i, j)
            ))
        )
        .collect(Collectors.toSet());
    log.info("Nombres d'antennes :{}", antennas.size());

    antinodesLocations = antennas.stream()
        .flatMap(a1 -> antennas.stream()
            .filter(a2 -> a1.frequency == a2.frequency && !a1.equals(a2))
            .map(a2 -> {
              int dx = a2.coordinates.x - a1.coordinates.x;
              int dy = a2.coordinates.y - a1.coordinates.y;
              int antinodeX = a1.coordinates.x + 2 * dx;
              int antinodeY = a1.coordinates.y + 2 * dy;

              if (isValidLocation(antinodeY, antinodeX, map)) {
                return new Coordinates<>(antinodeY, antinodeX);
              }
              return null;
            })
            .filter(Objects::nonNull)
        )
        .collect(Collectors.toSet());

    log.warn("Résultats 2024-8 Partie 1 : le nombre d'antinodes trouvés est : {}", antinodesLocations.size()); // ✅

    // PART II
    antinodesLocations = antennas.stream()
        .flatMap(a1 -> antennas.stream()
            // For each antenna "a1", filters and keeps only antennas with the same frequency (that aren't a1 itself) as "a2"s.
            .filter(a2 -> a1.frequency == a2.frequency && !a1.equals(a2))
            // For each pair (a1, a2), generates antinodes' coordinates
            .flatMap(a2 -> {
              Set<Coordinates<Integer, Integer>> newAntinodesLocations = new HashSet<>();
              // Add a1 coordinates as an antinode, as per instructions
              newAntinodesLocations.add(new Coordinates<>(a1.coordinates.y, a1.coordinates.x));

              int dx = a2.coordinates.x - a1.coordinates.x;
              int dy = a2.coordinates.y - a1.coordinates.y;
              // Note that now we just add dx and dy so the second antenna will be added as an antinode too (as per instructions)
              int antinodeX = a1.coordinates.x + dx;
              int antinodeY = a1.coordinates.y + dy;

              // Browse all points aligned in the direction of a2 until out of the map
              while (isValidLocation(antinodeY, antinodeX, map)) {
                newAntinodesLocations.add(new Coordinates<>(antinodeY, antinodeX)); // a new antinode

                // next point coordinates
                antinodeX += dx;
                antinodeY += dy;
              }
              // return all new antinodes as a stream ready to be collected
              return newAntinodesLocations.stream();
            })
        )
        .collect(Collectors.toSet());

    log.warn("Résultats 2024-8 Partie 2 : le nombre d'antinodes trouvés avec résonances harmoniques est : {}", antinodesLocations.size()); // ✅

  }

  private record Antenna(char frequency, Coordinates<Integer, Integer> coordinates) {

  }

  private record Coordinates<Y, X>(Y y, X x) {

  }
}
