/*
 * Copyright (c) 2013-2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.vtn.manager.integrationtest.internal;

import org.apache.felix.dm.Component;
import org.opendaylight.controller.sal.core.ComponentActivatorAbstractBase;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.controller.sal.connection.IPluginInConnectionService;
import org.opendaylight.controller.sal.flowprogrammer.IPluginInFlowProgrammerService;
import org.opendaylight.controller.sal.inventory.IPluginInInventoryService;
import org.opendaylight.controller.sal.inventory.IPluginOutInventoryService;
import org.opendaylight.controller.sal.reader.IPluginInReadService;
import org.opendaylight.controller.sal.topology.IPluginInTopologyService;
import org.opendaylight.controller.sal.topology.IPluginOutTopologyService;
import org.opendaylight.controller.sal.packet.IPluginInDataPacketService;
import org.opendaylight.controller.sal.utils.GlobalConstants;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * stub protocol plugin Activator
 *
 *
 */
public class Activator extends ComponentActivatorAbstractBase {
    private static final Logger LOG = LoggerFactory.
        getLogger(Activator.class);

    /**
     * Function called when the activator starts just after some initializations
     * are done by the ComponentActivatorAbstractBase.
     *
     */
    @Override
    public void init() {
        LOG.debug("openflow stub activator init called.");
    }

    /**
     * Function called when the activator stops just before the cleanup done by
     * ComponentActivatorAbstractBase
     *
     */
    @Override
    public void destroy() {
    }

    /**
     * Function that is used to communicate to dependency manager the list of
     * known implementations for services inside a container
     *
     *
     * @return An array containing all the CLASS objects that will be
     *         instantiated in order to get an fully working implementation
     *         Object
     */
    @Override
    public Object[] getImplementations() {
        LOG.debug("openflow stub activator getImplementations called.");
        Object[] res = {
            ReadService.class,
            InventoryService.class,
            TopologyServices.class,
            DataPacketServices.class,
        };
        return res;
    }

    /**
     * Function that is called when configuration of the dependencies is
     * required.
     *
     * @param c
     *            dependency manager Component object, used for configuring the
     *            dependencies exported and imported
     * @param imp
     *            Implementation class that is being configured, needed as long
     *            as the same routine can configure multiple implementations
     * @param containerName
     *            The containerName being configured, this allow also optional
     *            per-container different behavior if needed, usually should not
     *            be the case though.
     */
    @Override
    public void configureInstance(Component c, Object imp, String containerName) {
        LOG.debug("openflow stub activator configureInstance called.");
        if (imp.equals(ReadService.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInReadService.class.getName(), props);
        }

        if (imp.equals(InventoryService.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInInventoryService.class.getName(), props);
            c.add(createContainerServiceDependency(containerName)
                    .setService(IPluginOutInventoryService.class)
                    .setCallbacks("setPluginOutInventoryServices", "unsetPluginOutInventoryServices")
                    .setRequired(false));
            c.add(createServiceDependency()
                    .setService(IPluginInInventoryService.class, "(scope=Global)")
                    .setCallbacks("setInstanceForGlobal", "unsetInstanceForGlobal")
                    .setRequired(true));
        }

        if (imp.equals(TopologyServices.class)) {
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInTopologyService.class.getName(), props);
            c.add(createServiceDependency().setService(IPluginOutTopologyService.class, "")
                    .setCallbacks("setPluginOutTopologyService", "unsetPluginOutTopologyService")
                    .setRequired(true));

        }

        if (imp.equals(DataPacketServices.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInDataPacketService.class.getName(), props);
        }
    }

    @Override
    public Object[] getGlobalImplementations() {
        LOG.debug("openflow stub activator getGlobalImplementations called.");
        Object[] res = {
            FlowProgrammerService.class,
            InventoryService.class,
            DataPacketServices.class,
            ConnectionServices.class
        };
        return res;
    }

    @Override
    public void configureGlobalInstance(Component c, Object imp) {
        LOG.debug("openflow stub activator configureGlobalInstance called.");
        if (imp.equals(FlowProgrammerService.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInFlowProgrammerService.class.getName(), props);
        }

        if (imp.equals(InventoryService.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            props.put("scope", "Global");
            c.setInterface(IPluginInInventoryService.class.getName(), props);
            c.add(createServiceDependency().setService(IPluginOutInventoryService.class, "(scope=Global)")
                    .setCallbacks("setPluginOutInventoryServices", "unsetPluginOutInventoryServices")
                    .setRequired(true));
        }

        if (imp.equals(ConnectionServices.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInConnectionService.class.getName(), props);
        }

        if (imp.equals(DataPacketServices.class)) {
            // export the service to be used by SAL
            Dictionary<String, Object> props = new Hashtable<String, Object>();
            // Set the protocolPluginType property which will be used
            // by SAL
            props.put(GlobalConstants.PROTOCOLPLUGINTYPE.toString(), Node.NodeIDType.OPENFLOW);
            c.setInterface(IPluginInDataPacketService.class.getName(), props);
        }
    }
}