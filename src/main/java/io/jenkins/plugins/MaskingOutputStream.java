package io.jenkins.plugins;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

/**
 * An OutputStream wrapper with a list of values to mask from the output stream
 */
public class MaskingOutputStream extends OutputStream {
    private static final Logger LOGGER = Logger.getLogger(MaskingOutputStream.class.getName());

    private final OutputStream delegate;
    private final List<String> valuesToMaskList;

    public MaskingOutputStream(OutputStream delegate,
            List<String> valuesToMaskList) {

        LOGGER.log(Level.FINE, "MaskingOutputStream constructor called");
        this.delegate = delegate;
        this.valuesToMaskList = valuesToMaskList;
    }

    @Override
    public void write(int b) throws IOException {
        this.delegate.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    /**
     * Mask values in the byte array before writing to the output stream
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        LOGGER.log(Level.FINE, "MaskingOutputStream is writing to output stream");
        final char asterisk = '*';

        for (String value : valuesToMaskList) {
            int start = 0;
            int index = indexOf(b, off, len, start, value.getBytes());
            while (index != -1) {
                LOGGER.log(Level.FINE, "MaskingOutputStream detected a value and is masking it");
                for (int i = 0; i < value.length(); i++) {
                    b[i + index] = (byte) asterisk;
                }
                start = index + value.length();
                index = indexOf(b, off, len, start, value.getBytes());
            }
        }

        this.delegate.write(b, off, len);
    }

    /**
     * Find the index of the target byte array in the byte array
     *
     * This code is modeled after Guava's implementation
     *
     * @param array what to search in
     * @param off offset into the array
     * @param len length of bytes
     * @param start where to start search from relative to offset (starts at 0)
     * @param target what to search for
     * @return 0 or higher, index of match. -1 for not found
     */
    public int indexOf(byte[] array, int off, int len, int start, byte[] target) {
        if (target.length == 0) {
            return -1;
        }

        outer: for (int i = off + start; i < len - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
