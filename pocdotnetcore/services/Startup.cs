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
using Microsoft.AspNetCore.Authentication;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using System.Text;

namespace services
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

            //app.UseMiddleware<InspqOpenIdConnectMiddleware>();

            //app.UseCookieAuthentication(new CookieAuthenticationOptions()
            //{
            //    AuthenticationScheme = "Cookies",
            //    CookieName = "Cookies",
            //    AutomaticAuthenticate = true,
            //    ExpireTimeSpan = TimeSpan.FromMinutes(60)
            //});

            const string secretKey = "U1igWAy15RKhnkJHMN5J_Y_3";
            SymmetricSecurityKey signingKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secretKey));
            SigningCredentials signingCredentials = new SigningCredentials(signingKey, SecurityAlgorithms.RsaSha256);

            app.UseJwtBearerAuthentication(new JwtBearerOptions()
            {
                AutomaticChallenge = true,
                AutomaticAuthenticate = true,
                TokenValidationParameters = new TokenValidationParameters()
                {
                    AuthenticationType = "Bearer",
                    RequireSignedTokens = false,
                    ValidateActor = false,
                    ValidateIssuerSigningKey = true,
                    IssuerSigningKey = signingKey,
                    ValidateIssuer = false,
                    //ValidIssuer = "https://accounts.google.com",
                    ValidateAudience = false,
                    //ValidAudience = "518663208756-do0306qmhi6k721v92m9bfaaknsain8s.apps.googleusercontent.com",
                    ValidateLifetime = false
                },
                Events = new JwtBearerEvents()
                {
                    OnMessageReceived = context =>
                    {
                        return Task.FromResult(0);
                    },
                    OnChallenge = context =>
                    {
                        return Task.FromResult(0);
                    },
                    OnTokenValidated = context =>
                    {
                        return Task.FromResult(0);
                    }
                }
            });

            app.UseOpenIdConnectAuthentication(new OpenIdConnectOptions()
            {
                AuthenticationScheme = "oidc",
                AutomaticAuthenticate = true,
                //AutomaticChallenge = true,
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
                var identity = context.Principal.Identity as ClaimsIdentity;
                if (identity != null)
                {
                    if (!context.Principal.HasClaim(c => c.Type == ClaimTypes.Name) &&
                        identity.HasClaim(c => c.Type == "name"))
                    {
                        identity.AddClaim(new Claim(ClaimTypes.Name, identity.FindFirst("name").Value));
                    }
                }

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