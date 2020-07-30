package biz.shujutech.intf;

import org.joda.time.DateTime;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.LookupItem;
import biz.shujutech.db.object.LookupList;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.bznes.Contact;
import biz.shujutech.bznes.EmailTypePerson;
import biz.shujutech.bznes.PersonGotEmail;

public interface EmailSentRecord {
	
	public DateTime getSentDate() throws Exception;
	public void setSentDate(DateTime aSendDate) throws Exception;
	public String getEmailAddress() throws Exception;
	public void setEmailAddress(String aEmailAddress) throws Exception;
	
	public static void GetContactEmailList(Connection aConn, Contact aContact, Clasz aEmailSent, String aFieldName) throws Exception {
		if ((aEmailSent instanceof EmailSentRecord) == false) {
			throw new Hinderance("Trying to get email list from field isn't of interface EmailSentRecord");
		}

		EmailSentRecord sentRec = (EmailSentRecord) aEmailSent;

		if (aContact != null) {
			LookupList emailList = (LookupList) ObjectBase.CreateObjectTransient(aConn, LookupList.class); // to create front end drop down list of aContact's email
			aContact.fetchAllEmail(aConn);
			aContact.getEmail().resetIterator();
			while (aContact.getEmail().hasNext(aConn)) {
				PersonGotEmail eachEmail = (PersonGotEmail) aContact.getEmail().getNext();
				//if (eachEmail.getType().getDescr().equals(EmailTypePerson.PersonalDescr) || eachEmail.getType().getDescr().equals(EmailTypePerson.PersonalDescr1)) {
				//}
				LookupItem emailLookup = (LookupItem) ObjectBase.CreateObjectTransient(aConn, LookupItem.class); // for each email, need a LookupItem, we use new instead of CreateObject so no DDL is done
				emailLookup.setDescr(eachEmail.getEmailAddr());
				emailList.addItem(emailLookup);
				if (eachEmail.getType().getDescr().equals(EmailTypePerson.PersonalDescr)) { // default in the combobox, set personal email in front-end
					if (sentRec.getEmailAddress() == null || sentRec.getEmailAddress().isEmpty()) {
						sentRec.setEmailAddress(emailLookup.getDescr());
					}
				}
			}

			aEmailSent.createFieldObject(aFieldName, emailList); // create a transient lookup field for user to select the email to use
			//salarySlip.getField(SalarySlip.EmailNotification).forRemove(false);
		}
	}
}
