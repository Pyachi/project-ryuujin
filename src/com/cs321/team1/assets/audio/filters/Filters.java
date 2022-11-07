package com.cs321.team1.assets.audio.filters;

/**
 * Enum class specifying all implemented filters and provides simplified access to them
 */
public enum Filters {
    MUFFLE(new MuffleFilter()),
    ECHO(new EchoFilter());
    /**
     * The underlying filter behind the enum type
     */
    public final Filter filter;
    
    Filters(Filter filter) {
        this.filter = filter;
    }
}
