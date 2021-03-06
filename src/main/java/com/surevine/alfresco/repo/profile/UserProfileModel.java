/*
 * Copyright (C) 2008-2010 Surevine Limited.
 *   
 * Although intended for deployment and use alongside Alfresco this module should
 * be considered 'Not a Contribution' as defined in Alfresco'sstandard contribution agreement, see
 * http://www.alfresco.org/resource/AlfrescoContributionAgreementv2.pdf
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package com.surevine.alfresco.repo.profile;

import org.alfresco.service.namespace.QName;

/**
 * Note: This class was originally created for the alfresco_repository_module, 
 * but has been moved to remove a circular dependency between the repository 
 * module and the GSAConnector module.
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
