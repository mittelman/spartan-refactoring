package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-19 */
public enum op {
  ;
  public static PostfixExpression.Operator DECREMENT_POST = PostfixExpression.Operator.DECREMENT;
  public static PrefixExpression.Operator DECREMENT_PRE = PrefixExpression.Operator.DECREMENT;
  public static PostfixExpression.Operator INCREMENT_POST = PostfixExpression.Operator.INCREMENT;
  public static PrefixExpression.Operator INCREMENT_PRE = PrefixExpression.Operator.INCREMENT;
  public static PrefixExpression.Operator MINUS1 = PrefixExpression.Operator.MINUS;
  public static InfixExpression.Operator MINUS2 = InfixExpression.Operator.MINUS;
  public static PrefixExpression.Operator PLUS1 = PrefixExpression.Operator.PLUS;
  public static InfixExpression.Operator PLUS2 = InfixExpression.Operator.PLUS;
  public static PostfixExpression.Operator[] postfix = { op.INCREMENT_POST, op.DECREMENT_POST };
  public static PrefixExpression.Operator[] prefix = { INCREMENT, DECREMENT, PLUS1, MINUS1, COMPLEMENT, NOT, };
  /** This list was generated by manually from {@link #infix2assign}
   * {@link Assignment.Operator} . */
  @SuppressWarnings("serial") static Map<Assignment.Operator, InfixExpression.Operator> assign2infix = new HashMap<>() {
    {
      put(PLUS_ASSIGN, PLUS2);
      put(MINUS_ASSIGN, MINUS2);
      put(TIMES_ASSIGN, TIMES);
      put(DIVIDE_ASSIGN, DIVIDE);
      put(BIT_AND_ASSIGN, AND);
      put(BIT_OR_ASSIGN, OR);
      put(BIT_XOR_ASSIGN, XOR);
      put(REMAINDER_ASSIGN, REMAINDER);
      put(LEFT_SHIFT_ASSIGN, LEFT_SHIFT);
      put(RIGHT_SHIFT_SIGNED_ASSIGN, RIGHT_SHIFT_SIGNED);
      put(RIGHT_SHIFT_UNSIGNED_ASSIGN, RIGHT_SHIFT_UNSIGNED);
    }
  };
  /** This list was generated by manually editing the original list at
   * {@link Assignment.Operator} . */
  static Map<InfixExpression.Operator, Assignment.Operator> infix2assign = new HashMap<>() {
    static final long serialVersionUID = 0x308618DE1596448CL;
    {
      put(InfixExpression.Operator.PLUS, PLUS_ASSIGN);
      put(InfixExpression.Operator.MINUS, MINUS_ASSIGN);
      put(TIMES, TIMES_ASSIGN);
      put(DIVIDE, DIVIDE_ASSIGN);
      put(AND, BIT_AND_ASSIGN);
      put(OR, BIT_OR_ASSIGN);
      put(XOR, BIT_XOR_ASSIGN);
      put(REMAINDER, REMAINDER_ASSIGN);
      put(LEFT_SHIFT, LEFT_SHIFT_ASSIGN);
      put(RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_SIGNED_ASSIGN);
      put(RIGHT_SHIFT_UNSIGNED, RIGHT_SHIFT_UNSIGNED_ASSIGN);
      put(CONDITIONAL_AND, BIT_AND_ASSIGN);
      put(CONDITIONAL_OR, BIT_OR_ASSIGN);
    }
  };
  public static InfixExpression.Operator[] infixOperators = { TIMES, DIVIDE, REMAINDER, op.PLUS2, op.MINUS2, LEFT_SHIFT, RIGHT_SHIFT_SIGNED,
      RIGHT_SHIFT_UNSIGNED, LESS, GREATER, LESS_EQUALS, GREATER_EQUALS, EQUALS, NOT_EQUALS, XOR, AND, OR, CONDITIONAL_AND, CONDITIONAL_OR, };
  public static Assignment.Operator[] assignment = { ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, TIMES_ASSIGN, DIVIDE_ASSIGN, BIT_AND_ASSIGN, BIT_OR_ASSIGN,
      BIT_XOR_ASSIGN, REMAINDER_ASSIGN, LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_SIGNED_ASSIGN, RIGHT_SHIFT_UNSIGNED_ASSIGN };
  static Map<InfixExpression.Operator, InfixExpression.Operator> conjugate = new HashMap<>() {
    static final long serialVersionUID = 0x2025E6F34F4C06EL;
    {
      put(GREATER, LESS);
      put(LESS, GREATER);
      put(GREATER_EQUALS, LESS_EQUALS);
      put(LESS_EQUALS, GREATER_EQUALS);
      put(CONDITIONAL_AND, CONDITIONAL_OR);
      put(CONDITIONAL_OR, CONDITIONAL_AND);
    }
  };

  public static InfixExpression.Operator assign2infix(final Assignment.Operator ¢) {
    return assign2infix.get(¢);
  }
  /** Makes an opposite operator from a given one, which keeps its logical
   * operation after the node swapping. ¢.¢. "&" is commutative, therefore no
   * change needed. "<" isn't commutative, but it has its opposite: ">=".
   * @param ¢ The operator to flip
   * @return correspond operator - ¢.¢. "<=" will become ">", "+" will stay
   *         "+". */
  public static InfixExpression.Operator conjugate(final InfixExpression.Operator ¢) {
    return conjugate.getOrDefault(¢, ¢);
  }
  public static Assignment.Operator infix2assign(final InfixExpression.Operator ¢) {
    assert ¢ != null;
    final Assignment.Operator $ = infix2assign.get(¢);
    assert $ != null : "No assignment equivalent to " + ¢;
    return $;
  }
  /** @param o JD
   * @return whether one of {@link #InfixExpression.Operator.XOR},
   *         {@link #InfixExpression.Operator.OR},
   *         {@link #InfixExpression.Operator.AND}, and false otherwise */
  public static boolean isBitwise(final InfixExpression.Operator ¢) {
    return is.in(¢, XOR, OR, AND);
  }
  /** Determine whether an InfixExpression.Operator is a comparison operator or
   * not
   * @param o JD
   * @return whether one of {@link #InfixExpression.Operator.LESS},
   *         {@link #InfixExpression.Operator.GREATER},
   *         {@link #InfixExpression.Operator.LESS_EQUALS},
   *         {@link #InfixExpression.Operator.GREATER_EQUALS},
   *         {@link #InfixExpression.Operator.EQUALS},
   *         {@link #InfixExpression.Operator.NOT_EQUALS},
   *         {@link #InfixExpression.Operator.CONDITIONAL_OR},
   *         {@link #InfixExpression.Operator.CONDITIONAL_AND} and false
   *         otherwise */
  public static boolean isComparison(final InfixExpression.Operator ¢) {
    return is.in(¢, LESS, GREATER, LESS_EQUALS, GREATER_EQUALS, EQUALS, //
        NOT_EQUALS, CONDITIONAL_OR, CONDITIONAL_AND);
  }
  /** Determine whether an InfixExpression.Operator is a shift operator or not
   * @param o JD
   * @return whether one of {@link #InfixExpression.Operator.LEFT_SHIFT},
   *         {@link #InfixExpression.Operator.RIGHT_SHIFT_SIGNED},
   *         {@link #InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED} and false
   *         otherwise */
  public static boolean isShift(final InfixExpression.Operator ¢) {
    return is.in(¢, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED);
  }
  /** @param o JD
   * @return operator that produces the logical negation of the parameter */
  public static InfixExpression.Operator negate(final InfixExpression.Operator ¢) {
    return ¢.equals(CONDITIONAL_AND) ? CONDITIONAL_OR //
        : ¢.equals(CONDITIONAL_OR) ? CONDITIONAL_AND //
            : ¢.equals(EQUALS) ? NOT_EQUALS
                : ¢.equals(NOT_EQUALS) ? EQUALS
                    : ¢.equals(LESS_EQUALS) ? GREATER
                        : ¢.equals(GREATER) ? LESS_EQUALS //
                            : ¢.equals(GREATER_EQUALS) ? LESS //
                                : ¢.equals(LESS) ? GREATER_EQUALS //
                                    : null;
  }
  /** Determine whether a node is an infix expression whose operator is
   * non-associative.
   * @param pattern JD
   * @return whether the parameter is a node which is an infix expression whose
   *         operator is */
  public static boolean nonAssociative(final ASTNode ¢) {
    return nonAssociative(az.infixExpression(¢));
  }
  static InfixExpression.Operator assign2infix(final Assignment ¢) {
    return assign2infix.get(¢.getOperator());
  }
  /** the function checks if all the given assignments have the same left hand
   * side(variable) and operator
   * @param base The assignment to compare all others to
   * @param as The assignments to compare
   * @return whether all assignments has the same left hand side and operator as
   *         the first one or false otherwise */
  static boolean compatible(final Assignment base, final Assignment... as) {
    return !has.nil(base, as) && Stream.of(as).noneMatch(λ -> op.incompatible(base, λ));
  }
  static boolean compatible(final Assignment a1, final Assignment a2) {
    return !op.incompatible(a1, a2);
  }
  static boolean compatible(final Assignment.Operator o1, final InfixExpression.Operator o2) {
    return infix2assign.get(o2) == o1;
  }
  static InfixExpression.Operator convertToInfix(final Assignment.Operator ¢) {
    return ¢ == Assignment.Operator.BIT_AND_ASSIGN ? InfixExpression.Operator.AND
        : ¢ == Assignment.Operator.BIT_OR_ASSIGN ? InfixExpression.Operator.OR
            : ¢ == Assignment.Operator.BIT_XOR_ASSIGN ? InfixExpression.Operator.XOR
                : ¢ == Assignment.Operator.DIVIDE_ASSIGN ? InfixExpression.Operator.DIVIDE
                    : ¢ == Assignment.Operator.LEFT_SHIFT_ASSIGN ? InfixExpression.Operator.LEFT_SHIFT
                        : ¢ == Assignment.Operator.MINUS_ASSIGN ? InfixExpression.Operator.MINUS
                            : ¢ == Assignment.Operator.PLUS_ASSIGN ? InfixExpression.Operator.PLUS
                                : ¢ == Assignment.Operator.REMAINDER_ASSIGN ? InfixExpression.Operator.REMAINDER
                                    : ¢ == Assignment.Operator.RIGHT_SHIFT_SIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_SIGNED
                                        : ¢ == Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED : null;
  }
  /** Compute the "de Morgan" conjugate of the operator present on an
   * {@link InfixExpression}.
   * @param x an expression whose operator is either
   *        {@link Operator#CONDITIONAL_AND} or {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the operator present on the
   *         parameter is {@link Operator#CONDITIONAL_OR}, or
   *         {@link Operator#CONDITIONAL_OR} if this operator is
   *         {@link Operator#CONDITIONAL_AND}
   * @see copy#deMorgan(Operator) */
  static InfixExpression.Operator deMorgan(final InfixExpression ¢) {
    return deMorgan(¢.getOperator());
  }
  /** Compute the "de Morgan" conjugate of an operator.
   * @param o must be either {@link Operator#CONDITIONAL_AND} or
   *        {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the parameter is
   *         {@link Operator#CONDITIONAL_OR} , or
   *         {@link Operator#CONDITIONAL_OR} if the parameter is
   *         {@link Operator#CONDITIONAL_AND}
   * @see op#deMorgan(InfixExpression) */
  static InfixExpression.Operator deMorgan(final InfixExpression.Operator ¢) {
    assert iz.deMorgan(¢);
    return ¢.equals(CONDITIONAL_AND) ? CONDITIONAL_OR : CONDITIONAL_AND;
  }
  static boolean incompatible(final Assignment a1, final Assignment a2) {
    return has.nil(a1, a2) || !lisp.areEqual(a1.getOperator(), a2.getOperator()) || !wizard.eq(step.to(a1), step.to(a2));
  }
  static boolean nonAssociative(final InfixExpression ¢) {
    return ¢ != null && (notAssociative(¢.getOperator()) || iz.infixPlus(¢) && !type.isNotString(¢));
  }
  static boolean notAssociative(final Operator ¢) {
    return is.in(¢, MINUS2, DIVIDE, REMAINDER, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED);
  }
}
