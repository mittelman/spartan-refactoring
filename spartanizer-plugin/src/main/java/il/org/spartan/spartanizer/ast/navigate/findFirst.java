package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.spartanizer.ast.navigate.step.arguments;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.Wrapper;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-28 */
public interface findFirst {
  /** @param ¢ JD
   */
  static AbstractTypeDeclaration abstractTypeDeclaration(final ASTNode ¢) {
    return instanceOf(AbstractTypeDeclaration.class).in(¢);
  }
  /** Search for an {@link AssertStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link AssertStatement} found in an {@link ASTNode n}, or
   *         {@code null} if there is no such statement. */
  static AssertStatement assertStatement(final ASTNode ¢) {
    return instanceOf(AssertStatement.class).in(¢);
  }
  /** Search for an {@link ForStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link ForStatement} found in an {@link ASTNode n}, or
   *         {@code null} if there is no such statement. */
  static ForStatement forStatement(final ASTNode ¢) {
    return instanceOf(ForStatement.class).in(¢);
  }
  /** Search for an {@link IfStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link IfStatement} found in an {@link ASTNode n}, or
   *         {@code null} if there is no such statement. */
  static IfStatement ifStatement(final ASTNode ¢) {
    return instanceOf(IfStatement.class).in(¢);
  }
  /** Find the first {@link InfixExpression} representing an addition, under a
   * given node, as found in the usual visitation order.
   * @param n JD
   * @return first {@link InfixExpression} representing an addition under the
   *         parameter given node, or {@code null} if no such value
   *         could be found. */
  static InfixExpression infixPlus(final ASTNode n) {
    final Wrapper<InfixExpression> $ = new Wrapper<>();
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final InfixExpression ¢) {
        if ($.get() != null)
          return false;
        if (¢.getOperator() != op.PLUS2)
          return true;
        $.set(¢);
        return false;
      }
    });
    return $.get();
  }

  @FunctionalInterface
  interface In<N extends ASTNode> {
    N in(ASTNode n);
  }

  static <N extends ASTNode> In<N> instanceOf(final Class<N> c) {
    return n -> {
      if (n == null)
        return null;
      final Wrapper<N> $ = new Wrapper<>();
      n.accept(new ASTVisitor(true) {
        @Override @SuppressWarnings("unchecked") public boolean preVisit2(final ASTNode ¢) {
          if ($.get() != null)
            return false;
          if (¢.getClass() != c && !c.isAssignableFrom(¢.getClass()))
            return true;
          $.set((N) ¢);
          assert $.get() == ¢;
          return false;
        }
      });
      return $.get();
    };
  }
  /** @param ¢ JD
   */
  static TypeDeclaration typeDeclaration(final ASTNode ¢) {
    return instanceOf(TypeDeclaration.class).in(¢);
  }
  /** Return the first {@link VariableDeclarationFragment} encountered in a
   * visit of the tree rooted a the parameter.
   * @param pattern JD
   * @return first such node encountered in a visit of the tree rooted a the
   *         parameter, or {@code null} */
  static VariableDeclarationFragment variableDeclarationFragment(final ASTNode ¢) {
    return instanceOf(VariableDeclarationFragment.class).in(¢);
  }
  /** Search for an {@link WhileStatement} in the tree rooted at an
   * {@link ASTNode}.
   * @param pattern JD
   * @return first {@link WhileStatement} found in an {@link ASTNode n}, or
   *         {@code null} if there is no such statement. */
  static WhileStatement whileStatement(final ASTNode ¢) {
    return instanceOf(WhileStatement.class).in(¢);
  }
  /** @param ¢ JD
   */
  static ExpressionStatement expressionStatement(final ASTNode ¢) {
    return instanceOf(ExpressionStatement.class).in(¢);
  }
  /** @param ¢ JD
   */
  static Block block(final ASTNode ¢) {
    return instanceOf(Block.class).in(¢);
  }
  /** @param ¢ JD
   */
  static InfixExpression infixExpression(final ASTNode ¢) {
    return instanceOf(InfixExpression.class).in(¢);
  }
  /** @param ¢ JD
   */
  static TryStatement tryStatement(final ASTNode ¢) {
    return instanceOf(TryStatement.class).in(¢);
  }
  /** @param ¢ JD
   */
  static BooleanLiteral booleanLiteral(final ASTNode ¢) {
    return instanceOf(BooleanLiteral.class).in(¢);
  }
  /** @param ¢ JD
   */
  static FieldAccess fieldAccess(final ASTNode ¢) {
    return instanceOf(FieldAccess.class).in(¢);
  }
  static Name name(final ASTNode ¢) {
    return instanceOf(Name.class).in(¢);
  }
  static VariableDeclarationStatement variableDeclarationStatement(final ASTNode ¢) {
    return instanceOf(VariableDeclarationStatement.class).in(¢);
  }
  static ConditionalExpression conditionalArgument(final MethodInvocation ¢) {
    return arguments(¢).stream().filter(iz::conditionalExpression).map(az::conditionalExpression).findFirst().orElse(null);
  }
  static ASTNode statement(final ASTNode ¢) {
    return instanceOf(Statement.class).in(¢);
  }
  static ASTNode returnStatement(final ASTNode ¢) {
    return instanceOf(ReturnStatement.class).in(¢);
  }
}
