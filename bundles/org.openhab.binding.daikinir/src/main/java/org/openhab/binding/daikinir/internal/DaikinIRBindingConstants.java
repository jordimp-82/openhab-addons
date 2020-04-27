/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.daikinir.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link DaikinIRBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Jordi - Initial contribution
 */
@NonNullByDefault
public class DaikinIRBindingConstants {

    private static final String BINDING_ID = "daikinir";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_AC_UNIT = new ThingTypeUID(BINDING_ID, "daikin_ac_unit");

    // List of all Channel ids
    public static final String CHANNEL_AC_POWER = "power";
    public static final String CHANNEL_AC_TEMP = "settemp";
    public static final String CHANNEL_AC_MODE = "mode";
    public static final String CHANNEL_FAN_MODE = "fanmode";
    public static final String CHANNEL_FAN_SWING = "swing";
    public static final String CHANNEL_ECONO = "econo";
    public static final String CHANNEL_COMFORT = "comfort";
    public static final String CHANNEL_POWERFUL = "powerful";
    public static final String CHANNEL_TIMER_MODE = "timermode";
    public static final String CHANNEL_TIMER_DURATION = "timerduration";
    public static final String CHANNEL_STATE_CHANGED = "statechanged";

}
