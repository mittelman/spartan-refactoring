package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to convert <code>if (x) a += 3; else a += 9;</code> into
 * <code>a += x ? 3 : 9;</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class IfAssignToFooElseAssignToFoo extends Wring.ReplaceCurrentNode<IfStatement> {
  @Override Statement replacement(final IfStatement s) {
    final Assignment then = assignment(then(s));
    final Assignment elze = assignment(elze(s));
    return !compatible(then, elze) ? null
        : Subject.pair(left(then), Subject.pair(right(then), right(elze)).toCondition(s.getExpression()))
            .toStatement(then.getOperator());
  }
  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && compatible(extract.assignment(then(s)), assignment(elze(s)));
  }
  @Override String description(final IfStatement s) {
    return "Consolidate assignments to " + left(extract.assignment(then(s)));
  }
  @Override WringGroup wringGroup() {
    return WringGroup.IF_TO_TERNARY;
  }
}