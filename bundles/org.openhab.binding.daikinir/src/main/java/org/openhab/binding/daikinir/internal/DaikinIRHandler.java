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

import static org.openhab.binding.daikinir.internal.DaikinIRBindingConstants.*;

import javax.measure.quantity.Temperature;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.library.unit.SIUnits;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.daikinir.internal.api.DaikinUnitState.AC_MODE;
import org.openhab.binding.daikinir.internal.api.DaikinUnitState.FAN_MODE;
import org.openhab.binding.daikinir.internal.api.DaikinUnitTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DaikinIRHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jordi - Initial contribution
 */
@NonNullByDefault
public class DaikinIRHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(DaikinIRHandler.class);

    private @Nullable DaikinIRConfiguration config;

    private final DaikinUnitTransmitter daikinUnit = new DaikinUnitTransmitter(true);

    public DaikinIRHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        switch (channelUID.getId()) {
            case CHANNEL_AC_POWER:
                if (command instanceof OnOffType) {
                    if (((OnOffType) command).equals(OnOffType.ON)) {
                        daikinUnit.powerOn();
                    } else {
                        daikinUnit.powerOff();
                    }
                    return;
                }
                if (command instanceof RefreshType) {
                    // TODO: handle data refresh
                }
                // TODO: handle command

                // Note: if communication with thing fails for some reason,
                // indicate that by setting the status with detail information:
                // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                // "Could not control device at IP address x.x.x.x");
                break;

            case CHANNEL_AC_TEMP:
                int newTemperature;
                if (command instanceof DecimalType) {
                    newTemperature = ((DecimalType) command).intValue();
                } else if (command instanceof QuantityType) {
                    newTemperature = ((QuantityType<Temperature>) command).toUnit(SIUnits.CELSIUS).intValue();
                } else {
                    return;
                }
                daikinUnit.setTemperature(newTemperature);
                break;

            case CHANNEL_AC_MODE:
                if (command instanceof StringType) {
                    daikinUnit.setAcMode(AC_MODE.valueOf(((StringType) command).toString()));
                    return;
                }
                break;

            case CHANNEL_FAN_MODE:
                if (command instanceof StringType) {
                    setFanMode((((StringType) command).toString()));
                    return;
                }
                break;

            case CHANNEL_FAN_SWING:
                if (command instanceof OnOffType) {
                    if (((OnOffType) command).equals(OnOffType.ON)) {
                        daikinUnit.setSwing(true);
                    } else {
                        daikinUnit.setSwing(false);
                    }
                    return;
                }
                break;

            case CHANNEL_ECONO:
                if (command instanceof OnOffType) {
                    if (((OnOffType) command).equals(OnOffType.ON)) {
                        daikinUnit.setEcono(true);
                    } else {
                        daikinUnit.setEcono(false);
                    }
                    return;
                }
                break;

            case CHANNEL_COMFORT:
                if (command instanceof OnOffType) {
                    if (((OnOffType) command).equals(OnOffType.ON)) {
                        daikinUnit.setComfort(true);
                    } else {
                        daikinUnit.setComfort(false);
                    }
                    return;
                }
                break;

            case CHANNEL_POWERFUL:
                if (command instanceof OnOffType) {
                    if (((OnOffType) command).equals(OnOffType.ON)) {
                        daikinUnit.setPowerful(true);
                    } else {
                        daikinUnit.setPowerful(false);
                    }
                    return;
                }
                break;
        }
    }

    private void setFanMode(String fanMode) {

        switch (fanMode) {

            case "AUTO":
                daikinUnit.setFanMode(FAN_MODE.AUTOMATIC, 0);
                break;
            case "SILENT":
                daikinUnit.setFanMode(FAN_MODE.SILENT, 0);
                break;
            case "MANUAL":
                daikinUnit.setFanMode(FAN_MODE.MANUAL, 1);
                break;
            case "LEVEL_2":
                daikinUnit.setFanMode(FAN_MODE.MANUAL, 2);
                break;
            case "LEVEL_3":
                daikinUnit.setFanMode(FAN_MODE.MANUAL, 3);
                break;
            case "LEVEL_4":
                daikinUnit.setFanMode(FAN_MODE.MANUAL, 4);
                break;
            case "LEVEL_5":
                daikinUnit.setFanMode(FAN_MODE.MANUAL, 5);
                break;
            default:
                logger.warn("Received unknown Fan Mode");

        }
    }

    @Override
    public void initialize() {
        logger.info("Start initializing!");
        config = getConfigAs(DaikinIRConfiguration.class);

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly. Also, before leaving this method a thing
        // status from one of ONLINE, OFFLINE or UNKNOWN must be set. This might already be the real thing status in
        // case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);

        // Example for background initialization:
        scheduler.execute(() -> {
            boolean thingReachable = true; // <background task with long running initialization here>
            // when done do:
            if (thingReachable) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE);
            }
        });

        // logger.debug("Finished initializing!");

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }
}
