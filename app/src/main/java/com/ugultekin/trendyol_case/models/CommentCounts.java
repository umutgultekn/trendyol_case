package com.ugultekin.trendyol_case.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentCounts {
    @SerializedName("averageRating")
    @Expose
    private Double averageRating;
    @SerializedName("anonymousCommentsCount")
    @Expose
    private Integer anonymousCommentsCount;
    @SerializedName("memberCommentsCount")
    @Expose
    private Integer memberCommentsCount;

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getAnonymousCommentsCount() {
        return anonymousCommentsCount;
    }

    public void setAnonymousCommentsCount(Integer anonymousCommentsCount) {
        this.anonymousCommentsCount = anonymousCommentsCount;
    }

    public Integer getMemberCommentsCount() {
        return memberCommentsCount;
    }

    public void setMemberCommentsCount(Integer memberCommentsCount) {
        this.memberCommentsCount = memberCommentsCount;
    }
}
