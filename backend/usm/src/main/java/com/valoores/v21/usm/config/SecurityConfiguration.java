package com.valoores.v21.usm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(request -> request

				.antMatchers("/", "/valoores/**").permitAll()

				.antMatchers(HttpMethod.POST, "/api/login").permitAll().antMatchers(HttpMethod.GET, "/api/**")
				.permitAll()

				.antMatchers(HttpMethod.GET, "/swagger/**").hasAuthority("ADMIN")

		).httpBasic().and().csrf().disable();

		//http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin();
	}

//	static DirContext ldapContext;
//
//	public static void fetActiveDirectoryUsers(String url, String port, String username, String password)
//			throws Exception {
//
//		Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
//		ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//		ldapEnv.put(Context.PROVIDER_URL, "ldap://" + url + ":" + port);
//		ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
//		ldapEnv.put(Context.SECURITY_PRINCIPAL, username);
//		ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
//		ldapContext = new InitialDirContext(ldapEnv);
//
//		// Create the search controls
//		SearchControls searchCtls = new SearchControls();
//
//		// Specify the attributes to return
//		String returnedAtts[] = { "sn", "givenName", "samAccountName" };
//		searchCtls.setReturningAttributes(returnedAtts);
//
//		// Specify the search scope
//		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//
//		// specify the LDAP search filter
//		String searchFilter = "(&(objectClass=user))";
//
//		// Specify the Base for the search
//		String searchBase = "DC=beirut,DC=softsolutions-group,DC=com";
//		// initialize counter to total the results
//		int totalResults = 0;
//
//		// Search for objects using the filter
//		NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);
//
//		// Loop through the search results
//		while (answer.hasMoreElements()) {
//			SearchResult sr = (SearchResult) answer.next();
//
//			totalResults++;
//
//			System.out.println(">>>" + sr);
////			Attributes attrs = sr.getAttributes();
////			System.out.println(">>>>>>" + attrs.get("samAccountName"));
//		}
//
//		System.out.println("Total results: " + totalResults);
//		ldapContext.close();
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		 fetActiveDirectoryUsers("10.1.10.1", "389", "beirut\\antoun.k", "Valoores@2021");

		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).authorities("ADMIN")
				.and().withUser("user").password(passwordEncoder().encode("user")).authorities("USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
