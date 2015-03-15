package ch.heigvd.res.lab01.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer. When
 * filter encounters a line separator, it sends it to the decorated writer. It then
 * sends the line number and a tab character, before resuming the write process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

   private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

   private boolean newLine = true;
   private int lineNb = 0;
   private int previousChar;

   public FileNumberingFilterWriter(Writer out) {
      super(out);
   }

   @Override
   public void write(String str, int off, int len) throws IOException {
      int length;
      char currentChar;
      StringBuilder toWrite = new StringBuilder();

      str = str.substring(off, off + len);
      length = str.length();

      for (int i = 0; i < length; i++) {
         currentChar = str.charAt(i);

         if (newLine) {
            newLine = false;
            toWrite.append(++lineNb).append("\t").append(currentChar);
         } else if (currentChar == '\r') {
            if (i + 1 < length && str.charAt(i + 1) == '\n') { // Windows
               toWrite.append("\r\n").append(++lineNb).append("\t");
               i++;
            } else { // Mac OS < X
               toWrite.append("\r").append(++lineNb).append("\t");
            }
         } else if (currentChar == '\n') { // Unix / Mac OS X
            toWrite.append("\n").append(++lineNb).append("\t");
         } else {
            toWrite.append(currentChar);
         }
      }

      super.write(toWrite.toString(), 0, toWrite.length());
   }

   @Override
   public void write(char[] cbuf, int off, int len) throws IOException {
      write(new String(cbuf), off, len);
   }

   @Override
   public void write(int c) throws IOException {
      char currentChar = (char) c;

      if (newLine) {
         newLine = false;
         super.write((char) ('0' + ++lineNb));
         super.write('\t');
         super.write((char) c);
      } else if (currentChar == '\r') {
         previousChar = '\r';
      } else if (currentChar == '\n') {
         if (previousChar == '\r') { // Windows
            previousChar = '\u0000'; // Default value for char
            super.write('\r');
            super.write('\n');
            super.write((char) ('0' + ++lineNb));
            super.write('\t');
         } else { // Unix / Mac OS X
            super.write('\n');
            super.write((char) ('0' + ++lineNb));
            super.write('\t');
         }
      } else if (previousChar == '\r') { // Mac OS < X
         super.write('\r');
         super.write((char) ('0' + ++lineNb));
         super.write('\t');
         super.write((char) c);
         previousChar = '\u0000'; // Default value for char
      } else {
         super.write((char) c);
      }
   }

}
