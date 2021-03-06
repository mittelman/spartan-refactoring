package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.utils.Proposition;

/** A non-empty for loop pattern
 * @author Raviv Rachmiel <tt>raviv.rachmiel@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class NonEmptyForEach extends ForStatementPattern {
  private static final long serialVersionUID = -0x42C652C483D5421AL;
  protected Statement lastStatement;

  public NonEmptyForEach() {
    andAlso(new Proposition.Singleton("Applicable only on non empty for loops", () -> {
      lastStatement = extract.lastStatement(current);
      return !iz.emptyStatement(lastStatement);
    }));
  }
}
