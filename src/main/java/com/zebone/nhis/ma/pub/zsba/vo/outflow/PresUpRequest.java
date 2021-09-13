package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import java.util.List;

public class PresUpRequest {

    private VisitInfo visitInfo;
    private List<PresInfo> recipeInfo;

    public VisitInfo getVisitInfo() {
        return visitInfo;
    }

    public void setVisitInfo(VisitInfo visitInfo) {
        this.visitInfo = visitInfo;
    }

    public List<PresInfo> getRecipeInfo() {
        return recipeInfo;
    }

    public void setRecipeInfo(List<PresInfo> recipeInfo) {
        this.recipeInfo = recipeInfo;
    }

}
