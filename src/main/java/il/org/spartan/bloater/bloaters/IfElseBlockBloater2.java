package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.utils.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Convert : {@code
 * if(condition) block1 else block2
 * } to : {@code if(condition){block1}else{block2} } Tested in {@link Issue0971}
 * @author Dor Ma'ayan {@code dor.d.ma@gmail.com}
 * @since 2016-12-27 */
public class IfElseBlockBloater2 extends IfAbstractPattern implements TipperCategory.Bloater {
  private static final long serialVersionUID = 3585427879204988685L;

  public IfElseBlockBloater2() {
    andAlso("At least the if or the elze are not in a block", () -> {
      return !iz.block(current.getThenStatement()) || !iz.block(current.getElseStatement());
    });
  }

  @Override public Examples examples() {
    return //
    convert("if(f()) g();")//
        .to("if(f()) {g();}"). //
        convert("if(f()) g(); else h();")//
        .to("if(f()) {g();} else {h();}"). //
        convert("if(x) {a();b();} else h();")//
        .to("if(x) {a();b();} else {h();}")//
    ;
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement ¢) {
    return null;
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final IfStatement $ = copy.of(current);
    if (!iz.block(then(current))) {
      final Block b = current.getAST().newBlock();
      statements(b).add(copy.of(then(current)));
      $.setThenStatement(b);
    }
    if (elze(current) == null || iz.block(elze(current))) {
      r.replace(current, $, g);
      return r;
    }
    final Block b = current.getAST().newBlock();
    statements(b).add(copy.of(elze(current)));
    $.setElseStatement(b);
    r.replace(current, $, g);
    return r;
  }
}