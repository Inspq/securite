using System;
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
using System.Security.Cryptography;

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
            string authority = "http://login.bicycle2.inspq.qc.ca:18080/openam/oauth2/";
            if (!String.IsNullOrEmpty(Environment.GetEnvironmentVariable("OPENAM_URL")))
            {
                authority = Environment.GetEnvironmentVariable("OPENAM_URL") + "/oauth2/";
            }

            string clientId = Environment.GetEnvironmentVariable("OIDC_CLIENT_ID");
            if (String.IsNullOrEmpty(clientId))
            { 
                clientId = "sx5dotnetrestoidcmathieu";
            }

            string clientSecret = Environment.GetEnvironmentVariable("OIDC_CLIENT_SECRET");
            if (String.IsNullOrEmpty(clientSecret))
            {
                clientSecret = "Pan0rama";
            }

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            //app.UseMiddleware<InspqOpenIdConnectMiddleware>();

            app.UseCookieAuthentication(new CookieAuthenticationOptions()
            {
                AuthenticationScheme = "Cookies",
                CookieName = "Cookies",
                AutomaticAuthenticate = true,
                ExpireTimeSpan = TimeSpan.FromMinutes(60)
            });

            //app.UseJwtBearerAuthentication(new JwtBearerOptions()
            //{
            //    AutomaticChallenge = true,
            //    AutomaticAuthenticate = true,
            //    Authority = "https://accounts.google.com",
            //    TokenValidationParameters = new TokenValidationParameters()
            //    {
            //        ValidateIssuerSigningKey = true,
            //        ValidateIssuer = true,
            //        ValidIssuer = "https://accounts.google.com",
            //        ValidateAudience = true,
            //        ValidAudience = "518663208756-do0306qmhi6k721v92m9bfaaknsain8s.apps.googleusercontent.com",
            //        ValidateLifetime = true
            //    },
            //    Events = new JwtBearerEvents()
            //    {
            //        OnTokenValidated = context =>
            //        {
            //            String jwtTokenString = context.SecurityToken.ToString();
            //            var identity = context.Ticket.Principal.Identity as ClaimsIdentity;
            //            if (identity != null)
            //            {
            //                if (!context.Ticket.Principal.HasClaim(c => c.Type == ClaimTypes.Name) &&
            //                    identity.HasClaim(c => c.Type == "name"))
            //                {
            //                    identity.AddClaim(new Claim(ClaimTypes.Name, identity.FindFirst("name").Value));
            //                }
            //            }

            //            CookieOptions options = new CookieOptions();
            //            options.Expires = DateTime.Now.AddDays(1);
            //            context.Response.Cookies.Append("iPlanetDirectoryPro", jwtTokenString, options);
            //            return Task.FromResult(0);
            //        }
            //    }
            //});

            app.UseJwtBearerAuthentication(new JwtBearerOptions()
            {
                AutomaticChallenge = true,
                AutomaticAuthenticate = true,
                Authority = authority,
                RequireHttpsMetadata = false,
                TokenValidationParameters = new TokenValidationParameters()
                {
                    ValidateIssuerSigningKey = true,
                    ValidateIssuer = true,
                    ValidIssuer = authority,
                    ValidateAudience = false,
                    //ValidAudience = "sx5dotnetuioidc",
                    ValidateLifetime = true
                },
                Events = new JwtBearerEvents()
                {
                    OnTokenValidated = context =>
                    {
                        String jwtTokenString = context.SecurityToken.ToString();
                        var identity = context.Ticket.Principal.Identity as ClaimsIdentity;
                        if (identity != null)
                        {
                            if (!context.Ticket.Principal.HasClaim(c => c.Type == ClaimTypes.Name) &&
                                identity.HasClaim(c => c.Type == "name"))
                            {
                                identity.AddClaim(new Claim(ClaimTypes.Name, identity.FindFirst("name").Value));
                            }
                        }

                        CookieOptions options = new CookieOptions();
                        options.Expires = DateTime.Now.AddDays(1);
                        context.Response.Cookies.Append("iPlanetDirectoryPro", jwtTokenString, options);
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
                Authority = authority,
                RequireHttpsMetadata = false,
                ResponseType = "id_token",
                ClientId = clientId,
                ClientSecret = clientSecret,
                CallbackPath = new PathString("/login"),
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

            //app.UseOpenIdConnectAuthentication(new OpenIdConnectOptions()
            //{
            //    AuthenticationScheme = "oidc",
            //    AutomaticAuthenticate = true,
            //    //AutomaticChallenge = true,
            //    SignInScheme = "Cookies",
            //    Authority = "https://accounts.google.com",
            //    ResponseType = "id_token",
            //    ClientId = "518663208756-do0306qmhi6k721v92m9bfaaknsain8s.apps.googleusercontent.com",
            //    ClientSecret = "U1igWAy15RKhnkJHMN5J_Y_3",
            //    CallbackPath = new PathString("/signin-google"),
            //    GetClaimsFromUserInfoEndpoint = true,

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
