package org.ucd.shortlink.project.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Validate Date Type Enumeration
 */

@RequiredArgsConstructor
public enum ValiDateTypeEnum {
    /**
     * Date permanent validate
     */
    PERMANENT(0),


    /**
     * Validate date can be customized configured
     */
    CUSTOM(1);

    @Getter
    private final int type;
}
