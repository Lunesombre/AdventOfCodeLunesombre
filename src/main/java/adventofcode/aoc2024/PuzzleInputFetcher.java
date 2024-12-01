package adventofcode.aoc2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class PuzzleInputFetcher {

  private static final String AOC_URL = "https://adventofcode.com/%d/day/%d/input";
  private static final String INPUT_FILE_PATTERN = "%d_input_day_%d.txt";
  private static final String FILE_DIRECTORY = "src/main/resources/inputs";
  @Value("${aoc.session.cookie}")
  private String sessionCookie;

  public String getPuzzleInput(int year, int day) {
    if (!isPuzzleAvailable(year, day)) {
      System.out.println("Le puzzle n'est pas encore disponible.");
      return null;
    }

    String inputFilePath = String.format(INPUT_FILE_PATTERN, year, day);
    Path fullPath = Paths.get(FILE_DIRECTORY, inputFilePath);

    if (Files.exists(fullPath)) {
      return readInputFromFile(fullPath.toString());
    } else {
      String input = fetchInputFromAoC(year, day);
      saveInputToFile(input, year, day);
      return input;
    }
  }

  public boolean isPuzzleAvailable(int year, int day) {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("America/New_York"));
    LocalDateTime puzzleReleaseTime = LocalDateTime.of(year, 12, day, 0, 0);
    return now.isAfter(puzzleReleaseTime);
  }

  private String fetchInputFromAoC(int year, int day) {
    String url = String.format(AOC_URL, year, day);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", "session=" + sessionCookie);

    ResponseEntity<String> response = new RestTemplate().exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

    return response.getBody();
  }

  private void saveInputToFile(String input, int year, int day) {
    if (input == null) {
      log.error("L'input est nul, impossible de sauvegarder.");
      return;
    }

    // Checks if day and year are valid
    if (day <= 0 || day > 25) {
      System.out.println("Le jour doit être compris entre 1 et 25");
      return;
    }

    LocalDateTime currentDate = LocalDateTime.now();
    int currentMonth = currentDate.getMonth().getValue();
    int maxYear = (currentMonth == 12) ? currentDate.getYear() : (currentDate.getYear() - 1);
    if (year < 2015 || year > maxYear) {
      System.out.println("L'année doit être comprise entre 2015 et l'année actuelle si on est en Décembre, ou l'année dernière sinon.");
      return;
    }

    // Creates the FileName base on year and day
    String fileName = String.format(INPUT_FILE_PATTERN, year, day);
    Path directoryPath = Paths.get(FILE_DIRECTORY);

    // Creates the FILE_DIRECTORY if it doesn't exist
    if (Files.notExists(directoryPath)) {
      try {
        Files.createDirectories(directoryPath);

      } catch (IOException e) {
        log.error("Failed to create FILE_DIRECTORY: {} ", FILE_DIRECTORY, e);
        return;
      }
    }

    // Write the input in the file
    final Path filePath = directoryPath.resolve(fileName);
    try {
      Files.write(filePath, input.getBytes());
      log.info("Input enregistré dans : {}", filePath);
    } catch (IOException e) {
      log.error("Erreur lors de l'enregistrement de l'input : {}", e.getMessage(), e);
    }

  }


  private String readInputFromFile(String filePath) {
    try {
      return new String(Files.readAllBytes(Paths.get(filePath)));
    } catch (IOException e) {
      log.error("Erreur lors de la lecture de l'input : {}", e.getMessage(), e);
      return null;
    }
  }
}

