package com.bmw.location.movementtracker;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

/**
 * Defines base path, initiates application.
 *
 * @author Robert Lang
 */
@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({ "nsa", "vehicle", "web" })
@ApplicationPath("api")
public class App extends Application {
}
