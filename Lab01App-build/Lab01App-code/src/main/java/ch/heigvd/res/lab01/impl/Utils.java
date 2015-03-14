package ch.heigvd.res.lab01.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

   private static final Logger LOG = Logger.getLogger(Utils.class.getName());

   /**
    * This method looks for the next new line separators (\r, \n, \r\n) to extract
    * the next line in the string passed in arguments.
    *
    * @param lines a string that may contain 0, 1 or more lines
    * @return an array with 2 elements; the first element is the next line with the
    * line separator, the second element is the remaining text. If the argument does
    * not contain any line separator, then the first element is an empty string.
    */
   public static String[] getNextLine(String lines) {
      //return lines.split("\\b(\\r?\\n)");

      /*String[] tmp = lines.split("(?=\\n\\r)|(?=\\n)");
       System.out.println(tmp.length);
       return tmp;*/
      /*StringTokenizer tmp = new StringTokenizer(lines, "\r\n");
       ArrayList<String> tmp2 = new ArrayList<String>();
       while (tmp.hasMoreElements()) {
       tmp2.add(tmp.nextToken());
       }
       String[] simpleArray = new String[tmp2.size()];
       return tmp2.toArray(simpleArray);*/
      //return lines.split("\\r?\\n", 2);
      int length = lines.length();
      char c;
      for (int i = 0; i < length; i++) {
         c = lines.charAt(i);

         if (c == '\r' && i + 1 < length && lines.charAt(i + 1) == '\n') {
            return new String[]{lines.substring(0, i + 2), lines.substring(i + 2)};
         } else if (c == '\r' || c == '\n') {
            return new String[]{lines.substring(0, i + 1), lines.substring(i + 1)};
         }
      }
      
      return new String[]{"", lines};
   }

}
