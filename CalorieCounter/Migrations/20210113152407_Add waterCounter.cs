using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace CalorieCounter.Migrations
{
    public partial class AddwaterCounter : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "WaterCounters",
                columns: table => new
                {
                    UserId = table.Column<long>(type: "bigint", nullable: false),
                    WaterVolume = table.Column<int>(type: "integer", nullable: false),
                    DateOfDrink = table.Column<DateTime>(type: "timestamp without time zone", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_WaterCounters", x => x.UserId);
                    table.ForeignKey(
                        name: "FK_WaterCounters_AspNetUsers_UserId",
                        column: x => x.UserId,
                        principalTable: "AspNetUsers",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "WaterCounters");
        }
    }
}
