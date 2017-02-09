package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** A class to find all sort all things about a node, generally some small
 * analyses.
 * @author Ori Marcovitch
 * @author Ward Mattar
 * @author Vivian Shehadeh
 * @since 2016 */
public enum analyze {
  ;
  public static Collection<String> dependencies(final ASTNode n) {
    final Collection<String> $ = new HashSet<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final SimpleName node) {
        if (!izMethodName(node))
          $.add(identifier(node));
        return true;
      }

      boolean izMethodName(final Name ¢) {
        return iz.methodInvocation(parent(¢)) && (identifier(az.methodInvocation(parent(¢))) + "").equals(¢ + "")
            || iz.methodDeclaration(parent(¢)) && identifier(az.methodDeclaration(parent(¢))).equals(¢ + "");
      }

      @Override public boolean visit(final QualifiedName node) {
        if (!izMethodName(node))
          $.add(identifier(node));
        return true;
      }
    });
    return $;
  }

  public static Collection<String> dependencies(final Iterable<Expression> arguments) {
    final Set<String> $ = new HashSet<>();
    for (final Expression ¢ : arguments) {
      $.addAll(analyze.dependencies(¢));
      if (iz.name(¢))
        $.add(az.name(¢) + "");
    }
    return new ArrayList<>($).stream().collect(Collectors.toList());
  }

  public static String type(final Name n) {
    final MethodDeclaration m = yieldAncestors.untilContainingMethod().from(n);
    final String $ = m == null ? null : findDeclarationInMethod(n, m);
    return $ != null ? $ : findDeclarationInType(n, yieldAncestors.untilContainingType().from(n));
  }

  private static String findDeclarationInType(final Name n, final AbstractTypeDeclaration d) {
    if (!iz.typeDeclaration(d)) // TODO: Marco support all types of types
      return null;
    for (final FieldDeclaration $ : fieldDeclarations(az.typeDeclaration(d)))
      for (final VariableDeclarationFragment ¢ : fragments($))
        if (step.identifier(¢).equals(n + ""))
          return step.type($) + "";
    return null;
  }

  private static String findDeclarationInMethod(final Name n, final MethodDeclaration d) {
    final Str $ = new Str();
    d.accept(new ASTVisitor() {
      @Override public boolean visit(final SingleVariableDeclaration ¢) {
        if ($.notEmpty() || !step.identifier(¢).equals(n + ""))
          return true;
        $.set(step.type(¢));
        return false;
      }

      @Override public boolean visit(final VariableDeclarationFragment ¢) {
        if ($.notEmpty() || !(step.name(¢) + "").equals(n + ""))
          return true;
        $.set(step.type(¢));
        return false;
      }
    });
    return $.inner();
  }
}
