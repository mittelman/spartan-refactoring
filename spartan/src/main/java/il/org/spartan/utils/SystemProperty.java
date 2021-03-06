package il.org.spartan.utils;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import il.org.spartan.strings.StringUtils;

public enum SystemProperty {
  FILE_SEPARATOR, //
  FILE_ENCODING, //
  FILE_ENCODING_PKG, //
  JAVA_COMPILER, //
  JAVA_CLASS_PATH, //
  JAVA_CLASS_VERSION, //
  JAVA_HOME, //
  JAVA_IO_TMPDIR, //
  JAVA_VENDOR, //
  JAVA_VENDOR_URL, //
  JAVA_VERSION, //
  LINE_SEPARATOR, //
  OS_ARCH, //
  OS_NAME, //
  OS_VERSION, //
  PATH_SEPARATOR, //
  USER_DIR, //
  USER_HOME, //
  USER_NAME, //
  USER_REGION, //
  USER_TIMEZONE, //
  ;

  public static void main(final String[] args) throws RuntimeException {
    for (final SystemProperty ¢ : values()) {
      if (¢.value() == null)
        throw new RuntimeException("property " + ¢ + " is probably misspelled");
      System.out.println(¢.key + "='" + ¢.value() + "'");
    }
    for (final String ¢ : objectsToStrings(System.getProperties().keySet()))
      System.out.println(¢ + " = '" + StringUtils.visualize((String) System.getProperties().get(¢)) + "'");
  }

  private static TreeSet<String> objectsToStrings(final Set<Object> ¢) {
    return ¢.stream().map(λ -> (String) λ).collect(Collectors.toCollection(TreeSet::new));
  }

  public final String key;

  SystemProperty() {
    key = name().toLowerCase().replace('_', '.');
  }

  public String value() {
    return StringUtils.visualize(value(System.getProperties()));
  }

  public String value(final Properties ¢) {
    return ¢.getProperty(key);
  }
}
