package com.gaga.protocol;

import com.gaga.exception.MyException;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyFilterInputStream extends FilterInputStream {

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected MyFilterInputStream(InputStream in) {
        super(in);
    }

    public static String readLine(InputStream is) throws MyException {
        boolean needRead = true;
        StringBuilder sb = new StringBuilder();
        int b = -1;
        try {
            while (true) {
                if (needRead) {
                    b = is.read();
                    if (b == -1) {
                        throw new RuntimeException("not -1");
                    }
                } else {
                    needRead = true;
                }

                if (b == '\r') {
                    int c = is.read();
                    if (c == -1) {
                        throw new RuntimeException("not -1");
                    }

                    if (c == '\n') {
                        break;
                    }

                    if (c == '\r') {
                        sb.append((char) b);
                        b = c;
                        needRead = false;
                    } else {
                        sb.append((char) b);
                        sb.append((char) c);
                    }
                } else {
                    sb.append((char) b);
                }
            }
        } catch (IOException e) {

        }

        return sb.toString();
    }

    public static long readInteger(InputStream is) throws MyException {
        StringBuilder sb = new StringBuilder();
        boolean isNegative = false;
        try {
            int b = is.read();
            if (b == -1) {
                throw new RuntimeException("not -1");
            }
            if (b == '-') {
                isNegative = true;
            } else {
                sb.append((char) b);
            }
            while (true) {
                b = is.read();
                if (b == -1) {
                    throw new RuntimeException("not -1");
                }
                if (b == '\r') {
                    int c = is.read();
                    if (c == -1) {
                        throw new RuntimeException("not -1");
                    }
                    if (c == '\n') {
                        break;
                    }
                    throw new RuntimeException("\r must be follow by \n");
                } else {
                    sb.append((char) b);
                }
            }
        } catch (IOException e) {

        }
        long value = Long.parseLong(sb.toString());
        if (isNegative) {
            value = -value;
        }
        return value;

    }

}
