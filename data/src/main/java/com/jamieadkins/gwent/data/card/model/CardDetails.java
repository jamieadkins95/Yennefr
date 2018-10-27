package com.jamieadkins.gwent.data.card.model;

import java.util.List;
import java.util.Map;

public class CardDetails {
    private static final String DEFAULT_LOCALE = "en-US";

    private List<String> categoryIds;
    private List<String> keywords;
    private String faction;
    private Map<String, String> flavor;
    private Map<String, String> info;
    private String ingameId;
    private List<String> loyalties;
    private Map<String, String> name;
    private List<String> positions;
    private List<String> related;
    private boolean released;
    private String cardSet;
    private int strength;
    private int provision;
    private int mulligans;
    private String type;
    private Map<String, Variation> variations;

    private String patch;

    public CardDetails() {
        // Required empty constructor for Firebase.
    }

    public String getName(String locale) {
        if (name != null) {
            return name.get(locale);
        } else {
            return "";
        }
    }

    public String getInfo(String locale) {
        if (info != null) {
            return info.get(locale);
        } else {
            return "";
        }
    }

    public String getFlavor(String locale) {
        if (flavor != null) {
            return flavor.get(locale);
        } else {
            return "";
        }
    }

    public String getFaction() {
        return faction;
    }

    public String getIngameId() {
        return ingameId;
    }

    public String getType() {
        return type;
    }

    public int getStrength() {
        return strength;
    }

    public boolean isReleased() {
        return released;
    }

    public Map<String, Variation> getVariations() {
        return variations;
    }

    public List<String> getRelated() {
        return related;
    }

    public String getCardSet() {
        return cardSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CardDetails) {
            return ingameId.equals(((CardDetails) obj).getIngameId());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name.get(DEFAULT_LOCALE);
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public List<String> getCategories() {
        return categoryIds;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Map<String, String> getFlavor() {
        return flavor;
    }

    public Map<String, String> getInfo() {
        return info;
    }

    public List<String> getLoyalties() {
        return loyalties;
    }

    public Map<String, String> getName() {
        return name;
    }

    public List<String> getPositions() {
        return positions;
    }


    public String getImage() {
        for (String key : variations.keySet()) {
            if (variations.get(key).getArt() != null) {
                return variations.get(key).getArt().getLow();
            }
        }

        return null;
    }

    public String getRarity() {
        for (String key : variations.keySet()) {
            return variations.get(key).getRarity();
        }

        return Rarity.COMMON_ID;
    }

    public int getProvision() {
        return provision;
    }

    public void setProvision(int provision) {
        this.provision = provision;
    }

    public int getMulligans() {
        return mulligans;
    }

    public void setMulligans(int mulligans) {
        this.mulligans = mulligans;
    }

    public static class Variation {

        private String availability;
        private String rarity;
        private Art art;
        private Map<String, Integer> craft;
        private Map<String, Integer> mill;
        private boolean collectible;
        private String variationId;

        public Variation() {
            // Required empty constructor for Firebase.
        }

        public String getAvailability() {
            return availability;
        }

        public String getRarity() {
            return rarity;
        }

        public Art getArt() {
            return art;
        }

        public Map<String, Integer> getCraft() {
            return craft;
        }

        public Map<String, Integer> getMill() {
            return mill;
        }

        public boolean isCollectible() {
            return collectible;
        }

        public String getVariationId() {
            return variationId;
        }


        public static int getVariationNumber(String variationId) {
            return Integer.valueOf(variationId.substring(
                    Math.max(variationId.length() - 2, 0),
                    Math.max(variationId.length() - 1, 0)
            )) + 1;
        }
    }

    public static class Art {
        private String original;
        private String high;
        private String medium;
        private String low;
        private String thumbnail;

        private String artist;

        public Art() {
            // Required empty constructor for Firebase.
        }

        public String getArtist() {
            return artist;
        }

        public String getOriginal() {
            return original;
        }

        public String getHigh() {
            return high;
        }

        public String getMedium() {
            return medium;
        }

        public String getLow() {
            return low;
        }

        public String getThumbnail() {
            return thumbnail;
        }
    }
}
