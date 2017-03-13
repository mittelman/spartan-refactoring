package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;
import static il.org.spartan.utils.FileUtils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.ast.safety.iz.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;

/** Collection of definitions and functions that capture some of the quirks of
 * the {@link ASTNode} hierarchy.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2014 */
@SuppressWarnings("OverlyComplexClass")
public interface wizard {
  PostfixExpression.Operator DECREMENT_POST = PostfixExpression.Operator.DECREMENT;
  PrefixExpression.Operator DECREMENT_PRE = PrefixExpression.Operator.DECREMENT;
  PostfixExpression.Operator INCREMENT_POST = PostfixExpression.Operator.INCREMENT;
  PrefixExpression.Operator INCREMENT_PRE = PrefixExpression.Operator.INCREMENT;
  PrefixExpression.Operator MINUS1 = PrefixExpression.Operator.MINUS;
  InfixExpression.Operator MINUS2 = InfixExpression.Operator.MINUS;
  PrefixExpression.Operator PLUS1 = PrefixExpression.Operator.PLUS;
  InfixExpression.Operator PLUS2 = InfixExpression.Operator.PLUS;
  String[] keywords = { //
      "synchronized", //
      "instanceof", //
      "implements", //
      "protected", //
      "interface", //
      "transient", //
      "abstract", //
      "volatile", //
      "strictfp", //
      "continue", //
      "boolean", //
      "package", //
      "private", //
      "extends", //
      "finally", //
      "default", //
      "double", //
      "return", //
      "native", //
      "public", //
      "static", //
      "throw", //
      "switch", //
      "import", //
      "throws", //
      "assert", //
      "const", //
      "catch", //
      "class", //
      "false", //
      "while", //
      "float", //
      "final", //
      "super", //
      "break", //
      "short", //
      "byte", //
      "case", //
      "long", //
      "null", //
      "goto", //
      "this", //
      "true", //
      "void", //
      "char", //
      "else", //
      "enum", //
      "new", //
      "int", //
      "try", //
      "for", //
      "do", //
      "if", //
  };
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
  Predicate<Modifier> isAbstract = Modifier::isAbstract;
  Predicate<Modifier> isFinal = Modifier::isFinal;
  Predicate<Modifier> isPrivate = Modifier::isPrivate;
  Predicate<Modifier> isProtected = Modifier::isProtected;
  Predicate<Modifier> isPublic = Modifier::isPublic;
  Predicate<Modifier> isStatic = Modifier::isStatic;
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
  List<Predicate<Modifier>> visibilityModifiers = as.list(isPublic, isPrivate, isProtected);
  IProgressMonitor nullProgressMonitor = new NullProgressMonitor();
  Collection<String> valueTypes = new LinkedHashSet<String>(boxedTypes) {
    static final long serialVersionUID = 1L;
    {
      for (final String ¢ : new String[] { "String" }) {
        add(¢);
        add("java.lang." + ¢);
      }
    }
  };
  @SuppressWarnings("unchecked") Map<Class<? extends ASTNode>, Integer> //
  classToNodeType = new LinkedHashMap<Class<? extends ASTNode>, Integer>() {
    static final long serialVersionUID = 1L;
    {
      for (int nodeType = 1;; ++nodeType)
        try {
          // monitor.debug("Found node type number of " + nodeClassForType);
          put(ASTNode.nodeClassForType(nodeType), Integer.valueOf(nodeType));
        } catch (@SuppressWarnings("unused") final IllegalArgumentException ¢) {
          // We must suffer this exception; no other way to find the first
          // unused node type
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
  Bool resolveBinding = Bool.valueOf(false);

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
      final IDocument d = new Document(str);
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

  static <N extends ASTNode> List<? extends ASTNode> addRest(final List<ASTNode> $, final N n, final List<N> ns) {
    if (ns == null)
      return $;
    boolean add = false;
    for (final ASTNode x : ns)
      if (add)
        $.add(x);
      else
        add = x == n;
    return $;
  }

  /** @param d JD
   * @param s JD
   * @param r rewriter
   * @param g edit group, usually null */
  static void addStatement(final MethodDeclaration d, final ReturnStatement s, final ASTRewrite r, final TextEditGroup g) {
    r.getListRewrite(step.body(d), Block.STATEMENTS_PROPERTY).insertLast(s, g);
  }

  static Expression applyDeMorgan(final InfixExpression $) {
    return subject.operands(hop.operands(flatten.of($)).stream().map(make::notOf).collect(toList())).to(wizard.negate(operator($)));
  }

  static InfixExpression.Operator assign2infix(final Assignment ¢) {
    return assign2infix.get(¢.getOperator());
  }

  static InfixExpression.Operator assign2infix(final Assignment.Operator ¢) {
    return assign2infix.get(¢);
  }

  /** Converts a string into an AST, depending on it's form, as determined
   * by @link{GuessedContext.find}.
   * @param javaSnippet string to convert
   * @return AST, if string is not a valid AST according to any form, then
   *         null */
  static ASTNode ast(final String javaSnippet) {
    switch (GuessedContext.find(javaSnippet)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return into.cu(javaSnippet);
      case EXPRESSION_LOOK_ALIKE:
        return into.e(javaSnippet);
      case METHOD_LOOK_ALIKE:
        return into.m(javaSnippet);
      case OUTER_TYPE_LOOKALIKE:
        return into.t(javaSnippet);
      case STATEMENTS_LOOK_ALIKE:
        return into.s(javaSnippet);
      case BLOCK_LOOK_ALIKE:
        return az.astNode(first(statements(az.block(into.s(javaSnippet)))));
      default:
        for (final int guess : as.intArray(ASTParser.K_EXPRESSION, ASTParser.K_STATEMENTS, ASTParser.K_CLASS_BODY_DECLARATIONS,
            ASTParser.K_COMPILATION_UNIT)) {
          final ASTParser p = wizard.parser(guess);
          p.setSource(javaSnippet.toCharArray());
          final ASTNode $ = p.createAST(wizard.nullProgressMonitor);
          if (valid($))
            return $;
        }
        return null;
    }
  }

  static ASTNode commonAncestor(final ASTNode n1, final ASTNode n2) {
    final List<ASTNode> ns1 = ancestors.path(n1), ns2 = ancestors.path(n2);
    for (int $ = 0; $ < Math.min(ns1.size(), ns2.size()); ++$)
      if (ns1.get($) == ns2.get($))
        return ns1.get($);
    return null;
  }

  /** the function checks if all the given assignments have the same left hand
   * side(variable) and operator
   * @param base The assignment to compare all others to
   * @param as The assignments to compare
   * @return whetherall assignments has the same left hand side and operator as
   *         the first one or false otherwise */
  static boolean compatible(final Assignment base, final Assignment... as) {
    return !hasNull(base, as) && Stream.of(as).noneMatch(λ -> incompatible(base, λ));
  }

  static boolean compatible(final Assignment a1, final Assignment a2) {
    return !incompatible(a1, a2);
  }

  static boolean compatible(final Assignment.Operator o1, final InfixExpression.Operator o2) {
    return infix2assign.get(o2) == o1;
  }

  /** @param o the assignment operator to compare all to
   * @param os A unknown number of assignments operators
   * @return whetherall the operator are the same or false otherwise */
  static boolean compatibleOps(final Assignment.Operator o, final Assignment.Operator... os) {
    return !hasNull(o, os) && Stream.of(os).allMatch(λ -> λ != null && λ == o);
  }

  static CompilationUnit compilationUnitWithBinding(final File ¢) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(¢).createAST(null);
  }

  static CompilationUnit compilationUnitWithBinding(final String ¢) {
    return (CompilationUnit) makeAST.COMPILATION_UNIT.makeParserWithBinding(¢).createAST(null);
  }

  static <T> String completionIndex(final List<T> ts, final T t) {
    final String $ = ts.size() + "";
    String i = ts.indexOf(t) + 1 + "";
    while (i.length() < $.length())
      i = " " + i;
    return i + "/" + $;
  }

  /** Makes an opposite operator from a given one, which keeps its logical
   * operation after the node swapping. ¢.¢. "&" is commutative, therefore no
   * change needed. "<" isn't commutative, but it has its opposite: ">=".
   * @param ¢ The operator to flip
   * @return correspond operator - ¢.¢. "<=" will become ">", "+" will stay
   *         "+". */
  static InfixExpression.Operator conjugate(final InfixExpression.Operator ¢) {
    return !wizard.conjugate.containsKey(¢) ? ¢ : wizard.conjugate.get(¢);
  }

  /** @param ns unknown number of nodes to check
   * @return whetherone of the nodes is an Expression Statement of type Post or
   *         Pre Expression with ++ or -- operator. false if none of them are or
   *         if the given parameter is null. */
  static boolean containIncOrDecExp(final ASTNode... ns) {
    return ns != null && Stream.of(ns).anyMatch(λ -> λ != null && iz.incrementOrDecrement(λ));
  }

  static InfixExpression.Operator convertToInfix(final Operator ¢) {
    return ¢ == Operator.BIT_AND_ASSIGN ? InfixExpression.Operator.AND
        : ¢ == Operator.BIT_OR_ASSIGN ? InfixExpression.Operator.OR
            : ¢ == Operator.BIT_XOR_ASSIGN ? InfixExpression.Operator.XOR
                : ¢ == Operator.DIVIDE_ASSIGN ? InfixExpression.Operator.DIVIDE
                    : ¢ == Operator.LEFT_SHIFT_ASSIGN ? InfixExpression.Operator.LEFT_SHIFT
                        : ¢ == Operator.MINUS_ASSIGN ? InfixExpression.Operator.MINUS
                            : ¢ == Operator.PLUS_ASSIGN ? InfixExpression.Operator.PLUS
                                : ¢ == Operator.REMAINDER_ASSIGN ? InfixExpression.Operator.REMAINDER
                                    : ¢ == Operator.RIGHT_SHIFT_SIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_SIGNED
                                        : ¢ == Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN ? InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED : null;
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

  /** Eliminates a {@link VariableDeclarationFragment}, with any other fragment
   * fragments which are not live in the containing
   * {@link VariabelDeclarationStatement}. If no fragments are left, then this
   * containing node is eliminated as well.
   * @param f
   * @param r
   * @param g */
  static void eliminate(final VariableDeclarationFragment f, final ASTRewrite r, final TextEditGroup g) {
    final VariableDeclarationStatement parent = (VariableDeclarationStatement) f.getParent();
    final List<VariableDeclarationFragment> live = live(f, fragments(parent));
    if (live.isEmpty()) {
      r.remove(parent, g);
      return;
    }
    final VariableDeclarationStatement newParent = copy.of(parent);
    fragments(newParent).clear();
    fragments(newParent).addAll(live);
    r.replace(parent, newParent, g);
  }

  /** Determines if we can be certain that a {@link Statement} ends with a
   * sequencer ({@link ReturnStatement}, {@link ThrowStatement},
   * {@link BreakStatement}, {@link ContinueStatement}).
   * @param ¢ JD
   * @return true <b>iff</b> the Statement can be verified to end with a
   *         sequencer. */
  static boolean endsWithSequencer(final Statement ¢) {
    if (¢ == null)
      return false;
    final Statement $ = hop.lastStatement(¢);
    if ($ == null)
      return false;
    switch ($.getNodeType()) {
      case BLOCK:
        return endsWithSequencer(lisp.last(statements((Block) $)));
      case BREAK_STATEMENT:
      case CONTINUE_STATEMENT:
      case RETURN_STATEMENT:
      case THROW_STATEMENT:
        return true;
      case DO_STATEMENT:
        return endsWithSequencer(((DoStatement) $).getBody());
      case LABELED_STATEMENT:
        return endsWithSequencer(((LabeledStatement) $).getBody());
      case IF_STATEMENT:
        return endsWithSequencer(then((IfStatement) $)) && endsWithSequencer(elze((IfStatement) $));
      default:
        return false;
    }
  }

  /** Find the first matching expression to the given boolean (b).
   * @param b JD,
   * @param xs JD
   * @return first expression from the given list (es) whose boolean value
   *         matches to the given boolean (b). */
  static Expression find(final boolean b, final List<Expression> xs) {
    return xs.stream().filter(λ -> iz.booleanLiteral(λ) && b == az.booleanLiteral(λ).booleanValue()).findFirst().orElse(null);
  }

  /** Gets two lists of expressions and returns the idx of the only expression
   * which is different between them. If the lists differ with other then one
   * element, -1 is returned.
   * @param es1
   * @param es2
   * @return */
  @SuppressWarnings("boxing") static int findSingleDifference(final List<? extends ASTNode> es1, final List<? extends ASTNode> es2) {
    int $ = -1;
    for (final Integer ¢ : range.from(0).to(es1.size()))
      if (!wizard.same(es1.get(¢), es2.get(¢))) {
        if ($ >= 0)
          return -1;
        $ = ¢;
      }
    return $;
  }

  static boolean forbiddenOpOnPrimitive(final VariableDeclarationFragment f, final Statement nextStatement) {
    if (!iz.literal(f.getInitializer()) || !iz.expressionStatement(nextStatement))
      return false;
    final ExpressionStatement x = (ExpressionStatement) nextStatement;
    if (iz.methodInvocation(x.getExpression())) {
      final Expression $ = core(expression(x.getExpression()));
      return iz.simpleName($) && ((SimpleName) $).getIdentifier().equals(f.getName().getIdentifier());
    }
    if (!iz.fieldAccess(x.getExpression()))
      return false;
    final Expression e = core(((FieldAccess) x.getExpression()).getExpression());
    return iz.simpleName(e) && ((SimpleName) e).getIdentifier().equals(f.getName().getIdentifier());
  }

  static boolean frobiddenOpOnPrimitive(final VariableDeclarationFragment f, final Statement nextStatement) {
    if (!iz.literal(f.getInitializer()) || !iz.expressionStatement(nextStatement))
      return false;
    final ExpressionStatement x = (ExpressionStatement) nextStatement;
    if (iz.methodInvocation(x.getExpression())) {
      final Expression $ = core(expression(x.getExpression()));
      return iz.simpleName($) && ((SimpleName) $).getIdentifier().equals(f.getName().getIdentifier());
    }
    if (!iz.fieldAccess(x.getExpression()))
      return false;
    final Expression e = core(((FieldAccess) x.getExpression()).getExpression());
    return iz.simpleName(e) && ((SimpleName) e).getIdentifier().equals(f.getName().getIdentifier());
  }

  @SuppressWarnings("unchecked") static List<MethodDeclaration> getMethodsSorted(final ASTNode n) {
    final Collection<MethodDeclaration> $ = new ArrayList<>();
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final MethodDeclaration ¢) {
        $.add(¢);
        return false;
      }
    });
    return (List<MethodDeclaration>) $.stream().sorted((x, y) -> metrics.countStatements(x) > metrics.countStatements(y)
        || metrics.countStatements(x) == metrics.countStatements(y) && x.parameters().size() > y.parameters().size() ? -1 : 1);
  }

  static Expression goInfix(final InfixExpression from, final VariableDeclarationStatement s) {
    final List<Expression> $ = hop.operands(from);
    // TODO Raviv Rachmiel: use extract.core
    $.stream().filter(λ -> iz.parenthesizedExpression(λ) && iz.assignment(az.parenthesizedExpression(λ).getExpression())).forEachOrdered(x -> {
      final Assignment a = az.assignment(az.parenthesizedExpression(x).getExpression());
      final SimpleName var = az.simpleName(left(a));
      fragments(s).stream().filter(λ -> (name(λ) + "").equals(var + "")).forEach(λ -> {
        λ.setInitializer(copy.of(right(a)));
        $.set($.indexOf(x), x.getAST().newSimpleName(var + ""));
      });
    });
    return subject.append(subject.pair(first($), $.get(1)).to(from.getOperator()), chop(chop($)));
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

  /** @param o JD
   * @return whetherone of {@link #InfixExpression.Operator.XOR},
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
   * @return whetherone of {@link #InfixExpression.Operator.LESS},
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

  static boolean isObject(final Type ¢) {
    if (¢ == null)
      return false;
    switch (¢ + "") {
      case "Object":
      case "java.lang.Object":
        return true;
      default:
        return false;
    }
  }

  /** Determine whether an InfixExpression.Operator is a shift operator or not
   * @param o JD
   * @return whetherone of {@link #InfixExpression.Operator.LEFT_SHIFT},
   *         {@link #InfixExpression.Operator.RIGHT_SHIFT_SIGNED},
   *         {@link #InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED} and false
   *         otherwise */
  static boolean isShift(final InfixExpression.Operator ¢) {
    return in(¢, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED);
  }

  /** TODO Yossi Gil: Stub 'wizard::isString' (created on 2017-02-08)." );
   * <p>
   * @param type
   * @return
   *         <p>
   *         [[SuppressWarningsSpartan]] */
  static boolean isString(final String typeName) {
    if (typeName == null)
      return false;
    switch (typeName) {
      case "String":
      case "java.lang.String":
        return true;
      default:
        return false;
    }
  }

  static boolean isString(final Type ¢) {
    return isString(¢ + "");
  }

  static boolean isValueType(final String typeName) {
    return valueTypes.contains(typeName);
  }

  static boolean isValueType(final Type ¢) {
    return isValueType(!haz.binding(¢) ? ¢ + "" : ¢.resolveBinding().getBinaryName());
  }

  static List<VariableDeclarationFragment> live(final VariableDeclarationFragment f, final Collection<VariableDeclarationFragment> fs) {
    final List<VariableDeclarationFragment> $ = new ArrayList<>();
    fs.stream().filter(λ -> λ != f && λ.getInitializer() != null).forEach(λ -> $.add(copy.of(λ)));
    return $;
  }

  static Set<Modifier> matches(final BodyDeclaration d, final Set<Predicate<Modifier>> ms) {
    return extendedModifiers(d).stream().filter(λ -> test(λ, ms)).map(Modifier.class::cast).collect(toCollection(LinkedHashSet::new));
  }

  static Set<Modifier> matches(final List<IExtendedModifier> ms, final Set<Predicate<Modifier>> ps) {
    return ms.stream().filter(λ -> test(λ, ps)).map(Modifier.class::cast).collect(toSet());
  }

  static Set<Modifier> matchess(final BodyDeclaration ¢, final Set<Predicate<Modifier>> ms) {
    return matches(extendedModifiers(¢), ms);
  }

  static MethodDeclaration methodWithBinding(final String m) {
    return findFirst.instanceOf(MethodDeclaration.class).in(makeAST.CLASS_BODY_DECLARATIONS.makeParserWithBinding(m).createAST(null));
  }

  /** @param o JD
   * @return operator that produces the logical negation of the parameter */
  static InfixExpression.Operator negate(final InfixExpression.Operator ¢) {
    return ¢.equals(CONDITIONAL_AND) ? CONDITIONAL_OR //
        : ¢.equals(CONDITIONAL_OR) ? CONDITIONAL_AND //
            : ¢.equals(EQUALS) ? NOT_EQUALS
                : ¢.equals(NOT_EQUALS) ? EQUALS
                    : ¢.equals(LESS_EQUALS) ? GREATER
                        : ¢.equals(GREATER) ? LESS_EQUALS //
                            : ¢.equals(GREATER_EQUALS) ? LESS //
                                : ¢.equals(LESS) ? GREATER_EQUALS : null;
  }

  static String nodeName(final ASTNode ¢) {
    return ¢ == null ? "???" : nodeName(¢.getClass());
  }

  static String nodeName(final Class<? extends ASTNode> ¢) {
    return system.className(¢);
  }

  static int nodeTypesCount() {
    return classToNodeType.size() + 2;
  }

  /** Determine whether a node is an infix expression whose operator is
   * non-associative.
   * @param pattern JD
   * @return whether the parameter is a node which is an infix expression whose
   *         operator is */
  static boolean nonAssociative(final ASTNode ¢) {
    return nonAssociative(az.infixExpression(¢));
  }

  static boolean nonAssociative(final InfixExpression ¢) {
    return ¢ != null && (in(¢.getOperator(), MINUS2, DIVIDE, REMAINDER, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED)
        || iz.infixPlus(¢) && !type.isNotString(¢));
  }

  /** Parenthesize an expression (if necessary).
   * @param x JD
   * @return a {@link copy#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis. */
  static Expression parenthesize(final Expression ¢) {
    return iz.noParenthesisRequired(¢) ? copy.of(¢) : make.parethesized(¢);
  }

  static ASTParser parser(final int kind) {
    final ASTParser $ = ASTParser.newParser(AST.JLS8);
    setBinding($);
    $.setKind(kind);
    final Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
    $.setCompilerOptions(options);
    return $;
  }

  static <T> T previous(final T t, final List<T> ts) {
    if (ts == null)
      return null;
    final int $ = ts.indexOf(t);
    return $ < 1 ? null : ts.get($ - 1);
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
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
      $.remove(isFinal);
    final ASTNode container = containing.typeDeclaration(¢);
    if (container == null)
      return $;
    if (iz.annotationTypeDeclaration(container))
      $.add(isFinal);
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
      if (iz.fieldDeclaration(¢))
        $.addAll(visibilityModifiers);
      $.add(isPrivate);
      if (iz.isMethodDeclaration(¢))
        $.add(isFinal);
      if (iz.enumConstantDeclaration(containing.typeDeclaration(container)))
        $.add(isProtected);
    }
    if (iz.methodDeclaration(¢) && haz.hasSafeVarags(az.methodDeclaration(¢)))
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
    // noinspection ForLoopReplaceableByWhile
    for (;;) {
      final Expression x = find(¢, xs);
      if (x == null)
        return;
      xs.remove(x);
    }
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
   * Each of the parameters may be {@code null; a {@code null is only equal
   * to{@code null
   * @param n1 JD
   * @param n2 JD
   * @return {@code true} if the parameters are the same. */
  static boolean same(final ASTNode n1, final ASTNode n2) {
    return n1 == n2 || n1 != null && n2 != null && n1.getNodeType() == n2.getNodeType() && trivia.cleanForm(n1).equals(trivia.cleanForm(n2));
  }

  /** String wise comparison of all the given SimpleNames
   * @param ¢ string to compare all names to
   * @param xs SimplesNames to compare by their string value to cmpTo
   * @return whetherall names are the same (string wise) or false otherwise */
  static boolean same(final Expression x, final Expression... xs) {
    return Stream.of(xs).allMatch(λ -> same(λ, x));
  }

  /** Determine whether two lists of nodes are the same, in the sense that their
   * textual representations is identical.
   * @param ns1 first list to compare
   * @param ns2 second list to compare
   * @return are the lists equal string-wise */
  @SuppressWarnings("boxing") static <¢ extends ASTNode> boolean same(final List<¢> ns1, final List<¢> ns2) {
    return ns1 == ns2 || ns1.size() == ns2.size() && range.from(0).to(ns1.size()).stream().allMatch(λ -> same(ns1.get(λ), ns2.get(λ)));
  }

  static void setBinding(final ASTParser $) {
    $.setResolveBindings(resolveBinding.inner);
    if (resolveBinding.inner)
      $.setEnvironment(null, null, null, true);
  }

  static void setParserResolveBindings() {
    resolveBinding.inner = true;
  }

  static boolean test(final IExtendedModifier m, final Set<Predicate<Modifier>> ms) {
    return m instanceof Modifier && test((Modifier) m, ms);
  }

  static boolean test(final Modifier m, final Set<Predicate<Modifier>> ms) {
    return ms.stream().anyMatch(λ -> λ.test(m));
  }

  static Message[] getProblems(final ASTNode $) {
    return !($ instanceof CompilationUnit) ? null : ((CompilationUnit) $).getMessages();
  }

  static boolean valid(final ASTNode $) {
    return !($ instanceof CompilationUnit) || ((CompilationUnit) $).getProblems().length == 0;
  }

  static String problems(final ASTNode ¢) {
    return !(¢ instanceof CompilationUnit) ? "???" : problems((CompilationUnit) ¢);
  }

  static String problems(final CompilationUnit u) {
    final IProblem[] v = u.getProblems();
    if (v.length == 0)
      return "???";
    final Int $ = new Int();
    return Stream.of(v).map(λ -> "\n\t\t\t" + ++$.inner + ": " + λ.getMessage()).reduce((x, y) -> x + y).get();
  }
}
