package org.aksw.simba.knowledgeextraction.commons.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.common.base.Charsets;

/**
 * FileHelper with static methods.
 *
 * @author Ren&eacute; Speck <speck@informatik.uni-leipzig.de>
 *
 */
public class FileUtil {

  public static final Logger LOG = LogManager.getLogger(FileUtil.class);

  /**
   * Gets all files in the folder and sub folders. Ignores duplicates.
   *
   * @param folder
   * @return files
   */
  public static Set<Path> regularFilesInFolder(final String folder) {
    final Set<Path> path = new HashSet<>();
    try {
      Files.walk(Paths.get(folder)).forEach(filePath -> {
        if (Files.isRegularFile(filePath)) {
          path.add(filePath.getFileName());
        }
      });
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return path;
  }

  /**
   * Reads file.
   *
   * @param file
   * @return content of file or null
   * @throws ReadToJSONArrayException
   */
  public static JSONArray readToJSONArray(final File file) {

    JSONArray ja = null;
    try {
      if (Files.isReadable(file.toPath())) {
        ja = new JSONArray(new String(Files.readAllBytes(file.toPath())));
      }
    } catch (JSONException | IOException e) {
      LOG.error("Couldn't process with parameter: " + file.toString(), e);
    }
    return ja;
  }

  /**
   * Writes a {@link JSONArray} data object with elements of {@link JSONObject} instances to the
   * file.
   *
   * @param data {@link JSONArray}
   * @param file to write
   *
   * @return success
   */
  public static boolean writeFile(final JSONArray data, final File file) {
    try {

      if (file.getParentFile() != null) {
        file.getParentFile().mkdirs();
      }
      file.createNewFile();

      LOG.info("Write file: " + file.toPath().toString() + " array length:" + data.length());

      final FileOutputStream fos = new FileOutputStream(file);
      final OutputStreamWriter osw = new OutputStreamWriter(fos);
      osw.write("[ ");
      for (int i = 0; i < data.length(); i++) {
        osw.write(data.getJSONObject(i).toString());
        if (i < data.length() - 1) {
          osw.write(", ");
        }
      }
      osw.write(" ]");
      osw.close();
      fos.close();

    } catch (final Exception e) {
      LOG.error("Parameter: " + data.toString(2) + " \nfile: " + file.toString(), e);
      return false;
    }
    return true;
  }

  /**
   * Writes the content to the file.
   *
   * @param file
   * @param content
   * @return true without errors
   */
  public static boolean writeToFile(final String file, final String content) {
    boolean rtn = false;
    BufferedWriter out = null;
    try {
      out =
          Files.newBufferedWriter(new File(file).toPath(), Charset.forName(Charsets.UTF_8.name()));
      out.write(content);
      out.close();
      rtn = true;
    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage(), e);
      try {
        out.close();
      } catch (final IOException e1) {
      }
    }
    return rtn;
  }

  /**
   * Gets all files in the given folder
   *
   * @param folder
   * @return
   */
  public static Set<Path> filesInFolderSave(final String folder) {
    try {
      return filesInFolder(folder);
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
      return new HashSet<>();
    }
  }

  /**
   * Gets all files in the given folder
   *
   * @param folder
   * @return
   * @throws IOException
   */
  public static Set<Path> filesInFolder(final String folder) throws IOException {
    return Files.walk(Paths.get(folder))//
        .filter(Files::isRegularFile)//
        .map(Path::toAbsolutePath)//
        .collect(Collectors.toSet()//
        );
  }

  /**
   * Opens a BufferedReader to read all files.
   *
   * @param list of files
   * @return BufferedReader
   */
  public static BufferedReader openFileToRead(final List<String> files) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(
          new SequenceInputStream(new FilesEnumeration(files.toArray(new String[files.size()]))),
          Charsets.UTF_8.name()));
    } catch (final UnsupportedEncodingException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return br;
  }

  /**
   * Opens a BufferedReader to read a file.
   *
   * @param pathToFile path to the file
   * @return BufferedReader
   */
  public static BufferedReader getBufferedReader(final Path pathToFile) {
    return getBufferedReader(pathToFile, Charsets.UTF_8.name());
  }

  /**
   * Opens a BufferedReader to read a file.
   *
   * @param pathToFile path to the file
   * @param encoding used encoding (e.g.,Charsets.UTF_8.name())
   * @return BufferedReader
   */
  public static BufferedReader getBufferedReader(final Path pathToFile, final String encoding) {
    try {
      return Files.newBufferedReader(pathToFile, Charset.forName(encoding));
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
    }
    return null;
  }

  /**
   * Opens a BufferedWriter to write a file.
   *
   * @param pathToFile path to the file
   * @return BufferedWriter
   */
  public static BufferedWriter openFileToWrite(final String pathToFile) {
    return openFileToWrite(pathToFile, Charsets.UTF_8.name());
  }

  /**
   * Opens a BufferedWriter to write a file.
   *
   * @param pathToFile path to the file
   * @param encoding used encoding (e.g.,Charsets.UTF_8.name())
   * @return BufferedWriter
   */
  public static BufferedWriter openFileToWrite(final String pathToFile, final String encoding) {
    try {
      return Files.newBufferedWriter(new File(pathToFile).toPath(), Charset.forName(encoding));
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
      return null;
    }
  }

  /**
   * Reads a file to List.
   *
   * @param pathToFile path to the file
   * @param commentSymbol a line in the given file starting with the commentSymbole will be ignored
   * @return list of lines
   */
  public static List<String> fileToList(final Path pathToFile, final String commentSymbol) {
    return fileToListCatched(pathToFile, Charsets.UTF_8.name(), commentSymbol);
  }

  /**
   * Reads a file to List.
   *
   * @param pathToFile path to the used file
   * @return list of lines
   */
  public static List<String> fileToList(final Path pathToFile) {
    return fileToListCatched(pathToFile, Charsets.UTF_8.name(), "");
  }

  /**
   * Reads a file to List.
   *
   * @param pathToFile path to the used file
   * @param encoding used encoding (e.g.,Charsets.UTF_8.name())
   * @param commentSymbol a line in the given file starting with the commentSymbole will be ignored
   * @return list of lines
   */
  public static List<String> fileToList(final Path pathToFile, final String encoding,
      final String commentSymbol) throws IOException {
    final BufferedReader br = getBufferedReader(pathToFile, encoding);
    final List<String> results = new ArrayList<>();
    String line;
    while ((line = br.readLine()) != null) {
      if (commentSymbol.isEmpty()) {
        results.add(line);
      } else if (!commentSymbol.isEmpty() && !line.startsWith(commentSymbol)) {
        results.add(line);
      }
    }
    br.close();
    return results;
  }

  public static List<String> fileToListCatched(final Path pathToFile, final String encoding,
      final String commentSymbol) {
    try {
      return fileToList(pathToFile, encoding, commentSymbol);
    } catch (final IOException e) {
      LOG.error(e.getLocalizedMessage(), e);
      return new ArrayList<>();
    }
  }

  /**
   *
   * Downloads and copies to file.
   *
   * @param url source to download and copy
   * @param file path to the file
   */
  public static void download(final URL url, final String file) {
    download(url, new File(file));
  }

  /**
   *
   * Downloads and copies to file.
   *
   * @param url source to download and copy
   * @param file path to the file
   */
  public static void download(final URL url, final File file) {
    if (!fileExists(file.toPath())) {
      try {
        org.apache.commons.io.FileUtils.copyURLToFile(url, file);
      } catch (final IOException e) {
        final String msg = "" + "\n Error while downloading " + url.toString() + " and copying to "
            + file.toString();
        LOG.error(msg, e);
      }
    }
  }

  /**
   * Checks if a file exists.
   *
   * @param file
   * @return true if the file exists.
   */
  public static boolean fileExists(final String file) {
    return fileExists(Paths.get(file));
  }

  /**
   * Checks if a file exists.
   *
   * @param file
   * @return true if the file exists.
   */
  public static boolean fileExists(final Path path) {
    if (Files.exists(path) && !Files.isDirectory(path) && Files.isReadable(path)) {
      return true;
    }
    return false;
  }


  /**
   * Opens a BufferedReader to read a file.
   *
   * @param pathToFile path to the file
   * @return BufferedReader
   */
  public static BufferedReader openFileToRead(final String pathToFile) {
    return openFileToRead(pathToFile, "UTF-8");
  }

  /**
   * Opens a BufferedReader to read a file.
   *
   * @param pathToFile path to the file
   * @param encoding used encoding (e.g.,"UTF-8")
   * @return BufferedReader
   */
  public static BufferedReader openFileToRead(final String pathToFile, final String encoding) {
    try {
      return Files.newBufferedReader(new File(pathToFile).toPath(), Charset.forName(encoding));
    } catch (final Exception e) {
      LOG.error(e.getLocalizedMessage(), e);
      return null;
    }
  }


  /**
   * Reads a file to List.
   *
   * @param pathToFile path to the used file
   * @return list of lines
   */
  public static List<String> fileToList(final String pathToFile) {
    return fileToList(pathToFile, "UTF-8", "");
  }

  /**
   * Reads a file to List.
   *
   * @param pathToFile path to the used file
   * @param encoding used encoding (e.g.,"UTF-8")
   * @param commentSymbol a line in the given file starting with the commentSymbole will be ignored
   * @return list of lines
   */
  public static List<String> fileToList(final String pathToFile, final String encoding,
      final String commentSymbol) {
    return read(openFileToRead(pathToFile, encoding), commentSymbol);
  }

  public static List<String> read(final BufferedReader br) {
    return read(br, "");

  }

  public static List<String> read(final BufferedReader br, final String commentSymbol) {
    final List<String> results = new ArrayList<>();
    try {
      String line;
      while ((line = br.readLine()) != null) {
        if (!commentSymbol.isEmpty() && line.startsWith(commentSymbol)) {

        } else {
          results.add(line);
        }
      }
      br.close();
    } catch (final IOException e) {
      LOG.error("\n", e);
    }
    return results;

  }

  /**
   * Checks if a file exists.
   *
   * @param file
   * @return true if the file exists.
   */
  public static boolean fileExists(final File file) {
    if (file.exists() && !file.isDirectory()) {
      LOG.debug("File " + file.toString() + " exists.");
      return true;
    }
    return false;
  }
}
