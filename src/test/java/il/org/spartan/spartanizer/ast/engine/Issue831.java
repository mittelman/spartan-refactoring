package il.org.spartan.spartanizer.ast.engine;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.tdd.*;

import static org.junit.Assert.*;

/** see Issue #7831 for more details
 * @author Lidia Piatigorski
 * @author Nikita Dizhur
 * @author Alex V.
 * @since 16-11-14 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue831 {
  
  protected class MethodScannerIExt extends MethodScanner {

    public MethodScannerIExt(MethodDeclaration method) {
      super(method);
    }

    @Override protected List<Statement> availableStatements() {
      return statements;
    }
    
  }
  
  @Test public void scannerDoesNotHaveStatementsWhenMethodDoesNotHaveBody() {
    assertTrue((new MethodScannerIExt((MethodDeclaration) wizard.ast("public int a(String a);"))).availableStatements() == null);
  }
  
}
