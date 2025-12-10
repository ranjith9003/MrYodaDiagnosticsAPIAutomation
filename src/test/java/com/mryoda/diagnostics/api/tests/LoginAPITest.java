package com.mryoda.diagnostics.api.tests;

import com.mryoda.diagnostics.api.base.BaseTest;
import com.mryoda.diagnostics.api.config.ConfigLoader;
import com.mryoda.diagnostics.api.utils.RequestContext;
import com.mryoda.diagnostics.api.utils.TokenManager;
import org.testng.annotations.Test;

public class LoginAPITest extends BaseTest {

	@Test(priority = 1)
	public void testLoginWithOTP() {

	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║            MEMBER LOGIN TEST                             ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    
	    String mobile = ConfigLoader.getConfig().memberMobile();
	    String token = TokenManager.generateToken(mobile, TokenManager.MEMBER);

	    System.out.println("🟢 MEMBER LOGIN SUCCESS");
	    System.out.println("   Token: " + token);
	    System.out.println("   Stored in: RequestContext.getMemberToken()");
	    System.out.println("   First Name: " + RequestContext.getMemberFirstName());
	    System.out.println("   Last Name: " + RequestContext.getMemberLastName());
	    System.out.println("   User ID: " + RequestContext.getMemberUserId());
	}

	@Test(priority = 1)
	public void testLoginWithOTP_ExistingMember() {

	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║         EXISTING MEMBER LOGIN TEST                       ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    
	    String mobile = ConfigLoader.getConfig().nonMemberMobile();
	    String token = TokenManager.generateToken(mobile, TokenManager.EXISTING_MEMBER);

	    System.out.println("🟢 EXISTING MEMBER LOGIN SUCCESS");
	    System.out.println("   Token: " + token);
	    System.out.println("   Stored in: RequestContext.getExistingMemberToken()");
	    System.out.println("   First Name: " + RequestContext.getExistingMemberFirstName());
	    System.out.println("   Last Name: " + RequestContext.getExistingMemberLastName());
	    System.out.println("   User ID: " + RequestContext.getExistingMemberUserId());
	}

	@Test(priority = 2,
	       dependsOnMethods = "com.mryoda.diagnostics.api.tests.UserCreateAPITest.testUserRegistration_CreateNewUser")
	public void testLoginWithOTP_NewlyRegisteredUser() {

	    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
	    System.out.println("║            NEW USER LOGIN TEST                           ║");
	    System.out.println("╚══════════════════════════════════════════════════════════╝");
	    
	    String mobile = RequestContext.getMobile();
	    String token = TokenManager.generateToken(mobile, TokenManager.NEW_USER);

	    System.out.println("🟢 NEW USER LOGIN SUCCESS");
	    System.out.println("   Token: " + token);
	    System.out.println("   Stored in: RequestContext.getNewUserToken()");
	    System.out.println("   First Name: " + RequestContext.getNewUserFirstName());
	    System.out.println("   Last Name: " + RequestContext.getNewUserLastName());
	    System.out.println("   User ID: " + RequestContext.getNewUserUserId());
	}

}
