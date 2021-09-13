package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;
import java.util.Map;

public class InvoiceOutcome extends Outcome  {
      private List<DataTable> dataTable;

   public List<DataTable> getDataTable() {
      return dataTable;
   }

   public void setDataTable(List<DataTable> dataTable) {
      this.dataTable = dataTable;
   }
}
