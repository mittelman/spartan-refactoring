/* The following code was generated by JFlex 1.4.3 on 10/14/12 10:56 AM */
/** A general purpose Java tokenizer,
 * @author Yossi Gil
 * @since 2007/04/02 */
package il.org.spartan.java;

import static il.org.spartan.java.Token.*;

@SuppressWarnings("all")
/** This class is a scanner generated by
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3 on 10/14/12 10:56 AM from the
 * specification file
 * <tt>/home/yogi/Workspace/Services/src/il/ac/technion/cs/ssdl/java/Tokenizer.flex</tt> */
public class RawTokenizer {
  /** This character denotes the end of file */
  public static final int YYEOF = -1;
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;
  /** lexical states */
  public static final int SCAN_LINE_COMMENT = 10;
  public static final int BLOCK_EOLN = 16;
  public static final int DOC_EOLN = 18;
  public static final int RESET = 2;
  public static final int SCAN_STRING_LITERAL = 6;
  public static final int SCAN_BLOCK_COMMENT = 14;
  public static final int YYINITIAL = 0;
  public static final int SCAN_DOC_COMMENT = 12;
  public static final int SCAN_CHAR_LITERAL = 8;
  public static final int SCAN_CODE = 4;
  /** ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l at the
   * beginning of a line l is of the form l = 2*k, k a non negative integer */
  private static final int ZZ_LEXSTATE[] = { 0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8 };
  /** Translates characters to character classes */
  private static final String ZZ_CMAP_PACKED = "\t\u0005\u0001\u0003\u0001\u0001\u0001\u0000\u0001\u0003\u0001\u0002\u000e\u0005\u0004\u0000\u0001\u0003\u0001&\u0001\u0010\u0001\u0000\u0001\u0004\u0001.\u0001*\u0001\u0011\u0001\u001b\u0001\u001c\u0001\u000f\u0001,"
      + "\u0001\"\u0001\r\u0001\u000b\u0001\u000e\u0001\u0006\u0007\n\u0002\u0007\u0001)\u0001!\u0001%\u0001#\u0001$\u0001(\u0001\u0012\u0003\t\u00011\u0001\f\u00010\u0005\u0004\u0001/"
      + "\u000b\u0004\u0001\b\u0002\u0004\u0001\u001f\u00012\u0001 \u0001-\u0001\u0004\u0001\u0000\u0001\u0019\u0001\t\u0001\u001a\u00011\u0001\u0016\u0001\u0018\u0002\u0004\u0001\u0013\u0002\u0004\u0001/\u0001\u0004"
      + "\u0001\u0014\u0003\u0004\u0001\u0017\u0001\u0004\u0001\u0015\u0003\u0004\u0001\b\u0002\u0004\u0001\u001d\u0001+\u0001\u001e\u0001'!\u0005\u0002\u0000\u0004\u0004\u0004\u0000\u0001\u0004\u0002\u0000\u0001\u0005\u0007\u0000"
      + "\u0001\u0004\u0004\u0000\u0001\u0004\u0005\u0000\u0017\u0004\u0001\u0000\u001f\u0004\u0001\u0000Ŀ\u0004\u0019\u0000r\u0004\u0004\u0000\f\u0004\u000e\u0000\u0005\u0004\t\u0000\u0001\u0004\u0011\u0000X\u0005\u0005\u0000"
      + "\u0013\u0005\n\u0000\u0001\u0004\u000b\u0000\u0001\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0014\u0004\u0001\u0000,\u0004\u0001\u0000&\u0004\u0001\u0000\u0005\u0004\u0004\u0000\u0004\u0001\u0000"
      + "\u0004\u0005\u0003\u0000E\u0004\u0001\u0000&\u0004\u0002\u0000\u0002\u0004\u0006\u0000\u0010\u0004!\u0000&\u0004\u0002\u0000\u0001\u0004\u0007\u0000'\u0004\t\u0000\u0011\u0005\u0001\u0000\u0017\u0005\u0001\u0000"
      + "\u0003\u0005\u0001\u0000\u0001\u0005\u0001\u0000\u0002\u0005\u0001\u0000\u0001\u0005\u000b\u0000\u001b\u0004\u0005\u0000\u0003\u0004\r\u0000\u0004\u0005\f\u0000\u0006\u0005\u000b\u0000\u001a\u0004\u0005\u0000\u000b\u0004\u000e\u0005"
      + "\u0007\u0000\n\u0005\u0004\u0000\u0002\u0004\u0001\u0005c\u0004\u0001\u0000\u0001\u0004\b\u0005\u0001\u0000\u0006\u0005\u0002\u0004\u0002\u0005\u0001\u0000\u0004\u0005\u0002\u0004\n\u0005\u0003\u0004\u0002\u0000\u0001\u0004\u000f\u0000\u0001\u0005\u0001\u0004\u0001\u0005\u001e\u0004\u001b\u0005\u0002\u0000\u0003\u00040\u0000&\u0004"
      + "\u000b\u0005\u0001\u0004ŏ\u0000\u0003\u00056\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0010\u0005\u0002\u0000\u0001\u0004\u0004\u0005\u0003\u0000\n\u0004\u0002\u0005\u0002\u0000\n\u0005\u0011\u0000\u0003\u0005\u0001\u0000"
      + "\b\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0004\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0007\u0005\u0002\u0000\u0002\u0005\u0002\u0000\u0003\u0005\t\u0000\u0001\u0005\u0004\u0000\u0002\u0004\u0001\u0000\u0003\u0004\u0002\u0005\u0002\u0000\n\u0005\u0004\u0004\r\u0000"
      + "\u0003\u0005\u0001\u0000\u0006\u0004\u0004\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0002\u0000\u0001\u0005\u0001\u0000\u0005\u0005\u0004\u0000\u0002\u0005\u0002\u0000\u0003\u0005\u000b\u0000\u0004\u0004\u0001\u0000\u0001\u0004\u0007\u0000\f\u0005\u0003\u0004"
      + "\f\u0000\u0003\u0005\u0001\u0000\t\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\b\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0003\u0005\u0002\u0000\u0001\u0004\u000f\u0000\u0002\u0004\u0002\u0005\u0002\u0000\n\u0005\u0001\u0000"
      + "\u0001\u0004\u000f\u0000\u0003\u0005\u0001\u0000\b\u0004\u0002\u0000\u0002\u0004\u0002\u0000\u0016\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0006\u0005\u0003\u0000\u0002\u0005\u0002\u0000\u0003\u0005\b\u0000\u0002\u0005\u0004\u0000\u0002\u0004\u0001\u0000\u0003\u0004\u0004\u0000"
      + "\n\u0005\u0001\u0000\u0001\u0004\u0010\u0000\u0001\u0005\u0001\u0004\u0001\u0000\u0006\u0004\u0003\u0000\u0003\u0004\u0001\u0000\u0004\u0004\u0003\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0003\u0000\u0002\u0004\u0003\u0000\u0003\u0004\u0003\u0000\b\u0004\u0001\u0000\u0003\u0004\u0004\u0000\u0005\u0005\u0003\u0000\u0003\u0005"
      + "\u0001\u0000\u0004\u0005\t\u0000\u0001\u0005\u000f\u0000\t\u0005\t\u0000\u0001\u0004\u0007\u0000\u0003\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\n\u0004\u0001\u0000\u0005\u0004\u0004\u0000\u0007\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0004\u0005\u0007\u0000\u0002\u0005\t\u0000\u0002\u0004"
      + "\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\n\u0004\u0001\u0000\u0005\u0004\u0002\u0000\u0001\u0005\u0001\u0004\u0007\u0005\u0001\u0000\u0003\u0005\u0001\u0000\u0004\u0005\u0007\u0000\u0002\u0005\u0007\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0004\u0000\n\u0005"
      + "\u0012\u0000\u0002\u0005\u0001\u0000\b\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0017\u0004\u0001\u0000\u0010\u0004\u0004\u0000\u0006\u0005\u0002\u0000\u0003\u0005\u0001\u0000\u0004\u0005\t\u0000\u0001\u0005\b\u0000\u0002\u0004"
      + "\u0004\u0000\n\u0005\u0012\u0000\u0002\u0005\u0001\u0000\u0012\u0004\u0003\u0000\u0018\u0004\u0001\u0000\t\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0007\u0004\u0003\u0000\u0001\u0005\u0004\u0000\u0006\u0005\u0001\u0000\u0001\u0005\u0001\u0000\b\u0005\u0012\u0000\u0002\u0005\r\u00000\u0004\u0001\u0005\u0002\u0004\u0007\u0005\u0004\u0000"
      + "\b\u0004\b\u0005\u0001\u0000\n\u0005'\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0002\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0001\u0004\u0006\u0000\u0004\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0002\u0000\u0002\u0004\u0001\u0000\u0004\u0004\u0001\u0005\u0002\u0004"
      + "\u0006\u0005\u0001\u0000\u0002\u0005\u0001\u0004\u0002\u0000\u0005\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0006\u0005\u0002\u0000\n\u0005\u0002\u0000\u0002\u0004\"\u0000\u0001\u0004\u0017\u0000\u0002\u0005\u0006\u0000\n\u0005\u000b\u0000\u0001\u0005\u0001\u0000\u0001\u0005\u0001\u0000\u0001\u0005\u0004\u0000\u0002\u0005\b\u0004\u0001\u0000"
      + "\"\u0004\u0006\u0000\u0014\u0005\u0001\u0000\u0002\u0005\u0004\u0004\u0004\u0000\b\u0005\u0001\u0000$\u0005\t\u0000\u0001\u00059\u0000\"\u0004\u0001\u0000\u0005\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0007\u0005"
      + "\u0003\u0000\u0004\u0005\u0006\u0000\n\u0005\u0006\u0000\u0006\u0004\u0004\u0005F\u0000&\u0004\n\u0000)\u0004\u0007\u0000Z\u0004\u0005\u0000D\u0004\u0005\u0000R\u0004\u0006\u0000\u0007\u0004\u0001\u0000"
      + "?\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000'\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u001f\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000"
      + "\u0007\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0017\u0004\u0001\u0000\u001f\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0002\u0000\u0007\u0004\u0001\u0000'\u0004\u0001\u0000\u0013\u0004\u000e\u0000\t\u0005.\u0000"
      + "U\u0004\f\u0000ɬ\u0004\u0002\u0000\b\u0004\n\u0000\u001a\u0004\u0005\u0000K\u0004\u0003\u0000\u0003\u0004\u000f\u0000\r\u0004\u0001\u0000\u0004\u0004\u0003\u0005\u000b\u0000\u0012\u0004\u0003\u0005\u000b\u0000"
      + "\u0012\u0004\u0002\u0005\f\u0000\r\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0002\u0005\f\u00004\u0004 \u0005\u0003\u0000\u0001\u0004\u0003\u0000\u0002\u0004\u0001\u0005\u0002\u0000\n\u0005!\u0000\u0003\u0005"
      + "\u0002\u0000\n\u0005\u0006\u0000X\u0004\b\u0000)\u0004\u0001\u0005V\u0000\u001d\u0004\u0003\u0000\f\u0005\u0004\u0000\f\u0005\n\u0000\n\u0005\u001e\u0004\u0002\u0000\u0005\u0004΋\u0000l\u0004"
      + "\u0000\u0004\u0004\u0000Z\u0004\u0006\u0000\u0016\u0004\u0002\u0000\u0006\u0004\u0002\u0000&\u0004\u0002\u0000\u0006\u0004\u0002\u0000\b\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0001\u0004"
      + "\u0001\u0000\u001f\u0004\u0002\u00005\u0004\u0001\u0000\u0007\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u0003\u0000\u0004\u0004\u0002\u0000\u0006\u0004\u0004\u0000\r\u0004\u0005\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u000f\u0000\u0004\u0005\u001a\u0000\u0005\u0005\u0010\u0000\u0002\u0004\u0013\u0000\u0001\u0004"
      + "\u000b\u0000\u0004\u0005\u0006\u0000\u0006\u0005\u0001\u0000\u0001\u0004\r\u0000\u0001\u0004 \u0000\u0012\u0004\u001e\u0000\r\u0005\u0004\u0000\u0001\u0005\u0003\u0000\u0006\u0005\u0017\u0000\u0001\u0004\u0004\u0000\u0001\u0004\u0002\u0000\n\u0004\u0001\u0000\u0001\u0004\u0003\u0000\u0005\u0004\u0006\u0000\u0001\u0004\u0001\u0000\u0001\u0004"
      + "\u0001\u0000\u0001\u0004\u0001\u0000\u0004\u0004\u0001\u0000\u0003\u0004\u0001\u0000\u0007\u0004\u0003\u0000\u0003\u0004\u0005\u0000\u0005\u0004\u0016\u0000$\u0004ກ\u0000\u0003\u0004\u0019\u0000\t\u0004\u0006\u0005\u0001\u0000\u0005\u0004\u0002\u0000\u0005\u0004\u0004\u0000V\u0004\u0002\u0000\u0002\u0005\u0002\u0000\u0003\u0004\u0001\u0000"
      + "_\u0004\u0005\u0000(\u0004\u0004\u0000^\u0004\u0011\u0000\u0018\u00048\u0000\u0010\u0004Ȁ\u0000ᦶ\u0004J\u0000冦\u0004Z\u0000ҍ\u0004ݳ\u0000⮤\u0004⅜\u0000Į\u0004\u0002\u0000"
      + ";\u0004\u0000\u0007\u0004\f\u0000\u0005\u0004\u0005\u0000\u0001\u0004\u0001\u0005\n\u0004\u0001\u0000\r\u0004\u0001\u0000\u0005\u0004\u0001\u0000\u0001\u0004\u0001\u0000\u0002\u0004\u0001\u0000\u0002\u0004\u0001\u0000"
      + "l\u0004!\u0000ū\u0004\u0012\u0000@\u0004\u0002\u00006\u0004(\u0000\r\u0004\u0003\u0000\u0010\u0005\u0010\u0000\u0004\u0005\u000f\u0000\u0002\u0004\u0018\u0000\u0003\u0004\u0019\u0000\u0001\u0004\u0006\u0000"
      + "\u0005\u0004\u0001\u0000\u0004\u0002\u0000\u0001\u0005\u0004\u0000\u0001\u0004\u000b\u0000\n\u0005\u0007\u0000\u001a\u0004\u0004\u0000\u0001\u0004\u0001\u0000\u001a\u0004\n\u0000Z\u0004\u0003\u0000\u0006\u0004\u0002\u0000\u0006\u0004\u0002\u0000\u0006\u0004\u0002\u0000\u0003\u0004\u0003\u0000\u0002\u0004\u0003\u0000\u0002\u0004\u0012\u0000"
      + "\3\5\4\0";
  /** Translates characters to character classes */
  private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);
  /** Translates DFA states to action switch labels. */
  private static final int[] ZZ_ACTION = zzUnpackAction();
  private static final String ZZ_ACTION_PACKED_0 = "\t\u0000\u0002\u0001\u0001\u0002\u0002\u0003\u0001\u0004\u0001\u0005\u0002\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\u0002\u0001\r\u0001\u000e"
      + "\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\u0018\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0002\"\u0001#\u0001!\u0002$\u0001%"
      + "\u0001!\u0002&\u0002'\u0001!\u0002(\u0001!\u0002)\u0002*\u0001\u0006\u0002\u0000\u0001+\u0001\u0000\u0001,\u0001\u0006\u0001+\u0001-\u0001.\u0001/\u00010\u00011\u00012\u00023\u00014\u00015"
      + "\u00016\u00017\u00018\u00019\u0001:\u0001;\u0001<\u0001=\u0001>\u0001?\u0001@\u0001A\u0001B\u0001C\u0001\u0006\u0001+\u0001\u0000\u0001D\u00013\u0001E\u0001F\u0001G\u0001H\u00013"
      + "\1\111\5\63\1\112";
  /** Translates a state to a row index in the transition table */
  private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
  private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\63\0\146\0\231\0\314\0\377\0\u0132\0\u0165"
      + "\u0000Ƙ\u0000ǋ\u0000Ǿ\u0000ǋ\u0000ǋ\u0000ȱ\u0000ǋ\u0000ɤ\u0000ʗ\u0000ˊ\u0000˽\u0000̰\u0000ͣ\u0000Ζ\u0000ǋ\u0000ǋ"
      + "\u0000ω\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ϼ\u0000Я\u0000Ѣ\u0000ҕ\u0000ǋ\u0000ǋ\u0000ǋ"
      + "\u0000ӈ\u0000ӻ\u0000Ԯ\u0000ա\u0000֔\u0000ǋ\u0000ǋ\u0000ׇ\u0000ǋ\u0000׺\u0000ǋ\u0000ح\u0000ǋ\u0000٠\u0000ǋ\u0000ړ"
      + "\u0000ǋ\u0000ۆ\u0000۹\u0000ǋ\u0000ܬ\u0000ݟ\u0000ǋ\u0000ޒ\u0000ǋ\u0000߅\u0000߸\u0000ࠫ\u0000࡞\u0000࢑\u0000ࣄ\u0000ǋ"
      + "\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ࣷ\u0000ǋ\u0000ǋ\u0000प\u0000ढ़\u0000ǋ\u0000ǋ\u0000ঐ\u0000ǋ\u0000ৃ\u0000ǋ"
      + "\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000ǋ\u0000৶\u0000਩\u0000ੜ\u0000એ\u0000ૂ\u0000ǋ"
      + "\u0000૵\u0000ǋ\u0000ǋ\u0000ନ\u0000ǋ\u0000୛\u0000எ\u0000ு\u0000௴\u0000ధ\u0000प";
  /** The transition table of the DFA */
  private static final int[] ZZ_TRANS = zzUnpackTrans();
  private static final String ZZ_TRANS_PACKED_0 = "\u0002\n\u0001\u000b0\n\u0001\f\u0001\r\u0001\u000e\u0001\u000f\u0001\u0010\u0001\f\u0001\u0011\u0001\u0012\u0002\u0010\u0001\u0012\u0001\u0013\u0001\u0010\u0001\u0014"
      + "\u0001\u0015\u0001\u0016\u0001\u0017\u0001\u0018\u0001\u0019\b\u0010\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0001\"\u0001#\u0001$\u0001%\u0001&\u0001'\u0001(\u0001)\u0001*\u0001+"
      + "\u0001,\u0001-\u0003\u0010\u0001\f\u0001.\u0001/\u00010\r.\u00011!.\u00012\u0001.\u00013\u00014\u000e.\u00015 .\u00016\u0001.\u00017\u000181.\u00019\u0001:"
      + "\f.\u0001;$.\u0001<\u0001=\f.\u0001>#.\u0001\u0000\u0001?\u0001@1\u0000\u0001A\u0001Bd\u0000\u0001\n2\u0000\u0001\r5\u0000\u0007\u0010\u0001\u0000\u0001\u0010\u0006\u0000\b\u0010"
      + "\u0014\u0000\u0003\u0010\u0007\u0000\u0001C\u0001D\u0001E\u0001\u0000\u0001C\u0001F\u0001G\t\u0000\u0001G\u0001\u0000\u0001H\u0016\u0000\u0001I\u0001H\u0001J\u0007\u0000\u0002\u0012\u0002\u0000\u0001\u0012\u0001F\u0001G"
      + "\t\u0000\u0001G\u0001\u0000\u0001H\u0016\u0000\u0001I\u0001H\u0001J\u0007\u0000\u0002F\u0002\u0000\u0001F5\u0000\u0001K\u0015\u0000\u0001L\u001d\u0000\u0001M\u0001N\u0013\u0000\u0001O2\u0000\u0001P\u0013\u0000"
      + "\u0001Q\u0003\u0000\u0002Q\u0002\u0000\u0001Q\u0006\u0000\u0001R\u0007Q\u0014\u0000\u0003Q$\u0000\u0001S2\u0000\u0001T\u0001U1\u0000\u0001V\u0001\u0000\u0001W0\u0000\u0001X2\u0000\u0001Y\u0006\u0000"
      + "\u0001Z+\u0000\u0001[\u0007\u0000\u0001\\*\u0000\u0001]\b\u0000\u0001^)\u0000\u0001_2\u0000\u0001`\u0010\u0000\u0001/A\u0000\u0001.!\u0000\u0001.\u0001\u0000\u00013B\u0000\u0001. \u0000"
      + "\u0001.\u0001\u0000\u000172\u0000\u00019?\u0000\u0001a%\u0000\u0001<?\u0000\u0001b%\u0000\u0001?2\u0000\u0001A7\u0000\u0001C\u0001D\u0002\u0000\u0001C\u0001F#\u0000\u0001I\t\u0000"
      + "\u0002D\u0002\u0000\u0001D\u0001F-\u0000\u0002c\u0001\u0000\u0002c\u0001\u0000\u0001c\t\u0000\u0001c\u0001\u0000\u0003c\u0015\u0000\u0002c\u0007\u0000\u0002F\u0002\u0000\u0001F\u0001\u0000\u0001G\t\u0000\u0001G"
      + "\u0001\u0000\u0001H\u0017\u0000\u0001H\u0001J\u0007\u0000\u0002d\u0002\u0000\u0001d\u0002\u0000\u0001e\u001e\u0000\u0001e\u0015\u0000\u0001f'\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\bQ\u0014\u0000\u0003Q\u0005\u0000"
      + "\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0001Q\u0001g\u0006Q\u0014\u0000\u0003Q$\u0000\u0001h\u0001i1\u0000\u0001j\u0015\u0000\u0002c\u0001\u0000\u0002c\u0001\u0000\u0001c\t\u0000\u0001c\u0001\u0000\u0003c"
      + "\u0014\u0000\u0001I\u0002c\u0007\u0000\u0002d\u0002\u0000\u0001d\r\u0000\u0001H\u0017\u0000\u0001H\u0001J\u0007\u0000\u0002d\u0002\u0000\u0001d6\u0000\u0001k(\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0002Q"
      + "\u0001l\u0005Q\u0014\u0000\u0003Q$\u0000\u0001m\u0013\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0003Q\u0001n\u0004Q\u0014\u0000\u0003Q\u0005\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0004Q\u0001o\u0003Q"
      + "\u0014\u0000\u0003Q\u0005\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0005Q\u0001p\u0002Q\u0014\u0000\u0003Q\u0005\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0006Q\u0001q\u0001Q\u0014\u0000\u0003Q\u0005\u0000\u0007Q"
      + "\u0001\u0000\u0001Q\u0006\u0000\u0007Q\u0001r\u0014\u0000\u0003Q\u0005\u0000\u0007Q\u0001\u0000\u0001Q\u0006\u0000\u0003Q\u0001s\u0004Q\u0014\u0000\u0003Q\u0001\u0000";
  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = { "Unkown internal scanner error", "Error: could not match input",
      "Error: pushback value was too large" };
  /** ZZ_ATTRIBUTE[aState] contains the attributes of state
   * <code>aState</code> */
  private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
  private static final String ZZ_ATTRIBUTE_PACKED_0 = "\t\u0000\u0001\t\u0001\u0001\u0002\t\u0001\u0001\u0001\t\u0007\u0001\u0002\t\u0001\u0001\b\t\u0004\u0001\u0003\t\u0005\u0001\u0002\t\u0001\u0001\u0001\t"
      + "\u0001\u0001\u0001\t\u0001\u0001\u0001\t\u0001\u0001\u0001\t\u0001\u0001\u0001\t\u0002\u0001\u0001\t\u0002\u0001\u0001\t\u0001\u0001\u0001\t\u0002\u0001\u0002\u0000\u0001\u0001\u0001\u0000\u0006\t\u0001\u0001\u0002\t\u0002\u0001\u0002\t\u0001\u0001"
      + "\u0001\t\u0001\u0001\u000b\t\u0002\u0001\u0001\u0000\u0002\u0001\u0001\t\u0001\u0001\u0002\t\u0001\u0001\u0001\t\u0006\u0001";

  private static int[] zzUnpackAction() {
    final int[] result = new int[115];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(final String packed, final int offset, final int[] result) {
    int i = 0, j = offset;
    for (final int l = packed.length(); i < l;) {
      int count = packed.charAt(i++);
      final int value = packed.charAt(i++);
      do
        result[j++] = value;
      while (--count > 0);
    }
    return j;
  }

  private static int[] zzUnpackAttribute() {
    final int[] result = new int[115];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(final String packed, final int offset, final int[] result) {
    int i = 0, j = offset;
    for (final int l = packed.length(); i < l;) {
      int count = packed.charAt(i++);
      final int value = packed.charAt(i++);
      do
        result[j++] = value;
      while (--count > 0);
    }
    return j;
  }

  /** Unpacks the compressed character translation table.
   * @param packed the packed character translation table
   * @return the unpacked character translation table */
  private static char[] zzUnpackCMap(final String packed) {
    final char[] map = new char[0x10000];
    for (int i = 0, j = 0; i < 1764;) {
      int count = packed.charAt(i++);
      final char value = packed.charAt(i++);
      do
        map[j++] = value;
      while (--count > 0);
    }
    return map;
  }

  private static int[] zzUnpackRowMap() {
    final int[] result = new int[115];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(final String packed, final int offset, final int[] result) {
    int i = 0, j = offset;
    for (final int l = packed.length(); i < l;)
      result[j++] = packed.charAt(i++) | packed.charAt(i++) << 16;
    return j;
  }

  private static int[] zzUnpackTrans() {
    final int[] result = new int[3162];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(final String packed, final int offset, final int[] result) {
    int i = 0, j = offset;
    for (final int l = packed.length(); i < l;) {
      int count = packed.charAt(i++), value = packed.charAt(i++);
      --value;
      do
        result[j++] = value;
      while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;
  /** the current state of the DFA */
  private int zzState;
  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;
  /** this buffer contains the current text to be matched and is the source of
   * the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];
  /** the textposition at the last accepting state */
  private int zzMarkedPos;
  /** the current text position in the buffer */
  private int zzCurrentPos;
  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;
  /** endRead marks the last character in the buffer, that has been read from
   * input */
  private int zzEndRead;
  /** number of newlines encountered up to the start of the matched text */
  private int yyline;
  /** the number of characters up to the start of the matched text */
  private int yychar;
  /** the number of characters from the last newline up to the start of the
   * matched text */
  private int yycolumn;
  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;
  private final StringBuffer $ = new StringBuffer();

  /** Creates a new scanner. There is also java.io.Reader version of this
   * constructor.
   * @param in the java.io.Inputstream to read input from. */
  public RawTokenizer(final java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** Creates a new scanner There is also a java.io.InputStream version of this
   * constructor.
   * @param in the java.io.Reader to read input from. */
  public RawTokenizer(final java.io.Reader in) {
    reset();
    zzReader = in;
  }

  public int chars() {
    return yychar + 1;
  }

  public int column() {
    return yycolumn + 1;
  }

  public void error(final String ¢) {
    System.err.println(notify(¢));
    reset();
  }

  public int line() {
    return yyline + 1;
  }

  public String location() {
    return "[" + line() + "," + column() + "]: ";
  }

  /** Resumes scanning until the next regular expression is matched, the end of
   * input is encountered or an I/O-Error occurs.
   * @return the next token
   * @exception java.io.IOException if any I/O-Error occurs */
  public Token next() throws java.io.IOException {
    int zzInput, zzAction, zzCurrentPosL, zzMarkedPosL, zzEndReadL = zzEndRead;
    for (char[] zzBufferL = zzBuffer;;) {
      zzMarkedPosL = zzMarkedPos;
      yychar += zzMarkedPosL - zzStartRead;
      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL; ++zzCurrentPosL)
        switch (zzBufferL[zzCurrentPosL]) {
          case '\u000B':
          case '\u000C':
          case '\u0085':
          case '\u2028':
          case '\u2029':
            ++yyline;
            yycolumn = 0;
            zzR = false;
            break;
          case '\r':
            ++yyline;
            yycolumn = 0;
            zzR = true;
            break;
          case '\n':
            if (zzR)
              zzR = false;
            else {
              ++yyline;
              yycolumn = 0;
            }
            break;
          default:
            zzR = false;
            ++yycolumn;
        }
      if (zzR) {
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          final boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          zzPeek = !eof && zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek)
          --yyline;
      }
      zzAction = -1;
      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
      zzState = ZZ_LEXSTATE[zzLexicalState];
      zzForAction: while (true) {
        if (zzCurrentPosL >= zzEndReadL) {
          if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          zzCurrentPos = zzCurrentPosL;
          zzMarkedPos = zzMarkedPosL;
          final boolean eof = zzRefill();
          zzCurrentPosL = zzCurrentPos;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          zzEndReadL = zzEndRead;
          if (eof) {
            zzInput = YYEOF;
            break zzForAction;
          }
        }
        zzInput = zzBufferL[zzCurrentPosL++];
        final int zzNext = ZZ_TRANS[ZZ_CMAP[zzInput] + ZZ_ROWMAP[zzState]];
        if (zzNext == -1)
          break zzForAction;
        zzState = zzNext;
        if ((ZZ_ATTRIBUTE[zzState] & 1) == 1) {
          zzAction = zzState;
          zzMarkedPosL = zzCurrentPosL;
          if ((ZZ_ATTRIBUTE[zzState] & 8) == 8)
            break zzForAction;
        }
      }
      zzMarkedPos = zzMarkedPosL;
      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 4:
          return SPACE;
        case 75:
          break;
        case 74:
          return AT_INTERFACE;
        case 76:
          break;
        case 38: {
          endExcluding();
          return LINE_COMMENT;
        }
        case 77:
          break;
        case 73:
          return URSHIFTEQ;
        case 78:
          break;
        case 13:
          return LPAREN;
        case 79:
          break;
        case 26:
          return QUESTION;
        case 80:
          break;
        case 28:
          return AND;
        case 81:
          break;
        case 50:
          return MULTEQ;
        case 82:
          break;
        case 61:
          return OROR;
        case 83:
          break;
        case 37: {
          endIncluding();
          return CHARACTER_LITERAL;
        }
        case 84:
          break;
        case 27:
          return COLON;
        case 85:
          break;
        case 46:
          return MINUSEQ;
        case 86:
          break;
        case 53:
          return GTEQ;
        case 87:
          break;
        case 21:
          return EQ;
        case 88:
          break;
        case 44:
          return FLOAT_LITERAL;
        case 89:
          break;
        case 55:
          return LTEQ;
        case 90:
          break;
        case 69:
          return RSHIFTEQ;
        case 91:
          break;
        case 1: {
          regret();
          reset();
          continue;
        }
        case 92:
          break;
        case 39: {
          gotoExcluding(DOC_EOLN);
          return PARTIAL_DOC_COMMENT;
        }
        case 93:
          break;
        case 48: {
          begin(SCAN_BLOCK_COMMENT);
          continue;
        }
        case 94:
          break;
        case 63:
          return PLUSPLUS;
        case 95:
          break;
        case 51:
          return ANNOTATION;
        case 96:
          break;
        case 42: {
          truncate();
          goTo(SCAN_DOC_COMMENT);
          return NL_DOC_COMMENT;
        }
        case 97:
          break;
        case 19:
          return SEMICOLON;
        case 98:
          break;
        case 5:
          try {
            return Token.valueOf("_" + yytext());
          } catch (final IllegalArgumentException e) {
            return IDENTIFIER;
          }
        case 99:
          break;
        case 65:
          return MODEQ;
        case 100:
          break;
        case 20:
          return COMMA;
        case 101:
          break;
        case 10:
          return MULT;
        case 102:
          break;
        case 41: {
          truncate();
          goTo(SCAN_BLOCK_COMMENT);
          return NL_BLOCK_COMMENT;
        }
        case 103:
          break;
        case 32:
          return MOD;
        case 104:
          break;
        case 43:
          return DOUBLE_LITERAL;
        case 105:
          break;
        case 31:
          return XOR;
        case 106:
          break;
        case 9:
          return DIV;
        case 107:
          break;
        case 36: {
          endExcluding();
          return UNTERMINATED_CHARACTER_LITERAL;
        }
        case 108:
          break;
        case 71:
          return LSHIFTEQ;
        case 109:
          break;
        case 40: {
          gotoExcluding(BLOCK_EOLN);
          return PARTIAL_BLOCK_COMMENT;
        }
        case 110:
          break;
        case 30:
          return PLUS;
        case 111:
          break;
        case 45:
          return MINUSMINUS;
        case 112:
          break;
        case 64:
          return XOREQ;
        case 113:
          break;
        case 62:
          return PLUSEQ;
        case 114:
          break;
        case 34: {
          endExcluding();
          return UNTERMINATED_STRING_LITERAL;
        }
        case 115:
          break;
        case 52:
          return EQEQ;
        case 116:
          break;
        case 68: {
          begin(SCAN_DOC_COMMENT);
          continue;
        }
        case 117:
          break;
        case 23:
          return LT;
        case 118:
          break;
        case 12: {
          begin(SCAN_CHAR_LITERAL);
          continue;
        }
        case 119:
          break;
        case 49:
          return DIVEQ;
        case 120:
          break;
        case 7:
          return DOT;
        case 121:
          break;
        case 2:
          return UNKNOWN_CHARACTER;
        case 122:
          break;
        case 60:
          return OREQ;
        case 123:
          break;
        case 16:
          return RBRACE;
        case 124:
          break;
        case 22:
          return GT;
        case 125:
          break;
        case 18:
          return RBRACK;
        case 126:
          break;
        case 58:
          return ANDEQ;
        case 127:
          break;
        case 11: {
          begin(SCAN_STRING_LITERAL);
          continue;
        }
        case 128:
          break;
        case 70:
          return URSHIFT;
        case 129:
          break;
        case 67: {
          endIncluding();
          return BLOCK_COMMENT;
        }
        case 130:
          break;
        case 25:
          return COMP;
        case 131:
          break;
        case 29:
          return OR;
        case 132:
          break;
        case 3:
          return NL;
        case 133:
          break;
        case 6:
          return INTEGER_LITERAL;
        case 134:
          break;
        case 66: {
          endIncluding();
          return DOC_COMMENT;
        }
        case 135:
          break;
        case 15:
          return LBRACE;
        case 136:
          break;
        case 35: {
          endIncluding();
          return STRING_LITERAL;
        }
        case 137:
          break;
        case 17:
          return LBRACK;
        case 138:
          break;
        case 24:
          return NOT;
        case 139:
          break;
        case 33: {
          extend();
          continue;
        }
        case 140:
          break;
        case 72:
          return EMPTY_BLOCK_COMMENT;
        case 141:
          break;
        case 54:
          return RSHIFT;
        case 142:
          break;
        case 47: {
          begin(SCAN_LINE_COMMENT);
          continue;
        }
        case 143:
          break;
        case 8:
          return MINUS;
        case 144:
          break;
        case 57:
          return NOTEQ;
        case 145:
          break;
        case 14:
          return RPAREN;
        case 146:
          break;
        case 56:
          return LSHIFT;
        case 147:
          break;
        case 59:
          return ANDAND;
        case 148:
          break;
        default:
          if (zzInput != YYEOF || zzStartRead != zzCurrentPos)
            zzScanError(ZZ_NO_MATCH);
          else {
            zzAtEOF = true;
            switch (zzLexicalState) {
              case SCAN_LINE_COMMENT: {
                endExcluding();
                return LINE_COMMENT;
              }
              case 116:
                break;
              case RESET:
                return EOF;
              case 117:
                break;
              case SCAN_STRING_LITERAL: {
                regret();
                end();
                return UNTERMINATED_STRING_LITERAL;
              }
              case 118:
                break;
              case SCAN_BLOCK_COMMENT: {
                endExcluding();
                return UNTERMINATED_BLOCK_COMMENT;
              }
              case 119:
                break;
              case YYINITIAL:
                return EOF;
              case 120:
                break;
              case SCAN_DOC_COMMENT: {
                endExcluding();
                return UNTERMINATED_DOC_COMMENT;
              }
              case 121:
                break;
              case SCAN_CHAR_LITERAL: {
                endExcluding();
                return UNTERMINATED_CHARACTER_LITERAL;
              }
              case 122:
                break;
              case SCAN_CODE:
                return EOF;
              case 123:
                break;
              default:
                return null;
            }
          }
      }
    }
  }

  public String notify(final String ¢) {
    return location() + ¢ + " " + token();
  }

  public void reset() {
    truncate();
    yybegin(SCAN_CODE);
  }

  /* user code: */
  public String text() {
    return $.length() > 0 ? $ + "" : yytext();
  }

  public String token() {
    return "<" + text() + ">";
  }

  /** Enters a new lexical state
   * @param newState the new lexical state */
  public final void yybegin(final int newState) {
    zzLexicalState = newState;
  }

  /** Returns the character at position <tt>pos</tt> from the matched text. It
   * is equivalent to yytext().charAt(pos), but faster
   * @param pos the position of the character to fetch. A value from 0 to
   *        yylength()-1.
   * @return the character at position pos */
  public final char yycharat(final int pos) {
    return zzBuffer[pos + zzStartRead];
  }

  /** Closes the input stream. */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true; /* indicate end of file */
    zzEndRead = zzStartRead; /* invalidate buffer */
    if (zzReader != null)
      zzReader.close();
  }

  /** Returns the length of the matched text region. */
  public final int yylength() {
    return zzMarkedPos - zzStartRead;
  }

  /** Pushes the specified amount of characters back into the input stream. They
   * will be read again by then next call of the scanning method
   * @param number the number of characters to be read again. This number must
   *        not be greater than yylength()! */
  public void yypushback(final int number) {
    if (number > yylength())
      zzScanError(ZZ_PUSHBACK_2BIG);
    zzMarkedPos -= number;
  }

  /** Resets the scanner to read from a new input stream. Does not close the old
   * reader. All internal variables are reset, the old input stream
   * <b>cannot</b> be reused (internal buffer is discarded and lost). Lexical
   * state is set to <tt>ZZ_INITIAL</tt>.
   * @param reader the new input stream */
  public final void yyreset(final java.io.Reader ¢) {
    zzReader = ¢;
    zzAtEOF = false;
    yyline = yychar = yycolumn = zzCurrentPos = zzMarkedPos = 0;
    zzLexicalState = YYINITIAL;
  }

  /** Returns the current lexical state. */
  public final int yystate() {
    return zzLexicalState;
  }

  /** Returns the text matched by the current regular expression. */
  public final String yytext() {
    return new String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
  }

  private void begin(final int state) {
    truncate();
    extend();
    yybegin(state);
  }

  private void end() {
    goTo(RESET);
  }

  private void endExcluding() {
    regret();
    end();
  }

  private void endIncluding() {
    extend();
    end();
  }

  private void extend() {
    $.append(yytext());
  }

  private void goTo(final int state) {
    yybegin(state);
  }

  private void gotoExcluding(final int state) {
    regret();
    goTo(state);
  }

  private void regret() {
    yypushback(yylength());
  }

  private void truncate() {
    $.setLength(0);
  }

  /** Refills the input buffer.
   * @return <code>false</code>, iff there was new input.
   * @exception java.io.IOException if any I/O-Error occurs */
  private boolean zzRefill() throws java.io.IOException {
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead, zzBuffer, 0, zzEndRead - zzStartRead);
      zzEndRead -= zzStartRead;
      zzCurrentPos -= zzStartRead;
      zzMarkedPos -= zzStartRead;
      zzStartRead = 0;
    }
    if (zzCurrentPos >= zzBuffer.length) {
      final char newBuffer[] = new char[2 * zzCurrentPos];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }
    final int numRead = zzReader.read(zzBuffer, zzEndRead, zzBuffer.length - zzEndRead);
    if (numRead > 0)
      zzEndRead += numRead;
    else {
      if (numRead != 0)
        return true;
      final int c = zzReader.read();
      if (c == -1)
        return true;
      zzBuffer[zzEndRead++] = (char) c;
    }
    return false;
  }

  /** Reports an error that occured while scanning. In a wellformed scanner (no
   * or only correct usage of yypushback(int) and a match-all fallback rule)
   * this method will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong (e.g. a JFlex bug
   * producing a faulty scanner etc.). Usual syntax/scanner level error handling
   * should be done in error fallback rules.
   * @param errorCode the code of the errormessage to display */
  private void zzScanError(final int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    } catch (final ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }
    throw new Error(message);
  }
}
