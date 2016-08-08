package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Restructure.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.*;

/** A {@link Wring} to change name of unused variable to double underscore "__"
 * TODO Ori: (maybe) inherent VariableChangeName instead of
 * ReplaceCurrentNodeExclude
 * @author Ori Roth <code><ori.rothh [at] gmail.com></code>
 * @since 2016-05-08 */
@SuppressWarnings("javadoc") public class MethodRenameUnusedVariableToUnderscore extends ReplaceCurrentNodeExclude<SingleVariableDeclaration> {
  public static class IsUsed extends ASTVisitor {
    boolean c = true;
    String n;
    public IsUsed(final SimpleName sn) {
      n = sn.getIdentifier();
    }
    public IsUsed(final String sn) {
      n = sn;
    }
    public boolean conclusion() {
      return !c;
    }
    @Override public boolean preVisit2(@SuppressWarnings("unused") final ASTNode __) {
      return c;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final AnnotationTypeDeclaration __) {
      return false;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final AnonymousClassDeclaration __) {
      return false;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final EnumDeclaration __) {
      return false;
    }
    @Override public boolean visit(final SimpleName sn) {
      if (n.equals(sn.getIdentifier()))
        c = false;
      return c;
    }
    @Override public final boolean visit(@SuppressWarnings("unused") final TypeDeclaration __) {
      return false;
    }
  }
  // true iff renaming annotated variables only
  final static boolean BY_ANNOTATION = true;
  public static boolean isUsed(final MethodDeclaration d, final SimpleName n) {
    final IsUsed u = new IsUsed(n);
    d.getBody().accept(u);
    return u.conclusion();
  }
  @SuppressWarnings("unchecked") public static boolean suppressedUnused(final SingleVariableDeclaration d) {
    for (final IExtendedModifier m : (Iterable<IExtendedModifier>) d.modifiers())
      if (m instanceof SingleMemberAnnotation && "SuppressWarnings".equals(((SingleMemberAnnotation) m).getTypeName().toString())) {
        final Expression e = ((SingleMemberAnnotation) m).getValue();
        if (e instanceof StringLiteral)
          return "unused".equals(((StringLiteral) e).getLiteralValue());
        for (final Expression x : (Iterable<Expression>) ((ArrayInitializer) ((SingleMemberAnnotation) m).getValue()).expressions())
          return x instanceof StringLiteral && "unused".equals(((StringLiteral) x).getLiteralValue());
        break;
      }
    return false;
  }
  private static final String unusedVariableName() {
    return "__";
  }
  @Override String description(final SingleVariableDeclaration d) {
    return "Change name of unused variable " + d.getName().getIdentifier() + " to __";
  }
  @SuppressWarnings("unchecked") @Override ASTNode replacement(final SingleVariableDeclaration n, final ExclusionManager m) {
    final ASTNode p = n.getParent();
    if (p == null || !(p instanceof MethodDeclaration))
      return null;
    final MethodDeclaration d = (MethodDeclaration) p;
    for (final SingleVariableDeclaration svd : expose.parameters(d))
      if (unusedVariableName().equals(svd.getName().getIdentifier()))
        return null;
    if (BY_ANNOTATION && !suppressedUnused(n) || isUsed(d, n.getName()))
      return null;
    if (m != null)
      for (final SingleVariableDeclaration svd : expose.parameters(d))
        if (!n.equals(svd))
          m.exclude(svd);
    final SingleVariableDeclaration $ = n.getAST().newSingleVariableDeclaration();
    $.setName(n.getAST().newSimpleName(unusedVariableName()));
    $.setFlags($.getFlags());
    $.setInitializer($.getInitializer());
    $.setType(Funcs.duplicate(n.getType()));
    duplicateModifiers(expose.modifiers(n), expose.modifiers($));
    return $;
  }
  @Override WringGroup wringGroup() {
    return WringGroup.RENAME_PARAMETERS;
  }
}
