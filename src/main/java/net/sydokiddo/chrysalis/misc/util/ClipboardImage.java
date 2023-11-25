package net.sydokiddo.chrysalis.misc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

@Environment(EnvType.CLIENT)
public record ClipboardImage(Image image) implements Transferable {

    /**
     * Gets various data from the taken screenshot in order to copy it to the user's clipboard
     **/

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (isDataFlavorSupported(flavor)) return image;
        throw new UnsupportedFlavorException(flavor);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor == DataFlavor.imageFlavor;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }
}