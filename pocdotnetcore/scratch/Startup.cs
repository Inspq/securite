﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using System.Security.Claims;
using System.IdentityModel.Tokens.Jwt;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.OAuth;

namespace scratch
{
    public class Startup
    {
        // This method gets called by the runtime. Use this method to add services to the container.
        // For more information on how to configure your application, visit https://go.microsoft.com/fwlink/?LinkID=398940
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddMvc();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
        {
            loggerFactory.AddConsole();

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            app.UseStaticFiles();

            // add openid
            app.UseCookieAuthentication(new CookieAuthenticationOptions()
            {
                AuthenticationScheme = "Cookies",
                CookieName = "Cookies",
                AutomaticAuthenticate = true,
                ExpireTimeSpan = TimeSpan.FromMinutes(60)
            });

            //app.UseOpenIdConnectAuthentication(new OpenIdConnectOptions()
            //{
            //    AuthenticationScheme = "oidc",
            //    AutomaticAuthenticate = true,
            //    AutomaticChallenge = true,
            //    SignInScheme = "Cookies",
            //    RequireHttpsMetadata = false,
            //    Authority = "http://inspq-6111.inspq.qc.ca:8893/openam/oauth2/",
            //    ResponseType = "id_token",
            //    ClientId = "sx5dotnetuioidc",
            //    ClientSecret = "password",
            //    GetClaimsFromUserInfoEndpoint = true,
            //    CallbackPath = new PathString("/login"),

            //    Events = new OpenIdConnectEvents()
            //    {
            //        OnRemoteFailure = context =>
            //        {
            //            return OnRemoteFailure(context);
            //        },
            //        OnTicketReceived = context =>
            //        {
            //            return OnTicketReceived(context);
            //        }
            //    }
            //});

            app.UseOpenIdConnectAuthentication(new OpenIdConnectOptions()
            {
                AuthenticationScheme = "oidc",
                AutomaticAuthenticate = true,
                AutomaticChallenge = true,
                SignInScheme = "Cookies",
                Authority = "https://accounts.google.com",
                ResponseType = "id_token",
                ClientId = "518663208756-do0306qmhi6k721v92m9bfaaknsain8s.apps.googleusercontent.com",
                ClientSecret = "U1igWAy15RKhnkJHMN5J_Y_3",
                CallbackPath = new PathString("/signin-google"),
                GetClaimsFromUserInfoEndpoint = true,

                Events = new OpenIdConnectEvents()
                {
                    OnRemoteFailure = context =>
                    {
                        return OnRemoteFailure(context);
                    },
                    OnTicketReceived = context =>
                    {
                        return OnTicketReceived(context);
                    }
                }
            });


            //app.UseGoogleAuthentication(new GoogleOptions()
            //{
            //    ClientId = "518663208756-do0306qmhi6k721v92m9bfaaknsain8s.apps.googleusercontent.com",
            //    ClientSecret = "U1igWAy15RKhnkJHMN5J_Y_3",
            //    //CallbackPath = new PathString("/signin-google"),
            //    SignInScheme = "Cookies",
            //    AutomaticAuthenticate = true,
            //    AutomaticChallenge = true,

            //    Events = new OAuthEvents()
            //    {
            //        OnRemoteFailure = context =>
            //        {
            //            return OnRemoteFailure(context);
            //        },
            //        OnTicketReceived = context =>
            //        {
            //            return OnTicketReceived(context);
            //        }
            //    }
            //});

            // route mvc
            app.UseMvc(routes =>
            {
                routes.MapRoute(
                    name: "default",
                    template: "{controller=Home}/{action=Index}/{id?}");
            });
        }

        private Task OnTicketReceived(TicketReceivedContext context)
        {
            if (context.Request.HasFormContentType)
            {
                String jwtTokenString = context.Request.Form["id_token"];
                //JwtSecurityToken jwtToken = new JwtSecurityToken(jwtTokenString);
                //context.Principal.AddIdentity(new ClaimsIdentity(jwtToken.Claims));

                var identity = context.Principal.Identity as ClaimsIdentity;
                if (identity != null)
                {
                    if (!context.Principal.HasClaim(c => c.Type == ClaimTypes.Name) &&
                        identity.HasClaim(c => c.Type == "name"))
                    {
                        identity.AddClaim(new Claim(ClaimTypes.Name, identity.FindFirst("name").Value));
                    }
                }

                context.HttpContext.Authentication.SignInAsync("Cookies", context.Principal);

                CookieOptions options = new CookieOptions();
                options.Expires = DateTime.Now.AddDays(1);
                context.Response.Cookies.Append("iPlanetDirectoryPro", jwtTokenString, options);
            }
            return Task.FromResult(0);
        }

        private Task OnRemoteFailure(FailureContext context)
        {
            context.Response.Redirect("/AccessDenied?error=" + context.Failure.Message);
            return Task.FromResult(0);
        }
    }
}
