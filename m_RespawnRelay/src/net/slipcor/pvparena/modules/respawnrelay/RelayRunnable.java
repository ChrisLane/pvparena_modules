package net.slipcor.pvparena.modules.respawnrelay;

import net.slipcor.pvparena.PVPArena;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaPlayer;
import net.slipcor.pvparena.arena.ArenaPlayer.Status;
import net.slipcor.pvparena.core.Config.CFG;
import net.slipcor.pvparena.core.Debug;
import net.slipcor.pvparena.core.Language.MSG;
import net.slipcor.pvparena.managers.SpawnManager;
import net.slipcor.pvparena.runnables.ArenaRunnable;
import net.slipcor.pvparena.runnables.InventoryRefillRunnable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RelayRunnable extends ArenaRunnable {
    private final ArenaPlayer ap;
    private final Player maybePlayer;
    private final List<ItemStack> drops;
    private final Debug debug = new Debug(77);
    private final RespawnRelay mod;

    public RelayRunnable(final RespawnRelay relay, final Arena arena, final ArenaPlayer ap, final List<ItemStack> drops) {

        super(MSG.MODULE_RESPAWNRELAY_RESPAWNING.getNode(), arena.getArenaConfig().getInt(CFG.MODULES_RESPAWNRELAY_INTERVAL), ap.get(), null, false);
        mod = relay;
        this.ap = ap;
        this.drops = drops;
        this.maybePlayer = ap.get();
    }

    @Override
    protected void commit() {
        debug.i("RelayRunnable commiting", ap.getName());

        Player maybePlayer = this.maybePlayer;

        if (ap.get() == null) {
            if (maybePlayer == null) {
                PVPArena.instance.getLogger().warning("player null: " + ap.getName());
                return;
            }
        } else {
            maybePlayer = ap.get();
        }

        if (ap.getArena() == null) {
            return;
        }

        new InventoryRefillRunnable(ap.getArena(), maybePlayer, drops);
        final String spawn = mod.overrideMap.get(ap.getName());
        SpawnManager.respawn(ap.getArena(), ap, spawn);

        if (ap.getArena() == null) {
            return;
        }
        ap.getArena().unKillPlayer(ap.get(), maybePlayer.getLastDamageCause() == null ? null : ap.get().getLastDamageCause().getCause(), ap.get().getKiller());
        ap.setStatus(Status.FIGHT);
        mod.getRunnerMap().remove(ap.getName());
        mod.overrideMap.remove(ap.getName());
    }

    @Override
    protected void warn() {
        PVPArena.instance.getLogger().warning("RelayRunnable not scheduled yet!");
    }
}
