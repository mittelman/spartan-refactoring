package org.spartan.refactoring.wring;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.CONDITIONAL_AND;
import static org.spartan.refactoring.utils.Funcs.asIfStatement;
import static org.spartan.refactoring.utils.Funcs.duplicate;
import static org.spartan.refactoring.utils.Funcs.then;
import static org.spartan.refactoring.wring.Wrings.emptyElse;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.Extract;
import org.spartan.refactoring.utils.Rewrite;
import org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} to convert <code>if (x) if (a) f(); </code> into
 * <code>if (x && a)  f();</code>
 *
 * @author Yossi Gil
 * @since 2015-09-01
 */
public final class IfThenIfThenNoElseNoElse extends Wring<IfStatement> {
  @Override String description(@SuppressWarnings("unused") final IfStatement _) {
    return "Merge conditionals of nested if staement";
  }
  @Override boolean eligible(@SuppressWarnings("unused") final IfStatement _) {
    return true;
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return make(s) != null;
  }
  @Override Rewrite make(final IfStatement s, final ExclusionManager exclude) {
    if (!emptyElse(s))
      return null;
    final IfStatement then = asIfStatement(Extract.singleThen(s));
    if (then == null || !emptyElse(then))
      return null;
    if (exclude != null)
      exclude.exclude(then);
    return new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        collapse(Wrings.blockIfNeeded(s, r, g), r, g);
      }
    };
  }
  static void collapse(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    final IfStatement then = asIfStatement(Extract.singleThen(s));
    final InfixExpression e = Subject.pair(s.getExpression(), then.getExpression()).to(CONDITIONAL_AND);
    r.replace(s.getExpression(), e, g);
    r.replace(then, duplicate(then(then)), g);
  }
  @Override Rewrite make(final IfStatement n) {
    return make(n, null);
  }
}