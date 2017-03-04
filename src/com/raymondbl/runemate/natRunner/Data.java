package com.raymondbl.runemate.natRunner;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.web.Web;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Filter;

public final class Data {

    public static void initialize(RaysNatRunner runner, RaysNatRunnerGUI gui) {
        player = Players.getLocal();
        Data.runner = runner;
        Data.gui = gui;
        web = new Web();
    }

    public static Web web;
    public static Player player;
    public static RaysNatRunner runner;
    public static RaysNatRunnerGUI gui;
    public static final String ASTRAL = "Astral rune";
    public static final String COSMIC = "Cosmic rune";
    public static final int NATURE_RUNE_ID = 561;
    public static final int COSMIC_RUNE_ID = 564;
    public static final int ASTRAL_RUNE_ID = 9075;
    public static final int PURE_ESS = 7936;
    public static final Pouch SMALL_POUCH = new Pouch(5509);
    public static final Pouch MEDIUM_POUCH = new Pouch(5510);
    public static final Pouch LARGE_POUCH = new Pouch(5512);
    public static final Pouch GIANT_POUCH = new Pouch(5514);
    public static final Pouch NEW_MASSIVE_POUCH = new Pouch(24204);
    public static final Pouch DEGR_MEDIUM_POUCH = new Pouch(5511);
    public static final Pouch DEGR_LARGE_POUCH = new Pouch(5513);
    public static final Pouch DEGR_GIANT_POUCH = new Pouch(5515);
    public static final Pouch MASSIVE_POUCH = new Pouch(24205);
    public static final Pouch[] NON_DEGR_POUCHES_ARRAY = {GIANT_POUCH,
            LARGE_POUCH, MEDIUM_POUCH, SMALL_POUCH};

    public static final Pouch[] POUCHES_ARRAY = {SMALL_POUCH, MEDIUM_POUCH,
            LARGE_POUCH, GIANT_POUCH, NEW_MASSIVE_POUCH, DEGR_MEDIUM_POUCH,
            DEGR_LARGE_POUCH, DEGR_GIANT_POUCH, MASSIVE_POUCH};

    public static final int[] POUCHES_IDS = {SMALL_POUCH.getId(),
            MEDIUM_POUCH.getId(), LARGE_POUCH.getId(), GIANT_POUCH.getId(),
            NEW_MASSIVE_POUCH.getId(), DEGR_MEDIUM_POUCH.getId(),
            DEGR_LARGE_POUCH.getId(), DEGR_GIANT_POUCH.getId(), MASSIVE_POUCH.getId()};

    public static final Filter<SpriteItem> POUCHES_FILTER = new Filter<SpriteItem>() {
        @Override
        public boolean accepts(SpriteItem spriteItem) {
            int id = spriteItem.getId();
            for (Pouch pouch : POUCHES_ARRAY) {
                if (id == pouch.getId()) {
                    return true;
                }
            }
            return false;
        }
    };

    public static final Pouch[] DEGR_POUCHES = {DEGR_MEDIUM_POUCH,
            DEGR_LARGE_POUCH, DEGR_GIANT_POUCH};
    public static final Filter<SpriteItem> DEGR_POUCHES_FILTER = new Filter<SpriteItem>() {

        @Override
        public boolean accepts(SpriteItem item) {
            int itemId = item.getId();
            for (Pouch pouch : DEGR_POUCHES) {
                if (itemId == pouch.getId())
                    return true;
            }
            return false;
        }

    };
    public static final Filter<SpriteItem> ESS_FILTER = new Filter<SpriteItem>() {
        @Override
        public boolean accepts(SpriteItem spriteItem) {
            return spriteItem.getId() == PURE_ESS;
        }
    };
    public static final Coordinate OBELISK = new Coordinate(2851, 3027, 0);
    public static final Coordinate RUINS = new Coordinate(2869, 3019, 0);
    public static final String RUINS_NAME = "Mysterious ruins";
    public static final Coordinate ALTAR = new Coordinate(2400, 4841, 0);

    public static final int OBELISK_ID = 29938;
    public static final int RUINS_ID = 2460;
    public static final int ALTAR_ID = 2486;

    public static final String[] TELEPORTOPTIONS =
            {
                    "Fight Pit", "Fight Cave", "Fight Kiln", "Fight Cauldron"
            };

    public static final String OBELISK_NAME = "Small obelisk";

    public static final int RING_8 = 2552;
    public static final int RING_7 = 2554;
    public static final int RING_6 = 2556;
    public static final int RING_5 = 2558;
    public static final int RING_4 = 2560;
    public static final int RING_3 = 2562;
    public static final int RING_2 = 2564;
    public static final int RING_1 = 2566;
    public static final int[] RINGS = {RING_8, RING_7, RING_6,
            RING_5, RING_4, RING_3, RING_2, RING_1};
    public static final Filter<SpriteItem> RING_FILTER = new Filter<SpriteItem>() {

        @Override
        public boolean accepts(SpriteItem spriteItem) {
            for (int i : RINGS) {
                if (spriteItem.getId() == i)
                    return true;
            }
            return false;
        }

    };

    public static final int NEXT_TEXTURE_ID = 18635;
    public static final String LARGE_POUCH_TEXT = "Repair large pouch for 9,000 gp.";
    public static final String GIANT_POUCH_TEXT = "Repair giant pouch for 12,000 gp.";
    public static final String MED_POUCH_TEXT = "I've lost my medium-sized pouch.";
    public static final int[] SUMMONING = {1485, 51};
    public static final int FAMILIAR = 29;
    public static final String GRAAHK_POUCH = "Spirit graahk pouch";
    public static final int GRAAHK_POUCH_ID = 12810;
    public static final Filter<SpriteItem> GRAAHK_POUCH_FILTER = new Filter<SpriteItem>() {
        @Override
        public boolean accepts(SpriteItem spriteItem) {
            return spriteItem.getId() == GRAAHK_POUCH_ID;
        }
    };

    public static final String KORVAK_NAME = "Wizard Korvak";

    public static final int WICKED_HOOD_ID = 22332;
    public static final String WICKED_HOOD = "Wicked hood";
    public static final Filter<SpriteItem> WICKED_FILTER = new Filter<SpriteItem>() {
        @Override
        public boolean accepts(SpriteItem item) {
            return item.getId() == WICKED_HOOD_ID;
        }
    };
    public static final int RUNECRAFTING_PORTAL = 79518;
    public static final int Teleport = 530;
    public static final int KORVAK = 8029;
    public static final Filter<Npc> KORVAK_FILTER = new Filter<Npc>() {
        @Override
        public boolean accepts(Npc npc) {
            return npc.getId() == KORVAK;
        }
    };
    public static final int[] KORVAK_REPAIR = {1188, 12};
    public static final int bankInterfaceContainer = 762;
    public static final int banksInventoryInterfaceContainer = 7;
    public static final String GUILD_PORTAL = "Runecrafting Guild portal";
    public static final int NPC_CONTACT = 1734;
    public static final String DARK_MAGE = "Dark mage";

    public enum Teleport {
        DUEL, KIN, PIT, CAVE, KILN, CAULDRON;
    }

    public enum Repair {
        NPC_CONTACT, GUILD, NONE;
    }

    public enum Preset {
        PRESET1, PRESET2, NONE;
    }

}
