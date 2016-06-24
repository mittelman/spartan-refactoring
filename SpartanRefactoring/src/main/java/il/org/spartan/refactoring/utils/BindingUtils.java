package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Utils.unless;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.internal.corext.dom.Bindings;

/**
 * Some useful utility functions used for binding manipulations.
 *
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-04-24
 */
@SuppressWarnings("restriction") public class BindingUtils {
  /**
   * @param u current compilation unit
   * @return current package
   */
  public static IPackageBinding getPackage(CompilationUnit u) {
    return u.getPackage().resolveBinding();
  }
  /**
   * @param n an {@link ASTNode}
   * @return the type in which n is placed, or null if there is none
   */
  public static ITypeBinding getClass(ASTNode n) {
    ASTNode p = n;
    while (p != null && !(p instanceof TypeDeclaration))
      p = p.getParent();
    return p == null ? null : ((TypeDeclaration) p).resolveBinding();
  }
  /**
   * Determines whether an invocation of a method is legal in a specific
   * context.
   *
   * @param b a method
   * @param n the context in which the method is invoked
   * @param u current {@link CompilationUnit}
   * @return true iff method is visible from its context
   */
  public static boolean isVisible(IMethodBinding b, ASTNode n, CompilationUnit u) {
    final int ms = b.getModifiers();
    if (Modifier.isPublic(ms))
      return true;
    final ITypeBinding mc = b.getDeclaringClass();
    if (Modifier.isProtected(ms) && mc.getPackage().equals(getPackage(u)))
      return true;
    final ITypeBinding nc = getClass(n);
    return nc != null && nc.equals(mc);
  }
  /**
   * Finds visible method in hierarchy.
   *
   * @param b base type
   * @param mn method name
   * @param bs method parameters
   * @param n original {@link ASTNode} containing the method invocation. Used in
   *          order to determine the context in which the method is being used
   * @param u current {@link CompilationUnit}
   * @return the method's binding if it is visible from context, else null
   */
  public static IMethodBinding getVisibleMethod(ITypeBinding b, String mn, ITypeBinding[] bs, ASTNode n, CompilationUnit u) {
    if (b == null)
      return null;
    final IMethodBinding $ = Bindings.findMethodInHierarchy(b, mn, bs);
    return unless(!isVisible($, n, u), $);
  }
  /**
   * Checks if expression is simple.
   *
   * @param e an expression
   * @return true iff e is simple
   */
  public static boolean isSimple(Expression e) {
    return e instanceof Name || e instanceof NumberLiteral || e instanceof BooleanLiteral || e instanceof CharacterLiteral
        || e instanceof NullLiteral || e instanceof StringLiteral || e instanceof ThisExpression || e instanceof TypeLiteral;
  }
}
