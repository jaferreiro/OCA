/*******************************************************************************
 * Copyright 2002-2013, OpenNebula Project Leads (OpenNebula.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.opennebula.client.template;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.PoolElement;
import org.w3c.dom.Node;

/**
 * This class represents an OpenNebula template.
 * It also offers static XML-RPC call wrappers.
 */
public class Template extends PoolElement
{

    private static final String METHOD_PREFIX = "template.";
    private static final String ALLOCATE = METHOD_PREFIX + "allocate";
    private static final String INFO     = METHOD_PREFIX + "info";
    private static final String DELETE   = METHOD_PREFIX + "delete";
    private static final String UPDATE   = METHOD_PREFIX + "update";
    private static final String CHOWN    = METHOD_PREFIX + "chown";
    private static final String CHMOD    = METHOD_PREFIX + "chmod";
    private static final String INSTANTIATE = METHOD_PREFIX + "instantiate";
    private static final String CLONE    = METHOD_PREFIX + "clone";

    /**
     * Creates a new Template representation.
     * @param id The template id.
     * @param client XML-RPC Client.
     */
    public Template(int id, Client client)
    {
        super(id, client);
    }

    /**
     * @see PoolElement
     */
    protected Template(Node xmlElement, Client client)
    {
        super(xmlElement, client);
    }

    // =================================
    // Static XML-RPC methods
    // =================================


    /**
     * Allocates a new Template in OpenNebula.
     *
     * @param client XML-RPC Client.
     * @param description A string containing the template of the template.
     * @return If successful the message contains the associated
     * id generated for this Template.
     */
    public static OneResponse allocate(Client client, String description)
    {
        return client.call(ALLOCATE, description);
    }

    /**
     * Retrieves the information of the given Template.
     *
     * @param client XML-RPC Client.
     * @param id The template id for the template to retrieve the information from
     * @return If successful the message contains the string
     * with the information returned by OpenNebula.
     */
    public static OneResponse info(Client client, int id)
    {
        return client.call(INFO, id);
    }

    /**
     * Deletes a template from OpenNebula.
     *
     * @param client XML-RPC Client.
     * @param id The template id of the target template we want to delete.
     * @return A encapsulated response.
     */
    public static OneResponse delete(Client client, int id)
    {
        return client.call(DELETE, id);
    }

    /**
     * Replaces the template contents.
     *
     * @param client XML-RPC Client.
     * @param id The template id of the target template we want to modify.
     * @param new_template New template contents.
     * @return If successful the message contains the template id.
     */
    public static OneResponse update(Client client, int id, String new_template)
    {
        return client.call(UPDATE, id, new_template);
    }

    /**
     * Publishes or unpublishes a template.
     *
     * @param client XML-RPC Client.
     * @param id The template id of the target template we want to modify.
     * @param publish True for publishing, false for unpublishing.
     * @return If successful the message contains the template id.
     */
    public static OneResponse publish(Client client, int id, boolean publish)
    {
        int group_u = publish ? 1 : 0;

        return chmod(client, id, -1, -1, -1, group_u, -1, -1, -1, -1, -1);
    }

    /**
     * Changes the owner/group
     * 
     * @param client XML-RPC Client.
     * @param id The template id of the target template we want to modify.
     * @param uid The new owner user ID. Set it to -1 to leave the current one.
     * @param gid The new group ID. Set it to -1 to leave the current one.
     * @return If an error occurs the error message contains the reason.
     */
    public static OneResponse chown(Client client, int id, int uid, int gid)
    {
        return client.call(CHOWN, id, uid, gid);
    }

    /**
     * Changes the template permissions
     * 
     * @param client XML-RPC Client.
     * @param id The template id of the target template.
     * @param owner_u 1 to allow, 0 deny, -1 do not change
     * @param owner_m 1 to allow, 0 deny, -1 do not change
     * @param owner_a 1 to allow, 0 deny, -1 do not change
     * @param group_u 1 to allow, 0 deny, -1 do not change
     * @param group_m 1 to allow, 0 deny, -1 do not change
     * @param group_a 1 to allow, 0 deny, -1 do not change
     * @param other_u 1 to allow, 0 deny, -1 do not change
     * @param other_m 1 to allow, 0 deny, -1 do not change
     * @param other_a 1 to allow, 0 deny, -1 do not change
     * @return If an error occurs the error message contains the reason.
     */
    public static OneResponse chmod(Client client, int id,
                                    int owner_u, int owner_m, int owner_a,
                                    int group_u, int group_m, int group_a,
                                    int other_u, int other_m, int other_a)
    {
        return chmod(client, CHMOD, id,
                            owner_u, owner_m, owner_a,
                            group_u, group_m, group_a,
                            other_u, other_m, other_a);
    }

    /**
     * Changes the permissions
     * 
     * @param client XML-RPC Client.
     * @param id The id of the target object.
     * @param octet Permissions octed , e.g. 640
     * @return If an error occurs the error message contains the reason.
     */
    public static OneResponse chmod(Client client, int id, String octet)
    {
        return chmod(client, CHMOD, id, octet);
    }

    /**
     * Changes the permissions
     * 
     * @param client XML-RPC Client.
     * @param id The id of the target object.
     * @param octet Permissions octed , e.g. 640
     * @return If an error occurs the error message contains the reason.
     */
    public static OneResponse chmod(Client client, int id, int octet)
    {
        return chmod(client, CHMOD, id, octet);
    }

    /**
     * Creates a VM instance from a Template
     * 
     * @param client XML-RPC Client.
     * @param id The template id of the target template.
     * @param name A string containing the name of the VM instance, can be empty.
     * @return If successful the message contains the VM Instance ID.
     */
    public static OneResponse instantiate(Client client, int id, String name)
    {
        return client.call(INSTANTIATE, id, name);
    }

    /**
     * Clones this template into a new one
     *
     * @param client XML-RPC Client.
     * @param id The template id of the target template.
     * @param name Name for the new template.
     * @return If successful the message contains the new template ID.
     */
    public static OneResponse clone(Client client, int id, String name)
    {
        return client.call(CLONE, id, name);
    }

    // =================================
    // Instanced object XML-RPC methods
    // =================================

    /**
     * Retrieves the information of the Template.
     *
     * @return If successful the message contains the string
     * with the information returned by OpenNebula.
     */
    public OneResponse info()
    {
        OneResponse response = info(client, id);
        super.processInfo(response);
        return response;
    }

    /**
     * Deletes the template from OpenNebula.
     *
     * @return A encapsulated response.
     */
    public OneResponse delete()
    {
        return delete(client, id);
    }

    /**
     * Replaces the template contents.
     *
     * @param new_template New template contents.
     * @return If successful the message contains the template id.
     */
    public OneResponse update(String new_template)
    {
        return update(client, id, new_template);
    }

    /**
     * Publishes or unpublishes the template.
     *
     * @param publish True for publishing, false for unpublishing.
     * @return If successful the message contains the template id.
     */
    public OneResponse publish(boolean publish)
    {
        return publish(client, id, publish);
    }

    /**
     * Publishes the template.
     *
     * @return If successful the message contains the template id.
     */
    public OneResponse publish()
    {
        return publish(true);
    }

    /**
     * Unpublishes the template.
     *
     * @return If successful the message contains the template id.
     */
    public OneResponse unpublish()
    {
        return publish(false);
    }

    /**
     * Changes the owner/group
     * 
     * @param uid The new owner user ID. Set it to -1 to leave the current one.
     * @param gid The new group ID. Set it to -1 to leave the current one.
     * @return If an error occurs the error message contains the reason.
     */
    public OneResponse chown(int uid, int gid)
    {
        return chown(client, id, uid, gid);
    }

    /**
     * Changes the owner
     * 
     * @param uid The new owner user ID.
     * @return If an error occurs the error message contains the reason.
     */
    public OneResponse chown(int uid)
    {
        return chown(uid, -1);
    }

    /**
     * Changes the group
     * 
     * @param gid The new group ID.
     * @return If an error occurs the error message contains the reason.
     */
    public OneResponse chgrp(int gid)
    {
        return chown(-1, gid);
    }

    /**
     * Changes the template permissions
     * 
     * @param owner_u 1 to allow, 0 deny, -1 do not change
     * @param owner_m 1 to allow, 0 deny, -1 do not change
     * @param owner_a 1 to allow, 0 deny, -1 do not change
     * @param group_u 1 to allow, 0 deny, -1 do not change
     * @param group_m 1 to allow, 0 deny, -1 do not change
     * @param group_a 1 to allow, 0 deny, -1 do not change
     * @param other_u 1 to allow, 0 deny, -1 do not change
     * @param other_m 1 to allow, 0 deny, -1 do not change
     * @param other_a 1 to allow, 0 deny, -1 do not change
     * @return If an error occurs the error message contains the reason.
     */
    public OneResponse chmod(int owner_u, int owner_m, int owner_a,
                             int group_u, int group_m, int group_a,
                             int other_u, int other_m, int other_a)
    {
        return chmod(client, id,
                    owner_u, owner_m, owner_a,
                    group_u, group_m, group_a,
                    other_u, other_m, other_a);
    }

    /**
     * Changes the permissions
     *
     * @param octet Permissions octed , e.g. 640
     * @return If an error occurs the error message contains the reason.
     */
    public OneResponse chmod(String octet)
    {
        return chmod(client, id, octet);
    }

    /**
     * Changes the permissions
     *
     * @param octet Permissions octed , e.g. 640
     * @return If an error occurs the error message contains the reason.
     */
    public OneResponse chmod(int octet)
    {
        return chmod(client, id, octet);
    }

    /**
     * Creates a VM instance from a Template
     * 
     * @param name A string containing the name of the VM instance, can be empty.
     * @return If successful the message contains the VM Instance ID.
     */
    public OneResponse instantiate(String name)
    {
        return instantiate(client, id, name);
    }

    /**
     * Creates a VM instance from a Template
     * 
     * @return If successful the message contains the VM Instance ID.
     */
    public OneResponse instantiate()
    {
        return instantiate(client, id, "");
    }

    /**
     * Clones this template into a new one
     *
     * @param name Name for the new template.
     * @return If successful the message contains the new template ID.
     */
    public OneResponse clone(String name)
    {
        return clone(client, id, name);
    }

    // =================================
    // Helpers
    // =================================
}
