package adventofcode.aoc2024.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PuzzleSolverClass {

  String value(); // stores the solver's id (example "2024-1")
}
