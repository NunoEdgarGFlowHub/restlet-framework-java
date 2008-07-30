/*
 * Copyright 2005-2008 Noelios Consulting.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet.example.book.restlet.ch8.resources;

import java.util.Map;
import java.util.TreeMap;

import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.example.book.restlet.ch8.objects.Contact;
import org.restlet.example.book.restlet.ch8.objects.Mail;
import org.restlet.example.book.restlet.ch8.objects.Mailbox;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.restlet.util.Series;

/**
 * Resource for a mailbox.
 */
public class MailboxResource extends BaseResource {

    /** The mailbox represented by this resource. */
    private final Mailbox mailbox;

    public MailboxResource(Context context, Request request, Response response) {
        super(context, request, response);

        final String mailboxId = Reference.decode((String) request
                .getAttributes().get("mailboxId"), CharacterSet.ISO_8859_1);
        this.mailbox = getObjectsFacade().getMailboxById(mailboxId);
        System.out.println(Reference.encode(mailboxId));

        if (this.mailbox != null) {
            getVariants().add(new Variant(MediaType.TEXT_HTML));
        }

        // Avoid anonymous to update this resource.
        setModifiable(true);
    }

    /**
     * Accept the representation of a mail received from a sender, and create
     * it.
     */
    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {
        final Form form = new Form(entity);
        final Mail mail = new Mail();
        mail.setStatus(Mail.STATUS_RECEIVED);

        // Look for an existing contact or create it.
        final String senderAddress = form.getFirstValue("senderAddress");
        final String senderName = form.getFirstValue("senderName");

        Contact contact = getObjectsFacade().lookForContact(senderAddress,
                this.mailbox);
        if (contact == null) {
            contact = new Contact();
            contact.setMailAddress(senderAddress);
            contact.setName(senderName);
        }
        mail.setSender(contact);

        mail.setMessage(form.getFirstValue("message"));
        mail.setSubject(form.getFirstValue("subject"));
        // form2.add("sendingDate", mail.getSendingDate().toString());
        final Series<Parameter> recipients = form.subList("recipient");
        for (final Parameter recipient : recipients) {
            contact = getObjectsFacade().lookForContact(recipient.getValue(),
                    this.mailbox);
            if (contact == null) {
                contact = new Contact();
                final String[] recipientValues = recipient.getValue().split(
                        "\\$");
                contact.setMailAddress(recipientValues[0]);
                contact.setName(recipientValues[1]);
            }
            mail.getRecipients().add(contact);
        }
        getObjectsFacade().createMail(this.mailbox, mail);
    }

    /**
     * Remove this resource.
     */
    @Override
    public void removeRepresentations() throws ResourceException {
        getObjectsFacade().deleteMailbox(this.mailbox);
        getResponse().redirectSeeOther(
                getRequest().getResourceRef().getParentRef());
    }

    /**
     * Generate the HTML representation of this resource.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        final Map<String, Object> dataModel = new TreeMap<String, Object>();
        dataModel.put("currentUser", getCurrentUser());
        dataModel.put("mailbox", this.mailbox);
        dataModel.put("resourceRef", getRequest().getResourceRef());
        dataModel.put("rootRef", getRequest().getRootRef());

        return getHTMLTemplateRepresentation("mailbox.html", dataModel);
    }

    /**
     * Update the underlying mailbox according to the given representation.
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {
        final Form form = new Form(entity);
        this.mailbox.setNickname(form.getFirstValue("nickname"));
        this.mailbox.setSenderName(form.getFirstValue("senderName"));

        getObjectsFacade().updateMailbox(this.mailbox);
        getResponse().redirectSeeOther(getRequest().getResourceRef());
    }

}
