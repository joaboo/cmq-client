package com.cmq.client.common.util;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.cmq.client.common.Constants;

public class IOUtils {

	public static String toString(InputStream input) throws IOException {
		return toString(input, Constants.DEFAULT_CHARSET);
	}

	public static String toString(InputStream input, Charset encoding) throws IOException {
		return toString(new InputStreamReader(input, encoding));
	}

	public static String toString(Reader reader) throws IOException {
		CharArrayWriter sw = new CharArrayWriter();
		copy(reader, sw);
		return sw.toString();
	}

	public static long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[1 << 12];
		long count = 0;
		for (int n = 0; (n = input.read(buffer)) >= 0;) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = toBufferedReader(input);
		List<String> ret = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			ret.add(line);
		}
		return ret;
	}

	private static BufferedReader toBufferedReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

}
