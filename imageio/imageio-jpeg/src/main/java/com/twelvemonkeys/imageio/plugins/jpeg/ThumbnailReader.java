/*
 * Copyright (c) 2012, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name "TwelveMonkeys" nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.imageio.plugins.jpeg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * ThumbnailReader
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @author last modified by $Author: haraldk$
 * @version $Id: ThumbnailReader.java,v 1.0 18.04.12 12:22 haraldk Exp$
 */
abstract class ThumbnailReader {

    private final JPEGImageReader parent;
    protected final int imageIndex;
    protected final int thumbnailIndex;

    protected ThumbnailReader(final JPEGImageReader parent, final int imageIndex, final int thumbnailIndex) {
        this.parent = parent;
        this.imageIndex = imageIndex;
        this.thumbnailIndex = thumbnailIndex;
    }
    protected final void processThumbnailStarted() {
        parent.processThumbnailStarted(imageIndex, thumbnailIndex);
    }

    protected final void processThumbnailProgress(float percentageDone) {
        parent.processThumbnailProgress(percentageDone);
    }

    protected final void processThumbnailComplete() {
        parent.processThumbnailComplete();
    }

    static protected BufferedImage readJPEGThumbnail(InputStream stream) throws IOException {
        return ImageIO.read(stream);
    }

    static protected BufferedImage readRawThumbnail(final byte[] thumbnail, final int size, final int offset, int w, int h) {
        DataBufferByte buffer = new DataBufferByte(thumbnail, size, offset);
        WritableRaster raster = Raster.createInterleavedRaster(buffer, w, h, w * 3, 3, new int[] {0, 1, 2}, null);
        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

        return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
    }

    public abstract BufferedImage read() throws IOException;

    public abstract int getWidth() throws IOException;

    public abstract int getHeight() throws IOException;
}
