package net.sydokiddo.chrysalis.util.technical;

import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public record ClipboardImage(Image image) implements Transferable {

    /**
     * Gets various data from the taken screenshot in order to copy it to the user's clipboard.
     **/

    @Override
    public @NotNull Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
        if (this.isDataFlavorSupported(dataFlavor)) return this.image;
        throw new UnsupportedFlavorException(dataFlavor);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return DataFlavor.imageFlavor.equals(dataFlavor);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }
}