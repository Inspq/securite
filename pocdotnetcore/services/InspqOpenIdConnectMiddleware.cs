using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;

namespace services
{
    public class InspqOpenIdConnectMiddleware
    {
        private readonly RequestDelegate _next;

        public InspqOpenIdConnectMiddleware(RequestDelegate next)
        {
            _next = next;
        }

        public async Task Invoke(HttpContext context)
        {
            string authHeader = context.Request.Headers["Authorization"];
            if (!String.IsNullOrEmpty(authHeader))
            {
                string jwtTokenString = authHeader.Substring("Bearer ".Length).Trim();
                JwtSecurityToken jwtToken = new JwtSecurityToken(jwtTokenString);

                context.User = new ClaimsPrincipal(new ClaimsIdentity(jwtToken.Claims));
                

                var identity = context.User.Identity as ClaimsIdentity;
                if (identity != null)
                {
                    if (!context.User.HasClaim(c => c.Type == ClaimTypes.Name) &&
                        identity.HasClaim(c => c.Type == "name"))
                    {
                        identity.AddClaim(new Claim(ClaimTypes.Name, identity.FindFirst("name").Value));
                    }
                }

                await _next(context);
            }
            return;
        }
    }
}
