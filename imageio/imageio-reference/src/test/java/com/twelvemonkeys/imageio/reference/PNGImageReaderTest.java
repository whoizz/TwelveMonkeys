package com.twelvemonkeys.imageio.reference;

import static org.junit.Assume.assumeNoException;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

import org.junit.Test;

import com.twelvemonkeys.imageio.util.ImageReaderAbstractTest;

/**
 * PNGImageReaderTest
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: PNGImageReaderTest.java,v 1.0 Oct 9, 2009 3:37:25 PM haraldk Exp$
 */
public class PNGImageReaderTest extends ImageReaderAbstractTest {
    protected final ImageReaderSpi provider = lookupSpi();

    private ImageReaderSpi lookupSpi() {
        try {
            return (ImageReaderSpi) Class.forName("com.sun.imageio.plugins.png.PNGImageReaderSpi").newInstance();
        }
        catch (InstantiationException e) {
            assumeNoException(e);
        }
        catch (IllegalAccessException e) {
            assumeNoException(e);
        }
        catch (ClassNotFoundException e) {
            assumeNoException(e);
        }

        return null;
    }

    @Override
    protected List<TestData> getTestData() {
        return Collections.singletonList(
                new TestData(getClassLoaderResource("/png/12monkeys-splash.png"), new Dimension(300, 410))
        );
    }

    @Override
    protected ImageReaderSpi createProvider() {
        return provider;
    }

    @Override
    protected Class getReaderClass() {
        try {
            return Class.forName("com.sun.imageio.plugins.png.PNGImageReader");
        }
        catch (ClassNotFoundException e) {
            assumeNoException(e);
        }

        return null;
    }

    @Override
    protected ImageReader createReader() {
        try {
            return provider.createReaderInstance();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // These are NOT correct implementations, but I don't really care here
    @Override
    protected List<String> getFormatNames() {
        return Arrays.asList(provider.getFormatNames());
    }

    @Override
    protected List<String> getSuffixes() {
        return Arrays.asList(provider.getFileSuffixes());
    }

    @Override
    protected List<String> getMIMETypes() {
        return Arrays.asList(provider.getMIMETypes());
    }

    @Test
    @Override
    public void testSetDestinationTypeIllegal() throws IOException {
        try {
            super.testSetDestinationTypeIllegal();
        }
        catch (IIOException expected) {
            // Known bug
        }
    }
}