using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using CalorieCounter.Entities;
using CalorieCounter.Helpers;
using CalorieCounter.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.Features;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;

namespace CalorieCounter
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.Configure<FormOptions>(options =>
            {
                // Set the limit to 256 MB
                options.ValueCountLimit = 1024;
                options.KeyLengthLimit = 1024 * 2;
                options.ValueLengthLimit = 1024 * 1024 * 100;
            });

            services.AddCors();
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo
                {
                    Version = "v1",
                    Title = "YPS API",
                    Description = "A project  ASP.NET Core Web API",
                    TermsOfService = new Uri("https://example.com/terms"),
                    Contact = new OpenApiContact
                    {
                        Name = "Team YPS",
                        Email = string.Empty,
                    },

                });

                c.AddSecurityDefinition("Bearer",
                     new OpenApiSecurityScheme
                     {
                         Description = "JWT Authorization header using the Bearer scheme.",
                         Type = SecuritySchemeType.Http,
                         Scheme = "bearer"
                     });
                c.AddSecurityRequirement(new OpenApiSecurityRequirement{
                    {
                        new OpenApiSecurityScheme{
                            Reference = new OpenApiReference{
                                Id = "Bearer",
                                Type = ReferenceType.SecurityScheme
                            }
                        },new List<string>()
                    }
                });
                var xmlFile = $"{Assembly.GetExecutingAssembly().GetName().Name}.xml";
                var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);
                if (File.Exists(xmlPath))
                {
                    c.IncludeXmlComments(xmlPath);
                }

            });
            // Add framework services.
            services.AddDbContext<EFContext>(options =>
                options.UseNpgsql(Configuration.GetConnectionString("DefaultConnection")));

            services.AddIdentity<DbUser, DbRole>(options => options.Stores.MaxLengthForKeys = 128)
                .AddEntityFrameworkStores<EFContext>()
                .AddDefaultTokenProviders();

            services.AddTransient<IJwtTokenService, JwtTokenService>();
            // services.AddTransient<FileService, FileService>(); //!!!!!!!
            var signingKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Configuration.GetSection("SecretPhrase").Value));

            services.AddAuthentication(options =>
            {
                options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
                options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;


                //options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                //options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
            })
                  .AddJwtBearer(cfg =>
            {
                cfg.RequireHttpsMetadata = false;
                cfg.SaveToken = true;
                cfg.TokenValidationParameters = new TokenValidationParameters()
                {
                    IssuerSigningKey = signingKey,
                    ValidateAudience = false,
                    ValidateIssuer = false,
                    ValidateLifetime = true,
                    ValidateIssuerSigningKey = true,
                    ClockSkew = TimeSpan.Zero
                };
            });
            
            //.AddGoogle(opts =>
            //{
            //    opts.ClientId = "951922290898-tcl5ru7u75skn3cr52rfjnq98lc6n4ln.apps.googleusercontent.com";
            //    opts.ClientSecret = "3wYt99Y94rB0jzqIB66V7bXA";
            //    opts.SignInScheme = IdentityConstants.ExternalScheme;
            //});
            services.AddControllers();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            app.UseCors(
              builder => builder.AllowAnyHeader().AllowAnyMethod().AllowAnyOrigin());
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            #region  InitStaticFiles Images
            string pathRoot = InitStaticFiles
                .CreateFolderServer(env, this.Configuration,
                    new string[] { "ImagesPath" });

            app.UseStaticFiles(new StaticFileOptions()
            {
                FileProvider = new PhysicalFileProvider(pathRoot),
                RequestPath = new PathString('/' + Configuration.GetValue<string>("UrlImages"))
            });
            #endregion
            app.UseRouting();
            app.UseAuthentication();
            app.UseAuthorization();
            app.UseSwagger();
            app.UseSwaggerUI(c =>
            {
                c.SwaggerEndpoint("/swagger/v1/swagger.json", "My API V1");
            });

            SeederDB.SeedDataByAS(app.ApplicationServices);
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
