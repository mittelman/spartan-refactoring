package il.org.spartan.spartanizer.tippers;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import fluent.ly.note;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.engine.type.Primitive.Certain;

/** Evaluate the addition of numbers according to the following rules {@code
 * int + int --> int
 * double + double --> double
 * long + long --> long
 * int + double --> double
 * int + long --> long
 * long + double --> double
 * }
 * @author Dor Ma'ayan
 * @since 2016 */
public final class InfixAdditionEvaluate extends $EvaluateInfixExpression {
  private static final long serialVersionUID = 0x95F6C4DCCBB43E4L;

  @Override @SuppressWarnings("boxing") double evaluateDouble(final List<Expression> xs) {
    double $ = 0;
    try {
      $ = xs.stream().map(az.throwing::double¢).reduce((x, y) -> x + y).get();
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return $;
  }
  @Override int evaluateInt(final List<Expression> xs) {
    int $ = 0;
    try {
      for (final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE || type.of(¢) == Certain.LONG)
          throw new NumberFormatException();
        $ += az.throwing.int¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return $;
  }
  @Override long evaluateLong(final List<Expression> xs) {
    long $ = 0;
    try {
      for (final Expression ¢ : xs) {
        if (type.of(¢) == Certain.DOUBLE)
          throw new NumberFormatException();
        $ += az.throwing.long¢(¢);
      }
    } catch (final NumberFormatException ¢) {
      note.bug(this, ¢);
    }
    return $;
  }
  @Override String operation() {
    return "addition";
  }
  @Override Operator operator() {
    return il.org.spartan.spartanizer.ast.navigate.op.PLUS2;
  }
}
