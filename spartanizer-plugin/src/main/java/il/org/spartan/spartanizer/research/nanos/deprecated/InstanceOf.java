package il.org.spartan.spartanizer.research.nanos.deprecated;

import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.misc;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.yieldAncestors;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.research.nanos.methods.TypeChecker;
import il.org.spartan.spartanizer.tipping.Tip;

/** Catches instanceOf occurrences and replaces them with iz.X Even creates a
 * suitable method
 * @author Ori Marcovitch
 * @since 2016 */
@Deprecated
class InstanceOf extends NanoPatternTipper<InstanceofExpression> {
  private static final long serialVersionUID = 0x7A610415E281489BL;
  static final TypeChecker c = new TypeChecker();

  @Override public boolean canTip(final InstanceofExpression ¢) {
    if (!(type(¢) instanceof SimpleType))
      return false;
    final MethodDeclaration $ = yieldAncestors.untilContainingMethod().from(¢);
    final Javadoc j = $.getJavadoc();
    return (j == null || !(j + "").contains(c.tag())) && c.cantTip($) && !(type(¢) + "").contains(".");
  }
  @Override public Tip pattern(final InstanceofExpression ¢) {
    return new Tip(description(¢), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(!iz.parenthesizedExpression(¢.getParent()) ? ¢ : ¢.getParent(), make.ast(izMethodName(¢) + "(" + step.left(¢) + ")"), g);
        if (!izMethodExist(¢))
          addizMethod(¢, r, g);
      }
    };
  }
  static String izMethodName(final InstanceofExpression ¢) {
    return "iz" + type(¢);
  }
  static boolean izMethodExist(final InstanceofExpression ¢) {
    return step.methods(containingType(¢)).stream().filter(λ -> izMethodName(¢).equals(λ.getName() + "") && booleanType(step.returnType(λ)))
        .count() != 0;
  }
  private static boolean booleanType(final Type returnType) {
    return "boolean".equals(returnType + "");
  }
  static void addizMethod(final InstanceofExpression ¢, final ASTRewrite r, final TextEditGroup g) {
    misc.addMethodToType(containingType(¢), newIzMethod(¢), r, g);
  }
  private static MethodDeclaration newIzMethod(final InstanceofExpression ¢) {
    return az.methodDeclaration(make.ast("static boolean " + izMethodName(¢) + "(Object ¢){ return ¢ instanceof " + type(¢) + ";}"));
  }
  private static AbstractTypeDeclaration containingType(final InstanceofExpression ¢) {
    // smaybe in the future change to iz.java in package which will
    // be created automatically...
    return yieldAncestors.untilContainingType().from(¢);
  }
  @Override public String description(@SuppressWarnings("unused") final InstanceofExpression __) {
    return "replace instanceof with iz()";
  }
}
