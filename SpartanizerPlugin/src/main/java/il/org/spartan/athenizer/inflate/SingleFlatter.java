package il.org.spartan.athenizer.inflate;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** A tool for committing a single change to a {@link CompilationUnit}.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2016-12-20 */
public class SingleFlatter {
  private CompilationUnit compilationUnit;
  private OperationsProvider operationsProvider;
  private TextSelection textSelection;

  private SingleFlatter() {}

  /** Creates a new {@link SingleFlatter} for a {@link CompilationUnit}.
   * @param ¢ JD
   * @return new {@link SingleFlatter} */
  public static SingleFlatter in(final CompilationUnit ¢) {
    final SingleFlatter $ = new SingleFlatter();
    $.compilationUnit = ¢;
    return $;
  }

  /** Sets {@link OperationProvider} for this flater.
   * @param ¢ JD
   * @return this flater */
  public SingleFlatter from(final OperationsProvider ¢) {
    operationsProvider = ¢;
    return this;
  }

  /** Sets text selection limits for this flater.
   * @param ¢ JD
   * @return this flater */
  public SingleFlatter limit(final TextSelection ¢) {
    textSelection = ¢;
    return this;
  }

  /** Main operation. Commit a single change to the {@link CompilationUnit}.
   * @param flatterChooser a {@link Function} to choose an {@link Operation} to
   *        make out of a collection of {@link Option}s.
   * @param r JD
   * @param g JD
   * @return true iff a change has been commited */
  @SuppressWarnings({ "unchecked", "rawtypes" }) public boolean go(final Function<List<Operation<?>>, Operation<?>> flatterChooser,
      final ASTRewrite r, final TextEditGroup g) {
    if (compilationUnit == null || operationsProvider == null)
      return false;
    final List<Operation<?>> operations = new LinkedList<>();
    disabling.scan(compilationUnit);
    compilationUnit.accept(new DispatchingVisitor() {
      @SuppressWarnings("synthetic-access") @Override protected <N extends ASTNode> boolean go(final N n) {
        if (!inRange(n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = operationsProvider.getTipper(n);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
        }
        if (w == null)
          return true;
        operations.add(Operation.of(n, w));
        return true;
      }
    });
    if (operations.isEmpty())
      return false;
    final Operation $ = flatterChooser.apply(operations);
    try {
      $.tipper.tip($.node).go(r, g);
    } catch (final Exception ¢) {
      monitor.debug(this, ¢);
    }
    return true;
  }

  /** @param ¢ JD
   * @return true iff node is inside predeclared range */
  private boolean inRange(final ASTNode ¢) {
    final int $ = ¢.getStartPosition();
    return textSelection == null || $ >= textSelection.getOffset() && $ < textSelection.getLength() + textSelection.getOffset();
  }

  /* TODO Raviv: write ***Javadoc*** according to conventions --or
   *
   * @param startChar1 - starting char of first interval
   *
   * @param lenth1 - length of first interval
   *
   * @param startChar2 - starting char of second interval
   *
   * @param length2 - length of second interval SPARTANIZED - should use
   * Athenizer one day to understand it */
  static boolean intervalsIntersect(final int startChar1, final int length1, final int startChar2, final int length2) {
    return length1 != 0 && length2 != 0 && (startChar1 < startChar2 ? length1 + startChar1 > startChar2
        : startChar1 != startChar2 ? length2 + startChar2 > startChar1 : length1 > 0 && length2 > 0);
  }

  /** describes a single change operation, containing both an {@link ASTNode}
   * and a matching {@link Tipper}.
   * @param <N> JD
   * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
   * @since 2016-12-20 */
  protected static class Operation<N extends ASTNode> {
    public final N node;
    public final Tipper<N> tipper;

    private Operation(final N n, final Tipper<N> t) {
      node = n;
      tipper = t;
    }

    /** [[SuppressWarningsSpartan]] */
    public static <N extends ASTNode> Operation<N> of(final N node, final Tipper<N> tipper) {
      return new Operation<>(node, tipper);
    }
  }
}
