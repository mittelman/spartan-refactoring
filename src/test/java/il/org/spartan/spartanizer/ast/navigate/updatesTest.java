package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Yossi Gil: tests {@link compute#updatedVariables }
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM} {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-05 */
@SuppressWarnings("static-method")
public class updatesTest {
  @Test public void a() {
    assert compute.updatedVariables(into.e("i")) != null;
  }

  @Test public void b() {
    assert compute.updatedVariables(into.e("i++")) != null;
  }

  @Test public void c() {
    azzert.that(compute.updatedVariables(into.e("i++")).size(), is(1));
  }

  @Test public void d() {
    azzert.that(compute.updatedVariables(into.e("++i")).size(), is(1));
  }

  @Test public void e() {
    azzert.that(compute.updatedVariables(into.e("i=1")).size(), is(1));
  }

  @Test public void f() {
    azzert.that(compute.updatedVariables(into.e("i=j=1")).size(), is(2));
  }

  @Test public void g() {
    azzert.that(compute.updatedVariables(into.e("i=j=k++")).size(), is(3));
  }

  @Test public void h() {
    azzert.that(compute.updatedVariables(into.e("-i")).size(), is(0));
  }
}
