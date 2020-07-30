package biz.shujutech.payroll;

import biz.shujutech.base.App;
import biz.shujutech.base.Connection;
import biz.shujutech.base.Hinderance;
import biz.shujutech.bznes.Company;
import biz.shujutech.db.object.Clasz;
import biz.shujutech.db.object.FieldClasz.FetchStatus;
import biz.shujutech.db.object.FieldObjectBox;
import biz.shujutech.db.object.ObjectBase;
import biz.shujutech.db.object.ObjectIndex;
import biz.shujutech.db.relational.Database;
import biz.shujutech.db.relational.FieldType;
import biz.shujutech.db.relational.SortOrder;
import biz.shujutech.reflect.ReflectField;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.joda.time.DateTime;

public class PayslipRunList extends Clasz {
	@ReflectField(type=FieldType.OBJECT, deleteAsMember=false, clasz="biz.shujutech.bznes.Company", displayPosition=10) public static String Company;
	@ReflectField(type=FieldType.OBJECTBOX, deleteAsMember=true, clasz="biz.shujutech.payroll.PayslipRun", displayPosition=20) public static String PayslipRunListing;

	public Company getCompany(Connection aConn) throws Exception {
		return((Company) this.getValueObject(aConn, Company));
	}

	public void setCompany(Company aCompany) throws Exception {
		this.setValueObject(Company, aCompany);
	}

	public FieldObjectBox getPayslipRunListing() throws Exception {
		return (this.getFieldObjectBox(PayslipRunListing));
	}
	
	public void addPayslipRun(PayslipRun aPayslipRun) throws Exception {
		this.getPayslipRunListing().addValueObject(aPayslipRun);
	}

	// populate this PayslipRunList base on it's company
	public boolean populateByCompany(Connection aConn) throws Exception {
		if (this.getCompany(aConn) == null) {
			throw new Hinderance("PaylipRunList cannot be populated, it's company has no name/alias, set it's company name/alias first");
		}
		boolean fetchedOk = this.populateByMember(aConn, this.getFieldObject(Company));
		return(fetchedOk);
	}

	public PayslipRun getPayslipRun(Connection aConn, String aBatchId) throws Exception {
		if (this.getCompany(aConn) == null) {
			throw new Hinderance("PaylipRunList cannot be populated, it's company has no name/alias, set it's company name/alias first");
		}

		PayslipRun payslipRun = (PayslipRun) ObjectBase.CreateObject(aConn, PayslipRun.class);
		payslipRun.setBatchId(aBatchId);
		payslipRun = this.getPayslipRun(aConn, payslipRun);
		return(payslipRun);
	}

	public PayslipRun getPayslipRun(Connection aConn, PayslipRun aCriteria) throws Exception {
		String idxName = ObjectIndexOnPayslipRunListingBatchId(aConn); 
		PayslipRun payslipRun = (PayslipRun) this.getPayslipRunListing().fetchUniqueMember(aConn, aCriteria, idxName);
		return(payslipRun);
	}

	public String getLatestBatchId(Connection aConn) throws Exception {
		List<String> ary = new CopyOnWriteArrayList<>();
		ary.add("");
		DateTime latestRunDate = new DateTime( Long.MIN_VALUE );
		this.getPayslipRunListing().forEachMember(aConn, ((Connection bConn, Clasz eachClasz) -> {
			PayslipRun eachRun = (PayslipRun) eachClasz;
			if (eachRun.getRunDate().isAfter(latestRunDate)) {
				ary.set(0, eachRun.getBatchId());
			}
			return(true);
		}));
		String latestBatchId = ary.get(0);
		return(latestBatchId);
	}

	public static void ObjectIndexOnPayslipRunListingBatchIdPopulate(Connection aConn, String aIndexName) throws Exception {
			App.logInfo(PayslipRunList.class, "Populating object index: " + aIndexName + ", on its batch id");
			String sqlGetPayslipRunList = "select " + Clasz.CreatePkName(PayslipRunList.class) + " from " + Clasz.CreateTableName(PayslipRunList.class);
			Clasz.ForEachClasz(aConn, PayslipRunList.class, sqlGetPayslipRunList, ((Connection bConn, Clasz eachPayslipRunList) -> {
				PayslipRunList payslipRunList = (PayslipRunList) eachPayslipRunList;
				ObjectIndex.UpdateIndex(bConn, payslipRunList, aIndexName, true);
				return(true);
			}));
			App.logInfo(PayslipRunList.class, "Completed populating Payslip Run List object index: " + aIndexName);
	}

	public static String ObjectIndexOnPayslipRunListingBatchId(Connection aConn) throws Exception {
		PayslipRunList claszPayslipRunList = (PayslipRunList) ObjectBase.CreateObject(aConn, PayslipRunList.class);
		claszPayslipRunList.clearAllIndexKey();
		claszPayslipRunList.getPayslipRunListing().setObjectKey(true);
		claszPayslipRunList.getPayslipRunListing().getMetaObj().getField(biz.shujutech.payroll.PayslipRun.BatchId).setObjectKey(true);
		String indexName = ObjectIndex.GetObjectIndexName(aConn, claszPayslipRunList);
		if (Database.TableExist(aConn, indexName) == false) {
			App.logInfo(PayslipRunList.class, "Creating object index for: " + PayslipRunList.class.getName() + ", on Batch Id");
			ObjectIndex.CreateObjectIndex(aConn, claszPayslipRunList); // create field index by name
			ObjectIndexOnPayslipRunListingBatchIdPopulate(aConn, indexName);
		}
		return(indexName);
	}

	public FetchStatus fetchPayslipRunBySectionDsc(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue) throws Exception {
		return(fetchPayslipRunBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, SortOrder.DSC));
	}

	public FetchStatus fetchPayslipRunBySection(Connection aConn, String aPageDirection, int aFetchSize, String aSortField, String aSortValue, SortOrder aOrder) throws Exception {
		String idxName = ObjectIndexOnPayslipRunListingBatchId(aConn); 
		FetchStatus result = this.getPayslipRunListing().fetchBySection(aConn, aPageDirection, aFetchSize, aSortField, aSortValue, idxName, aOrder);
		return(result);
	}

}
