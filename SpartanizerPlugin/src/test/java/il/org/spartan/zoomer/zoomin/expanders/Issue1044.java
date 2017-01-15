package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;

/** Test class for name generation from Namespace (Environments)
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc" })
public class Issue1044 extends ReflectiveTester {
  private final Namespace fixtureClass = Environment.of(myCompilationUnit()).getChild(1);
  private final Namespace firstBlock = fixtureClass.getChild(0);
  private final Namespace functionF = fixtureClass.getChild(1);
  private final Namespace classX = fixtureClass.getChild(2);

  // TODO: Doron Meshulam: please use azzert.that(x, is(y)) --yg
  @Test public void test1a() {
    azzert.that(firstBlock.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new Integer(5)"))))), is("i4"));
    // TODO Auto-generated method stub
  }

  @Test public void test1b() {
    azzert.that(firstBlock.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new B();"))))), is("b1"));
    // TODO Auto-generated method stub
  }

  @Test public void test2a() {
    azzert.that(functionF.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new Integer(5);"))))), is("i1"));
    // TODO Auto-generated method stub
  }

  @Test public void test2b() {
    azzert.that(functionF.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new A();"))))), is("a2"));
    // TODO Auto-generated method stub
  }

  @Test public void test3a() {
    azzert.that(classX.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new X();"))))), is("x3"));
    // TODO Auto-generated method stub
  }
}

class NamespaceFixture {
  static {
    // TODO: Doron, should be documented
  }

  int f(final int a1) {
    return a1 >>> hashCode();
  }

  interface X {
    int x1 = 7, x2 = 5;
  }
}
