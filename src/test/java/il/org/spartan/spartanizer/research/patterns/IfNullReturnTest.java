package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class IfNullReturnTest {
  @Test public void a() {
    trimmingOf("statement(); if(x == null) return; use(); use();")//
        .using(IfStatement.class, new AssertNotNull())//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("statement(); if(x == null) return null; use(); use();")//
        .using(IfStatement.class, new AssertNotNull())//
        .gives("statement(); azzert.notNull(x); use(); use();")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("void m(){if(x == null) return; use(); use();}")//
        .using(IfStatement.class, new AssertNotNull())//
        .stays();
  }
}
