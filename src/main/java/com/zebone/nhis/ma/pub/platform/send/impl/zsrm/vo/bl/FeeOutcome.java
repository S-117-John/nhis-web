package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;

import java.util.List;

public class FeeOutcome extends Outcome  {
      private List<Outfee> outfee;

      private List<OutFeedeTail> outFeedeTail;

   public List<Outfee> getOutfee() {
      return outfee;
   }

   public void setOutfee(List<Outfee> outfee) {
      this.outfee = outfee;
   }

   public List<OutFeedeTail> getOutFeedeTail() {
      return outFeedeTail;
   }

   public void setOutFeedeTail(List<OutFeedeTail> outFeedeTail) {
      this.outFeedeTail = outFeedeTail;
   }
}
