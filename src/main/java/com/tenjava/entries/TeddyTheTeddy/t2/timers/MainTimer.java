package com.tenjava.entries.TeddyTheTeddy.t2.timers;

import com.tenjava.entries.TeddyTheTeddy.t2.TenJava;
import com.tenjava.entries.TeddyTheTeddy.t2.Util;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.FallingSand;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 12/07/2014.
 */
public class MainTimer extends BukkitRunnable {

    private final TenJava pl;

    public MainTimer(TenJava pl) {
        this.pl = pl;
    }

    @Override
    public void run() {
        List<Location> toRemove = new ArrayList<Location>();
        Map<Location, Integer> toSet = new HashMap<Location, Integer>();
        for (Location l : pl.getLoadedTransmutationStructures().keySet()) {
            if (Util.checkStructure(l.clone())) {
                if(Util.checkLight(l.clone())) {
                    if (pl.getLoadedTransmutationStructures().get(l) == 0) {
                        toRemove.add(l);
                        if (pl.getBlockFromTo().containsKey(l.getBlock().getType())) {
                            FallingBlock block = l.getWorld().spawnFallingBlock(l, pl.getBlockFromTo().get(l.getBlock().getType()), (byte) 0);
                            block.setVelocity(new Vector(0, 1.5, 0));
                            l.getWorld().playSound(l, Sound.LEVEL_UP, 1, 1);
                            l.getWorld().playEffect(l, Effect.MOBSPAWNER_FLAMES, 10);
                            l.getBlock().setType(Material.AIR);

                            spawnFirework(l);
                        }
                    } else {
                        toSet.put(l, pl.getLoadedTransmutationStructures().get(l) - 1);
                    }
                }
            } else {
                toRemove.add(l);
                l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 10, false, false);
            }
        }

        for (Location l : toRemove) {
            pl.getLoadedTransmutationStructures().remove(l);
        }

        for (Location l : toSet.keySet()) {
            pl.getLoadedTransmutationStructures().put(l, toSet.get(l));
        }
    }

    private void spawnFirework(Location l){
        Firework firework = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(false).trail(false).withColor(Color.GREEN).withFade(Color.PURPLE).build();
        fireworkMeta.addEffect(effect);

        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }
}
