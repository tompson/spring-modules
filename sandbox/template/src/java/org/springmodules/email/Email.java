/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.email;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.springframework.core.io.Resource;
import javax.mail.internet.InternetAddress;

/**
 * Represents an email. This separate abstraction of an email message is required as a {@link javax.mail.internet.MimeMessage}
 * can only be constructed while a session is open, and on the other hand spring's
 * {@link org.springframework.mail.SimpleMailMessage} is too simple :-) and limited (e.g doesn't support attachments).
 * <p/>
 * This email supports attachments in the form of an {@link org.springmodules.email.Attachment} objects, where the
 * the actual attachment resources are represented using spring's {@link org.springframework.core.io.Resource resources)
 *
 * @author Uri Boness
 * @see Attachment
 */
public class Email {

    // List<InternetAddress>
    private List to = new ArrayList();
    private List cc = new ArrayList();
    private List bcc = new ArrayList();

    private InternetAddress from;
    private InternetAddress replyTo;

    private EmailPriority priority = EmailPriority.NORMAL;

    private String subject;
    private String htmlBody;
    private String textBody;

    // Set<Attachment>
    private Set attachments;

    // Set<Attachment>
    private Set inlineAttachments;

    /**
     * Sets the address from which this email is sent.
     *
     * @param from The address from which this email is sent.
     */
    public void setFrom(InternetAddress from) {
        this.from = from;
    }

    /**
     * Sets The address from which this email is sent.
     *
     * @param address The email address from which this email is sent.
     */
    public void setFrom(String address) {
        this.from = EmailUtils.createAddress(address);
    }

    /**
     * Sets the address from which this email is sent.
     *
     * @param name The personal name of the sender.
     * @param address The email address of the sender.
     */
    public void setFrom(String name, String address) {
        this.from = EmailUtils.createAddress(name, address);
    }

    /**
     * Returns the address from which this email is sent.
     *
     * @return The address from which this email is sent.
     */
    public InternetAddress getFrom() {
        return from;
    }

    /**
     * Sets the list of addresses to which this email is sent.
     *
     * @param to The list of addresses to which this email is sent.
     */
    public void setTo(InternetAddress[] to) {
        this.to = new ArrayList();
        addTo(to);
    }

    /**
     * Adds the given address to the address list to which this email is sent.
     *
     * @param address Another address to which this email should be sent.
     */
    public void addTo(InternetAddress address) {
        addTo(new InternetAddress[] { address });
    }

    /**
     * Adds the given addresses to the address list to which this email is sent.
     *
     * @param addresses Additional addresses to which this email should be sent.
     */
    public void addTo(InternetAddress[] addresses) {
        for (int i=0; i<addresses.length; i++) {
            this.to.add(addresses[i]);
        }
    }

    /**
     * Adds the given address to the address list to which this email is sent.
     *
     * @param address Additional address to which this email should be sent.
     */
    public void addTo(String address) {
        addTo(new String[] { address });
    }

    /**
     * Adds the given addresses to the address list to which this email is sent.
     *
     * @param addresses Additional email addresses to which this email should be sent.
     */
    public void addTo(String[] addresses) {
        for (int i=0; i<addresses.length; i++) {
            this.to.add(EmailUtils.createAddress(addresses[i]));
        }
    }

    /**
     * Returns the list of addresses to which this email is sent.
     *
     * @return The list of addresses to which this email is sent.
     */
    public InternetAddress[] getTo() {
        return (InternetAddress[])to.toArray(new InternetAddress[to.size()]);
    }

    /**
     * Sets the addresses to which this email is cc'ed.
     *
     * @param cc The addresses to which this email is cc'ed.
     */
    public void setCc(InternetAddress[] cc) {
        this.cc = new ArrayList();
        addCc(cc);
    }

    /**
     * Adds the given address to the address list to which this email will be cc'ed.
     *
     * @param address Additional address to which this email should be cc'ed.
     */
    public void addCc(InternetAddress address) {
        addCc(new InternetAddress[] { address });
    }

    /**
     * Adds the given addresses to the address list to which this email will be cc'ed.
     *
     * @param addresses Additional addresses to which this email should be cc'ed.
     */
    public void addCc(InternetAddress[] addresses) {
        for (int i=0; i<addresses.length; i++) {
            cc.add(addresses[i]);
        }
    }

    /**
     * Adds the given address to the address list to which this email will be cc'ed.
     *
     * @param address Additional address to which this email should be cc'ed.
     */
    public void addCc(String address) {
        addCc(new String[] { address });
    }

    /**
     * Adds the given addresses to the address list to which this email will be cc'ed.
     *
     * @param addresses Additional addresses to which this email should be cc'ed.
     */
    public void addCc(String[] addresses) {
        for (int i=0; i<addresses.length; i++) {
            cc.add(EmailUtils.createAddress(addresses[i]));
        }
    }

    /**
     * Returns the addresses to which this email will be cc'ed.
     *
     * @return The addresses to which this email will be cc'ed.
     */
    public InternetAddress[] getCc() {
        return (InternetAddress[]) cc.toArray(new InternetAddress[cc.size()]);
    }

    /**
     * Sets the addresses to which this email is bcc'ed.
     *
     * @param bcc The addresses to which this email is bcc'ed.
     */
    public void setBcc(InternetAddress[] bcc) {
        this.bcc = new ArrayList();
        addBcc(bcc);
    }

    /**
     * Adds the given address to the address list to which this email is bcc'ed.
     *
     * @param address Additional address to which this email should be bcc'ed.
     */
    public void addBcc(InternetAddress address) {
        addBcc(new InternetAddress[] { address });
    }

    /**
     * Adds the given addresses to the address list to which this email is bcc'ed.
     *
     * @param addresses Additional addresses to which this email should be bcc'ed.
     */
    public void addBcc(InternetAddress[] addresses) {
        for (int i=0; i<addresses.length; i++) {
            bcc.add(addresses[i]);
        }
    }

    /**
     * Adds the given address to the address list to which this email is bcc'ed.
     *
     * @param address Additional address to which this email should be bcc'ed.
     */
    public void addBcc(String address) {
        addBcc(new String[] { address });
    }

    /**
     * Adds the given addresses to the address list to which this email is bcc'ed.
     *
     * @param addresses Additional addresses to which this email should be bcc'ed.
     */
    public void addBcc(String[] addresses) {
        for (int i=0; i<addresses.length; i++) {
            bcc.add(EmailUtils.createAddress(addresses[i]));
        }
    }

    /**
     * Returns the addresses to which this email is bcc'ed.
     *
     * @return The addresses to which this email is bcc'ed.
     */
    public InternetAddress[] getBcc() {
        return (InternetAddress[]) bcc.toArray(new InternetAddress[bcc.size()]);
    }

    /**
     * Sets the address to which this email should be replied to.
     *
     * @param replyTo The address to which this email should be replied to.
     */
    public void setReplyTo(InternetAddress replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * Sets the address to which this email should be replied to.
     *
     * @param replyTo The address to which this email should be replied to.
     */
    public void setReplyTo(String replyTo) {
        this.replyTo = EmailUtils.createAddress(replyTo);
    }

    /**
     * Sets the address to which this email should be replied to.
     *
     * @param name The personal name of the recipient.
     * @param address The email address of the recipient.
     */
    public void setReplyTo(String name, String address) {
        this.replyTo = EmailUtils.createAddress(name, address);
    }

    /**
     * Returns the address to which this email should be replied to.
     *
     * @return The address to which this email should be replied to.
     */
    public InternetAddress getReplyTo() {
        return replyTo;
    }

    /**
     * Sets the priority of this email.
     *
     * @param priority The priority of this email.
     */
    public void setPriority(EmailPriority priority) {
        this.priority = priority;
    }

    /**
     * Returns the priority of this email.
     *
     * @return The priority of this email.
     */
    public EmailPriority getPriority() {
        return priority;
    }

    /**
     * Sets the subject of this email.
     *
     * @param subject The subject of this email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the subject of this email.
     *
     * @return The subject of this email.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the plain text body of this email.
     *
     * @param textBody The plain text body of this email.
     */
    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    /**
     * Returns the plain text body of this email.
     *
     * @return The plain text body of this email.
     */
    public String getTextBody() {
        return textBody;
    }

    /**
     * Sets the HTML version of the body of this email.
     *
     * @param htmlBody The HTML version of the body of this email.
     */
    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    /**
     * Returns the HTML version of the body of this email.
     *
     * @return The HTML version of the body of this email.
     */
    public String getHtmlBody() {
        return htmlBody;
    }

    /**
     * Sets the attachments of this email.
     *
     * @param attachments The attachments of this email.
     */
    public void setAttachments(Set attachments) {
        this.attachments = attachments;
    }

    /**
     * Adds the given attachment to this email.
     *
     * @param attachment The attachment to be added to this email.
     */
    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    /**
     * Adds the an attachment to this email.
     *
     * @param fileName The file name of the attachment.
     * @param resource The resource to be attached.
     */
    public void addAttachment(String fileName, Resource resource) {
        addAttachment(new Attachment(fileName, resource));
    }

    /**
     * Returns the attachments of this emails.
     *
     * @return Set<Attachment> The attachments of this emails.
     */
    public Set getAttachments() {
        return attachments;
    }

    /**
     * Sets the inline (embedded) attachments of this email.
     *
     * @param inlineAttachments The inline (embedded) attachments of this email.
     */
    public void setInlineAttachments(Set inlineAttachments) {
        this.inlineAttachments = inlineAttachments;
    }

    /**
     * Adds the given attachment as an inline (embedded) attachment to this email.
     *
     * @param attachment The inline attachment to be added to this email.
     */
    public void addInlineAttachment(Attachment attachment) {
        inlineAttachments.add(attachment);
    }

    /**
     * Adds the given attachment as an inline (embedded) attachment to this email.
     *
     * @param contentId The id of this inline attachment.
     * @param resource The resource to be attached.
     */
    public void addInlineAttachment(String contentId, Resource resource) {
        inlineAttachments.add(new Attachment(contentId, resource));
    }

    /**
     * Returns all the inline (embedded) attachments of this email.
     *
     * @return Set<Attachment> All the inline (embedded) attachments of this email.
     */
    public Set getInlineAttachments() {
        return inlineAttachments;
    }

}
