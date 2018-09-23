package org.aksw.simba.knowledgeextraction.commons.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Compress {
  public static final Logger LOG = LogManager.getLogger(Compress.class);


  public static BufferedReader gunzipIt(final Path zipPath) {
    return gunzipIt(zipPath.toAbsolutePath().toString());
  }

  public static List<String> gunzipItToList(final String pathToFile) {
    return FileUtil.read(gunzipIt(pathToFile), "");
  }

  public static BufferedReader gunzipIt(final String zipFile) {
    final byte[] buffer = new byte[1024];
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      final GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(zipFile));
      int len;
      while ((len = gzis.read(buffer)) > 0) {
        baos.write(buffer, 0, len);
      }
      gzis.close();
      baos.close();
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return new BufferedReader(new StringReader(baos.toString()));
  }

  /**
   *
   * @param file
   * @return
   */
  public static BufferedReader openBZip2File(final String file) {
    String s = "";
    try {
      final FileInputStream in = new FileInputStream(file);
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
      final byte[] buffer = new byte[1028];
      int n = 0;
      while (-1 != (n = bzIn.read(buffer))) {
        out.write(buffer, 0, n);
      }
      out.close();
      bzIn.close();
      s = out.toString();
    } catch (final Exception e) {
      LOG.error("\n", e);
      return null;
    }
    return new BufferedReader(new StringReader(s));
  }


  /**
   * Reads a file to List.
   *
   * @param pathToFile path to the file
   * @param commentSymbol a line in the given file starting with the commentSymbole will be ignored
   * @return list of lines
   */
  public static List<String> fileToList(final String pathToFile, final String commentSymbol) {
    return FileUtil.fileToList(pathToFile, "UTF-8", commentSymbol);
  }

  public static List<String> bzip2ToList(final String pathToFile) {
    return bzip2ToList(pathToFile, "");
  }

  public static List<String> bzip2ToList(final String pathToFile, final String commentSymbol) {
    return FileUtil.read(openBZip2File(pathToFile), commentSymbol);
  }


}
