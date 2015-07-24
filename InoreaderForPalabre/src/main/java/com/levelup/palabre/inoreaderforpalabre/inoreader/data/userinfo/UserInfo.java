
package com.levelup.palabre.inoreaderforpalabre.inoreader.data.userinfo;

import com.google.gson.annotations.Expose;

public class UserInfo {

    @Expose
    private String userId;
    @Expose
    private String userName;
    @Expose
    private String userProfileId;
    @Expose
    private String userEmail;
    @Expose
    private Boolean isBloggerUser;
    @Expose
    private Long signupTimeSec;
    @Expose
    private Boolean isMultiLoginEnabled;

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 
     * @return
     *     The userProfileId
     */
    public String getUserProfileId() {
        return userProfileId;
    }

    /**
     * 
     * @param userProfileId
     *     The userProfileId
     */
    public void setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
    }

    /**
     * 
     * @return
     *     The userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 
     * @param userEmail
     *     The userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * 
     * @return
     *     The isBloggerUser
     */
    public Boolean getIsBloggerUser() {
        return isBloggerUser;
    }

    /**
     * 
     * @param isBloggerUser
     *     The isBloggerUser
     */
    public void setIsBloggerUser(Boolean isBloggerUser) {
        this.isBloggerUser = isBloggerUser;
    }

    /**
     * 
     * @return
     *     The signupTimeSec
     */
    public Long getSignupTimeSec() {
        return signupTimeSec;
    }

    /**
     * 
     * @param signupTimeSec
     *     The signupTimeSec
     */
    public void setSignupTimeSec(Long signupTimeSec) {
        this.signupTimeSec = signupTimeSec;
    }

    /**
     * 
     * @return
     *     The isMultiLoginEnabled
     */
    public Boolean getIsMultiLoginEnabled() {
        return isMultiLoginEnabled;
    }

    /**
     * 
     * @param isMultiLoginEnabled
     *     The isMultiLoginEnabled
     */
    public void setIsMultiLoginEnabled(Boolean isMultiLoginEnabled) {
        this.isMultiLoginEnabled = isMultiLoginEnabled;
    }

}
