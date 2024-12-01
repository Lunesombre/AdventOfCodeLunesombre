package adventofcode.aoc2024;

import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import adventofcode.aoc2024.common.PuzzleSolverClass;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PuzzleManager {

  private static final String PACKAGE_NAME = "adventofcode.aoc2024.solvers";
  private final Map<String, AbstractPuzzleSolver> solvers = new HashMap<>();

  public PuzzleManager(PuzzleInputFetcher puzzleInputFetcher) {
    registerSolvers(puzzleInputFetcher);
  }

  private void registerSolvers(PuzzleInputFetcher puzzleInputFetcher) {

    String path = PACKAGE_NAME.replace('.', '/');

    try {
      URL resource = getClass().getClassLoader().getResource(path);
      if (resource != null) {
        File directory = new File(resource.toURI());
        if (directory.exists() && directory.isDirectory()) {
          File[] files = directory.listFiles();
          if (files != null) { // then files contains, well, files 😉
            for (File file : files) {
              if (file.getName().endsWith(".class")) {
                String className = PACKAGE_NAME + '.' + file.getName().replace(".class", "");
                Class<?> cls = Class.forName(className);
                registerSolverIfValid(cls, puzzleInputFetcher);
              }
            }
          } else {
            log.warn("Le répertoire est vide ou n'a pas de fichiers.");
          }
        } else {
          log.warn("Le répertoire n'existe pas ou n'est pas un répertoire valide.");
        }
      } else {
        log.error("Le chemin du package n'a pas pu être trouvé.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void registerSolverIfValid(Class<?> cls, PuzzleInputFetcher puzzleInputFetcher) {
    if (AbstractPuzzleSolver.class.isAssignableFrom(cls) && !cls.equals(AbstractPuzzleSolver.class)) {
      PuzzleSolverClass annotation = cls.getAnnotation(PuzzleSolverClass.class);
      if (annotation != null) {
        String key = annotation.value(); // Gets the id
        try {
          AbstractPuzzleSolver solver = (AbstractPuzzleSolver) cls.getConstructor(PuzzleInputFetcher.class).newInstance(puzzleInputFetcher);
          solvers.put(key, solver); // Registers the Solver with its id
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public AbstractPuzzleSolver getSolver(int year, int day) {
    String key = year + "-" + day;
    return solvers.get(key);
  }
}
