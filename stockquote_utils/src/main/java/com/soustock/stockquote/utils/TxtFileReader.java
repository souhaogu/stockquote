package com.soustock.stockquote.utils;

import java.io.*;

public class TxtFileReader {
    private InputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    public TxtFileReader(String filePathStr) throws FileNotFoundException {
        inputStream = new FileInputStream(filePathStr);
        inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
    }

    public String readLine() throws IOException {
        return bufferedReader.readLine();
    }

    public void close() throws IOException {
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
    }
}
