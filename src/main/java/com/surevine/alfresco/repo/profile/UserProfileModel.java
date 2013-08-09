package com.surevine.alfresco.repo.profile;

import org.alfresco.service.namespace.QName;

/**
 * Note: This class is logically located in the wrong place.
 * It was originally created for the alfresco_repository_module, but has been moved to remove a circular dependency between the repository module and the GSAConnector module.
 * 
 * @author jonnyheavey
 *
 */
public final class UserProfileModel {

	private UserProfileModel(){}
	
	public static final String NAMESPACE_USER_PROFILE="http://www.surevine.com/alfresco/model/userProfile/1.0";
	
	public static final QName PROP_BIOGRAPHY = QName.createQName(NAMESPACE_USER_PROFILE, "biography");
	
	public static final QName PROP_ASK_ME_ABOUT = QName.createQName(NAMESPACE_USER_PROFILE, "askMeAbouts");
	
	public static final QName PROP_TELEPHONE_NUMBERS = QName.createQName(NAMESPACE_USER_PROFILE, "telephoneNumbers");

	public static final QName PROP_MODIFIED = QName.createQName(NAMESPACE_USER_PROFILE, "lastModified");
	
}