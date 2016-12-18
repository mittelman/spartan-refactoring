package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.utils.FileUtils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.ast.safety.iz.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.utils.*;

/** Collection of definitions and functions that capture some of the quirks of
 * the {@link ASTNode} hierarchy.
 * @author Yossi Gil
 * @since 2014 */
public interface wizard {
  /** This list was generated by manually from {@link #infix2assign}
   * {@link Assignment.Operator} . */
  Map<Assignment.Operator, InfixExpression.Operator> assign2infix = new HashMap<Assignment.Operator, InfixExpression.Operator>() {
    static final long serialVersionUID = 1L;
    {
      put(PLUS_ASSIGN, InfixExpression.Operator.PLUS);
      put(MINUS_ASSIGN, InfixExpression.Operator.MINUS);
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
  Set<String> boxedTypes = new LinkedHashSet<String>() {
    static final long serialVersionUID = 1L;
    {
      for (final String ¢ : new String[] { "Boolean", "Byte", "Character", "Double", "Float", "Integer", "Long", "Short" }) {
        add(¢);
        add("java.lang." + ¢);
      }
    }
  };
  Map<InfixExpression.Operator, InfixExpression.Operator> conjugate = new HashMap<InfixExpression.Operator, InfixExpression.Operator>() {
    static final long serialVersionUID = 1L;
    {
      put(GREATER, LESS);
      put(LESS, GREATER);
      put(GREATER_EQUALS, LESS_EQUALS);
      put(LESS_EQUALS, GREATER_EQUALS);
    }
  };
  PostfixExpression.Operator DECREMENT_POST = PostfixExpression.Operator.DECREMENT;
  PrefixExpression.Operator DECREMENT_PRE = PrefixExpression.Operator.DECREMENT;
  PostfixExpression.Operator INCREMENT_POST = PostfixExpression.Operator.INCREMENT;
  PrefixExpression.Operator INCREMENT_PRE = PrefixExpression.Operator.INCREMENT;
  /** This list was generated by manually editing the original list at
   * {@link Assignment.Operator} . */
  Map<InfixExpression.Operator, Assignment.Operator> infix2assign = new HashMap<InfixExpression.Operator, Assignment.Operator>() {
    static final long serialVersionUID = 1L;
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
  Predicate<Modifier> isAbstract = Modifier::isAbstract;
  Predicate<Modifier> isFinal = Modifier::isFinal;
  Predicate<Modifier> isPrivate = Modifier::isPrivate;
  Predicate<Modifier> isProtected = Modifier::isProtected;
  Predicate<Modifier> isPublic = Modifier::isPublic;
  Predicate<Modifier> isStatic = Modifier::isStatic;
  List<Predicate<Modifier>> visibilityModifiers = as.list(isPublic, isPrivate, isProtected);
  PrefixExpression.Operator MINUS1 = PrefixExpression.Operator.MINUS;
  InfixExpression.Operator MINUS2 = InfixExpression.Operator.MINUS;
  NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
  PrefixExpression.Operator PLUS1 = PrefixExpression.Operator.PLUS;
  InfixExpression.Operator PLUS2 = InfixExpression.Operator.PLUS;
  Set<String> valueTypes = new LinkedHashSet<String>(boxedTypes) {
    static final long serialVersionUID = 1L;
    {
      for (final String ¢ : new String[] { "String" }) {
        add(¢);
        add("java.lang." + ¢);
      }
    }
  };
  @SuppressWarnings({ "unchecked" }) //
  Map<Class<? extends ASTNode>, Integer> //
  classToNodeType = new LinkedHashMap<Class<? extends ASTNode>, Integer>() {
    static final long serialVersionUID = 1L;
    {
      for (int nodeType = 1;; ++nodeType)
        try {
          monitor.debug("Searching for " + nodeType);
          final Class<? extends ASTNode> nodeClassForType = ASTNode.nodeClassForType(nodeType);
          monitor.debug("Found node type number of  " + nodeClassForType);
          put(nodeClassForType, Integer.valueOf(nodeType));
        } catch (final IllegalArgumentException ¢) {
          monitor.debug(this, ¢);
          break;
        } catch (final Exception ¢) {
          monitor.logEvaluationError(this, ¢);
          break;
        }
    }
  };
  InfixExpression.Operator[] infixOperators = { TIMES, DIVIDE, REMAINDER, PLUS2, MINUS2, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED, LESS,
      GREATER, LESS_EQUALS, GREATER_EQUALS, EQUALS, NOT_EQUALS, XOR, AND, OR, CONDITIONAL_AND, CONDITIONAL_OR, };
  Assignment.Operator[] assignmentOperators = { ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, TIMES_ASSIGN, DIVIDE_ASSIGN, BIT_AND_ASSIGN, BIT_OR_ASSIGN,
      BIT_XOR_ASSIGN, REMAINDER_ASSIGN, LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_SIGNED_ASSIGN, RIGHT_SHIFT_UNSIGNED_ASSIGN };
  PrefixExpression.Operator[] prefixOperators = { INCREMENT, DECREMENT, PLUS1, MINUS1, COMPLEMENT, NOT, };
  PostfixExpression.Operator[] postfixOperators = { INCREMENT_POST, DECREMENT_POST };

  static void addImport(final CompilationUnit u, final ASTRewrite r, final ImportDeclaration d) {
    r.getListRewrite(u, CompilationUnit.IMPORTS_PROPERTY).insertLast(d, null);
  }

  static <N extends MethodDeclaration> void addJavaDoc(final N n, final ASTRewrite r, final TextEditGroup g, final String addedJavadoc) {
    final Javadoc j = n.getJavadoc();
    if (j == null)
      r.replace(n,
          r.createGroupNode(new ASTNode[] { r.createStringPlaceholder("/**\n" + addedJavadoc + "\n*/\n", ASTNode.JAVADOC), r.createCopyTarget(n) }),
          g);
    else
      r.replace(j,
          r.createStringPlaceholder(
              (j + "").replaceFirst("\\*\\/$", ((j + "").matches("(?s).*\n\\s*\\*\\/$") ? "" : "\n ") + "* " + addedJavadoc + "\n */"),
              ASTNode.JAVADOC),
          g);
  }

  /** Adds method m to the first type in file.
   * @param fileName
   * @param m */
  static void addMethodToFile(final String fileName, final MethodDeclaration m) {
    try {
      final String str = readFromFile(fileName);
      final Document d = new Document(str);
      final AbstractTypeDeclaration t = findFirst.abstractTypeDeclaration(makeAST.COMPILATION_UNIT.from(d));
      final ASTRewrite r = ASTRewrite.create(t.getAST());
      wizard.addMethodToType(t, m, r, null);
      r.rewriteAST(d, null).apply(d);
      writeToFile(fileName, d.get());
    } catch (IOException | MalformedTreeException | IllegalArgumentException | BadLocationException x2) {
      x2.printStackTrace();
    }
  }

  /** @param d JD
   * @param m JD
   * @param r rewriter
   * @param g edit group, usually null */
  static void addMethodToType(final AbstractTypeDeclaration d, final MethodDeclaration m, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(d, d.getBodyDeclarationsProperty()).insertLast(ASTNode.copySubtree(d.getAST(), m), g);
  }

  /** @param d JD
   * @param s JD
   * @param r rewriter
   * @param g edit group, usually null */
  static void addStatement(final MethodDeclaration d, final ReturnStatement s, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(d.getBody(), Block.STATEMENTS_PROPERTY).insertLast(s, g);
  }

  static Expression applyDeMorgan(final InfixExpression $) {
    final List<Expression> operands = new ArrayList<>();
    for (final Expression ¢ : hop.operands(flatten.of($)))
      operands.add(make.notOf(¢));
    return subject.operands(operands).to(PrefixNotPushdown.conjugate($.getOperator()));
  }

  static int arity(final InfixExpression ¢) {
    return 2 + step.extendedOperands(¢).size();
  }

  static InfixExpression.Operator assign2infix(final Assignment.Operator ¢) {
    return assign2infix.get(¢);
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String asString(final ASTNode ¢) {
    return removeWhites(wizard.body(¢));
  }

  /** Converts a string into an AST, depending on it's form, as determined
   * by @link{GuessedContext.find}.
   * @param p string to convert
   * @return AST, if string is not a valid AST according to any form, then
   *         null */
  static ASTNode ast(final String p) {
    switch (GuessedContext.find(p)) {
      case BLOCK_LOOK_ALIKE:
        return az.astNode(first(statements(az.block(into.s(p)))));
      case COMPILATION_UNIT_LOOK_ALIKE:
        return into.cu(p);
      case EXPRESSION_LOOK_ALIKE:
        return into.e(p);
      case OUTER_TYPE_LOOKALIKE:
        return into.t(p);
      case STATEMENTS_LOOK_ALIKE:
        return into.s(p);
      case METHOD_LOOK_ALIKE:
        return into.m(p);
      default:
        return null;
    }
  }

  static String body(final ASTNode ¢) {
    return tide.clean(¢ + "");
  }

  /** the function checks if all the given assignments have the same left hand
   * side(variable) and operator
   * @param base The assignment to compare all others to
   * @param as The assignments to compare
   * @return <code><b>true</b></code> <em>iff</em>all assignments has the same
   *         left hand side and operator as the first one or false otherwise */
  static boolean compatible(final Assignment base, final Assignment... as) {
    if (hasNull(base, as))
      return false;
    for (final Assignment ¢ : as)
      if (wizard.incompatible(base, ¢))
        return false;
    return true;
  }

  static boolean compatible(final Assignment.Operator o1, final InfixExpression.Operator o2) {
    return infix2assign.get(o2) == o1;
  }

  /** @param o the assignment operator to compare all to
   * @param os A unknown number of assignments operators
   * @return <code><b>true</b></code> <em>iff</em>all the operator are the same
   *         or false otherwise */
  static boolean compatibleOps(final Assignment.Operator o, final Assignment.Operator... os) {
    if (hasNull(o, os))
      return false;
    for (final Assignment.Operator ¢ : os)
      if (¢ == null || ¢ != o)
        return false;
    return true;
  }

  static CompilationUnit compilationUnitWithBinding(final File ¢) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(¢).createAST(null);
  }

  static CompilationUnit compilationUnitWithBinding(final String ¢) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(¢).createAST(null);
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String condense(final ASTNode ¢) {
    return removeWhites(wizard.body(¢));
  }

  /** Makes an opposite operator from a given one, which keeps its logical
   * operation after the node swapping. ¢.¢. "&" is commutative, therefore no
   * change needed. "<" isn'¢ commutative, but it has its opposite: ">=".
   * @param ¢ The operator to flip
   * @return correspond operator - ¢.¢. "<=" will become ">", "+" will stay
   *         "+". */
  static InfixExpression.Operator conjugate(final InfixExpression.Operator ¢) {
    return !wizard.conjugate.containsKey(¢) ? ¢ : wizard.conjugate.get(¢);
  }

  /** @param ns unknown number of nodes to check
   * @return <code><b>true</b></code> <em>iff</em>one of the nodes is an
   *         Expression Statement of type Post or Pre Expression with ++ or --
   *         operator. false if none of them are or if the given parameter is
   *         null. */
  static boolean containIncOrDecExp(final ASTNode... ns) {
    if (ns == null)
      return false;
    for (final ASTNode ¢ : ns)
      if (¢ != null && iz.incrementOrDecrement(¢))
        return true;
    return false;
  }

  /** Compute the "de Morgan" conjugate of the operator present on an
   * {@link InfixExpression}.
   * @param x an expression whose operator is either
   *        {@link Operator#CONDITIONAL_AND} or {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the operator present on the
   *         parameter is {@link Operator#CONDITIONAL_OR}, or
   *         {@link Operator#CONDITIONAL_OR} if this operator is
   *         {@link Operator#CONDITIONAL_AND}
   * @see duplicate#deMorgan(Operator) */
  static InfixExpression.Operator deMorgan(final InfixExpression ¢) {
    return wizard.deMorgan(¢.getOperator());
  }

  /** Compute the "de Morgan" conjugate of an operator.
   * @param o must be either {@link Operator#CONDITIONAL_AND} or
   *        {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the parameter is
   *         {@link Operator#CONDITIONAL_OR} , or
   *         {@link Operator#CONDITIONAL_OR} if the parameter is
   *         {@link Operator#CONDITIONAL_AND}
   * @see wizard#deMorgan(InfixExpression) */
  static InfixExpression.Operator deMorgan(final InfixExpression.Operator ¢) {
    assert iz.deMorgan(¢);
    return ¢.equals(CONDITIONAL_AND) ? CONDITIONAL_OR : CONDITIONAL_AND;
  }

  static String essence(final String codeFragment) {
    return fixTideClean(tide.clean(wizard.removeComments2(codeFragment)));
  }

  /** Find the first matching expression to the given boolean (b).
   * @param b JD,
   * @param xs JD
   * @return first expression from the given list (es) whose boolean value
   *         matches to the given boolean (b). */
  static Expression find(final boolean b, final List<Expression> xs) {
    for (final Expression $ : xs)
      if (iz.booleanLiteral($) && b == az.booleanLiteral($).booleanValue())
        return $;
    return null;
  }

  /** This method fixes a bug from tide.clean which causes ^ to replaced with
   * [^]
   * @param ¢
   * @return */
  static String fixTideClean(final String ¢) {
    return ¢.replaceAll("\\[\\^\\]", "\\^");
  }

  @SuppressWarnings("unchecked") static List<MethodDeclaration> getMethodsSorted(final ASTNode n) {
    final List<MethodDeclaration> $ = new ArrayList<>();
    n.accept(new ASTVisitor() {
      @Override public boolean visit(final MethodDeclaration ¢) {
        $.add(¢);
        return false;
      }
    });
    return (List<MethodDeclaration>) $.stream().sorted((x, y) -> metrics.countStatements(x) > metrics.countStatements(y)
        || metrics.countStatements(x) == metrics.countStatements(y) && x.parameters().size() > y.parameters().size() ? -1 : 1);
  }

  static boolean hasSafeVarags(final MethodDeclaration d) {
    for (final Annotation ¢ : extract.annotations(d))
      if (iz.identifier("SafeVarargs", ¢.getTypeName()))
        return true;
    return false;
  }

  static boolean incompatible(final Assignment a1, final Assignment a2) {
    return hasNull(a1, a2) || !compatibleOps(a1.getOperator(), a2.getOperator()) || !wizard.same(to(a1), to(a2));
  }

  static Operator infix2assign(final InfixExpression.Operator ¢) {
    assert ¢ != null;
    final Operator $ = infix2assign.get(¢);
    assert $ != null : "No assignment equivalent to " + ¢;
    return $;
  }

  /** Determine whether an InfixExpression.Operator is a comparison operator or
   * not
   * @param o JD
   * @return <code><b>true</b></code> <em>iff</em>one of
   *         {@link #InfixExpression.Operator.XOR},
   *         {@link #InfixExpression.Operator.OR},
   *         {@link #InfixExpression.Operator.AND}, and false otherwise */
  static boolean isBitwiseOperator(final InfixExpression.Operator ¢) {
    return in(¢, XOR, OR, AND);
  }

  static boolean isBoxedType(final String typeName) {
    return boxedTypes.contains(typeName);
  }

  /** Determine whether an InfixExpression.Operator is a comparison operator or
   * not
   * @param o JD
   * @return <code><b>true</b></code> <em>iff</em>one of
   *         {@link #InfixExpression.Operator.LESS},
   *         {@link #InfixExpression.Operator.GREATER},
   *         {@link #InfixExpression.Operator.LESS_EQUALS},
   *         {@link #InfixExpression.Operator.GREATER_EQUALS},
   *         {@link #InfixExpression.Operator.EQUALS},
   *         {@link #InfixExpression.Operator.NOT_EQUALS},
   *         {@link #InfixExpression.Operator.CONDITIONAL_OR},
   *         {@link #InfixExpression.Operator.CONDITIONAL_AND} and false
   *         otherwise */
  static boolean isComparison(final InfixExpression.Operator ¢) {
    return in(¢, LESS, GREATER, LESS_EQUALS, GREATER_EQUALS, EQUALS, //
        NOT_EQUALS, CONDITIONAL_OR, CONDITIONAL_AND);
  }

  static boolean isDefaultLiteral(final Expression ¢) {
    return !iz.nullLiteral(¢) && !iz.literal0(¢) && !literal.false¢(¢) && !iz.literal(¢, 0.0) && !iz.literal(¢, 0L);
  }

  /** Determine whether an InfixExpression.Operator is a shift operator or not
   * @param o JD
   * @return <code><b>true</b></code> <em>iff</em>one of
   *         {@link #InfixExpression.Operator.LEFT_SHIFT},
   *         {@link #InfixExpression.Operator.RIGHT_SHIFT_SIGNED},
   *         {@link #InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED} and false
   *         otherwise */
  static boolean isShift(final InfixExpression.Operator ¢) {
    return in(¢, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED);
  }

  static boolean isValueType(final String typeName) {
    return valueTypes.contains(typeName);
  }

  static boolean isValueType(final Type ¢) {
    return isValueType(!haz.binding(¢) ? ¢ + "" : ¢.resolveBinding().getBinaryName());
  }

  static Set<Modifier> matches(final BodyDeclaration d, final Set<Predicate<Modifier>> ms) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier ¢ : extendedModifiers(d))
      if (test(¢, ms))
        $.add((Modifier) ¢);
    return $;
  }

  static Set<Modifier> matches(final List<IExtendedModifier> ms, final Set<Predicate<Modifier>> ps) {
    final Set<Modifier> $ = new LinkedHashSet<>();
    for (final IExtendedModifier ¢ : ms)
      if (test(¢, ps))
        $.add((Modifier) ¢);
    return $;
  }

  static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ms) {
    return matches(extendedModifiers(¢), ms);
  }

  static MethodDeclaration methodWithBinding(final String m) {
    return findFirst.methodDeclaration(makeAST.CLASS_BODY_DECLARATIONS.makeParserWithBinding(m).createAST(null));
  }

  /** Determine whether a node is an infix expression whose operator is
   * non-associative.
   * @param pattern JD
   * @return <code><b>true</b></code> <i>iff</i> the parameter is a node which
   *         is an infix expression whose operator is */
  static boolean nonAssociative(final ASTNode ¢) {
    return nonAssociative(az.infixExpression(¢));
  }

  static boolean nonAssociative(final InfixExpression ¢) {
    return ¢ != null && (in(¢.getOperator(), MINUS2, DIVIDE, REMAINDER, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED)
        || iz.infixPlus(¢) && !type.isNotString(¢));
  }

  /** Parenthesize an expression (if necessary).
   * @param x JD
   * @return a
   *         {@link il.org.spartan.spartanizer.ast.factory.duplicate#duplicate(Expression)}
   *         of the parameter wrapped in parenthesis. */
  static Expression parenthesize(final Expression ¢) {
    return iz.noParenthesisRequired(¢) ? duplicate.of(¢) : make.parethesized(¢);
  }

  Bool resolveBinding = Bool.valueOf(false);

  static void setParserResolveBindings() {
    resolveBinding.inner = true;
  }

  static void setBinding(final ASTParser $) {
    $.setResolveBindings(resolveBinding.inner);
    if (resolveBinding.inner)
      $.setEnvironment(null, null, null, true);
  }

  static ASTParser parser(final int kind) {
    final ASTParser $ = ASTParser.newParser(ASTParser.K_COMPILATION_UNIT);
    setBinding($);
    $.setKind(kind);
    final Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8); // or newer
    // version
    $.setCompilerOptions(options);
    return $;
  }

  static BodyDeclaration prune(final BodyDeclaration $, final Set<Predicate<Modifier>> ms) {
    for (final Iterator<IExtendedModifier> ¢ = extendedModifiers($).iterator(); ¢.hasNext();)
      if (test(¢.next(), ms))
        ¢.remove();
    return $;
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type.
   * @see ASTNode#copySubtree
   * @see ASTRewrite */
  @SuppressWarnings("unchecked") static <N extends ASTNode> N rebase(final N n, final AST t) {
    return (N) copySubtree(t, n);
  }

  /** As {@link elze(ConditionalExpression)} but returns the last else statement
   * in "if - else if - ... - else" statement
   * @param ¢ JD
   * @return last nested else statement */
  static Statement recursiveElze(final IfStatement ¢) {
    for (Statement $ = ¢.getElseStatement();; $ = ((IfStatement) $).getElseStatement())
      if (!($ instanceof IfStatement))
        return $;
  }

  static Set<Predicate<Modifier>> redundancies(final BodyDeclaration ¢) {
    final Set<Predicate<Modifier>> $ = new LinkedHashSet<>();
    if (extendedModifiers(¢) == null || extendedModifiers(¢).isEmpty())
      return $;
    if (iz.enumDeclaration(¢))
      $.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.enumConstantDeclaration(¢)) {
      $.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        $.addAll(as.list(isFinal, isStatic, isAbstract));
    }
    if (iz.interface¢(¢) || ¢ instanceof AnnotationTypeDeclaration)
      $.addAll(as.list(isStatic, isAbstract, isFinal));
    if (iz.isMethodDeclaration(¢) && (iz.private¢(¢) || iz.static¢(¢)))
      $.add(isFinal);
    if (iz.methodDeclaration(¢) && hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    final ASTNode container = hop.containerType(¢);
    if (container == null)
      return $;
    if (iz.abstractTypeDeclaration(container) && iz.final¢(az.abstractTypeDeclaration(container)) && iz.isMethodDeclaration(¢))
      $.add(isFinal);
    if (iz.enumDeclaration(container)) {
      $.add(isProtected);
      if (iz.constructor(¢))
        $.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢))
        $.add(isFinal);
    }
    if (iz.interface¢(container)) {
      $.addAll(visibilityModifiers);
      if (iz.isMethodDeclaration(¢)) {
        $.add(isAbstract);
        $.add(isFinal);
      }
      if (iz.fieldDeclaration(¢)) {
        $.add(isStatic);
        $.add(isFinal);
      }
      if (iz.abstractTypeDeclaration(¢))
        $.add(isStatic);
    }
    if (iz.anonymousClassDeclaration(container)) {
      $.add(isPrivate);
      if (iz.isMethodDeclaration(¢))
        $.add(isFinal);
      if (iz.enumConstantDeclaration(hop.containerType(container)))
        $.add(isProtected);
    }
    if (iz.methodDeclaration(¢) && hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    return $;
  }

  static Set<Modifier> redundants(final BodyDeclaration ¢) {
    return matches(¢, redundancies(¢));
  }

  /** Remove all occurrences of a boolean literal from a list of
   * {@link Expression}¢
   * <p>
   * @param ¢ JD
   * @param xs JD */
  static void removeAll(final boolean ¢, final List<Expression> xs) {
    for (;;) {
      final Expression x = find(¢, xs);
      if (x == null)
        return;
      xs.remove(x);
    }
  }

  static String removeComments(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\n", "\n").replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  static String removeComments2(final String codeFragment) {
    return codeFragment//
        .replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  /** replaces an ASTNode with another
   * @param n
   * @param with */
  static <N extends ASTNode> void replace(final N n, final N with, final ASTRewrite r) {
    r.replace(n, with, null);
  }

  /** Determine whether two nodes are the same, in the sense that their textual
   * representations is identical.
   * <p>
   * Each of the parameters may be <code><b>null</b></code>; a
   * <code><b>null</b></code> is only equal to<code><b>null</b></code>
   * @param n1 JD
   * @param n2 JD
   * @return <code><b>true</b></code> if the parameters are the same. */
  static boolean same(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType() && body(n1).equals(body(n2));
  }

  /** String wise comparison of all the given SimpleNames
   * @param ¢ string to compare all names to
   * @param xs SimplesNames to compare by their string value to cmpTo
   * @return <code><b>true</b></code> <em>iff</em>all names are the same (string
   *         wise) or false otherwise */
  static boolean same(final Expression x, final Expression... xs) {
    for (final Expression ¢ : xs)
      if (!same(¢, x))
        return false;
    return true;
  }

  /** Determine whether two lists of nodes are the same, in the sense that their
   * textual representations is identical.
   * @param ns1 first list to compare
   * @param ns2 second list to compare
   * @return are the lists equal string-wise */
  @SuppressWarnings("boxing") static <¢ extends ASTNode> boolean same(final List<¢> ns1, final List<¢> ns2) {
    if (ns1 == ns2)
      return true;
    if (ns1.size() != ns2.size())
      return false;
    for (final Integer ¢ : range.from(0).to(ns1.size()))
      if (!same(ns1.get(¢), ns2.get(¢)))
        return false;
    return true;
  }

  static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ms) {
    return m instanceof Modifier && test((Modifier) m, ms);
  }

  static boolean test(final Modifier m, final Set<Predicate<Modifier>> ms) {
    for (final Predicate<Modifier> ¢ : ms)
      if (¢.test(m))
        return true;
    return false;
  }

  static String trim(final Object ¢) {
    return ¢ == null || (¢ + "").length() < 35 ? (¢ + "") : (¢ + "").substring(1, 35);
  }
}
