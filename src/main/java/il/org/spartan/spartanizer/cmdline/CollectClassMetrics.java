package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.collections.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** Collect basic metrics of files (later on, maybe change to classes)
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Oct 3, 2016 */
enum CollectClassMetrics {
  ;
  private static final String OUTPUT = "/tmp/commons-lang-halstead.CSV";
  private static final CSVStatistics output = init();

  public static void main(@NotNull final String[] where) {
    go(where.length != 0 ? where : as.array("."));
    System.err.println("Your output should be here: " + output.close());
  }

  @NotNull
  static CompilationUnit spartanize(@NotNull final CompilationUnit before) {
    @NotNull final Trimmer tr = new Trimmer();
    assert tr != null;
    @NotNull final ICompilationUnit $ = (ICompilationUnit) before.getJavaElement();
    tr.setICompilationUnit($);
    assert $ != null;
    try {
      tr.checkAllConditions(null);
    } catch (@NotNull OperationCanceledException | CoreException ¢) {
      ¢.printStackTrace();
    }
    return before;
  }

  private static void go(@NotNull final File f) {
    try {
      // This line is going to give you trouble if you process class by class.
      output.put("File", f.getName());
      go(FileUtils.read(f));
    } catch (@NotNull final IOException ¢) {
      System.err.println(¢.getMessage());
    }
  }

  private static void go(@NotNull final String javaCode) {
    output.put("Characters", javaCode.length());
    report("Before-", (CompilationUnit) makeAST.COMPILATION_UNIT.from(javaCode));
  }

  private static void go(final String... where) {
    new FilesGenerator(".java").from(where).forEach(CollectClassMetrics::go);
  }

  @NotNull
  private static CSVStatistics init() {
    try {
      return new CSVStatistics(OUTPUT, "property");
    } catch (@NotNull final IOException ¢) {
      throw new RuntimeException(OUTPUT, ¢);
    }
  }

  /** fault, what happens if we have many classes in the same file? Also, we do
   * not want to count imports, and package instructions. Write a method that
   * finds all classes, which could be none, at the upper level, and collect on
   * these. Note that you have to print the file name which is common to all
   * classes. Turn this if you like into a documentation
   * @param string */
  private static void report(final String prefix, @NotNull final CompilationUnit ¢) {
    // TODO Matteo: make sure that the counting does not include comments.
    // Do
    // this by adding stuff to the metrics suite.
    output.put(prefix + "Length", ¢.getLength());
    output.put(prefix + "Count", count.nodes(¢));
    output.put(prefix + "Non whites", count.nonWhiteCharacters(¢));
    output.put(prefix + "Condensed size", metrics.condensedSize(¢));
    output.put(prefix + "Lines", count.lines(¢));
    output.put(prefix + "Dexterity", metrics.dexterity(¢));
    output.put(prefix + "Leaves", metrics.leaves(¢));
    output.put(prefix + "Nodes", metrics.nodes(¢));
    output.put(prefix + "Internals", metrics.internals(¢));
    output.put(prefix + "Vocabulary", metrics.vocabulary(¢));
    output.put(prefix + "Literacy", metrics.literacy(¢));
    output.put(prefix + "Imports", count.imports(¢));
    output.put(prefix + "No Imports", count.noimports(¢));
    output.nl();
  }
}
