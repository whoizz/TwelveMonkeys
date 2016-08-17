package com.twelvemonkeys.imageio.plugins.svg;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.junit.Test;

import com.twelvemonkeys.imageio.stream.ByteArrayImageInputStream;
import com.twelvemonkeys.imageio.stream.URLImageInputStreamSpi;

/**
 * SVGImageReaderSpiTest.
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: harald.kuhr$
 * @version $Id: SVGImageReaderSpiTest.java,v 1.0 08/08/16 harald.kuhr Exp$
 */
public class SVGImageReaderSpiTest {

    private static final String[] VALID_INPUTS = {
            "/svg/Android_robot.svg", // Minimal, no xml dec, no namespace
            "/svg/batikLogo.svg",     // xml dec, comments, namespace
            "/svg/blue-square.svg",   // xml dec, namespace
            "/svg/red-square.svg",
    };

    private static final String[] INVALID_INPUTS = {
        "<xml>",
        "<",
        "<?",
        "<?1",
        "<?12",
        "<?123", // #275 Infinite loop issue
        "<!--",
        "<!-- ", // #275 Infinite loop issue
        "<?123?>", // #275 Infinite loop issue
        "<svg",
    };

    static {
        IIORegistry.getDefaultInstance().registerServiceProvider(new URLImageInputStreamSpi());
        ImageIO.setUseCache(false);
    }

    private final ImageReaderSpi provider = new SVGImageReaderSpi();

    @Test
    public void canDecodeInput() throws Exception {
        for (String validInput : VALID_INPUTS) {
        	ImageInputStream input = null;
            try {
            	input = ImageIO.createImageInputStream(getClass().getResource(validInput));
                assertTrue("Can't read valid input: " + validInput, provider.canDecodeInput(input));
            }
            finally {
            	if (input != null) {
            		input.close();
            	}
            }
        }
    }

    // Test will time out, if EOFs are not properly detected, see #275
    @Test(timeout = 5000)
    public void canDecodeInputInvalid() throws Exception {
        for (String invalidInput : INVALID_INPUTS) {
        	ImageInputStream input = null;
            try {
            	input = new ByteArrayImageInputStream(invalidInput.getBytes("UTF-8"));
                assertFalse("Claims to read invalid input:" + invalidInput, provider.canDecodeInput(input));
            }
            finally {
                if (input != null) {
                	input.close();
                }
            }
        }
    }
}