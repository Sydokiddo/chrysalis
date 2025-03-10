package net.sydokiddo.chrysalis.util.technical.splash_texts;

import com.google.common.collect.Iterables;
import net.minecraft.client.User;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sydokiddo.chrysalis.util.technical.splash_texts.types.SimpleSplashText;
import net.sydokiddo.chrysalis.util.technical.splash_texts.types.SplashText;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CSplashManager extends SplashManager {

    /**
     * The splash manager for ChrysalisMod's custom splash text system.
     **/

    public CSplashManager(User user) {
        super(user);
    }

    public SplashText getRandomSplash() {

        List<SplashText> splashes = SplashTextLoader.INSTANCE.getSplashTexts();
        if (splashes.isEmpty()) return new SimpleSplashText("");

        Iterator<SplashText> iterator = Iterables.cycle(splashes).iterator();
        int poolWeight = SplashTextLoader.INSTANCE.getMaxWeight();
        Collections.shuffle(splashes);

        while (true) {
            SplashText nextSplash = iterator.next();
            poolWeight -= nextSplash.getWeight() * ((splashes.size() / 100) + 1);
            if (poolWeight > 0) continue;
            return nextSplash;
        }
    }

    @Override
    public SplashRenderer getSplash() {
        return this.getRandomSplash().renderer();
    }
}