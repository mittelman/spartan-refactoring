package il.org.spartan.athenizer;

import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.preference.IPreferenceStore;

import fluent.ly.English;
import il.org.spartan.spartanizer.plugin.Plugin;

/**
 * Classification of Expanders
 * 
 * @author Raviv Rachmiel
 * @since 24-12-16
 */
@FunctionalInterface public interface ExpanderCategory {
  String description();

  /**
   * Returns the preference group to which the tipper belongs to. This method
   * should be overridden for each tipper and should return one of the values of
   * ExpanderGroup TODO Roth, add - {@link ExpanderGroup} when you make the
   * expander preferencesResources
   * 
   * @return preference group this tipper belongs to
   */
  default ExpanderGroup tipperGroup() {
    return ExpanderGroup.find(this);
  }

  default ExpanderGroup ExpanderGroup() {
    return ExpanderGroup.find(this);
  }

  static String getLabel(final Class<? extends ExpanderCategory> ¢) {
    return English.name(¢);
  }

  // TODO Roth, to preferences?
  @FunctionalInterface interface Nominal extends ExpanderCategory {
    String label = "Nominal";
  }

  interface Clearification extends Nominal {
    String label = "adding brackets or some chars which might make the code clearer";

    @Override default String description() {
      return label;
    }
  }

  interface Explanation extends Nominal {
    String label = "expanding a statement to clearer information, though longer";

    @Override default String description() {
      return label;
    }
  }

  interface Ternarization extends Nominal {
    String label = "ternary arguments";

    @Override default String description() {
      return label;
    }
  }

  /** An enum holding together all the "enabled expanders" options */
  // TODO Roth, please make a preferencesResources file for the expanders
  enum ExpanderGroup {
    Abbreviation(ExpanderCategory.Clearification.class), //
    Explanation(ExpanderCategory.Explanation.class), //
    Ternarization(ExpanderCategory.Ternarization.class), //
    ;

    @SuppressWarnings("null") public static @Nullable ExpanderGroup find(final ExpanderCategory ¢) {
      Optional<ExpanderGroup> find = find(¢.getClass());
      return find.orElse(null);
    }

    static IPreferenceStore store() {
      return Plugin.plugin().getPreferenceStore();
    }

    @SuppressWarnings("null") private static Optional<@NonNull ExpanderGroup> find(
        final Class<? extends ExpanderCategory> ¢) {
      return Stream.of(ExpanderGroup.values()).filter(λ -> λ.clazz.isAssignableFrom(¢)).findFirst();
    }

    private final Class<? extends ExpanderCategory> clazz;
    final String id;
    final String label;

    ExpanderGroup(final Class<? extends ExpanderCategory> clazz) {
      this.clazz = clazz;
      id = clazz.getCanonicalName();
      label = ExpanderCategory.getLabel(clazz);
    }

    public boolean isEnabled() {
      return Plugin.plugin() == null || store().getBoolean(id);
    }
  }
}
