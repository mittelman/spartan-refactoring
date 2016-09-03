package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.assemble.Plant.*;
import static il.org.spartan.refactoring.ast.iz.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** Replace <code>X-0</code> by <code>X</code> and <code>0-X</code> by
 * <code>-X<code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixSubtractionZero extends ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  @Override String description(final InfixExpression x) {
    return "Remove subtraction of 0 in " + x;
  }

  private static boolean isZero(final Expression x){
    return x.getNodeType()==ASTNode.NUMBER_LITERAL && ("0".equals(((NumberLiteral) x).getToken()));
  }
  
  @Override ASTNode replacement(final InfixExpression x) {
    return x.getOperator() != MINUS || !isZero(x.getRightOperand()) && !isZero(x.getLeftOperand()) ? null : go(x);
  }

  private static ASTNode go(final InfixExpression x) {
    return x.hasExtendedOperands() ? plant(go(hop.operands(x))).into(step.parent(x))
        : literal0(step.left(x)) ? plant(il.org.spartan.refactoring.assemble.make.minus(step.right(x))).into(step.parent(x)) //
            : literal0(step.right(x)) ? plant(step.left(x)).into(step.parent(x)) //
                : null;
  }

  private static Expression go(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>(xs);
    if (literal0(lisp.first($))) {
      $.remove(0);
      $.set(0, il.org.spartan.refactoring.assemble.make.minus(lisp.first($)));
    } else
      for (int i = 1, size = $.size(); i < size; ++i)
        if (literal0($.get(i))) {
          $.remove(i);
          break;
        }
    return subject.operands($).to(MINUS);
  }
}
