package net.sydokiddo.chrysalis.util.technical;

import com.mojang.blaze3d.platform.NativeImage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

@OnlyIn(Dist.CLIENT)
public record ClipboardImage(NativeImage nativeImage) implements Transferable {

    /**
     * Gets various data from the taken screenshot in order to copy it to the user's clipboard.
     **/

    @Override
    public @NotNull Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException {
        if (this.isDataFlavorSupported(dataFlavor)) return this.nativeImage;
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