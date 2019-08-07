package com.gaga.protocol;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyFilterOutputSteam extends FilterOutputStream {
    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public MyFilterOutputSteam(OutputStream out) {
        super(out);
    }

    public void writeCRLF() throws IOException {
        write('\r');
        write('\n');
    }

    public void writeInteger(long v) throws IOException {
        if (v < 0) {
            write('-');
            v = -v;
        }
        byte[] bytes = Long.toString(v).getBytes(Protocol.charset());
        write(bytes);
    }

}
