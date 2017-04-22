package il.org.spartan.spartanizer.patterns;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-22 */
public final class ReturnDeadAssignment extends ReturnValuePattern {
  private static final long serialVersionUID = 2597846433179905741L;
  private Assignment assignment;
  private SimpleName to;
  private Operator operator;
  private Expression from;

  public ReturnDeadAssignment() {
    super//
    .andAlso("Returned value must be an assignment", //
        () -> assignment = az.assignment(value) //
    ).andAlso("Assigment is to a variable", //
        () -> to = az.simpleName(to(assignment)) //
    ).andAlso("Variable is a local variable", //
        () -> Environment.of(methodDeclaration).nest.doesntHave(to + "")//
    ).andAlso("Extract from", //
        () -> from = from(assignment) //
    ).andAlso("Extract operator", //
        () -> operator = assignment.getOperator() //
    );
  }

  @Override public Examples examples() {
    return convert("int f() {int a = 2; return a = 3;}")//
        .to("int f() {int a = 2; return 3;}")//
        .convert("int f() {int a = 2; return a += 3;}").to("int f() {int a = 2; return a + 3;}")//
        .convert("int f(int a) { return a = 3;}")//
        .to("int f(int a) {return a;}").convert("int f() {int a = 2; return a = 3;}")//
        .to("int f() {int a = 2; return 3;}");
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    if (operator == ASSIGN)
      r.replace(assignment, copy.of(from), g);
    else
      r.replace(assignment, subject.pair(to, from).to(op.assign2infix(operator)), g);
    return r;
  }
}
