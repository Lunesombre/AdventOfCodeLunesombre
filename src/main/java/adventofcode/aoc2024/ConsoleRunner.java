package adventofcode.aoc2024;

import adventofcode.aoc2024.common.AbstractPuzzleSolver;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {

  private final Scanner scanner;
  private final PuzzleManager puzzleManager;

  public ConsoleRunner(PuzzleManager puzzleManager) {
    this.puzzleManager = puzzleManager;
    this.scanner = new Scanner(System.in);

  }

  @Override
  public void run(String... args) {
    while (true) {
      System.out.println("Choix du puzzle à traiter : ");
      System.out.println("Entrez le numéro du jour (1-25) ou 0 pour quitter. Par défaut année en cours, sinon tapez d'abord l'année :");
      int day;

      LocalDateTime currentDate = LocalDateTime.now();
      int currentMonth = currentDate.getMonth().getValue();
      int maxYear = (currentMonth == 12) ? currentDate.getYear() : (currentDate.getYear() - 1);
      int year = maxYear;

// Vérifiez si l'entrée est un entier
      if (scanner.hasNextInt()) {
        int input = scanner.nextInt();
        if (input == 0) {
          break;
        } else if (input > 0 && input <= 25) {
          day = input;
          AbstractPuzzleSolver solver = puzzleManager.getSolver(year, day);
          if (solver != null) {
            solver.solvePuzzle(day, year);
          } else {
            System.out.println("Aucun solveur disponible pour le puzzle " + year + "-" + day);
          }
        } else if (input >= 2015 && input <= maxYear) {
          year = input;

          System.out.println("Entrez le numéro du jour (1-25) :");
          while (true) {
            day = scanner.nextInt();
            if (day > 0 && day <= 25) {
              break; // Sortir de la boucle si le jour est valide
            } else {
              System.out.println("Jour invalide, veuillez réessayer (1-25) :");
            }
          }
          AbstractPuzzleSolver solver = puzzleManager.getSolver(year, day); // Obtenez le solveur
          if (solver != null) {
            solver.solvePuzzle(day, year);
          } else {
            System.out.println("Aucun solveur disponible pour le puzzle " + year + "-" + day);
          }
        } else {
          System.out.println("Entrée invalide, veuillez réessayer.");
        }
      } else {
        System.out.println("Entrée invalide, veuillez entrer un nombre entier.");
        scanner.next(); // Consumes the invalid input to avoid infinite loop
      }
    }
    scanner.close();
  }
}
