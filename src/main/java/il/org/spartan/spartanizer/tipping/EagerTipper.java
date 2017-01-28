package il.org.spartan.spartanizer.tipping;

/** A {@link Tipper} in which only the tip has to be implemented.
 * @year 2016
 * @author Yossi Gil
 * @since Sep 26, 2016 */
public abstract class EagerTipper<N extends ASTNode> extends Tipper<N> {
  @Override public final boolean canTip(final N ¢) {
    return tip(¢) != null;
  }
}