package com.ugultekin.trendyol_case.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Social {
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("commentCounts")
    @Expose
    private CommentCounts commentCounts;

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public CommentCounts getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(CommentCounts commentCounts) {
        this.commentCounts = commentCounts;
    }
}
